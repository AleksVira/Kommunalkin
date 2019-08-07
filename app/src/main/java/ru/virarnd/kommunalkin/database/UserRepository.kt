package ru.virarnd.kommunalkin.database

import androidx.lifecycle.LiveData
import ru.virarnd.kommunalkin.models.*
import ru.virarnd.kommunalkin.ui.KommunalkinApp

object UserRepository {

    lateinit var currentUser: User

    private var userDao: UserDao
    private var estateObjectFootprintDao: EstateObjectFootprintDao
    private var counterDao: CounterDao
    //    private var allUsers: LiveData<List<User>>
    private lateinit var mainUser: User

    init {
        val database = UserDatabase.getInstance(KommunalkinApp.applicationContext())
        userDao = database.userDao()
        estateObjectFootprintDao = database.estateObjectDao()
        counterDao = database.counterDao()
//        allUsers = userDao.getAllUsers()
    }


    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    fun update(user: User) {
        userDao.update(user)
    }

    fun delete(user: User) {
        userDao.delete(user)
    }

    fun clear() {
        userDao.clear()
    }

    fun getAllUsers(): LiveData<List<User>> = userDao.getAllUsers()

    suspend fun getUserByLogin(login: String): User? {
        return userDao.getUserByLogin(login)
    }

    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }


    fun loginIsFree(testUser: User): Boolean {
        return true
    }

    suspend fun addNewUser(testUser: User) {
        userDao.insert(testUser)
    }

    suspend fun addNewUserAndGetId(testUser: User): Long {
        return userDao.insert(testUser)
    }

    suspend fun initUserDb() {
        val usersList = arrayListOf(
            User(login = "1", password = "1", userId = 1),
            User(login = "67889", password = "0000", userId = 2),
            User(login = "aass", password = "00", userId = 3),
            User(login = "66", password = "6"),
            User(login = "77", password = "7"),
            User(login = "88", password = "8")
        )
        userDao.insertAll(usersList)

        val estateObjectsList = arrayListOf(
            EstateObjectFootprint(footprintId = 1L, ownerId = 1, name = "Квартира 1", comment = "Отличный вариант", objectDate = 201907, objectId = 10, status = EstateObjectStatus.COMPLETED),
            EstateObjectFootprint(footprintId = 2L, ownerId = 1, name = "Квартира 2", comment = "Вариант попроще", objectDate = 201907, objectId = 11, status = EstateObjectStatus.COMPLETED),
            EstateObjectFootprint(footprintId = 3L, ownerId = 1, name = "Квартира 3", comment = "Ужасный вариант", objectDate = 201907, objectId = 12, status = EstateObjectStatus.COMPLETED),
            EstateObjectFootprint(footprintId = 4L, ownerId = 1, name = "Квартира 1", comment = "Отличный вариант", objectDate = 201908, objectId = 10, status = EstateObjectStatus.COMPLETED),
            EstateObjectFootprint(footprintId = 5L, ownerId = 1, name = "Квартира 2", comment = "Вариант попроще", objectDate = 201908, objectId = 11, status = EstateObjectStatus.DRAFT),
            EstateObjectFootprint(footprintId = 6L, ownerId = 1, name = "Квартира 3", comment = "Ужасный вариант", objectDate = 201908, objectId = 12, status = EstateObjectStatus.EMPTY),
            EstateObjectFootprint(footprintId = 7L, ownerId = 1, name = "Квартира 4", comment = "Компромиссный вариант", objectDate = 201907, objectId = 13, status = EstateObjectStatus.COMPLETED)
            )
        estateObjectFootprintDao.insertAll(estateObjectsList)

        val countersList = arrayListOf(
            Counter(estateObjectFootprintId = 1, counterType = CounterType.COLD_WATER, counterName = "Холодая вода 1", counterReading = 55.5),
            Counter(estateObjectFootprintId = 1, counterType = CounterType.COLD_WATER, counterName = "Холодая вода 2", counterReading = 126.6),
            Counter(estateObjectFootprintId = 1, counterType = CounterType.HOT_WATER, counterName = "Горячая вода 1", counterReading = 17.0),
            Counter(estateObjectFootprintId = 1, counterType = CounterType.HOT_WATER, counterName = "Горячая вода 2", counterReading = 80.1),
            Counter(estateObjectFootprintId = 1, counterType = CounterType.ELECTRICITY, counterName = "Свет", counterReading = 930.7),
            Counter(estateObjectFootprintId = 1, counterType = CounterType.GAS, counterName = "Газ", counterReading = 16.95),
            Counter(estateObjectFootprintId = 2, counterType = CounterType.COLD_WATER, counterName = "Хол. вода", counterReading = 145.5),
            Counter(estateObjectFootprintId = 2, counterType = CounterType.HOT_WATER, counterName = "Гор. вода", counterReading = 260.0),
            Counter(estateObjectFootprintId = 2, counterType = CounterType.ELECTRICITY, counterName = "Электричество", counterReading = 1020.7),
            Counter(estateObjectFootprintId = 2, counterType = CounterType.GAS, counterName = "Газ", counterReading = 98.95),
            Counter(estateObjectFootprintId = 3, counterType = CounterType.COLD_WATER, counterName = "Холодая вода", counterReading = 345.5),
            Counter(estateObjectFootprintId = 3, counterType = CounterType.HOT_WATER, counterName = "Горячая вода", counterReading = 290.0),
            Counter(estateObjectFootprintId = 3, counterType = CounterType.ELECTRICITY, counterName = "Свет", counterReading = 7610.7),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.COLD_WATER, counterName = "Холодая вода 1", counterReading = 65.5),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.COLD_WATER, counterName = "Холодая вода 2", counterReading = 136.6),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.HOT_WATER, counterName = "Горячая вода 1", counterReading = 20.0),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.HOT_WATER, counterName = "Горячая вода 2", counterReading = 85.1),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.ELECTRICITY, counterName = "Свет", counterReading = 950.7),
            Counter(estateObjectFootprintId = 4, counterType = CounterType.GAS, counterName = "Газ", counterReading = 18.95),
            Counter(estateObjectFootprintId = 5, counterType = CounterType.COLD_WATER, counterName = "Хол. вода", counterReading = 165.5),
            Counter(estateObjectFootprintId = 5, counterType = CounterType.HOT_WATER, counterName = "Гор. вода", counterReading = 280.0),
            Counter(estateObjectFootprintId = 5, counterType = CounterType.ELECTRICITY, counterName = "Электричество", counterReading = 1250.7),
            Counter(estateObjectFootprintId = 5, counterType = CounterType.GAS, counterName = "Газ", counterReading = 118.95),
            Counter(estateObjectFootprintId = 6, counterType = CounterType.COLD_WATER, counterName = "Холодая вода", counterReading = 365.5),
            Counter(estateObjectFootprintId = 6, counterType = CounterType.HOT_WATER, counterName = "Горячая вода", counterReading = 320.0),
            Counter(estateObjectFootprintId = 6, counterType = CounterType.ELECTRICITY, counterName = "Свет", counterReading = 7950.7),

            Counter(estateObjectFootprintId = 7, counterType = CounterType.COLD_WATER, counterName = "Холодая водичка", counterReading = 105.8),
            Counter(estateObjectFootprintId = 7, counterType = CounterType.HOT_WATER, counterName = "Горячая водичка", counterReading = 90.0)
            )
        counterDao.insertAll(countersList)
    }

    suspend fun getAllUsersDataById(userId: Long): UserAndEstateObjects? {
        return userDao.getUserAndEstateObjectsById(userId)
    }

