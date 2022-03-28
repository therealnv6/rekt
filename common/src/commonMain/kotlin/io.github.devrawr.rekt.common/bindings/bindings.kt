package io.github.devrawr.rekt.common.bindings

import io.github.devrawr.rekt.common.RedisConnection

suspend fun RedisConnection.callReturnRead(vararg args: Any): Any? =
    callReturnRead(args.toList())