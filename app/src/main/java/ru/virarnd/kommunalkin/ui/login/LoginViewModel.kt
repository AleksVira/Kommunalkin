package ru.virarnd.kommunalkin.ui.login

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.common.*
import ru.virarnd.kommunalkin.models.User
import ru.virarnd.kommunalkin.database.UserRepository


class LoginViewModel: ViewModel() {

    private val userRepository = UserRepository

    private val _loginFieldError: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val loginFieldError: LiveData<Int>
        get() = _loginFieldError

    private val _passwordFieldError: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val passwordFieldError: LiveData<Int>
        get() = _passwordFieldError

    private val _idUserLoggedIn: SingleLiveEvent<Long> by lazy { SingleLiveEvent<Long>() }
    val idUserLoggedIn: LiveData<Long>
        get() = _idUserLoggedIn

    init {
        _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
        _passwordFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
        _idUserLoggedIn.value = null

        viewModelScope.launch() {
            userRepository.initUserDb()
        }

    }

    fun onLoginCheckFields(testLogin: String, testPassword: String) {
        var inputError = false
//        Timber.d { "onLoginCheckFields() run !!!" }
//        Timber.d { "login = $testLogin, password = $testPassword" }

        _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
        _passwordFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS

        if (testLogin.isBlank()) {
            _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
            inputError = true
        }
        if (testPassword.isBlank()) {
            _passwordFieldError.value = INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
            inputError = true
        }
        if (inputError) return

        val testUser = User(login = testLogin, password = testPassword)

        viewModelScope.launch() {
            val dbUser = userRepository.getUserByLogin(testUser.login)
            if (testUser.login == dbUser?.login) {
                if (testUser.password == dbUser.password) {
                    _idUserLoggedIn.value = dbUser.userId
                } else {
                    _passwordFieldError.value = INPUT_ERROR_CHECK_STATUS_NOT_SUITABLE
                }
            } else {
                _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_UNKNOWN_TEXT
            }
        }
    }

/*
    fun onTransitionFromLoginFragmentToMainInfoFragmentComplete() {
        _idUserLoggedIn.value = null
    }
*/

}


