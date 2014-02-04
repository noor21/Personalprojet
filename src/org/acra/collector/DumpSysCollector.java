package org.acra.collector;

import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.acra.ACRA;

final class DumpSysCollector
{
  public static String collectMemInfo()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (;;)
    {
      try
      {
        localObject = new ArrayList();
        ((List)localObject).add("dumpsys");
        ((List)localObject).add("meminfo");
        ((List)localObject).add(Integer.toString(android.os.Process.myPid()));
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec((String[])((List)localObject).toArray(new String[((List)localObject).size()])).getInputStream()), 8192);
        localObject = localBufferedReader.readLine();
        if (localObject != null) {
          continue;
        }
      }
      catch (IOException localIOException)
      {
        Object localObject;
        Log.e(ACRA.LOG_TAG, "DumpSysCollector.meminfo could not retrieve data", localIOException);
        continue;
      }
      return localStringBuilder.toString();
      localStringBuilder.append((String)localObject);
      localStringBuilder.append("\n");
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.DumpSysCollector
 * JD-Core Version:    0.7.0.1
 */