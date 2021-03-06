package com.duhos.duhosii;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String naslov=intent.getStringExtra("naslov");
        String vrijeme=intent.getStringExtra("vrijeme");
        String datum=intent.getStringExtra("datum");
        String lokacija=intent.getStringExtra("lokacija");
        int randomID=intent.getIntExtra("randomID",200);


        Intent intent1=new Intent(context,MainActivity.class);
        intent1.putExtra("notification", "openCalendar");

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notify")
                .setSmallIcon(R.drawable.ic_duhos_logo)
                //.setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        //        R.drawable.davor))
                .setStyle(new NotificationCompat.BigTextStyle()
                       .bigText(naslov+"\n"+datum+ " u "+vrijeme+"h"+"\n"+"Lokacija: "+lokacija))
                //.setContentText(naslov+"\n"+datum+ " u "+vrijeme+"h"+"\n"+"Lokacija: "+lokacija)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent1, 0))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(randomID,builder.build());


        builder.build();


    }

}
