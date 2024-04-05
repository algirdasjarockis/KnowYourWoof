package com.ajrock.knowyourwoof

import android.app.Application
import android.util.Log
import com.ajrock.knowyourwoof.ioc.AppContainer

class App : Application() {

    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()

        container = AppContainer(this)
    }
}