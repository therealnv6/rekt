package io.github.devrawr.rekt.common.resp.decoding.impl.decoders

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.RESPDecodingConverter

object ErrorDecoder : RESPDecodingConverter<Any>
{
    override suspend fun convert(connection: RedisConnection): Any?
    {
        throw Exception(
            this.readString(connection.readChannel).decodeToString(), Error("")
        )
    }
}