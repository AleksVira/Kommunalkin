package ru.virarnd.kommunalkin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.github.ajalt.timberkt.Timber
import com.github.ajalt.timberkt.d
import com.idescout.sql.SqlScoutServer
import ru.virarnd.kommunalkin.R

class MainActivity : AppCompatActivity() {

//    protected inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
//        object : ViewModelProvider.Factory {
//            override fun <T : ViewModel> create(aClass: Class<T>):T = f() as T
//        }

    // viewModelFactory -- универсальная фабрика для создания ViewModule'й с параметрами, можно использовать примерно так:
//    binding.authViewModel = ViewModelProviders.of(
//    this,
//    viewModelFactory { MyViewModel("albert") }
//    ).get(AuthViewModel::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        Timber.d { "MainActivity.onCreate started" }
        val navCollection = findNavController(R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navCollection)
        SqlScoutServer.create(this, packageName);

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
