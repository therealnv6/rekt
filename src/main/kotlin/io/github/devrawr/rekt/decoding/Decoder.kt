package io.github.devrawr.rekt.decoding

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
     * @param stream the underlying stream
     * @return the parsed object - nullable
     * @throws IOException         inherited from underlying stream
     * @throws ByteLayoutException if it's an unexpected byte layout
     */
    fun decode(stream: InputStream): Any?

    /**
     * Scan the input for the next carriage return.
     *
     * @param stream the underlying stream to get the next CR from
     * @return the bytes until the next CR
     * @throws IOException inherited from underlying stream
     */
    fun readString(stream: InputStream): String
}