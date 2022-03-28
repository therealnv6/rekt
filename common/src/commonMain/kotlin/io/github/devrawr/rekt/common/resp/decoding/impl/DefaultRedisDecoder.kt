package io.github.devrawr.rekt.common.resp.decoding.impl

import ARRAY
import BULK_STRING
import ERROR
import INTEGER
import STRING

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.RESPDecodingConverter
import io.github.devrawr.rekt.common.resp.decoding.Decoder
import io.github.devrawr.rekt.common.resp.decoding.impl.decoders.*

object DefaultRedisDecoder : Decoder
{
    private val decoders = hashMapOf<Int, RESPDecodingConverter<*>>(
        STRING.code to StringDecoder,
        INTEGER.code to NumberDecoder,
        ARRAY.code to ArrayDecoder,
        BULK_STRING.code to BulkStringDecoder,
        ERROR.code to ErrorDecoder
    )

    override suspend fun decode(connection: RedisConnection): Any?
    {
        val readChannel = connection.readChannel

        if (readChannel.isClosedForRead)
        {
            return null
        }

        return decoders[readChannel.readInt()]?.convert(connection)
    }
}