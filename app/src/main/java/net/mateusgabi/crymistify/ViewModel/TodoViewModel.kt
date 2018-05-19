package net.mateusgabi.crymistify.ViewModel

import io.reactivex.Single
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.Services.API

class TodoViewModel {

    val service: API = API()


    fun getAllTodos(): Single<Collection<Todo>> = service.getAllTodos()
    fun getTodos(): Single<Collection<Todo>> = service.getTodos()
    fun getDones(): Single<Collection<Todo>> = service.getDones()

}
