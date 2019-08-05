package ru.virarnd.kommunalkin.ui.detailinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.library.baseAdapters.BR
import androidx.databinding.library.baseAdapters.BR.viewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.ajalt.timberkt.Timber
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.databinding.ItemDetailInfoBinding
import ru.virarnd.kommunalkin.models.Counter
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoAdapter(val viewModel: DetailInfoViewModel, val status: EstateObjectStatus) : RecyclerView.Adapter<DetailInfoAdapter.DetailViewHolder>(){

    var counterAndPrevReadingList: ArrayList<Pair<Counter, Double>> = arrayListOf()
        get() = field
        set(value) {
            Timber.d{"Vira_DetailInfoAdapter Set counterAndPrevReadingList"}
            field = value
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_detail_info, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount() = counterAndPrevReadingList.size

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.setupItem(counterAndPrevReadingList[position])
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)
        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }

        val binding: ItemDetailInfoBinding = ItemDetailInfoBinding.bind(itemView)

        fun setupItem(dataSet: Pair<Counter, Double>) {
            binding.lifecycleOwner = this@DetailViewHolder
            binding.detailInfoViewModel = viewModel
            binding.counter = dataSet.first
            binding.previousReading = dataSet.second
            binding.tvConsumption.text = (dataSet.first.counterReading - dataSet.second).toString()
        }

    }
}
//    ListAdapter<Pair<Counter, Double>, DetailInfoAdapter.DetailViewHolder>(DetailInfoDiffUtilCallback()) {

/*
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_detail_info, parent, false)
        return DetailViewHolder(view)
    }
*/

/*
    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.setupItem(getItem(position))
    }
*/

/*
    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemDetailInfoBinding = ItemDetailInfoBinding.bind(itemView)

        fun setupItem(dataSet: Pair<Counter, Double>) {
            binding.detailInfoViewModel = viewModel
            binding.counter = dataSet.first
            binding.previousReading = dataSet.second
        }
    }
*/

    class DetailInfoDiffUtilCallback(private val oldList: List<Pair<Counter, Double>>, private val newList: List<Pair<Counter, Double>>) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition].first
            val newItem = newList[newItemPosition].first
            Timber.d{"Vira_DetailInfoAdapter Compare Items $oldItemPosition and $newItemPosition,\n" +
                    "result: ${oldItem.counterStateId == newItem.counterStateId && oldList[oldItemPosition].second == newList[newItemPosition].second}"}
            return oldItem.counterStateId == newItem.counterStateId && oldList[oldItemPosition].second == newList[newItemPosition].second
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition].first
            val newItem = newList[newItemPosition].first
            Timber.d{"Vira_DetailInfoAdapter Compare content $oldItemPosition and $newItemPosition, result: ${oldItem == newItem}"}
            return oldItem == newItem
        }

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

/*
        override fun areItemsTheSame(oldItem: Pair<Counter, Double>, newItem: Pair<Counter, Double>): Boolean {
            Timber.d{"Vira_DetailInfoAdapter Simple try to compare: ${oldItem.first.counterStateId} and ${newItem.first.counterStateId},\n" +
                    "result: ${oldItem.first.counterStateId == newItem.first.counterStateId && oldItem.second == newItem.second}"}
            return oldItem.first.counterStateId == newItem.first.counterStateId && oldItem.second == newItem.second
        }

        override fun areContentsTheSame(oldItem: Pair<Counter, Double>, newItem: Pair<Counter, Double>): Boolean {
            Timber.d{"Vira_DetailInfoAdapter Complex try to compare: ${oldItem.first.counterStateId} and ${newItem.first.counterStateId},\n" +
                    "result: ${oldItem.first.counterReading == newItem.first.counterReading
                            && oldItem.first.estateObjectFootprintId == newItem.first.estateObjectFootprintId
                            && oldItem.first.counterName == newItem.first.counterName
                            && oldItem.first.counterType == newItem.first.counterType}"}
            return oldItem.first.counterReading == newItem.first.counterReading
                    && oldItem.first.estateObjectFootprintId == newItem.first.estateObjectFootprintId
                    && oldItem.first.counterName == newItem.first.counterName
                    && oldItem.first.counterType == newItem.first.counterType
        }
*/
    }
