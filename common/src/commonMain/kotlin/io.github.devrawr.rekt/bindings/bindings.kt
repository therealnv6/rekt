package io.github.devrawr.rekt.bindings

import io.github.devrawr.rekt.RedisConnection
import io.github.devrawr.rekt.stream.Subscriber

suspend fun RedisConnection.callReturnRead(vararg args: Any): Any? =
    callReturnRead(args.toList())


suspend fun RedisConnection.subscribe(vararg channel: String, subscriber: (message: String) -> Unit)
{
    subscribe(
        subscriber = object : Subscriber
        {
            override suspend fun handleIncoming(channel: String, message: String)
            {
                subscriber.invoke(message)
            }
        },
        channel = channel
    )
}

suspend fun RedisConnection.pSubscribe(vararg pattern: String, subscriber: (message: String) -> Unit)
{
    this.pSubscribe(
        subscriber = object : Subscriber
        {
            override suspend fun handleIncoming(channel: String, message: String)
            {
                subscriber.invoke(message)
            }
        },
        pattern = pattern
    )
}
