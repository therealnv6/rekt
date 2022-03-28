package resp.decoding.impl.decoders

import RedisConnection
import resp.RESPDecodingConverter

object ErrorDecoder : RESPDecodingConverter<Any>
{
    override suspend fun convert(connection: RedisConnection): Any?
    {
        throw Exception(
            this.readString(connection.readChannel).decodeToString(), Error("")
        )
    }
}