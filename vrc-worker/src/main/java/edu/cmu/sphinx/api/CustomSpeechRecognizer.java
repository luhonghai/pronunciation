package edu.cmu.sphinx.api;

import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.util.TimeFrame;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luhonghai on 10/15/14.
 */
public class CustomSpeechRecognizer extends AbstractSpeechRecognizer {
    /**
     * Constructs new stream recognizer.
     *
     * @param configuration configuration
     */
    public CustomSpeechRecognizer(Configuration configuration)
            throws IOException
    {
        super(configuration);
    }

    public void startRecognition(InputStream stream) {
        startRecognition(stream, TimeFrame.INFINITE);
    }

    /**
     * Starts recognition process.
     *
     * Starts recognition process and optionally clears previous data.
     *
     * @param clear clear cached microphone data
     * @see         StreamSpeechRecognizer#stopRecognition()
     */
    public void startRecognition(InputStream stream, TimeFrame timeFrame) {
        if (recognizer.getState() == Recognizer.State.DEALLOCATED) {
            recognizer.allocate();
        }
        context.setSpeechSource(stream, timeFrame);
    }

    public void allocate() {
        recognizer.allocate();
    }

    /**
     * Stops recognition process.
     *
     * Recognition process is paused until the next call to startRecognition.
     *
     * @see StreamSpeechRecognizer#startRecognition(boolean)
     */
    public void stopRecognition() {
        recognizer.deallocate();
    }
}
