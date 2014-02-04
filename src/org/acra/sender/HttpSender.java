package org.acra.sender;

import android.net.Uri;
import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.util.HttpRequest;
import org.acra.util.JSONReportBuilder.JSONReportException;
import org.json.JSONObject;

public class HttpSender
  implements ReportSender
{
  private final Uri mFormUri;
  private final Map<ReportField, String> mMapping;
  private final Method mMethod;
  private final Type mType;
  
  public HttpSender(Method paramMethod, Type paramType, String paramString, Map<ReportField, String> paramMap)
  {
    this.mMethod = paramMethod;
    this.mFormUri = Uri.parse(paramString);
    this.mMapping = paramMap;
    this.mType = paramType;
  }
  
  public HttpSender(Method paramMethod, Type paramType, Map<ReportField, String> paramMap)
  {
    this.mMethod = paramMethod;
    this.mFormUri = null;
    this.mMapping = paramMap;
    this.mType = paramType;
  }
  
  private Map<String, String> remap(Map<ReportField, String> paramMap)
  {
    ReportField[] arrayOfReportField = ACRA.getConfig().customReportContent();
    if (arrayOfReportField.length == 0) {
      arrayOfReportField = ACRAConstants.DEFAULT_REPORT_FIELDS;
    }
    HashMap localHashMap = new HashMap(paramMap.size());
    arrayOfReportField = arrayOfReportField;
    int i = arrayOfReportField.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return localHashMap;
      }
      ReportField localReportField = arrayOfReportField[j];
      if ((this.mMapping != null) && (this.mMapping.get(localReportField) != null)) {
        localHashMap.put(this.mMapping.get(localReportField), paramMap.get(localReportField));
      } else {
        localHashMap.put(localReportField.toString(), paramMap.get(localReportField));
      }
    }
  }
  
  public void send(CrashReportData paramCrashReportData)
    throws ReportSenderException
  {
    String str1 = null;
    HttpRequest localHttpRequest;
    for (;;)
    {
      try
      {
        if (this.mFormUri == null)
        {
          URL localURL1 = new URL(ACRA.getConfig().formUri());
          Log.d(ACRA.LOG_TAG, "Connect to " + localURL1.toString());
          if (!ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthLogin())) {
            break label361;
          }
          str2 = null;
          if (!ACRAConfiguration.isNull(ACRA.getConfig().formUriBasicAuthPassword())) {
            break label372;
          }
          localHttpRequest = new HttpRequest();
          localHttpRequest.setConnectionTimeOut(ACRA.getConfig().connectionTimeout());
          localHttpRequest.setSocketTimeOut(ACRA.getConfig().socketTimeout());
          localHttpRequest.setMaxNrRetries(ACRA.getConfig().maxNumberOfRequestRetries());
          localHttpRequest.setLogin(str2);
          localHttpRequest.setPassword(str1);
          localHttpRequest.setHeaders(ACRA.getConfig().getHttpHeaders());
          switch (1.$SwitchMap$org$acra$sender$HttpSender$Type[this.mType.ordinal()])
          {
          default: 
            str1 = HttpRequest.getParamsAsFormString(remap(paramCrashReportData));
            switch (1.$SwitchMap$org$acra$sender$HttpSender$Method[this.mMethod.ordinal()])
            {
            default: 
              throw new UnsupportedOperationException("Unknown method: " + this.mMethod.name());
            }
            break;
          }
        }
      }
      catch (IOException localIOException)
      {
        throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), localIOException);
        URL localURL2 = new URL(this.mFormUri.toString());
        continue;
      }
      catch (JSONReportBuilder.JSONReportException localJSONReportException)
      {
        throw new ReportSenderException("Error while sending " + ACRA.getConfig().reportType() + " report via Http " + this.mMethod.name(), localJSONReportException);
      }
      label361:
      String str2 = ACRA.getConfig().formUriBasicAuthLogin();
      continue;
      label372:
      str1 = ACRA.getConfig().formUriBasicAuthPassword();
      continue;
      str1 = paramCrashReportData.toJSON().toString();
    }
    URL localURL3 = new URL(localJSONReportException.toString() + '/' + paramCrashReportData.getProperty(ReportField.REPORT_ID));
    localHttpRequest.send(localURL3, this.mMethod, str1, this.mType);
  }
  
  public static abstract enum Type
  {
    static
    {
      Type[] arrayOfType = new Type[2];
      arrayOfType[0] = FORM;
      arrayOfType[1] = JSON;
      $VALUES = arrayOfType;
    }
    
    private Type() {}
    
    public abstract String getContentType();
  }
  
  public static enum Method
  {
    static
    {
      Method[] arrayOfMethod = new Method[2];
      arrayOfMethod[0] = POST;
      arrayOfMethod[1] = PUT;
      $VALUES = arrayOfMethod;
    }
    
    private Method() {}
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.sender.HttpSender
 * JD-Core Version:    0.7.0.1
 */