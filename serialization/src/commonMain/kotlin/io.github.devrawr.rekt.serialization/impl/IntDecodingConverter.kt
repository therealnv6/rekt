package io.github.devrawr.rekt.serialization.impl

import io.github.devrawr.rekt.serialization.DecodingConverter

object IntDecodingConverter : DecodingConverter<Int>
{
    override fun convert(data: ByteArray?): Int?
    {
        return LongDecodingConverter.convert(data)?.toInt()
    }
}