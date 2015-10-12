package com.cmg.vrc.worker;

import com.cmg.vrc.worker.sphinx.SphinxResult;

import java.io.Serializable;

/**
 * Created by cmg on 17/09/15.
 */
public class AnalyzingResponse implements Serializable {

    private static final long serialVersionUID = -8819364295899294659L;

    private AnalyzingRequest request;

    private boolean status;

    private String message;

    private SphinxResult result;

    private long startTime;

    private long executionTime;

    public AnalyzingRequest getRequest() {
        return request;
    }

    public void setRequest(AnalyzingRequest request) {
        this.request = request;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public SphinxResult getResult() {
        return result;
    }

    public void setResult(SphinxResult result) {
        this.result = result;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }
}
