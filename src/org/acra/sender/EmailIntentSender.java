package org.acra.sender;

import android.content.Context;
import android.content.Intent;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.ACRAConstants;
import org.acra.ReportField;
import org.acra.collector.CrashReportData;

public class EmailIntentSender
  implements ReportSender
{
  private final Context mContext;
  
  public EmailIntentSender(Context paramContext)
  {
    this.mContext = paramContext;
  }
  
  private String buildBody(CrashReportData paramCrashReportData)
  {
    ReportField[] arrayOfReportField = ACRA.getConfig().customReportContent();
    if (arrayOfReportField.length == 0) {
      arrayOfReportField = ACRAConstants.DEFAULT_MAIL_REPORT_FIELDS;
    }
    StringBuilder localStringBuilder = new StringBuilder();
    arrayOfReportField = arrayOfReportField;
    int j = arrayOfReportField.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return localStringBuilder.toString();
      }
      ReportField localReportField = arrayOfReportField[i];
      localStringBuilder.append(localReportField.toString()).append("=");
      localStringBuilder.append((String)paramCrashReportData.get(localReportField));
      localStringBuilder.append('\n');
    }
  }
  
  public void send(CrashReportData paramCrashReportData)
    throws ReportSenderException
  {
    Object localObject = this.mContext.getPackageName() + " Crash Report";
    String str = buildBody(paramCrashReportData);
    Intent localIntent = new Intent("android.intent.action.SEND");
    localIntent.addFlags(268435456);
    localIntent.setType("text/plain");
    localIntent.putExtra("android.intent.extra.SUBJECT", (String)localObject);
    localIntent.putExtra("android.intent.extra.TEXT", str);
    localObject = new String[1];
    localObject[0] = ACRA.getConfig().mailTo();
    localIntent.putExtra("android.intent.extra.EMAIL", (String[])localObject);
    this.mContext.startActivity(localIntent);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.sender.EmailIntentSender
 * JD-Core Version:    0.7.0.1
 */