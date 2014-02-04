package org.acra.collector;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

final class ReflectionCollector
{
  public static String collectConstants(Class<?> paramClass)
  {
    return collectConstants(paramClass, "");
  }
  
  public static String collectConstants(Class<?> paramClass, String paramString)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Field[] arrayOfField = paramClass.getFields();
    int j = arrayOfField.length;
    int i = 0;
    for (;;)
    {
      if (i < j)
      {
        Field localField = arrayOfField[i];
        if ((paramString != null) && (paramString.length() > 0)) {
          localStringBuilder.append(paramString).append('.');
        }
        localStringBuilder.append(localField.getName()).append("=");
        try
        {
          localStringBuilder.append(localField.get(null).toString());
          localStringBuilder.append("\n");
          i++;
        }
        catch (IllegalArgumentException localIllegalArgumentException)
        {
          for (;;)
          {
            localStringBuilder.append("N/A");
          }
        }
        catch (IllegalAccessException localIllegalAccessException)
        {
          for (;;)
          {
            localStringBuilder.append("N/A");
          }
        }
      }
    }
    return localStringBuilder.toString();
  }
  
  public static String collectStaticGettersResults(Class<?> paramClass)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Method[] arrayOfMethod = paramClass.getMethods();
    int i = arrayOfMethod.length;
    int j = 0;
    for (;;)
    {
      Method localMethod;
      if (j < i)
      {
        localMethod = arrayOfMethod[j];
        if ((localMethod.getParameterTypes().length != 0) || ((!localMethod.getName().startsWith("get")) && (!localMethod.getName().startsWith("is"))) || (localMethod.getName().equals("getClass"))) {}
      }
      try
      {
        localStringBuilder.append(localMethod.getName());
        localStringBuilder.append('=');
        localStringBuilder.append(localMethod.invoke(null, (Object[])null));
        localStringBuilder.append("\n");
        label116:
        j++;
        continue;
        return localStringBuilder.toString();
      }
      catch (InvocationTargetException localInvocationTargetException)
      {
        break label116;
      }
      catch (IllegalAccessException localIllegalAccessException)
      {
        break label116;
      }
      catch (IllegalArgumentException localIllegalArgumentException)
      {
        break label116;
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.collector.ReflectionCollector
 * JD-Core Version:    0.7.0.1
 */