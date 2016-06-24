package com.example.topza.piggy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    SwitchCompat check1;
    SwitchCompat check5;
    SwitchCompat check10;
    SwitchCompat check20;
    SwitchCompat check100;

    Button save;

    SharedPreferences sp;

    Toolbar toolbar;

    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        iniInstances();

        check1.setChecked(sp.getBoolean("Check1",true));
        check5.setChecked(sp.getBoolean("Check5",true));
        check10.setChecked(sp.getBoolean("Check10",true));
        check20.setChecked(sp.getBoolean("Check20",true));
        check100.setChecked(sp.getBoolean("Check100",true));
    }

    private void iniInstances(){
        toolbar = (Toolbar) findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Setting");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        check1 = (SwitchCompat) findViewById(R.id.Check1);
        check5 = (SwitchCompat) findViewById(R.id.Check5);
        check10 = (SwitchCompat) findViewById(R.id.Check10);
        check20 = (SwitchCompat) findViewById(R.id.Check20);
        check100 = (SwitchCompat) findViewById(R.id.Check100);

        save = (Button) findViewById(R.id.SaveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSetting();
            }
        });

        sp = getSharedPreferences("SETTING",Context.MODE_PRIVATE);

    }

    private void saveSetting() {
        Boolean check = check1.isChecked()|| check5.isChecked()|| check10.isChecked()|| check20.isChecked()|| check100.isChecked();
        if(check) {

            if(check1.isChecked()) count++;
            if(check5.isChecked()) count++;
            if(check10.isChecked()) count++;
            if(check20.isChecked()) count++;
            if(check100.isChecked()) count++;

            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("Check1",check1.isChecked());
            editor.putBoolean("Check5",check5.isChecked());
            editor.putBoolean("Check10",check10.isChecked());
            editor.putBoolean("Check20",check20.isChecked());
            editor.putBoolean("Check100",check100.isChecked());
            editor.putInt("CheckCount",count);
            editor.commit();

            Bundle saveSetting = new Bundle();
            saveSetting.putBoolean("Check1",check1.isChecked());
            saveSetting.putBoolean("Check5",check5.isChecked());
            saveSetting.putBoolean("Check10",check10.isChecked());
            saveSetting.putBoolean("Check20",check20.isChecked());
            saveSetting.putBoolean("Check100",check100.isChecked());
            saveSetting.putInt("CheckCount",count);

            Intent returnIntent = new Intent();
            returnIntent.putExtra("SaveSetting",saveSetting);
            setResult(RESULT_OK,returnIntent);
            finish();

        } else Toast.makeText(SettingActivity.this, "Please set one or more Coin", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(android.R.id.home == item.getItemId()){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}