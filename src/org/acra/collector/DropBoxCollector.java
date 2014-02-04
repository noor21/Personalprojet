package org.acra.collector;

import android.content.Context;
import android.text.format.Time;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;

final class DropBoxCollector
{
  private static final String NO_RESULT = "N/A";
  private static final String[] SYSTEM_TAGS;
  
  static
  {
    String[] arrayOfString = new String[15];
    arrayOfString[0] = "system_app_anr";
    arrayOfString[1] = "system_app_wtf";
    arrayOfString[2] = "system_app_crash";
    arrayOfString[3] = "system_server_anr";
    arrayOfString[4] = "system_server_wtf";
    arrayOfString[5] = "system_server_crash";
    arrayOfString[6] = "BATTERY_DISCHARGE_INFO";
    arrayOfString[7] = "SYSTEM_RECOVERY_LOG";
    arrayOfString[8] = "SYSTEM_BOOT";
    arrayOfString[9] = "SYSTEM_LAST_KMSG";
    arrayOfString[10] = "APANIC_CONSOLE";
    arrayOfString[11] = "APANIC_THREADS";
    arrayOfString[12] = "SYSTEM_RESTART";
    arrayOfString[13] = "SYSTEM_TOMBSTONE";
    arrayOfString[14] = "data_app_strictmode";
    SYSTEM_TAGS = arrayOfString;
  }
  
  public static String read(Context paramContext, String[] paramArrayOfString)
  {
    Object localObject1;
    try
    {
      localObject1 = Compatibility.getDropBoxServiceName();
      if (localObject1 == null)
      {
        localObject1 = "N/A";
      }
      else
      {
        localObject1 = paramContext.getSystemService((String)localObject1);
        Class localClass1 = localObject1.getClass();
        Class[] arrayOfClass1 = new Class[2];
        arrayOfClass1[0] = String.class;
        arrayOfClass1[1] = Long.TYPE;
        localMethod1 = localClass1.getMethod("getNextEntry", arrayOfClass1);
        if (localMethod1 == null)
        {
          localObject1 = "";
        }
        else
        {
          localTime = new Time();
          localTime.setToNow();
          localTime.minute -= ACRA.getConfig().dropboxCollectionMinutes();
          localTime.normalize(false);
          long l1 = localTime.toMillis(false);
          localObject2 = new ArrayList();
          if (ACRA.getConfig().includeDropBoxSystemTags()) {
            ((List)localObject2).addAll(Arrays.asList(SYSTEM_TAGS));
          }
          if ((paramArrayOfString != null) && (paramArrayOfString.length > 0)) {
            ((List)localObject2).addAll(Arrays.asList(paramArrayOfString));
          }
          if (((List)localObject2).isEmpty())
          {
            localObject1 = "No tag configured for collection.";
          }
          else
          {
            localStringBuilder = new StringBuilder();
            Iterator localIterator = ((List)localObject2).iterator();
            for (;;)
            {
              if (!localIterator.hasNext()) {
                break label545;
              }
              localObject2 = (String)localIterator.next();
              localStringBuilder.append("Tag: ").append((String)localObject2).append('\n');
              localObject3 = new Object[2];
              localObject3[0] = localObject2;
              localObject3[1] = Long.valueOf(l1);
              localObject3 = localMethod1.invoke(localObject1, (Object[])localObject3);
              if (localObject3 != null) {
                break;
              }
              localStringBuilder.append("Nothing.").append('\n');
            }
          }
        }
      }
    }
    catch (SecurityException localSecurityException)
    {
      Method localMethod1;
      Time localTime;
      Object localObject2;
      Object localObject3;
      Method localMethod2;
      Method localMethod3;
      Method localMethod4;
      do
      {
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
        localObject1 = "N/A";
        break;
        Class localClass2 = localObject3.getClass();
        Class[] arrayOfClass2 = new Class[1];
        arrayOfClass2[0] = Integer.TYPE;
        localMethod2 = localClass2.getMethod("getText", arrayOfClass2);
        localMethod3 = localObject3.getClass().getMethod("getTimeMillis", (Class[])null);
        localMethod4 = localObject3.getClass().getMethod("close", (Class[])null);
      } while (localObject3 == null);
      long l2 = ((Long)localMethod3.invoke(localObject3, (Object[])null)).longValue();
      localTime.set(l2);
      localStringBuilder.append("@").append(localTime.format2445()).append('\n');
      Object localObject4 = new Object[1];
      localObject4[0] = Integer.valueOf(500);
      localObject4 = (String)localMethod2.invoke(localObject3, (Object[])localObject4);
      if (localObject4 != null) {
        localStringBuilder.append("Text: ").append((String)localObject4).append('\n');
      }
      for (;;)
      {
        localMethod4.invoke(localObject3, (Object[])null);
        localObject3 = new Object[2];
        localObject3[0] = localObject2;
        localObject3[1] = Long.valueOf(l2);
        localObject3 = localMethod1.invoke(localObject1, (Object[])localObject3);
        break;
        localStringBuilder.append("Not Text!").append('\n');
      }
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      StringBuilder localStringBuilder;
      for (;;)
      {
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
      }
      localObject1 = localStringBuilder.toString();
      localObject1 = localObject1;
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
      }
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      for (;;)
      {
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
      }
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      for (;;)
      {
        label545:
        Log.i(ACRA.LOG_TAG, "DropBoxManager not available.");
      }
    }
    return localObject1;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.DropBoxCollector
 * JD-Core Version:    0.7.0.1
 */