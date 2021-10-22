package com.appdeveloper.workmanagerdemo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.NonNull
import androidx.core.app.NotificationCompat
import androidx.work.*

class MyWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {
    @NonNull
    override fun doWork(): Result {
        val data = inputData
        val desc = data.getString(MainActivity.KEY_TASK_DESC)
        displayNotification("Hey I am your work ", desc)
        val output: Data = Data.Builder()
            .putString(KEY_TASK_OUTPUT, "Task Finished Successfully")
            .build()
        return Result.success(output)
    }
    private fun displayNotification(task: String, desc: String?) {
        val manager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "workManagerNotification",
                "mynotification",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        val r = (Math.random() * 9999 + 1).toInt()
        val builder: NotificationCompat.Builder = NotificationCompat.Builder(applicationContext, "workManagerNotification")
            .setContentTitle(task)
            .setContentText(desc)
            .setSmallIcon(R.mipmap.ic_launcher)
        manager.notify(r, builder.build())
    }

    companion object {
        const val KEY_TASK_OUTPUT = "key_task_output"
    }
}
