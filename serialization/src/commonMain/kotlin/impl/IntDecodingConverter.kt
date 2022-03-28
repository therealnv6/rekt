package impl

import DecodingConverter

object IntDecodingConverter : DecodingConverter<Int>
{
    override fun convert(data: ByteArray?): Int?
    {
        return LongDecodingConverter.convert(data)?.toInt()
    }
}