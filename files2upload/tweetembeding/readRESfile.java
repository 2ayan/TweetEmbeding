/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author ayan
 */
public class readRESfile {
    result[] r;
    void read_result_file(String resfile) throws IOException
    {
        FileReader fr = new FileReader(resfile);
        BufferedReader br = new BufferedReader(fr);
        String line;
        
        r=new result[50];
        for(int i=0;i<50;i++)
            r[i]=new result();
        
        
        int count=-1;
        while ((line = br.readLine()) != null) {
            String[] token=line.split("\\s+");
            int resno=Integer.parseInt(token[3]);
            if(resno==0)
                {
                  count++;
                  r[count].queryID=token[0];
                  r[count].doclist[resno]= token[2];
                  r[count].rank[resno]= Integer.parseInt(token[3]);
                  r[count].score[resno]= Float.parseFloat(token[4]);
                  
                }
            else
                {
                  r[count].doclist[resno]= token[2];
                  r[count].rank[resno]= Integer.parseInt(token[3]);
                  r[count].score[resno]= Float.parseFloat(token[4]);
                }
        }
    }
}