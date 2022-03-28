package impl

import DecodingConverter

object LongDecodingConverter : DecodingConverter<Long>
{
    override fun convert(data: ByteArray?): Long?
    {
        return data?.decodeToString()?.toLongOrNull()
    }
}