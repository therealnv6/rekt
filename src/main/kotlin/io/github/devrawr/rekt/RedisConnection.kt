package io.github.devrawr.rekt

import io.github.devrawr.rekt.convert.Converters
import io.github.devrawr.rekt.encoding.Encoder
import io.github.devrawr.rekt.decoding.Decoder
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

    fun <T> call(vararg args: Any): T
    {
        encoder.write(output, args.toList())
        output.flush()

        return this.read()
    }

    fun set(key: String, value: Any)
    {
        call<ByteArray>("SET", key, value)
    }

    @JvmName("getInline")
    inline fun <reified T> get(key: String): T = get(key, T::class.java)

    fun <T> get(key: String, type: Class<T>): T
    {
        val call = call<ByteArray>("GET", key)
        val converter = Converters.retrieveConverter(type)

        return if (converter == null)
        {
            call as T
        } else
        {
            converter.convert(call)!!
        }
    }

    fun get(key: String): ByteArray
    {
        return get<ByteArray>(key)
    }

    fun hset(hash: String, key: String, value: Any)
    {
        call<ByteArray>("HSET", hash, key, value)
    }

    @JvmName("hgetInline")
    inline fun <reified T> hget(hash: String, key: String) = hget(hash, key, T::class.java)

    fun <T> hget(hash: String, key: String, type: Class<T>): T
    {
        val call = call<ByteArray>("HGET", hash, key)
        val converter = Converters.retrieveConverter(type)

        return if (converter == null)
        {
            call as T
        } else
        {
            converter.convert(call)!!
        }
    }

    fun hdel(hash: String, key: String)
    {
        call<ByteArray>("HDEL", hash, key)
    }

    fun <T> hgetAll(hash: String, type: Class<T>): List<T>
    {
        val call = call<List<ByteArray>>("HGETALL", hash)

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

    fun <T> read(): T
    {
        return decoder.decode(input) as T
    }
}