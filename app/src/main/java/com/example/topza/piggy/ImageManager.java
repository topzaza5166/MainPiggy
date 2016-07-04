package com.example.topza.piggy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageManager extends AppCompatActivity {

    private static final int PICK_PHOTO = 0011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_manager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(ImageManager.this, "Error Please Try Again", Toast.LENGTH_SHORT).show();
                return;
            }

            InputStream inputStream = new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };

            try {
                inputStream = this.getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inputStream, save it as file, upload to a server, decode a bitmap...
            Bitmap b = BitmapFactory.decodeStream(inputStream);
            Bitmap avatarBitmap = Bitmap.createScaledBitmap(b, 120, 120, false);

            //Convert to byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            avatarBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Intent returnIntent = new Intent();
            returnIntent.putExtra("image",byteArray);
            setResult(RESULT_OK,returnIntent);
            finish();
        }
    }
}
