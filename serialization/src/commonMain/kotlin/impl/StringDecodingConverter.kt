package impl

import DecodingConverter

object StringDecodingConverter : DecodingConverter<String>
{
    override fun convert(data: ByteArray?): String?
    {
        return data?.decodeToString()
    }
}