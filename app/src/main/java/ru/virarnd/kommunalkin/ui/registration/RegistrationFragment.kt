package ru.virarnd.kommunalkin.ui.registration

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController

import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_ALREADY_EXIST
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_DOES_NOT_MATCH
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
import ru.virarnd.kommunalkin.databinding.FragmentRegistrationBinding
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_NO_ERRORS
import ru.virarnd.kommunalkin.database.UserDatabase

class RegistrationFragment : Fragment() {

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_registration, container, false)
        val application = requireNotNull(this.activity).application
        viewModel =
            ViewModelProviders.of(this).get(RegistrationViewModel::class.java)
//        viewModel =
//            ViewModelProviders.of(this, RegistrationViewModel.RegistrationViewModelFactory(application))
//                .get(RegistrationViewModel::class.java)

        binding.lifecycleOwner = this
        binding.registrationViewModel = viewModel

        viewModel.loginFieldError.observe(this, Observer { errorStatus ->
            binding.textLogin.error = null
            when (errorStatus) {
                INPUT_ERROR_CHECK_STATUS_NO_ERRORS -> binding.textInputLayoutLogin.error = null
                INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD -> {
                    binding.textInputLayoutLogin.error = "Incorrect empty login"
                    binding.textLogin.error = "Type something"
                }
                INPUT_ERROR_CHECK_STATUS_ALREADY_EXIST -> {
                    binding.textInputLayoutLogin.error = "Login already exist"
                    binding.textLogin.error = "Type another"
                }
            }
        })

        viewModel.passwordOneFieldError.observe(this, Observer { errorStatus ->
            binding.textPasswordOne.error = null
            when (errorStatus) {
                INPUT_ERROR_CHECK_STATUS_NO_ERRORS -> binding.textInputLayoutPasswordOne.error = null
                INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD -> {
                    binding.textInputLayoutPasswordOne.error = "Incorrect empty password"
                    binding.textPasswordOne.error = "Type something"
                }
            }
        })

        viewModel.passwordTwoFieldError.observe(this, Observer { errorStatus ->
            binding.textPasswordTwo.error = null
            when (errorStatus) {
                INPUT_ERROR_CHECK_STATUS_NO_ERRORS -> binding.textInputLayoutPasswordTwo.error = null
                INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD -> {
                    binding.textInputLayoutPasswordTwo.error = "Incorrect empty password"
                    binding.textPasswordTwo.error = "Type something"
                }
                INPUT_ERROR_CHECK_STATUS_DOES_NOT_MATCH -> {
                    binding.textInputLayoutPasswordTwo.error = "Password doesn't match"
                    binding.textPasswordTwo.error = "Type another"
                }
            }
        })

        viewModel.newUserCreated.observe(this, Observer { userId ->
            userId?.let {
                this.findNavController()
                    .navigate(RegistrationFragmentDirections.actionRegistrationFragmentToMainInfoFragment(userId))
//                viewModel.onTransitionFromRegistrationFragmentComplete()
            }
        })


        return binding.root
    }
}
