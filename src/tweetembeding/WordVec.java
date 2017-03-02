/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

/**
 *
 * @author ayan
 */
public class WordVec {
    String word;
    float[] vec;

    WordVec(String line) {
        String[] wordvec = line.split("\\s+");
        word = wordvec[0];
        vec = new float[wordvec.length-1];
        for (int i=0; i < vec.length; i++) {
            vec[i] = Float.parseFloat(wordvec[i+1]);
        }
    }
}
