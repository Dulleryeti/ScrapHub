package com.example.scraphub

import android.app.Application

class ScrapHubApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        ScrapRepository.initialize(this)
    }
}