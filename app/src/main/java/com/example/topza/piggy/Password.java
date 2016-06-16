package com.example.topza.piggy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mhk.android.passcodeview.PasscodeView;

public class Password extends AppCompatActivity {
    PasscodeView passcodeview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        passcodeview = (PasscodeView) findViewById(R.id.passcode_view);
        passcodeview.setPasscodeEntryListener(new PasscodeView.PasscodeEntryListener() {
            @Override
            public void onPasscodeEntered(String passcode) {
                Intent intent = new Intent(Password.this,HomeActivity.class);
                startActivity(intent);
                Toast.makeText(Password.this, "Your PassCode is " + passcodeview.getText().toString() , Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
