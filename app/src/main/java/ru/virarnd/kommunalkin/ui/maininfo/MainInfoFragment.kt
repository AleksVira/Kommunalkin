package ru.virarnd.kommunalkin.ui.maininfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.ajalt.timberkt.Timber
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail_info.*
import kotlinx.android.synthetic.main.fragment_main_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.databinding.FragmentMainInfoBinding

class MainInfoFragment : Fragment() {
    private lateinit var viewModel: MainInfoViewModel
    private lateinit var binding: FragmentMainInfoBinding
    private lateinit var adapter: MainInfoAdapter

    private var pressedOnce = false
    private lateinit var snackbar: Snackbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (pressedOnce) {
                    snackbar.dismiss()
                    findNavController().navigateUp()
                    return
                }
                pressedOnce = true
                snackbar.show()
                GlobalScope.launch {
                    delay(3000)
                    pressedOnce = false
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_info, container, false)
        snackbar = Snackbar.make(container!!, "Please click BACK again to logout", Snackbar.LENGTH_LONG)

        val arguments: MainInfoFragmentArgs by navArgs()
        val mainInfoLayoutManager = LinearLayoutManager(context)

        viewModel =
            ViewModelProviders.of(this, MainInfoViewModel.MainInfoViewModelFactory(arguments.userId))
                .get(MainInfoViewModel::class.java)
        adapter = MainInfoAdapter(viewModel)


        with(binding) {
            lifecycleOwner = this@MainInfoFragment
            mainInfoViewModel = viewModel
            mainRecyclerView.layoutManager = mainInfoLayoutManager
            mainRecyclerView.adapter = adapter
        }

        viewModel.estateObjectsListFootprint.observe(this, Observer { objectAndCountersList ->
            adapter.submitList(objectAndCountersList)
        })

        viewModel.navigateToDetailViewWithTwoFootprints.observe(this, Observer {
            it?.let { transferData ->
                this.findNavController().navigate(
                    MainInfoFragmentDirections.actionMainInfoFragmentToDetailInfoFragment(
                        transferData.previousFootprintId,
                        transferData.nowFootprintId,
                        transferData.currentStateStatus
                    )
                )
            }
        })

//        viewModel.onMenuSelected()

        return binding.root
    }


}