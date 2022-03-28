package io.github.devrawr.rekt.common.resp.decoding.impl.decoders

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.RESPDecodingConverter

object NumberDecoder : RESPDecodingConverter<Long>
{
    override suspend fun convert(connection: RedisConnection): Long?
    {
        return this.readString(connection.readChannel)
            .decodeToString()
            .toLongOrNull()
    }
}