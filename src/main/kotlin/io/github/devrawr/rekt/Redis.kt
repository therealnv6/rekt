package io.github.devrawr.rekt

import io.github.devrawr.rekt.encoding.Encoder
import io.github.devrawr.rekt.encoding.impl.DefaultRedisEncoder
import io.github.devrawr.rekt.decoding.Decoder
import io.github.devrawr.rekt.decoding.impl.DefaultRedisDecoder
import io.github.devrawr.rekt.util.ClassReflectionUtil.getOrCreateInstance
import java.net.Socket
import kotlin.reflect.KClass

const val STRING = '+'.code
const val ERROR = '_'.code
const val INTEGER = ':'.code
const val BULK_STRING = '$'.code
const val ARRAY = '*'.code

val CRLF = byteArrayOf(
    '\r'.code.toByte(),
    '\n'.code.toByte()
)

object Redis
{
    var encoder: Encoder = DefaultRedisEncoder
    var decoder: Decoder = DefaultRedisDecoder

    inline fun <reified T : Encoder> encoder(): Redis
    {
        return this.apply {
            this.encoder(T::class)
        }
    }

    fun <T : Encoder> encoder(clazz: KClass<T>): Redis
    {
        return this.apply {
            this.encoder(clazz.getOrCreateInstance() as Encoder)
        }
    }

    fun <T : Encoder> encoder(encoder: T): Redis
    {
        return this.apply {
            this.encoder = encoder
        }
    }

    inline fun <reified T : Decoder> parser(): Redis
    {
        return this.apply {
            this.parser(T::class)
        }
    }

    fun <T : Decoder> parser(clazz: KClass<T>): Redis
    {
        return this.apply {
            this.parser(clazz.getOrCreateInstance() as Decoder)
        }
    }

    fun <T : Decoder> parser(parser: T): Redis
    {
        return this.apply {
            this.decoder = parser
        }
    }

    fun create(
        socket: Socket,
        decoder: Decoder = this.decoder,
        encoder: Encoder = this.encoder
    ): RedisConnection
    {
        return RedisConnection(socket, decoder, encoder)
    }

    fun create(
        hostname: String = "127.0.0.1",
        port: Int = 6379,
        decoder: Decoder = this.decoder,
        encoder: Encoder = this.encoder
    ): RedisConnection
    {
        return this.create(
            socket = Socket(hostname, port),
            decoder = decoder,
            encoder = encoder
        )
    }
}