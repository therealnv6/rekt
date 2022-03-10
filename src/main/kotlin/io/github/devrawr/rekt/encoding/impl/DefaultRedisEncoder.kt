package io.github.devrawr.rekt.encoding.impl

import io.github.devrawr.rekt.ARRAY
import io.github.devrawr.rekt.BULK_STRING
import io.github.devrawr.rekt.CRLF
import io.github.devrawr.rekt.encoding.Encoder
import java.io.OutputStream

object DefaultRedisEncoder : Encoder
{
    override fun write(stream: OutputStream, value: Any)
    {
        when (value)
        {
            is ByteArray -> write(stream, value)
            is String -> write(stream, value.toByteArray())
            is Long -> write(stream, value)
            is Int -> write(stream, value.toLong())
            is List<*> -> write(stream, value)
            else -> throw IllegalStateException("Wrong type provided, ${value.javaClass.name}")
        }
    }

    override fun write(stream: OutputStream, value: ByteArray)
    {
        stream.write(BULK_STRING)
        stream.write(value.size.toString().toByteArray())
        stream.write(CRLF)
        stream.write(value)
        stream.write(CRLF)
    }

    override fun write(stream: OutputStream, value: Long)
    {
        // TODO: 3/10/2022 fix RESP integer writing. temporarily just writing as bulk string.
        write(stream, value.toString())

        // doesn't work - java.net.SocketException: Broken pipe (Write failed) on next call.
//        stream.write(INTEGER)
//        stream.write(value.toString().encodeToByteArray())
//        stream.write(CRLF)
    }

    override fun write(stream: OutputStream, value: List<*>)
    {
        stream.write(ARRAY)
        stream.write(value.size.toString().toByteArray())
        stream.write(CRLF)

        value.forEach {
            if (it != null)
            {
                write(stream, it)
            }
        }
    }
}