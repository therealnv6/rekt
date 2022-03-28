package io.github.devrawr.rekt.common

import dispatcher
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.*

enum class ConnectionType
{
    TCP
}

class RedisConnectionBuilder
{
    private var connectionType = ConnectionType.TCP

    private var hostname = "127.0.0.1"
    private var port = 6379

    private var autoFlush = true

    fun hostname(hostname: String): RedisConnectionBuilder
    {
        return this.apply {
            this.hostname = hostname
        }
    }

    fun port(port: Int): RedisConnectionBuilder
    {
        return this.apply {
            this.port = port
        }
    }

    fun connectionType(type: ConnectionType): RedisConnectionBuilder
    {
        return this.apply {
            this.connectionType = type
        }
    }

    fun autoFlush(flush: Boolean): RedisConnectionBuilder
    {
        return this.apply {
            this.autoFlush = flush
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun build(): RedisConnection
    {
        val thread = dispatcher
        val socketBuilder = aSocket(SelectorManager(thread))

        val socket =
            socketBuilder
                .tcp()
                .connect(this.hostname, this.port)


        return RedisConnection(
            readChannel = socket.openReadChannel(),
            writeChannel = socket.openWriteChannel(
                this.autoFlush
            )
        )
    }
}