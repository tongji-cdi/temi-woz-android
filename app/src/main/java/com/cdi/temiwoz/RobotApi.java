package com.cdi.temiwoz;

import android.content.pm.PackageManager;

import com.robotemi.sdk.*;
import com.robotemi.sdk.Robot.*;
import com.robotemi.sdk.Robot.Companion;
import com.robotemi.sdk.activitystream.ActivityStreamObject;
import com.robotemi.sdk.activitystream.ActivityStreamPublishMessage;
import com.robotemi.sdk.constants.*;
import com.robotemi.sdk.exception.OnSdkExceptionListener;
import com.robotemi.sdk.exception.SdkException;
import com.robotemi.sdk.face.ContactModel;
import com.robotemi.sdk.face.OnContinuousFaceRecognizedListener;
import com.robotemi.sdk.face.OnFaceRecognizedListener;
import com.robotemi.sdk.listeners.*;
import com.robotemi.sdk.map.MapModel;
import com.robotemi.sdk.map.OnLoadMapStatusChangedListener;
import com.robotemi.sdk.model.CallEventModel;
import com.robotemi.sdk.model.DetectionData;
import com.robotemi.sdk.navigation.listener.OnCurrentPositionChangedListener;
import com.robotemi.sdk.navigation.listener.OnDistanceToLocationChangedListener;
import com.robotemi.sdk.navigation.listener.OnReposeStatusChangedListener;
import com.robotemi.sdk.navigation.model.Position;
import com.robotemi.sdk.navigation.model.SafetyLevel;
import com.robotemi.sdk.navigation.model.SpeedLevel;
import com.robotemi.sdk.permission.OnRequestPermissionResultListener;
import com.robotemi.sdk.permission.Permission;
import com.robotemi.sdk.sequence.OnSequencePlayStatusChangedListener;
import com.robotemi.sdk.sequence.SequenceModel;
import com.robotemi.sdk.voice.ITtsService;

import org.jetbrains.annotations.NotNull;
import org.json.*;

public class RobotApi implements OnRobotReadyListener,
        ConversationViewAttachesListener, WakeupWordListener, ActivityStreamPublishListener,
        TtsListener, OnBeWithMeStatusChangedListener, OnGoToLocationStatusChangedListener,
        OnLocationsUpdatedListener, OnConstraintBeWithStatusChangedListener,
        OnDetectionStateChangedListener, AsrListener, OnTelepresenceEventChangedListener,
        OnRequestPermissionResultListener, OnDistanceToLocationChangedListener,
        OnCurrentPositionChangedListener, OnSequencePlayStatusChangedListener, OnRobotLiftedListener,
        OnDetectionDataChangedListener, OnUserInteractionChangedListener, OnFaceRecognizedListener,
        OnConversationStatusChangedListener, OnTtsVisualizerWaveFormDataChangedListener,
        OnTtsVisualizerFftDataChangedListener, OnReposeStatusChangedListener,
        OnLoadMapStatusChangedListener, OnDisabledFeatureListUpdatedListener,
        OnMovementVelocityChangedListener, OnMovementStatusChangedListener,
        OnContinuousFaceRecognizedListener, ITtsService,
        TextToSpeech.OnInitListener, OnSdkExceptionListener {

    private Robot robot;

    public TemiWebsocketServer server;

    String speak_id;
    String ask_id;
    String goto_id;
    String tilt_id;
    String turn_id;


    RobotApi (Robot robotInstance) {
        robot = robotInstance;
        robot.addOnRequestPermissionResultListener(this);
        robot.addOnTelepresenceEventChangedListener(this);
        robot.addOnFaceRecognizedListener(this);
        robot.addOnContinuousFaceRecognizedListener(this);
        robot.addOnLoadMapStatusChangedListener(this);
        robot.addOnDisabledFeatureListUpdatedListener(this);
        robot.addOnSdkExceptionListener(this);
        robot.addOnMovementStatusChangedListener(this);
        robot.addTtsListener(this);
        robot.addAsrListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addOnRobotReadyListener(this);
        robot.addOnBeWithMeStatusChangedListener(this);
        robot.addOnGoToLocationStatusChangedListener(this);
        robot.addConversationViewAttachesListener(this);
        robot.addWakeupWordListener(this);
        robot.addTtsListener(this);
        robot.addOnLocationsUpdatedListener(this);
        robot.addOnConstraintBeWithStatusChangedListener(this);
        robot.addOnDetectionStateChangedListener(this);
        robot.addAsrListener(this);
        robot.addOnDistanceToLocationChangedListener(this);
        robot.addOnCurrentPositionChangedListener(this);
        robot.addOnSequencePlayStatusChangedListener(this);
        robot.addOnRobotLiftedListener(this);
        robot.addOnDetectionDataChangedListener(this);
        robot.addOnUserInteractionChangedListener(this);
        robot.addOnConversationStatusChangedListener(this);
        robot.addOnTtsVisualizerWaveFormDataChangedListener(this);
        robot.addOnTtsVisualizerFftDataChangedListener(this);
        robot.addOnReposeStatusChangedListener(this);
        robot.addOnMovementVelocityChangedListener(this);
        robot.showTopBar();
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
            server.broadcast(new JSONObject().put("id", ask_id).toString());
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
