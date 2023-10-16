package com.datnt.app_quanlycongviec

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.Calendar
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Tạo và đặt lịch nhắc nhở công việc hàng ngày vào 6 giờ sáng
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
            .setRequiresCharging(false)
            .setRequiresBatteryNotLow(false)
            .build()

        val dailyWorkRequest = PeriodicWorkRequestBuilder<NotificationTask>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "daily_task_reminder", ExistingPeriodicWorkPolicy.KEEP, dailyWorkRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        val currentTimeMillis = System.currentTimeMillis()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 6)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val triggerTimeMillis = calendar.timeInMillis

        // Tính thời gian còn lại đến 6 giờ sáng
        var initialDelay = triggerTimeMillis - currentTimeMillis
        if (initialDelay < 0) {
            // Nếu đã quá 6 giờ sáng, đặt lại lịch cho ngày mai
            initialDelay += TimeUnit.DAYS.toMillis(1)
        }

        return initialDelay
    }
}

