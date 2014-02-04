package org.acra;

import java.lang.annotation.Annotation;
import java.util.Map;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;

public class ACRAConfiguration
  implements ReportsCrashes
{
  private String[] mAdditionalDropboxTags = null;
  private String[] mAdditionalSharedPreferences = null;
  private String mApplicationLogFile = null;
  private Integer mApplicationLogFileLines = null;
  private Integer mConnectionTimeout = null;
  private ReportField[] mCustomReportContent = null;
  private Boolean mDeleteOldUnsentReportsOnApplicationStart = null;
  private Boolean mDeleteUnapprovedReportsOnApplicationStart = null;
  private Boolean mDisableSSLCertValidation = null;
  private Integer mDropboxCollectionMinutes = null;
  private String[] mExcludeMatchingSettingsKeys = null;
  private String[] mExcludeMatchingSharedPreferencesKeys = null;
  private Boolean mForceCloseDialogAfterToast = null;
  private String mFormKey = null;
  private String mFormUri = null;
  private String mFormUriBasicAuthLogin = null;
  private String mFormUriBasicAuthPassword = null;
  private String mGoogleFormUrlFormat = null;
  private Map<String, String> mHttpHeaders;
  private HttpSender.Method mHttpMethod = null;
  private Boolean mIncludeDropboxSystemTags = null;
  private String[] mLogcatArguments = null;
  private Boolean mLogcatFilterByPid = null;
  private String mMailTo = null;
  private Integer mMaxNumberOfRequestRetries = null;
  private ReportingInteractionMode mMode = null;
  private HttpSender.Type mReportType = null;
  private ReportsCrashes mReportsCrashes = null;
  private Integer mResDialogCommentPrompt = null;
  private Integer mResDialogEmailPrompt = null;
  private Integer mResDialogIcon = null;
  private Integer mResDialogOkToast = null;
  private Integer mResDialogText = null;
  private Integer mResDialogTitle = null;
  private Integer mResNotifIcon = null;
  private Integer mResNotifText = null;
  private Integer mResNotifTickerText = null;
  private Integer mResNotifTitle = null;
  private Integer mResToastText = null;
  private Boolean mSendReportsInDevMode = null;
  private Integer mSharedPreferenceMode = null;
  private String mSharedPreferenceName = null;
  private Integer mSocketTimeout = null;
  
  public ACRAConfiguration(ReportsCrashes paramReportsCrashes)
  {
    this.mReportsCrashes = paramReportsCrashes;
  }
  
  public static boolean isNull(String paramString)
  {
    boolean bool;
    if ((paramString != null) && (!"ACRA-NULL-STRING".equals(paramString))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String[] additionalDropBoxTags()
  {
    String[] arrayOfString;
    if (this.mAdditionalDropboxTags == null)
    {
      if (this.mReportsCrashes == null) {
        arrayOfString = new String[0];
      } else {
        arrayOfString = this.mReportsCrashes.additionalDropBoxTags();
      }
    }
    else {
      arrayOfString = this.mAdditionalDropboxTags;
    }
    return arrayOfString;
  }
  
  public String[] additionalSharedPreferences()
  {
    String[] arrayOfString;
    if (this.mAdditionalSharedPreferences == null)
    {
      if (this.mReportsCrashes == null) {
        arrayOfString = new String[0];
      } else {
        arrayOfString = this.mReportsCrashes.additionalSharedPreferences();
      }
    }
    else {
      arrayOfString = this.mAdditionalSharedPreferences;
    }
    return arrayOfString;
  }
  
  public Class<? extends Annotation> annotationType()
  {
    return this.mReportsCrashes.annotationType();
  }
  
  public String applicationLogFile()
  {
    String str;
    if (this.mApplicationLogFile == null)
    {
      if (this.mReportsCrashes == null) {
        str = "";
      } else {
        str = this.mReportsCrashes.applicationLogFile();
      }
    }
    else {
      str = this.mApplicationLogFile;
    }
    return str;
  }
  
  public int applicationLogFileLines()
  {
    int i;
    if (this.mApplicationLogFileLines == null)
    {
      if (this.mReportsCrashes == null) {
        i = 100;
      } else {
        i = this.mReportsCrashes.applicationLogFileLines();
      }
    }
    else {
      i = this.mApplicationLogFileLines.intValue();
    }
    return i;
  }
  
  public int connectionTimeout()
  {
    int i;
    if (this.mConnectionTimeout == null)
    {
      if (this.mReportsCrashes == null) {
        i = 3000;
      } else {
        i = this.mReportsCrashes.connectionTimeout();
      }
    }
    else {
      i = this.mConnectionTimeout.intValue();
    }
    return i;
  }
  
  public ReportField[] customReportContent()
  {
    ReportField[] arrayOfReportField;
    if (this.mCustomReportContent == null)
    {
      if (this.mReportsCrashes == null) {
        arrayOfReportField = new ReportField[0];
      } else {
        arrayOfReportField = this.mReportsCrashes.customReportContent();
      }
    }
    else {
      arrayOfReportField = this.mCustomReportContent;
    }
    return arrayOfReportField;
  }
  
  public boolean deleteOldUnsentReportsOnApplicationStart()
  {
    boolean bool;
    if (this.mDeleteOldUnsentReportsOnApplicationStart == null)
    {
      if (this.mReportsCrashes == null) {
        bool = true;
      } else {
        bool = this.mReportsCrashes.deleteOldUnsentReportsOnApplicationStart();
      }
    }
    else {
      bool = this.mDeleteOldUnsentReportsOnApplicationStart.booleanValue();
    }
    return bool;
  }
  
  public boolean deleteUnapprovedReportsOnApplicationStart()
  {
    boolean bool;
    if (this.mDeleteUnapprovedReportsOnApplicationStart == null)
    {
      if (this.mReportsCrashes == null) {
        bool = true;
      } else {
        bool = this.mReportsCrashes.deleteUnapprovedReportsOnApplicationStart();
      }
    }
    else {
      bool = this.mDeleteUnapprovedReportsOnApplicationStart.booleanValue();
    }
    return bool;
  }
  
  public boolean disableSSLCertValidation()
  {
    boolean bool;
    if (this.mDisableSSLCertValidation == null)
    {
      if (this.mReportsCrashes == null) {
        bool = false;
      } else {
        bool = this.mReportsCrashes.disableSSLCertValidation();
      }
    }
    else {
      bool = this.mDisableSSLCertValidation.booleanValue();
    }
    return bool;
  }
  
  public int dropboxCollectionMinutes()
  {
    int i;
    if (this.mDropboxCollectionMinutes == null)
    {
      if (this.mReportsCrashes == null) {
        i = 5;
      } else {
        i = this.mReportsCrashes.dropboxCollectionMinutes();
      }
    }
    else {
      i = this.mDropboxCollectionMinutes.intValue();
    }
    return i;
  }
  
  public String[] excludeMatchingSettingsKeys()
  {
    String[] arrayOfString;
    if (this.mExcludeMatchingSettingsKeys == null)
    {
      if (this.mReportsCrashes == null) {
        arrayOfString = new String[0];
      } else {
        arrayOfString = this.mReportsCrashes.excludeMatchingSettingsKeys();
      }
    }
    else {
      arrayOfString = this.mExcludeMatchingSettingsKeys;
    }
    return arrayOfString;
  }
  
  public String[] excludeMatchingSharedPreferencesKeys()
  {
    String[] arrayOfString;
    if (this.mExcludeMatchingSharedPreferencesKeys == null)
    {
      if (this.mReportsCrashes == null) {
        arrayOfString = new String[0];
      } else {
        arrayOfString = this.mReportsCrashes.excludeMatchingSharedPreferencesKeys();
      }
    }
    else {
      arrayOfString = this.mExcludeMatchingSharedPreferencesKeys;
    }
    return arrayOfString;
  }
  
  public boolean forceCloseDialogAfterToast()
  {
    boolean bool;
    if (this.mForceCloseDialogAfterToast == null)
    {
      if (this.mReportsCrashes == null) {
        bool = false;
      } else {
        bool = this.mReportsCrashes.forceCloseDialogAfterToast();
      }
    }
    else {
      bool = this.mForceCloseDialogAfterToast.booleanValue();
    }
    return bool;
  }
  
  public String formKey()
  {
    String str;
    if (this.mFormKey == null)
    {
      if (this.mReportsCrashes == null) {
        str = "";
      } else {
        str = this.mReportsCrashes.formKey();
      }
    }
    else {
      str = this.mFormKey;
    }
    return str;
  }
  
  public String formUri()
  {
    String str;
    if (this.mFormUri == null)
    {
      if (this.mReportsCrashes == null) {
        str = "";
      } else {
        str = this.mReportsCrashes.formUri();
      }
    }
    else {
      str = this.mFormUri;
    }
    return str;
  }
  
  public String formUriBasicAuthLogin()
  {
    String str;
    if (this.mFormUriBasicAuthLogin == null)
    {
      if (this.mReportsCrashes == null) {
        str = "ACRA-NULL-STRING";
      } else {
        str = this.mReportsCrashes.formUriBasicAuthLogin();
      }
    }
    else {
      str = this.mFormUriBasicAuthLogin;
    }
    return str;
  }
  
  public String formUriBasicAuthPassword()
  {
    String str;
    if (this.mFormUriBasicAuthPassword == null)
    {
      if (this.mReportsCrashes == null) {
        str = "ACRA-NULL-STRING";
      } else {
        str = this.mReportsCrashes.formUriBasicAuthPassword();
      }
    }
    else {
      str = this.mFormUriBasicAuthPassword;
    }
    return str;
  }
  
  public Map<String, String> getHttpHeaders()
  {
    return this.mHttpHeaders;
  }
  
  public String googleFormUrlFormat()
  {
    String str;
    if (this.mGoogleFormUrlFormat == null)
    {
      if (this.mReportsCrashes == null) {
        str = "https://docs.google.com/spreadsheet/formResponse?formkey=%s&ifq";
      } else {
        str = this.mReportsCrashes.googleFormUrlFormat();
      }
    }
    else {
      str = this.mGoogleFormUrlFormat;
    }
    return str;
  }
  
  public HttpSender.Method httpMethod()
  {
    HttpSender.Method localMethod;
    if (this.mHttpMethod == null)
    {
      if (this.mReportsCrashes == null) {
        localMethod = HttpSender.Method.POST;
      } else {
        localMethod = this.mReportsCrashes.httpMethod();
      }
    }
    else {
      localMethod = this.mHttpMethod;
    }
    return localMethod;
  }
  
  public boolean includeDropBoxSystemTags()
  {
    boolean bool;
    if (this.mIncludeDropboxSystemTags == null)
    {
      if (this.mReportsCrashes == null) {
        bool = false;
      } else {
        bool = this.mReportsCrashes.includeDropBoxSystemTags();
      }
    }
    else {
      bool = this.mIncludeDropboxSystemTags.booleanValue();
    }
    return bool;
  }
  
  public String[] logcatArguments()
  {
    String[] arrayOfString;
    if (this.mLogcatArguments == null)
    {
      if (this.mReportsCrashes == null)
      {
        arrayOfString = new String[4];
        arrayOfString[0] = "-t";
        arrayOfString[1] = Integer.toString(100);
        arrayOfString[2] = "-v";
        arrayOfString[3] = "time";
      }
      else
      {
        arrayOfString = this.mReportsCrashes.logcatArguments();
      }
    }
    else {
      arrayOfString = this.mLogcatArguments;
    }
    return arrayOfString;
  }
  
  public boolean logcatFilterByPid()
  {
    boolean bool;
    if (this.mLogcatFilterByPid == null)
    {
      if (this.mReportsCrashes == null) {
        bool = false;
      } else {
        bool = this.mReportsCrashes.logcatFilterByPid();
      }
    }
    else {
      bool = this.mLogcatFilterByPid.booleanValue();
    }
    return bool;
  }
  
  public String mailTo()
  {
    String str;
    if (this.mMailTo == null)
    {
      if (this.mReportsCrashes == null) {
        str = "";
      } else {
        str = this.mReportsCrashes.mailTo();
      }
    }
    else {
      str = this.mMailTo;
    }
    return str;
  }
  
  public int maxNumberOfRequestRetries()
  {
    int i;
    if (this.mMaxNumberOfRequestRetries == null)
    {
      if (this.mReportsCrashes == null) {
        i = 3;
      } else {
        i = this.mReportsCrashes.maxNumberOfRequestRetries();
      }
    }
    else {
      i = this.mMaxNumberOfRequestRetries.intValue();
    }
    return i;
  }
  
  public ReportingInteractionMode mode()
  {
    ReportingInteractionMode localReportingInteractionMode;
    if (this.mMode == null)
    {
      if (this.mReportsCrashes == null) {
        localReportingInteractionMode = ReportingInteractionMode.SILENT;
      } else {
        localReportingInteractionMode = this.mReportsCrashes.mode();
      }
    }
    else {
      localReportingInteractionMode = this.mMode;
    }
    return localReportingInteractionMode;
  }
  
  public HttpSender.Type reportType()
  {
    HttpSender.Type localType;
    if (this.mReportType == null)
    {
      if (this.mReportsCrashes == null) {
        localType = HttpSender.Type.FORM;
      } else {
        localType = this.mReportsCrashes.reportType();
      }
    }
    else {
      localType = this.mReportType;
    }
    return localType;
  }
  
  public int resDialogCommentPrompt()
  {
    int i;
    if (this.mResDialogCommentPrompt == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resDialogCommentPrompt();
      }
    }
    else {
      i = this.mResDialogCommentPrompt.intValue();
    }
    return i;
  }
  
  public int resDialogEmailPrompt()
  {
    int i;
    if (this.mResDialogEmailPrompt == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resDialogEmailPrompt();
      }
    }
    else {
      i = this.mResDialogEmailPrompt.intValue();
    }
    return i;
  }
  
  public int resDialogIcon()
  {
    int i;
    if (this.mResDialogIcon == null)
    {
      if (this.mReportsCrashes == null) {
        i = 17301543;
      } else {
        i = this.mReportsCrashes.resDialogIcon();
      }
    }
    else {
      i = this.mResDialogIcon.intValue();
    }
    return i;
  }
  
  public int resDialogOkToast()
  {
    int i;
    if (this.mResDialogOkToast == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resDialogOkToast();
      }
    }
    else {
      i = this.mResDialogOkToast.intValue();
    }
    return i;
  }
  
  public int resDialogText()
  {
    int i;
    if (this.mResDialogText == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resDialogText();
      }
    }
    else {
      i = this.mResDialogText.intValue();
    }
    return i;
  }
  
  public int resDialogTitle()
  {
    int i;
    if (this.mResDialogTitle == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resDialogTitle();
      }
    }
    else {
      i = this.mResDialogTitle.intValue();
    }
    return i;
  }
  
  public int resNotifIcon()
  {
    int i;
    if (this.mResNotifIcon == null)
    {
      if (this.mReportsCrashes == null) {
        i = 17301624;
      } else {
        i = this.mReportsCrashes.resNotifIcon();
      }
    }
    else {
      i = this.mResNotifIcon.intValue();
    }
    return i;
  }
  
  public int resNotifText()
  {
    int i;
    if (this.mResNotifText == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resNotifText();
      }
    }
    else {
      i = this.mResNotifText.intValue();
    }
    return i;
  }
  
  public int resNotifTickerText()
  {
    int i;
    if (this.mResNotifTickerText == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resNotifTickerText();
      }
    }
    else {
      i = this.mResNotifTickerText.intValue();
    }
    return i;
  }
  
  public int resNotifTitle()
  {
    int i;
    if (this.mResNotifTitle == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resNotifTitle();
      }
    }
    else {
      i = this.mResNotifTitle.intValue();
    }
    return i;
  }
  
  public int resToastText()
  {
    int i;
    if (this.mResToastText == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.resToastText();
      }
    }
    else {
      i = this.mResToastText.intValue();
    }
    return i;
  }
  
  public boolean sendReportsInDevMode()
  {
    boolean bool;
    if (this.mSendReportsInDevMode == null)
    {
      if (this.mReportsCrashes == null) {
        bool = true;
      } else {
        bool = this.mReportsCrashes.sendReportsInDevMode();
      }
    }
    else {
      bool = this.mSendReportsInDevMode.booleanValue();
    }
    return bool;
  }
  
  public void setAdditionalDropboxTags(String[] paramArrayOfString)
  {
    this.mAdditionalDropboxTags = paramArrayOfString;
  }
  
  public void setAdditionalSharedPreferences(String[] paramArrayOfString)
  {
    this.mAdditionalSharedPreferences = paramArrayOfString;
  }
  
  public void setApplicationLogFile(String paramString)
  {
    this.mApplicationLogFile = paramString;
  }
  
  public void setApplicationLogFileLines(int paramInt)
  {
    this.mApplicationLogFileLines = Integer.valueOf(paramInt);
  }
  
  public void setConnectionTimeout(Integer paramInteger)
  {
    this.mConnectionTimeout = paramInteger;
  }
  
  public void setCustomReportContent(ReportField[] paramArrayOfReportField)
  {
    this.mCustomReportContent = paramArrayOfReportField;
  }
  
  public void setDeleteOldUnsentReportsOnApplicationStart(Boolean paramBoolean)
  {
    this.mDeleteOldUnsentReportsOnApplicationStart = paramBoolean;
  }
  
  public void setDeleteUnapprovedReportsOnApplicationStart(Boolean paramBoolean)
  {
    this.mDeleteUnapprovedReportsOnApplicationStart = paramBoolean;
  }
  
  public void setDisableSSLCertValidation(boolean paramBoolean)
  {
    this.mDisableSSLCertValidation = Boolean.valueOf(paramBoolean);
  }
  
  public void setDropboxCollectionMinutes(Integer paramInteger)
  {
    this.mDropboxCollectionMinutes = paramInteger;
  }
  
  public void setExcludeMatchingSettingsKeys(String[] paramArrayOfString)
  {
    this.mExcludeMatchingSettingsKeys = paramArrayOfString;
  }
  
  public void setExcludeMatchingSharedPreferencesKeys(String[] paramArrayOfString)
  {
    this.mExcludeMatchingSharedPreferencesKeys = paramArrayOfString;
  }
  
  public void setForceCloseDialogAfterToast(Boolean paramBoolean)
  {
    this.mForceCloseDialogAfterToast = paramBoolean;
  }
  
  public void setFormKey(String paramString)
  {
    this.mFormKey = paramString;
  }
  
  public void setFormUri(String paramString)
  {
    this.mFormUri = paramString;
  }
  
  public void setFormUriBasicAuthLogin(String paramString)
  {
    this.mFormUriBasicAuthLogin = paramString;
  }
  
  public void setFormUriBasicAuthPassword(String paramString)
  {
    this.mFormUriBasicAuthPassword = paramString;
  }
  
  public void setHttpHeaders(Map<String, String> paramMap)
  {
    this.mHttpHeaders = paramMap;
  }
  
  public void setHttpMethod(HttpSender.Method paramMethod)
  {
    this.mHttpMethod = paramMethod;
  }
  
  public void setIncludeDropboxSystemTags(Boolean paramBoolean)
  {
    this.mIncludeDropboxSystemTags = paramBoolean;
  }
  
  public void setLogcatArguments(String[] paramArrayOfString)
  {
    this.mLogcatArguments = paramArrayOfString;
  }
  
  public void setLogcatFilterByPid(Boolean paramBoolean)
  {
    this.mLogcatFilterByPid = paramBoolean;
  }
  
  public void setMailTo(String paramString)
  {
    this.mMailTo = paramString;
  }
  
  public void setMaxNumberOfRequestRetries(Integer paramInteger)
  {
    this.mMaxNumberOfRequestRetries = paramInteger;
  }
  
  public void setMode(ReportingInteractionMode paramReportingInteractionMode)
    throws ACRAConfigurationException
  {
    this.mMode = paramReportingInteractionMode;
    ACRA.checkCrashResources();
  }
  
  public void setReportType(HttpSender.Type paramType)
  {
    this.mReportType = paramType;
  }
  
  public void setResDialogCommentPrompt(int paramInt)
  {
    this.mResDialogCommentPrompt = Integer.valueOf(paramInt);
  }
  
  public void setResDialogEmailPrompt(int paramInt)
  {
    this.mResDialogEmailPrompt = Integer.valueOf(paramInt);
  }
  
  public void setResDialogIcon(int paramInt)
  {
    this.mResDialogIcon = Integer.valueOf(paramInt);
  }
  
  public void setResDialogOkToast(int paramInt)
  {
    this.mResDialogOkToast = Integer.valueOf(paramInt);
  }
  
  public void setResDialogText(int paramInt)
  {
    this.mResDialogText = Integer.valueOf(paramInt);
  }
  
  public void setResDialogTitle(int paramInt)
  {
    this.mResDialogTitle = Integer.valueOf(paramInt);
  }
  
  public void setResNotifIcon(int paramInt)
  {
    this.mResNotifIcon = Integer.valueOf(paramInt);
  }
  
  public void setResNotifText(int paramInt)
  {
    this.mResNotifText = Integer.valueOf(paramInt);
  }
  
  public void setResNotifTickerText(int paramInt)
  {
    this.mResNotifTickerText = Integer.valueOf(paramInt);
  }
  
  public void setResNotifTitle(int paramInt)
  {
    this.mResNotifTitle = Integer.valueOf(paramInt);
  }
  
  public void setResToastText(int paramInt)
  {
    this.mResToastText = Integer.valueOf(paramInt);
  }
  
  public void setSendReportsInDevMode(Boolean paramBoolean)
  {
    this.mSendReportsInDevMode = paramBoolean;
  }
  
  public void setSharedPreferenceMode(Integer paramInteger)
  {
    this.mSharedPreferenceMode = paramInteger;
  }
  
  public void setSharedPreferenceName(String paramString)
  {
    this.mSharedPreferenceName = paramString;
  }
  
  public void setSocketTimeout(Integer paramInteger)
  {
    this.mSocketTimeout = paramInteger;
  }
  
  public int sharedPreferencesMode()
  {
    int i;
    if (this.mSharedPreferenceMode == null)
    {
      if (this.mReportsCrashes == null) {
        i = 0;
      } else {
        i = this.mReportsCrashes.sharedPreferencesMode();
      }
    }
    else {
      i = this.mSharedPreferenceMode.intValue();
    }
    return i;
  }
  
  public String sharedPreferencesName()
  {
    String str;
    if (this.mSharedPreferenceName == null)
    {
      if (this.mReportsCrashes == null) {
        str = "";
      } else {
        str = this.mReportsCrashes.sharedPreferencesName();
      }
    }
    else {
      str = this.mSharedPreferenceName;
    }
    return str;
  }
  
  public int socketTimeout()
  {
    int i;
    if (this.mSocketTimeout == null)
    {
      if (this.mReportsCrashes == null) {
        i = 5000;
      } else {
        i = this.mReportsCrashes.socketTimeout();
      }
    }
    else {
      i = this.mSocketTimeout.intValue();
    }
    return i;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.ACRAConfiguration
 * JD-Core Version:    0.7.0.1
 */