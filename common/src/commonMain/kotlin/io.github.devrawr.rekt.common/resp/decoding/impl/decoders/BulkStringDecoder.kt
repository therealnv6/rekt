package io.github.devrawr.rekt.common.resp.decoding.impl.decoders

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.resp.RESPDecodingConverter
import io.github.devrawr.rekt.common.resp.decoding.exception.ByteLayoutException

object BulkStringDecoder : RESPDecodingConverter<ByteArray>
{
    override suspend fun convert(connection: RedisConnection): ByteArray?
    {
        val readChannel = connection.readChannel
        val length = this.readString(readChannel)
            .decodeToString()
            .toIntOrNull()

        if (length == null // failed to parse the number, probably a wrongly formatted string, or length is higher than the 32-bit integer limit
            || length == -1 // Null Bulk String
        )
        {
            return null // Null Bulk String, just return null (I guess?)
        }

        val buffer = ByteArray(length)
        var index = 0

        while (index < length)
        {
            index += readChannel.readAvailable(buffer, index, length - index)
        }

        if (readChannel.readInt() != '\r'.code || readChannel.readInt() != '\n'.code)
        {
            throw ByteLayoutException("CRLF scheme is not formatted properly!")
        }

        return buffer
    }
}