package com.blackflag.wifichat;

import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.w3c.dom.Text;

import java.net.InetSocketAddress;
import java.net.URI;

public class ServerActivity extends AppCompatActivity {

    WebSocketServer webSocketServer;
    static WebSocket webSocket;
    TextView disp,mess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        disp= (TextView) findViewById(R.id.display);
        mess= (TextView) findViewById(R.id.message);


        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webSocket!=null)
                    webSocket.send(mess.getText().toString());

            }
        });
        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                webSocketServer= new WebSocketServer(new InetSocketAddress(7000)) {
                    @Override
                    public void onOpen(final WebSocket conn, ClientHandshake handshake) {


                        webSocket=conn;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                disp.setText("\nClinet Connected"+conn.getRemoteSocketAddress());
//stuff that updates ui

                            }
                        });
                        Log.d("Server","Client Connected " +conn.getRemoteSocketAddress());


                    }

                    @Override
                    public void onClose(final WebSocket conn, int code, String reason, boolean remote) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                disp.append("\nClinet Disconnected"+conn.getRemoteSocketAddress());
//stuff that updates ui

                            }
                        });

                    }

                    @Override
                    public void onMessage(final WebSocket conn, final String message) {
                        /*disp.append(conn.getRemoteSocketAddress()+" Says:");
                        disp.append(message);*/
                        Log.d("Message : ","Message"+message);
                        conn.send("Got Your Message");

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                disp.append(conn.getRemoteSocketAddress()+"  Says : \n");
                                disp.append(message+"\n");
//stuff that updates ui

                            }
                        });
                    }

                    @Override
                    public void onError(WebSocket conn, Exception ex) {

                        //disp.append("Error"+ex.getMessage());
                        Log.d("Error",ex.getMessage());
                    }
                };
                webSocketServer.start();
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disp.append("Open At :"+ip);

                    }
                });

                Log.d("Server","Server Open");

            }
        });
        thread.start();


    }
}
