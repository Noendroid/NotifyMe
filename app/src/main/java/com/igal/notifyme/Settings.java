package com.igal.notifyme;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

    public static final String BATTERY_LEVEL = "battery_level";
    public static final String MY_LOCATION = "my_location";
    public static final String RAM_USAGE = "ram_usage";
    public static final String FREE_STORAGE = "free_storage";
    public static final String NETWORK_NAME = "network_name";
    public static final String NEXT_ALARM = "next_alarm";

    private SettingsAdapter listAdapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        listAdapter = new SettingsAdapter(this);//                      create the adapter
        listView = (ListView) findViewById(R.id.settings_list);//   find the list view
        listView.setAdapter(listAdapter);//                         add the list adapter to the listView

    }

    public static void initiate_settings_preferences(SharedPreferences.Editor editor){
        editor.putBoolean(BATTERY_LEVEL, false);
        editor.putBoolean(FREE_STORAGE, false);
        editor.putBoolean(MY_LOCATION, false);
        editor.putBoolean(NETWORK_NAME, false);
        editor.putBoolean(RAM_USAGE, false);
        editor.putBoolean(NEXT_ALARM, false);
    }
}
