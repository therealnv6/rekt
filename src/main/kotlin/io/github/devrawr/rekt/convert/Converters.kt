package io.github.devrawr.rekt.convert

import io.github.devrawr.rekt.convert.impl.IntDecodingConverter
import io.github.devrawr.rekt.convert.impl.LongDecodingConverter
import io.github.devrawr.rekt.convert.impl.StringDecodingConverter

/**
 * These converters are simply for classes which are
 * not natively supported by redis, or if you want
 * a way to convert a certain type (e.g. RESP String to a Long).
 *
 * Basic converter implementation example could be found in the
 * LongTypeConverter class.
 *
 * If you want a RESP converter, look at RESPDecodingConverter instead,
 * or one of the RESP implementations within the impl/resp package.
 */
object Converters
{
    private val converters = hashMapOf<Class<*>, DecodingConverter<*>>(
        String::class.java to StringDecodingConverter,
        Long::class.java to LongDecodingConverter,
        Int::class.java to IntDecodingConverter
    )

    inline fun <reified T : Any> retrieveConverter(): DecodingConverter<T>? = retrieveConverter(T::class.java)

    fun <T : Any> retrieveConverter(type: Class<T>): DecodingConverter<T>?
    {
        return converters[if (type.kotlin.javaPrimitiveType != null)
        {
            type.kotlin.javaPrimitiveType!!
        } else
        {
            type
        }] as DecodingConverter<T>?
    }

    inline fun <reified T : Any> createConverter(noinline convert: (ByteArray?) -> T?): DecodingConverter<T> =
        createConverter(T::class.java, convert)

    fun <T : Any> createConverter(type: Class<T>, convert: (ByteArray?) -> T?): DecodingConverter<T>
    {
        return registerConverter(
            type,
            object : DecodingConverter<T>
            {
                override fun convert(data: ByteArray?): T?
                {
                    return convert.invoke(data)
                }
            }
        )
    }

    fun <T : Any> registerConverter(type: Class<T>, converter: DecodingConverter<T>): DecodingConverter<T>
    {
        return converter.apply {
            converters[type] = this
        }
    }
}