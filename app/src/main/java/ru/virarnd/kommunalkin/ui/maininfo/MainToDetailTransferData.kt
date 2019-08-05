package ru.virarnd.kommunalkin.ui.maininfo

import ru.virarnd.kommunalkin.models.EstateObjectStatus

class MainToDetailTransferData(
    val previousFootprintId: Long = 0,
    val nowFootprintId: Long = 0,
    val currentStateStatus: EstateObjectStatus = EstateObjectStatus.EMPTY)