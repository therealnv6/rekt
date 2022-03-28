package io.github.devrawr.rekt.stream

interface Subscriber
{
    suspend fun handleIncoming(channel: String, message: String)
}