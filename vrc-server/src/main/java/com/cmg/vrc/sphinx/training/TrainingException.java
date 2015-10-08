package com.cmg.vrc.sphinx.training;

/**
 * Created by cmg on 05/10/2015.
 */
public class TrainingException extends Exception {
    public TrainingException(String message) {
        super(message);
    }

    public TrainingException(String message, Throwable e) {
        super(message, e);
    }
}
