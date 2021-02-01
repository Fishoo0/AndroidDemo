package com.demo.fishdemoxhb.test

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.demo.fishdemoxhb.quick.mvvm.QuickBaseModel

class DemoModel(application: Application) : QuickBaseModel(application) {

    val testResourceData = MutableLiveData<Long>(0)

    fun fetchTestData() {
        testResourceData.value = System.currentTimeMillis()
    }
}