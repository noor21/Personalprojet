package org.acra.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import org.acra.ACRA;

public final class PackageManagerWrapper
{
  private final Context context;
  
  public PackageManagerWrapper(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public PackageInfo getPackageInfo()
  {
    PackageInfo localPackageInfo = null;
    PackageManager localPackageManager = this.context.getPackageManager();
    if (localPackageManager == null) {}
    for (;;)
    {
      return localPackageInfo;
      try
      {
        localPackageInfo = localPackageManager.getPackageInfo(this.context.getPackageName(), 0);
        localPackageInfo = localPackageInfo;
      }
      catch (PackageManager.NameNotFoundException localNameNotFoundException)
      {
        Log.v(ACRA.LOG_TAG, "Failed to find PackageInfo for current App : " + this.context.getPackageName());
      }
      catch (RuntimeException localRuntimeException) {}
    }
  }
  
  public boolean hasPermission(String paramString)
  {
    boolean bool = false;
    PackageManager localPackageManager = this.context.getPackageManager();
    if (localPackageManager == null) {}
    for (;;)
    {
      return bool;
      try
      {
        int i = localPackageManager.checkPermission(paramString, this.context.getPackageName());
        if (i == 0) {
          bool = true;
        }
      }
      catch (RuntimeException localRuntimeException) {}
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.PackageManagerWrapper
 * JD-Core Version:    0.7.0.1
 */