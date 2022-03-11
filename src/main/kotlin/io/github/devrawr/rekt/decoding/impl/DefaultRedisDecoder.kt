package io.github.devrawr.rekt.decoding.impl

import io.github.devrawr.rekt.*
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.decoding.exception.ByteLayoutException
import java.io.InputStream

object DefaultRedisDecoder : Decoder
{
    override fun decode(stream: InputStream): Any?
    {
        if (stream.available() < 0)
        {
            return null
        }

        return when (stream.read())
        {
            STRING, INTEGER -> readString(stream)
            ARRAY ->
            {
                val length = readString(stream)
                    .decodeToString()
                    .toLongOrNull()

                return if (length == null || length == -1L)
                {
                    null
                } else
                {
                    (0..length)
                        .map { this.decode(stream) }
                        .toList()
                }
            }
            BULK_STRING ->
            {
                val length = readString(stream)
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
                    index += stream.read(buffer, index, length - index)
                }

                if (stream.read() != '\r'.code || stream.read() != '\n'.code)
                {
                    throw ByteLayoutException("CRLF scheme is not formatted properly!")
                }

                return buffer
            }

            ERROR -> throw Exception(readString(stream).decodeToString(), Error(""))
            else -> null
        }
    }

    override fun readString(stream: InputStream): ByteArray
    {
        var size = 1024
        var index = 0
        var buffer = ByteArray(size)

        var char: Char?

        while (true)
        {
            char = stream.read().toChar()

            if (char == '\r')
            {
                break
            }

            buffer[index++] = char.code.toByte()

            if (index == size)
            {
                size *= 2
                buffer = buffer.copyOf(size * 2)
            }
        }

        if (stream.read() != '\n'.code)
        {
            throw ByteLayoutException("CRLF scheme is not formatted properly!")
        }

        return buffer
            .copyOfRange(0, index)
    }
}