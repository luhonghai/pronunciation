package com.cmg.vrc.dictionary;

import com.cmg.vrc.dictionary.walker.DictionaryWalker;
import com.cmg.vrc.dictionary.walker.OxfordDictionaryWalker;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luhonghai on 12/23/14.
 */
public class DictionaryAliveWord {
    public static void main(String[] args) throws IOException {
        List<String> words = FileUtils.readLines(new File("/Volumes/DATA/Development/test-zone/words.txt"), "UTF-8");
        DictionaryWalker walker = new OxfordDictionaryWalker(new File("/Volumes/DATA/CMG/git/pronunciation/sphinx-data/"));
        final List<String> correctWords = new ArrayList<String>();
        correctWords.add("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        correctWords.add("<resources>");
        correctWords.add("\t<string-array name=\"words_list\">");
        for (String word : words) {
            word = word.replace("\t", " ");
            word = word.replace("\n", " ");
            while (word.contains("  ")) {
                word = word.replace("  ", " ");
            }
            word = word.trim();
            String[] tmp = word.split(" ");
            String pron = "";
            String w = tmp[0].trim();
            if (!w.contains("'")) {
                if (tmp.length > 1) {
                    String t = tmp[1];
                    t = t.replace("ˈ", "\\'");
                    if (t.contains(",")) {
                        pron = t.split(",")[0].trim();
                    } else {
                        pron = t;
                    }
                }
                correctWords.add("\t\t<item>" + w + "|" + pron + "</item>");
            }
        }

        correctWords.add("\t</string-array>");
        correctWords.add("</resources>");
        FileUtils.writeLines(new File("/Volumes/DATA/CMG/git/pronunciation/voice-recorder-app/src/main/res/values/words.xml"), correctWords);
    }
}
