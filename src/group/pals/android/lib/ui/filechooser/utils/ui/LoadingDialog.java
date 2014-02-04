package group.pals.android.lib.ui.filechooser.utils.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

public abstract class LoadingDialog
  extends AsyncTask<Void, Void, Object>
{
  public static final String _ClassName = LoadingDialog.class.getName();
  private int mDelayTime = 500;
  private final ProgressDialog mDialog;
  private boolean mFinished = false;
  private Throwable mLastException;
  
  public LoadingDialog(Context paramContext, int paramInt, boolean paramBoolean)
  {
    this(paramContext, paramContext.getString(paramInt), paramBoolean);
  }
  
  public LoadingDialog(Context paramContext, String paramString, boolean paramBoolean)
  {
    this.mDialog = new ProgressDialog(paramContext);
    this.mDialog.setMessage(paramString);
    this.mDialog.setIndeterminate(true);
    this.mDialog.setCancelable(paramBoolean);
    if (paramBoolean)
    {
      this.mDialog.setCanceledOnTouchOutside(true);
      this.mDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          LoadingDialog.this.cancel(true);
        }
      });
    }
  }
  
  private void doFinish()
  {
    this.mFinished = true;
    try
    {
      this.mDialog.dismiss();
      return;
    }
    catch (Throwable localThrowable)
    {
      for (;;)
      {
        Log.e(_ClassName, "doFinish() - dismiss dialog: " + localThrowable);
      }
    }
  }
  
  public int getDelayTime()
  {
    return this.mDelayTime;
  }
  
  protected Throwable getLastException()
  {
    return this.mLastException;
  }
  
  protected void onCancelled()
  {
    doFinish();
    super.onCancelled();
  }
  
  protected void onPostExecute(Object paramObject)
  {
    doFinish();
  }
  
  protected void onPreExecute()
  {
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        if (!LoadingDialog.this.mFinished) {}
        try
        {
          LoadingDialog.this.mDialog.show();
          return;
        }
        catch (Throwable localThrowable)
        {
          for (;;)
          {
            Log.e(LoadingDialog._ClassName, "onPreExecute() - show dialog: " + localThrowable);
          }
        }
      }
    }, getDelayTime());
  }
  
  public LoadingDialog setDelayTime(int paramInt)
  {
    if (paramInt < 0) {
      paramInt = 0;
    }
    this.mDelayTime = paramInt;
    return this;
  }
  
  protected void setLastException(Throwable paramThrowable)
  {
    this.mLastException = paramThrowable;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.ui.LoadingDialog
 * JD-Core Version:    0.7.0.1
 */