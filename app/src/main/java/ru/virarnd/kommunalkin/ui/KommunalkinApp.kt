package ru.virarnd.kommunalkin.ui

import android.app.Application
import android.content.Context
import com.github.ajalt.timberkt.Timber

class KommunalkinApp : Application() {

    companion object{
        private var instance : KommunalkinApp? = null
        fun applicationContext(): Context{
            return instance!!.applicationContext
        }

    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}