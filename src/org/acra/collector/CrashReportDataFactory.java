package org.acra.collector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.annotation.ReportsCrashes;
import org.acra.util.Installation;
import org.acra.util.PackageManagerWrapper;
import org.acra.util.ReportUtils;

public final class CrashReportDataFactory
{
  private final Time appStartDate;
  private final Context context;
  private final Map<String, String> customParameters = new HashMap();
  private final String initialConfiguration;
  private final SharedPreferences prefs;
  
  public CrashReportDataFactory(Context paramContext, SharedPreferences paramSharedPreferences, Time paramTime, String paramString)
  {
    this.context = paramContext;
    this.prefs = paramSharedPreferences;
    this.appStartDate = paramTime;
    this.initialConfiguration = paramString;
  }
  
  private String createCustomInfoString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = this.customParameters.keySet().iterator();
    for (;;)
    {
      if (!localIterator.hasNext()) {
        return localStringBuilder.toString();
      }
      String str2 = (String)localIterator.next();
      String str1 = (String)this.customParameters.get(str2);
      localStringBuilder.append(str2);
      localStringBuilder.append(" = ");
      if (str1 != null) {
        str1 = str1.replaceAll("\n", "\\\\n");
      }
      localStringBuilder.append(str1);
      localStringBuilder.append("\n");
    }
  }
  
  private List<ReportField> getReportFields()
  {
    ACRAConfiguration localACRAConfiguration = ACRA.getConfig();
    ReportField[] arrayOfReportField = localACRAConfiguration.customReportContent();
    if (arrayOfReportField.length == 0)
    {
      if ((localACRAConfiguration.mailTo() != null) && (!"".equals(localACRAConfiguration.mailTo())))
      {
        Log.d(ACRA.LOG_TAG, "Using default Mail Report Fields");
        arrayOfReportField = ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS;
      }
      else
      {
        Log.d(ACRA.LOG_TAG, "Using default Report Fields");
        arrayOfReportField = ACRAConstants.DEFAULT_REPORT_FIELDS;
      }
    }
    else
    {
      Log.d(ACRA.LOG_TAG, "Using custom Report Fields");
      arrayOfReportField = arrayOfReportField;
    }
    return Arrays.asList(arrayOfReportField);
  }
  
  private String getStackTrace(Throwable paramThrowable)
  {
    Object localObject = new StringWriter();
    PrintWriter localPrintWriter = new PrintWriter((Writer)localObject);
    for (Throwable localThrowable = paramThrowable;; localThrowable = localThrowable.getCause())
    {
      if (localThrowable == null)
      {
        localObject = localObject.toString();
        localPrintWriter.close();
        return localObject;
      }
      localThrowable.printStackTrace(localPrintWriter);
    }
  }
  
  public CrashReportData createCrashData(Throwable paramThrowable, boolean paramBoolean, Thread paramThread)
  {
    CrashReportData localCrashReportData = new CrashReportData();
    for (;;)
    {
      try
      {
        List localList = getReportFields();
        localCrashReportData.put(ReportField.STACK_TRACE, getStackTrace(paramThrowable));
        localCrashReportData.put(ReportField.USER_APP_START_DATE, this.appStartDate.format3339(false));
        if (paramBoolean) {
          localCrashReportData.put(ReportField.IS_SILENT, "true");
        }
        if (localList.contains(ReportField.REPORT_ID)) {
          localCrashReportData.put(ReportField.REPORT_ID, UUID.randomUUID().toString());
        }
        if (localList.contains(ReportField.INSTALLATION_ID)) {
          localCrashReportData.put(ReportField.INSTALLATION_ID, Installation.id(this.context));
        }
        if (localList.contains(ReportField.INITIAL_CONFIGURATION)) {
          localCrashReportData.put(ReportField.INITIAL_CONFIGURATION, this.initialConfiguration);
        }
        if (localList.contains(ReportField.CRASH_CONFIGURATION)) {
          localCrashReportData.put(ReportField.CRASH_CONFIGURATION, ConfigurationCollector.collectConfiguration(this.context));
        }
        if ((!(paramThrowable instanceof OutOfMemoryError)) && (localList.contains(ReportField.DUMPSYS_MEMINFO))) {
          localCrashReportData.put(ReportField.DUMPSYS_MEMINFO, DumpSysCollector.collectMemInfo());
        }
        if (localList.contains(ReportField.PACKAGE_NAME)) {
          localCrashReportData.put(ReportField.PACKAGE_NAME, this.context.getPackageName());
        }
        if (localList.contains(ReportField.BUILD)) {
          localCrashReportData.put(ReportField.BUILD, ReflectionCollector.collectConstants(Build.class) + ReflectionCollector.collectConstants(Build.VERSION.class, "VERSION"));
        }
        if (localList.contains(ReportField.PHONE_MODEL)) {
          localCrashReportData.put(ReportField.PHONE_MODEL, Build.MODEL);
        }
        if (localList.contains(ReportField.ANDROID_VERSION)) {
          localCrashReportData.put(ReportField.ANDROID_VERSION, Build.VERSION.RELEASE);
        }
        if (localList.contains(ReportField.BRAND)) {
          localCrashReportData.put(ReportField.BRAND, Build.BRAND);
        }
        if (localList.contains(ReportField.PRODUCT)) {
          localCrashReportData.put(ReportField.PRODUCT, Build.PRODUCT);
        }
        if (localList.contains(ReportField.TOTAL_MEM_SIZE)) {
          localCrashReportData.put(ReportField.TOTAL_MEM_SIZE, Long.toString(ReportUtils.getTotalInternalMemorySize()));
        }
        if (localList.contains(ReportField.AVAILABLE_MEM_SIZE)) {
          localCrashReportData.put(ReportField.AVAILABLE_MEM_SIZE, Long.toString(ReportUtils.getAvailableInternalMemorySize()));
        }
        if (localList.contains(ReportField.FILE_PATH)) {
          localCrashReportData.put(ReportField.FILE_PATH, ReportUtils.getApplicationFilePath(this.context));
        }
        if (localList.contains(ReportField.DISPLAY)) {
          localCrashReportData.put(ReportField.DISPLAY, DisplayManagerCollector.collectDisplays(this.context));
        }
        if (localList.contains(ReportField.USER_CRASH_DATE))
        {
          localObject1 = new Time();
          ((Time)localObject1).setToNow();
          localCrashReportData.put(ReportField.USER_CRASH_DATE, ((Time)localObject1).format3339(false));
        }
        if (localList.contains(ReportField.CUSTOM_DATA)) {
          localCrashReportData.put(ReportField.CUSTOM_DATA, createCustomInfoString());
        }
        if (localList.contains(ReportField.USER_EMAIL)) {
          localCrashReportData.put(ReportField.USER_EMAIL, this.prefs.getString("acra.user.email", "N/A"));
        }
        if (localList.contains(ReportField.DEVICE_FEATURES)) {
          localCrashReportData.put(ReportField.DEVICE_FEATURES, DeviceFeaturesCollector.getFeatures(this.context));
        }
        if (localList.contains(ReportField.ENVIRONMENT)) {
          localCrashReportData.put(ReportField.ENVIRONMENT, ReflectionCollector.collectStaticGettersResults(Environment.class));
        }
        if (localList.contains(ReportField.SETTINGS_SYSTEM)) {
          localCrashReportData.put(ReportField.SETTINGS_SYSTEM, SettingsCollector.collectSystemSettings(this.context));
        }
        if (localList.contains(ReportField.SETTINGS_SECURE)) {
          localCrashReportData.put(ReportField.SETTINGS_SECURE, SettingsCollector.collectSecureSettings(this.context));
        }
        if (localList.contains(ReportField.SETTINGS_GLOBAL)) {
          localCrashReportData.put(ReportField.SETTINGS_GLOBAL, SettingsCollector.collectGlobalSettings(this.context));
        }
        if (localList.contains(ReportField.SHARED_PREFERENCES)) {
          localCrashReportData.put(ReportField.SHARED_PREFERENCES, SharedPreferencesCollector.collect(this.context));
        }
        Object localObject1 = new PackageManagerWrapper(this.context);
        localObject3 = ((PackageManagerWrapper)localObject1).getPackageInfo();
        if (localObject3 != null)
        {
          if (localList.contains(ReportField.APP_VERSION_CODE)) {
            localCrashReportData.put(ReportField.APP_VERSION_CODE, Integer.toString(((PackageInfo)localObject3).versionCode));
          }
          Object localObject2;
          if (localList.contains(ReportField.APP_VERSION_NAME))
          {
            localObject2 = ReportField.APP_VERSION_NAME;
            if (((PackageInfo)localObject3).versionName == null) {
              break label1359;
            }
            localObject3 = ((PackageInfo)localObject3).versionName;
            localCrashReportData.put((Enum)localObject2, localObject3);
          }
          if ((localList.contains(ReportField.DEVICE_ID)) && (this.prefs.getBoolean("acra.deviceid.enable", true)) && (((PackageManagerWrapper)localObject1).hasPermission("android.permission.READ_PHONE_STATE")))
          {
            localObject2 = ReportUtils.getDeviceId(this.context);
            if (localObject2 != null) {
              localCrashReportData.put(ReportField.DEVICE_ID, localObject2);
            }
          }
          if (((this.prefs.getBoolean("acra.syslog.enable", true)) && (((PackageManagerWrapper)localObject1).hasPermission("android.permission.READ_LOGS"))) || (Compatibility.getAPILevel() >= 16))
          {
            Log.i(ACRA.LOG_TAG, "READ_LOGS granted! ACRA can include LogCat and DropBox data.");
            if (localList.contains(ReportField.LOGCAT)) {
              localCrashReportData.put(ReportField.LOGCAT, LogCatCollector.collectLogCat(null));
            }
            if (localList.contains(ReportField.EVENTSLOG)) {
              localCrashReportData.put(ReportField.EVENTSLOG, LogCatCollector.collectLogCat("events"));
            }
            if (localList.contains(ReportField.RADIOLOG)) {
              localCrashReportData.put(ReportField.RADIOLOG, LogCatCollector.collectLogCat("radio"));
            }
            if (localList.contains(ReportField.DROPBOX)) {
              localCrashReportData.put(ReportField.DROPBOX, DropBoxCollector.read(this.context, ACRA.getConfig().additionalDropBoxTags()));
            }
            if (localList.contains(ReportField.APPLICATION_LOG)) {
              localCrashReportData.put(ReportField.APPLICATION_LOG, LogFileCollector.collectLogFile(this.context, ACRA.getConfig().applicationLogFile(), ACRA.getConfig().applicationLogFileLines()));
            }
            if (localList.contains(ReportField.MEDIA_CODEC_LIST)) {
              localCrashReportData.put(ReportField.MEDIA_CODEC_LIST, MediaCodecListCollector.collecMediaCodecList());
            }
            if (localList.contains(ReportField.THREAD_DETAILS)) {
              localCrashReportData.put(ReportField.THREAD_DETAILS, ThreadCollector.collect(paramThread));
            }
            if (!localList.contains(ReportField.USER_IP)) {
              break label1356;
            }
            localCrashReportData.put(ReportField.USER_IP, ReportUtils.getLocalIpAddress());
            break label1356;
          }
        }
        else
        {
          localCrashReportData.put(ReportField.APP_VERSION_NAME, "Package info unavailable");
          continue;
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        Log.e(ACRA.LOG_TAG, "Error while retrieving crash data", localRuntimeException);
        break label1356;
        Log.i(ACRA.LOG_TAG, "READ_LOGS not allowed. ACRA will not include LogCat and DropBox data.");
        continue;
      }
      catch (FileNotFoundException localFileNotFoundException)
      {
        Log.e(ACRA.LOG_TAG, "Error : application log file " + ACRA.getConfig().applicationLogFile() + " not found.", localFileNotFoundException);
      }
      catch (IOException localIOException)
      {
        Log.e(ACRA.LOG_TAG, "Error while reading application log file " + ACRA.getConfig().applicationLogFile() + ".", localIOException);
      }
      label1356:
      return localCrashReportData;
      label1359:
      Object localObject3 = "not set";
    }
  }
  
  public String getCustomData(String paramString)
  {
    return (String)this.customParameters.get(paramString);
  }
  
  public String putCustomData(String paramString1, String paramString2)
  {
    return (String)this.customParameters.put(paramString1, paramString2);
  }
  
  public String removeCustomData(String paramString)
  {
    return (String)this.customParameters.remove(paramString);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.CrashReportDataFactory
 * JD-Core Version:    0.7.0.1
 */