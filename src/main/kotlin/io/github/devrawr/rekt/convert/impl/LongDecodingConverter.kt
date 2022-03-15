package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.DecodingConverter
import java.nio.ByteBuffer

object LongDecodingConverter : DecodingConverter<Long>
{
    override fun convert(data: ByteArray?): Long?
    {
        if (data == null)
        {
            return null
        }

        return try
        {
            ByteBuffer.wrap(data).long
        } catch (ignored: Exception)
        {
            data.decodeToString().toLongOrNull()
        }
    }
}