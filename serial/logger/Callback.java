// Decompiled by Jad v1.5.8e. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/kpdus/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   Callback.java

package serial.logger;

import java.io.IOException;

public interface Callback
{

    public abstract void execute(Object obj)
        throws IOException;
}
