package com.igal.notifyme;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.Manifest;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by igalh on 01-Aug-17.
 */

public class Device {
    public static String[] MY_SERVICES = {"Battery level", "My Location", "Ram usage", "Free Storage", "Network name", "Next alarm"};

    public Device() {

    }

    public static int getNumOfIndividuals() {
        return MY_SERVICES.length;// IN CASE OFF ADDING OPTIONS CHANGE THIS VARIABLE
    }

    public static ArrayList<View> getIndividualsView(Context context) {
        ArrayList<View> list = new ArrayList<View>();
        LayoutInflater infltr;
        infltr = LayoutInflater.from(context);
        for (int i = 0; i < getNumOfIndividuals(); i++) {
            View convertView = infltr.inflate(R.layout.item_on_off, null);
            TextView name = (TextView) convertView.findViewById(R.id.item_name);
            name.setText(MY_SERVICES[i]);
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

    public static String getNetworkName(Context context) {
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

    public static String getNextAlarm(Context context) {
        String nextAlarm = Settings.System.getString(context.getContentResolver(),
                Settings.System.NEXT_ALARM_FORMATTED);
        return nextAlarm;
    }

    public String getUserLocation(Context context) {
        String curCity = "Searching...";
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Home.MY_PERMISSION_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, Home.MY_PERMISSION_REQUEST_LOCATION);
            }
        } else {
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                curCity = getLocation(context, location.getLatitude(), location.getLongitude());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return curCity;
    }

    private String getLocation(Context context, double lat, double lon) {
        String curCity = "Searching...";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocation(lat, lon, 1);
            if (addressList.size() > 0) {
                curCity = addressList.get(0).getLocality();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return curCity;
    }
}
