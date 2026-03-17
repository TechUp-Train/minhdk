package data.config

sealed class RequestResponse<T> {
    class Success<T>(val mData: T) : RequestResponse<T>()
    class Error(e: Throwable): RequestResponse<Nothing>()

    companion object {
        fun <T> success(data: T) = Success(data)
        fun error(ex: Throwable) = Error(ex)
    }
}