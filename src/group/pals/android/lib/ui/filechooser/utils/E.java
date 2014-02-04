package group.pals.android.lib.ui.filechooser.utils;

import android.app.AlertDialog;
import android.content.Context;
import group.pals.android.lib.ui.filechooser.utils.ui.Dlg;

public class E
{
  public static void show(Context paramContext)
  {
    try
    {
      localObject = new Object[2];
      localObject[0] = "android-filechooser";
      localObject[1] = "5.0";
      localObject = String.format("Hi  :-)\n\n%s v%s\n…by Hai Bison Apps\n\nhttp://www.haibison.com\n\nHope you enjoy this library.", (Object[])localObject);
      localObject = localObject;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        AlertDialog localAlertDialog;
        Object localObject = "Oops… You've found a broken Easter egg, try again later  :-(";
      }
    }
    localAlertDialog = Dlg.newDlg(paramContext);
    localAlertDialog.setButton(-2, null, null);
    localAlertDialog.setTitle("…");
    localAlertDialog.setMessage((CharSequence)localObject);
    localAlertDialog.show();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.E
 * JD-Core Version:    0.7.0.1
 */