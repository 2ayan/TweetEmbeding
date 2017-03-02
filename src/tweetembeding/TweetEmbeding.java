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
public class TweetEmbeding {

    /**
     * @param args the command line arguments
     */
    
    public HashMap<String, Integer> term_termID;
    public HashMap<Integer, String> termID_term;
    public HashMap<Integer, Integer[]> NearestNeighbour;
    
    public Properties prop;
    
    TweetEmbeding() throws IOException
    {
        prop=new GetProjetBaseDirAndSetProjectPropFile().prop;
    }
    
    public void load_terms() throws FileNotFoundException, IOException
    {
        term_termID = new HashMap<String, Integer>();
        termID_term = new HashMap<Integer, String>();
        
        String VecFile=prop.getProperty("VecFile");
        
        FileReader fr = new FileReader(VecFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        int i=0;
        while ((line = br.readLine()) != null) {
            term_termID.put(line.split("\\s+")[0], Integer.valueOf(i));
            termID_term.put(Integer.valueOf(i), line.split("\\s+")[0]);
            i++;
        }

        br.close();
        fr.close();
    }
    
    public void load_NearestNebour() throws IOException
    {
        String NearestNeighbourIDFile=prop.getProperty("NearestNeighbourIDFile");
        
        NearestNeighbour=new HashMap();
        
        int termsId=-1;
        Integer []NN_termIds;
        FileReader fr = new FileReader(NearestNeighbourIDFile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        String []ids;
        while ((line = br.readLine()) != null) {
           ids=line.replace("\n", "").split("\\s+");
           NN_termIds=new Integer[ids.length];
           for(int i=0;i<ids.length;i++)
           {
               if(i==0)
                   termsId=Integer.valueOf(ids[i]);
                   NN_termIds[i]=Integer.valueOf(ids[i]);
           }
           NearestNeighbour.put(termsId, NN_termIds);
        }
        
    }
    
    public void expand_document() throws IOException
    {
      load_terms();
      load_NearestNebour();  
      
      FileReader fr = new FileReader(prop.getProperty("DocFile"));
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        String []ids;
        
        FileWriter fw = new FileWriter(prop.getProperty("OutputFile"));
        while ((line = br.readLine()) != null) 
            {
            ids=line.replace("\n", "").trim().replaceAll("\\s+", " ").split("\\s+");
            
            for(int i=0;i<ids.length;i++)
                {
                Integer termsID=term_termID.get(ids[i]);
                Integer []NNS=NearestNeighbour.get(termsID);
                //    System.err.println(termsID);
                if(NNS!=null)
                for(int j=0;j<NNS.length;j++)
                    {
                     fw.write(" "+termID_term.get(NNS[j]));
                     System.err.print(" "+termID_term.get(NNS[j]));
                    }
                fw.write("  "); 
                }
                fw.write("\n");
                System.err.print("\n");
            }
        fw.close();
        br.close();
        fr.close();
      
    }
    

    public static void main(String[] args) throws IOException {
        
        // TODO code application logic here
        TweetEmbeding te=new TweetEmbeding();
        te.expand_document();
        
    }
    
}
