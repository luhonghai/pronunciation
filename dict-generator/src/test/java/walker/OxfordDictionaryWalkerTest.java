package walker;

import com.cmg.vrc.dictionary.DictionaryFetcher;
import com.cmg.vrc.dictionary.Runner;
import com.cmg.vrc.dictionary.walker.DictionaryWalker;
import com.cmg.vrc.dictionary.walker.OxfordDictionaryWalker;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.Before; 
import org.junit.After;

import java.io.File;
import java.io.IOException;

/** 
* OxfordDictionaryWalker Tester. 
* 
* @author <Authors name> 
* @since <pre>Sep 19, 2014</pre> 
* @version 1.0 
*/ 
public class OxfordDictionaryWalkerTest {

@Before
public void before() throws Exception { 
}
@After
public void after() throws Exception { 
} 

/** 
* 
* Method: execute(String word) 
* 
*/ 
@Test
public void testExecute() throws Exception {
    String configFile = "/Volumes/DATA/Development/dict-generator/example.json";
    File cFile = new File(configFile);
    if (cFile.exists() && !cFile.isDirectory()) {
        try {
            Gson gson = new Gson();
            String configSource = FileUtils.readFileToString(cFile);
            Runner.Configuration configuration = gson.fromJson(configSource, Runner.Configuration.class);
            DictionaryFetcher fetcher = new DictionaryFetcher(configuration);
            if (fetcher.validate()) {
                fetcher.execute();
            } else  {
                System.out.println("Invalid configuration");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 


} 
