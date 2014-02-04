package org.acra.collector;

import android.content.Context;
import android.os.Build.VERSION;
import java.lang.reflect.Field;

public final class Compatibility
{
  public static int getAPILevel()
  {
    try
    {
      i = Build.VERSION.class.getField("SDK_INT").getInt(null);
      i = i;
    }
    catch (SecurityException localSecurityException)
    {
      for (;;)
      {
        i = Integer.parseInt(Build.VERSION.SDK);
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;)
      {
        i = Integer.parseInt(Build.VERSION.SDK);
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        i = Integer.parseInt(Build.VERSION.SDK);
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        int i = Integer.parseInt(Build.VERSION.SDK);
      }
    }
    return i;
  }
  
  public static String getDropBoxServiceName()
    throws NoSuchFieldException, IllegalAccessException
  {
    String str = null;
    Field localField = Context.class.getField("DROPBOX_SERVICE");
    if (localField != null) {
      str = (String)localField.get(null);
    }
    return str;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.Compatibility
 * JD-Core Version:    0.7.0.1
 */