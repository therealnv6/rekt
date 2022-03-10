package io.github.devrawr.rekt.decoding.impl

import io.github.devrawr.rekt.*
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.decoding.exception.ByteLayoutException
import java.io.InputStream
import java.rmi.ServerError

object DefaultRedisDecoder : Decoder
{
    override fun decode(stream: InputStream): Any?
    {
        return when (stream.read())
        {
            STRING -> readString(stream)
            ERROR -> throw ServerError(readString(stream), Error(""))
            INTEGER -> readString(stream).toLong()
            ARRAY ->
            {
                val length = readString(stream).toLongOrNull()

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
                val length = readString(stream).toIntOrNull()

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

            else -> null
        }
    }

    override fun readString(stream: InputStream): String
    {
        var index = 0
        val buffer = ByteArray(1024)

        var char: Char?

        while (true)
        {
            char = stream.read().toChar()

            if (char == '\r')
            {
                break
            }

            buffer[index++] = char.code.toByte()
        }

        if (stream.read() != '\n'.code)
        {
            throw ByteLayoutException("CRLF scheme is not formatted properly!")
        }

        return buffer.copyOfRange(0, index).decodeToString()
    }
}