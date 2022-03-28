package io.github.devrawr.rekt

import io.github.devrawr.rekt.bindings.callReturnRead
import io.github.devrawr.rekt.resp.decoding.Decoder
import io.github.devrawr.rekt.resp.decoding.impl.DefaultRedisDecoder
import io.github.devrawr.rekt.resp.encoding.Encoder
import io.github.devrawr.rekt.resp.encoding.impl.DefaultRedisEncoder
import io.github.devrawr.rekt.stream.DataStream
import io.github.devrawr.rekt.stream.Subscriber
import io.github.devrawr.rekt.stream.impl.DefaultDataStream
import io.ktor.util.*
import io.ktor.utils.io.*

class RedisConnection(
    val readChannel: ByteReadChannel,
    val writeChannel: ByteWriteChannel
)
{
    var decoder: Decoder = DefaultRedisDecoder
    var encoder: Encoder = DefaultRedisEncoder
    var dataStream: DataStream = DefaultDataStream

    companion object
    {
        @OptIn(InternalAPI::class)
        fun build(): RedisConnectionBuilder
        {
            return RedisConnectionBuilder()
        }
    }

    suspend fun call(vararg args: Any) = call(args.toList())

    suspend fun call(args: List<*>)
    {
        encoder.write(this, args)
        this.read() // even though we're not returning this, we're still reading to clear the buffer!
    }

    suspend fun callReturnRead(args: List<*>): Any?
    {
        // don't call this.call(List<*>), we want to handle this.read() on our own,
        // but because call(List<*>) already calls this.read(), the buffer will be cleared,
        // causing us to not receive any data.
        encoder.write(this, args)

        return this.read()
    }

    suspend fun set(key: String, value: Any)
    {
        call(ByteArray::class, "SET", key, value)
    }

    suspend fun get(key: String): Any?
    {
        return callReturnRead("GET", key)
    }

    suspend fun hset(hash: String, key: String, value: Any)
    {
        call("HSET", hash, key, value)
    }

    suspend fun hset(string: String, value: Any)
    {
        val pair = string.toHashKey()

        val hash = pair.first
        val key = pair.second

        hset(hash, key, value)
    }

    suspend fun hget(string: String): Any?
    {
        val pair = string.toHashKey()

        val hash = pair.first
        val key = pair.second

        return hget(hash, key)
    }

    suspend fun hget(hash: String, key: String): Any?
    {
        return callReturnRead("HGET", hash, key)
    }

    suspend fun hdel(string: String)
    {
        val pair = string.toHashKey()

        val hash = pair.first
        val key = pair.second

        hdel(hash, key)
    }

    suspend fun hdel(hash: String, key: String)
    {
        call("HDEL", hash, key)
    }

    suspend fun hgetAll(hash: String): List<Any?>
    {
        val call = callReturnRead("HGETALL", hash)
            ?: return emptyList()

        if (call !is Iterable<*>)
        {
            return emptyList()
        }

        return call.map {
            it
        }
    }

    suspend fun subscribe(subscriber: Subscriber, vararg channel: String)
    {
        this.dataStream.subscribe(this, subscriber, *channel)
    }

    suspend fun pSubscribe(subscriber: Subscriber, vararg pattern: String)
    {
        this.dataStream.pSubscribe(this, subscriber, *pattern)
    }

    private suspend fun read(): Any?
    {
        return decoder.decode(this)
    }

    fun String.toHashKey(): Pair<String, String>
    {
        val split = this.split("/")

        if (split.size != 2)
        {
            throw IllegalArgumentException("Provided key is $this, must be formatted like 'hash/key'")
        }

        return Pair(split[0], split[1])
    }
}