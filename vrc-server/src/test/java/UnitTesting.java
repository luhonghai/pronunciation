import com.cmg.vrc.service.amt.TranscriptionService;

/**
 * Created by cmg on 08/07/15.
 */
public class UnitTesting {

    public static void main(String[] args) {
        TranscriptionService transcriptionService = new TranscriptionService();
        try {
            transcriptionService.loadTranscription();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
