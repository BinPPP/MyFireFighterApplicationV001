package com.example.myfirefighterapplicationv001;

import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
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
    private int yellowColorValue = Color.YELLOW;
    private int greenColorValue = Color.GREEN;
    private int redColorValue = Color.RED;
    private GasValues mGasValues = new GasValues(0,0,0,0);


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
        //set up progress drawable so that we may see the alarm level in runtime
        LayerDrawable layerDrawableCH4 = (LayerDrawable) ringCH4.getProgressDrawable();
        final Drawable progressDrawableCH4 = layerDrawableCH4.findDrawableByLayerId(android.R.id.progress);
        progressDrawableCH4.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
        LayerDrawable layerDrawableIBUT = (LayerDrawable) ringIBUT.getProgressDrawable();
        final Drawable progressDrawableIBUT = layerDrawableIBUT.findDrawableByLayerId(android.R.id.progress);
        progressDrawableCH4.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
        LayerDrawable layerDrawableO2 = (LayerDrawable) ringO2.getProgressDrawable();
        final Drawable progressDrawableO2 = layerDrawableO2.findDrawableByLayerId(android.R.id.progress);
        progressDrawableCH4.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
        LayerDrawable layerDrawableCO = (LayerDrawable) ringCO.getProgressDrawable();
        final Drawable progressDrawableCO = layerDrawableCO.findDrawableByLayerId(android.R.id.progress);
        progressDrawableCH4.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);


        //drag listener for CH4
        barCH4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ringCH4.setProgress(progress/10);
                float dummy;
                dummy = (float)progress;
                dummy = dummy/10;
                mGasValues.setCH4(dummy);
                textCH4.setText("CH4: "+dummy+"%");
                if(dummy >= 10 && dummy < 20){
                    progressDrawableCH4.setColorFilter(yellowColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(dummy < 10){
                    progressDrawableCH4.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(dummy >= 20){
                    progressDrawableCH4.setColorFilter(redColorValue , PorterDuff.Mode.SRC_IN);
                }

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
                mGasValues.setIBUT(progress);
                textIBUT.setText("IBUT: "+progress+" ppm");
                if(progress >= 100 && progress < 200){
                    progressDrawableIBUT.setColorFilter(yellowColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(progress < 100){
                    progressDrawableIBUT.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(progress >= 200){
                    progressDrawableIBUT.setColorFilter(redColorValue , PorterDuff.Mode.SRC_IN);
                }
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
                mGasValues.setO2(dummy);
                textO2.setText("O2: "+ dummy + "%");
                if(dummy <= 19){
                    progressDrawableO2.setColorFilter(yellowColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(dummy < 23 && dummy > 19 ){
                    progressDrawableO2.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(progress >= 23){
                    progressDrawableO2.setColorFilter(redColorValue , PorterDuff.Mode.SRC_IN);
                }
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
                mGasValues.setCO(progress);
                textCO.setText("CO: "+progress + " ppm");
                if(progress >= 20 && progress < 100){
                    progressDrawableCO.setColorFilter(yellowColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(progress < 20){
                    progressDrawableCO.setColorFilter(greenColorValue , PorterDuff.Mode.SRC_IN);
                }
                else if(progress >= 100){
                    progressDrawableCO.setColorFilter(redColorValue , PorterDuff.Mode.SRC_IN);
                }
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
                if(mBluetoothSocket != null){
                    if(mBluetoothSocket.isConnected()){
                writeActivity(mBluetoothSocket);
                writeInputGasValues(mBluetoothSocket, mGasValues);}
                }
                Log.d(TAG, "the value for CH4 is sent as" + mGasValues.getCH4());
                for (byte b : mGasValues.getBytesCH4()) {
                    //System.out.println(Integer.toBinaryString(b & 255 | 256).substring(1));
                    Log.d(TAG,""+ Integer.toBinaryString(b & 255 | 256).substring(1));
                }
                Log.d(TAG, "the value for IBUT is sent as" + mGasValues.getIBUT());
                Log.d(TAG, "the value for O2 is sent as" + mGasValues.getO2());
                Log.d(TAG, "the value for CO is sent as" + mGasValues.getCO());


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
    public void writeInputString(BluetoothSocket socket, String input){
        try {
            socket.getOutputStream().write(input.getBytes());
        } catch (IOException e) {
            Log.e(TAG, "write: Error writing to output stream. " + e.getMessage() );
        }
    }

    public void writeInputGasValues(BluetoothSocket socket, GasValues mGasVlaues){
        try {
            socket.getOutputStream().write(mGasVlaues.getBytesCH4());
            socket.getOutputStream().write(mGasVlaues.getBytesIBUT());
            socket.getOutputStream().write(mGasVlaues.getBytesO2());
            socket.getOutputStream().write(mGasVlaues.getBytesCO());
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
