package org.acra.collector;

import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;

final class SettingsCollector
{
  public static String collectGlobalSettings(Context paramContext)
  {
    Object localObject1;
    if (Compatibility.getAPILevel() < 17) {
      localObject1 = "";
    }
    for (;;)
    {
      return localObject1;
      localObject1 = new StringBuilder();
      try
      {
        Class localClass = Class.forName("android.provider.Settings$Global");
        Field[] arrayOfField = localClass.getFields();
        Class[] arrayOfClass = new Class[2];
        arrayOfClass[0] = ContentResolver.class;
        arrayOfClass[1] = String.class;
        Method localMethod = localClass.getMethod("getString", arrayOfClass);
        int i = arrayOfField.length;
        for (int j = 0; j < i; j++)
        {
          Field localField = arrayOfField[j];
          if ((!localField.isAnnotationPresent(Deprecated.class)) && (localField.getType() == String.class) && (isAuthorized(localField)))
          {
            Object localObject2 = new Object[2];
            localObject2[0] = paramContext.getContentResolver();
            localObject2[1] = ((String)localField.get(null));
            localObject2 = localMethod.invoke(null, (Object[])localObject2);
            if (localObject2 != null) {
              ((StringBuilder)localObject1).append(localField.getName()).append("=").append(localObject2).append("\n");
            }
          }
        }
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        Log.w(ACRA.LOG_TAG, "Error : ", localIllegalArgumentException);
        localObject1 = ((StringBuilder)localObject1).toString();
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Error : ", localIllegalAccessException);
        }
      }
      catch (ClassNotFoundException localClassNotFoundException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Error : ", localClassNotFoundException);
        }
      }
      catch (SecurityException localSecurityException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Error : ", localSecurityException);
        }
      }
      catch (NoSuchMethodException localNoSuchMethodException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Error : ", localNoSuchMethodException);
        }
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Error : ", localInvocationTargetException);
        }
      }
    }
  }
  
  public static String collectSecureSettings(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Field[] arrayOfField = Settings.Secure.class.getFields();
    int i = arrayOfField.length;
    int j = 0;
    for (;;)
    {
      if (j < i)
      {
        Field localField = arrayOfField[j];
        if ((!localField.isAnnotationPresent(Deprecated.class)) && (localField.getType() == String.class) && (isAuthorized(localField))) {}
        try
        {
          String str = Settings.Secure.getString(paramContext.getContentResolver(), (String)localField.get(null));
          if (str != null) {
            localStringBuilder.append(localField.getName()).append("=").append(str).append("\n");
          }
          j++;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          for (;;)
          {
            Log.w(ACRA.LOG_TAG, "Error : ", localIllegalArgumentException);
          }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          for (;;)
          {
            Log.w(ACRA.LOG_TAG, "Error : ", localIllegalAccessException);
          }
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String collectSystemSettings(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Field[] arrayOfField = Settings.System.class.getFields();
    int j = arrayOfField.length;
    int i = 0;
    for (;;)
    {
      if (i < j)
      {
        Field localField = arrayOfField[i];
        if ((!localField.isAnnotationPresent(Deprecated.class)) && (localField.getType() == String.class)) {}
        try
        {
          String str = Settings.System.getString(paramContext.getContentResolver(), (String)localField.get(null));
          if (str != null) {
            localStringBuilder.append(localField.getName()).append("=").append(str).append("\n");
          }
          i++;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          for (;;)
          {
            Log.w(ACRA.LOG_TAG, "Error : ", localIllegalArgumentException);
          }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          for (;;)
          {
            Log.w(ACRA.LOG_TAG, "Error : ", localIllegalAccessException);
          }
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  private static boolean isAuthorized(Field paramField)
  {
    boolean bool = false;
    String[] arrayOfString;
    int i;
    if ((paramField != null) && (!paramField.getName().startsWith("WIFI_AP")))
    {
      arrayOfString = ACRA.getConfig().excludeMatchingSettingsKeys();
      i = arrayOfString.length;
    }
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        bool = true;
      }
      else
      {
        String str = arrayOfString[j];
        if (!paramField.getName().matches(str)) {
          continue;
        }
      }
      return bool;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.SettingsCollector
 * JD-Core Version:    0.7.0.1
 */