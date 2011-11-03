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

import au.com.bytecode.opencsv.CSVReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import pt.ieeta.anonymouspatientdata.core.IRandomNames;
import pt.ieeta.anonymouspatientdata.core.RandomString;

/**
 *
 * Contains a list of 
 * 
 * @author Luís A. Bastião Silva <bastiao@ua.pt>
 */
public class ForeignNames implements Serializable, IRandomNames
{

    
    public static final String ENCODING = "UTF-8";
    Set<String> names = new HashSet<String> ();
    
    private static ForeignNames instance = null;
    
    private ForeignNames()
    {
    
        
    }
    
    public static ForeignNames getInstance()
    {
    
        if (instance==null)
            instance = new ForeignNames();
        return instance;
        
    }
    
    
    public void loadCSV(String fileName) throws FileNotFoundException
    {
    
        CSVReader reader = new CSVReader(new FileReader(fileName), '\t', '\'');
        String [] nextLine;
        try {
            while ((nextLine = reader.readNext()) != null) {
                
                for (int i = 0; i<nextLine.length; i++)
                {
                    // Verify if it is a string
                    Pattern p = Pattern.compile("[,\\s]+");
                    
                    if (p.matcher(nextLine[0]).find())
                    {
                        addName(nextLine[0]);
                    }
                }
                
            }
        } catch (IOException ex) {
            Logger.getLogger(ForeignNames.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public void loadFromPlainText(String fileName) throws FileNotFoundException {
        
        StringBuilder text = new StringBuilder();
        
        Scanner scanner = new Scanner(new FileInputStream(fileName), ENCODING);
        try {
            while (scanner.hasNextLine()) {
                addName(scanner.nextLine());
            }
        } finally {
            scanner.close();
        }
        
    }

    public void loadRandomPlainText(String fileName) throws FileNotFoundException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean addName(String name) 
    {
        return this.names.add(name);
    }
    
    public String pop()
    {
        String result =  this.names.iterator().next();
        if (result==null)
        {
            RandomString random = new RandomString(10);
            result = random.nextString();
        }
        else
        {
            this.names.remove(result);
        }return result;
    }
    
    public static void load()
    {
       
        FileInputStream fileIn;
        try {
            fileIn = new FileInputStream("foreignNames.ser");
                        ObjectInputStream in = new ObjectInputStream(fileIn);

            
            instance = (ForeignNames)in.readObject();

            
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
