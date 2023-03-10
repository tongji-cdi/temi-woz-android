package com.cdi.temiwoz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Collections;

import org.java_websocket.WebSocket;
import org.java_websocket.WebSocketImpl;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.protocols.IProtocol;
import org.java_websocket.server.WebSocketServer;

import org.json.*;

public class TemiWebsocketServer extends WebSocketServer {

    MainActivity activity;
    RobotApi robot;

    public TemiWebsocketServer( int port ) throws UnknownHostException {
        super( new InetSocketAddress( port ) );
    }

    public TemiWebsocketServer( InetSocketAddress address ) {
        super( address );
    }

    public TemiWebsocketServer(int port, Draft_6455 draft) {
        super( new InetSocketAddress( port ), Collections.<Draft>singletonList(draft));
    }

    public void addActivity(MainActivity someActivity) {
        activity = someActivity;
    }

    public void addRobotApi(RobotApi api) {
        robot = api;
        robot.server = this;
    }

    @Override
    public void onOpen( WebSocket conn, ClientHandshake handshake ) {
        conn.send("Temi is ready to receive commands!"); //This method sends a message to the new client
    }

    @Override
    public void onClose( WebSocket conn, int code, String reason, boolean remote ) {
        broadcast( conn + " has left the room!" );
    }

    @Override
    public void onMessage( WebSocket conn, String message ) {

        conn.send("get a new message resolving...");
        try {

            final JSONObject cmd = new JSONObject(message);

            //conn.send("command:" + cmd.getString("command"));


            switch (cmd.getString("command")) {
                case "openURL":
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                activity.setInterfaceUrl(cmd.getString("url"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    break;
                case "speak":
                    robot.speak(cmd.getString("sentence"), cmd.getString("id"));
                    conn.send("msg:" + message);
                    broadcast(message);
                    break;
                case "ask":
                    robot.askQuestion(cmd.getString("sentence"), cmd.getString("id"));
                    break;
                case "goto":
                    robot.gotoLocation(cmd.getString("location"), cmd.getString("id"));
                    break;
                case "tilt":
                    robot.tiltAngle(cmd.getInt("angle"), cmd.getString("id"));
                    break;
                case "turn":
                    robot.turnBy(cmd.getInt("angle"), cmd.getString("id"));
                    break;
                case "getContact":
                    robot.getContact(cmd.getString("id"));
                    break;
                case "call":
                    robot.startCall(cmd.getString("userId"), cmd.getString("id"));
                    break;
                case "wakeup":
                    robot.wakeup(cmd.getString("id"));
                    break;
                case "saveLocation":
                    robot.saveLocation(cmd.getString("locationName"), cmd.getString("id"));
                    break;
                case "deleteLocation":
                    robot.deleteLocation(cmd.getString("locationName"), cmd.getString("id"));
                    break;
                case "stopMovement":
                    robot.stopMovement(cmd.getString("id"));
                    break;
                case "setDetectionMode":
                    robot.setDetectionMode(cmd.getBoolean("on"), cmd.getString("id"));
                    break;
                default:
                    System.out.println("Invalid command");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMessage( WebSocket conn, ByteBuffer message ) {
        broadcast( message.array() );
    }

    @Override
    public void onError( WebSocket conn, Exception ex ) {
        ex.printStackTrace();
        if( conn != null ) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }

    @Override
    public void onStart() {
        setConnectionLostTimeout(0);
        setConnectionLostTimeout(100);
    }

}
