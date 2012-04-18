// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   COMPortManager.java

package serial.logger;

import gnu.io.*;
import java.io.*;
import java.util.*;

// Referenced classes of package serial.logger:
//            SerialConnection

public class COMPortManager
{

    public COMPortManager()
    {
        ports = new HashMap();
    }

    public void close()
    {
        for(Iterator itr = ports.values().iterator(); itr.hasNext(); ((SerialConnection)itr.next()).close());
    }

    public static void main(String args[])
        throws PortInUseException, UnsupportedCommOperationException, IOException
    {
        getInstance().openConnection("/dev/ttyUSB0", 4800, "Hello World");
        byte fillMe[] = new byte[10];
        getInstance().grabConnection("/dev/ttyUSB0").readData(fillMe);
        System.out.println(getInstance().grabConnection("/dev/ttyUSB0").getPort().getBaudBase());
        System.out.println(new String(fillMe));
    }

    public static COMPortManager getInstance()
    {
        if(instance == null)
            instance = new COMPortManager();
        return instance;
    }

    public String listPorts()
    {
        String returnMe = "";
        for(Enumeration portList = CommPortIdentifier.getPortIdentifiers(); portList.hasMoreElements();)
        {
            CommPortIdentifier temp = (CommPortIdentifier)portList.nextElement();
            returnMe = (new StringBuilder(String.valueOf(returnMe))).append(temp.getName()).toString();
            returnMe = (new StringBuilder(String.valueOf(returnMe))).append("\n\tType: ").toString();
            switch(temp.getPortType())
            {
            case 1: // '\001'
                returnMe = (new StringBuilder(String.valueOf(returnMe))).append("SerialPort").toString();
                break;

            default:
                returnMe = (new StringBuilder(String.valueOf(returnMe))).append("Unknown").toString();
                break;
            }
            returnMe = (new StringBuilder(String.valueOf(returnMe))).append("\t\tOwner: ").append(temp.getCurrentOwner()).append("\n").toString();
        }

        return returnMe;
    }

    public boolean portExists(String name)
    {
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        boolean exists = false;
        while(portList.hasMoreElements()) 
        {
            CommPortIdentifier temp = (CommPortIdentifier)portList.nextElement();
            if(temp.getName().equals(name))
            {
                exists = true;
                break;
            }
        }
        return exists;
    }

    public String[] listPortsToArray()
    {
        ArrayList returnMe = new ArrayList();
        for(Enumeration portList = CommPortIdentifier.getPortIdentifiers(); portList.hasMoreElements();)
        {
            CommPortIdentifier temp = (CommPortIdentifier)portList.nextElement();
            switch(temp.getPortType())
            {
            case 1: // '\001'
                returnMe.add(temp.getName());
                break;
            }
        }

        return (String[])returnMe.toArray(new String[returnMe.size()]);
    }

    public boolean readByte(int com)
    {
        SerialPort temp = ((SerialConnection)ports.get(Integer.valueOf(com))).getPort();
        if(temp != null)
            try
            {
                System.out.println("Attempting To Read Byte");
                while(temp.getInputStream().available() <= 0) ;
                int dummy = temp.getInputStream().read();
                System.out.println((new StringBuilder("Recieved ")).append(dummy).toString());
            }
            catch(IOException e)
            {
                return false;
            }
        return true;
    }

    public SerialConnection grabConnection(String number)
    {
        SerialConnection temp = (SerialConnection)ports.get(number);
        if(temp == null)
            return null;
        else
            return temp;
    }

    public boolean openConnection(String comName, int baud, String appName)
    {
        SerialConnection temp = setupConnection(comName, baud, appName);
        if(temp != null)
        {
            ports.put(comName, temp);
            return true;
        } else
        {
            return false;
        }
    }

    public boolean openConnection(String comName, int baud)
    {
        SerialConnection temp = setupConnection(comName, baud);
        if(temp != null)
        {
            ports.put(comName, temp);
            return true;
        } else
        {
            return false;
        }
    }

    private static SerialConnection setupConnection(String comName, int baud)
    {
        return setupConnection(comName, baud, "SimpleWrite");
    }

    private static SerialConnection setupConnection(String comName, int baud, String appName)
    {
        SerialPort serialPort = null;
        OutputStream outputStream = null;
        boolean portFound = false;
        portList = CommPortIdentifier.getPortIdentifiers();
        while(portList.hasMoreElements()) 
        {
            CommPortIdentifier portId = (CommPortIdentifier)portList.nextElement();
            if(portId.getPortType() != 1 || !portId.getName().equals(comName))
                continue;
            System.out.println((new StringBuilder("Found port ")).append(comName).toString());
            portFound = true;
            try
            {
                serialPort = (SerialPort)portId.open(appName, 2000);
            }
            catch(PortInUseException e)
            {
                System.out.println("Port in use.");
                continue;
            }
            try
            {
                outputStream = serialPort.getOutputStream();
            }
            catch(IOException ioexception) { }
            try
            {
                serialPort.setSerialPortParams(baud, 8, 1, 0);
            }
            catch(UnsupportedCommOperationException unsupportedcommoperationexception) { }
            try
            {
                serialPort.notifyOnOutputEmpty(true);
            }
            catch(Exception e)
            {
                System.out.println("Error setting event notification");
                System.out.println(e.toString());
                System.exit(-1);
            }
        }
        if(!portFound)
            System.out.println((new StringBuilder("port ")).append(comName).append(" not found.").toString());
        return new SerialConnection(serialPort, outputStream);
    }

    private HashMap ports;
    static Enumeration portList;
    private static COMPortManager instance;
}
