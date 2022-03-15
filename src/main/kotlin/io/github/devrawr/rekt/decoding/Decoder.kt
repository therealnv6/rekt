package io.github.devrawr.rekt.decoding

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.decoding.exception.ByteLayoutException
import java.io.IOException
import java.io.InputStream

/**
 * Mainly for reading the incoming data of the redis stream.
 *
 * Many of the information used to create this class can be found
 * at the following links
 * - https://redis.io/topics/protocol
 * - https://github.com/redis/jedis
 * - another client I sadly cannot find anymore
 */
interface Decoder
{
    /**
     * Decode incoming data from connection stream.
     *
     * @param connection the underlying connection
     * @return the parsed object - nullable
     */
    fun decode(connection: RedisConnection): Any?
}