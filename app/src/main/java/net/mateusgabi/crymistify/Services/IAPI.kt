package net.mateusgabi.crymistify.Services

import net.mateusgabi.crymistify.Model.Todo

import io.reactivex.Single

import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface IAPI {

    @GET("all")
    fun getAllTodos(@HeaderMap headers: Map<String, String>) : Single<Collection<Todo>>

}