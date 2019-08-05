package ru.virarnd.kommunalkin.ui.maininfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_child_main_info.view.*
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.models.Counter

class MainInfoChildAdapter(private val countersList: List<Counter>) : RecyclerView.Adapter<MainInfoChildAdapter.ChildInfoViewHolder>(){

    override fun getItemCount() = countersList.size

    override fun onBindViewHolder(holder: ChildInfoViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildInfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_child_main_info, parent, false)
        return ChildInfoViewHolder(view)
    }

    inner class ChildInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            val item = countersList[position]
            itemView.apply {
                tv_counter_name.text = item.counterName
                tv_counter_value.text = item.counterReading.toString()
            }
        }
    }
}