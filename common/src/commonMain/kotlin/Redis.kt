import kotlinx.coroutines.CoroutineDispatcher

const val STRING = '+'
const val ERROR = '_'
const val INTEGER = ':'
const val BULK_STRING = '$'
const val ARRAY = '*'

internal expect val dispatcher: CoroutineDispatcher
