package com.cdi.temiwoz;

import androidx.appcompat.app.AppCompatActivity;
// import androidx.recyclerview.widget.OrientationHelper;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import java.net.UnknownHostException;

/*
import com.facebook.litho.Component;
import com.facebook.litho.ComponentContext;
import com.facebook.litho.LithoView;
import com.facebook.litho.sections.SectionContext;
import com.facebook.litho.sections.widget.ListRecyclerConfiguration;
import com.facebook.litho.sections.widget.RecyclerCollectionComponent;
*/

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.listeners.*;

public class MainActivity extends AppCompatActivity implements OnRobotReadyListener {

    private Robot robot;
    private RobotApi robotApi;
    WebView interfaceView;
    TemiWebsocketServer server;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        interfaceView = new WebView(this);
        WebSettings webSettings = interfaceView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(false);
        webSettings.setDomStorageEnabled(true);
        webSettings.setBuiltInZoomControls(false);



        setContentView(interfaceView);

        interfaceView.loadUrl("http://baidu.com");

        robot = Robot.getInstance();
        robotApi = new RobotApi(robot);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Add listeners
        robot.addOnRobotReadyListener(this);
        robot.showTopBar();

        // Add WebSocket server
        int port = 8175;
        try {
            server = new TemiWebsocketServer( port );
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        server.addActivity(this);
        server.addRobotApi(robotApi);
        server.start();
    }

    @Override
    protected void onPause() {
        // Stop listeners
        robot.removeOnRobotReadyListener(this);

        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listeners
        robot.removeOnRobotReadyListener(this);
        // Stop WebSocket Server
        try {
            server.stop(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        robotApi.stop();
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

    public void setInterfaceUrl(String url) {
        interfaceView.loadUrl(url);
    }
}