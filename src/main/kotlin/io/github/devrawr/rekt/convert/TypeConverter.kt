package io.github.devrawr.rekt.convert

interface TypeConverter<T>
{
    fun convert(data: ByteArray?): T?
}