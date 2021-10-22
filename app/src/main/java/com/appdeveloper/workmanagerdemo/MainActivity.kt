package com.appdeveloper.workmanagerdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.*
import com.appdeveloper.workmanagerdemo.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        val KEY_TASK_DESC = "key_task_desc"
    }
   lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data: Data = Data.Builder()
            .putString(KEY_TASK_DESC, "Hey I am sending the work data")
            .build()
        val constraints: Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        val request: OneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .setConstraints(constraints)
            .addTag("onetime")
            .build()
        val periodicWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setInputData(data)
                .addTag("periodic")
                .setConstraints(constraints)
                .build()
        binding.oneTimeRequest.setOnClickListener {
            WorkManager.getInstance(this).enqueue(request)
        }
        binding.periodicRequest.setOnClickListener {
            WorkManager.getInstance(this).enqueue(periodicWorkRequest)
        }
        binding.cancelAll.setOnClickListener { WorkManager.getInstance(this).cancelAllWork() }

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.id)
            .observe(this, { workInfo ->
                if (workInfo != null) {
                    if (workInfo.state.isFinished) {
                        val data: Data = workInfo.outputData
                        val output: String = data?.getString(MyWorker.KEY_TASK_OUTPUT)?:""
                       binding.textView.append(output+"\n")
                    }
                    val status = workInfo.state.name
                    binding.textView.append(status+"\n")
                }
            })
    }
}
