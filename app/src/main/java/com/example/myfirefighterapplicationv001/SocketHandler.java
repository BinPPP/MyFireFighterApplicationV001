package com.example.myfirefighterapplicationv001;

import android.bluetooth.BluetoothSocket;

//import java.net.Socket;

public class SocketHandler {
    private static BluetoothSocket socket;

    public static synchronized BluetoothSocket getSocket(){
        return socket;
    }

    public static synchronized void setSocket(BluetoothSocket socket){
        SocketHandler.socket = socket;
    }
}
