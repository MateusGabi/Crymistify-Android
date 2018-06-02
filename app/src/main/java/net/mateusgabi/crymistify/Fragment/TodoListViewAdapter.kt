package net.mateusgabi.crymistify.Fragment

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.mateusgabi.date.DateHelper
import net.mateusgabi.crymistify.R

import kotlinx.android.synthetic.main.fragment_todo.view.*
import net.mateusgabi.crymistify.Model.Todo
import java.text.SimpleDateFormat
import java.util.*

/**
 * [RecyclerView.Adapter] that can display a [DummyItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 * TODO: Replace the implementation with code for your data type.
 */
class TodoListViewAdapter(
        private val mValues: MutableList<Todo>,
        private val mListener: OnListFragmentInteractionListener?)
    : RecyclerView.Adapter<TodoListViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            val item = v.tag as Todo
            // Notify the active callbacks interface (the activity, if the fragment is attached to
            // one) that an item has been selected.
            mListener?.onListFragmentInteraction(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_todo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.mIdView.text = item.title
        holder.mContentView.text = item.description


        with(holder.mView) {
            tag = item
            setOnClickListener(mOnClickListener)

            if (!item.expire_in.isNullOrBlank() && item.expire_in.length == 25) {
                val date = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX").parse(item.expire_in)
                val now = Date()

                date_expire_in.text = DateHelper.format(date, "MM/dd h:mm a")

                var color = R.color.material_light_white


                if (dateIsLessThanXDays(date, 7)) {
                    color = R.color.material_yellow_500
                }

                if (dateIsLessThanXDays(date, 3)) {
                    color = R.color.material_orange_500
                }

                if (dateIsLessThanXDays(date, 1)) {
                    color = R.color.material_red_500
                }

                if (dateIsLessThanXDays(date,0)) {
                    color = R.color.material_light_black
                }


                border_hue.setBackgroundColor(context.getColor(color))
            }

        }
    }

    private fun dateIsLessThanXDays(date: Date, days: Int): Boolean {
        return DateHelper.add(Date(), DateHelper.DAY, days).after(date)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mIdView: TextView = mView.item_number
        val mContentView: TextView = mView.content

        override fun toString(): String {
            return super.toString() + " '" + mContentView.text + "'"
        }
    }

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: Todo)
    }
}
