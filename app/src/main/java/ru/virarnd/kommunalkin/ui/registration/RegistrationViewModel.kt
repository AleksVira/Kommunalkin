package ru.virarnd.kommunalkin.ui.registration

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.common.*
import ru.virarnd.kommunalkin.database.UserRepository
import ru.virarnd.kommunalkin.models.User

class RegistrationViewModel : ViewModel() {

    private val userRepository = UserRepository

    private val _loginFieldError: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val loginFieldError: LiveData<Int>
        get() = _loginFieldError

    private val _passwordOneFieldError: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val passwordOneFieldError: LiveData<Int>
        get() = _passwordOneFieldError

    private val _passwordTwoFieldError: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }
    val passwordTwoFieldError: LiveData<Int>
        get() = _passwordTwoFieldError

    private val _newUserCreated: SingleLiveEvent<Long> by lazy { SingleLiveEvent<Long>() }
    val newUserCreated: LiveData<Long>
        get() = _newUserCreated


    init {
        clearErrors()
        _newUserCreated.value = null
    }


    fun onRegistrationClick(testLogin: String, testOnePassword: String, testTwoPassword: String) {
        var errorBlankField = false
        clearErrors()

        if (testLogin.isBlank()) {
            _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
            errorBlankField = true
        }
        if (testOnePassword.isBlank()) {
            _passwordOneFieldError.value = INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
            errorBlankField = true
        }
        if (testTwoPassword.isBlank()) {
            _passwordTwoFieldError.value = INPUT_ERROR_CHECK_STATUS_EMPTY_FIELD
            errorBlankField = true
        }
        if (errorBlankField) return

        if (testOnePassword != testTwoPassword) {
            _passwordTwoFieldError.value = INPUT_ERROR_CHECK_STATUS_DOES_NOT_MATCH
            return
        }

        val testUser = User(login = testLogin, password = testOnePassword)
        viewModelScope.launch() {
            val dbUser = userRepository.getUserByLogin(testUser.login)
            if (dbUser == null) {
                val newUserId = userRepository.addNewUserAndGetId(testUser)
                _newUserCreated.value = newUserId
            } else {
                _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_ALREADY_EXIST
            }
        }
    }

    private fun clearErrors() {
        _loginFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
        _passwordOneFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
        _passwordTwoFieldError.value = INPUT_ERROR_CHECK_STATUS_NO_ERRORS
    }

    fun onTransitionFromRegistrationFragmentComplete() {
        _newUserCreated.value = null
    }


/*
    class RegistrationViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegistrationViewModel::class.java)) {
                return RegistrationViewModel(application) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
*/
}

