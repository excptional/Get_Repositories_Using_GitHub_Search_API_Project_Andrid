package com.example.ghrapp

sealed class Response<T>(val errorMassage: String? = null){
    class Success<T>(): Response<T>()
    class Failure<T>(errorMassage: String): Response<T>(errorMassage = errorMassage)
}
