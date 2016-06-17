package com.example.topza.piggy;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
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
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigation;
    BluetoothSPP bt;
    SharedPreferences sp;
    BalanceFragment balanceFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contentContainer, new BalanceFragment().newInstance(5.0), "BalanceFragment")
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
                if(state == BluetoothState.STATE_CONNECTED)
                    bt.send(Double.toString(balanceFragment.getCountMoney()),true);
                Toast.makeText(HomeActivity.this, "Connection Complete", Toast.LENGTH_SHORT).show();
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

        sp = getPreferences(Context.MODE_PRIVATE);

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
        bt.stopService();
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
        balanceFragment.setBluetooth(bt);
        balanceFragment.setCountMoney((double) sp.getFloat("CountMoney",0));
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sp.edit();
        editor.putFloat("CountMoney",(float) balanceFragment.getCountMoney());
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
            Toast.makeText(HomeActivity.this, "Setting Mode", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}
