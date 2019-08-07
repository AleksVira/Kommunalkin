package ru.virarnd.kommunalkin.ui.detailinfo

import androidx.lifecycle.*
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import ru.virarnd.kommunalkin.common.SingleLiveEvent
import ru.virarnd.kommunalkin.database.UserRepository
import ru.virarnd.kommunalkin.models.Counter
import ru.virarnd.kommunalkin.models.EstateObjectFootprint
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoViewModel(
    private val prevFootprintId: Long,
    private val nowFootprintId: Long,
    val status: EstateObjectStatus
) :
    ViewModel() {

    private var countersFromCurrentMonth: List<Counter>? = null
    private val repository = UserRepository
    private var searchFor = ""

    private val _countersList: MutableLiveData<MutableList<Pair<Counter, Double>>> =
        MutableLiveData<MutableList<Pair<Counter, Double>>>()
    val countersList: LiveData<MutableList<Pair<Counter, Double>>>
        get() = _countersList

    private val _listItemUpdated: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val listItemUpdated: SingleLiveEvent<Int>
        get() = _listItemUpdated

    private val _validationResponse: SingleLiveEvent<CheckResponse> by lazy { SingleLiveEvent<CheckResponse>() }
    val validationResponse: SingleLiveEvent<CheckResponse>
        get() = _validationResponse

    private val _navigateToMainInfoFragment: SingleLiveEvent<Long> by lazy { SingleLiveEvent<Long>() }
    val navigateToMainInfoFragment: LiveData<Long>
        get() = _navigateToMainInfoFragment


    init {
        viewModelScope.launch {
            val pairDataFromTwoMonths = mutableListOf<Pair<Counter, Double>>()
            countersFromCurrentMonth = repository.selectCountersByFootprint(nowFootprintId)
            val countersFromPreviousMonth = repository.selectCountersByFootprint(prevFootprintId)

            countersFromPreviousMonth?.forEach { fromPrevMonth ->
                val prevCounterName = fromPrevMonth.counterName
                val counterFromCurrentMonthWitTheSameName = countersFromCurrentMonth?.find { fromCurrentMonth ->
                    fromCurrentMonth.counterName == prevCounterName
                }
                pairDataFromTwoMonths.add(
                    Pair(
                        counterFromCurrentMonthWitTheSameName ?: Counter(
                            counterType = fromPrevMonth.counterType,
                            counterName = fromPrevMonth.counterName
                        ),
                        fromPrevMonth.counterReading
                    )
                )
            }
            _countersList.postValue(pairDataFromTwoMonths)
            Timber.d { "Vira_DetailInfoViewModel New list is ready!" }
        }
    }

    fun afterReadingTextChanged(s: CharSequence, counter: Counter, position: Int) {
        var inputString = s.toString()
        if (inputString.isEmpty()) {
            inputString = "0"
        }
        val newText = inputString.trim()
        if (newText == searchFor) return
        searchFor = newText
        // Делаю аналог debounce
        viewModelScope.launch {
            delay(500)  //debounce timeOut
            Timber.d { "newText = $newText, searchFor = $searchFor" }
            if (newText != searchFor) {
                return@launch
            }
            val newReading = inputString.toDouble()
            // Обновляю общий список
            _countersList.value?.get(position)?.first?.counterReading = newReading
            // и вызываю обновление одного элемента
            _listItemUpdated.value = position
            searchFor = ""
        }
    }


    fun onSendButtonClicked() {
        val allSavedCountersAreValid: Boolean = saveAllValidCounters()
        if (!allSavedCountersAreValid) {
            // Если хоть один расход отрицательный, данные отправить нельзя
            _validationResponse.value =
                CheckResponse(message = "Расход не может быть отрицательным!", status = CheckResponse.Status.ERROR)
            return
        }

        val listOfReadings = _countersList.value?.map { it -> it.first.counterReading.toString() }
        val dialogAnswer = showConfirmationInfoDialog(listOfReadings)
        if (dialogAnswer) {
            var userId = 0L;
            userId = changeStatusToCompleted(nowFootprintId)
            Timber.d { "Vira_DetailInfoViewModel UserId = $userId" }
            _validationResponse.value =
                CheckResponse(message = "Данные отправлены!", status = CheckResponse.Status.OK)

            viewModelScope.launch { delay(600) }
//            _navigateToMainInfoFragment.value = userId
            _navigateToMainInfoFragment.value = userId

        }
    }


    private fun changeStatusToCompleted(nowFootprintId: Long): Long {
        var estateObjectFootprint: EstateObjectFootprint? = null
        runBlocking {
            estateObjectFootprint = repository.getEstateObjectFootprintByFootprintId(nowFootprintId)
            estateObjectFootprint?.status = EstateObjectStatus.COMPLETED
            estateObjectFootprint?.let { repository.updateEstateObjectFootprint(it) }
        }
        Timber.d { "Vira_DetailInfoViewModel changeStatusToCompleted, footprintID = $nowFootprintId, check it for counters!!!" }
        return estateObjectFootprint?.ownerId ?: -1L
    }

    private fun showConfirmationInfoDialog(listOfReadings: List<String>?): Boolean {
        return true
    }

    fun saveAllValidCounters(): Boolean {
        //Сохраняю все валидные счётчики в БД.
        var allValid = true
        _countersList.value?.forEach { pair ->
            Timber.d { "Vira_DetailInfoViewModel calculate diff: ${pair.first.counterReading - pair.second}" }
            if ((pair.first.counterReading - pair.second) > 0) {
                viewModelScope.launch {
                    Timber.d { "Vira_DetailInfoViewModel Counter ID for save: ${pair.first.counterStateId}" }
                    async(IO) { repository.saveCounterById(pair.first) }.await()
                }
            } else {
                allValid = false
            }
        }
        return allValid
    }


    class DetailInfoViewModelFactory(
        private val prevFootprintId: Long,
        private val nowFootprintId: Long,
        val status: EstateObjectStatus
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailInfoViewModel::class.java)) {
                return DetailInfoViewModel(prevFootprintId, nowFootprintId, status) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
