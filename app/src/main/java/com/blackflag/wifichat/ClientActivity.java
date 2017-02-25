package com.blackflag.wifichat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class ClientActivity extends AppCompatActivity {

    EditText editText;
    TextView disp,i1,i2,i3,i4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        editText= (EditText) findViewById(R.id.message);
        disp= (TextView) findViewById(R.id.display);
        i1= (TextView) findViewById(R.id.ip1);
        i2= (TextView) findViewById(R.id.ip2);
        i3= (TextView) findViewById(R.id.ip3);
        i4= (TextView) findViewById(R.id.ip4);

        findViewById(R.id.connect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    connectWebsoket(i1.getText().toString(),i2.getText().toString(),i3.getText().toString(),i4.getText().toString());
                    mWebSocketClient.connect();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               mWebSocketClient.send(editText.getText().toString());
            }
        });


    }
    public static String ip1="0";
    public static String ip2="102";




    //methos

    public   boolean isConnected=false;
    public  WebSocketClient mWebSocketClient;
    public  void connectWebsoket(String ip1,String ip2,String ip3,String ip4) throws Exception
    {

        Log.d("%%%%%%%%%%","connectedWebsoket");
        URI uri;
        try {

            uri = new URI("ws://"+ip1+"."+ip2+"."+ip3+"."+ip4+""+":7000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient=new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {

                Log.d("%%%%","open");
                isConnected=true;
            }

            @Override
            public void onMessage(final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        disp.append("\n Server Says : " +message+"\n");
//stuff that updates ui

                    }
                });

            }

            @Override
            public void onClose(int code, String reason, boolean remote) {

                isConnected=false;


            }
            @Override
            public void onError(Exception ex) {

                Log.d("%%%%",ex.getMessage());
                isConnected=false;

            }
        };
    }

}
