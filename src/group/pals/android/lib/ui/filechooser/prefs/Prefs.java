package group.pals.android.lib.ui.filechooser.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import group.pals.android.lib.ui.filechooser.R.string;

public class Prefs
{
  public static final String _Uid = "9795e88b-2ab4-4b81-a548-409091a1e0c6";
  
  public static final String genPreferenceFilename(Context paramContext)
  {
    Object[] arrayOfObject = new Object[2];
    arrayOfObject[0] = paramContext.getString(R.string.afc_lib_name);
    arrayOfObject[1] = "9795e88b-2ab4-4b81-a548-409091a1e0c6";
    return String.format("%s_%s", arrayOfObject);
  }
  
  public static SharedPreferences p(Context paramContext)
  {
    return paramContext.getApplicationContext().getSharedPreferences(genPreferenceFilename(paramContext), 4);
  }
  
  public static void setupPreferenceManager(Context paramContext, PreferenceManager paramPreferenceManager)
  {
    paramPreferenceManager.setSharedPreferencesMode(4);
    paramPreferenceManager.setSharedPreferencesName(genPreferenceFilename(paramContext));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.prefs.Prefs
 * JD-Core Version:    0.7.0.1
 */