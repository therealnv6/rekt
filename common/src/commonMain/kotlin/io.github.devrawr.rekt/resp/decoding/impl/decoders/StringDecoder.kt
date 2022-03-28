package io.github.devrawr.rekt.resp.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.resp.RESPDecodingConverter

object StringDecoder : RESPDecodingConverter<String>
{
    override suspend fun convert(connection: RedisConnection): String
    {
        return this.readString(connection.readChannel).decodeToString()
    }
}