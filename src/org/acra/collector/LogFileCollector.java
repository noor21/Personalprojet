package org.acra.collector;

import android.content.Context;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.acra.util.BoundedLinkedList;

class LogFileCollector
{
  public static String collectLogFile(Context paramContext, String paramString, int paramInt)
    throws IOException
  {
    BoundedLinkedList localBoundedLinkedList = new BoundedLinkedList(paramInt);
    BufferedReader localBufferedReader;
    if (!paramString.contains("/")) {
      localBufferedReader = new BufferedReader(new InputStreamReader(paramContext.openFileInput(paramString)), 1024);
    } else {
      localBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(paramString)), 1024);
    }
    for (String str = localBufferedReader.readLine();; str = localBufferedReader.readLine())
    {
      if (str == null) {
        return localBoundedLinkedList.toString();
      }
      localBoundedLinkedList.add(str + "\n");
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.LogFileCollector
 * JD-Core Version:    0.7.0.1
 */