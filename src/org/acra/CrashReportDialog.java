package org.acra;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import java.io.IOException;
import org.acra.collector.CrashReportData;
import org.acra.log.ACRALog;
import org.acra.util.ToastSender;

public class CrashReportDialog
  extends Activity
  implements DialogInterface.OnClickListener, DialogInterface.OnDismissListener
{
  private static final String STATE_COMMENT = "comment";
  private static final String STATE_EMAIL = "email";
  AlertDialog mDialog;
  String mReportFileName;
  private SharedPreferences prefs;
  private EditText userComment;
  private EditText userEmail;
  
  private View buildCustomView(Bundle paramBundle)
  {
    LinearLayout localLinearLayout1 = new LinearLayout(this);
    localLinearLayout1.setOrientation(1);
    localLinearLayout1.setPadding(10, 10, 10, 10);
    localLinearLayout1.setLayoutParams(new ViewGroup.LayoutParams(-1, -2));
    localLinearLayout1.setFocusable(true);
    localLinearLayout1.setFocusableInTouchMode(true);
    Object localObject1 = new ScrollView(this);
    localLinearLayout1.addView((View)localObject1, new LinearLayout.LayoutParams(-1, -1, 1.0F));
    LinearLayout localLinearLayout2 = new LinearLayout(this);
    localLinearLayout2.setOrientation(1);
    ((ScrollView)localObject1).addView(localLinearLayout2);
    localObject1 = new TextView(this);
    int j = ACRA.getConfig().resDialogText();
    if (j != 0) {
      ((TextView)localObject1).setText(getText(j));
    }
    localLinearLayout2.addView((View)localObject1);
    int i = ACRA.getConfig().resDialogCommentPrompt();
    Object localObject2;
    if (i != 0)
    {
      TextView localTextView = new TextView(this);
      localTextView.setText(getText(i));
      localTextView.setPadding(localTextView.getPaddingLeft(), 10, localTextView.getPaddingRight(), localTextView.getPaddingBottom());
      localLinearLayout2.addView(localTextView, new LinearLayout.LayoutParams(-1, -2));
      this.userComment = new EditText(this);
      this.userComment.setLines(2);
      if (paramBundle != null)
      {
        localObject2 = paramBundle.getString("comment");
        if (localObject2 != null) {
          this.userComment.setText((CharSequence)localObject2);
        }
      }
      localLinearLayout2.addView(this.userComment);
    }
    int k = ACRA.getConfig().resDialogEmailPrompt();
    if (k != 0)
    {
      localObject2 = new TextView(this);
      ((TextView)localObject2).setText(getText(k));
      ((TextView)localObject2).setPadding(((TextView)localObject2).getPaddingLeft(), 10, ((TextView)localObject2).getPaddingRight(), ((TextView)localObject2).getPaddingBottom());
      localLinearLayout2.addView((View)localObject2);
      this.userEmail = new EditText(this);
      this.userEmail.setSingleLine();
      this.userEmail.setInputType(33);
      this.prefs = getSharedPreferences(ACRA.getConfig().sharedPreferencesName(), ACRA.getConfig().sharedPreferencesMode());
      localObject2 = null;
      if (paramBundle != null) {
        localObject2 = paramBundle.getString("email");
      }
      if (localObject2 == null) {
        this.userEmail.setText(this.prefs.getString("acra.user.email", ""));
      } else {
        this.userEmail.setText((CharSequence)localObject2);
      }
      localLinearLayout2.addView(this.userEmail);
    }
    return localLinearLayout1;
  }
  
  private void cancelReports()
  {
    ACRA.getErrorReporter().deletePendingNonApprovedReports(false);
  }
  
  private void sendCrash()
  {
    String str3;
    if (this.userComment != null) {
      str3 = this.userComment.getText().toString();
    }
    for (;;)
    {
      String str1;
      Object localObject;
      if ((this.prefs != null) && (this.userEmail != null))
      {
        str1 = this.userEmail.getText().toString();
        localObject = this.prefs.edit();
        ((SharedPreferences.Editor)localObject).putString("acra.user.email", str1);
        ((SharedPreferences.Editor)localObject).commit();
        localObject = new CrashReportPersister(getApplicationContext());
      }
      try
      {
        Log.d(ACRA.LOG_TAG, "Add user comment to " + this.mReportFileName);
        CrashReportData localCrashReportData = ((CrashReportPersister)localObject).load(this.mReportFileName);
        localCrashReportData.put(ReportField.USER_COMMENT, str3);
        localCrashReportData.put(ReportField.USER_EMAIL, str1);
        ((CrashReportPersister)localObject).store(localCrashReportData, this.mReportFileName);
        Log.v(ACRA.LOG_TAG, "About to start SenderWorker from CrashReportDialog");
        ACRA.getErrorReporter().startSendingReports(false, true);
        int i = ACRA.getConfig().resDialogOkToast();
        if (i != 0) {
          ToastSender.sendToast(getApplicationContext(), i, 1);
        }
        return;
        str3 = "";
        continue;
        String str2 = "";
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "User comment not added: ", localIOException);
        }
      }
    }
  }
  
  protected void cancelNotification()
  {
    ((NotificationManager)getSystemService("notification")).cancel(666);
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    if (paramInt != -1) {
      cancelReports();
    } else {
      sendCrash();
    }
    finish();
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (!getIntent().getBooleanExtra("FORCE_CANCEL", false))
    {
      this.mReportFileName = getIntent().getStringExtra("REPORT_FILE_NAME");
      Log.d(ACRA.LOG_TAG, "Opening CrashReportDialog for " + this.mReportFileName);
      if (this.mReportFileName == null) {
        finish();
      }
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      int i = ACRA.getConfig().resDialogTitle();
      if (i != 0) {
        localBuilder.setTitle(i);
      }
      i = ACRA.getConfig().resDialogIcon();
      if (i != 0) {
        localBuilder.setIcon(i);
      }
      localBuilder.setView(buildCustomView(paramBundle));
      localBuilder.setPositiveButton(17039370, this);
      localBuilder.setNegativeButton(17039360, this);
      cancelNotification();
      this.mDialog = localBuilder.create();
      this.mDialog.setCanceledOnTouchOutside(false);
      this.mDialog.setOnDismissListener(this);
      this.mDialog.show();
    }
    else
    {
      ACRA.log.d(ACRA.LOG_TAG, "Forced reports deletion.");
      cancelReports();
      finish();
    }
  }
  
  public void onDismiss(DialogInterface paramDialogInterface)
  {
    finish();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    if ((this.userComment != null) && (this.userComment.getText() != null)) {
      paramBundle.putString("comment", this.userComment.getText().toString());
    }
    if ((this.userEmail != null) && (this.userEmail.getText() != null)) {
      paramBundle.putString("email", this.userEmail.getText().toString());
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.CrashReportDialog
 * JD-Core Version:    0.7.0.1
 */