package com.igal.notifyme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.io.Console;

public class Home extends AppCompatActivity {
    FloatingActionButton home_settings;
    RelativeLayout home_body;
    Switch home_switch;
    Handler handler = new Handler();
    Thread background_thread;

    // GREEN -  #66BB6A
    private int R_ON = 102;
    private int G_ON = 187;
    private int B_ON = 106;
    // RED -    #FF4136
    private int R_OFF = 255;
    private int G_OFF = 65;
    private int B_OFF = 54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        background_thread = new Thread();

        home_settings = (FloatingActionButton) findViewById(R.id.fab);
        home_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Settings.class);
                startActivity(intent);
            }
        });

        home_body = (RelativeLayout) findViewById(R.id.home_body);
        home_switch = (Switch) findViewById(R.id.home_toggle);
        if(home_switch.isChecked()){
            home_body.setBackgroundColor(Color.argb(220, R_ON, G_ON, B_ON));
        } else {
            home_body.setBackgroundColor(Color.argb(220, R_OFF, G_OFF, B_OFF));
        }
        home_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_switch.toggle();
                TextView title = (TextView) findViewById(R.id.home_title);
                change_background(home_switch.isChecked());
                if (home_switch.isChecked()) {
                    title.setText("ON");
                } else {
                    title.setText("OFF");
                }
            }
        });
    }

    public void change_background(final boolean toggle_mode) {

        background_thread = new Thread() {
            @Override
            public void run() {
                boolean completed = false;
                int r, g, b;
                int r2, g2, b2;
                int rd, gd, bd;
                final int alpha = 220;
                if (toggle_mode) {
                    r = R_OFF;
                    g = G_OFF;
                    b = B_OFF;
                    r2 = R_ON;
                    g2 = G_ON;
                    b2 = B_ON;
                    rd = (r > r2 ? -1 : 1);
                    gd = (g > g2 ? -1 : 1);
                    bd = (b > b2 ? -1 : 1);
                } else {
                    r = R_ON;
                    g = G_ON;
                    b = B_ON;
                    r2 = R_OFF;
                    g2 = G_OFF;
                    b2 = B_OFF;
                    rd = (r > r2 ? -1 : 1);
                    gd = (g > g2 ? -1 : 1);
                    bd = (b > b2 ? -1 : 1);
                }
                while (!completed) {
                    r += rd;
                    g += gd;
                    b += bd;

                    if (r == r2 && g == g2 && b == b2) {
                        completed = true;
                    }

                    //  check if the color got to his end value
                    if (rd > 0) {
                        if (r > r2) {
                            r -= rd;
                            rd = 0;
                        }
                    } else {
                        if (r < r2) {
                            r -= rd;
                            rd = 0;
                        }
                    }
                    if (gd > 0) {
                        if (g > g2) {
                            g -= gd;
                            gd = 0;
                        }
                    } else {
                        if (g < g2) {
                            g -= gd;
                            gd = 0;
                        }
                    }
                    if (bd > 0) {
                        if (b > b2) {
                            b -= bd;
                            bd = 0;
                        }
                    } else {
                        if (b < b2) {
                            b -= bd;
                            bd = 0;
                        }
                    }
                    /*
                    if (r + rd != r2) {
                    }
                    if (g + gd != g2) {
                    }
                    if (b + bd != b2) {
                    }
                    */

                    final int finalR = r;
                    final int finalG = g;
                    final int finalB = b;
                    handler.post(new Runnable() {
                        public void run() {
                            home_body.setBackgroundColor(Color.argb(alpha, finalR, finalG, finalB));
                        }
                    });
                    try {
                        sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

                /*
                for (int g = 210; g > 0; g--) {
                    final int G = g;
                    handler.post(new Runnable() {
                        public void run() {
                            home_body.setBackgroundColor(Color.argb(255, 150, G, 50));
                        }
                    });
                    // next will pause the thread for some time
                    try {
                        sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }*/
            }
        };
        background_thread.start();
    }
}
