package ru.virarnd.kommunalkin.ui.detailinfo

import androidx.lifecycle.*
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.common.SingleLiveEvent
import ru.virarnd.kommunalkin.database.UserRepository
import ru.virarnd.kommunalkin.models.Counter
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoViewModel(
    private val prevFootprintId: Long,
    private val nowFootprintId: Long,
    val status: EstateObjectStatus
) :
    ViewModel() {

    private val repository = UserRepository
    private var searchFor = ""

    private val _countersList: MutableLiveData<MutableList<Pair<Counter, Double>>> =
        MutableLiveData<MutableList<Pair<Counter, Double>>>()
    val countersList: LiveData<MutableList<Pair<Counter, Double>>>
        get() = _countersList

    private val _listItemUpdated: SingleLiveEvent<Int> by lazy { SingleLiveEvent<Int>() }
    val listItemUpdated: SingleLiveEvent<Int>
        get() = _listItemUpdated


    init {
//        _listItemUpdated.value = -1
        viewModelScope.launch {
            val pairDataFromTwoMonths = mutableListOf<Pair<Counter, Double>>()
            val countersFromCurrentMonth = repository.selectCountersByFootprint(nowFootprintId)
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
        }
    }

    fun saveCurrent() {
        Timber.d { "Vira_DetailInfoViewModel ID for save: ${nowFootprintId}" }
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
