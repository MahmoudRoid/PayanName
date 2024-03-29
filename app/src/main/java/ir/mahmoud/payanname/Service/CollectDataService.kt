package ir.mahmoud.payanname.Service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.IBinder
import android.widget.Toast
import androidx.core.app.NotificationCompat
import ir.mahmoud.payanname.Classes.Constants
import ir.mahmoud.payanname.Classes.GetData
import ir.mahmoud.payanname.R

class CollectDataService : Service() {

    var notification: Notification? = null
    var isStopped = true

    companion object {
        val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null){
            val action = intent.action
            when(action){
                ACTION_START_FOREGROUND_SERVICE->{
                    startForegroundService()
                    Toast.makeText(applicationContext, "collect data service is started.", Toast.LENGTH_LONG).show()
                }
                ACTION_STOP_FOREGROUND_SERVICE ->{
                    stopForegroundService()
                    Toast.makeText(applicationContext, "collect data service is stopped.", Toast.LENGTH_LONG).show()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startForegroundService() {
        isStopped = false

        showNotification()
        // Start foreground service.
        startForeground(1, notification)
        start()
    }

    private fun start() {

        Thread(Runnable {
            while ( !isStopped ){
                GetData.getInstance().saveData()
                Thread.sleep(Constants.collectDataServiceIntervalTime)
            }
        }).start()
    }

    private fun showNotification() {
        // Create notification default intent.
        val intent = Intent()
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        // Create notification builder.
        val builder = NotificationCompat.Builder(this)
        builder.setContentTitle("Collect Data Service")
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher)
        val largeIconBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_round)
        builder.setLargeIcon(largeIconBitmap)
        // Make the notification max priority.
        builder.priority = Notification.PRIORITY_MAX
        // Make head-up notification.
        builder.setFullScreenIntent(pendingIntent, true)
        // Build the notification.
        notification = builder.build()
    }

    private fun stopForegroundService() {
        isStopped = true
        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
    }

}
