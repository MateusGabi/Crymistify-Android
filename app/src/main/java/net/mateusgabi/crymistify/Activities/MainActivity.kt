package net.mateusgabi.crymistify.Activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import net.mateusgabi.crymistify.Fragment.TodoListFragment
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.R
import net.mateusgabi.crymistify.Services.API

class MainActivity :
        AppCompatActivity(),
        TodoListFragment.OnListFragmentInteractionListener

{
    private val TAG: String = javaClass.canonicalName

    private var fragment: Fragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                getTodos()
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        switchFragment(TodoListFragment.newInstance(1))

        getTodos()
    }

    private fun getTodos() {
        API().getAllTodos().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<Collection<Todo>> {
                    override fun onSuccess(value: Collection<Todo>?) {
                        Log.i(TAG, value?.size.toString())
                    }

                    override fun onSubscribe(d: Disposable?) {

                    }

                    override fun onError(e: Throwable?) {
                        Log.e(TAG, e?.message.toString())
                    }

                })
    }

    private fun switchFragment(fragment: Fragment) {
        this.fragment = fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.commit()
    }

    override fun onListFragmentInteraction(item: Todo?) {
        Log.i(TAG, "User click")
    }
}
