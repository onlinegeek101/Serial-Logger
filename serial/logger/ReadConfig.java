// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   ReadConfig.java

package serial.logger;

import java.io.*;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class ReadConfig extends HashMap
{

    public ReadConfig(String paramNames[])
    {
        String as[];
        int k = (as = paramNames).length;
        for(int j = 0; j < k; j++)
        {
            String name = as[j];
            put(name, "Undefined");
        }

        try
        {
            File temp = new File((new StringBuilder(".")).append(File.separator).append("config").toString());
            BufferedReader in = new BufferedReader(new FileReader(temp));
            String line = in.readLine();
            int i = 0;
            for(; line != null; line = in.readLine())
            {
                parseLine(line, i);
                i++;
            }

            if(paramNames.length != i)
            {
                String quantifier = paramNames.length <= i ? "To Many" : "To Few";
                JOptionPane.showMessageDialog(null, (new StringBuilder(String.valueOf(quantifier))).append(" Config File Parameters").toString());
                System.exit(1);
            }
        }
        catch(IOException e)
        {
            JOptionPane.showMessageDialog(null, "No Config File In Launch Directory!");
            System.exit(1);
        }
    }

    public String get(Object key)
    {
        return (String)super.get(key);
    }

    public void parseLine(String line, int number)
    {
        String items[] = line.split("=");
        if(items.length < 2)
        {
            JOptionPane.showMessageDialog(null, (new StringBuilder("Config File Bad Line ")).append(line).append(" at Line Number ").append(number).toString());
            System.exit(1);
        } else
        {
            String get = get(items[0]);
            if(get != null)
            {
                put(items[0], items[1]);
            } else
            {
                JOptionPane.showMessageDialog(null, (new StringBuilder("Config File Bad Line - Undefined Field ")).append(line).append(" at Line Number ").append(number).toString());
                System.exit(1);
            }
        }
    }

    public volatile Object get(Object obj)
    {
        return get(obj);
    }
}
