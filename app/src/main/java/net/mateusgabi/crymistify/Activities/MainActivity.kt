package net.mateusgabi.crymistify.Activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Gravity
import android.view.View
import android.widget.Adapter
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gigamole.navigationtabstrip.NavigationTabStrip
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.OnItemClickListener
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import net.mateusgabi.crymistify.R

import kotlinx.android.synthetic.main.activity_main2.*
import net.mateusgabi.crymistify.Adapter.SimpleAdapter
import net.mateusgabi.crymistify.Fragment.TodoListViewAdapter
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.Services.API

class MainActivity : AppCompatActivity(), TodoListViewAdapter.OnListFragmentInteractionListener {

    private var allTodos = mutableListOf<Todo>()
    private var actualScreen = Screens.TODOS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)

        title = getString(R.string.app_name)

        fab.setOnClickListener {
            startActivity(Intent(this, AddTodoActivity::class.java))
        }

        navtab.setTitles("Todo", "Done", "All")
        navtab.setTabIndex(0, true)
        navtab.stripGravity = NavigationTabStrip.StripGravity.BOTTOM

        pull_to_refresh.setOnRefreshListener {
            loadAndSetNewData()
        }

        navtab.onTabStripSelectedIndexListener = object : NavigationTabStrip.OnTabStripSelectedIndexListener {
            override fun onEndTabSelected(title: String?, index: Int) {

            }

            override fun onStartTabSelected(title: String?, index: Int) {

                actualScreen = when(index) {
                    0 -> Screens.TODOS
                    1 -> Screens.DONES
                    2 -> Screens.ALL
                    else -> Screens.TODOS
                }

                screenChange()

            }

        }

        screenChange()

    }

    private fun screenChange() {

        if (allTodos.size == 0) {
            loadAndSetNewData()
        }
        else {
            setDatasFromScreen()
        }
    }

    private fun loadAndSetNewData() {
        API().getAllTodos().subscribe({
            allTodos = it.toMutableList()
            setDatasFromScreen()

        }, {
            Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_LONG).show()
        })
    }



    private fun setDatasFromScreen() {

        var todos = when(actualScreen) {
            Screens.TODOS -> allTodos.filter { !it.done }
            Screens.DONES -> allTodos.filter { it.done }
            else -> allTodos
        }

        list.layoutManager = LinearLayoutManager(this@MainActivity)
        list.adapter = TodoListViewAdapter(todos.toMutableList(), this)

        list.adapter.notifyDataSetChanged()
        pull_to_refresh.setRefreshing(false)

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

    override fun onListFragmentInteraction(item: Todo) {

        val dialogPlus = DialogPlus.newDialog(this).apply {
            adapter = SimpleAdapter(this@MainActivity, item)
            isExpanded = true
        }


        dialogPlus.create().show()

    }

    private fun toast(string: String) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
    }


    enum class Screens {
        TODOS, DONES, ALL
    }


}
