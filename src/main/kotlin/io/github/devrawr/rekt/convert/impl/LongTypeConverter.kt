package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.TypeConverter
import java.nio.ByteBuffer

object LongTypeConverter : TypeConverter<Long>
{
    override fun convert(data: ByteArray): Long
    {
        return try
        {
            ByteBuffer.wrap(data).long
        } catch (ignored: Exception)
        {
            data.decodeToString().toLong()
        }
    }
}