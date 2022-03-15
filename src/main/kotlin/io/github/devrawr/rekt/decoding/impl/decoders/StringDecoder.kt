package io.github.devrawr.rekt.decoding.impl.decoders

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.convert.RESPDecodingConverter

object StringDecoder : RESPDecodingConverter<String>
{
    override fun convert(connection: RedisConnection): String
    {
        return this.readString(connection.input).decodeToString()
    }
}