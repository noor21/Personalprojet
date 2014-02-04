package com.kharybdis.hitchernet;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;

public class Preferences
{
  private static final String APP_SHARED_PREFS = "com.bajratech.hitchernet_preferences";
  private static final String DEVICE_NAME = "thisDeviceName";
  private static final String FIRST_START = "firstStart";
  private static final String FOLDER_SAVE_PATH = "fileSavingLocation";
  private SharedPreferences appSharedPrefs;
  private Context context;
  private SharedPreferences.Editor prefsEditor;
  
  public Preferences(Context paramContext)
  {
    this.appSharedPrefs = paramContext.getSharedPreferences("com.bajratech.hitchernet_preferences", 0);
    this.prefsEditor = this.appSharedPrefs.edit();
    this.context = paramContext;
  }
  
  public String getFileSavingLocation()
  {
    String str = Environment.getExternalStorageDirectory() + "/HitcherNet/";
    return this.appSharedPrefs.getString("fileSavingLocation", str);
  }
  
  public String getIPFromMac(String paramString)
  {
    return this.appSharedPrefs.getString(paramString, "");
  }
  
  public String getThisDeviceName()
  {
    return this.appSharedPrefs.getString("thisDeviceName", "");
  }
  
  public boolean isFirstStart()
  {
    return this.appSharedPrefs.getBoolean("firstStart", true);
  }
  
  public void setFileSaveLocation(String paramString)
  {
    this.prefsEditor.putString("fileSavingLocation", paramString);
    this.prefsEditor.commit();
  }
  
  public void setFirstStart(boolean paramBoolean)
  {
    this.prefsEditor.putBoolean("firstStart", paramBoolean);
    this.prefsEditor.commit();
  }
  
  public void setIPForMac(String paramString1, String paramString2)
  {
    this.prefsEditor.putString(paramString1, paramString2);
    this.prefsEditor.commit();
  }
  
  public void setThisDeviceName(String paramString)
  {
    this.prefsEditor.putString("thisDeviceName", paramString);
    this.prefsEditor.commit();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.Preferences
 * JD-Core Version:    0.7.0.1
 */