package group.pals.android.lib.ui.filechooser.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.inputmethod.InputMethodManager;

public class Ui
{
  public static void hideSoftKeyboard(Context paramContext, IBinder paramIBinder)
  {
    InputMethodManager localInputMethodManager = (InputMethodManager)paramContext.getSystemService("input_method");
    if (localInputMethodManager != null) {
      localInputMethodManager.hideSoftInputFromWindow(paramIBinder, 0);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.Ui
 * JD-Core Version:    0.7.0.1
 */