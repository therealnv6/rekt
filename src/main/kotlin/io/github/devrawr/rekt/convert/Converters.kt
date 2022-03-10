package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.convert.impl.LongTypeConverter
import io.github.devrawr.rekt.convert.impl.StringTypeConverter

object Converters
{
    private val converters = hashMapOf<Class<*>, TypeConverter<*>>(
        String::class.java to StringTypeConverter,
        Long::class.java to LongTypeConverter
    )

    inline fun <reified T : Any> retrieveConverter(): TypeConverter<T>? = retrieveConverter(T::class.java)

    fun <T : Any> retrieveConverter(type: Class<T>): TypeConverter<T>?
    {
        return converters[if (type.kotlin.javaPrimitiveType != null)
        {
            type.kotlin.javaPrimitiveType!!
        } else
        {
            type
        }] as TypeConverter<T>?
    }
}