import io.github.devrawr.rekt.Redis
import io.github.devrawr.rekt.decoding.impl.DefaultRedisDecoder
import io.github.devrawr.rekt.encoding.impl.DefaultRedisEncoder
import org.junit.jupiter.api.Test
import java.net.Socket

class BaseRedisCacheTest
{
    @Test
    fun createPool()
    {
        val pool = Redis.builder()
            .encoderOf(DefaultRedisEncoder)
            .decoderOf(DefaultRedisDecoder)
            .socketOf(Socket("127.0.0.1", 6379))
            .build()

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