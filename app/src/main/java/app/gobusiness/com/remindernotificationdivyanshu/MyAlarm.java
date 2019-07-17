package app.gobusiness.com.remindernotificationdivyanshu;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MyAlarm extends BroadcastReceiver {

    String  title,date,desc,time;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "alarm");

        Toast.makeText(context,"Alerm fired!!!",Toast.LENGTH_SHORT).show();

        title = intent.getStringExtra("title");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        desc = intent.getStringExtra("desc");

        Log.d("MyAlarm","title: "+title);
        Log.d("MyAlarm","date: "+date);
        Log.d("MyAlarm","time: "+time);
        Log.d("MyAlarm","desc: "+desc);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        //PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        String channelId = "123456";

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.stalwart)
                .setContentTitle(title)
                .setContentText(desc)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel =new NotificationChannel(channelId,"channel human readable title",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0,notificationBuilder.build());
    }
}
