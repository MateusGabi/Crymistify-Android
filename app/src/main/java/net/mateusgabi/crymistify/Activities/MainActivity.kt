package net.mateusgabi.crymistify.Activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.mateusgabi.crymistify.Fragment.AllTodosListFragment
import net.mateusgabi.crymistify.Fragment.DonesTodosListFragment
import net.mateusgabi.crymistify.Fragment.TodoListFragment
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.R

class MainActivity :
        AppCompatActivity(),
        TodoListFragment.OnListFragmentInteractionListener,
        DonesTodosListFragment.OnListFragmentInteractionListener,
        AllTodosListFragment.OnListFragmentInteractionListener

{
    private val TAG: String = javaClass.canonicalName

    private var fragment: Fragment? = null
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(TodoListFragment.newInstance(1))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                switchFragment(DonesTodosListFragment.newInstance(1))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                switchFragment(AllTodosListFragment.newInstance(1))
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
    }

    private fun switchFragment(fragment: Fragment) {
        this.fragment = fragment
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, fragment)
        fragmentTransaction.commit()
    }

    override fun onListFragmentInteraction(item: Todo?) {
        Log.i(TAG, "User click")
        Toast.makeText(this, "Pode clicar a vontade", Toast.LENGTH_LONG).show()
    }
}
