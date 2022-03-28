package resp

import RedisConnection
import resp.decoding.exception.ByteLayoutException
import io.ktor.utils.io.*

interface RESPDecodingConverter<T>
{
    suspend fun convert(connection: RedisConnection): T?

    suspend fun readString(readChannel: ByteReadChannel): ByteArray
    {
        var size = 1024
        var index = 0
        var buffer = ByteArray(size)

        var char: Char?

        while (true)
        {
            char = readChannel.readInt().toChar()

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

        if (readChannel.readInt() != '\n'.code)
        {
            throw ByteLayoutException("CRLF scheme is not formatted properly!")
        }

        return buffer
            .copyOfRange(0, index)
    }
}