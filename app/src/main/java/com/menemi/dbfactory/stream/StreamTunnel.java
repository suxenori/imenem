package com.menemi.dbfactory.stream;

import android.util.Log;

import com.menemi.dbfactory.stream.messages.StreamMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;

/**
 * Created by irondev on 08.06.16.
 */
public class StreamTunnel {

    private static final int CONNECT_TIMEOUT_MILLIS = 20_000;
    Socket socket;
    String ipAdress = "";
    int port = 0;
    String receivedData = "";
    CommunicationDelegate delegate = null;
    boolean socketCreated = false;



    boolean ping() {
        return true;
        // writeData("p");
    }

    boolean isConnected() {
        return socketCreated && socket.isConnected();
    }

    void initWithIP(String ipAdress, int port) {
        this.port = port;
        this.ipAdress = ipAdress;
        this.receivedData = "";

    }

    void connect() {
        if (this.isConnected()) {
            return;
        }

        //mSocket = GCDAsyncSocket(delegate: self, delegateQueue: dispatch_get_main_queue())
        socketCreated = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket();
                    socket.connect(new InetSocketAddress(ipAdress, port));
                    afterConnectToHost(ipAdress, port);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    delegate.didDisconnectFromHost();
                } catch (IOException e) {
                    e.printStackTrace();
                    delegate.didDisconnectFromHost();
                }
            }
        }).start();


    }

    void setInSocket(Socket socket) {
        if (!socket.isConnected()) {
            this.socket = socket;
            socketCreated = true;
            //mSocket!.setDelegate(self, delegateQueue: dispatch_get_main_queue())
        }
    }

    void disconnect() {
        if (this.socket != null) {
            //mSocket!.setDelegate(nil, delegateQueue: dispatch_get_main_queue())
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
            socketCreated = false;
        }
    }

    void reconnect() {
        //mSocket!.setDelegate(nil, delegateQueue: dispatch_get_main_queue())
        try {
            if (socket == null) {
                return;
            }
            socket.close();
            Thread.sleep(3000);
            reconnectRoutine();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    void reconnectRoutine() {
        socket = null;
        socketCreated = false;
        connect();
    }

    void checkIncomingData() {
        if (socketCreated) {
            //socket.readDataWithTimeout(-1, tag: 0)
        }
    }

    //
    boolean writeData(String data) {
        if (socketCreated) {
            byte[] encodedBytes = data.getBytes(Charset.forName("UTF-8"));

            try {
                BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
                out.write(encodedBytes);
                out.flush();
                afterWrite();
                return true;
            } catch (SocketException se) {
                reconnectRoutine();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    void startReadData() {

        if (socketCreated) {
            try {
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream());


                Log.d("COMMAND", "start read data");

                while (true) {

                    char val = (char) in.read();
                    //Log.d("COMMAND", "read " + val);

                    afterRead("" + val);
                }

            }catch (java.net.SocketException se){
                socketDidDisconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void socketDidDisconnect() {
        this.delegate.didDisconnectFromHost();

    }

    /**
     * Called when a socket has completed writing the requested data. Not called if there is an error.
     */
    private void afterWrite() {
        Log.v("StreamTunel", "Did Write Data to socket: " + ipAdress);
    }

    /**
     * Called when a socket has completed reading the requested data into memory. Not called if there is an error.
     */
    private void afterRead(String data) {
      //  Log.v("StreamTunnel", "SOCKET RCVD: %@ DATA: |%ld|%@| from " + ipAdress);

        if (receivedData == null) {
            receivedData = "";
        }

        receivedData += data;

        String receivedStr = receivedData;


        if (receivedStr.contains(StreamController.MESSAGE_BEGIN) && receivedStr.contains(StreamController.MESSAGE_END)) {
            String parts[] = receivedStr.split(StreamController.MESSAGE_BEGIN);
            receivedData = "";
            Log.v("StreamTunel", "contains!");
            for (int i = 0; i < parts.length; i++) {
                if (!parts[i].equals("")) {
                    if (parts[i].contains(StreamController.MESSAGE_END)) {

                        String bodie = parts[i].replaceAll(StreamController.MESSAGE_END, "");

                        if (!bodie.equals("")) {
                            parts[i] = "";
                            Log.d("COMMAND", "bodies[i]" + bodie);
                            StreamMessage message = StreamMessage.fromJSON(bodie);
                            this.delegate.didReceivedMessage(this, message);

                        }

                    } else {
                        receivedData = "";
                        if(parts[i].equals("-1")){
                            socket = null;
                            socketCreated = false;
                            socketDidDisconnect();
                        }
                        receivedData += StreamController.MESSAGE_BEGIN + parts[i];
                        break;
                    }
                }
            }

        }
    }

    /**
     * Called when a socket connects and is ready for reading and writing.
     *
     * @param host IP address, not a DNS name
     * @param port
     */
    void afterConnectToHost(String host, int port) {
        Log.v("StreamTunnel", "Connection with " + host + ":" + port + " established");

        this.delegate.didConnectToHost();

        //SEND FULL LIST OF OBJECTS
        //TODO:!?
        //if([[self delegate] respondsToSelector:@selector(didConnectToHost)])
        //[self.delegate didConnectToHost];
    }


    public interface CommunicationDelegate {
        void didReceivedMessage(StreamTunnel sender, StreamMessage message);

        void didConnectToHost();

        void didDisconnectFromHost();
    }


}
