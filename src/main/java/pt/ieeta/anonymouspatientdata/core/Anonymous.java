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

package pt.ieeta.anonymouspatientdata.core;

import pt.ieeta.anonymouspatientdata.core.impl.ForeignNames;
import pt.ieeta.anonymouspatientdata.core.impl.MatchTable;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class Anonymous 
{

    private static Anonymous instance = null;
    
    private Anonymous()
    {
    
    }
    
    public static Anonymous getInstance()
    {
    
        if (instance==null)
            instance = new Anonymous();
        return instance;
        
    }
    
    
    public void start()
    {
        
        ForeignNames.load();
        MatchTable.load();
    
    }
    
    public void stop()
    {
        ForeignNames.save();
        MatchTable.save();   
    }
    
    
}
