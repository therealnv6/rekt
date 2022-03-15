package io.github.devrawr.rekt.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.convert.RESPDecodingConverter

object ErrorDecoder : RESPDecodingConverter<Any>
{
    override fun convert(connection: RedisConnection): Any?
    {
        throw Exception(
            this.readString(connection.input).decodeToString(), Error("")
        )
    }
}