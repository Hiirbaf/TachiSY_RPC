package com.jery.tachisyrpc;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

@SuppressWarnings("RedundantCast")
@SuppressLint( "StaticFieldLeak, UseSwitchCompatOrMaterialCode, BatteryLife" )
public class MainActivity extends AppCompatActivity {

    public static Chip Token, Footer;
    public static EditText Name, State, Details;
    public static Switch Switch;
    public static Activity activity;
    public static ClipboardManager clipboard;
    public static ClipData.Item clipItem;
    public static String clipText = "Testing";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;

        Token = (Chip) findViewById(R.id.chipToken);
        Name = (EditText) findViewById(R.id.edittextName);
        State = (EditText) findViewById(R.id.edittextState);
        Details = (EditText) findViewById(R.id.edittextDetails);  // Reference Text : Akatsuki no Yona
        Switch = (Switch) findViewById(R.id.switchRPC);
        Footer = (Chip) findViewById(R.id.chipFooter);

        if (android.os.Build.VERSION.SDK_INT <= 33) {
            clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard.hasPrimaryClip())
                clipItem = clipboard.getPrimaryClip().getItemAt(0);
            if (clipItem != null) {
                clipText = clipItem.getText().toString();
                Details.setText(clipText);
            }
        }

        Switch.setChecked(isServiceRunning(MyService.class));
        Switch.setOnClickListener(v -> {
            if (Switch.isChecked()) {
                Toast.makeText(MainActivity.this, clipText, Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, MyService.class));
            } else {
                Toast.makeText(MainActivity.this, "Turning off RPC", Toast.LENGTH_SHORT).show();
                stopService(new Intent(MainActivity.this, MyService.class));
            }
        });

        Footer.setOnLongClickListener(v -> {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            if (pm != null && !pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Battery optimization already disabled", Toast.LENGTH_SHORT).show();
            }
            return true;
        });
    }

    public boolean isNotificationPresent() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        StatusBarNotification[] notificationArray = notificationManager.getActiveNotifications();
        return notificationArray != null && notificationArray.length > 0;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}