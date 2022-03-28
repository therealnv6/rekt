package io.github.devrawr.rekt.common.resp.decoding.impl.decoders

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.RESPDecodingConverter

object StringDecoder : RESPDecodingConverter<String>
{
    override suspend fun convert(connection: RedisConnection): String
    {
        return this.readString(connection.readChannel).decodeToString()
    }
}