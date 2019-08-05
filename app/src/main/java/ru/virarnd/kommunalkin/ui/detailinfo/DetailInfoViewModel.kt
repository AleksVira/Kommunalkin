package ru.virarnd.kommunalkin.ui.detailinfo

import androidx.lifecycle.*
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.common.SingleLiveEvent
import ru.virarnd.kommunalkin.database.UserRepository
import ru.virarnd.kommunalkin.models.Counter
import ru.virarnd.kommunalkin.models.EstateObjectStatus

class DetailInfoViewModel(private val prevFootprintId: Long, private val nowFootprintId: Long, val status: EstateObjectStatus) :
    ViewModel() {

    private val repository = UserRepository

//    private val _countersList: MutableLiveData<MutableList<Pair<Counter, Double>>> by lazy { MutableLiveData<MutableList<Pair<Counter, Double>>>() }
    private val _countersList: MutableLiveData<MutableList<Pair<Counter, Double>>> = MutableLiveData<MutableList<Pair<Counter, Double>>>()
    val countersList: LiveData<MutableList<Pair<Counter, Double>>>
        get() = _countersList

/*
    private val _listUpdated: SingleLiveEvent<Boolean> by lazy {SingleLiveEvent<Boolean>()}
    val listUpdated: SingleLiveEvent<Boolean>
        get() = _listUpdated
*/


    init {
//        _listUpdated.value = false
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

    fun afterReadingTextChanged(s: CharSequence, counter: Counter) {
        val liveDataElementToChange = _countersList.value?.firstOrNull { pair -> pair.first.counterStateId == counter.counterStateId}
        val counterIndex = countersList.value?.indexOf(liveDataElementToChange) ?: -1
//        val newReading = Integer.parseInt(s.toString())
        val newReading = s.toString().toDouble()
        Timber.d { "Vira_DetailInfoViewModel $s, counter: ${counter.counterName}, index: $counterIndex, parameter = $newReading" }
        if (counterIndex > -1) {
            val tmpPairList = countersList.value
            tmpPairList?.get(counterIndex)?.first?.counterReading = newReading
            _countersList.value = tmpPairList
//            _listUpdated.value = true
            Timber.d{"Vira_DetailInfoViewModel Try to update List"}

//            val newCounter = liveDataElementToChange?.first
//            newCounter?.counterReading = newReading
//            val newPair: Pair<Counter, Double> = Pair (newCounter!!, newReading)
//            tmpPairList?.set(counterIndex, newPair)
//            _countersList.value = tmpPairList

//            tmpPairList?.set(counterIndex, liveDataElementToChange?.copy().first.counterReading)
//            tmpPairList[counterIndex].first.counterReading = newReading


//            var readingFromPrevious = liveDataElementToChange?.second
//            newCounter.counterReading = newReading

//            var newPair = liveDataElementToChange.
//            newPair?.first.counterReading = newReading
//            _countersList.value.set(counterIndex, )
//            _countersList.value?.get(counterIndex)?.first?.counterReading = newReading
        }
    }

    fun saveCurrent() {
        Timber.d{"Vira_DetailInfoViewModel ID for save: ${nowFootprintId}"}
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
