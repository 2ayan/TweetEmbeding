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
    HashMap<String, DocVec> Doc2Vec_hashmap;
    ReadIndex r;
    
    public  HashMap<String, DocVec> Query;
    public  HashMap<String, DocVec> Documents;
    
    public  readRESfile rr;
    public readVectors rv;        
    
    VectorizeDocs() throws IOException
    {
        prop=(new GetProjetBaseDirAndSetProjectPropFile()).prop;
        
        VecFile=prop.getProperty("VecFile");
        
        OutputFile=prop.getProperty("OutputFile");
        
        Word2Vec_hashmap = new HashMap<>();
        Doc2Vec_hashmap = new HashMap<>();
        r=new ReadIndex();
        r.loadIndex();
        r.Create_tweettime_docid_map();
    }
    
    static String getAvgVec(HashMap<String, WordVec> WordVec_hashmap, String line) throws Exception  {
        
        String[] tokens = line.split("\\s+");
        String docName = tokens[0];
        

        double[] dvec = null;
        
        int missing_terms_no=0;
        
        for (int i=1; i < tokens.length; i++) {
            String w = tokens[i];
            WordVec wv = WordVec_hashmap.get(w);
           
            if (wv == null){
                missing_terms_no++;
                continue;
                }
            else {
                if (dvec == null)
                    {
                    dvec = new double[wv.vec.length];
                    for(int j=0;j<wv.vec.length;j++)
                           dvec[j]=wv.vec[j];
                    }

                for (int j=0; j<wv.vec.length; j++) {
                    dvec[j] += wv.vec[j];
                }
            }
        }
        System.err.println("Number of Missing terms:"+missing_terms_no);
        if (dvec == null)
            return null;

       // for (int j=0; j<dvec.length; j++) 
         //   dvec[j] = dvec[j]/(float)(tokens.length-1+1);  // normalize by #words in doc    
        
        String docvecstring=docName;
        for(int i=0;i<dvec.length;i++){
            docvecstring=docvecstring+" "+Double.toString(dvec[i]);
        }
        DocVec dv=new DocVec(docvecstring);
        
        return docvecstring;
    }

  
    public void load_inputs_and_compute_avg() throws FileNotFoundException, IOException, Exception
    {

        FileReader fr = new FileReader(VecFile);
        BufferedReader br = new BufferedReader(fr);
        String line;

        int c=0;
        while ((line = br.readLine()) != null) {
           if(c==0)
            if(line.split("\\s+").length<3)
                continue;
            WordVec wv = new WordVec(line);
            Word2Vec_hashmap.putIfAbsent(wv.word.toLowerCase(), wv);
           
            c++;
        }

        br.close();
        fr.close();
        System.out.println("Loaded " + Word2Vec_hashmap.size() + " words in hashmap...");

        System.out.println("Starting to create "+prop.getProperty("Doc2VecFile"));
        //compute_and_write_dvec("DocFile", "Doc2VecFile", Word2Vec_hashmap);
        read_result_file_and_compute_dvec("TRECresultFile", "Doc2VecFile", Word2Vec_hashmap);
        System.out.println("Starting to create "+prop.getProperty("Query2VecFile"));
        compute_and_write_dvec("QueryFile", "Query2VecFile", Word2Vec_hashmap);
        
        
    }
    void read_result_file_and_compute_dvec(String resfile, String dvecFile, HashMap hm) throws IOException, Exception
    {
        FileReader fr = new FileReader(prop.getProperty(resfile));
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(prop.getProperty(dvecFile)); 
        AnalyzerClass a=new AnalyzerClass(); 
        
        String line;
        Integer docid;   
        
        while ((line = br.readLine()) != null) {
            String[] token=line.split("\\s+");
            String tt=token[2];
           
            docid=r.tweetid_docid_map.getOrDefault(tt, -1);
            if(docid==-1){
                System.out.println(tt+" "+docid);
                continue;}
            String tweet_text=r.reader.document(docid).get("TEXT");
            
            if(tweet_text==null) {System.out.println("Tweet Text is null for: "+docid);continue;}
            if(a.analizeString("TEXT", tweet_text)==null) {System.out.println("Analyzed Tweet Text is null for: "+docid);continue;}
            String analized_tweet_text=a.analizeString("TEXT", tweet_text);
            //System.out.println("analized Tweet Text: "+analized_tweet_text);
            String o=tt.concat(" "+analized_tweet_text);
           
            fw.append(getAvgVec(hm, o)+"\n");
            
            fw.flush();
            tweet_text="";
        }

        fw.close();
        br.close();
        fr.close();
        
    }
    
    
    
    void compute_and_write_dvec(String docFile, String dvecFile, HashMap hm) throws IOException, Exception{
        FileReader fr = new FileReader(prop.getProperty(docFile));
        BufferedReader br = new BufferedReader(fr);
        FileWriter fw = new FileWriter(prop.getProperty(dvecFile));   
        String line=null;
        while ((line = br.readLine()) != null) {
            //System.out.println(line);
            fw.append(getAvgVec(hm, line)+"\n");
        }

        fw.close();
        br.close();
        fr.close();
    }
    
    public static void main(String[] args) throws IOException, Exception  {
        /*if (args.length < 3) {
            System.err.println("usage: java VectorizeDocs <tweets.vec file> <doc file> <o/p file>");
            return;
        }*/
        
        VectorizeDocs VD = new VectorizeDocs();
        VD.load_inputs_and_compute_avg();
        
        /// Computing Similarity
        VD.rv=new readVectors();
        VD.rv.compSim();
    }
}