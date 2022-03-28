import impl.IntDecodingConverter
import impl.LongDecodingConverter
import impl.StringDecodingConverter
import kotlin.reflect.KClass

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
    private val converters = hashMapOf<KClass<*>, DecodingConverter<*>>(
        String::class to StringDecodingConverter,
        Long::class to LongDecodingConverter,
        Int::class to IntDecodingConverter
    )

    inline fun <reified T : Any> retrieveConverter(): DecodingConverter<T>? =
        retrieveConverter(T::class)

    fun <T : Any> retrieveConverter(type: KClass<T>): DecodingConverter<T>?
    {
        return converters[type] as DecodingConverter<T>?
    }

    inline fun <reified T : Any> createConverter(noinline convert: (ByteArray?) -> T?): DecodingConverter<T> =
        createConverter(T::class, convert)

    fun <T : Any> createConverter(type: KClass<T>, convert: (ByteArray?) -> T?): DecodingConverter<T>
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

    fun <T : Any> registerConverter(type: KClass<T>, converter: DecodingConverter<T>): DecodingConverter<T>
    {
        return converter.apply {
            converters[type] = this
        }
    }
}