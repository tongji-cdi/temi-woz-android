package com.cdi.temiwoz;

import androidx.appcompat.app.AppCompatActivity;
// import androidx.recyclerview.widget.OrientationHelper;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements OnRobotReadyListener {

    private Robot robot;
    private RobotApi robotApi;
    TemiWebsocketServer server;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        final ComponentContext context = new ComponentContext(this);

        final Component component = RecyclerCollectionComponent.create(context)
                .recyclerConfiguration(ListRecyclerConfiguration.create().orientation(OrientationHelper.HORIZONTAL).build())
                .disablePTR(true)
                .section(OntologyList.create(new SectionContext(context)).build())
                .build();

        setContentView(LithoView.create(context, component));
         */
        WebView myWebView = new WebView(this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl("http://10.0.1.10:5000/static/index.html");
        setContentView(myWebView);


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
}