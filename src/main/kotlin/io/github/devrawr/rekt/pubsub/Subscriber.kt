package io.github.devrawr.rekt.pubsub

interface Subscriber
{
    fun handleIncoming(channel: String, message: String)
}