package resp.decoding.impl.decoders

import RedisConnection
import resp.RESPDecodingConverter

object StringDecoder : RESPDecodingConverter<String>
{
    override suspend fun convert(connection: RedisConnection): String
    {
        return this.readString(connection.readChannel).decodeToString()
    }
}