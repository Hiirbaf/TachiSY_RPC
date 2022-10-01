package com.jery.tachisyrpc;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.my.kizzyrpc.KizzyRPCservice;

public class MyService extends Service {
    public static KizzyRPCservice rpc = new KizzyRPCservice(MainActivity.Token.getText().toString());

    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, MainActivity.clipText, Toast.LENGTH_SHORT).show();

        rpc.setName(MainActivity.Name.getText().toString())
            .setState(MainActivity.State.getText().toString())
            .setDetails(MainActivity.Details.getText().toString())
            .setApplicationId("962990036020756480")
            .setLargeImage("attachments/961577469427736636/971135180322529310/unknown.png")
            .setSmallImage("attachments/949382602073210921/1001372717783711814/reading-icon.png")
            .setType(0)
            .setStartTimestamps(System.currentTimeMillis())
//            .setButton1("Button1", "https://youtu.be/1yVm_M1sKBE")
//            .setButton2("Button2", "https://youtu.be/1yVm_M1sKBE")
            .setStatus("online")
            .build();
        notification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Stop RPC", Toast.LENGTH_SHORT).show();
        rpc.closeRPC();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private void notification() {
        Context context = this;
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(new NotificationChannel("Discord RPC", "Discord RPC", NotificationManager.IMPORTANCE_MIN));
        Notification.Builder builder = new Notification.Builder(context, "Discord RPC");
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(MainActivity.Name.getText().toString())
                .setContentText(MainActivity.Details.getText().toString())
                .setSubText(MainActivity.State.getText().toString())
                .setUsesChronometer(true);
        startForeground(11234, builder.build());
    }
}