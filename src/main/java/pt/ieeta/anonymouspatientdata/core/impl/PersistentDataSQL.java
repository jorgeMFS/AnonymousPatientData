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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class PersistentDataSQL 
{
    private Connection conn = null;
    
    public void open()
    {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(PersistentDataSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            Connection conn =
              DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (SQLException ex) 
        {
            Logger.getLogger(PersistentDataSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void createStructure()
    {
    

        Statement stat;
        try {
            stat = conn.createStatement();
            
            stat.executeUpdate("drop table if exists people;");
            stat.executeUpdate("create table people (name, map_name);");
            PreparedStatement prep = conn.prepareStatement(
                    "insert into people values (?, ?);");
        } catch (SQLException ex) {
            Logger.getLogger(PersistentDataSQL.class.getName()).log(Level.SEVERE, null, ex);
        }
   
    }
    
    
    public void insert(String name, String mappedName)
    {
    
    
    }
    

    public static void main(String [] args)
    {
        
        for (int i = 0 ; i<1000000; i++)
        {
        
        }
        
        
    }
    
    
    
    
    
}
