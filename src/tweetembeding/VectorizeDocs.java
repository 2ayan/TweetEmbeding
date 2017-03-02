/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 *
 * @author ayan
 */


public class VectorizeDocs {
    public Properties prop;
    String VecFile, DocFile, OutputFile;
    VectorizeDocs() throws IOException
    {
        prop=(new GetProjetBaseDirAndSetProjectPropFile()).prop;
        
        VecFile=prop.getProperty("VecFile");
        DocFile=prop.getProperty("DocFile");
        OutputFile=prop.getProperty("OutputFile");
    }
    
    static String getAvgVec(HashMap<String, WordVec> map, String line) throws Exception  {
        StringBuffer buff = new StringBuffer();
        String[] tokens = line.split("\\s+");
        String docName = tokens[0];
        buff.append(docName).append("\t");

        float[] dvec = null;

        for (int i=1; i < tokens.length; i++) {
            String w = tokens[i];
            WordVec wv = map.get(w);
            if (wv == null)
                continue;
            else {
                if (dvec == null)
                    dvec = new float[wv.vec.length];

                for (int j=0; j<wv.vec.length; j++) {
                    dvec[j] += wv.vec[j];
                }
            }
        }

        if (dvec == null)
            return null;

        for (int j=0; j<dvec.length; j++) {
            dvec[j] = dvec[j]/(float)(tokens.length-1);  // normalize by #words in doc
            buff.append(dvec[j]).append(" ");
        }
        return buff.toString();

    }

  
    public void load_inputs_and_compute_avg() throws FileNotFoundException, IOException, Exception
    {
        HashMap<String, WordVec> map = new HashMap<>();
        FileReader fr = new FileReader(VecFile);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            WordVec wv = new WordVec(line);
            map.put(wv.word, wv);
        }

        br.close();
        fr.close();
        System.out.println("Loaded " + map.size() + " words in hashmap...");

       
        fr = new FileReader(DocFile);
        br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(OutputFile);   
       
        while ((line = br.readLine()) != null) {
            String dvec = getAvgVec(map, line);
            if (dvec!=null) {
                fw.write(dvec + "\n");
            }
        }

        fw.close();
        br.close();
        fr.close();
    }
    
      public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            System.err.println("usage: java VectorizeDocs <tweets.vec file> <doc file> <o/p file>");
            return;
        }
        
        new VectorizeDocs().load_inputs_and_compute_avg();
    }
    
    
}
