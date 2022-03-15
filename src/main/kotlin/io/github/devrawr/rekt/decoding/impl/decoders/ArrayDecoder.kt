package io.github.devrawr.rekt.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.convert.RESPDecodingConverter

object ArrayDecoder : RESPDecodingConverter<List<*>>
{
    override fun convert(connection: RedisConnection): List<*>?
    {
        val length = this.readString(connection.input)
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