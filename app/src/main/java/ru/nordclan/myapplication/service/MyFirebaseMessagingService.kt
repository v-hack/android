package ru.nordclan.myapplication.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import ru.nordclan.myapplication.MyApplication
import ru.nordclan.myapplication.R
import ru.nordclan.myapplication.api.Api
import javax.inject.Inject

class MyFirebaseMessagingService : FirebaseMessagingService() {

    companion object {

        const val NOTIFICATION_ID = 1
    }

    @Inject
    lateinit var api: Api

    private var disposable: Disposable? = null

    override fun onCreate() {
        super.onCreate()
        (applicationContext as MyApplication).appComponent.inject(this)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        message.notification?.let { n ->
            val title = n.title ?: getString(R.string.app_name)
            val body = n.body ?: getString(R.string.app_name)
            showNotification(title, body)
        }
    }

    override fun onNewToken(token: String) {
        disposable = api.sendToken(token)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    override fun onDestroy() {
        disposable?.dispose()
        super.onDestroy()
    }

    private fun showNotification(title: String, text: String) {
        val channelId = getString(R.string.default_notification_channel_id)
        val notification = notification(title, text, channelId)
        NotificationManagerCompat.from(this).let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    channelId,
                    getString(R.string.default_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH
                )
                it.createNotificationChannel(channel)
            }
            it.notify(NOTIFICATION_ID, notification)
        }
    }

    private fun notification(
        title: String,
        text: String,
        channelId: String
    ): Notification =
        NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_ava)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .build()
}
