package net.mateusgabi.crymistify.Adapter

import android.content.ClipDescription
import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main2.*
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

            viewHolder = ViewHolder(
                    view.findViewById(R.id.text_view),
                    view.findViewById(R.id.description)
            )
            view.tag = viewHolder
        } else {
            viewHolder = view.tag as ViewHolder
        }

        val context = parent.context
        when (position) {
            0 -> {
                viewHolder.textView.text = todo.title
                viewHolder.description.text = todo.description

            }
            1 -> {
                viewHolder.textView.text = context.getString(R.string.google_maps_title)
            }
            else -> {
                viewHolder.textView.text = context.getString(R.string.google_messenger_title)
            }
        }

        return view!!
    }

    data class ViewHolder(val textView: TextView, val description: TextView)
}
