package com.example.topza.piggy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    Switch check1;
    Switch check5;
    Switch check10;
    Switch check20;
    Switch check100;

    Button cancel;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    private void iniInstances(){

    }
}
