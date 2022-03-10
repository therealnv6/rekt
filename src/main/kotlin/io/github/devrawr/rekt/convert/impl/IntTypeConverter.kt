package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.TypeConverter

object IntTypeConverter : TypeConverter<Int>
{
    override fun convert(data: ByteArray?): Int?
    {
        return LongTypeConverter.convert(data)?.toInt()
    }
}