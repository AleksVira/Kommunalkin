package ru.virarnd.kommunalkin.ui.detailinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.Timber
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.databinding.FragmentDetailInfoBinding
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoFragment : Fragment() {
    private lateinit var viewModel: DetailInfoViewModel
    private lateinit var binding: FragmentDetailInfoBinding
    private lateinit var status: EstateObjectStatus
    private lateinit var adapter: DetailInfoAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_info, container, false)
        val arguments: DetailInfoFragmentArgs by navArgs()

        val detailInfoViewModelFactory = DetailInfoViewModel.DetailInfoViewModelFactory(
            arguments.prevFootprintId,
            arguments.nowFootprintId,
            arguments.status
        )
        viewModel = ViewModelProviders.of(this, detailInfoViewModelFactory).get(DetailInfoViewModel::class.java)

        Timber.d { "Vira_DetailInfoFragment Получил: 1 ID = ${arguments.nowFootprintId}, 2 ID = ${arguments.prevFootprintId}, статус = ${arguments.status.name}" }
        status = arguments.status
        val detailInfoLayoutManager = LinearLayoutManager(context)
        adapter = DetailInfoAdapter(viewModel, status)


        with(binding) {
            lifecycleOwner = this@DetailInfoFragment
            detailInfoViewModel = viewModel
            detailRecyclerView.layoutManager = detailInfoLayoutManager
            detailRecyclerView.adapter = adapter
        }

        viewModel.countersList.observe(this, Observer { countersList ->
//            adapter.submitList(null)
            adapter.submitList(countersList.toMutableList())
            Timber.d { "Vira_DetailInfoFragment Name of the first: ${countersList[0].first.counterName}" }
        })

/*
        viewModel.listUpdated.observe(this, Observer {
            adapter.submitList(viewModel.countersList.value)
//            adapter.notifyDataSetChanged()
        })
*/

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveCurrent()
    }
}