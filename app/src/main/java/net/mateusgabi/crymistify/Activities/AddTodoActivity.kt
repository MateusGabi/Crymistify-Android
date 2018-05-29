package net.mateusgabi.crymistify.Activities

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import net.mateusgabi.crymistify.R

import kotlinx.android.synthetic.main.activity_add_todo.*
import kotlinx.android.synthetic.main.content_add_todo.*
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : AppCompatActivity() {

    private var todo_date: Date? = Date()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        setSupportActionBar(toolbar)

        title = "Create a Todo"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        input_date.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            todo_date = calendar.time
        }

        fab.setOnClickListener {
            createTodo()
        }
    }

    private fun createTodo() {
        val title = input_title.text.toString()
        val description = input_description.text.toString()
        val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").format(todo_date)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item?.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)

    }

}
