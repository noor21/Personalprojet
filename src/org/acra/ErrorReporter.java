package org.acra;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Looper;
import android.os.Process;
import android.text.format.Time;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.acra.annotation.ReportsCrashes;
import org.acra.collector.Compatibility;
import org.acra.collector.ConfigurationCollector;
import org.acra.collector.CrashReportData;
import org.acra.collector.CrashReportDataFactory;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat;
import org.acra.jraf.android.util.activitylifecyclecallbackscompat.ApplicationHelper;
import org.acra.log.ACRALog;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.GoogleFormSender;
import org.acra.sender.HttpSender;
import org.acra.sender.ReportSender;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.ToastSender;

public class ErrorReporter
  implements Thread.UncaughtExceptionHandler
{
  private static int mNotificationCounter = 0;
  private static boolean toastWaitEnded = true;
  private Thread brokenThread;
  private final CrashReportDataFactory crashReportDataFactory;
  private boolean enabled = false;
  private final CrashReportFileNameParser fileNameParser = new CrashReportFileNameParser();
  private transient Activity lastActivityCreated;
  private final Application mContext;
  private final Thread.UncaughtExceptionHandler mDfltExceptionHandler;
  private final List<ReportSender> mReportSenders = new ArrayList();
  private final SharedPreferences prefs;
  private Throwable unhandledThrowable;
  
  ErrorReporter(Application paramApplication, SharedPreferences paramSharedPreferences, boolean paramBoolean)
  {
    this.mContext = paramApplication;
    this.prefs = paramSharedPreferences;
    this.enabled = paramBoolean;
    String str = ConfigurationCollector.collectConfiguration(this.mContext);
    Time localTime = new Time();
    localTime.setToNow();
    if (Compatibility.getAPILevel() >= 14) {
      ApplicationHelper.registerActivityLifecycleCallbacks(paramApplication, new ActivityLifecycleCallbacksCompat()
      {
        public void onActivityCreated(Activity paramAnonymousActivity, Bundle paramAnonymousBundle)
        {
          if (!(paramAnonymousActivity instanceof CrashReportDialog)) {
            ErrorReporter.access$002(ErrorReporter.this, paramAnonymousActivity);
          }
        }
        
        public void onActivityDestroyed(Activity paramAnonymousActivity) {}
        
        public void onActivityPaused(Activity paramAnonymousActivity) {}
        
        public void onActivityResumed(Activity paramAnonymousActivity) {}
        
        public void onActivitySaveInstanceState(Activity paramAnonymousActivity, Bundle paramAnonymousBundle) {}
        
        public void onActivityStarted(Activity paramAnonymousActivity) {}
        
        public void onActivityStopped(Activity paramAnonymousActivity) {}
      });
    }
    this.crashReportDataFactory = new CrashReportDataFactory(this.mContext, paramSharedPreferences, localTime, str);
    this.mDfltExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
    Thread.setDefaultUncaughtExceptionHandler(this);
    checkReportsOnApplicationStart();
  }
  
  private boolean containsOnlySilentOrApprovedReports(String[] paramArrayOfString)
  {
    int k = paramArrayOfString.length;
    for (int j = 0;; j++)
    {
      if (j >= k)
      {
        int i = 1;
        return ???;
      }
      String str = paramArrayOfString[j];
      if (!this.fileNameParser.isApproved(str)) {
        break;
      }
    }
    boolean bool = false;
    return bool;
  }
  
  private void deletePendingReports(boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    String[] arrayOfString = new CrashReportFinder(this.mContext).getCrashReportFiles();
    Arrays.sort(arrayOfString);
    if (arrayOfString != null) {}
    for (int i = 0;; i++)
    {
      if (i >= arrayOfString.length - paramInt) {
        return;
      }
      String str = arrayOfString[i];
      boolean bool = this.fileNameParser.isApproved(str);
      if (((bool) && (paramBoolean1)) || ((!bool) && (paramBoolean2)))
      {
        File localFile = new File(this.mContext.getFilesDir(), str);
        ACRA.log.d(ACRA.LOG_TAG, "Deleting file " + str);
        if (!localFile.delete()) {
          Log.e(ACRA.LOG_TAG, "Could not delete report : " + localFile);
        }
      }
    }
  }
  
  private void endApplication()
  {
    if ((ACRA.getConfig().mode() != ReportingInteractionMode.SILENT) && ((ACRA.getConfig().mode() != ReportingInteractionMode.TOAST) || (!ACRA.getConfig().forceCloseDialogAfterToast())))
    {
      Log.e(ACRA.LOG_TAG, this.mContext.getPackageName() + " fatal error : " + this.unhandledThrowable.getMessage(), this.unhandledThrowable);
      if (this.lastActivityCreated != null)
      {
        Log.i(ACRA.LOG_TAG, "Finishing the last Activity prior to killing the Process");
        this.lastActivityCreated.finish();
        Log.i(ACRA.LOG_TAG, "Finished " + this.lastActivityCreated.getClass());
        this.lastActivityCreated = null;
      }
      Process.killProcess(Process.myPid());
      System.exit(10);
    }
    else
    {
      this.mDfltExceptionHandler.uncaughtException(this.brokenThread, this.unhandledThrowable);
    }
  }
  
  public static ErrorReporter getInstance()
  {
    return ACRA.getErrorReporter();
  }
  
  private String getLatestNonSilentReport(String[] paramArrayOfString)
  {
    String str;
    if ((paramArrayOfString == null) || (paramArrayOfString.length <= 0))
    {
      Object localObject = null;
    }
    else
    {
      for (int i = -1 + paramArrayOfString.length;; str--)
      {
        if (i < 0) {
          return paramArrayOfString[(-1 + paramArrayOfString.length)];
        }
        if (!this.fileNameParser.isSilent(paramArrayOfString[str])) {
          break;
        }
      }
      str = paramArrayOfString[str];
    }
    return str;
  }
  
  private String getReportFileName(CrashReportData paramCrashReportData)
  {
    Object localObject = new Time();
    ((Time)localObject).setToNow();
    long l = ((Time)localObject).toMillis(false);
    String str = paramCrashReportData.getProperty(ReportField.IS_SILENT);
    localObject = new StringBuilder().append("").append(l);
    if (str == null) {
      str = "";
    } else {
      str = ACRAConstants.SILENT_SUFFIX;
    }
    return str + ".stacktrace";
  }
  
  private void handleException(Throwable paramThrowable, ReportingInteractionMode paramReportingInteractionMode, boolean paramBoolean1, final boolean paramBoolean2)
  {
    final boolean bool1 = true;
    if (this.enabled)
    {
      boolean bool2 = false;
      if (paramReportingInteractionMode != null)
      {
        if ((paramReportingInteractionMode == ReportingInteractionMode.SILENT) && (ACRA.getConfig().mode() != ReportingInteractionMode.SILENT)) {
          bool2 = true;
        }
      }
      else {
        paramReportingInteractionMode = ACRA.getConfig().mode();
      }
      if (paramThrowable == null) {
        paramThrowable = new Exception("Report requested by developer");
      }
      int i;
      if ((paramReportingInteractionMode != ReportingInteractionMode.TOAST) && ((ACRA.getConfig().resToastText() == 0) || ((paramReportingInteractionMode != ReportingInteractionMode.NOTIFICATION) && (paramReportingInteractionMode != ReportingInteractionMode.DIALOG)))) {
        i = 0;
      } else {
        i = bool1;
      }
      if (i != 0) {
        new Thread()
        {
          public void run()
          {
            Looper.prepare();
            ToastSender.sendToast(ErrorReporter.this.mContext, ACRA.getConfig().resToastText(), 1);
            Looper.loop();
          }
        }.start();
      }
      Object localObject2 = this.crashReportDataFactory.createCrashData(paramThrowable, paramBoolean1, this.brokenThread);
      final String str = getReportFileName((CrashReportData)localObject2);
      saveCrashReportFile(str, (CrashReportData)localObject2);
      localObject2 = null;
      if ((paramReportingInteractionMode != ReportingInteractionMode.SILENT) && (paramReportingInteractionMode != ReportingInteractionMode.TOAST) && (!this.prefs.getBoolean("acra.alwaysaccept", false)))
      {
        if (paramReportingInteractionMode == ReportingInteractionMode.NOTIFICATION) {
          Log.d(ACRA.LOG_TAG, "Notification will be created on application start.");
        }
      }
      else
      {
        Log.d(ACRA.LOG_TAG, "About to start ReportSenderWorker from #handleException");
        localObject2 = startSendingReports(bool2, bool1);
      }
      if (i != 0)
      {
        toastWaitEnded = false;
        new Thread()
        {
          public void run()
          {
            Time localTime2 = new Time();
            Time localTime1 = new Time();
            localTime2.setToNow();
            long l1 = localTime2.toMillis(false);
            long l2 = 0L;
            for (;;)
            {
              if (l2 < 3000L) {
                try
                {
                  Thread.sleep(3000L);
                  localTime1.setToNow();
                  l2 = localTime1.toMillis(false) - l1;
                }
                catch (InterruptedException localInterruptedException)
                {
                  for (;;)
                  {
                    Log.d(ACRA.LOG_TAG, "Interrupted while waiting for Toast to end.", localInterruptedException);
                  }
                }
              }
            }
            ErrorReporter.access$202(true);
          }
        }.start();
      }
      final Object localObject1 = localObject2;
      if ((paramReportingInteractionMode != ReportingInteractionMode.DIALOG) || (this.prefs.getBoolean("acra.alwaysaccept", false))) {
        bool1 = false;
      }
      new Thread()
      {
        public void run()
        {
          Log.d(ACRA.LOG_TAG, "Waiting for Toast + worker...");
          while ((!ErrorReporter.toastWaitEnded) || ((localObject1 != null) && (localObject1.isAlive()))) {
            try
            {
              Thread.sleep(100L);
            }
            catch (InterruptedException localInterruptedException)
            {
              Log.e(ACRA.LOG_TAG, "Error : ", localInterruptedException);
            }
          }
          if (bool1)
          {
            Log.d(ACRA.LOG_TAG, "About to create DIALOG from #handleException");
            ErrorReporter.this.notifyDialog(str);
          }
          Log.d(ACRA.LOG_TAG, "Wait for Toast + worker ended. Kill Application ? " + paramBoolean2);
          if (paramBoolean2) {
            ErrorReporter.this.endApplication();
          }
        }
      }.start();
    }
  }
  
  private void notifySendReport(String paramString)
  {
    NotificationManager localNotificationManager = (NotificationManager)this.mContext.getSystemService("notification");
    Object localObject2 = ACRA.getConfig();
    Notification localNotification = new Notification(((ReportsCrashes)localObject2).resNotifIcon(), this.mContext.getText(((ReportsCrashes)localObject2).resNotifTickerText()), System.currentTimeMillis());
    Object localObject1 = this.mContext.getText(((ReportsCrashes)localObject2).resNotifTitle());
    localObject2 = this.mContext.getText(((ReportsCrashes)localObject2).resNotifText());
    Intent localIntent = new Intent(this.mContext, CrashReportDialog.class);
    Log.d(ACRA.LOG_TAG, "Creating Notification for " + paramString);
    localIntent.putExtra("REPORT_FILE_NAME", paramString);
    Object localObject3 = this.mContext;
    int i = mNotificationCounter;
    mNotificationCounter = i + 1;
    localObject3 = PendingIntent.getActivity((Context)localObject3, i, localIntent, 134217728);
    localNotification.setLatestEventInfo(this.mContext, (CharSequence)localObject1, (CharSequence)localObject2, (PendingIntent)localObject3);
    localObject1 = new Intent(this.mContext, CrashReportDialog.class);
    ((Intent)localObject1).putExtra("FORCE_CANCEL", true);
    localNotification.deleteIntent = PendingIntent.getActivity(this.mContext, -1, (Intent)localObject1, 0);
    localNotificationManager.notify(666, localNotification);
  }
  
  private void saveCrashReportFile(String paramString, CrashReportData paramCrashReportData)
  {
    try
    {
      Log.d(ACRA.LOG_TAG, "Writing crash report file " + paramString + ".");
      new CrashReportPersister(this.mContext).store(paramCrashReportData, paramString);
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Log.e(ACRA.LOG_TAG, "An error occurred while writing the report file...", localException);
      }
    }
  }
  
  @Deprecated
  public void addCustomData(String paramString1, String paramString2)
  {
    this.crashReportDataFactory.putCustomData(paramString1, paramString2);
  }
  
  public void addReportSender(ReportSender paramReportSender)
  {
    this.mReportSenders.add(paramReportSender);
  }
  
  public void checkReportsOnApplicationStart()
  {
    long l = this.prefs.getInt("acra.lastVersionNr", 0);
    Object localObject1 = new PackageManagerWrapper(this.mContext).getPackageInfo();
    int i;
    if ((localObject1 == null) || (((PackageInfo)localObject1).versionCode <= l)) {
      i = 0;
    } else {
      i = 1;
    }
    if (i != 0)
    {
      if (ACRA.getConfig().deleteOldUnsentReportsOnApplicationStart()) {
        deletePendingReports();
      }
      localObject2 = this.prefs.edit();
      ((SharedPreferences.Editor)localObject2).putInt("acra.lastVersionNr", ((PackageInfo)localObject1).versionCode);
      ((SharedPreferences.Editor)localObject2).commit();
    }
    if (((ACRA.getConfig().mode() == ReportingInteractionMode.NOTIFICATION) || (ACRA.getConfig().mode() == ReportingInteractionMode.DIALOG)) && (ACRA.getConfig().deleteUnapprovedReportsOnApplicationStart())) {
      deletePendingNonApprovedReports(true);
    }
    Object localObject2 = new CrashReportFinder(this.mContext);
    localObject1 = ((CrashReportFinder)localObject2).getCrashReportFiles();
    if ((localObject1 != null) && (localObject1.length > 0))
    {
      localObject1 = ACRA.getConfig().mode();
      String[] arrayOfString = ((CrashReportFinder)localObject2).getCrashReportFiles();
      boolean bool = containsOnlySilentOrApprovedReports(arrayOfString);
      if ((localObject1 != ReportingInteractionMode.SILENT) && (localObject1 != ReportingInteractionMode.TOAST) && ((!bool) || ((localObject1 != ReportingInteractionMode.NOTIFICATION) && (localObject1 != ReportingInteractionMode.DIALOG))))
      {
        if (ACRA.getConfig().mode() != ReportingInteractionMode.NOTIFICATION)
        {
          if (ACRA.getConfig().mode() != ReportingInteractionMode.DIALOG) {}
        }
        else {
          notifySendReport(getLatestNonSilentReport(arrayOfString));
        }
      }
      else
      {
        if ((localObject1 == ReportingInteractionMode.TOAST) && (!bool)) {
          ToastSender.sendToast(this.mContext, ACRA.getConfig().resToastText(), 1);
        }
        Log.v(ACRA.LOG_TAG, "About to start ReportSenderWorker from #checkReportOnApplicationStart");
        startSendingReports(false, false);
      }
    }
  }
  
  void deletePendingNonApprovedReports(boolean paramBoolean)
  {
    int i;
    if (!paramBoolean) {
      i = 0;
    } else {
      i = 1;
    }
    deletePendingReports(false, true, i);
  }
  
  void deletePendingReports()
  {
    deletePendingReports(true, true, 0);
  }
  
  public String getCustomData(String paramString)
  {
    return this.crashReportDataFactory.getCustomData(paramString);
  }
  
  public void handleException(Throwable paramThrowable)
  {
    handleException(paramThrowable, ACRA.getConfig().mode(), false, false);
  }
  
  public void handleException(Throwable paramThrowable, boolean paramBoolean)
  {
    handleException(paramThrowable, ACRA.getConfig().mode(), false, paramBoolean);
  }
  
  public void handleSilentException(Throwable paramThrowable)
  {
    if (!this.enabled)
    {
      Log.d(ACRA.LOG_TAG, "ACRA is disabled. Silent report not sent.");
    }
    else
    {
      handleException(paramThrowable, ReportingInteractionMode.SILENT, true, false);
      Log.d(ACRA.LOG_TAG, "ACRA sent Silent report.");
    }
  }
  
  void notifyDialog(String paramString)
  {
    Log.d(ACRA.LOG_TAG, "Creating Dialog for " + paramString);
    Intent localIntent = new Intent(this.mContext, CrashReportDialog.class);
    localIntent.putExtra("REPORT_FILE_NAME", paramString);
    localIntent.setFlags(268435456);
    this.mContext.startActivity(localIntent);
  }
  
  public String putCustomData(String paramString1, String paramString2)
  {
    return this.crashReportDataFactory.putCustomData(paramString1, paramString2);
  }
  
  public void removeAllReportSenders()
  {
    this.mReportSenders.clear();
  }
  
  public String removeCustomData(String paramString)
  {
    return this.crashReportDataFactory.removeCustomData(paramString);
  }
  
  public void removeReportSender(ReportSender paramReportSender)
  {
    this.mReportSenders.remove(paramReportSender);
  }
  
  public void removeReportSenders(Class<?> paramClass)
  {
    Iterator localIterator;
    if (ReportSender.class.isAssignableFrom(paramClass)) {
      localIterator = this.mReportSenders.iterator();
    }
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return;
      }
      ReportSender localReportSender = (ReportSender)localIterator.next();
      if (paramClass.isInstance(localReportSender)) {
        this.mReportSenders.remove(localReportSender);
      }
    }
  }
  
  public void setDefaultReportSenders()
  {
    ACRAConfiguration localACRAConfiguration = ACRA.getConfig();
    Application localApplication = ACRA.getApplication();
    removeAllReportSenders();
    if ("".equals(localACRAConfiguration.mailTo()))
    {
      if (new PackageManagerWrapper(localApplication).hasPermission("android.permission.INTERNET"))
      {
        if ((localACRAConfiguration.formUri() == null) || ("".equals(localACRAConfiguration.formUri())))
        {
          if ((localACRAConfiguration.formKey() != null) && (!"".equals(localACRAConfiguration.formKey().trim()))) {
            addReportSender(new GoogleFormSender());
          }
        }
        else {
          setReportSender(new HttpSender(ACRA.getConfig().httpMethod(), ACRA.getConfig().reportType(), null));
        }
      }
      else {
        Log.e(ACRA.LOG_TAG, localApplication.getPackageName() + " should be granted permission " + "android.permission.INTERNET" + " if you want your crash reports to be sent. If you don't want to add this permission to your application you can also enable sending reports by email. If this is your will then provide your email address in @ReportsCrashes(mailTo=\"your.account@domain.com\"");
      }
    }
    else
    {
      Log.w(ACRA.LOG_TAG, localApplication.getPackageName() + " reports will be sent by email (if accepted by user).");
      setReportSender(new EmailIntentSender(localApplication));
    }
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    String str2 = ACRA.LOG_TAG;
    StringBuilder localStringBuilder = new StringBuilder().append("ACRA is ");
    String str1;
    if (!paramBoolean) {
      str1 = "disabled";
    } else {
      str1 = "enabled";
    }
    Log.i(str2, str1 + " for " + this.mContext.getPackageName());
    this.enabled = paramBoolean;
  }
  
  public void setReportSender(ReportSender paramReportSender)
  {
    removeAllReportSenders();
    addReportSender(paramReportSender);
  }
  
  SendWorker startSendingReports(boolean paramBoolean1, boolean paramBoolean2)
  {
    SendWorker localSendWorker = new SendWorker(this.mContext, this.mReportSenders, paramBoolean1, paramBoolean2);
    localSendWorker.start();
    return localSendWorker;
  }
  
  public void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    try
    {
      if (!this.enabled) {
        if (this.mDfltExceptionHandler != null)
        {
          Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - forwarding uncaught Exception on to default ExceptionHandler");
          this.mDfltExceptionHandler.uncaughtException(paramThread, paramThrowable);
        }
        else
        {
          Log.e(ACRA.LOG_TAG, "ACRA is disabled for " + this.mContext.getPackageName() + " - no default ExceptionHandler");
        }
      }
    }
    catch (Throwable localThrowable)
    {
      if (this.mDfltExceptionHandler != null)
      {
        this.mDfltExceptionHandler.uncaughtException(paramThread, paramThrowable);
        return;
        this.brokenThread = paramThread;
        this.unhandledThrowable = paramThrowable;
        Log.e(ACRA.LOG_TAG, "ACRA caught a " + paramThrowable.getClass().getSimpleName() + " exception for " + this.mContext.getPackageName() + ". Building report.");
        handleException(paramThrowable, ACRA.getConfig().mode(), false, true);
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.ErrorReporter
 * JD-Core Version:    0.7.0.1
 */