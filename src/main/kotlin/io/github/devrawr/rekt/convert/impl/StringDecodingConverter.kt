package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.DecodingConverter

object StringDecodingConverter : DecodingConverter<String>
{
    override fun convert(data: ByteArray?): String?
    {
        return data?.decodeToString()
    }
}