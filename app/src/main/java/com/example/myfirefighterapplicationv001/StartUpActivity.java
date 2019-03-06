package com.example.myfirefighterapplicationv001;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Set;

public class StartUpActivity extends AppCompatActivity {
    BluetoothAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = BluetoothAdapter.getDefaultAdapter();

        setContentView(R.layout.activity_start_up);
        Button conBtn = findViewById(R.id.conBtn);
        Button bltBtn = findViewById(R.id.bltBtn);
        Button testBtn = findViewById(R.id.testBtn);

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
}
