interface DecodingConverter<T>
{
    fun convert(data: ByteArray?): T?
}