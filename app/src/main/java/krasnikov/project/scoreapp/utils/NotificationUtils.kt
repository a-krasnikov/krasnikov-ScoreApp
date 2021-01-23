package krasnikov.project.scoreapp.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import krasnikov.project.scoreapp.R
import krasnikov.project.scoreapp.model.GameViewModel
import krasnikov.project.scoreapp.ui.MainActivity

object NotificationUtils {
    private const val TIMER_NOTIFICATION_CHANNEL_ID = "timerNotification"
    private const val TIMER_NOTIFICATION_ID = 0

    fun showNotification(context: Context, game: GameViewModel) {
        game.timer.timeUpdateCallback = {
            // Update notification while timer is active
            updateNotification(context, game)
        }

        game.onGameFinishCallback = {
            //TODO go to winner screen
            // Intent to load the app
            val showApp = Intent(context, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                action = Intent.ACTION_MAIN
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val pendingShowApp: PendingIntent =
                PendingIntent.getActivity(context, 0, showApp, PendingIntent.FLAG_UPDATE_CURRENT)
            pendingShowApp.send()
        }
    }

    fun hideNotification(context: Context, game: GameViewModel) {
        game.timer.timeUpdateCallback = null
        game.onGameFinishCallback = null
        NotificationManagerCompat.from(context)
            .cancel(TIMER_NOTIFICATION_ID)
    }

    private fun updateNotification(context: Context, game: GameViewModel) {
        val resources = context.resources

        // Intent to load the app
        val showApp = Intent(context, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            action = Intent.ACTION_MAIN
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val pendingShowApp: PendingIntent =
            PendingIntent.getActivity(context, 0, showApp, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification: NotificationCompat.Builder =
            NotificationCompat.Builder(context, TIMER_NOTIFICATION_CHANNEL_ID)
                .setOngoing(true)
                .setLocalOnly(true)
                .setShowWhen(false)
                .setAutoCancel(false)
                .setContentIntent(pendingShowApp)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setStyle(NotificationCompat.DecoratedCustomViewStyle())
                //.setColor(ContextCompat.getColor(context, R.color.default_background))
                .setSmallIcon(R.drawable.ic_play)
                .setContentTitle(resources.getString(R.string.title_notification_game))
                .setContentText(TimerStringFormatter.formatTimeRemaining(game.timer.secondsRemaining))
                .addAction(
                    R.drawable.ic_play,
                    resources.getString(R.string.btn_notification_play),
                    pendingShowApp
                )

        createChannel(context)
        val notificationManager = NotificationManagerCompat.from(context)
        notificationManager.notify(TIMER_NOTIFICATION_ID, notification.build())
    }

    private fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(
                TIMER_NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.timer_channel),
                importance
            )

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.createNotificationChannel(channel)
        }
    }
}