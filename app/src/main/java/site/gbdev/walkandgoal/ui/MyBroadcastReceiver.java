package site.gbdev.walkandgoal.ui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

import site.gbdev.walkandgoal.R;
import site.gbdev.walkandgoal.db.FitnessDbWrapper;
import site.gbdev.walkandgoal.models.Goal;
import site.gbdev.walkandgoal.models.HistoricGoal;
import site.gbdev.walkandgoal.models.Units;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by gavin on 20/03/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();

        Goal goal = FitnessDbWrapper.getActiveGoal(context);

        if (goal != null) {
            double progress = FitnessDbWrapper.getTotalActivityForDate(Units.getUNITS()[goal.getUnit()], yesterday, context);
            HistoricGoal historicGoal = new HistoricGoal(goal, progress);
            FitnessDbWrapper.setFinished(goal, yesterday, context);

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            boolean notifications = sharedPreferences.getBoolean("pref_notifications", true);

            if (historicGoal.getPercentageCompleted() >= 100 && notifications) {

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

                Intent newIntent = new Intent(context, MainActivity.class);
                PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), newIntent, 0);

                Notification n = new Notification.Builder(context)
                        .setContentTitle("Goal Completed!")
                        .setContentText(goal.getName() + " - " + goal.getDisplayDistance())
                        .setSmallIcon(R.drawable.ic_menu_add_goal)
                        .setContentIntent(pIntent)
                        .setAutoCancel(true).build();

                notificationManager.notify(0, n);
            }
        }
    }
}
