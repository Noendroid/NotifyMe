package com.igal.notifyme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Settings extends AppCompatActivity {

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
}
