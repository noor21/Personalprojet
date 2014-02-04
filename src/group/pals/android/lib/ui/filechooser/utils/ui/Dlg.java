package group.pals.android.lib.ui.filechooser.utils.ui;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;
import group.pals.android.lib.ui.filechooser.R.string;

public class Dlg
{
  public static final int _LengthLong = 1;
  public static final int _LengthShort;
  private static Toast mToast;
  
  public static void confirmYesno(Context paramContext, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener)
  {
    confirmYesno(paramContext, paramCharSequence, paramOnClickListener, null);
  }
  
  public static void confirmYesno(Context paramContext, CharSequence paramCharSequence, DialogInterface.OnClickListener paramOnClickListener, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    AlertDialog localAlertDialog = newDlg(paramContext);
    localAlertDialog.setIcon(17301543);
    localAlertDialog.setTitle(R.string.afc_title_confirmation);
    localAlertDialog.setMessage(paramCharSequence);
    localAlertDialog.setButton(-1, paramContext.getString(17039379), paramOnClickListener);
    localAlertDialog.setOnCancelListener(paramOnCancelListener);
    localAlertDialog.show();
  }
  
  public static AlertDialog newDlg(Context paramContext)
  {
    AlertDialog localAlertDialog = newDlgBuilder(paramContext).create();
    localAlertDialog.setCanceledOnTouchOutside(true);
    return localAlertDialog;
  }
  
  public static AlertDialog.Builder newDlgBuilder(Context paramContext)
  {
    return new AlertDialog.Builder(paramContext);
  }
  
  public static void showError(Context paramContext, int paramInt, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    showError(paramContext, paramContext.getString(paramInt), paramOnCancelListener);
  }
  
  public static void showError(Context paramContext, CharSequence paramCharSequence, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    AlertDialog localAlertDialog = newDlg(paramContext);
    localAlertDialog.setIcon(17301543);
    localAlertDialog.setTitle(R.string.afc_title_error);
    localAlertDialog.setMessage(paramCharSequence);
    localAlertDialog.setOnCancelListener(paramOnCancelListener);
    localAlertDialog.show();
  }
  
  public static void showInfo(Context paramContext, int paramInt)
  {
    showInfo(paramContext, paramContext.getString(paramInt));
  }
  
  public static void showInfo(Context paramContext, CharSequence paramCharSequence)
  {
    AlertDialog localAlertDialog = newDlg(paramContext);
    localAlertDialog.setIcon(17301659);
    localAlertDialog.setTitle(R.string.afc_title_info);
    localAlertDialog.setMessage(paramCharSequence);
    localAlertDialog.show();
  }
  
  public static void showUnknownError(Context paramContext, Throwable paramThrowable, DialogInterface.OnCancelListener paramOnCancelListener)
  {
    String str = paramContext.getString(R.string.afc_pmsg_unknown_error);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = paramThrowable;
    showError(paramContext, String.format(str, arrayOfObject), paramOnCancelListener);
  }
  
  public static void toast(Context paramContext, int paramInt1, int paramInt2)
  {
    toast(paramContext, paramContext.getString(paramInt1), paramInt2);
  }
  
  public static void toast(Context paramContext, CharSequence paramCharSequence, int paramInt)
  {
    if (mToast != null) {
      mToast.cancel();
    }
    mToast = Toast.makeText(paramContext, paramCharSequence, paramInt);
    mToast.show();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.ui.Dlg
 * JD-Core Version:    0.7.0.1
 */