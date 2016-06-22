package com.example.topza.piggy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigation;
    BluetoothSPP bt;
    SharedPreferences preferences;
    SharedPreferences sharedPreferences;
    BalanceFragment balanceFragment;
    ImageView imageAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        sharedPreferences = getSharedPreferences("SETTING", Context.MODE_PRIVATE);
        Bundle sp = new Bundle();
        sp.putBoolean("Check1", sharedPreferences.getBoolean("Check1", true));
        sp.putBoolean("Check5", sharedPreferences.getBoolean("Check5", true));
        sp.putBoolean("Check10", sharedPreferences.getBoolean("Check10", true));
        sp.putBoolean("Check20", sharedPreferences.getBoolean("Check20", true));
        sp.putBoolean("Check100", sharedPreferences.getBoolean("Check100", true));

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new BalanceFragment().newInstance(sp), "BalanceFragment")
                    .commit();
        }

        initInstances();

        bt = new BluetoothSPP(this);
        setBluetooth();

    }

    private void setBluetooth() {
        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    bt.send(Double.toString(balanceFragment.getCountMoney()), true);
                    Toast.makeText(HomeActivity.this, "Connection Complete", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt.setAutoConnectionListener(new BluetoothSPP.AutoConnectionListener() {
            public void onNewConnection(String name, String address) {
                Log.i("Check", "New Connection - " + name + " - " + address);
            }

            public void onAutoConnectionStarted() {
                Log.i("Check", "Auto connection started");
            }
        });


    }

    private void initInstances() {
        toolbar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Balance");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerHomeLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                HomeActivity.this,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        //drawerLayout.setDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigation = (NavigationView) findViewById(R.id.navigation);
        setNavigationItem();

        View v = navigation.getHeaderView(0);
        imageAvatar = (ImageView) v.findViewById(R.id.profile_image);
        imageAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(HomeActivity.this, "This is your Avatar", Toast.LENGTH_SHORT).show();
            }
        });

        preferences = getPreferences(Context.MODE_PRIVATE);

    }

    private void setNavigationItem() {
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                Fragment fragment = getSupportFragmentManager()
                        .findFragmentById(R.id.contentContainer);

                switch (id) {
                    case R.id.navItem1:
                        if (fragment instanceof BalanceFragment == false) {
                            getSupportActionBar().setTitle("Balance");
                            getSupportFragmentManager().popBackStack();
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.navItem2:
                        if (fragment instanceof AccountFragment == false) {
                            getSupportActionBar().setTitle("Account");
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.contentContainer, AccountFragment.newInstance(), "AccountFragment")
                                    .addToBackStack(null)
                                    .commit();
                        }
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.navItem3:
                        Toast.makeText(HomeActivity.this, "Help", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled())
            bt.enable();
        else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
                bt.autoConnect("Piggy");
            }
        }

        balanceFragment = (BalanceFragment) getSupportFragmentManager().findFragmentByTag("BalanceFragment");
        balanceFragment.setCountMoney((double) preferences.getFloat("CountMoney", 0));
    }

    public void sendBluetoothText(String text) {
        if (bt.getServiceState() == BluetoothState.STATE_CONNECTED)
            bt.send(text, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        bt.disconnect();
        bt.stopAutoConnect();
        bt.stopService();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat("CountMoney", (float) balanceFragment.getCountMoney());
        editor.commit();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Toast.makeText(HomeActivity.this, "Setting Menu", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, SettingActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contentContainer, BalanceFragment.newInstance(data.getBundleExtra("SaveSetting")), "BalanceFragment")
                        .commit();
                Toast.makeText(HomeActivity.this, "Save Setting", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(HomeActivity.this, "CANCELED", Toast.LENGTH_SHORT).show();
            }

        }

//        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
//
//            if (data == null) {
//                //Display an error
//                Toast.makeText(HomeActivity.this, "Error Please Try Again", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            InputStream inputStream = new InputStream() {
//                @Override
//                public int read() throws IOException {
//                    return 0;
//                }
//            };
//
//            try {
//                inputStream = this.getContentResolver().openInputStream(data.getData());
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            //Now you can do whatever you want with your inputStream, save it as file, upload to a server, decode a bitmap...
//            Bitmap avatarBitmap = BitmapFactory.decodeStream(inputStream);
//            imageAvatar.setImageBitmap(avatarBitmap);
//        }
    }

//    public void pickImage() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
//    }

    void OpenFileDialog(String file) {

        //Read file in Internal Storage
        FileInputStream fis;
        String content = "";
        try {
            fis = openFileInput(file);
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {
            }
            content += new String(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
