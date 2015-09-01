package com.cmg.vrc.sphinx;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cmg on 26/08/15.
 */
public class WordListGenerator {

    private Map<String, String> dictionary = new HashMap<String, String>();

    public void load(File input) throws IOException {
        dictionary.clear();
        Document document = Jsoup.parse(input, "UTF-8");
        Elements elements = document.select("item");
        if (elements != null && elements.size() > 0) {
            int total = elements.size();
            for (int i = 0; i < elements.size(); i++) {
                Element element = elements.get(i);
                String text = element.text().trim();
                String[] raw = text.split("\\|");
                if (raw.length > 0) {
                    String word = raw[0];
                    if (!dictionary.containsKey(word.toLowerCase())) {
                        System.out.println("Found word: " + word);
                        dictionary.put(word.toLowerCase(), word);
                    }
                }
            }
            System.out.println("Total: " + total);
        } else {
            System.out.println("No item found");
        }
    }

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
                        List<String> phonemes = dictionaryHelper.getCorrectPhonemes(text.toLowerCase());
                        boolean isBeep = phonemes != null && phonemes.size() > 0;
                        boolean isExist = dictionary.containsKey(text.toLowerCase());
                        if (isBeep && isExist) {
                            System.out.println("Word: " + text + " is correct");
                        } else {
                            System.out.println("ERROR! Word: " + text + " is NOT BEEP or Oxford");
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

    public static void main(String[] args) throws IOException {
        WordListGenerator generator = new WordListGenerator();
        generator.load(new File("/Users/cmg/git/pronunciation/voice-recorder-app/src/main/res/values/words.xml"));
        System.out.println("#################### VI ####################");
        generator.generate(new File("/Users/cmg/git/pronunciation/voice-recorder-app/src/main/res/values-vi/random_words.xml"),
                null, true);
        System.out.println("#################### TH ####################");
        generator.generate(new File("/Users/cmg/git/pronunciation/voice-recorder-app/src/main/res/values-th/random_words.xml"),
                null, true);
        System.out.println("#################### GB ####################");
        generator.generate(new File("/Users/cmg/git/pronunciation/voice-recorder-app/src/main/res/values/random_words.xml"),
                null, true);
    }
}
