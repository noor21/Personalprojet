package org.acra;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.acra.collector.CrashReportData;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

final class SendWorker
  extends Thread
{
  private final boolean approvePendingReports;
  private final Context context;
  private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();
  private final List<ReportSender> reportSenders;
  private final boolean sendOnlySilentReports;
  
  public SendWorker(Context paramContext, List<ReportSender> paramList, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.context = paramContext;
    this.reportSenders = paramList;
    this.sendOnlySilentReports = paramBoolean1;
    this.approvePendingReports = paramBoolean2;
  }
  
  private void approvePendingReports()
  {
    Log.d(ACRA.LOG_TAG, "Mark all pending reports as approved.");
    String[] arrayOfString = new CrashReportFinder(this.context).getCrashReportFiles();
    int i = arrayOfString.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return;
      }
      Object localObject = arrayOfString[j];
      if (!this.fileNameParser.isApproved((String)localObject))
      {
        File localFile = new File(this.context.getFilesDir(), (String)localObject);
        localObject = ((String)localObject).replace(".stacktrace", "-approved.stacktrace");
        localObject = new File(this.context.getFilesDir(), (String)localObject);
        if (!localFile.renameTo((File)localObject)) {
          Log.e(ACRA.LOG_TAG, "Could not rename approved report from " + localFile + " to " + localObject);
        }
      }
    }
  }
  
  private void checkAndSendReports(Context paramContext, boolean paramBoolean)
  {
    Log.d(ACRA.LOG_TAG, "#checkAndSendReports - start");
    String[] arrayOfString = new CrashReportFinder(paramContext).getCrashReportFiles();
    Arrays.sort(arrayOfString);
    int k = 0;
    int j = arrayOfString.length;
    int i = 0;
    String str;
    if (i < j)
    {
      str = arrayOfString[i];
      if ((!paramBoolean) || (this.fileNameParser.isSilent(str))) {}
    }
    for (;;)
    {
      i++;
      break;
      if (k >= 5)
      {
        label78:
        Log.d(ACRA.LOG_TAG, "#checkAndSendReports - finish");
        return;
      }
      Log.i(ACRA.LOG_TAG, "Sending file " + str);
      try
      {
        sendCrashReport(new CrashReportPersister(paramContext).load(str));
        deleteFile(paramContext, str);
        k++;
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e(ACRA.LOG_TAG, "Failed to send crash reports for " + str, localRuntimeException);
        deleteFile(paramContext, str);
        break label78;
      }
      catch (IOException localIOException)
      {
        Log.e(ACRA.LOG_TAG, "Failed to load crash report for " + str, localIOException);
        deleteFile(paramContext, str);
        break label78;
      }
      catch (ReportSenderException localReportSenderException)
      {
        for (;;)
        {
          Log.e(ACRA.LOG_TAG, "Failed to send crash report for " + str, localReportSenderException);
        }
      }
    }
  }
  
  private void deleteFile(Context paramContext, String paramString)
  {
    if (!paramContext.deleteFile(paramString)) {
      Log.w(ACRA.LOG_TAG, "Could not delete error report : " + paramString);
    }
  }
  
  private void sendCrashReport(CrashReportData paramCrashReportData)
    throws ReportSenderException
  {
    if ((!ACRA.isDebuggable()) || (ACRA.getConfig().sendReportsInDevMode()))
    {
      int i = 0;
      Iterator localIterator = this.reportSenders.iterator();
      while (localIterator.hasNext())
      {
        ReportSender localReportSender = (ReportSender)localIterator.next();
        try
        {
          localReportSender.send(paramCrashReportData);
          i = 1;
        }
        catch (ReportSenderException localReportSenderException)
        {
          if (i == 0) {
            throw localReportSenderException;
          }
          Log.w(ACRA.LOG_TAG, "ReportSender of class " + localReportSender.getClass().getName() + " failed but other senders completed their task. ACRA will not send this report again.");
        }
      }
    }
  }
  
  public void run()
  {
    if (this.approvePendingReports) {
      approvePendingReports();
    }
    checkAndSendReports(this.context, this.sendOnlySilentReports);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.SendWorker
 * JD-Core Version:    0.7.0.1
 */