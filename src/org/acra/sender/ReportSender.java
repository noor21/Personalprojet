package org.acra.sender;

import org.acra.collector.CrashReportData;

public abstract interface ReportSender
{
  public abstract void send(CrashReportData paramCrashReportData)
    throws ReportSenderException;
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.sender.ReportSender
 * JD-Core Version:    0.7.0.1
 */