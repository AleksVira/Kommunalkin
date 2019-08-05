package ru.virarnd.kommunalkin.ui.maininfo

import android.widget.Toast
import androidx.lifecycle.*
import com.github.ajalt.timberkt.Timber
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.virarnd.kommunalkin.common.SingleLiveEvent
import ru.virarnd.kommunalkin.database.EstateObjectFootprintAndCounters
import ru.virarnd.kommunalkin.database.UserRepository
import java.text.SimpleDateFormat
import java.util.*

class MainInfoViewModel(val userId: Long) : ViewModel() {

    private val repository = UserRepository
    private val currentYearAndMont: Int
    private val previousYearAndMont: Int

    private val _estateObjectsListFootprint: MutableLiveData<List<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>>> by lazy { MutableLiveData<List<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>>>() }
    val estateObjectsListFootprint: LiveData<List<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>>>
        get() = _estateObjectsListFootprint

    private val _navigateToDetailViewWithTwoFootprints: SingleLiveEvent<MainToDetailTransferData> by lazy {SingleLiveEvent<MainToDetailTransferData>()}
    val navigateToDetailViewWithTwoFootprints: LiveData<MainToDetailTransferData>
        get() = _navigateToDetailViewWithTwoFootprints

    private val _navigateToLogin: SingleLiveEvent<Boolean> by lazy {SingleLiveEvent<Boolean>()}
    val navigateToLogin: LiveData<Boolean>
        get() = _navigateToLogin


    init {
        currentYearAndMont = currentDateInt();
        previousYearAndMont = currentDateInt() - 1
//        Timber.d{"Текущая дата: $currentYearAndMont, прошлая дата: $previousYearAndMont"}
        viewModelScope.launch {
            //Достаю из базы по userId и previousYearAndMont все объекты.
            // Затем у них анализирую данные по текущему месяцу, смотреть кто есть и с каким статусом.
            // Т.е. данные показываются из прошлого месяца, а статус смотрю у текущего. Соединяю в List<Pair<>>
            val pairDataFromTwoMonth = mutableListOf<Pair<EstateObjectFootprintAndCounters, EstateObjectFootprintAndCounters?>>()
            val dataFromPreviousMonth = repository.getAllObjectDataByUserIdAndDate(userId, previousYearAndMont)
            val dataFromCurrentMonth = repository.getAllObjectDataByUserIdAndDate(userId, currentYearAndMont)
            dataFromPreviousMonth?.forEach {savedDataFromPreviousDate ->
                val objectIdFromSearch = savedDataFromPreviousDate.estateObjectFootprint.objectId
                val dataFromCurrentDateWithTheSameObjectId = dataFromCurrentMonth?.find { estateObjectFootprintAndCounters ->
                    estateObjectFootprintAndCounters.estateObjectFootprint.objectId == objectIdFromSearch }
                pairDataFromTwoMonth.add(Pair(savedDataFromPreviousDate, dataFromCurrentDateWithTheSameObjectId))
            }
            _estateObjectsListFootprint.postValue(pairDataFromTwoMonth)
            Timber.d{"Got it!!! ${dataFromPreviousMonth}"}
        }
    }

    private fun currentDateInt(): Int {
        val sdf = SimpleDateFormat("yyyyMM", Locale.forLanguageTag("RU"))
        val currentDate = sdf.format(Date())
        return currentDate.toInt()
    }

    fun onClickCard(firstEstateObjectFootprint: EstateObjectFootprintAndCounters, secondEstateObjectFootprint: EstateObjectFootprintAndCounters) {
        Timber.d{"Объект ${firstEstateObjectFootprint.estateObjectFootprint.name}, дата ${secondEstateObjectFootprint.estateObjectFootprint.objectDate}, статус ${secondEstateObjectFootprint.estateObjectFootprint.status.name}"}
        val transferData = MainToDetailTransferData(firstEstateObjectFootprint.estateObjectFootprint.footprintId, secondEstateObjectFootprint.estateObjectFootprint.footprintId, secondEstateObjectFootprint.estateObjectFootprint.status)
        _navigateToDetailViewWithTwoFootprints.value = transferData
    }


    fun onMenuSelected() {

    }

    class MainInfoViewModelFactory(private val userId: Long) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainInfoViewModel::class.java)) {
                return MainInfoViewModel(userId) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
