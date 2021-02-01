package com.demo.fishdemoxhb.test

import android.app.Application
import androidx.lifecycle.Transformations
import com.demo.fishdemoxhb.quick.mvvm.QuickBaseViewModel

class DemoViewModel(application: Application) : QuickBaseViewModel<DemoModel>(application) {

    val serverData = model.testResourceData

    val serverDataAnalyze = Transformations.map(model.testResourceData) {
        if (it % 2 == 0L) {
            "Value $it is \n" +
                    "even number"
        } else {
            "Value $it is \n" +
                    "odd number"
        }
    }

    fun fetchData() {
        model.fetchTestData()
    }

    init {
        fetchData()
    }

}