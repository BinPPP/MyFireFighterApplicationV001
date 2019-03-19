package com.example.myfirefighterapplicationv001;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class StartUpActivity extends AppCompatActivity {
    BluetoothAdapter adapter;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    private static final String TAG = "StartUpActivity";

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "onReceive: ACTION FOUND.");

            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);
                mBTDevices.add(device);
                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());
                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);
                lvNewDevices.setAdapter(mDeviceListAdapter);
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = BluetoothAdapter.getDefaultAdapter();
        setContentView(R.layout.activity_start_up);
        Button conBtn = findViewById(R.id.conBtn);
        Button bltBtn = findViewById(R.id.bltBtn);
        Button testBtn = findViewById(R.id.testBtn);

        lvNewDevices = (ListView) findViewById(R.id.lvDevices);
        mBTDevices = new ArrayList<>();
        //test for viewing paired devices(works now!)
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPairedDevice();
            }
        });

        //go to the pairing activity so that we could pair(not done yet!)
        conBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent connectIntent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(connectIntent);
            }
        });

        //for turning on the bluetooth(works now!)
        bltBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                enableBT();
            }
        });
    }
    public void enableBT(){
        if(adapter == null){
            TextView errorMsg = findViewById(R.id.errorMsg);
            errorMsg.setText(getString(R.string.msg));
        }
        else if(adapter.isEnabled()){
            TextView errorMsg = findViewById(R.id.errorMsg);
            errorMsg.setText(getString(R.string.msg2));
        }
        else if(!adapter.isEnabled()){
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);
            TextView errorMsg = findViewById(R.id.errorMsg);
            errorMsg.setText(getString(R.string.msg3));
        }
    }

    public void getPairedDevice(){
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                TextView name = findViewById(R.id.name);
                name.setText(deviceName);
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    }

    public void btnDiscover(View view) {
        Log.d(TAG,"looking for devices");
        if(adapter.isDiscovering()){
            adapter.cancelDiscovery();
            Log.d(TAG, "btnDiscover: Canceling discovery.");

            //check BT permissions in manifest
            checkBTPermissions();

            adapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }
        if(!adapter.isDiscovering()){

            //check BT permissions in manifest
            checkBTPermissions();

            adapter.startDiscovery();
            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);
        }

    }

    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            if (permissionCheck != 0) {

                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
    }

/*git enabled*/