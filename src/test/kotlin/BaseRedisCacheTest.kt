import io.github.devrawr.rekt.Redis
import org.junit.jupiter.api.Test

class BaseRedisCacheTest
{
    @Test
    fun createPool()
    {
        val pool = Redis.create()

        pool.hset("fuck", "dir", "40")
        pool.hset("fuck", "fir", "hello")
        pool.hset("fuck", "sir", 39)


        listOf(
            pool.hget<Long>("fuck", "sir"), // saved as int, read as long
            pool.hget<Long>("fuck", "dir"), // saved as string, read as long
            pool.hget<String>("fuck", "fir") // saved as string, read as string
        ).forEach {
            println(it)
        }
    }
}