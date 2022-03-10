package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.convert.impl.LongTypeConverter
import io.github.devrawr.rekt.convert.impl.StringTypeConverter

object Converters
{
    private val converters = hashMapOf<Class<*>, TypeConverter<*>>(
        String::class.java to StringTypeConverter,
        Long::class.javaObjectType to LongTypeConverter
    )

    inline fun <reified T> retrieveConverter(): TypeConverter<T>? = retrieveConverter(T::class.java)

    fun <T> retrieveConverter(type: Class<T>): TypeConverter<T>?
    {
        return converters[type] as TypeConverter<T>?
    }
}