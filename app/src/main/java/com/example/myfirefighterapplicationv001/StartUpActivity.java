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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Set;

public class StartUpActivity extends AppCompatActivity {
    BluetoothAdapter adapter;
    BluetoothDevice mBluetoothDevice;
    BTconnection mBTconnection;
    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();
    public DeviceListAdapter mDeviceListAdapter;
    ListView lvNewDevices;
    private static final String TAG = "StartUpActivity";

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
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

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (action.equals(adapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, adapter.ERROR);

                switch(state){
                    case BluetoothAdapter.STATE_OFF:
                        Log.d(TAG, "onReceive: STATE OFF");
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");
                        break;
                }
            }
        }
    };

    private BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();


            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //3 cases:
                //case1: bonded already
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");
                    mBluetoothDevice = mDevice;
                }
                //case2: creating a bone
                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {
                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");
                }
                //case3: breaking a bond
                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");
                }
            }
            else
            {
                Log.d(TAG,"bond state not changed");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);


        adapter = BluetoothAdapter.getDefaultAdapter();
        Button conBtn = findViewById(R.id.conBtn);
        Button bltBtn = findViewById(R.id.bltBtn);
        Button testBtn = findViewById(R.id.testBtn);
        Button searchBtn = findViewById(R.id.search);
        lvNewDevices = findViewById(R.id.lvDevices);
        mBTDevices = new ArrayList<>();
        //test for viewing paired devices(works now!)
        testBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBTconnection(mBluetoothDevice);
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

        //IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        IntentFilter bondFilter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver2, bondFilter);


        //for turning on the bluetooth(works now!)
        bltBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                enableBT();
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                btnDiscover();
            }
        });
        lvNewDevices.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
                        adapter.cancelDiscovery();
                        Log.d(TAG, "onItemClick: You Clicked on a device.");
                        String deviceName = mBTDevices.get(i).getName();
                        String deviceAddress = mBTDevices.get(i).getAddress();

                        Log.d(TAG, "onItemClick: deviceName = " + deviceName);
                        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);

                        //create the bond.
                        //NOTE: Requires API 17+? I think this is JellyBean
                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){
                            Log.d(TAG, "Trying to pair with " + deviceName);
                            if(mBTDevices.get(i).createBond())
                            {
                                Log.d(TAG,"Bonding process successfully started");
                            }
                            else {
                                Log.d(TAG,"Bonding will not start");
                                if(mBTDevices.get(i).getBondState()== BluetoothDevice.BOND_BONDED)
                                {
                                    Log.d(TAG,"This devices is already paired");
                                    mBluetoothDevice = mBTDevices.get(i);
                                    mBTconnection = new BTconnection(StartUpActivity.this,adapter);
                                }
                            }

                        }
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver3);
        unregisterReceiver(mBroadcastReceiver2);
        unregisterReceiver(mBroadcastReceiver1);
    }

    public void startBTconnection(BluetoothDevice device){
        mBTconnection.startClient(device);
    }

    public void enableBT(){
        /*if(adapter == null){
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
        }*/
        if(adapter == null){
            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");
        }
        if(!adapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: enabling BT.");
            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
        }
        if(adapter.isEnabled()){
            Log.d(TAG, "enableDisableBT: disabling BT.");
            adapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            registerReceiver(mBroadcastReceiver1, BTIntent);
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

    public void btnDiscover() {
        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");
        lvNewDevices.setAdapter(null);
        mBTDevices.clear();
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
                Log.d(TAG, "checkBTPermissions: premissions checked. SDK version > LOLLIPOP.");
            }
        }else{
            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");
        }
    }
}

/*git enabled*/