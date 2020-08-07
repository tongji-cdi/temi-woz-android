package com.cdi.temiwoz;

import android.content.pm.PackageManager;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.TtsRequest.Status;

import com.robotemi.sdk.Robot.TtsListener;

public class RobotApi implements TtsListener {

    private Robot robot;

    public TemiWebsocketServer server;

    RobotApi (Robot robotInstance) {
        robot = robotInstance;
        robot.addTtsListener(this);
    }

    public void speak(String sentence) {
        robot.speak(TtsRequest.create(sentence, false));
    }

    public void askQuestion(String sentence) {
        robot.askQuestion(sentence);
    }

    public void gotoLocation(String location) {
        robot.goTo(location);
    }

    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == Status.COMPLETED) {
            server.broadcast("TTS_COMPLETED/" + ttsRequest.getSpeech());
        }
    }

}
