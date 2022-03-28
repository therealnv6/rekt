package bindings

import RedisConnection

suspend fun RedisConnection.callReturnRead(vararg args: Any): Any? =
    callReturnRead(args.toList())