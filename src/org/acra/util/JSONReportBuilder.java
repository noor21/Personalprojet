package org.acra.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;
import org.acra.log.ACRALog;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONReportBuilder
{
  private static void addJSONFromProperty(JSONObject paramJSONObject, String paramString)
    throws JSONException
  {
    int i = paramString.indexOf('=');
    if (i <= 0)
    {
      paramJSONObject.put(paramString.trim(), true);
    }
    else
    {
      String str = paramString.substring(0, i).trim();
      Object localObject = guessType(paramString.substring(i + 1).trim());
      if ((localObject instanceof String)) {
        localObject = ((String)localObject).replaceAll("\\\\n", "\n");
      }
      String[] arrayOfString = str.split("\\.");
      if (arrayOfString.length <= 1) {
        paramJSONObject.accumulate(str, localObject);
      } else {
        addJSONSubTree(paramJSONObject, arrayOfString, localObject);
      }
    }
  }
  
  private static void addJSONSubTree(JSONObject paramJSONObject, String[] paramArrayOfString, Object paramObject)
    throws JSONException
  {
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfString.length) {
        return;
      }
      String str = paramArrayOfString[i];
      if (i >= -1 + paramArrayOfString.length)
      {
        paramJSONObject.accumulate(str, paramObject);
      }
      else
      {
        JSONObject localJSONObject;
        if (!paramJSONObject.isNull(str))
        {
          localJSONObject = paramJSONObject.getJSONObject(str);
        }
        else
        {
          localJSONObject = new JSONObject();
          paramJSONObject.accumulate(str, localJSONObject);
        }
        paramJSONObject = localJSONObject;
      }
    }
  }
  
  public static JSONObject buildJSONReport(CrashReportData paramCrashReportData)
    throws JSONReportBuilder.JSONReportException
  {
    JSONObject localJSONObject1 = new JSONObject();
    Iterator localIterator = paramCrashReportData.keySet().iterator();
    ReportField localReportField;
    while (localIterator.hasNext())
    {
      localReportField = (ReportField)localIterator.next();
      try
      {
        if (localReportField.containsKeyValuePairs())
        {
          JSONObject localJSONObject2 = new JSONObject();
          BufferedReader localBufferedReader = new BufferedReader(new StringReader(paramCrashReportData.getProperty(localReportField)), 1024);
          try
          {
            for (;;)
            {
              String str = localBufferedReader.readLine();
              if (str == null) {
                break;
              }
              addJSONFromProperty(localJSONObject2, str);
            }
          }
          catch (IOException localIOException)
          {
            ACRA.log.e(ACRA.LOG_TAG, "Error while converting " + localReportField.name() + " to JSON.", localIOException);
            localJSONObject1.accumulate(localReportField.name(), localJSONObject2);
          }
        }
        else
        {
          localJSONException.accumulate(localReportField.name(), guessType(paramCrashReportData.getProperty(localReportField)));
        }
      }
      catch (JSONException localJSONException)
      {
        throw new JSONReportException("Could not create JSON object for key " + localReportField, localJSONException);
      }
    }
    return localJSONException;
  }
  
  private static Object guessType(String paramString)
  {
    Object localObject;
    if (paramString.equalsIgnoreCase("true")) {
      localObject = Boolean.valueOf(true);
    }
    for (;;)
    {
      return localObject;
      if (paramString.equalsIgnoreCase("false"))
      {
        localObject = Boolean.valueOf(false);
      }
      else if (paramString.matches("(?:^|\\s)([1-9](?:\\d*|(?:\\d{0,2})(?:,\\d{3})*)(?:\\.\\d*[1-9])?|0?\\.\\d*[1-9]|0)(?:\\s|$)"))
      {
        localObject = NumberFormat.getInstance(Locale.US);
        try
        {
          localObject = ((NumberFormat)localObject).parse(paramString);
          localObject = localObject;
        }
        catch (ParseException localParseException) {}
      }
      else
      {
        localObject = paramString;
      }
    }
  }
  
  public static class JSONReportException
    extends Exception
  {
    private static final long serialVersionUID = -694684023635442219L;
    
    public JSONReportException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.JSONReportBuilder
 * JD-Core Version:    0.7.0.1
 */