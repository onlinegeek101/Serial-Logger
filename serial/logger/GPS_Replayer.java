// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GPS_Replayer.java

package serial.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

// Referenced classes of package serial.logger:
//            Callback, COMPortManager, SerialConnection, JCountDown, 
//            GUI

public class GPS_Replayer
    implements Callback, ActionListener
{

    public GPS_Replayer(String comName)
    {
        replaying = false;
        connected = false;
        com = comName;
    }

    public void start(int id)
        throws IOException
    {
        SerialConnection temp = COMPortManager.getInstance().grabConnection(com);
        temp.setCallback(this);
        inRead = new Scanner(in);
        inRead.useDelimiter("GPGGA");
        replaying = true;
        temp.setId(id);
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
        if(replaying)
        {
            inRead.close();
            inRead = null;
        }
        replaying = false;
    }

    public static int getTimeToRun(File filename)
        throws IOException
    {
        BufferedReader is = new BufferedReader(new FileReader(filename));
        int i;
        int count = 0;
        int readChars = 0;
        for(String line = is.readLine(); line != null; line = is.readLine())
            if(line.startsWith("$GPGGA"))
                count++;

        i = count;
        is.close();
        return i;
        Exception exception;
        exception;
        is.close();
        throw exception;
    }

    public void execute(Object passMe)
        throws IOException
    {
        SerialConnection temp = (SerialConnection)passMe;
        while(replaying && inRead.hasNext()) 
        {
            String out = inRead.next();
            System.out.print((new StringBuilder("$GPGGA")).append(out.substring(0, out.length() - 1)).toString());
            ((JCountDown)JCountDown.items.get(Integer.valueOf(temp.id))).down();
            temp.sendData((new StringBuilder("$GPGGA")).append(out.substring(0, out.length() - 1)).toString().getBytes());
            try
            {
                Thread.sleep(1000L);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        replaying = false;
        JOptionPane.showMessageDialog(null, (new StringBuilder("Done Replaying ")).append(in.getName()).toString());
        GUI.removeActionItem(in.getName());
        temp.stopThread();
        temp.close();
    }

    public void actionPerformed(ActionEvent e)
    {
        if(!replaying)
        {
            Object options[] = {
                "Choose a File to Replay", "Cancel"
            };
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(null);
            if(returnVal == 0)
            {
                in = fc.getSelectedFile();
                int id = GUI.getInstance().addReplayer(in, this);
                try
                {
                    start(id);
                }
                catch(IOException e1)
                {
                    e1.printStackTrace();
                }
            }
        } else
        {
            Object options[] = {
                "Keep Replaying", "Stop Replaying"
            };
            int n = JOptionPane.showOptionDialog(GUI.getInstance(), (new StringBuilder("Would You Like to log")).append(com).toString(), "Logging In Progress", 1, 3, null, options, options[1]);
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
                GUI.removeActionItem(in.getName());
            }
        }
    }

    String com;
    File in;
    Scanner inRead;
    boolean replaying;
    boolean connected;
}
