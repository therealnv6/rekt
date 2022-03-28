package io.github.devrawr.rekt.resp.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.resp.RESPDecodingConverter

object ArrayDecoder : RESPDecodingConverter<List<*>>
{
    override suspend fun convert(connection: RedisConnection): List<*>?
    {
        val length = this.readString(connection.readChannel)
            .decodeToString()
            .toLongOrNull()

        return if (length == null || length == -1L)
        {
            null
        } else
        {
            (0..length)
                .map {
                    connection.decoder.decode(connection)
                }
                .toList()
        }
    }
}