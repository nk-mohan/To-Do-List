package com.makeover.todolist.scheduler

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.makeover.todolist.room.TaskRepository
import com.makeover.todolist.utils.AppConstants
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ScheduleTask @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun scheduleTask(taskId: Long, context: Context?) {
        context?.let {
            val calendar = Calendar.getInstance()
            val customCalendar = Calendar.getInstance()
            val task = taskRepository.getTask(taskId.toInt())
            if (task.date != null) {
                calendar.timeInMillis = task.date
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                customCalendar.set(year, month, day, task.hour!!, task.minute!!, 0)
                val customTime = customCalendar.timeInMillis
                val currentTime = System.currentTimeMillis()

                if (customTime > currentTime) {
                    val data = Data.Builder()
                        .putInt(AppConstants.NOTIFICATION_ID, task.id ?: 0)
                        .putString(AppConstants.NOTIFICATION_TITLE, task.title)
                        .putString(AppConstants.NOTIFICATION_SUB_TITLE, task.description)
                        .build()
                    val delay = customTime - currentTime
                    scheduleNotification(delay, data, it, task.id ?: 0)
                }
            }
        }
    }

    private fun scheduleNotification(delay: Long, data: Data, context: Context, workName: Int) {
        val notificationWork = OneTimeWorkRequest.Builder(ScheduleTaskWorker::class.java)
            .setInitialDelay(delay, TimeUnit.MILLISECONDS).setInputData(data).build()

        val workManager = WorkManager.getInstance(context)
        workManager.beginUniqueWork(
            workName.toString(),
            ExistingWorkPolicy.REPLACE,
            notificationWork
        ).enqueue()
    }

    fun cancelScheduleTask(context: Context?, workName: Int) {
        context?.let {
            val workManager = WorkManager.getInstance(context)
            workManager.cancelUniqueWork(workName.toString())
        }
    }

}