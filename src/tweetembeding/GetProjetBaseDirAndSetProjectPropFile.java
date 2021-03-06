/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweetembeding;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author ayan
 */
public class GetProjetBaseDirAndSetProjectPropFile extends Properties{

    public String  absProjectpath;
    public String propFileName;
    public Properties  prop; 

        public GetProjetBaseDirAndSetProjectPropFile() throws IOException {
        
        /* getting Project base absolute path */
        java.io.File file = new java.io.File("");   //Dummy file
        absProjectpath=file.getAbsolutePath();

        /* set Project base absolute path */
        String f= new PropFile().getPropFileName();
        File f1= new File(f);
        if(f1.exists())
            {
            /* property file loading */
            prop = new Properties();
            prop.load(new FileReader(f));
            System.out.println("Setting properties file: "+ f);
            }
        else
            {
            propFileName=absProjectpath.concat("/init.properties");
            prop = new Properties();
            prop.load(new FileReader(propFileName));
       // System.out.println("Setting properties file: "+ f);
       // System.out.println("File "+f+" does not exists.");
       // System.out.println("Setting Default properties file: "+ propFileName);
        }
        /* property files are loaded */
    }
        /*
        Overloading getProperty() method of Properties class.
        Now it will remove white spaces and new lines also then return the output.
        */

    /**
     *
     * @param s
     * @return
     */
    
   public String getProperty(String s){
       return getProperty(s).replaceAll("\\s+","").replaceAll("(\n|\r)+", "");
   }
    
}
class PropFile{
    String PropFileName;
    public PropFile(){
        PropFileName="";
    }
    public void setPropFileName(String f){
        PropFileName=f;
    }
    public String getPropFileName(){
        return PropFileName;
                
    }
}
