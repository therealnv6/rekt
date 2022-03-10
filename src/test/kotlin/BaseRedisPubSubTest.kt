import io.github.devrawr.rekt.Redis
import kotlin.test.Test

class BaseRedisPubSubTest
{
    @Test
    fun subscribe()
    {
        val redis = Redis.create()

        redis.subscribe("test") {
            println(it)
        }
    }
}