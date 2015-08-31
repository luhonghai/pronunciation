package com.cmg.vrc.sphinx;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.List;

/**
 * Created by cmg on 26/08/15.
 */
public class WordListGenerator {

    public void generate(File input, File output, boolean doValidate) {
        try {
            DictionaryHelper dictionaryHelper = new DictionaryHelper(DictionaryHelper.Type.BEEP);
            Document document = Jsoup.parse(input, "UTF-8");
            Elements elements = document.select("item");
            if (elements != null && elements.size() > 0) {
                int total = elements.size();
                int notfound = 0;
                int found = 0;
                for (int i = 0; i < elements.size(); i++) {
                    Element element = elements.get(i);
                    String text = element.text().trim();
                    //System.out.println("Found text: " + text);
                    if (doValidate) {
                        List<String> phonemes = dictionaryHelper.getCorrectPhonemes(text);
                        boolean isBeep = phonemes != null && phonemes.size() > 0;
                        if (isBeep) {
                            System.out.println("Word: " + text + " is BEEP");
                        } else {
                            System.out.println("Word: " + text + " is NOT BEEP");
                        }
                    } else {
                        String[] raw = text.split("\\|");
                        if (raw.length > 0) {
                            String word = raw[0].trim();
                            List<String> phonemes = dictionaryHelper.getCorrectPhonemes(word);
                            boolean isBeep = phonemes != null && phonemes.size() > 0;
                            element.text(text + "|" + isBeep);
                            if (isBeep) {
                                found++;
                            } else {
                                notfound++;
                            }
                        } else {
                            total--;
                            element.remove();
                            System.out.println("Error text: " + text);
                        }
                    }
                }
                System.out.println("Total: " + total + ". Not in beep: " + notfound +". Correct word: " + found);
            } else {
                System.out.println("No item found");
            }
            if (!doValidate)
                FileUtils.writeStringToFile(output, document.select("resources").outerHtml(), "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WordListGenerator generator = new WordListGenerator();
        generator.generate(new File("/Users/cmg/git/pronunciation/voice-recorder-app/src/main/res/values-vi/random_words.xml"),
                null, true);
    }
}
