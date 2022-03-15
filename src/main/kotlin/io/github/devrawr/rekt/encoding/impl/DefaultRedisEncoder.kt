package io.github.devrawr.rekt.encoding.impl

import io.github.devrawr.rekt.ARRAY
import io.github.devrawr.rekt.BULK_STRING
import io.github.devrawr.rekt.CRLF
import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.encoding.Encoder
import java.io.OutputStream

object DefaultRedisEncoder : Encoder
{
    override fun write(connection: RedisConnection, value: ByteArray)
    {
        val stream = connection.output

        stream.write(BULK_STRING)
        stream.write(value.size.toString().toByteArray())
        stream.write(CRLF)
        stream.write(value)
        stream.write(CRLF)
    }

    override fun write(connection: RedisConnection, value: Long)
    {
        // TODO: 3/10/2022 fix RESP integer writing. temporarily just writing as bulk string.
        write(connection, value.toString())
    }

    override fun write(connection: RedisConnection, value: List<*>)
    {
        val stream = connection.output

        stream.write(ARRAY)
        stream.write(value.size.toString().toByteArray())
        stream.write(CRLF)

        value.forEach {
            if (it != null)
            {
                write(connection, it)
            }
        }
    }
}