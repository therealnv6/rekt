import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

internal actual val dispatcher: CoroutineDispatcher = Executors
    .newCachedThreadPool()
    .asCoroutineDispatcher()

