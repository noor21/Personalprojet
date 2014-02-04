package org.acra.collector;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;
import org.acra.util.BoundedLinkedList;

class LogCatCollector
{
  private static final int DEFAULT_TAIL_COUNT = 100;
  
  public static String collectLogCat(String paramString)
  {
    int i = android.os.Process.myPid();
    String str = null;
    if ((ACRA.getConfig().logcatFilterByPid()) && (i > 0)) {
      str = Integer.toString(i) + "):";
    }
    Object localObject1 = new ArrayList();
    ((List)localObject1).add("logcat");
    if (paramString != null)
    {
      ((List)localObject1).add("-b");
      ((List)localObject1).add(paramString);
    }
    Object localObject2 = new ArrayList(Arrays.asList(ACRA.getConfig().logcatArguments()));
    int k = ((List)localObject2).indexOf("-t");
    label190:
    BoundedLinkedList localBoundedLinkedList;
    if ((k > -1) && (k < ((List)localObject2).size()))
    {
      i = Integer.parseInt((String)((List)localObject2).get(k + 1));
      if (Compatibility.getAPILevel() < 8)
      {
        ((List)localObject2).remove(k + 1);
        ((List)localObject2).remove(k);
        ((List)localObject2).add("-d");
      }
      if (i <= 0) {
        break label307;
      }
      localBoundedLinkedList = new BoundedLinkedList(i);
      ((List)localObject1).addAll((Collection)localObject2);
    }
    for (;;)
    {
      try
      {
        localObject2 = Runtime.getRuntime().exec((String[])((List)localObject1).toArray(new String[((List)localObject1).size()]));
        localObject1 = new BufferedReader(new InputStreamReader(((java.lang.Process)localObject2).getInputStream()), 8192);
        Log.d(ACRA.LOG_TAG, "Retrieving logcat output...");
        new Thread(new Runnable()
        {
          public void run()
          {
            try
            {
              InputStream localInputStream = LogCatCollector.this.getErrorStream();
              byte[] arrayOfByte = new byte[8192];
              int i;
              do
              {
                i = localInputStream.read(arrayOfByte);
              } while (i >= 0);
              label24:
              return;
            }
            catch (IOException localIOException)
            {
              break label24;
            }
          }
        }).start();
        localObject2 = ((BufferedReader)localObject1).readLine();
        if (localObject2 != null) {
          continue;
        }
      }
      catch (IOException localIOException)
      {
        int j;
        label307:
        Log.e(ACRA.LOG_TAG, "LogCatCollector.collectLogCat could not retrieve data.", localIOException);
        continue;
      }
      return localBoundedLinkedList.toString();
      j = -1;
      break;
      j = 100;
      break label190;
      if ((str == null) || (((String)localObject2).contains(str))) {
        j.add((String)localObject2 + "\n");
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.LogCatCollector
 * JD-Core Version:    0.7.0.1
 */