package com.example.myfirefighterapplicationv001;

import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private BluetoothSocket mBluetoothSocket = SocketHandler.getSocket();
    private SeekBar barCH4;
    private SeekBar barIBUT;
    private SeekBar barO2;
    private SeekBar barCO;
    private ProgressBar ringCH4;
    private ProgressBar ringIBUT;
    private ProgressBar ringO2;
    private ProgressBar ringCO;
    private TextView textCH4;
    private TextView textIBUT;
    private TextView textO2;
    private TextView textCO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set up the progress rings
        ringCH4 = findViewById(R.id.ringCH4);
        ringCH4.setSecondaryProgress(100);
        ringIBUT = findViewById(R.id.ringIBUT);
        ringIBUT.setSecondaryProgress(100);
        ringO2 = findViewById(R.id.ringO2);
        ringO2.setSecondaryProgress(100);
        ringCO = findViewById(R.id.ringCO);
        ringCO.setSecondaryProgress(100);
        //set up textViews for displaying the values
        textCH4 = findViewById(R.id.textCH4);
        textIBUT = findViewById(R.id.textIBUT);
        textO2 = findViewById(R.id.textO2);
        textCO = findViewById(R.id.textCO);
        //set up the seek bars
        barCH4 = findViewById(R.id.seekBarCH4);
        barIBUT = findViewById(R.id.seekBarIBUT);
        barO2 = findViewById(R.id.seekBarO2);
        barCO = findViewById(R.id.seekBarCO);

        //drag listener for CH4
        barCH4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringCH4.setProgress(progress/10);
                float dummy;
                dummy = (float)progress;
                dummy = dummy/10;
                textCH4.setText("CH4: "+dummy+"%");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //drag listener for IBUT
        barIBUT.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringIBUT.setProgress(progress/20);
                textIBUT.setText("IBUT: "+progress+" ppm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //drag listener for O2
        barO2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringO2.setProgress(progress/25);
                float dummy = (float)progress/100;
                textO2.setText("O2: "+ dummy + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //drag listener for CO
        barCO.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringCO.setProgress(progress/5);
                textCO.setText("CO: "+progress + " ppm");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        Button addBtn = findViewById(R.id.setBtn);
        //press button to send all four values to the slave
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                writeActivity(mBluetoothSocket);
                writeInput(mBluetoothSocket, fillString(textCH4.getText().toString(),20));
                writeInput(mBluetoothSocket, fillString(textIBUT.getText().toString(),20));
                writeInput(mBluetoothSocket, fillString(textO2.getText().toString(),20));
                writeInput(mBluetoothSocket, fillString(textCO.getText().toString(),20));

            }
        });

    }

    public void writeActivity(BluetoothSocket socket) {
        String text = "In MainActivity\n";
        Log.d(TAG, "write: Writing to outputstream: " + text);

        try {
            socket.getOutputStream().write(text.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }
    public void writeInput(BluetoothSocket socket, String input){
        try {
            socket.getOutputStream().write(input.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }
    public String fillString(String mString, int i){
        if(mString.length() >= i)
        {
            Log.d(TAG,"this String exceeds the limit of the function, returning original String");
            return mString;
        }
        else {
            int deficit = i - mString.length();
            int j;
            for(j = 0; j < deficit; j++){
                mString = mString+" ";
            }
            return mString;
        }

    }


}
