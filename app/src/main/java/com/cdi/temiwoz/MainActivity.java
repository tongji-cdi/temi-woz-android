package com.cdi.temiwoz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.net.UnknownHostException;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;

public class MainActivity extends AppCompatActivity implements OnRobotReadyListener {

    private Robot robot;
    private RobotApi robotApi;
    TemiWebsocketServer server;

    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        robot = Robot.getInstance();
        robotApi = new RobotApi(robot);
        pDialog = new ProgressDialog(this);
        pDialog.setTitle("Temi WOZ platform");
        pDialog.setMessage("Waiting for a connection...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        // pDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add listeners
        robot.addOnRobotReadyListener(this);

        // Add WebSocket server
        int port = 8175;
        try {
            server = new TemiWebsocketServer( port );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.addRobotApi(robotApi);
        server.start();
    }

    @Override
    protected void onPause() {
        // Stop listeners
        robot.removeOnRobotReadyListener(this);
        // Stop WebSocket Server
        try {
            server.stop(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    public void onRobotReady(boolean isReady) {
        if (isReady) {
            try {
                final ActivityInfo activityInfo = getPackageManager().getActivityInfo(getComponentName(), PackageManager.GET_META_DATA);
                robot.onStart(activityInfo);
            } catch (PackageManager.NameNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}