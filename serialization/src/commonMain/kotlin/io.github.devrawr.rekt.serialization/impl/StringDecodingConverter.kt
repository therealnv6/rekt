package io.github.devrawr.rekt.serialization.impl

import io.github.devrawr.rekt.serialization.DecodingConverter

object StringDecodingConverter : DecodingConverter<String>
{
    override fun convert(data: ByteArray?): String?
    {
        return data?.decodeToString()
    }
}