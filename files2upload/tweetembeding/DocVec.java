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
import java.util.Map;

/**
 *
 * @author ayan
 */

    public class DocVec {
    String Docid;
    float[] vec;

    public DocVec() {
    }

    
    DocVec(String line) {
        String[] docvec = line.split("\\s+");
        Docid = docvec[0];
        vec = new float[docvec.length-1];
        for (int i=0; i < vec.length; i++) {
            vec[i] = Float.parseFloat(docvec[i+1]);
        }
    }
    
    String compute_cosine_distance(DocVec dv1, DocVec dv2)
    {
        float o=cosine(dv1.vec, dv2.vec);
        return dv1.Docid
                .concat(" "+dv2.Docid+" ")
                .concat(Float.toString(o)); 
    }
    
    float cosine(float[] a, float[] b)
    {
        float veclen1=0, veclen2=0,  dotproduct=0;
        for(int i=0;i<a.length;i++)
        {
               veclen1+=a[i]*a[i];
               veclen2+=b[i]*b[i];
               dotproduct+=a[i]*b[i];
        }
        return dotproduct/(veclen1*veclen2);
    }
    
}

    