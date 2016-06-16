package com.example.topza.piggy;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.ndk.CrashlyticsNdk;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    ImageButton imagebutton;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics(), new CrashlyticsNdk());
        setContentView(R.layout.activity_main);
        initInstances();
    }

    private void initInstances(){
        intent = new Intent(MainActivity.this,Login.class);
        imagebutton = (ImageButton) findViewById(R.id.BackgroundImage);
        imagebutton.setOnClickListener(next);
    }

    View.OnClickListener next = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        new CountDownTimer(1000, 100) {

            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                startActivity(intent);
                finish();
            }
        }.start();

    }
}
