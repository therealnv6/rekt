package io.github.devrawr.rekt

import io.github.devrawr.rekt.convert.Converters
import io.github.devrawr.rekt.encoding.Encoder
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.pubsub.Subscriber
import io.github.devrawr.rekt.pubsub.DataStream
import java.io.*

class RedisConnection(
    private val input: InputStream,
    private val output: OutputStream,
    private val dataStream: DataStream,
    private val decoder: Decoder,
    private val encoder: Encoder
)
{
    @JvmName("hgetInline")
    inline fun <reified T : Any> hget(key: String): T? = hget(key, T::class.java)

    @JvmName("hgetInline")
    inline fun <reified T : Any> hget(hash: String, key: String) = hget(hash, key, T::class.java)

    @JvmName("getInline")
    inline fun <reified T : Any> get(key: String): T? = get(key, T::class.java)

    inline fun <reified T : Any> hgetAll(hash: String): List<T?> = hgetAll(hash, T::class.java)

    inline fun <reified T : Any> callReturnRead(vararg args: Any): T? = callReturnRead(args.toList())
    inline fun <reified T : Any> callReturnRead(args: List<*>): T? = callReturnRead(T::class.java, args)

    fun <T : Any> callReturnRead(type: Class<T>, vararg args: Any): T? = callReturnRead(type, args.toList())

    fun call(vararg args: Any) = call(args.toList())

    fun call(args: List<*>)
    {
        encoder.write(output, args)
        output.flush()

        this.read(ByteArray::class.java) // even though we're not returning this, we're still reading to clear the buffer!
    }

    fun <T : Any> callReturnRead(type: Class<T>, args: List<*>): T?
    {
        // don't call this.call(List<*>), we want to handle this.read() on our own,
        // but because call(List<*>) already calls this.read(), the buffer will be cleared,
        // causing us to not receive any data.
        encoder.write(output, args)
        output.flush()

        return this.read(type)
    }

    fun set(key: String, value: Any)
    {
        call(ByteArray::class.java, "SET", key, value)
    }

    fun <T : Any> get(key: String, type: Class<T>): T?
    {
        return callReturnRead(type, "GET", key)
    }

    fun get(key: String): ByteArray?
    {
        return get<ByteArray>(key)
    }

    fun hset(hash: String, key: String, value: Any)
    {
        call("HSET", hash, key, value)
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
        return callReturnRead(type, "HGET", hash, key)
    }

    fun hdel(hash: String, key: String)
    {
        call("HDEL", hash, key)
    }

    fun <T : Any> hgetAll(hash: String, type: Class<T>): List<T?>
    {
        val call = callReturnRead<List<ByteArray>>("HGETALL", hash) ?: return emptyList()

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
        call("PUBLISH", channel, message)
    }

    fun subscribe(subscriber: Subscriber, vararg channel: String)
    {
        this.dataStream.subscribe(this, subscriber, *channel)
    }

    fun pSubscribe(subscriber: Subscriber, vararg pattern: String)
    {
        this.dataStream.pSubscribe(this, subscriber, *pattern)
    }

    fun subscribe(vararg channel: String, subscriber: (message: String) -> Unit)
    {
        subscribe(
            subscriber = object : Subscriber
            {
                override fun handleIncoming(channel: String, message: String)
                {
                    subscriber.invoke(message)
                }
            },
            channel = channel
        )
    }

    fun pSubscribe(vararg pattern: String, subscriber: (message: String) -> Unit)
    {
        this.pSubscribe(
            subscriber = object : Subscriber
            {
                override fun handleIncoming(channel: String, message: String)
                {
                    subscriber.invoke(message)
                }
            },
            pattern = pattern
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