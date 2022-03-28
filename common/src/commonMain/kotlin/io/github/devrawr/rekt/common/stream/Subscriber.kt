package io.github.devrawr.rekt.common.stream

interface Subscriber
{
    suspend fun handleIncoming(channel: String, message: String)
}