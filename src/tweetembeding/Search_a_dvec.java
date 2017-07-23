/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author ayan
 */
public class Search_a_dvec {
    
    
    public static readVectors rv;
    
    public Search_a_dvec() throws IOException, FileNotFoundException{
        rv=new readVectors();
        System.err.println("Loading Document Vectors ....");
        rv.Documents=rv.Load_DocumentVectors_from_file(rv.prop.getProperty("VecFile"));
        System.err.println("Documents has been loaded ....");    
    }
    
    public static void main(String[] args) throws IOException, FileNotFoundException {
        Search_a_dvec Sv=new Search_a_dvec();
        
        BufferedReader br = new BufferedReader(new java.io.FileReader(Sv.rv.prop.getProperty("DocVecs2Search")));
        FileWriter fw = new FileWriter(Sv.rv.prop.getProperty("DocVec2Search.Output"));
       
        String line;
        while ((line = br.readLine()) != null) {
            DocVec f=Sv.rv.Documents.get(line.replaceAll("\\s+", "").replaceAll("\n", ""));
            fw.append(f.Docid);
            for(int i=0;i<f.vec.length;i++)
                fw.append(" "+f.vec[i]);
            fw.append("\n");
        } 
        fw.close();
        br.close();
    }
    
    
}
