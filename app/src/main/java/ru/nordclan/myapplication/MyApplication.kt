package ru.nordclan.myapplication

import androidx.multidex.MultiDexApplication
import ru.nordclan.myapplication.dagger.ApiModule
import ru.nordclan.myapplication.dagger.AppComponent
import ru.nordclan.myapplication.dagger.DaggerAppComponent
import ru.nordclan.myapplication.dagger.TokenModule

class MyApplication : MultiDexApplication() {

    val appComponent: AppComponent = DaggerAppComponent.builder()
        .apiModule(
            ApiModule(
                mobileUrl = BuildConfig.MOBILE_URL,
                trace = BuildConfig.DEBUG
            )
        )
        .tokenModule(
            TokenModule(
//            TokenSupplierImpl()
            )
        )
        .build()
}
