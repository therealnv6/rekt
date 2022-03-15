package io.github.devrawr.rekt.convert

interface DecodingConverter<T>
{
    fun convert(data: ByteArray?): T?
}