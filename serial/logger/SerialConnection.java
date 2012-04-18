// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   SerialConnection.java

package serial.logger;

import gnu.io.SerialPort;
import java.io.*;

// Referenced classes of package serial.logger:
//            Callback

public class SerialConnection
    implements Runnable
{

    public void setId(int id_id)
    {
        id = id_id;
    }

    public SerialConnection(SerialPort port, OutputStream out)
    {
        keepRunning = false;
        id = -1;
        serialPort = port;
        outputStream = out;
    }

    public SerialPort getPort()
    {
        return serialPort;
    }

    public void close()
    {
        serialPort.close();
    }

    public void setCallback(Callback doMe)
    {
        executeMe = doMe;
    }

    public void stopThread()
    {
        keepRunning = false;
    }

    public boolean startThread()
    {
        keepRunning = true;
        (new Thread(this)).start();
        return true;
    }

    public synchronized void sendData(byte data[])
        throws IOException
    {
        outputStream.write(data);
        try
        {
            Thread.sleep(2L);
        }
        catch(Exception exception) { }
        outputStream.flush();
    }

    public synchronized int readData(byte data[])
        throws IOException
    {
        if(serialPort.getInputStream().available() > 0)
            return serialPort.getInputStream().read(data);
        else
            return 0;
    }

    public void run()
    {
        if(executeMe != null)
            while(keepRunning) 
                try
                {
                    executeMe.execute(this);
                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }
    }

    private SerialPort serialPort;
    private OutputStream outputStream;
    private Callback executeMe;
    static boolean outputBufferEmptyFlag = false;
    boolean keepRunning;
    int id;

}
