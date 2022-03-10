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

    inline fun <reified T : Encoder> encoder(): Redis = this.encoder(T::class)
    inline fun <reified T : Decoder> decoder(): Redis = this.decoder(T::class)

    fun <T : Encoder> encoder(clazz: KClass<T>): Redis = this.encoder(clazz.getOrCreateInstance() as Encoder)
    fun <T : Decoder> decoder(clazz: KClass<T>): Redis = this.decoder(clazz.getOrCreateInstance() as Decoder)

    fun <T : Encoder> encoder(encoder: T): Redis
    {
        return this.apply {
            this.encoder = encoder
        }
    }

    fun <T : Decoder> decoder(parser: T): Redis
    {
        return this.apply {
            this.decoder = parser
        }
    }

    @JvmOverloads
    fun create(
        socket: Socket,
        decoder: Decoder = this.decoder,
        encoder: Encoder = this.encoder
    ): RedisConnection
    {
        return RedisConnection(socket, decoder, encoder)
    }

    @JvmOverloads
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