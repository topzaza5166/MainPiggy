package com.example.topza.piggy;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import app.akexorcist.bluetoothspp.BluetoothSPP;
import app.akexorcist.bluetoothspp.BluetoothState;
import app.akexorcist.bluetoothspp.DeviceList;


public class SelectDeviceFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    ListView listView;
    HistoryListAdapter historyListAdapter;
    BluetoothAdapter mBtAdapter;

    public static SelectDeviceFragment newInstance() {
        SelectDeviceFragment fragment = new SelectDeviceFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_device_fragment, container, false);
        initInstances(rootView);

//        HomeActivity.bt = new BluetoothSPP(getActivity().getApplicationContext());
//        setBluetooth();
        return rootView;
    }

    private void setBluetooth() {
        if (!HomeActivity.bt.isBluetoothAvailable()) {
            Toast.makeText(getActivity().getApplicationContext(), "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        HomeActivity.bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    Toast.makeText(getActivity().getApplicationContext(), "Connection Complete", Toast.LENGTH_SHORT).show();
                }
//                if (state == BluetoothState.STATE_NONE) {
//                    Toast.makeText(HomeActivity.this, "Service is Null", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        HomeActivity.bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getActivity().getApplicationContext(), "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() {
                Toast.makeText(getActivity().getApplicationContext(), "Connection lost"
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getActivity().getApplicationContext(), "Unable to connect"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initInstances(View rootView) {
        String[] save_list = new String[10];

        String[] device_list = { "device 1", "device 2", "device 3"
                , "device 4", "device 5", "device 6", "device 7"
                , "device 8", "device 9", "device 10"
                , "device 11" };

        DeviceAdapter adapter = new DeviceAdapter(getActivity().getApplicationContext(), save_list, device_list);

        ListView listView1 = (ListView)rootView.findViewById(R.id.fragmentDeviceListView);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                Toast.makeText(getActivity().getApplicationContext(), "1234"
                        , Toast.LENGTH_SHORT).show();
            }
        });

        Button add_new_device = (Button)rootView.findViewById(R.id.btnAddConnect);
        add_new_device.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(HomeActivity.bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    HomeActivity.bt.disconnect();
                } else {
                    Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
                Toast.makeText(getActivity().getApplicationContext(), "5678"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK){
                HomeActivity.bt.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                HomeActivity.bt.setupService();
                HomeActivity.bt.startService(BluetoothState.DEVICE_ANDROID);
                Intent intent = new Intent(getActivity().getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }

    }
}
