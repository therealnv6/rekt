package io.github.devrawr.rekt.resp.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.resp.RESPDecodingConverter

object NumberDecoder : RESPDecodingConverter<Long>
{
    override suspend fun convert(connection: RedisConnection): Long?
    {
        return this.readString(connection.readChannel)
            .decodeToString()
            .toLongOrNull()
    }
}