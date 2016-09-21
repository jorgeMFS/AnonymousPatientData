/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package anonymizer;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

/**
 *
 * @author Carlos
 */
public class Anonymizer
{
    private final ArrayList<String> fieldsIndexed;
    private final ArrayList<String> fieldsAnonymized;
    private MessageDigest md;
    
    public Anonymizer(ArrayList<String> fieldsIndexed, ArrayList<String> fieldsAnonymized)
    {
        this.fieldsIndexed = fieldsIndexed;
        this.fieldsAnonymized = fieldsAnonymized;
        
        this.md = null;
        try
        {
            this.md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(Anonymizer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private String anonymizeText(String text)
    {
        byte[] hash = null;
        try
        {
            hash = md.digest(text.getBytes("UTF-8"));
        }
        catch (UnsupportedEncodingException ex)
        {
            Logger.getLogger(Anonymizer.class.getName()).log(Level.SEVERE, null, ex);
        }
        //converting byte array to Hexadecimal String
        StringBuilder sb = new StringBuilder(2 * hash.length);
        for (byte b : hash)
        {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    
    public Document anonymize(Document toAnonymize)
    {
        Document newDoc = new Document();
        for(String f: fieldsIndexed)
        {
            Field field = toAnonymize.getField(f);
            
            if(field != null)
            {
                newDoc.add(field);
            }
        }
        
        for(String f: fieldsAnonymized)
        {
            Field field = toAnonymize.getField(f);
            if(field != null)
            {
                String str = field.stringValue();
                String newStr = this.anonymizeText(str);
                
                if(str.contains(" "))
                {
                    String[] tokens = str.split(str);
                    
                    for(String tok : tokens)
                    {
                        if(!tok.equals(""))
                        {
                            newStr += " "+ this.anonymizeText(tok);   
                        }
                    }
                                    
                 }
                //System.out.println(newStr);
                field.setValue(newStr);
                newDoc.add(field);              
            }
        }
        return newDoc;     
    }
}
