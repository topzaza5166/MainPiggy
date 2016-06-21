package com.example.topza.piggy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {
    Switch check1;
    Switch check5;
    Switch check10;
    Switch check20;
    Switch check100;

    Button cancel;
    Button save;

    SharedPreferences sp;

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
        check1 = (Switch) findViewById(R.id.Check1);
        check5 = (Switch) findViewById(R.id.Check5);
        check10 = (Switch) findViewById(R.id.Check10);
        check20 = (Switch) findViewById(R.id.Check20);
        check100 = (Switch) findViewById(R.id.Check100);

        cancel = (Button) findViewById(R.id.CancelButton);
        save = (Button) findViewById(R.id.SaveButton);

        sp = getSharedPreferences("SETTING",Context.MODE_PRIVATE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean check = check1.isChecked()|| check5.isChecked()|| check10.isChecked()|| check20.isChecked()|| check100.isChecked();
                if(check) {
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putBoolean("Check1",check1.isChecked());
                    editor.putBoolean("Check5",check5.isChecked());
                    editor.putBoolean("Check10",check10.isChecked());
                    editor.putBoolean("Check20",check20.isChecked());
                    editor.putBoolean("Check100",check100.isChecked());
                    editor.commit();

                    Bundle saveSetting = new Bundle();
                    saveSetting.putBoolean("Check1",check1.isChecked());
                    saveSetting.putBoolean("Check5",check5.isChecked());
                    saveSetting.putBoolean("Check10",check10.isChecked());
                    saveSetting.putBoolean("Check20",check20.isChecked());
                    saveSetting.putBoolean("Check100",check100.isChecked());

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("SaveSetting",saveSetting);
                    setResult(RESULT_OK,returnIntent);
                    finish();
                } else Toast.makeText(SettingActivity.this, "Please set one or more Coin", Toast.LENGTH_SHORT).show();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent();
                setResult(RESULT_CANCELED,returnIntent);
                finish();
            }
        });

    }
}
