package com.example.myfirefighterapplicationv001;

import android.bluetooth.BluetoothSocket;

public class SocketHandler {
    static SocketHandler mSocketHandler;
    private SocketHandler(){

        //Prevent form the reflection api.
        if (mSocketHandler != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static synchronized SocketHandler getInstance(){
        if (mSocketHandler == null){ //if there is no instance available... create new one
            mSocketHandler = new SocketHandler();
        }

        return mSocketHandler;
    }

    private BluetoothSocket socket;

    public synchronized BluetoothSocket getSocket(){
        return socket;
    }

    public synchronized void setSocket(BluetoothSocket socket){
        this.socket = socket;
    }
}
