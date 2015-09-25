package com.cmg.vrc.service;

import com.cmg.vrc.common.Constant;
import com.cmg.vrc.data.UserProfile;
import com.cmg.vrc.data.dao.impl.RecorderDAO;
import com.cmg.vrc.data.dao.impl.RecordedSentenceHistoryDAO;
import com.cmg.vrc.data.jdo.RecordedSentence;
import com.cmg.vrc.data.jdo.RecordedSentenceHistory;
import com.cmg.vrc.processor.CustomFFMPEGLocator;
import com.cmg.vrc.properties.Configuration;
import com.cmg.vrc.util.AWSHelper;
import com.cmg.vrc.util.FileHelper;
import com.cmg.vrc.util.UUIDGenerator;
import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncodingAttributes;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by CMGT400 on 8/13/2015.
 */
public class RecorderSentenceService {
    private static int STATUS_NOT_RECORD = 0;
    public static String RETURN_SUCCESS = "success";
    public static String RETURN_ERROR="error";

    private final AWSHelper awsHelper;

    public RecorderSentenceService() {
        awsHelper = new AWSHelper();
    }
    public int version(){
        RecorderDAO recorderDAO=new RecorderDAO();
        try {
            int version = recorderDAO.getLatestVersion() + 1;
            return version;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * function admin update
     * @param idSentence
     * @param isDeleted
     * @return
     */
    public String adminUpdate(String idSentence, int isDeleted){
        int version=version();
        String result = RETURN_ERROR;
        RecorderDAO recorderDAO=new RecorderDAO();
        boolean condition = recorderDAO.adminUpdate(idSentence,isDeleted,STATUS_NOT_RECORD,version);
        if(condition){
            result = RETURN_SUCCESS;
        }
        return result;
    }

    /**
     * list sentence
     * @param version version max client
     * @param username account recorder
     * @return
     */
    public List<RecordedSentence> getListByVersionAndUsername(int version,String username){
        RecorderDAO recorderDAO = new RecorderDAO();
        List<RecordedSentence> list= new ArrayList<RecordedSentence>();
        try {
            list= recorderDAO.getListByVersion(version,username);
            System.out.println("start dao get list : " + list.size());
            for(RecordedSentence rc : list){
                System.out.println("version : " + rc.getVersion());
                System.out.println("status : " + rc.getStatus());

            }
            System.out.println("start dao get list : " + list.size());
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    /**
     * client upload file
     * @param user
     * @param sentenceId
     * @param recordedVoice
     * @return
     * @throws Exception
     */
    public String clientUpdate(UserProfile user, String sentenceId, File recordedVoice, int clientVersion) throws Exception {
        String uuid=null;
        RecorderDAO recorderDAO=new RecorderDAO();
        RecordedSentenceHistoryDAO recordedSentenceHistoryDAO = new RecordedSentenceHistoryDAO();
        try {
            RecordedSentence recordedSentence = recorderDAO.getBySentenceIdAndAccount(sentenceId, user.getUsername());

            if (recordedSentence == null) {
                recordedSentence = new RecordedSentence();
                uuid = UUIDGenerator.generateUUID();
            }
            uuid=recordedSentence.getId();
            Date now = new Date(System.currentTimeMillis());
            int lastStatus = recordedSentence.getStatus();
            recordedSentence.setId(uuid);
            recordedSentence.setStatus(RecordedSentence.PENDING);
            recordedSentence.setAccount(user.getUsername());
            recordedSentence.setAdmin("System");
            recordedSentence.setVersion(clientVersion);
            if (recordedSentence.getCreatedDate() == null)
                recordedSentence.setCreatedDate(now);
            recordedSentence.setModifiedDate(now);
            recordedSentence.setFileName(recordedVoice.getName());
            recordedSentence.setSentenceId(sentenceId);
            awsHelper.upload(Constant.FOLDER_REOCORDED_VOICES_AMT + "/" + user.getUsername() + "/" + recordedSentence.getFileName(), recordedVoice);

            File mp3Audio = new File(FileHelper.getTmpSphinx4DataDir(), user.getUsername() + File.separator + recordedSentence.getFileName() + ".mp3");
            if (!mp3Audio.exists()) {
                AudioAttributes audio = new AudioAttributes();
                audio.setCodec("libmp3lame");
                audio.setBitRate(new Integer(128000));
                audio.setChannels(new Integer(2));
                audio.setSamplingRate(new Integer(44100));
                EncodingAttributes attrs = new EncodingAttributes();
                attrs.setFormat("mp3");
                attrs.setAudioAttributes(audio);
                String env = Configuration.getValue(Configuration.SYSTEM_ENVIRONMENT);
                Encoder encoder;
                if (env.equalsIgnoreCase("prod") || env.equalsIgnoreCase("sat")
                        || env.equalsIgnoreCase("int")
                        || env.equalsIgnoreCase("aws")) {
                    encoder = new Encoder(new CustomFFMPEGLocator());
                } else {
                   encoder =new Encoder();
                }
                encoder.encode(recordedVoice, mp3Audio, attrs);
                awsHelper.upload(Constant.FOLDER_REOCORDED_VOICES_AMT + "/" + user.getUsername() + "/" + recordedSentence.getFileName() + ".mp3", mp3Audio);
                if (mp3Audio.exists()) {
                    FileUtils.forceDelete(mp3Audio);
                }
            }

            if (recordedVoice.exists()) {
                FileUtils.forceDelete(recordedVoice);
            }
            if (recorderDAO.put(recordedSentence)) {
                RecordedSentenceHistory recordedSentenceHistory = new RecordedSentenceHistory();
                recordedSentenceHistory.setActor(user.getUsername());
                recordedSentenceHistory.setActorType(RecordedSentenceHistory.ACTOR_TYPE_USER);
                recordedSentenceHistory.setPreviousStatus(lastStatus);
                recordedSentenceHistory.setNewStatus(recordedSentence.getStatus());
                recordedSentenceHistory.setMessage("Uploaded by user " + user.getUsername());
                recordedSentenceHistory.setTimestamp(now);
                recordedSentenceHistory.setRecordedSentenceId(recordedSentence.getId());
                if (recordedSentenceHistoryDAO.put(recordedSentenceHistory)) {
                    return RETURN_SUCCESS;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return RETURN_ERROR;
    }

    /**
     * save file recorder
     * @param user
     * @param temp
     * @param targetDir
     * @param parameter
     * @return
     */
    public File saveRecorderFile(UserProfile user, File temp, String targetDir, String parameter){

        File target = new File(targetDir, user.getUsername());
        if (!target.exists() && !target.isDirectory()) {
            target.mkdirs();
        }
        String uuid = UUIDGenerator.generateUUID();
        String fileTempName  = parameter + "_" + uuid + "_raw" + ".wav";
        File targetRaw = new File(target, fileTempName);
        try {
            FileUtils.moveFile(temp, targetRaw);
            if (temp.exists())
                FileUtils.forceDelete(temp);
            return targetRaw;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * check exist sentence with user
     * @param user
     * @param sentenceId
     * @return
     */
    public  boolean checkIsExist(UserProfile user, String sentenceId){
        boolean result=false;
        RecorderDAO recorderDAO=new RecorderDAO();
        RecordedSentence recordedSentence = null;
        try {
            recordedSentence = recorderDAO.getBySentenceIdAndAccount(sentenceId, user.getUsername());
            if (recordedSentence != null) {
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
