package io.github.devrawr.rekt.encoding

import io.github.devrawr.rekt.RedisConnection

interface Encoder
{
    /**
     * General method to write to the [connection]
     *
     * @param connection the underlying connection
     * @param value the string to write
     */
    fun write(connection: RedisConnection, value: Any)
    {
        when (value)
        {
            is ByteArray -> write(connection, value)
            is String -> write(connection, value.toByteArray())
            is Long -> write(connection, value)
            is Int -> write(connection, value.toLong())
            is List<*> -> write(connection, value)
            else -> throw IllegalStateException("Wrong type provided, ${value.javaClass.name}")
        }
    }

    /**
     * Write a Redis Bulk String formatted message to the [connection]
     *
     * @param connection the underlying connection
     * @param value the string to write
     */
    fun write(connection: RedisConnection, value: ByteArray)

    /**
     * Write a Redis-Integer formatted message to the [connection]
     *
     * @param connection the underlying connection
     * @param value the number
     */
    fun write(connection: RedisConnection, value: Long)

    /**
     * Write a Redis-ARRAY formatted message to the [connection]
     *
     * @param connection the underlying connection
     * @param value the list of values (the array) to format
     */
    fun write(connection: RedisConnection, value: List<*>)
}