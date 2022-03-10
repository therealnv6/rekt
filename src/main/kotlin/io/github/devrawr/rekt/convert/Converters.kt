package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.convert.impl.IntTypeConverter
import io.github.devrawr.rekt.convert.impl.LongTypeConverter
import io.github.devrawr.rekt.convert.impl.StringTypeConverter
import java.lang.reflect.Type

object Converters
{
    private val converters = hashMapOf<Class<*>, TypeConverter<*>>(
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

    inline fun <reified T : Any> createConverter(noinline convert: (ByteArray?) -> T?): TypeConverter<T> =
        createConverter(T::class.java, convert)

    fun <T : Any> createConverter(type: Class<T>, convert: (ByteArray?) -> T?): TypeConverter<T>
    {
        return registerConverter(
            type,
            object : TypeConverter<T>
            {
                override fun convert(data: ByteArray?): T?
                {
                    return convert.invoke(data)
                }
            }
        )
    }

    fun <T : Any> registerConverter(type: Class<T>, converter: TypeConverter<T>): TypeConverter<T>
    {
        return converter.apply {
            converters[type] = this
        }
    }
}