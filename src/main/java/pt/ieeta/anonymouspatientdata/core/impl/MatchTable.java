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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Pattern;
import pt.ieeta.anonymouspatientdata.core.IMatch;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class MatchTable  implements Serializable, IMatch
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
        StringTokenizer st = new StringTokenizer(word);
        
        while (st.hasMoreTokens()) {
            String current = st.nextToken("(^|#| )");
         
            String wordReplace;
            if (patientNames.containsKey(current))
            {
                wordReplace = patientNames.get(current);
            }
            else
            {
                wordReplace = ForeignNames.getInstance().pop();
                this.patientNames.put(current, wordReplace);
            }
            
            word = word.replace(current,wordReplace );
         
        }
    
        
        /**
        Pattern p = Pattern.compile("[,\\s]+");
        
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
                this.patientNames.put(result[i], wordReplace);
            }
            
            word = word.replace(result[i],wordReplace );
        
        }
         **/
        
        return word;

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
    
    

    
    public static void load()
    {
        File f = new File("match.ser");
        if (!f.exists())
        {    
            return;
        }
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream("match.ser");
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
            fileOut = new FileOutputStream("match.ser");
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

    public String getPatientID(String word) {
        String result;
        if (patientIds.containsKey(word))
        {
            result = patientIds.get(word);
        }
        else
        {
            result = UUID.randomUUID().toString();
            patientIds.put(word, result);
        }
        return result;
    }

    public String getAccessionNumber(String word) {
        
        String result;
        if (accessionNumbers.containsKey(word))
        {
            result = accessionNumbers.get(word);
        }
        else
        {
            result = UUID.randomUUID().toString();
            accessionNumbers.put(word, result);
        }
        return result;
        
    }
    
    
    public static void main(String [] args)
    {
        String word = "Luis^Bastiao#das asd";
        StringTokenizer st = new StringTokenizer(word);
        
        while (st.hasMoreTokens()) {
         System.out.println(st.nextToken("(^|#| )"));
        }
        
        
    
    }

}
