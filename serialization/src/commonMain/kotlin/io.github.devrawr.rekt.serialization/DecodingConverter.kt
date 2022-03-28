package io.github.devrawr.rekt.serialization

interface DecodingConverter<T>
{
    fun convert(data: ByteArray?): T?
}