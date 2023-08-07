package com.mo.sh.studyassistant.app

import android.app.Application
import androidx.annotation.StringRes
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App

        fun getString(@StringRes id: Int): String {
            return instance.getString(id)
        }
    }
}