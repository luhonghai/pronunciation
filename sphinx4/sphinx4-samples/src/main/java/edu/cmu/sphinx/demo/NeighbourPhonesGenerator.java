package edu.cmu.sphinx.demo;

import com.sun.deploy.util.StringUtils;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luhonghai on 11/13/14.
 */
public class NeighbourPhonesGenerator {

    private Map<String, List<String>> neighbourPhones;
    private List<String> phomenes;
    private List<String> dictDump;
    private List<String> allDictDump;
    private  List<String> words;
    private String mdefSource;
    public void load() {
        try {
            mdefSource = FileUtils.readFileToString(new File("/Volumes/DATA/Development/Sphinx/wsj-en-us/mdef"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClassLoader loader = NeighbourPhonesGenerator.class.getClassLoader();
        try {
            words = FileUtils.readLines(new File(loader.getResource("edu/cmu/sphinx/demo/words.txt").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            phomenes = FileUtils.readLines(new File(loader.getResource("edu/cmu/sphinx/demo/phonemes.txt").getFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is =loader.getResourceAsStream("edu/cmu/sphinx/demo/cmubet_neighbour_phones.txt");
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            neighbourPhones = new HashMap<String, List<String>>();
            String line;
            while ((line = br.readLine()) != null) {
                String[] raw = line.split("neighbors:");
                String phones = raw[0].trim();
                System.out.println("=======");
                System.out.println("Found phones " + phones);
                List<String> neighbours = null;
                if (neighbourPhones.containsKey(phones)) {
                    neighbours = neighbourPhones.get(phones);
                    neighbourPhones.remove(phones);
                }
                if (neighbours == null)
                    neighbours  = new ArrayList<String>();

                String[] rawNeighbours = raw[1].trim().split("\\|");
                if(rawNeighbours.length > 0) {
                    for (String nb : rawNeighbours) {
                        nb = nb.trim();
                        if (!neighbours.contains(nb) && !nb.equals(phones) && (phomenes.contains(nb) || nb.equalsIgnoreCase("sil"))) {
                            System.out.println("Found neighbour " + nb);
                            neighbours.add(nb);
                        }
                    }
                }
                neighbourPhones.put(phones, neighbours);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                }catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void completeSample(String[] raw) {

        String line = "";
        for (String r : raw) {
            line += r + " ";
        }
        line = line.trim();
        /**
         *  Test Phones is exist in mdef data
         */
        boolean test = true;
//        for (int i = 1; i < raw.length;i++) {
//            String testStr="";
//            if (i == 1 ) {
//                 testStr = fixPhoneText(raw[i]) + "  " + fixPhoneText(raw[i+1]);
//            } else if (i == raw.length - 1) {
//                testStr = fixPhoneText(raw[i-1]) + "  " + fixPhoneText(raw[i]);
//            } else {
//                testStr = fixPhoneText(raw[i-1]) +"  "+ fixPhoneText(raw[i]) + "  " + fixPhoneText(raw[i+1]);
//            }
//            testStr = testStr.trim();
//            if (testStr.length() != 0) {
//                test = mdefSource.contains(testStr);
//            }
//        }
        if (test) {
            if (!dictDump.contains(line)) {
                dictDump.add(line);
            }
        } else {
            System.out.println("Skip wrong neihbour phones " + line);
        }
    }

    private String fixPhoneText(String p) {
        if (p.length() == 1) {
            return " " + p;
        }
        return p;
    }


    private void calPhones(String[] raw, int pos, boolean isNb) {
        if (pos >= raw.length) return;
        String p = raw[pos].trim();
        if (!isNb) {
            List<String> nbs = neighbourPhones.get(p);
            if (nbs != null && nbs.size() > 0) {
                for (String nb : nbs) {
                    String[] tmp = raw.clone();
                    tmp[pos] = nb;
                    calPhones(tmp, pos, true);
                }
            }
        } else {
            completeSample(raw);
            calPhones(raw, 1 + pos, false);
        }

    }

    public void generate() {
        load();
        allDictDump = new ArrayList<>();
        for (String w: words) {
            if (!w.startsWith("#")) {
                String rawDict = w;
                String[] raws = rawDict.trim().split(" ");
                String word = raws[0].trim().toLowerCase();
                if (word.equalsIgnoreCase(raws[0])) {
                    dictDump = new ArrayList<>();
                    System.out.println("Generate dictionary for word " + word);
                    // Store correct phonemes first
                    dictDump.add(rawDict);
                    calPhones(raws, 1, false);
                    System.out.println("Found " + dictDump.size() + " neighbour phonemes for word " + word);
                    try {
                        FileUtils.writeLines(new File("/Volumes/DATA/Development/Sphinx/dict/" + word), dictDump);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    allDictDump.addAll(dictDump);
                } else {
                    System.out.println("Not matching");
                }
            } else {
                System.out.println("Skip word " + w);
            }
        }
        try {
            FileUtils.writeLines(new File("/Volumes/DATA/Development/Sphinx/dict/cmudict_words"), allDictDump);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        NeighbourPhonesGenerator npg = new NeighbourPhonesGenerator();
        npg.generate();

    }
}
