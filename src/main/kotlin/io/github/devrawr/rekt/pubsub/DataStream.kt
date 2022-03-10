package io.github.devrawr.rekt.pubsub

import io.github.devrawr.rekt.RedisConnection

interface DataStream
{
    /**
     * Write a <b>SUBSCRIBE</b> message to the redis server.
     *
     * @param connection the connection to write the message to
     * @param subscriber the subscriber to handle the incoming messages with
     * @param channel    the specific channel to listen to
     */
    fun subscribe(connection: RedisConnection, subscriber: Subscriber, vararg channel: String)

    /**
     * Write a <b>PSUBSCRIBE</b> message to the redis server.
     *
     * @param connection the connection to write the message to
     * @param subscriber the subscriber to handle the incoming messages with
     * @param pattern    the is the pattern the redis client will listen to.
     *                   more information on these patterns can be found
     *                   at [redis docs](https://redis.io/commands/psubscribe)
     */
    fun pSubscribe(connection: RedisConnection, subscriber: Subscriber, vararg pattern: String)
}