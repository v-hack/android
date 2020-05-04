package ru.nordclan.myapplication.dagger

import dagger.Component
import ru.nordclan.myapplication.activity.BaseActivity
import ru.nordclan.myapplication.fragment.BaseFragment
import ru.nordclan.myapplication.service.MyFirebaseMessagingService
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, TokenModule::class])
interface AppComponent {

    fun inject(activity: BaseActivity)

    fun inject(fragment: BaseFragment)

    fun inject(service: MyFirebaseMessagingService)

//    fun inject(receiver: BaseBroadcastReceiver)
//    fun inject(module: CustomGlideModule)
}
