/*  Copyright   2011 - Luís A. Bastião Silva
 *
 *  This file is part of AnonymousPatientData.
 *
 *  AnonymousPatientData is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  AnonymousPatientData is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with PACScloud.  If not, see <http://www.gnu.org/licenses/>.
 */


package pt.ieeta.anonymouspatientdata.core.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class MatchTable  implements Serializable
{
    
    
    private Map<String, String> patientNames = new HashMap<String, String>();

    private Map<String, String> patientIds = new HashMap<String, String>();
    
    private Map<String, String> accessionNumbers = new HashMap<String, String>();
    
    private MatchTable()
    {
    
    }
    
    private static MatchTable instance = null;
    
    
    
    public static MatchTable getInstance()
    {
    
        if (instance==null)
            instance = new MatchTable();
        return instance;
        
    }
    
    public String getName(String word)
    {
    
        
        // Create a pattern to match breaks
        Pattern p = Pattern.compile("[,\\s]+");
        // Split input with the pattern
        String[] result = 
                 p.split(word);
        for (int i=0; i<result.length; i++)
        {
            String wordReplace = "";
            if (patientNames.containsKey(result[i]))
            {
                wordReplace = patientNames.get(result[i]);
            }
            else
            {
                wordReplace = ForeignNames.getInstance().pop();
            }
            
            word = word.replace(result[i],wordReplace );
        
        }
        
        return "";

    }

    /**
     * @return the patientNames
     */
    public Map<String, String> getPatientNames() {
        return patientNames;
    }

    /**
     * @param patientNames the patientNames to set
     */
    public void setPatientNames(Map<String, String> patientNames) {
        this.patientNames = patientNames;
    }

    /**
     * @return the patientIds
     */
    public Map<String, String> getPatientIds() {
        return patientIds;
    }

    /**
     * @param patientIds the patientIds to set
     */
    public void setPatientIds(Map<String, String> patientIds) {
        this.patientIds = patientIds;
    }

    /**
     * @return the accessionNumbers
     */
    public Map<String, String> getAccessionNumbers() {
        return accessionNumbers;
    }

    /**
     * @param accessionNumbers the accessionNumbers to set
     */
    public void setAccessionNumbers(Map<String, String> accessionNumbers) {
        this.accessionNumbers = accessionNumbers;
    }
    
    
    
    public static void main(String [] args)
    {
        MatchTable matchTable = new MatchTable();
        
        
        
    }
    
    
        public static void load()
    {
       
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream("foreignNames.ser");
                        ObjectInputStream in = new ObjectInputStream(fileIn);

            
            instance = (MatchTable)in.readObject();

            
            in.close();
            fileIn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    
    public static void save()
    {
               FileOutputStream fileOut;
        try {
            fileOut = new FileOutputStream("HTExample.ser");
                       ObjectOutputStream out = new ObjectOutputStream(fileOut);

            System.out.println("Writing Hashtable Object...");
            out.writeObject(instance);

            System.out.println("Closing all output streams...\n");
            out.close();
            fileOut.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
 
    
    }
    

}
