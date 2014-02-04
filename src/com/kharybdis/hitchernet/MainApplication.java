package com.kharybdis.hitchernet;

import android.app.Application;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.acra.sender.HttpSender.Method;
import org.acra.sender.HttpSender.Type;

@ReportsCrashes(formKey="", formUri="http://droplet.bajratechnologies.com/dipendra/report.php", formUriBasicAuthLogin="", formUriBasicAuthPassword="", httpMethod=HttpSender.Method.POST, logcatArguments={"-t", "500", "-v", "long", "Hitchernet:V"}, mode=ReportingInteractionMode.DIALOG, reportType=HttpSender.Type.FORM, resDialogCommentPrompt=2131165298, resDialogIcon=17301659, resDialogOkToast=2131165299, resDialogText=2131165297, resDialogTitle=2131165296)
public class MainApplication
  extends Application
{
  public void onCreate()
  {
    super.onCreate();
    ACRA.init(this);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.MainApplication
 * JD-Core Version:    0.7.0.1
 */