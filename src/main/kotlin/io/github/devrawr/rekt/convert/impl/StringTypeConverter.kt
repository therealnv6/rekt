package io.github.devrawr.rekt.convert.impl

import io.github.devrawr.rekt.convert.TypeConverter

object StringTypeConverter : TypeConverter<String>
{
    override fun convert(data: ByteArray): String
    {
        return data.decodeToString()
    }
}