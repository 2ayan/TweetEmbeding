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
    
    HashMap<String, WordVec> Word2Vec_hashmap;
    HashMap<String, WordVec> Doc2Vec_hashmap;
    VectorizeDocs() throws IOException
    {
        prop=(new GetProjetBaseDirAndSetProjectPropFile()).prop;
        
        VecFile=prop.getProperty("VecFile");
        DocFile=prop.getProperty("DocFile");
        OutputFile=prop.getProperty("OutputFile");
        
        HashMap<String, WordVec> WordVec_hashmap = new HashMap<>();
        HashMap<String, DocVec> Doc2Vec_hashmap = new HashMap<>();
    }
    
    static String getAvgVec(HashMap<String, WordVec> WordVec_hashmap, String line) throws Exception  {
        
        String[] tokens = line.split("\\s+");
        String docName = tokens[0];
        

        float[] dvec = null;

        for (int i=1; i < tokens.length; i++) {
            String w = tokens[i];
            WordVec wv = WordVec_hashmap.get(w);
            if (wv == null)
                continue;
            else {
                if (dvec == null)
                    {
                    dvec = new float[wv.vec.length];
                    for(int j=0;j<wv.vec.length;j++)
                           dvec[j]=wv.vec[j];
                    }

                for (int j=0; j<wv.vec.length; j++) {
                    dvec[j] += wv.vec[j];
                }
            }
        }

        if (dvec == null)
            return null;

        for (int j=0; j<dvec.length; j++) {
            dvec[j] = dvec[j]/(float)(tokens.length-1+1);  // normalize by #words in doc
            
        }
        String docvecstring=docName;
        for(int i=0;i<dvec.length;i++)
            docvecstring.concat(" "+dvec[i]);
        
        DocVec dv=new DocVec(docvecstring);
        
        //Doc2Vec_hashmap.put(dv.Docid, dv.vec);
        //return dv;
        return docvecstring;
    }

  
    public void load_inputs_and_compute_avg() throws FileNotFoundException, IOException, Exception
    {

        FileReader fr = new FileReader(VecFile);
        BufferedReader br = new BufferedReader(fr);
        String line;

        while ((line = br.readLine()) != null) {
            WordVec wv = new WordVec(line);
            Word2Vec_hashmap.put(wv.word, wv);
        }

        br.close();
        fr.close();
        System.out.println("Loaded " + Word2Vec_hashmap.size() + " words in hashmap...");

       
        fr = new FileReader(DocFile);
        br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(prop.getProperty("Doc2VecFile"));   
       
        while ((line = br.readLine()) != null) {
            fw.append(getAvgVec(Word2Vec_hashmap, line)+"\n");
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
        
        VectorizeDocs VD = new VectorizeDocs();
        VD.load_inputs_and_compute_avg();
    }
    
    
}
