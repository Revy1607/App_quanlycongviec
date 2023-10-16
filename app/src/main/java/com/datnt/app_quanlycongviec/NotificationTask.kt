package com.datnt.app_quanlycongviec

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.Worker
import androidx.work.WorkerParameters


class NotificationTask(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    companion object {
        private const val channelId = "work_channel"
        private const val NOTIFICATION_ID = 1
    }
    override fun doWork(): Result {
        val task = getTask()

        if(task != null){
            showNotification(task)
        }else{
            showNoTaskNotification()
        }
        return Result.success()
    }

    private fun getTask(): String? {
        val databaseHelper = DatabaseHelper(applicationContext)
        val db = databaseHelper.readableDatabase

        val projection = arrayOf(DatabaseHelper.TASK_NAME)
        val cursor = db.query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null)
        var task: String? = null
        if(cursor != null){
            if(cursor.moveToFirst()){
                task = cursor.getString(cursor.getColumnIndex(DatabaseHelper.TASK_NAME))
            }
            cursor.close()
        }
        db.close()

        return task
    }

    private fun createNotificationChannel(){
        // Kiểm tra và tạo kênh thông báo (channel) trước khi hiển thị thông báo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "WorkChannel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            // Tạo đối tượng NotificationManager để quản lý thông báo
            val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }
    private fun showNotification(task: String) {
        val notificationIntent = Intent(applicationContext, ListTaskFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, 0)

        // Tạo đối tượng NotificationCompat.Builder để xây dựng thông báo
        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Nhắc nhở công việc hàng ngày")
            .setContentText("Công việc: $task")
            .setSmallIcon(R.drawable.baseline_work_24)
            .setContentIntent(pendingIntent)

        createNotificationChannel()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun showNoTaskNotification() {

        val notificationIntent = Intent(applicationContext, ListTaskFragment::class.java)
        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Nhắc nhở công việc hàng ngày")
            .setContentText("Không có công việc")
            .setSmallIcon(R.drawable.baseline_work_24)
            .setContentIntent(pendingIntent)

        createNotificationChannel()

        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

}