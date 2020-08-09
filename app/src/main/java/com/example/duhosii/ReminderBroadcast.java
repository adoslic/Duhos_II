package com.example.duhosii;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import io.realm.Realm;
import io.realm.RealmResults;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String naslov=intent.getStringExtra("naslov");
        String vrijeme=intent.getStringExtra("vrijeme");
        String lokacija=intent.getStringExtra("lokacija");
        int randomID=intent.getIntExtra("randomID",200);

        Intent intent1=new Intent(context,MainActivity.class);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context,"notify")
                .setSmallIcon(R.drawable.ic_duhos_logo)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(naslov+" u "+vrijeme+"h"+"\n"+"Lokacija: "+lokacija))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(PendingIntent.getActivity(context, 0, intent1, 0))
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(randomID,builder.build());


        builder.build();


    }

}
