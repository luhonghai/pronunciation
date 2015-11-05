package com.cmg.android.bbcaccent.dictionary;

import com.cmg.android.bbcaccent.data.dto.lesson.word.WordCollection;
import com.cmg.android.bbcaccent.data.sqlite.lesson.LessonDBAdapterService;
import com.cmg.android.bbcaccent.utils.SimpleAppLog;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * Created by luhonghai on 27/10/2015.
 */
public class DatabaseDictionaryWalker extends DictionaryWalker {

    protected DatabaseDictionaryWalker(File targetDir) {
        super(targetDir);
    }

    protected DatabaseDictionaryWalker(File targetDir, boolean fetchAudio) {
        super(targetDir, fetchAudio);
    }

    @Override
    public void execute(String word) {
        DictionaryItem dictionaryItem = new DictionaryItem();
        dictionaryItem.setWord(word);
        try {
            WordCollection wordCollection = LessonDBAdapterService.getInstance().findObject("word = ?", new String[]{word}, WordCollection.class);
            if (wordCollection != null) {
                SimpleAppLog.logJson(wordCollection);
                dictionaryItem.setWordId(wordCollection.getId());
                dictionaryItem.setPronunciation(wordCollection.getPronunciation());
                dictionaryItem.setDefinition(wordCollection.getDefinition());
                if (isFetchAudio() && wordCollection.getMp3Path() != null && wordCollection.getMp3Path().length() > 0) {
                    dictionaryItem.setAudioUrl(wordCollection.getMp3Path());
                    if (!getTargetDir().exists() || !getTargetDir().isDirectory()) {
                        getTargetDir().mkdirs();
                    }
                    File saveFile = new File(getTargetDir(), word + ".mp3");
                    if (saveFile.exists()) {
                    } else {
                        FileUtils.copyURLToFile(new URL(dictionaryItem.getAudioUrl()), saveFile);

                    }
                    dictionaryItem.setAudioFile(saveFile.getAbsolutePath());
                }
                onDetectWord(dictionaryItem);
            } else {
                onWordNotFound(dictionaryItem, new FileNotFoundException("Could not found word " + word + " on lesson database"));
            }
        } catch (Exception e) {
            onError(dictionaryItem, "Could not get word from database",e);
        }
    }
}
