package io.github.devrawr.rekt

import io.github.devrawr.rekt.convert.Converters
import io.github.devrawr.rekt.encoding.Encoder
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.pubsub.Subscriber
import java.io.*
import java.net.Socket

class RedisConnection(
    socket: Socket,
    private val decoder: Decoder,
    private val encoder: Encoder
)
{
    private val input: InputStream
    private val output: OutputStream

    init
    {
        this.input = socket.getInputStream()
        this.output = socket.getOutputStream()
    }

    inline fun <reified T : Any> call(vararg args: Any): T? = call(T::class.java, args)

    fun <T : Any> call(type: Class<T>, vararg args: Any): T?
    {
        encoder.write(output, args.toList())
        output.flush()

        return this.read(type)
    }

    fun set(key: String, value: Any)
    {
        call(ByteArray::class.java, "SET", key, value)
    }

    @JvmName("getInline")
    inline fun <reified T : Any> get(key: String): T? = get(key, T::class.java)

    fun <T : Any> get(key: String, type: Class<T>): T?
    {
        return call(type, "GET", key)
    }

    fun get(key: String): ByteArray?
    {
        return get<ByteArray>(key)
    }

    fun hset(hash: String, key: String, value: Any)
    {
        call(ByteArray::class.java, "HSET", hash, key, value)
    }

    fun hset(key: String, value: Any)
    {
        val split = key.split("/")

        if (split.size != 2)
        {
            throw IllegalArgumentException("Provided key is $key, must be formatted like 'hash/key'")
        }

        hset(split[0], split[1], value)
    }

    @JvmName("hgetInline")
    inline fun <reified T : Any> hget(key: String): T?
    {
        val split = key.split("/")

        if (split.size != 2)
        {
            throw IllegalArgumentException("Provided key is $key, must be formatted like 'hash/key'")
        }

        return hget(split[0], split[1])
    }

    @JvmName("hgetInline")
    inline fun <reified T : Any> hget(hash: String, key: String) = hget(hash, key, T::class.java)

    fun <T : Any> hget(key: String, type: Class<T>): T?
    {
        val split = key.split("/")

        if (split.size != 2)
        {
            throw IllegalArgumentException("Provided key is $key, must be formatted like 'hash/key'")
        }

        return hget(split[0], split[1], type)
    }

    fun <T : Any> hget(hash: String, key: String, type: Class<T>): T?
    {
        return call(type, "HGET", hash, key)
    }

    fun hdel(hash: String, key: String)
    {
        call<ByteArray>("HDEL", hash, key)
    }

    inline fun <reified T : Any> hgetAll(hash: String): List<T?> = hgetAll(hash, T::class.java)

    fun <T : Any> hgetAll(hash: String, type: Class<T>): List<T?>
    {
        val call = call<List<ByteArray>>("HGETALL", hash) ?: return emptyList()

        return call.map {
            val converter = Converters.retrieveConverter(type)

            if (converter == null)
            {
                it as T
            } else
            {
                converter.convert(it)!!
            }
        }
    }

    fun publish(channel: String, message: String)
    {
        call<ByteArray>("PUBLISH", channel, message)
    }

    fun subscribe(subscriber: Subscriber, vararg channel: String)
    {
        val handled = hashMapOf<String, Int>()

        while (true)
        {
            val result = call<List<*>>("SUBSCRIBE", *channel)
                ?: continue

            for (entries in result.withIndex())
            {
                val entry = entries.value

                if (entry != null && entry is ByteArray)
                {
                    val content = entry.decodeToString()

                    if (handled.containsKey(content) && handled[content] == entries.index)
                    {
                        continue
                    }

                    for (string in channel)
                    {
                        subscriber.handleIncoming(string, content)
                    }

                    // TODO: 3/10/2022 better solution for this, not entirely sure how this works.
                    handled[content] = entries.index
                }
            }
        }
    }

    fun subscribe(subscriber: (channel: String, message: String) -> Unit, vararg channel: String)
    {
        subscribe(
            subscriber = object : Subscriber
            {
                override fun handleIncoming(channel: String, message: String)
                {
                    subscriber.invoke(channel, message)
                }
            },
            channel = channel
        )
    }

    fun subscribe(vararg channel: String, subscriber: (message: String) -> Unit)
    {
        subscribe(
            subscriber = { _, message ->
                subscriber.invoke(message)
            },
            channel = channel
        )
    }

    private fun <T : Any> read(type: Class<T>): T?
    {
        val converter = Converters.retrieveConverter(type)
        val decoded = decoder.decode(input)
            ?: return null

        val converted = try
        {
            decoded as T
        } catch (ignored: java.lang.ClassCastException)
        {
            decoded as ByteArray
        }

        return if (converted is ByteArray)
        {
            if (converter == null)
            {
                converted as T
            } else
            {
                converter.convert(converted)
            }
        } else
        {
            converted as T?
        }
    }
}
