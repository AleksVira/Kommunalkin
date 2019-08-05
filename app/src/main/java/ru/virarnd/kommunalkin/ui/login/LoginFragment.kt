package ru.virarnd.kommunalkin.ui.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController

import ru.virarnd.kommunalkin.R
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_NOT_SUITABLE
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_NO_ERRORS
import ru.virarnd.kommunalkin.common.INPUT_ERROR_CHECK_STATUS_UNKNOWN_TEXT
import ru.virarnd.kommunalkin.database.UserDatabase
import ru.virarnd.kommunalkin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        with(binding) {
            lifecycleOwner = this@LoginFragment
            loginViewModel = viewModel
            textPassword.setOnFocusChangeListener { _, _ -> turnOnPasswordToggleEnabled() }
            textPassword.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    turnOnPasswordToggleEnabled()
                }
            })
            btnRegistration.setOnClickListener { view ->
                binding.textLogin.text?.clear()
                binding.textPassword.text?.clear()
                view.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
            }
        }

        viewModel.loginFieldError.observe(this, Observer { errorStatus ->
            with(binding) {
                textLogin.error = null
                when (errorStatus) {
                    INPUT_ERROR_CHECK_STATUS_NO_ERRORS -> textInputLayoutLogin.error = null
                    INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD -> {
                        textInputLayoutLogin.error = "Incorrect empty login"
                        textLogin.error = "Type something"
                    }
                    INPUT_ERROR_CHECK_STATUS_UNKNOWN_TEXT -> textInputLayoutLogin.error = "Unknown user, register new?"
                    else -> textInputLayoutLogin.error = "Some mistake"
                }
            }
        })

        viewModel.passwordFieldError.observe(this, Observer { errorStatus ->
            with(binding) {
                textInputLayoutPassword.isPasswordVisibilityToggleEnabled = true
                textPassword.error = null
                when (errorStatus) {
                    INPUT_ERROR_CHECK_STATUS_NO_ERRORS -> textInputLayoutPassword.error = null
                    INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD -> {
                        textInputLayoutPassword.error = "Incorrect empty password"
                        textInputLayoutPassword.isPasswordVisibilityToggleEnabled = false
                        textPassword.error = "Type something"
                    }
                    INPUT_ERROR_CHECK_STATUS_NOT_SUITABLE -> textInputLayoutPassword.error = "Wrong password"
                    else -> textInputLayoutPassword.error = "Some mistake"
                }
            }
        })

        viewModel.idUserLoggedIn.observe(this, Observer { userId ->
            userId?.let {
                binding.textLogin.text?.clear()
                binding.textPassword.text?.clear()
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainInfoFragment(userId))
            }
        })

        return binding.root
    }

    private fun turnOnPasswordToggleEnabled() {
        binding.textInputLayoutPassword.isPasswordVisibilityToggleEnabled = true
        binding.textPassword.error = null
    }
}