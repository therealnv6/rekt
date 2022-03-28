package io.github.devrawr.rekt.common.resp.encoding.impl

import ARRAY
import BULK_STRING
import INTEGER
import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.encoding.Encoder
import io.ktor.utils.io.*

object DefaultRedisEncoder : Encoder
{
    override suspend fun write(connection: RedisConnection, value: ByteArray)
    {
        connection
            .writeChannel
            .writeStringUtf8(
                "${BULK_STRING}${value.size}\r\n${value}\r\n"
            )
    }

    override suspend fun write(connection: RedisConnection, value: Long)
    {
        connection
            .writeChannel
            .writeStringUtf8(
                "${INTEGER}${value}\r\n"
            )
    }

    override suspend fun write(connection: RedisConnection, value: List<*>)
    {
        connection
            .writeChannel
            .writeStringUtf8(
                "${ARRAY}${value.size}\r\n"
            )

        value.forEach {
            if (it != null)
            {
                write(connection, it)
            }
        }
    }
}