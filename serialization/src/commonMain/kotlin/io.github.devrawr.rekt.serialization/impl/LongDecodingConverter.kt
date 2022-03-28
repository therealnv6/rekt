package io.github.devrawr.rekt.serialization.impl

import io.github.devrawr.rekt.serialization.DecodingConverter

object LongDecodingConverter : DecodingConverter<Long>
{
    override fun convert(data: ByteArray?): Long?
    {
        return data?.decodeToString()?.toLongOrNull()
    }
}