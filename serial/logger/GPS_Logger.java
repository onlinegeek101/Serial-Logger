// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GPS_Logger.java

package serial.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JOptionPane;

// Referenced classes of package serial.logger:
//            Callback, COMPortManager, GUI, ReadConfig, 
//            SerialConnection

public class GPS_Logger
    implements Callback, ActionListener
{

    public GPS_Logger(String logName, String comName)
    {
        dest = "";
        logging = false;
        connected = false;
        name = logName;
        com = comName;
    }

    public GPS_Logger(String logName, String comName, String destination)
    {
        dest = "";
        logging = false;
        connected = false;
        name = logName;
        com = comName;
        dest = destination;
    }

    public void connect()
        throws IOException
    {
        if(!connected)
        {
            SerialConnection temp = COMPortManager.getInstance().grabConnection(com);
            boolean exists = false;
            if(temp == null)
                exists = COMPortManager.getInstance().openConnection(com, 4800, name);
            if(temp != null || exists)
            {
                output = new File((new StringBuilder(String.valueOf(GUI.config().get("LOG_DIR")))).append(File.separator).append(dest).append(File.separator).append(name.replace(File.separator, "_")).append(".gps").toString());
                if(!output.exists())
                    output.createNewFile();
            }
            connected = true;
        }
    }

    public void start()
        throws IOException
    {
        SerialConnection temp = COMPortManager.getInstance().grabConnection(com);
        temp.setCallback(this);
        outWrite = new FileWriter(output);
        logging = true;
        temp.startThread();
    }

    public void close()
        throws IOException
    {
        stop();
    }

    public void stop()
        throws IOException
    {
        COMPortManager.getInstance().grabConnection(com).stopThread();
        if(logging)
        {
            outWrite.flush();
            outWrite.close();
            outWrite = null;
        }
        logging = false;
    }

    public void execute(Object passMe)
        throws IOException
    {
        SerialConnection temp = (SerialConnection)passMe;
        byte buffer[] = new byte[1024];
        int read = temp.readData(buffer);
        if(read > 0)
        {
            String tempStr = new String(buffer, 0, read);
            System.out.println(tempStr);
            outWrite.append(tempStr);
            outWrite.flush();
        }
    }

    public void actionPerformed(ActionEvent e)
    {
        if(!logging)
        {
            Object options[] = {
                "Log This Device", "Cancel"
            };
            int n = JOptionPane.showOptionDialog(GUI.getInstance(), (new StringBuilder("Would You Like to log")).append(name).toString(), "Log?", 1, 3, null, options, options[1]);
            if(n == 0)
            {
                GUI.getInstance().addLogger(name, this);
                try
                {
                    start();
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        } else
        {
            Object options[] = {
                "Keep Logging", "Stop Logging"
            };
            int n = JOptionPane.showOptionDialog(GUI.getInstance(), (new StringBuilder("Would You Like to log")).append(name).toString(), "Logging In Progress", 1, 3, null, options, options[1]);
            if(n == 1)
            {
                try
                {
                    stop();
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }
                GUI.removeActionItem(name);
            }
        }
    }

    String name;
    String com;
    File output;
    String dest;
    FileWriter outWrite;
    boolean logging;
    boolean connected;
}
