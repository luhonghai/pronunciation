package com.cmg.vrc.dictionary;

import java.io.FileNotFoundException;

/**
 * Created by luhonghai on 9/19/14.
 */
public interface DictionaryListener {

    public void onDetectWord(DictionaryItem item);

    public void onWordNotFound(DictionaryItem item, FileNotFoundException ex);

    public void onError(DictionaryItem item, Exception ex);

}
