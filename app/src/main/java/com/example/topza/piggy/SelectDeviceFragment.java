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
import android.widget.ListView;
import android.widget.TextView;

import app.akexorcist.bluetoothspp.BluetoothState;


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
        return rootView;
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
//        listView.setOnItemClickListener(mDeviceClickListener);
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            // Cancel discovery because it's costly and we're about to connect
            if(mBtAdapter.isDiscovering())
                mBtAdapter.cancelDiscovery();

            String strNoFound = getActivity().getIntent().getStringExtra("no_devices_found");
            if(strNoFound == null)
                strNoFound = "No devices found";
            if(!((TextView) v).getText().toString().equals(strNoFound)) {
                // Get the device MAC address, which is the last 17 chars in the View
                String info = ((TextView) v).getText().toString();
                String address = info.substring(info.length() - 17);

                // Create the result Intent and include the MAC address
                Intent intent = new Intent();
                intent.putExtra(BluetoothState.EXTRA_DEVICE_ADDRESS, address);

                // Set result and finish this Activity
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        }
    };
}
