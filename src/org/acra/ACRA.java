package org.acra;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;
import org.acra.annotation.ReportsCrashes;
import org.acra.log.ACRALog;
import org.acra.log.AndroidLogDelegate;

public class ACRA
{
  public static final boolean DEV_LOGGING = false;
  public static final String LOG_TAG = ACRA.class.getSimpleName();
  public static final String PREF_ALWAYS_ACCEPT = "acra.alwaysaccept";
  public static final String PREF_DISABLE_ACRA = "acra.disable";
  public static final String PREF_ENABLE_ACRA = "acra.enable";
  public static final String PREF_ENABLE_DEVICE_ID = "acra.deviceid.enable";
  public static final String PREF_ENABLE_SYSTEM_LOGS = "acra.syslog.enable";
  public static final String PREF_LAST_VERSION_NR = "acra.lastVersionNr";
  public static final String PREF_USER_EMAIL_ADDRESS = "acra.user.email";
  private static ACRAConfiguration configProxy;
  private static ErrorReporter errorReporterSingleton;
  public static ACRALog log = new AndroidLogDelegate();
  private static Application mApplication;
  private static SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
  private static ReportsCrashes mReportsCrashes;
  
  static void checkCrashResources()
    throws ACRAConfigurationException
  {
    ACRAConfiguration localACRAConfiguration = getConfig();
    switch (2.$SwitchMap$org$acra$ReportingInteractionMode[localACRAConfiguration.mode().ordinal()])
    {
    case 1: 
      if (localACRAConfiguration.resToastText() == 0) {
        throw new ACRAConfigurationException("TOAST mode: you have to define the resToastText parameter in your application @ReportsCrashes() annotation.");
      }
      break;
    case 2: 
      if ((localACRAConfiguration.resNotifTickerText() == 0) || (localACRAConfiguration.resNotifTitle() == 0) || (localACRAConfiguration.resNotifText() == 0) || (localACRAConfiguration.resDialogText() == 0)) {
        throw new ACRAConfigurationException("NOTIFICATION mode: you have to define at least the resNotifTickerText, resNotifTitle, resNotifText, resDialogText parameters in your application @ReportsCrashes() annotation.");
      }
      break;
    case 3: 
      if (localACRAConfiguration.resDialogText() == 0) {
        break label119;
      }
    }
    return;
    label119:
    throw new ACRAConfigurationException("DIALOG mode: you have to define at least the resDialogText parameters in your application @ReportsCrashes() annotation.");
  }
  
  public static SharedPreferences getACRASharedPreferences()
  {
    Object localObject = getConfig();
    if ("".equals(((ReportsCrashes)localObject).sharedPreferencesName())) {
      localObject = PreferenceManager.getDefaultSharedPreferences(mApplication);
    } else {
      localObject = mApplication.getSharedPreferences(((ReportsCrashes)localObject).sharedPreferencesName(), ((ReportsCrashes)localObject).sharedPreferencesMode());
    }
    return localObject;
  }
  
  static Application getApplication()
  {
    return mApplication;
  }
  
  public static ACRAConfiguration getConfig()
  {
    if (configProxy == null)
    {
      if (mApplication == null) {
        log.w(LOG_TAG, "Calling ACRA.getConfig() before ACRA.init() gives you an empty configuration instance. You might prefer calling ACRA.getNewDefaultConfig(Application) to get an instance with default values taken from a @ReportsCrashes annotation.");
      }
      configProxy = getNewDefaultConfig(mApplication);
    }
    return configProxy;
  }
  
  public static ErrorReporter getErrorReporter()
  {
    if (errorReporterSingleton != null) {
      return errorReporterSingleton;
    }
    throw new IllegalStateException("Cannot access ErrorReporter before ACRA#init");
  }
  
  public static ACRAConfiguration getNewDefaultConfig(Application paramApplication)
  {
    ACRAConfiguration localACRAConfiguration;
    if (paramApplication == null) {
      localACRAConfiguration = new ACRAConfiguration(null);
    } else {
      localACRAConfiguration = new ACRAConfiguration((ReportsCrashes)paramApplication.getClass().getAnnotation(ReportsCrashes.class));
    }
    return localACRAConfiguration;
  }
  
  public static void init(Application paramApplication)
  {
    if (mApplication != null) {
      log.w(LOG_TAG, "ACRA#init called more than once. Won't do anything more.");
    }
    for (;;)
    {
      return;
      mApplication = paramApplication;
      mReportsCrashes = (ReportsCrashes)mApplication.getClass().getAnnotation(ReportsCrashes.class);
      if (mReportsCrashes == null)
      {
        log.e(LOG_TAG, "ACRA#init called but no ReportsCrashes annotation on Application " + mApplication.getPackageName());
        continue;
      }
      SharedPreferences localSharedPreferences = getACRASharedPreferences();
      try
      {
        checkCrashResources();
        log.d(LOG_TAG, "ACRA is enabled for " + mApplication.getPackageName() + ", intializing...");
        if (!shouldDisableACRA(localSharedPreferences)) {}
        int i;
        for (boolean bool = true;; i = 0)
        {
          ErrorReporter localErrorReporter = new ErrorReporter(mApplication, localSharedPreferences, bool);
          localErrorReporter.setDefaultReportSenders();
          errorReporterSingleton = localErrorReporter;
          mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener()
          {
            public void onSharedPreferenceChanged(SharedPreferences paramAnonymousSharedPreferences, String paramAnonymousString)
            {
              if (("acra.disable".equals(paramAnonymousString)) || ("acra.enable".equals(paramAnonymousString)))
              {
                boolean bool;
                if (ACRA.shouldDisableACRA(paramAnonymousSharedPreferences)) {
                  bool = false;
                } else {
                  bool = true;
                }
                ACRA.getErrorReporter().setEnabled(bool);
              }
            }
          };
          localSharedPreferences.registerOnSharedPreferenceChangeListener(mPrefListener);
          break;
        }
      }
      catch (ACRAConfigurationException localACRAConfigurationException)
      {
        for (;;)
        {
          log.w(LOG_TAG, "Error : ", localACRAConfigurationException);
        }
      }
    }
  }
  
  static boolean isDebuggable()
  {
    boolean bool = false;
    PackageManager localPackageManager = mApplication.getPackageManager();
    try
    {
      int i = localPackageManager.getApplicationInfo(mApplication.getPackageName(), 0).flags;
      if ((i & 0x2) > 0) {
        bool = true;
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      label32:
      break label32;
    }
    return bool;
  }
  
  public static void setConfig(ACRAConfiguration paramACRAConfiguration)
  {
    configProxy = paramACRAConfiguration;
  }
  
  public static void setLog(ACRALog paramACRALog)
  {
    log = paramACRALog;
  }
  
  private static boolean shouldDisableACRA(SharedPreferences paramSharedPreferences)
  {
    boolean bool2 = true;
    boolean bool1 = false;
    for (;;)
    {
      try
      {
        if (paramSharedPreferences.getBoolean("acra.enable", true)) {
          continue;
        }
        bool1 = paramSharedPreferences.getBoolean("acra.disable", bool2);
        bool1 = bool1;
      }
      catch (Exception localException)
      {
        continue;
      }
      return bool1;
      bool2 = false;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.ACRA
 * JD-Core Version:    0.7.0.1
 */