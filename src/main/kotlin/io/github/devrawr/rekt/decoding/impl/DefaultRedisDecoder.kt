package io.github.devrawr.rekt.decoding.impl

import io.github.devrawr.rekt.*
import io.github.devrawr.rekt.convert.RESPDecodingConverter
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.decoding.impl.decoders.*

object DefaultRedisDecoder : Decoder
{
    private val decoders = hashMapOf<Int, RESPDecodingConverter<*>>(
        STRING to StringDecoder,
        INTEGER to NumberDecoder,
        ARRAY to ArrayDecoder,
        BULK_STRING to BulkStringDecoder,
        ERROR to ErrorDecoder
    )

    override fun decode(connection: RedisConnection): Any?
    {
        if (connection.input.available() < 0)
        {
            return null
        }

        return decoders[connection.input.read()]?.convert(connection)
    }
}