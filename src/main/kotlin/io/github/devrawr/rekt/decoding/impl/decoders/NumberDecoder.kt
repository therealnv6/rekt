package io.github.devrawr.rekt.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.convert.RESPDecodingConverter

object NumberDecoder : RESPDecodingConverter<Long>
{
    override fun convert(connection: RedisConnection): Long?
    {
        return this.readString(connection.input)
            .decodeToString()
            .toLongOrNull()
    }
}