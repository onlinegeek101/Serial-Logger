// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   GUI.java

package serial.logger;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.*;
import java.io.*;
import java.util.HashMap;
import java.util.UUID;
import javax.swing.*;

// Referenced classes of package serial.logger:
//            ReadConfig, COMPortManager, COMPortButton, JCountDown, 
//            GPS_Replayer, GPS_Logger

public class GUI extends JFrame
    implements WindowListener
{

    public static void install()
    {
        conf = new ReadConfig(new String[] {
            "JAVA_HOME", "LOG_DIR"
        });
        String osType = System.getProperty("os.arch");
        String osName = System.getProperty("os.name");
        if(!osName.startsWith("Mac OS X") && !osName.equals("Linux"))
        {
            System.out.println((new StringBuilder("Not a ")).append(osName).append(" Application Sorry").toString());
            System.exit(1);
        }
        if(osType.equals("amd64") || osType.equals("i686") || osType.equals("i386"))
            installFile(osType);
        else
            System.out.println((new StringBuilder("Os Architecture")).append(osType).append(" Not Supported").toString());
    }

    public static void installFile(String os)
    {
        String s = File.separator;
        File driver = new File((new StringBuilder("Drivers")).append(s).append(os).append(s).append("librxtxSerial.so").toString());
        System.out.println(driver.getAbsolutePath());
        if(!driver.exists())
        {
            System.out.println((new StringBuilder("Missing ")).append(os).append(" Driver").toString());
            System.exit(1);
        }
        File cpyTo = new File((new StringBuilder(String.valueOf(conf.get("JAVA_HOME")))).append(s).append("jre").append(s).append("lib").append(s).append(os).append(s).append("librxtxSerial.so").toString());
        copyfile(driver, cpyTo);
    }

    public static void main(String args[])
    {
        if(args.length > 0)
        {
            if(args[0].equalsIgnoreCase("-install") || args[0].equals("-i"))
                install();
            else
            if(args[0].equals("-help") || args[0].equals("-h"))
                System.out.println("To Install Use the -install or -i flag");
        } else
        {
            getInstance().setVisible(true);
        }
    }

    private GUI()
    {
        actionButtons = new HashMap();
        conf = new ReadConfig(new String[] {
            "LOG_DIR", "JAVA_HOME"
        });
        File temp = new File(conf.get("LOG_DIR"));
        if(!temp.exists())
            temp.mkdirs();
        actionItems.setLayout(new BoxLayout(actionItems, 0));
        COMPortList.setLayout(new BoxLayout(COMPortList, 1));
        infoSection.setLayout(new BoxLayout(infoSection, 1));
        setTitle("GPS Logger and Emulator");
        setSize(300, 400);
        setLocation(200, 200);
        String ports[] = COMPortManager.getInstance().listPortsToArray();
        String as[];
        int j = (as = ports).length;
        for(int i = 0; i < j; i++)
        {
            String item = as[i];
            COMPortButton tempButton = new COMPortButton(item, item, COMPortList, infoSection);
            COMPortList.add(tempButton);
        }

        addWindowListener(this);
        setDefaultCloseOperation(3);
        setLayout(new BorderLayout());
        add(COMPortList, "Before");
        infoSection.setSize(400, 350);
        actionItems.setSize(400, 50);
        add(infoSection, "Center");
        add(actionItems, "Last");
        (new Timer(1000, new ActionListener() {

            public void actionPerformed(ActionEvent actionevent)
            {
            }

            final GUI this$0;

            
            {
                this$0 = GUI.this;
                super();
            }
        })).start();
    }

    public static ReadConfig config()
    {
        return conf;
    }

    public void addLogger(String name, ActionListener temp)
    {
        JButton item = new JButton((new StringBuilder("Logging ")).append(name).toString());
        item.addActionListener(temp);
        actionButtons.put(name, item);
        actionItems.add(item);
        actionItems.revalidate();
    }

    public int addReplayer(File name, ActionListener temp)
    {
        JButton item = new JButton((new StringBuilder("Replaying ")).append(name.getName()).toString());
        int id = (int)UUID.randomUUID().getLeastSignificantBits();
        try
        {
            JCountDown down = new JCountDown(GPS_Replayer.getTimeToRun(name), id, item);
            actionItems.add(down);
        }
        catch(IOException ioexception) { }
        item.addActionListener(temp);
        actionButtons.put(name.getName(), item);
        actionItems.add(item);
        actionItems.revalidate();
        return id;
    }

    public static GUI getInstance()
    {
        if(instance == null)
            instance = new GUI();
        return instance;
    }

    public void windowActivated(WindowEvent windowevent)
    {
    }

    public void windowClosed(WindowEvent windowevent)
    {
    }

    public void windowClosing(WindowEvent arg0)
    {
        Component acomponent[];
        int j = (acomponent = COMPortList.getComponents()).length;
        for(int i = 0; i < j; i++)
        {
            Component temp = acomponent[i];
            if(temp.getClass().equals(serial/logger/COMPortButton))
                try
                {
                    COMPortButton lol = (COMPortButton)temp;
                    try
                    {
                        lol.logger.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                catch(Exception exception) { }
        }

        COMPortManager.getInstance().close();
    }

    public void windowDeactivated(WindowEvent windowevent)
    {
    }

    public void windowDeiconified(WindowEvent windowevent)
    {
    }

    public void windowIconified(WindowEvent windowevent)
    {
    }

    public void windowOpened(WindowEvent windowevent)
    {
    }

    public static void removeActionItem(String name)
    {
        JButton temp = (JButton)getInstance().actionButtons.get(name);
        getInstance().actionItems.remove(temp);
        getInstance().actionItems.revalidate();
    }

    private static void copyfile(File f1, File f2)
    {
        try
        {
            if(f2.getParentFile().exists())
                f2.createNewFile();
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);
            byte buf[] = new byte[1024];
            int len;
            while((len = in.read(buf)) > 0) 
                out.write(buf, 0, len);
            in.close();
            out.close();
            System.out.println("File copied.");
        }
        catch(FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
            System.exit(1);
        }
        catch(IOException e)
        {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    private static ReadConfig conf;
    final JPanel COMPortList = new JPanel();
    final JPanel infoSection = new JPanel();
    final JPanel actionItems = new JPanel();
    private static GUI instance;
    HashMap actionButtons;
}
