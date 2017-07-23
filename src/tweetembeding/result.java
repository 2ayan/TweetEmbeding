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
public class result {
    String queryID;
    String[] doclist;
    int[] rank;
    float[] score;
    
   result()
    {
            queryID="";
            doclist=new String[10000];
            rank=new int[10000];
            score=new float[10000];
    }
    
}
