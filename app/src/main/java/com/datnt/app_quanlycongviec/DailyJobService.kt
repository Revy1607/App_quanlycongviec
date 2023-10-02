package com.datnt.app_quanlycongviec

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class DailyJobService: JobService() {
    private val notificationId = 1

    override fun onStartJob(p0: JobParameters?): Boolean {
        val contentResolver: ContentResolver = applicationContext.contentResolver
        val uri: Uri = Uri.parse("content://com.datnt.app_quanlycongviec.TaskContentProvider/task")

        val cursor = contentResolver.query(uri, null, null, null, null)

        if(cursor != null && cursor.moveToFirst()){
            do {
                val taskId = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.ID))
                val taskName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_NAME))
                val taskDetails = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_DETAILS))
                val taskTime = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_TIME))

                if(isToday(taskTime)){
                    val notificationTitle = "Công việc hôm nay: $taskName"
                    val notificationContent = "Thời gian: $taskTime"

                    showNotification(applicationContext, notificationTitle, notificationContent)
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
        // Lập lịch lại công việc cho ngày mai
        scheduleNextJob()

        return false
    }

    private fun scheduleNextJob() {
        val componentName = ComponentName(this, DailyJobService::class.java)
        val builder = JobInfo.Builder(1, componentName)

        // Đặt thời gian thực hiện công việc hàng ngày vào 6h sáng
        builder.setPersisted(true)
        builder.setRequiresCharging(false)
        builder.setRequiresDeviceIdle(false)
        builder.setPeriodic(24 * 60 * 60 * 1000) // Mỗi ngày

        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        return false
    }

    private fun isToday(taskTime: String): Boolean {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val taskDate = sdf.parse(taskTime)
        val currentDate = Calendar.getInstance().time

        val calendar = Calendar.getInstance()
        calendar.time = taskDate

        return calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR) &&
                calendar.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH) &&
                calendar.get(Calendar.DAY_OF_MONTH) == Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    private fun showNotification(context: Context, title: String, content: String){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "default_channel_id"
            val channelName = "Default Channel"
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        // Tạo thông báo
        val notificationBuilder = NotificationCompat.Builder(context, "default_channel_id")
            .setSmallIcon(R.drawable.baseline_work_24)
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        val notification: Notification = notificationBuilder.build()

        // Hiển thị thông báo
        notificationManager.notify(notificationId, notification)
    }
}