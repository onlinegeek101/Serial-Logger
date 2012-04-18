// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   JCountDown.java

package serial.logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.*;

public class JCountDown extends JLabel
    implements ActionListener
{

    public JCountDown(int time_in, int id, JButton but)
    {
        button = but;
        if(items == null)
            items = new HashMap();
        title = button.getText();
        time = time_in;
        button.setText((new StringBuilder(String.valueOf(title))).append(" ").append(getTime(time)).toString());
        timer = new Timer(1000, this);
        items.put(new Integer(id), this);
    }

    public void start_down()
    {
    }

    public void down()
    {
        time--;
        button.setText((new StringBuilder(String.valueOf(title))).append(" ").append(getTime(time)).toString());
    }

    public String getTime(int time)
    {
        int hour = time / 3600;
        int min = (time / 60) % 60;
        int sec = time % 60;
        return (new StringBuilder(String.valueOf(hour))).append(":").append(min).append(":").append(sec).toString();
    }

    public void pause()
    {
        timer.stop();
    }

    public void actionPerformed(ActionEvent e)
    {
        time--;
        button.setText((new StringBuilder(String.valueOf(title))).append(" ").append(getTime(time)).toString());
    }

    public static HashMap items;
    Timer timer;
    String title;
    JButton button;
    int time;
}
