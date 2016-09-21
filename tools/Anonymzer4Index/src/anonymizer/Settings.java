/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package anonymizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Carlos
 */
public class Settings
{
    private final ArrayList<String> indexFields;
    private final ArrayList<String> anonymizedFields;
    
    public Settings(String neededFieldsFile, String anonymizedFieldsFile) throws FileNotFoundException, IOException
    {
        this.indexFields = new ArrayList<>();
        this.anonymizedFields = new ArrayList<>();
        
        File indexFieldsF = new File(neededFieldsFile);
        File anonymizedFieldsF = new File(anonymizedFieldsFile);
        
        BufferedReader brindex = new BufferedReader(new FileReader(indexFieldsF));
        BufferedReader branonym = new BufferedReader(new FileReader(anonymizedFieldsF));
        
        String line;
        while((line = brindex.readLine()) != null)
        {
            indexFields.add(line);
        }
        while((line = branonym.readLine()) != null)
        {
            anonymizedFields.add(line);
        }    
    }
    
    public ArrayList<String> getIndexFields()
    {
        return (ArrayList<String>) this.indexFields.clone();
    }
    
    public ArrayList<String> getAnonymizedFields()
    {
        return (ArrayList<String>) this.anonymizedFields.clone();
    }
    
}
