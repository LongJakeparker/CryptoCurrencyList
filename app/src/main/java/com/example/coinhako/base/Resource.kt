package com.example.coinhako.base


/**
 * @author longtran
 * @since 29/05/2021
 */

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<out T>(
    val status: Status,
    val data: T?,
    val exception: Throwable?,
    val progressDone: Int?
) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null, null)
        }

        fun <T> error(exception: Throwable?): Resource<T> {
            return Resource(Status.ERROR, null, exception, null)
        }

        fun <T> loading(progressDone: Int): Resource<T> {
            return Resource(Status.LOADING, null, null, progressDone)
        }
    }
}

/**
 * Status of a resource that is provided to the UI.
 *
 *
 * These are usually created by the Repository classes where they return
 * `LiveData<Resource<T>>` to pass back the latest data to the UI with its fetch status.
 */
enum class Status {
    LOADING,
    ERROR,
    SUCCESS
}