//    suspend fun getAllUsersLiveDataById(userId: Long): LiveData<UserAndEstateObjects> {
//        return userDao.getLiveUserAndEstateObjectsById(userId)
//    }

    suspend fun getAllObjectsDataById(userId: Long): List<EstateObjectFootprintAndCounters> {
        return estateObjectFootprintDao.getEstateObjectAndCountersByOwnerId(userId)
    }

    suspend fun getAllObjectDataByUserIdAndDate(userId: Long, yearAndMonth: Int): List<EstateObjectFootprintAndCounters>? {
        return estateObjectFootprintDao.getEstateObjectAndCountersByOwnerIdAndDate(userId, yearAndMonth)
    }

    suspend fun selectCountersByFootprint(prevFootprintId: Long): List<Counter>? {
        return counterDao.selectCountersByFootprint(prevFootprintId)
    }

    suspend fun saveCounterById(counterToSave: Counter) {
        counterDao.update(counterToSave)
    }

    suspend fun getEstateObjectFootprintByFootprintId(footprintId: Long) : EstateObjectFootprint? {
        return estateObjectFootprintDao.getByEstateObjectId(footprintId)
    }

    suspend fun updateEstateObjectFootprint(estateObjectFootprint: EstateObjectFootprint) {
        estateObjectFootprintDao.update(estateObjectFootprint)
    }

//    suspend fun getLiveEstateObjectDataByOwnerId(userId: Long): LiveData<List<EstateObjectFootprint>> {
//        return estateObjectFootprintDao.getLiveEstateObjectDataByOwnerId(userId)
//    }
}
