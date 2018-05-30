package net.mateusgabi.crymistify.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import io.reactivex.Single.create
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.mateusgabi.crymistify.Model.Todo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


class API {

    private val TAG = javaClass.canonicalName

    val service: IAPI

    val URI: String = "https://us-central1-todo-app-b2a7b.cloudfunctions.net"

    val ENDPOINT = "$URI/"

    var userToken: String? = null

    init {


        val httpClient = OkHttpClient.Builder()

        val gson: Gson = GsonBuilder().setLenient().create()

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build()

        service = retrofit.create<IAPI>(IAPI::class.java)

    }

    fun getAllTodos(): Single<Collection<Todo>> {

        return Single.create({ emitter ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
                if (it.isSuccessful) {

                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer ${it.result.token}"

                    Log.i(TAG, "Authorization is ${headers["Authorization"]}")

                    service.getAllTodos(headers).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : SingleObserver<Collection<Todo>> {
                                override fun onSuccess(value: Collection<Todo>?) {
                                    emitter.onSuccess(value)
                                }

                                override fun onSubscribe(d: Disposable?) {

                                }

                                override fun onError(e: Throwable?) {
                                    Log.e(TAG, e?.message.toString())
                                    emitter.onError(e)
                                }

                            })

                }
            }
        })
    }

    fun getTodos(): Single<Collection<Todo>> {

        return Single.create({ emitter ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
                if (it.isSuccessful) {

                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer ${it.result.token}"

                    Log.i(TAG, "Authorization is ${headers["Authorization"]}")

                    service.getTodos(headers).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : SingleObserver<Collection<Todo>> {
                                override fun onSuccess(value: Collection<Todo>?) {
                                    emitter.onSuccess(value)
                                }

                                override fun onSubscribe(d: Disposable?) {

                                }

                                override fun onError(e: Throwable?) {
                                    Log.e(TAG, e?.message.toString())
                                    emitter.onError(e)
                                }

                            })

                }
            }
        })
    }

    fun getDones(): Single<Collection<Todo>> {

        return Single.create({ emitter ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(true)?.addOnCompleteListener {
                if (it.isSuccessful) {

                    val headers = HashMap<String, String>()
                    headers["Authorization"] = "Bearer ${it.result.token}"

                    Log.i(TAG, "Authorization is ${headers["Authorization"]}")

                    service.getDones(headers).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(object : SingleObserver<Collection<Todo>> {
                                override fun onSuccess(value: Collection<Todo>?) {
                                    emitter.onSuccess(value)
                                }

                                override fun onSubscribe(d: Disposable?) {

                                }

                                override fun onError(e: Throwable?) {
                                    Log.e(TAG, e?.message.toString())
                                    emitter.onError(e)
                                }

                            })

                }
            }
        })
    }

    fun addTodo(todo: Todo): Single<Boolean> {
        return create {
            responseEmitter ->

            val user = FirebaseAuth.getInstance().currentUser?.uid
            val uid = UUID.randomUUID().toString()

            FirebaseDatabase.getInstance()
                    .getReference("users/$user/todos/$uid")
                    .setValue(todo)
                    .addOnCompleteListener {
                        responseEmitter.onSuccess(it.isSuccessful)
                    }
        }
    }

    fun markAsDone(todo: Todo): Single<Boolean> {
        return create {
            responseEmitter ->


            val user = FirebaseAuth.getInstance().currentUser?.uid
            val uid = todo._key

            todo.done = true

            FirebaseDatabase.getInstance()
                    .getReference("users/$user/todos/$uid")
                    .setValue(todo)
                    .addOnCompleteListener {
                        responseEmitter.onSuccess(it.isSuccessful)
                    }
        }
    }

}