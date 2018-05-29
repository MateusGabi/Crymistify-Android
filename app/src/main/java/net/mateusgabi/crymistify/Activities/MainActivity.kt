package net.mateusgabi.crymistify.Activities

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import net.mateusgabi.crymistify.Fragment.AllTodosListFragment
import net.mateusgabi.crymistify.Fragment.DonesTodosListFragment
import net.mateusgabi.crymistify.Fragment.ProfileViewFragment
import net.mateusgabi.crymistify.Fragment.TodoListFragment
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.R

class MainActivity :
        AppCompatActivity(),
        TodoListFragment.OnListFragmentInteractionListener,
        DonesTodosListFragment.OnListFragmentInteractionListener,
        AllTodosListFragment.OnListFragmentInteractionListener,
        ProfileViewFragment.OnFragmentInteractionListener

{
    private val TAG: String = javaClass.canonicalName

    private var fragment: Fragment? = null
    private var todosFragment: TodoListFragment? = null
    private var allTodosFragment: AllTodosListFragment? = null
    private var donesTodosFragment: DonesTodosListFragment? = null

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                switchFragment(Screens.TODOS)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                switchFragment(Screens.DONES)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                switchFragment(Screens.ALL)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profile -> {
                switchFragment(Screens.PROFILE)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        switchFragment(Screens.TODOS)
    }

    private fun switchFragment(screen: Screens) {

        when(screen) {
            Screens.TODOS -> {
                if (this.todosFragment == null) {
                    todosFragment = TodoListFragment.newInstance(1)
                }

                this.fragment = todosFragment
            }
            Screens.ALL -> {
                if (this.allTodosFragment == null) {
                    allTodosFragment = AllTodosListFragment.newInstance(1)
                }

                this.fragment = allTodosFragment
            }
            Screens.DONES -> {
                if (this.donesTodosFragment == null) {
                    donesTodosFragment = DonesTodosListFragment.newInstance(1)
                }

                this.fragment = donesTodosFragment
            }
            Screens.PROFILE -> {
                if (this.todosFragment == null) {
                    todosFragment = TodoListFragment.newInstance(1)
                }

                this.fragment = todosFragment
            }
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content, this.fragment)
        fragmentTransaction.commit()
    }

    override fun onListFragmentInteraction(item: Todo?) {
        Log.i(TAG, "User click")
        Toast.makeText(this, "Pode clicar a vontade", Toast.LENGTH_LONG).show()
    }

    override fun onFragmentInteraction(uri: Uri) {

    }

    enum class Screens {
        TODOS, DONES, ALL, PROFILE
    }
}
