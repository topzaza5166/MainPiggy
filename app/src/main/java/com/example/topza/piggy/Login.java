package com.example.topza.piggy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

public class Login extends AppCompatActivity {

    Button submitButton;
    MaterialEditText editUsername;
    MaterialEditText editPin;
    MaterialEditText editActiveCode;
    String username;
    String pin;
    String activecode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initInstances();
    }

    private void initInstances() {
        editUsername = (MaterialEditText) findViewById(R.id.EditUsername);
        editPin = (MaterialEditText) findViewById(R.id.EditPin);
        editActiveCode = (MaterialEditText) findViewById(R.id.EditActivateCode);

        submitButton = (Button) findViewById(R.id.submitbutton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = editUsername.getText().toString();
                pin = editPin.getText().toString();
                activecode = editActiveCode.getText().toString();

                if (!username.equals("topza5166") || !pin.equals("1234"))
                    Toast.makeText(Login.this, "Error Username or Pin is Incorrect", Toast.LENGTH_SHORT).show();
                else if (!activecode.equals("") && !activecode.equals("top"))
                    Toast.makeText(Login.this, "Error This Code is Invariable", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(Login.this, Password.class);
                    startActivity(intent);
                    finish();
                }

            }
        });
    }
}
