package com.cdi.temiwoz;

import android.content.pm.PackageManager;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.TtsRequest.Status;

import com.robotemi.sdk.constants.*;
import com.robotemi.sdk.telepresence.*;
import com.robotemi.sdk.Robot.TtsListener;
import com.robotemi.sdk.Robot.AsrListener;
import com.robotemi.sdk.UserInfo;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;
import org.json.*;

import java.util.ArrayList;
import java.util.List;


public class RobotApi implements TtsListener,
                                 AsrListener,
                                 OnGoToLocationStatusChangedListener {

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
        // robot.toggleNavigationBillboard(false);
    }

    public void speak(String sentence, String id) {
        robot.speak(TtsRequest.create(sentence, false));
        speak_id = id;
    }


    public void askQuestion(String sentence, String id) {
        robot.askQuestion(sentence);
        ask_id = id;
    }

    public void gotoLocation(String location, String id) {
        robot.goTo(location);
        goto_id = id;
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
