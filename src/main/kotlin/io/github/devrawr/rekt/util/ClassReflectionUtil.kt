package io.github.devrawr.rekt.util

import kotlin.reflect.KClass

object ClassReflectionUtil
{
    fun <T : Any> KClass<*>.getOrCreateInstance(): T
    {
        return (this.objectInstance ?: this.java.newInstance()) as T
    }
}