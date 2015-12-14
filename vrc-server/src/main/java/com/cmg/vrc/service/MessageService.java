package com.cmg.vrc.service;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.cmg.vrc.properties.Configuration;
import org.apache.log4j.Logger;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class MessageService {
    private static final int MULTICAST_SIZE = 1000;
    private static Logger logger = Logger.getLogger(MessageService.class
            .getName());
    private Sender sender;
    private static final Executor threadPool = Executors.newFixedThreadPool(5);
    private final Message message;

    public MessageService(Message message) {
        this.message = message;
    }

    protected void doPostMessage(List<String> devices) throws Exception {
        if (sender == null) {
            sender = new Sender(Configuration.getValue("api.key"));
        }
        String status;
        if (devices.isEmpty()) {
            status = "Message ignored as there is no device registered!";
        } else {
            // NOTE: check below is for demonstration purposes; a real
            // application
            // could always send a multicast, even for just one recipient
            if (devices.size() == 1) {
                // send a single message using plain post
                String registrationId = devices.get(0);
                Result result = sender.send(message, registrationId, 5);
                status = "Sent message to one device: " + result;
            } else {
                // send a multicast message using JSON
                // must split in chunks of 1000 devices (GCM limit)
                int total = devices.size();
                List<String> partialDevices = new ArrayList<String>(total);
                int counter = 0;
                int tasks = 0;
                for (String device : devices) {
                    counter++;
                    partialDevices.add(device);
                    int partialSize = partialDevices.size();
                    if (partialSize == MULTICAST_SIZE || counter == total) {
                        asyncSend(partialDevices);
                        partialDevices.clear();
                        tasks++;
                    }
                }
                status = "Asynchronously sending " + tasks
                        + " multicast messages to " + total + " devices";
            }
        }
        logger.info(status);
    }

    private void asyncSend(List<String> partialDevices) {
        if (sender == null) {
            sender = new Sender(Configuration.getValue("api.key"));
        }
        // make a copy
        final List<String> devices = new ArrayList<String>(partialDevices);
        threadPool.execute(new Runnable() {

            public void run() {
                MulticastResult multicastResult = null;
                try {
                    multicastResult = sender.send(message, devices, 5);
                } catch (IOException e) {
                    logger.error("Error when send message", e);
                    e.printStackTrace();
                }
                if (multicastResult != null) {
                    List<Result> results = multicastResult.getResults();
                    // analyze the results
                    for (int i = 0; i < devices.size(); i++) {
                        String regId = devices.get(i);
                        Result result = results.get(i);
                        String messageId = result.getMessageId();
                        if (messageId != null) {
                            System.out
                                    .println("Succesfully sent message to device: "
                                            + regId
                                            + "; messageId = "
                                            + messageId);
                            String canonicalRegId = result
                                    .getCanonicalRegistrationId();
                            if (canonicalRegId != null) {
                                // same device has more than on registration id:
                                // update it
                                logger.info("canonicalRegId " + canonicalRegId);
                            }
                        } else {
                            String error = result.getErrorCodeName();
                            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                                // application has been removed from device -
                                // unregister it
                                logger.info("Unregistered device: " + regId);
                            } else {
                                logger.info("Error sending message to " + regId
                                        + ": " + error);
                            }
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        Message message = new Message.Builder().addData("data", "test").build();
        MessageService messageService = new MessageService(message);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("APA91bGKp5kmhgjM1_oh8JU8F6aGyAPL53HpmmLKup5zDoQPqxWMr2w-1QL7KHMAjUte-cmBx_GybV_9IEZ7qfSuAmcQ7ci8TxodbTBIjFun4XleUR6THSeCRUiqQWoI96ybwzAvBATf");
        try {
            messageService.doPostMessage(arrayList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}