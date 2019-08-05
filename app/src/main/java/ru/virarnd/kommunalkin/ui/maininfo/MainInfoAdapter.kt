package ru.virarnd.kommunalkin.ui.maininfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.database.EstateObjectFootprintAndCounters
import ru.virarnd.kommunalkin.databinding.ItemMainInfoBinding
import ru.virarnd.kommunalkin.models.EstateObjectStatus


class MainInfoAdapter(val viewModel: MainInfoViewModel) :
    ListAdapter<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>, MainInfoAdapter.InfoViewHolder>(
        MainInfoDiffUtilCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_main_info, parent, false)
        return InfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.setupItem(getItem(position))
    }


    inner class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: ItemMainInfoBinding = ItemMainInfoBinding.bind(itemView)

        fun setupItem(item: Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>) {
            binding.firstEstateObjectFootprint = item.first
            binding.secondEstateObjectFootprint = item.second
            binding.viewModel = viewModel
            with(binding) {
                when (item.second?.estateObjectFootprint?.status) {
                    EstateObjectStatus.COMPLETED -> mainInfoCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.light_green
                        )
                    )
                    EstateObjectStatus.DRAFT -> mainInfoCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.light_yellow
                        )
                    )
                    EstateObjectStatus.EMPTY -> mainInfoCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.light_red
                        )
                    )
                    else -> mainInfoCardView.setCardBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.light_red
                        )
                    )
                }
                with(childMainRecycler) {
                    layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                    adapter = MainInfoChildAdapter(item.first.counters)
                    suppressLayout(true)
//                    isLayoutFrozen = true
                }
            }
        }
    }

    class MainInfoDiffUtilCallback :
        DiffUtil.ItemCallback<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>>() {
        override fun areItemsTheSame(
            oldItem: Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>,
            newItem: Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>
        ): Boolean {
            return (
                    oldItem.first.estateObjectFootprint == newItem.first.estateObjectFootprint &&
                            oldItem.second?.estateObjectFootprint == newItem.second?.estateObjectFootprint)
        }

        override fun areContentsTheSame(
            oldItem: Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>,
            newItem: Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>
        ): Boolean {
            return oldItem == newItem
        }
    }
}