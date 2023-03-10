package com.cdi.temiwoz;

import android.content.pm.PackageManager;


import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.TtsRequest.Status;


import com.robotemi.sdk.constants.*;
import com.robotemi.sdk.telepresence.*;
import com.robotemi.sdk.Robot.TtsListener;
import com.robotemi.sdk.Robot.AsrListener;
import com.robotemi.sdk.listeners.OnBeWithMeStatusChangedListener;
import com.robotemi.sdk.listeners.OnConstraintBeWithStatusChangedListener;
import com.robotemi.sdk.UserInfo;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;



import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.util.ArrayList;
import java.util.List;


public class RobotApi implements TtsListener,
                                 AsrListener,
                                 OnGoToLocationStatusChangedListener,
        OnBeWithMeStatusChangedListener,
        OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener
{

    private Robot robot;

    public TemiWebsocketServer server;

    String speak_id;
    String ask_id;
    String goto_id;
    String tilt_id;
    String turn_id;
    String getContact_id;
    String call_id;


    RobotApi (Robot robotInstance) {
        robot = robotInstance;
        robot.addTtsListener(this);
        robot.addAsrListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnConstraintBeWithStatusChangedListener(this);
        // robot.toggleNavigationBillboard(false);

    }



    // location related
    public void gotoLocation(String location, String id) {
        robot.goTo(location);
        goto_id = id;
    }
    public void saveLocation(String location, String id){
        boolean finished = robot.saveLocation(location);

        try {
            server.broadcast(new JSONObject().put("saveLocation",finished).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void deleteLocation(String location, String id){
        boolean finished = robot.saveLocation(location);

        try {
            server.broadcast(new JSONObject().put("deleteLocation",finished).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //movement..
    public void stopMovement(String id){
        robot.stopMovement();
    }

    public void beWithMe(String id){
        robot.beWithMe();
    }

    public void constraintBeWith(String id){
        robot.constraintBeWith();
    }


    public void tiltAngle(int angle, String id) {
        robot.tiltAngle(angle);
        tilt_id = id;
        try {
            server.broadcast(new JSONObject().put("id", tilt_id).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void turnBy(int angle, String id) {
        robot.turnBy(angle);
        turn_id = id;
        try {
            server.broadcast(new JSONObject().put("id", turn_id).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // call someone
    public void getContact(String id){
        List<UserInfo> list = new ArrayList<>();
        list = robot.getAllContact();

        getContact_id = id;

        try{
            server.broadcast(new JSONObject().put("id", getContact_id).put("userinfo",list).toString());
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void startCall(String userId, String id){
        call_id = id;
        robot.startTelepresence("孙老师", userId);


    }

    // user interaction
    public void wakeup(String id){
        robot.wakeup();
    }
    public void speak(String sentence, String id) {
        robot.speak(TtsRequest.create(sentence, false));
        speak_id = id;
    }

    public void askQuestion(String sentence, String id) {
        robot.askQuestion(sentence);
        ask_id = id;
    }

    public void setDetectionMode(boolean on, String id){
        robot.setDetectionModeOn(on);
        try {
            server.broadcast(new JSONObject().put("detection mode", on).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDetectionStateChanged(int state){
        try {
            server.broadcast(new JSONObject().put("detectionState", state).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBeWithMeStatusChanged(String status ){
        try {
            server.broadcast(new JSONObject().put("BeWithMeStatus", status).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onConstraintBeWithStatusChanged(boolean isConstraint) {
        try{
            server.broadcast(new JSONObject().put("isConstraint",isConstraint).toString());
        }catch (JSONException e ){
            e.printStackTrace();
        }
    }



    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == Status.COMPLETED) {
            try {
                server.broadcast(new JSONObject().put("id", speak_id).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onGoToLocationStatusChanged(String location, @GoToLocationStatus String status, int descriptionId, String description) {
        if (status.equals("complete")) {
            try {
                server.broadcast(new JSONObject().put("id", goto_id).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onAsrResult(@NotNull String text) {
        try {
            server.broadcast(new JSONObject().put("id", ask_id).put("reply",text).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        robot.finishConversation();
    }

    public void stop() {
        //robot.removeTtsListener(this);
        //robot.removeAsrListener(this);
        //robot.removeOnGoToLocationStatusChangedListener(this);
    }
}
