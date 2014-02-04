package org.acra;

public final class ACRAConstants
{
  static final String APPROVED_SUFFIX = "-approved";
  public static final String DEFAULT_APPLICATION_LOGFILE = "";
  public static final int DEFAULT_APPLICATION_LOGFILE_LINES = 100;
  public static final int DEFAULT_BUFFER_SIZE_IN_BYTES = 8192;
  public static final int DEFAULT_CONNECTION_TIMEOUT = 3000;
  public static final boolean DEFAULT_DELETE_OLD_UNSENT_REPORTS_ON_APPLICATION_START = true;
  public static final boolean DEFAULT_DELETE_UNAPPROVED_REPORTS_ON_APPLICATION_START = true;
  public static final int DEFAULT_DIALOG_ICON = 17301543;
  public static final boolean DEFAULT_DISABLE_SSL_CERT_VALIDATION = false;
  public static final int DEFAULT_DROPBOX_COLLECTION_MINUTES = 5;
  public static final boolean DEFAULT_FORCE_CLOSE_DIALOG_AFTER_TOAST = false;
  public static final String DEFAULT_GOOGLE_FORM_URL_FORMAT = "https://docs.google.com/spreadsheet/formResponse?formkey=%s&ifq";
  public static final boolean DEFAULT_INCLUDE_DROPBOX_SYSTEM_TAGS = false;
  public static final boolean DEFAULT_LOGCAT_FILTER_BY_PID = false;
  public static final int DEFAULT_LOGCAT_LINES = 100;
  public static final ReportField[] DEFAULT_MAIL_REPORT_FIELDS;
  public static final int DEFAULT_MAX_NUMBER_OF_REQUEST_RETRIES = 3;
  public static final int DEFAULT_NOTIFICATION_ICON = 17301624;
  public static final ReportField[] DEFAULT_REPORT_FIELDS;
  public static final int DEFAULT_RES_VALUE = 0;
  public static final boolean DEFAULT_SEND_REPORTS_IN_DEV_MODE = true;
  public static final int DEFAULT_SHARED_PREFERENCES_MODE = 0;
  public static final int DEFAULT_SOCKET_TIMEOUT = 5000;
  public static final String DEFAULT_STRING_VALUE = "";
  protected static final String EXTRA_FORCE_CANCEL = "FORCE_CANCEL";
  protected static final String EXTRA_REPORT_FILE_NAME = "REPORT_FILE_NAME";
  static final int MAX_SEND_REPORTS = 5;
  static final int NOTIF_CRASH_ID = 666;
  public static final String NULL_VALUE = "ACRA-NULL-STRING";
  public static final String REPORTFILE_EXTENSION = ".stacktrace";
  static final String SILENT_SUFFIX = "-" + ReportField.IS_SILENT;
  static final int TOAST_WAIT_DURATION = 3000;
  
  static
  {
    ReportField[] arrayOfReportField = new ReportField[7];
    arrayOfReportField[0] = ReportField.USER_COMMENT;
    arrayOfReportField[1] = ReportField.ANDROID_VERSION;
    arrayOfReportField[2] = ReportField.APP_VERSION_NAME;
    arrayOfReportField[3] = ReportField.BRAND;
    arrayOfReportField[4] = ReportField.PHONE_MODEL;
    arrayOfReportField[5] = ReportField.CUSTOM_DATA;
    arrayOfReportField[6] = ReportField.STACK_TRACE;
    DEFAULT_MAIL_REPORT_FIELDS = arrayOfReportField;
    arrayOfReportField = new ReportField[31];
    arrayOfReportField[0] = ReportField.REPORT_ID;
    arrayOfReportField[1] = ReportField.APP_VERSION_CODE;
    arrayOfReportField[2] = ReportField.APP_VERSION_NAME;
    arrayOfReportField[3] = ReportField.PACKAGE_NAME;
    arrayOfReportField[4] = ReportField.FILE_PATH;
    arrayOfReportField[5] = ReportField.PHONE_MODEL;
    arrayOfReportField[6] = ReportField.BRAND;
    arrayOfReportField[7] = ReportField.PRODUCT;
    arrayOfReportField[8] = ReportField.ANDROID_VERSION;
    arrayOfReportField[9] = ReportField.BUILD;
    arrayOfReportField[10] = ReportField.TOTAL_MEM_SIZE;
    arrayOfReportField[11] = ReportField.AVAILABLE_MEM_SIZE;
    arrayOfReportField[12] = ReportField.CUSTOM_DATA;
    arrayOfReportField[13] = ReportField.IS_SILENT;
    arrayOfReportField[14] = ReportField.STACK_TRACE;
    arrayOfReportField[15] = ReportField.INITIAL_CONFIGURATION;
    arrayOfReportField[16] = ReportField.CRASH_CONFIGURATION;
    arrayOfReportField[17] = ReportField.DISPLAY;
    arrayOfReportField[18] = ReportField.USER_COMMENT;
    arrayOfReportField[19] = ReportField.USER_EMAIL;
    arrayOfReportField[20] = ReportField.USER_APP_START_DATE;
    arrayOfReportField[21] = ReportField.USER_CRASH_DATE;
    arrayOfReportField[22] = ReportField.DUMPSYS_MEMINFO;
    arrayOfReportField[23] = ReportField.LOGCAT;
    arrayOfReportField[24] = ReportField.INSTALLATION_ID;
    arrayOfReportField[25] = ReportField.DEVICE_FEATURES;
    arrayOfReportField[26] = ReportField.ENVIRONMENT;
    arrayOfReportField[27] = ReportField.SHARED_PREFERENCES;
    arrayOfReportField[28] = ReportField.SETTINGS_SYSTEM;
    arrayOfReportField[29] = ReportField.SETTINGS_SECURE;
    arrayOfReportField[30] = ReportField.SETTINGS_GLOBAL;
    DEFAULT_REPORT_FIELDS = arrayOfReportField;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.ACRAConstants
 * JD-Core Version:    0.7.0.1
 */