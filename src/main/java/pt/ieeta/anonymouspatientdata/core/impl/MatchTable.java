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
    
    public MatchTable()
    {
    
    }
    
    public String getName(String word)
    {
    
        // Create a pattern to match breaks
        Pattern p = Pattern.compile("[,\\s]+");
        // Split input with the pattern
        String[] result = 
                 p.split(word);
        for (int i=0; i<result.length; i++)
            System.out.println(result[i]);
        
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

}
