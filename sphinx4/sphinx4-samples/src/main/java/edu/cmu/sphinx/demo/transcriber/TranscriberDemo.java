/*
 * Copyright 1999-2013 Carnegie Mellon University.
 * Portions Copyright 2004 Sun Microsystems, Inc.
 * Portions Copyright 2004 Mitsubishi Electric Research Laboratories.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package edu.cmu.sphinx.demo.transcriber;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.api.StreamSpeechRecognizer;
import edu.cmu.sphinx.decoder.adaptation.Stats;
import edu.cmu.sphinx.decoder.adaptation.Transform;
import edu.cmu.sphinx.linguist.acoustic.Unit;
import edu.cmu.sphinx.linguist.dictionary.Pronunciation;
import edu.cmu.sphinx.result.Node;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.result.WordResult;


/**
 * A simple example that shows how to transcribe a continuous audio file that
 * has multiple utterances in it.
 */
public class TranscriberDemo {

    public static void main(String[] args) throws Exception {
        System.out.println("Loading models...");
        long start = System.currentTimeMillis();
        Configuration configuration = new Configuration();
        configuration.setUseGrammar(true);
        configuration.setGrammarName("variable");
        configuration.setGrammarPath("/Volumes/DATA/Development/Sphinx/grammar");
        configuration.setAcousticModelPath("file:///Volumes/DATA/Development/Sphinx/wsj-en-us");
        configuration.setDictionaryPath("file:///Volumes/DATA/Development/Sphinx/dict/cmuphonedict.dict");

        //configuration.setDictionaryPath("file:///Volumes/DATA/Development/Sphinx/dict/cmudict.0.7a_SPHINX_40");
        // configuration.setDictionaryPath("file:///Volumes/DATA/Development/Sphinx/dict/variable");
        //configuration.setDictionaryPath("file:///Volumes/DATA/Development/Sphinx/dict/cmudict_words");
        //configuration.setLanguageModelPath("file:///Volumes/DATA/Development/Sphinx/lm/all_words.lm");
       // configuration.setLanguageModelPath("file:///Volumes/DATA/Development/Sphinx/en-70k_1n.lm");
        //configuration.setLanguageModelPath("file:///Volumes/DATA/Development/Sphinx/en-phone.lm.dmp");

        StreamSpeechRecognizer recognizer = 
            new StreamSpeechRecognizer(configuration);

        String fVariable = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/variable_0ec2d743-2d6b-4723-9762-fffb8f41df06_raw.wav";
        String fSeashell = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/seashell_26207c0c-4810-4ce4-9feb-ffcc37ad92b7_raw.wav";
        String fQuarter = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/quarter_a4b3dffa-6295-4614-bb19-cfd668cec6f5_raw.wav";
        String fParticularly = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/particularly_40bbdad9-b5df-4fdc-aad2-c4a574b48288_raw.wav";
        String fNecessarily = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/necessarily_a5774a26-122a-4d33-b7bf-4111416e59d6_raw.wav";
        String fHaiNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-hai.wav";
        String fHaiSeashell = "/Volumes/DATA/Development/voice-sample/seashell-hai.wav";
        String fHaiVariable = "/Volumes/DATA/Development/voice-sample/variable-hai.wav";
        String fLanNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-lan.wav";
        String fLan2Necessarily = "/Volumes/DATA/Development/voice-sample/necessarily-lan-2.wav";
        String fAnhNecessarily = "/Volumes/DATA/Development/voice-sample/necessarily-anh.wav";
        String fAnhSeashell = "/Volumes/DATA/Development/voice-sample/seashell-anh.wav";
        String fAnh2Seashell = "/Volumes/DATA/Development/voice-sample/seashell-anh-2.wav";
        String fAnhVariable = "/Volumes/DATA/Development/voice-sample/variable-anh.wav";
        String fAnhVariableCleanedNoised = "/Volumes/DATA/Development/voice-sample/variable-anh-c-n.wav";
        String fAnh2Variable = "/Volumes/DATA/Development/voice-sample/variable-anh-2.wav";
        String fBarter = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/barter_a1f7b6cd-4969-45df-bcd0-cea2d76d8542_raw.wav";
        String fBorrower = "/Volumes/DATA/Development/voice-sample/dominic_1_1_2 2/borrower_8c830b69-4068-45b2-b549-fefed76a2726_raw.wav";

        InputStream stream = new FileInputStream(fVariable);
        
        // Simple recognition with generic model
        recognizer.startRecognition(stream);
        SpeechResult result;
        Result rs;
        while ((result = recognizer.getResult()) != null) {
            rs = result.getResult();
            System.out.format("Hypothesis: %s\n",
                              result.getHypothesis());
                              
            //System.out.println("List of recognized words and their times:");

            for (WordResult r : result.getWords()) {
        	    //System.out.println(r);
                Pronunciation[] prons = r.getWord().getPronunciations();
                //System.out.println("=========");
                for (Pronunciation rp : prons) {
                  //  System.out.println("-----");
                    Unit[] units = rp.getUnits();
                    String pron = "";
                    if (units != null && units.length > 0) {
                        for (Unit u : units) {
                            pron += " " + u.getName();
                        }
                    }
                   // System.out.println(pron);
                }
            }

            System.out.println("Best 5 hypothesis:");
            for (String s : result.getNbest(5))
                System.out.println("- " + s);
            Collection<Node> nodes = result.getLattice().getNodes();
            int nSize = result.getLattice().getNodes().size();
            System.out.println("Lattice contains " +nSize + " nodes");
            Iterator<Node> iterator = nodes.iterator();
            int count = 1;
            while (iterator.hasNext()) {
                Node n = iterator.next();
                System.out.println("#" + count + " | " + n.getId() + " | " + n.getBeginTime() + " | " + n.getEndTime() + " | " + n.getBackwardScore() + " | " + n.getPosterior());
                count++;
            }
            //System.out.println("Raw pron: " + pron);
            //System.out.println(rs.getBestResultNoFiller());
            System.out.println("Best pron: " + rs.getBestPronunciationResult());
        }
        recognizer.stopRecognition();
        long end = System.currentTimeMillis();
        System.out.println("Execution time: " + (end - start) + "ms.");
        
    }
}
