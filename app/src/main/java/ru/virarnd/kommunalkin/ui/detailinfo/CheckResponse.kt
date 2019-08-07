package ru.virarnd.kommunalkin.ui.detailinfo

class CheckResponse(
    var message: String = "",
    var status: Status = Status.OK
) {
    enum class Status {ERROR, OK}
}