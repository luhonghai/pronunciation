package com.cmg.vrc.worker;

import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.cmg.vrc.worker.common.Constant;
import com.cmg.vrc.worker.util.AWSHelper;
import com.cmg.vrc.worker.util.UUIDGenerator;
import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by cmg on 17/09/15.
 */
public class AnalyzingSender {

    public enum Type {
        WORKER,
        AGENT
    }

    private static final Logger logger = Logger.getLogger(AnalyzingSender.class.getName());

    private static final int REQUEST_TIMEOUT = 3 * 60 * 1000;

    private static final int INTERVAL_TIME = 100;

    private final String url;

    private final Type type;

    public AnalyzingSender(Type type, String url) {
        this.url = url;
        this.type = type;
    }

    public AnalyzingResponse send(AnalyzingRequest request) {
        long start = System.currentTimeMillis();
        if (StringUtils.isEmpty(request.getId()))
            request.setId(UUIDGenerator.generateUUID());
        Gson gson = new Gson();
        switch (type) {
            case AGENT:
                String requestData = gson.toJson(request);
                HttpURLConnection urlConn;
                try {
                    URL mUrl = new URL(url);
                    urlConn = (HttpURLConnection) mUrl.openConnection();
                    urlConn.setConnectTimeout(REQUEST_TIMEOUT);
                    urlConn.setReadTimeout(REQUEST_TIMEOUT);
                    urlConn.setDoOutput(true);
                    urlConn.addRequestProperty("Content-Type", "application/json");
                    urlConn.setRequestProperty("Content-Length", Integer.toString(requestData.length()));
                    urlConn.getOutputStream().write(requestData.getBytes("UTF8"));
                    if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        return gson.fromJson(IOUtils.toString(urlConn.getInputStream(), "UTF-8"), AnalyzingResponse.class);
                    }
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Could not send analyzing request", e);
                }
                break;
            case WORKER:
                AWSHelper awsHelper = new AWSHelper(request.getRegionName(), request.getBucketName());
                SendMessageResult result = awsHelper.sendAnalyzingRequest(request, url);
                if (result != null && !StringUtils.isEmpty(result.getMessageId())) {
                    String keyName = Constant.CACHE_FOLDER + "/" + request.getAnalyzingKey();
                    while ((System.currentTimeMillis() - start) < REQUEST_TIMEOUT) {
                        S3Object s3Object = awsHelper.getS3Object(keyName);
                        if (s3Object != null) {
                            try {
                                AnalyzingResponse response = gson.fromJson(IOUtils.toString(s3Object.getObjectContent(), "UTF-8"), AnalyzingResponse.class);
                                awsHelper.deleteFile(keyName);
                                return response;
                            } catch (IOException e) {
                                logger.log(Level.SEVERE, "Could not fetch result object", e);
                                break;
                            }
                        }
                        try {
                            Thread.sleep(INTERVAL_TIME);
                        } catch (InterruptedException e) {}
                    }
                }
            default:
                break;
        }

        return null;
    }


    private static final int POOL_SIZE = 20;

    private static final int NUMBER_OF_REQUEST = 100 * 1000;

    private static final ExecutorService executorService
            = Executors.newFixedThreadPool(POOL_SIZE);

    public static void main(String[] args) {
 //       AnalyzingSender sender = new AnalyzingSender(Type.WORKER, "https://sqs.ap-southeast-1.amazonaws.com/673375924332/queue_vrc_worker");
        final AnalyzingSender sender = new AnalyzingSender(Type.AGENT, "http://vrc-agent.elasticbeanstalk.com");
        for (int i = 0; i < NUMBER_OF_REQUEST; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    long start = System.currentTimeMillis();
                    String requestId = UUIDGenerator.generateUUID();
                    AnalyzingRequest request = new AnalyzingRequest();
                    request.setId(requestId);
                    request.setBucketName("com-accenteasy-bbc-accent-dev");
                    request.setElasticCacheAddress("vrc-worker-cache.qvhoby.cfg.apse1.cache.amazonaws.com:11211");
                    request.setKeyName("voices/hai.lu@c-mg.com/able_cb35e4f3-90da-4b62-96ad-09076f66ff27_raw.wav");
                    request.setRegionName("ap-southeast-1");
                    request.setWord("able");
                    request.setDictionary(new AnalyzingRequest.S3File("dictionary-v1.dict", "sphinx-data/dict/beep-1.0"));
                    request.setNeighbourPhones(new AnalyzingRequest.S3File("cmubet_neighbour_phones.txt", "sphinx-data/extra/cmubet_neighbour_phones.txt"));
                    request.setPhoneDictionary(new AnalyzingRequest.S3File("cmuphonemedict", "sphinx-data/dict/cmuphonemedict"));
                    request.setAcousticModel(new AnalyzingRequest.S3File("wsj-en-us", "sphinx-data/wsj-en-us.zip"));
                    System.out.println("Send request ID: " + requestId);
                    AnalyzingResponse response = sender.send(request);
                    if (response != null) {
                        System.out.println(
                                "Response ID: " + response.getRequest().getId()
                                +". Score: " + response.getResult().getScore()
                                + ". Execution time: " + (System.currentTimeMillis() - start) + "ms");
                    } else {
                        System.out.println(
                                "Response ID: " + request.getId()
                                +
                                ". No response found. Execution time: " + (System.currentTimeMillis() - start) + "ms");
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }


    }
}
