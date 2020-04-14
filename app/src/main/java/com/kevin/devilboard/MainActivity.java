package com.kevin.devilboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.kevin.devilboard.models.LogMessage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    MqttAndroidClient mqttAndroidClient;

    final String serverUri = "tcp://134.209.144.25:1883";
    String clientId = "ExampleAndroidClient";
    final String subscriptionTopic = "D/+";
    private RecyclerView recyclerView;

    private ArrayList<LogMessage> messages = new ArrayList<>();
    KAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDevl();
        recyclerView = findViewById(R.id.rv);
        // messages.add("Hi welcome to Devil logs");
        adapter = new KAdapter(messages, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
//        RxEventBus.getInstance().listenTo(RxEvent.class)
//                .filter(rxEvent -> rxEvent.getCode() == 1883)
//                .subscribeOn(RxSchedulerProvider.computation())
//                .observeOn(RxSchedulerProvider.ui())
//                .subscribe(rxEvent -> {
//                    try {
//                        count.setText(rxEvent.getName());
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }
//                });
    }

    private void initDevl() {
        clientId = clientId + System.currentTimeMillis();
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), serverUri, clientId);
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {

                if (reconnect) {
                    log("Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic();

                } else {
                    // publishMessage();
                    log("Connected to: " + serverURI);

                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                log("The Connection was lost.");
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                log("Incoming message: " + new String(message.getPayload()));
//                RxEvent rxEvent = new RxEvent(message.toString(), 1883);
//                RxEventBus.getInstance().publish(rxEvent);

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);


        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    subscribeToTopic();


                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    log("Failed to connect to: " + serverUri);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }

    }


    final String DEBUG = "DEBUG";
    final String ERROR = "ERROR";
    final String REQUEST = "REQUEST";
    final String FAILED_REQUEST = "FAILED_REQUEST";
    final String EXCEPTION = "EXCEPTION";
    final String ALIVE = "ALIVE";
     final String DEAD = "DEAD";


    private void update(String topic, String message) {
        recyclerView.scrollToPosition(messages.size() - 1);
        if (topic.contains(DEBUG))
            messages.add(new LogMessage(DEBUG, message));
        else if (topic.contains(ERROR))
            messages.add(new LogMessage(ERROR, message));
        else if (topic.contains(FAILED_REQUEST))
            messages.add(new LogMessage(FAILED_REQUEST, message));
        else if (topic.contains(EXCEPTION))
            messages.add(new LogMessage(EXCEPTION, message));
        else if (topic.contains(ALIVE))
            messages.add(new LogMessage(ALIVE, message));
        else if (topic.contains(REQUEST))
            messages.add(new LogMessage(REQUEST, message));
        else if (topic.contains(DEAD))
            messages.add(new LogMessage(DEAD, message));

        adapter.notifyDataSetChanged();
        recyclerView.smoothScrollToPosition(messages.size() - 1);
    }

    private void subscribeToTopic() {
        try {
            mqttAndroidClient.subscribe(subscriptionTopic, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    log("Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    log("Failed to subscribe");
                }
            });

            // THIS DOES NOT WORK!
            mqttAndroidClient.subscribe(subscriptionTopic, 1, (topic, message) -> {
                // message Arrived!
                update(topic, new String(message.getPayload()));
            });

        } catch (MqttException ex) {
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    private void log(String message) {
        Log.e("Mqtt client ", message);
    }

//
//    RxEvent rxEvent = new RxEvent(Constants.RxEventNames.AUTO_AUTH_FLOW_EVENT, Constants.RxEventCodes.AUTO_AUTH_RECALL);
//            rxEvent.setStringPayload(AutoAuthController.autoAuthMap);
//            RxEventBus.getInstance().publish(rxEvent);


    //    RxEventBus.getInstance().listenTo(RxEvent.class)
//                .filter(rxEvent -> rxEvent.getCode() == Constants.RxEventCodes.FCO_FLOW_SDK_PROGRESS_CALL_RECEIVED)
//            .subscribeOn(RxSchedulerProvider.computation())
//            .observeOn(RxSchedulerProvider.ui())
//            .subscribe(rxEvent -> {
//        RxEventBus.getInstance().publish(new RxEvent(Constants.RxEventNames.FCO_FLOW_SDK_PROGRESS_SENT, Constants.RxEventCodes.FCO_FLOW_SDK_PROGRESS_SENT));
//    });
//    public void publishMessage() {
//
//        try {
//            MqttMessage message = new MqttMessage();
//            message.setPayload(publishMessage.getBytes());
//            mqttAndroidClient.publish(publishTopic, message);
//            log("Message Published");
//            if (!mqttAndroidClient.isConnected()) {
//                log(mqttAndroidClient.getBufferedMessageCount() + " messages in buffer.");
//            }
//        } catch (MqttException e) {
//            System.err.println("Error Publishing: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

}
