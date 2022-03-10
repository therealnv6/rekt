package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.convert.impl.IntTypeConverter
import io.github.devrawr.rekt.convert.impl.LongTypeConverter
import io.github.devrawr.rekt.convert.impl.StringTypeConverter
import java.lang.reflect.Type

object Converters
{
    val converters = hashMapOf<Class<*>, TypeConverter<*>>(
        String::class.java to StringTypeConverter,
        Long::class.java to LongTypeConverter,
        Int::class.java to IntTypeConverter
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

    inline fun <reified T : Any> createConverter(noinline convert: (ByteArray?) -> T?): TypeConverter<T>
    {
        return object : TypeConverter<T>
        {
            override fun convert(data: ByteArray?): T?
            {
                return convert.invoke(data)
            }
        }.apply {
            converters[T::class.java] = this
        }
    }
}