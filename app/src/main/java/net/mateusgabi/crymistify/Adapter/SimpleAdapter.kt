package net.mateusgabi.crymistify.Adapter

import android.content.ClipDescription
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.simple_list_item.view.*
import net.mateusgabi.crymistify.Model.Todo
import net.mateusgabi.crymistify.R

class SimpleAdapter(
        context: Context,
        private val todo: Todo
) : BaseAdapter() {

    private val isGrid: Boolean = false
    private val count: Int = 1

    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return count
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder: ViewHolder
        var view: View? = convertView

        if (view == null) {
            view = if (isGrid) {
                layoutInflater.inflate(R.layout.simple_grid_item, parent, false)
            } else {
                layoutInflater.inflate(R.layout.simple_list_item, parent, false)
            }

            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val context = parent.context
        when (position) {
            0 -> {
                viewHolder.mView.title.text = todo.title
                viewHolder.mView.description.text = todo.description

                viewHolder.mView.btn_done.setOnClickListener {
                    Toast.makeText(context, "Our best jest marked this shit as done", Toast.LENGTH_SHORT).show()
                }

                viewHolder.mView.btn_delete.setOnClickListener {
                    Toast.makeText(context, "Holly chaves, this action is not enable yet", Toast.LENGTH_SHORT).show()
                }
            }
        }

        return view!!
    }

    data class ViewHolder(val mView: View)
}
