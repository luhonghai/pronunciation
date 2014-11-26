package edu.cmu.sphinx.demo;

import edu.cmu.sphinx.linguist.g2p.G2PConverter;
import edu.cmu.sphinx.linguist.g2p.Path;

import java.util.ArrayList;

/**
 * Created by luhonghai on 11/25/14.
 */
public class G2PConverterTest {
    public static void main(String[] args) {
        G2PConverter converter = new G2PConverter("/Volumes/DATA/Development/Sphinx/en_us_nostress/model.fst.ser");
        ArrayList<Path> list = converter.phoneticize("good", 1);
        for (Path p : list) {
            System.out.println("========");
            for (String _p : p.getPath()) {
                System.out.println(_p);
            }

        }
    }
}
