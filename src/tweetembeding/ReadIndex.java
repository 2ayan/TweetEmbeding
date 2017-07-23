/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author ayan
 */
public class ReadIndex {

    Properties prop;
    String propFileName;
    IndexReader reader;
    public HashMap<String, Integer> tweetid_docid_map;
    AnalyzerClass a;
    
    public ReadIndex() throws IOException {
    
    GetProjetBaseDirAndSetProjectPropFile setPropFile= new GetProjetBaseDirAndSetProjectPropFile();
        prop=setPropFile.prop;
        propFileName=setPropFile.propFileName;
        tweetid_docid_map=new HashMap<>();
        a=new AnalyzerClass();
    }
    
    void loadIndex() throws IOException{
        String indexDirectoryPath = prop.getProperty("indexPath");
        reader = DirectoryReader.open(FSDirectory.open(new File(indexDirectoryPath)));
        System.out.println("Index Loaded from "+prop.getProperty("indexPath"));
    }
    
    void Create_tweettime_docid_map() throws IOException{
        AnalyzerClass a=new AnalyzerClass();
        Analyzer anallyzer= a.setAnalyzer();
        
        int totalDocs=reader.numDocs();
        
        for(int i=0;i<totalDocs;i++){
           // System.out.println(reader.document(i).get("tweettime"));
            tweetid_docid_map.putIfAbsent((String)reader.document(i).get("tweettime"), Integer.valueOf(i));
            //System.err.println(tweetid_docid_map.get(reader.document(i).get("tweettime")));
        }
        System.out.println("Tweettime -> Docid HashMap Created");
    }
/*    
    public static void main(String[] args) throws IOException {
        ReadIndex ri=new ReadIndex();
        ri.loadIndex();
        ri.Create_tweettime_docid_map();
    }*/
}


