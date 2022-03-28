package io.github.devrawr.rekt.common.stream.impl

import io.github.devrawr.rekt.common.RedisConnection
import io.github.devrawr.rekt.common.stream.DataStream
import io.github.devrawr.rekt.common.stream.Subscriber

object DefaultDataStream : DataStream
{
    override suspend fun publish(connection: RedisConnection, message: String, channel: String)
    {
        connection.call("PUBLISH", channel, message)
    }

    override suspend fun subscribe(connection: RedisConnection, subscriber: Subscriber, vararg channel: String)
    {
        globalSubscribeMethod(
            "SUBSCRIBE",
            connection,
            subscriber,
            *channel
        )
    }

    override suspend fun pSubscribe(connection: RedisConnection, subscriber: Subscriber, vararg pattern: String)
    {
        globalSubscribeMethod(
            "PSUBSCRIBE",
            connection,
            subscriber,
            *pattern
        )
    }

    suspend fun globalSubscribeMethod(
        type: String,
        connection: RedisConnection,
        subscriber: Subscriber,
        vararg pattern: String
    )
    {
        val handled = hashMapOf<String, Int>()
        val newPattern = pattern.copyInto(mutableListOf(type).toTypedArray(), 1)

        while (true)
        {
            val result = connection.callReturnRead(newPattern.toList())
                ?: continue

            if (result !is List<*>)
            {
                continue
            }

            for (entries in result.withIndex())
            {
                val entry = entries.value

                if (entry != null && entry is ByteArray)
                {
                    val content = entry.decodeToString()

                    if (handled.containsKey(content) && handled[content] == entries.index)
                    {
                        continue
                    }

                    for (string in pattern)
                    {
                        subscriber.handleIncoming(string, content)
                    }

                    // TODO: 3/10/2022 better solution for this, not entirely sure how this works.
                    handled[content] = entries.index
                }
            }
        }
    }
}