/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anonymzer4index;

import anonymizer.Anonymizer;
import anonymizer.Settings;
import index.IndexHandler;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class Anonymzer4Index
{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        int sleepingTime = 0;
        if(args.length > 0)
        {
             sleepingTime = Integer.parseInt(args[0]);
        }
        
        String indexPath = "./index/indexed";
        String indexFinalPath = "./index2";
        
        if(args.length > 1)
        {
            indexPath = args[1];
        }
        
        String indexedFields = "./settingFiles/fieldsIndex.txt";
        String anonymizedFields = "./settingFiles/anonymizeFields.txt";
        
        Settings settings = null;
        try
        {
           settings = new Settings(indexedFields, anonymizedFields);
        }
        catch (IOException ex)
        {
            Logger.getLogger(Anonymzer4Index.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        Anonymizer anon;
        anon = new Anonymizer(settings.getIndexFields(), settings.getAnonymizedFields());
        
        File f = new File(indexPath);
        File fout = new File(indexFinalPath);
        
        IndexHandler handler = new IndexHandler(f, fout);
        try
        {
            handler.anonymizeIndex(anon, sleepingTime);
           // handler.testFinalIndex();
        }
        catch (IOException ex)
        {
            Logger.getLogger(Anonymzer4Index.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
}
