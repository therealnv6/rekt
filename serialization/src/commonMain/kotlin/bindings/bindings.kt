package bindings

import io.github.devrawr.rekt.RedisConnection
import Converters
import kotlin.reflect.KClass

suspend inline fun <reified T : Any> RedisConnection.hget(string: String): T?
{
    val pair = string.toHashKey()

    val hash = pair.first
    val key = pair.second

    return hget<T>(hash, key)
}

suspend inline fun <reified T : Any> RedisConnection.hget(hash: String, key: String): T?
{
    return callReturnRead<T>("HGET", hash, key)
}

suspend inline fun <reified T : Any> RedisConnection.get(key: String): T?
{
    return callReturnRead<T>(key)
}

suspend inline fun <reified T : Any> RedisConnection.hgetAll(hash: String): List<T?>
{
    val call = callReturnRead<List<ByteArray>>("HGETALL", hash) ?: return emptyList()

    return call.map {
        val converter = Converters.retrieveConverter(T::class)

        if (converter == null)
        {
            it as T
        } else
        {
            converter.convert(it)!!
        }
    }
}

suspend inline fun <reified T : Any> RedisConnection.callReturnRead(vararg args: Any): T?
{
    encoder.write(this, args)
    return this.read(T::class)
}

suspend fun <T : Any> RedisConnection.read(type: KClass<T>): T?
{
    val converter = Converters.retrieveConverter(type)
    val decoded = decoder.decode(this)
        ?: return null

    val converted = try
    {
        decoded as T
    } catch (ignored: ClassCastException)
    {
        decoded as ByteArray
    }

    return if (converted is ByteArray)
    {
        if (converter == null)
        {
            converted as T
        } else
        {
            converter.convert(converted)
        }
    } else
    {
        converted as T?
    }
}