package com.cdi.temiwoz;

import android.content.pm.PackageManager;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.TtsRequest.Status;

import com.robotemi.sdk.Robot.TtsListener;
import com.robotemi.sdk.Robot.AsrListener;
import com.robotemi.sdk.listeners.OnGoToLocationStatusChangedListener;

import org.jetbrains.annotations.NotNull;

public class RobotApi implements TtsListener,
                                 AsrListener,
                                 OnGoToLocationStatusChangedListener {

    private Robot robot;

    public TemiWebsocketServer server;

    RobotApi (Robot robotInstance) {
        robot = robotInstance;
        robot.addTtsListener(this);
        robot.addAsrListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        // robot.toggleNavigationBillboard(false);
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

    @Override
    public void onTtsStatusChanged(TtsRequest ttsRequest) {
        if (ttsRequest.getStatus() == Status.COMPLETED) {
            server.broadcast("TTS_COMPLETED/" + ttsRequest.getSpeech());
        }
    }

    @Override
    public void onGoToLocationStatusChanged(String location, @GoToLocationStatus String status, int descriptionId, String description) {
        if (status.equals("complete")) {
            server.broadcast("GOTO_COMPLETED/" + location);
        }
    }

    @Override
    public void onAsrResult(@NotNull String text) {
        server.broadcast("ASR_COMPLETED/" + text);
        robot.finishConversation();
    }

    public void stop() {
        //robot.removeTtsListener(this);
        //robot.removeAsrListener(this);
        //robot.removeOnGoToLocationStatusChangedListener(this);
    }
}
