package com.igal.notifyme;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by igalh on 01-Aug-17.
 */

public class Device {
    public Device(){

    }

    public static int getNumOfIndividuals(){
        return 5;// IN CASE OFF ADDING OPTIONS CHANGE THIS VARIABLE
    }

    public static ArrayList<View> getIndividualsView(Context context){
        ArrayList<View> list = new ArrayList<View>();
        LayoutInflater infltr;
        infltr = LayoutInflater.from(context);
        String[] names = {"Battery level", "Ram usage", "Free Storage", "Network name", "Next alarm"};
        for (int i = 0; i < getNumOfIndividuals(); i++){
            View convertView = infltr.inflate(R.layout.item_on_off, null);
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(names[i]);
            list.add(convertView);
        }
        return list;
    }

    public static int getButteryLevel(Context context) {
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        float batteryPct = level / (float) scale * 100;

        return (int) batteryPct;
    }

    public static int getRamUsage(Context context) {
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(mi);
        double availableMegs = mi.availMem / 0x100000L;

        //Percentage can be calculated for API 16+
        double percentAvail = mi.availMem / (double) mi.totalMem * 100;
        return (int) percentAvail;
    }

    public static int getFreeStorageMB(Context context) {
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        long megAvailable = bytesAvailable / (1024 * 1024);
        return (int) megAvailable;
    }

    public static String getNetworkName(Context context){
        String ssid = null;
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (networkInfo.isConnected()) {
            final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            if (connectionInfo != null && !connectionInfo.getSSID().isEmpty()) {
                ssid = connectionInfo.getSSID();
            }
            return ssid;
        } else {
            return "No connection";
        }
    }

    public static String getNextAlarm(Context context){
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        return nextAlarm;
    }


}
