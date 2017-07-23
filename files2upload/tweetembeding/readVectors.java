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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author ayan
 */
public class readVectors {
    
    public static HashMap<String, DocVec> Query;
    public static HashMap<String, DocVec> Documents;
    
    public static readRESfile rr;
            
    public static Properties prop;
    
    public readVectors() throws IOException {
    prop=new GetProjetBaseDirAndSetProjectPropFile().prop;
        
    }
         
    
    
    public HashMap<String, DocVec> Load_DocumentVectors_from_file(String filename) throws FileNotFoundException, IOException
    {
        HashMap<String, DocVec> dv_map=new HashMap<>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        
        
        String line;
        while ((line = br.readLine()) != null) {
        String[] docvec = line.split("\\s+");
        DocVec dv=new DocVec(line);
        
        dv_map.put(dv.Docid,dv);
                
        }
        return dv_map;
    }
    
    void compute_similarity() throws IOException
    {
        DocVec DV= new DocVec();
        
        FileWriter fw = new FileWriter(prop.getProperty("Query_Doc_vector_dist"));
        
        Iterator Q = Query.entrySet().iterator();
        while (Q.hasNext()) {
            Map.Entry q = (Map.Entry)Q.next();
            String qno=q.getKey().toString().replaceAll("^MB00*", "");
            DocVec qv=(DocVec) q.getValue();
            
            for(int i=0;i<50;i++)
            {//System.err.println(qno+"   "+rr.r[i].queryID);
                if(qno.equals(rr.r[i].queryID))
                {
                    for(int j=0;i<=rr.r[i].doclist.length && rr.r[i].doclist[j]!=null; j++)
                    {
                        DocVec d;
                        if(Documents.containsKey(rr.r[i].doclist[j]))
                             d= Documents.get(rr.r[i].doclist[j]);
                        else
                            continue;
                            
                        String o=DV.compute_cosine_distance(qv, d)+" "+rr.r[i].rank[j]+" "+rr.r[i].score[j]+"\n";
                        fw.append(DV.compute_cosine_distance(qv, d)+" "+rr.r[i].rank[j]+" "+rr.r[i].score[j]+"\n");
                    }
                }
                    
            }
        }
    }

    /**
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        readVectors rv=new readVectors();
        Documents=rv.Load_DocumentVectors_from_file(prop.getProperty("Doc2VecFile"));
        System.out.println("Documents has been loaded ....");
        Query=rv.Load_DocumentVectors_from_file(prop.getProperty("Query2VecFile"));
        System.out.println("Documents has been loaded ....");
        
        rr=new readRESfile();
        rr.read_result_file(prop.getProperty("TRECresultFile"));
        System.out.println("Result file has been loaded ....");
        System.out.println("Computing Similarities ....");
        rv.compute_similarity();
        
        
    }
}
