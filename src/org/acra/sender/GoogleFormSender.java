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

public class GoogleFormSender
  implements ReportSender
{
  private final Uri mFormUri;
  
  public GoogleFormSender()
  {
    this.mFormUri = null;
  }
  
  public GoogleFormSender(String paramString)
  {
    String str = ACRA.getConfig().googleFormUrlFormat();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramString;
    this.mFormUri = Uri.parse(String.format(str, arrayOfObject));
  }
  
  private Map<String, String> remap(Map<ReportField, String> paramMap)
  {
    ReportField[] arrayOfReportField1 = ACRA.getConfig().customReportContent();
    if (arrayOfReportField1.length == 0) {
      arrayOfReportField1 = ACRAConstants.DEFAULT_REPORT_FIELDS;
    }
    int i = 0;
    HashMap localHashMap = new HashMap();
    ReportField[] arrayOfReportField2 = arrayOfReportField1;
    int k = arrayOfReportField2.length;
    for (int j = 0;; j++)
    {
      if (j >= k) {
        return localHashMap;
      }
      arrayOfReportField1 = arrayOfReportField2[j];
      switch (1.$SwitchMap$org$acra$ReportField[arrayOfReportField1.ordinal()])
      {
      default: 
        localHashMap.put("entry." + i + ".single", paramMap.get(arrayOfReportField1));
        break;
      case 1: 
        localHashMap.put("entry." + i + ".single", "'" + (String)paramMap.get(arrayOfReportField1));
        break;
      case 2: 
        localHashMap.put("entry." + i + ".single", "'" + (String)paramMap.get(arrayOfReportField1));
      }
      i++;
    }
  }
  
  public void send(CrashReportData paramCrashReportData)
    throws ReportSenderException
  {
    Object localObject1;
    if (this.mFormUri == null)
    {
      localObject2 = ACRA.getConfig().googleFormUrlFormat();
      localObject1 = new Object[1];
      localObject1[0] = ACRA.getConfig().formKey();
    }
    for (Object localObject2 = Uri.parse(String.format((String)localObject2, (Object[])localObject1));; localObject2 = this.mFormUri)
    {
      localObject1 = remap(paramCrashReportData);
      ((Map)localObject1).put("pageNumber", "0");
      ((Map)localObject1).put("backupCache", "");
      ((Map)localObject1).put("submit", "Envoyer");
      try
      {
        URL localURL = new URL(((Uri)localObject2).toString());
        Log.d(ACRA.LOG_TAG, "Sending report " + (String)paramCrashReportData.get(ReportField.REPORT_ID));
        Log.d(ACRA.LOG_TAG, "Connect to " + localURL);
        localObject2 = new HttpRequest();
        ((HttpRequest)localObject2).setConnectionTimeOut(ACRA.getConfig().connectionTimeout());
        ((HttpRequest)localObject2).setSocketTimeOut(ACRA.getConfig().socketTimeout());
        ((HttpRequest)localObject2).setMaxNrRetries(ACRA.getConfig().maxNumberOfRequestRetries());
        ((HttpRequest)localObject2).send(localURL, HttpSender.Method.POST, HttpRequest.getParamsAsFormString((Map)localObject1), HttpSender.Type.FORM);
        return;
      }
      catch (IOException localIOException)
      {
        throw new ReportSenderException("Error while sending report to Google Form.", localIOException);
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.sender.GoogleFormSender
 * JD-Core Version:    0.7.0.1
 */