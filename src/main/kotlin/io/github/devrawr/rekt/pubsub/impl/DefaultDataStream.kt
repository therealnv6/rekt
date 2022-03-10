package io.github.devrawr.rekt.pubsub.impl

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.pubsub.Subscriber
import io.github.devrawr.rekt.pubsub.DataStream

object DefaultDataStream : DataStream
{
    override fun subscribe(connection: RedisConnection, subscriber: Subscriber, vararg channel: String)
    {
        this.globalSubscribeMethod(
            "SUBSCRIBE",
            connection,
            subscriber,
            *channel
        )
    }

    override fun pSubscribe(connection: RedisConnection, subscriber: Subscriber, vararg pattern: String)
    {
        this.globalSubscribeMethod(
            "PSUBSCRIBE",
            connection,
            subscriber,
            *pattern
        )
    }

    fun globalSubscribeMethod(type: String, connection: RedisConnection, subscriber: Subscriber, vararg pattern: String)
    {
        val handled = hashMapOf<String, Int>()

        while (true)
        {
            val result = connection.callReturnRead<List<*>>(type, *pattern)
                ?: continue

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