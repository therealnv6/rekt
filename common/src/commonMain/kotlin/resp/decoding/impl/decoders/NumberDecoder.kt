package resp.decoding.impl.decoders

import RedisConnection
import resp.RESPDecodingConverter

object NumberDecoder : RESPDecodingConverter<Long>
{
    override suspend fun convert(connection: RedisConnection): Long?
    {
        return this.readString(connection.readChannel)
            .decodeToString()
            .toLongOrNull()
    }
}