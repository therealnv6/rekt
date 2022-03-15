package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.decoding.exception.ByteLayoutException
import java.io.InputStream

interface RESPDecodingConverter<T>
{
    fun convert(connection: RedisConnection): T?

    fun readString(stream: InputStream): ByteArray
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