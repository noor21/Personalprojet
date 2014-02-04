package org.acra.collector;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.acra.ACRA;
import org.acra.ACRAConfiguration;

final class SharedPreferencesCollector
{
  public static String collect(Context paramContext)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    TreeMap localTreeMap = new TreeMap();
    localTreeMap.put("default", PreferenceManager.getDefaultSharedPreferences(paramContext));
    Object localObject = ACRA.getConfig().additionalSharedPreferences();
    int i;
    if (localObject != null) {
      i = localObject.length;
    }
    Iterator localIterator2;
    for (int j = 0;; localIterator2++)
    {
      if (j >= i)
      {
        Iterator localIterator1 = localTreeMap.keySet().iterator();
        if (!localIterator1.hasNext()) {
          return localStringBuilder.toString();
        }
        str1 = (String)localIterator1.next();
        localObject = (SharedPreferences)localTreeMap.get(str1);
        if (localObject == null)
        {
          localStringBuilder.append("null\n");
        }
        else
        {
          localObject = ((SharedPreferences)localObject).getAll();
          if ((localObject == null) || (((Map)localObject).size() <= 0)) {
            localStringBuilder.append(str1).append('=').append("empty\n");
          } else {
            localIterator2 = ((Map)localObject).keySet().iterator();
          }
        }
        for (;;)
        {
          if (!localIterator2.hasNext())
          {
            localStringBuilder.append('\n');
            break;
          }
          String str2 = (String)localIterator2.next();
          if (!filteredKey(str2)) {
            if (((Map)localObject).get(str2) == null) {
              localStringBuilder.append(str1).append('.').append(str2).append('=').append("null\n");
            } else {
              localStringBuilder.append(str1).append('.').append(str2).append('=').append(((Map)localObject).get(str2).toString()).append("\n");
            }
          }
        }
      }
      String str1 = localObject[localIterator2];
      localTreeMap.put(str1, paramContext.getSharedPreferences(str1, 0));
    }
  }
  
  private static boolean filteredKey(String paramString)
  {
    String[] arrayOfString = ACRA.getConfig().excludeMatchingSharedPreferencesKeys();
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return false;
      }
      if (paramString.matches(bool[i])) {
        break;
      }
    }
    boolean bool = true;
    return bool;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.SharedPreferencesCollector
 * JD-Core Version:    0.7.0.1
 */