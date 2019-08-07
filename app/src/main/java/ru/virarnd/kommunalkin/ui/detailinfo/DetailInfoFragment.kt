package ru.virarnd.kommunalkin.ui.detailinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.databinding.FragmentDetailInfoBinding
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoFragment : Fragment() {
    private lateinit var viewModel: DetailInfoViewModel
    private lateinit var binding: FragmentDetailInfoBinding
    private lateinit var status: EstateObjectStatus
    private lateinit var adapter: DetailInfoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                Timber.d { "Vira_DetailInfoFragment handleOnBackPressed()" }
                //TODO Реализовать обработку нажатия назад: сохранение валидных и пр.
                when (status) {
                    EstateObjectStatus.COMPLETED -> findNavController().navigateUp()
                    else -> findNavController().navigateUp()

                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_info, container, false)
        val arguments: DetailInfoFragmentArgs by navArgs()

        val detailInfoViewModelFactory = DetailInfoViewModel.DetailInfoViewModelFactory(
            arguments.prevFootprintId,
            arguments.nowFootprintId,
            arguments.status
        )
        viewModel = ViewModelProviders.of(this, detailInfoViewModelFactory).get(DetailInfoViewModel::class.java)

        status = arguments.status
        val detailInfoLayoutManager = LinearLayoutManager(context)
        adapter = DetailInfoAdapter(viewModel, status)


        with(binding) {
            lifecycleOwner = this@DetailInfoFragment
            detailInfoViewModel = viewModel
            detailRecyclerView.layoutManager = detailInfoLayoutManager
            detailRecyclerView.adapter = adapter
            detailRecyclerView.hasFixedSize()
        }

        viewModel.countersList.observe(this, Observer { countersList ->
            adapter.submitList(countersList)
        })

        viewModel.listItemUpdated.observe(this, Observer { position ->
            var cardView = binding.detailRecyclerView.layoutManager?.findViewByPosition(position) as CardView
            var editText = cardView.findViewById<TextInputEditText>(R.id.et_reading_now)
            val cursorPosition = editText.selectionStart

            viewModel.viewModelScope.launch {
                adapter.notifyItemChanged(position)
                delay(50)
                cardView = binding.detailRecyclerView.layoutManager?.findViewByPosition(position) as CardView
                editText = cardView.findViewById<TextInputEditText>(R.id.et_reading_now)
                editText.setSelection(cursorPosition)
            }
        })

        viewModel.validationResponse.observe(this, Observer {
            Timber.d{"Vira_DetailInfoFragment ErrorStatus: ${it.status}"}
            when (it.status) {
                CheckResponse.Status.ERROR -> {
                    Snackbar.make(container!!, it.message, Snackbar.LENGTH_LONG).show()
                }
                else -> {
                    Snackbar.make(binding.detailLayout, it.message, Snackbar.LENGTH_LONG).show()
                }
            }
        })

        viewModel.navigateToMainInfoFragment.observe(this, Observer {
            Timber.d{"Vira_DetailInfoFragment navigateToMainInfoFragment"}
//            findNavController().navigateUp()
            findNavController().navigate(DetailInfoFragmentDirections.actionDetailInfoFragmentToMainInfoFragment(it))
        })


        return binding.root
    }



    override fun onPause() {
        super.onPause()
        viewModel.saveAllValidCounters()
    }
}