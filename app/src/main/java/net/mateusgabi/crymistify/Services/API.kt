package net.mateusgabi.crymistify.Services

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import io.reactivex.Single
import io.reactivex.Single.create
import io.reactivex.Single.zip
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import net.mateusgabi.crymistify.Model.Todo
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.function.BiFunction


class API {

    private val TAG = javaClass.canonicalName

    val service : IAPI

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

    private fun getUserToken(): Single<Boolean> {
        return Single.create({ emitter ->
            FirebaseAuth.getInstance().currentUser?.getIdToken(false)?.addOnCompleteListener {
                if (it.isSuccessful) {
                    userToken = (it.result?.token)
                    Log.i(TAG, "Usertoken set to $userToken")
                    emitter.onSuccess(true)
                }
                else {
                    emitter.onError(Throwable("error"))
                }
            }
        })
    }

    fun getAllTodos() : Single<Collection<Todo>> {

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


//        return service.getAllTodos(headers)
    }


}