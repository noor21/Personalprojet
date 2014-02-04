package org.acra.collector;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.acra.ACRA;

final class DeviceFeaturesCollector
{
  public static String getFeatures(Context paramContext)
  {
    Object localObject1;
    if (Compatibility.getAPILevel() < 5) {
      localObject1 = "Data available only with API Level >= 5";
    }
    for (;;)
    {
      return localObject1;
      localObject1 = new StringBuilder();
      try
      {
        PackageManager localPackageManager = paramContext.getPackageManager();
        Object[] arrayOfObject = (Object[])PackageManager.class.getMethod("getSystemAvailableFeatures", (Class[])null).invoke(localPackageManager, new Object[0]);
        int j = arrayOfObject.length;
        int i = 0;
        if (i < j)
        {
          Object localObject2 = arrayOfObject[i];
          String str = (String)localObject2.getClass().getField("name").get(localObject2);
          if (str != null) {
            ((StringBuilder)localObject1).append(str);
          }
          for (;;)
          {
            ((StringBuilder)localObject1).append("\n");
            i++;
            break;
            localObject2 = (String)localObject2.getClass().getMethod("getGlEsVersion", (Class[])null).invoke(localObject2, new Object[0]);
            ((StringBuilder)localObject1).append("glEsVersion = ");
            ((StringBuilder)localObject1).append((String)localObject2);
          }
        }
      }
      catch (Throwable localThrowable)
      {
        Log.w(ACRA.LOG_TAG, "Couldn't retrieve DeviceFeatures for " + paramContext.getPackageName(), localThrowable);
        ((StringBuilder)localObject1).append("Could not retrieve data: ");
        ((StringBuilder)localObject1).append(localThrowable.getMessage());
        localObject1 = ((StringBuilder)localObject1).toString();
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.DeviceFeaturesCollector
 * JD-Core Version:    0.7.0.1
 */