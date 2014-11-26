/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package anonymzer4index;

import anonymizer.Settings;
import index.IndexHandler;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class Test
{
    public static void main(String[] args) throws IOException
    {
        String indexPath = "./index/indexed";
        String indexFinalPath = "./index2";
        File f = new File(indexPath);
        File fout = new File(indexFinalPath);
        //IndexHandler i = new IndexHandler(f, fout);
        Settings set = new Settings("./settingFiles/fieldsIndex.txt", "./settingFiles/anonymizeFields.txt");
        ArrayList<String> fields = set.getIndexFields();
        for (String fi : fields)
        {
            System.out.println(fi);
        }
        MessageDigest md = null;
        try
        {
            md = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException ex)
        {
            Logger.getLogger(Test.class.getName()).log(Level.SEVERE, null, ex);
        }
        byte[] hash = md.digest("bla".getBytes("UTF-8"));
        //converting byte array to Hexadecimal String
        StringBuilder sb = new StringBuilder(2 * hash.length);
        for (byte b : hash)
        {
            sb.append(String.format("%02x", b & 0xff));
        }
        System.out.println(sb.toString());
        //File output = new File("log.txt");
        //System.setOut(new PrintStream(output));
        //i.getFields();
    }
}
