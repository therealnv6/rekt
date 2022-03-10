import io.github.devrawr.rekt.Redis
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BaseRedisCacheTest
{
    @Test
    fun createPool()
    {
        val pool = Redis.createConnection()

        pool.hset("fuck", "dir", "40")
        pool.hset("fuck", "fir", "hello")
        pool.hset("fuck", "sir", 39)

        assertEquals(39, pool.hget<Long>("fuck", "sir"))
        assertEquals(40, pool.hget<Long>("fuck", "dir"))
        assertEquals("hello", pool.hget("fuck", "fir"))
    }
}