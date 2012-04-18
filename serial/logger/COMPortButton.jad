// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   COMPortButton.java

package serial.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

// Referenced classes of package serial.logger:
//            GPS_Logger, GPS_Replayer

public class COMPortButton extends JButton
    implements ActionListener
{

    public String getName()
    {
        return pName;
    }

    public COMPortButton(String text, String portName, JPanel par, JPanel info)
    {
        super(text);
        infoPanel = info;
        parent = par;
        pName = portName;
        logger = new GPS_Logger(pName, pName);
        replayer = new GPS_Replayer(pName);
        addActionListener(this);
        update.start();
    }

    public void actionPerformed(ActionEvent e)
    {
        infoPanel.removeAll();
        infoPanel.add(new JLabel(pName));
        JButton log = new JButton("Log");
        JButton replay = new JButton("Replay");
        try
        {
            logger.connect();
            log.addActionListener(logger);
            replay.addActionListener(replayer);
        }
        catch(IOException e1)
        {
            e1.printStackTrace();
        }
        infoPanel.add(log);
        infoPanel.add(replay);
        infoPanel.revalidate();
    }

    final String pName;
    final JButton this_ = this;
    final Timer update = new Timer(1000, new ActionListener() {

        public void actionPerformed(ActionEvent actionevent)
        {
        }

        final COMPortButton this$0;

            
            {
                this$0 = COMPortButton.this;
                super();
            }
    });
    final JPanel infoPanel;
    final JPanel parent;
    final GPS_Logger logger;
    final GPS_Replayer replayer;
}
