package group.pals.android.lib.ui.filechooser.utils;

import android.content.Context;

public class Utils
{
  public static boolean hasPermissions(Context paramContext, String... paramVarArgs)
  {
    boolean bool = false;
    int j = paramVarArgs.length;
    for (int i = 0; i < j; i++) {
      if (paramContext.checkCallingOrSelfPermission(paramVarArgs[i]) == -1) {
        return bool;
      }
    }
    bool = true;
    return bool;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.Utils
 * JD-Core Version:    0.7.0.1
 */