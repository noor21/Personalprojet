package org.acra.collector;

import java.util.EnumMap;
import org.acra.ReportField;
import org.acra.util.JSONReportBuilder;
import org.acra.util.JSONReportBuilder.JSONReportException;
import org.json.JSONObject;

public final class CrashReportData
  extends EnumMap<ReportField, String>
{
  private static final long serialVersionUID = 4112578634029874840L;
  
  public CrashReportData()
  {
    super(ReportField.class);
  }
  
  public String getProperty(ReportField paramReportField)
  {
    return (String)super.get(paramReportField);
  }
  
  public JSONObject toJSON()
    throws JSONReportBuilder.JSONReportException
  {
    return JSONReportBuilder.buildJSONReport(this);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.CrashReportData
 * JD-Core Version:    0.7.0.1
 */