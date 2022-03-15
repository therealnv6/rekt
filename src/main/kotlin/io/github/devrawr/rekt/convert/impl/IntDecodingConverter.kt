package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.DecodingConverter

object IntDecodingConverter : DecodingConverter<Int>
{
    override fun convert(data: ByteArray?): Int?
    {
        return LongDecodingConverter.convert(data)?.toInt()
    }
}