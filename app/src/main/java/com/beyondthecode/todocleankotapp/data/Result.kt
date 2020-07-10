package com.beyondthecode.todocleankotapp.data

/**
 * Created by Elías Bellido on 7/7/20
 * Powered by LuminDevs
 * TodoCleanKotlinSample Android Project
 */

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class Result<out R>{

    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()

    override fun toString(): String {
        return when(this){
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }

    /**
     * true if [Result] is of type [Success] & holds non null [Succes.data]
     * */
    val Result<*>.succeeded
        get() = this is Success && data != null
}