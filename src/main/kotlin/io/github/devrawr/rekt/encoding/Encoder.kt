package io.github.devrawr.rekt.encoding

import java.io.OutputStream

interface Encoder
{
    /**
     * General method to write to the [stream]
     *
     * @param stream the stream to write it to
     * @param value the string to write
     */
    fun write(stream: OutputStream, value: Any)

    /**
     * Write a Redis Bulk String formatted message to the [stream]
     *
     * @param stream the stream to write it to
     * @param value the string to write
     */
    fun write(stream: OutputStream, value: ByteArray)

    /**
     * Write a Redis-Integer formatted message to the [stream]
     *
     * @param stream the stream to write it to
     * @param value the number
     */
    fun write(stream: OutputStream, value: Long)

    /**
     * Write a Redis-ARRAY formatted message to the [stream]
     *
     * @param stream the stream to write it to
     * @param value the list of values (the array) to format
     */
    fun write(stream: OutputStream, value: List<*>)
}