/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 *
 * @author ayan
 */
public class AnalyzerClass {
    
    Analyzer analyzer;
    Properties prop; 

    AnalyzerClass() throws IOException {
        GetProjetBaseDirAndSetProjectPropFile setPropFile= new GetProjetBaseDirAndSetProjectPropFile();
        prop=setPropFile.prop;
        this.analyzer=setAnalyzer();
    }
    
    AnalyzerClass(String rPropFile) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
     public final org.apache.lucene.analysis.Analyzer setAnalyzer() throws IOException {
      

        String stopFile = prop.getProperty("stopFile");
        
        List<String> stopwords = new ArrayList<>();

        String line;
        try {
            FileReader fr = new FileReader(stopFile);
            BufferedReader br = new BufferedReader(fr);
            while ( (line = br.readLine()) != null ) {
                stopwords.add(line.trim().replaceAll("\\s+", "").replaceAll("\n", "").replaceAll("\r", ""));
            }
            br.close(); fr.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }

         analyzer = new EnglishAnalyzer(StopFilter.makeStopSet(stopwords));
         return analyzer;
    }
    
    public org.apache.lucene.analysis.Analyzer getAnalyzer() {
        return analyzer;
    }
    
   public String analizeString(String FIELD, String txt) throws IOException {
    this.analyzer=setAnalyzer();
    TokenStream stream = analyzer.tokenStream(FIELD, new StringReader(txt));
    CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
    stream.reset();

    StringBuffer tokenizedContentBuff= new StringBuffer();
    while (stream.incrementToken()) {
        String term = termAtt.toString();
        if(!term.equals("nbsp"))
            tokenizedContentBuff.append(term).append(" ");
    }

    stream.end();
    stream.close();
    
    return tokenizedContentBuff.toString();
   }
}
