package com.kharybdis.hitchernet;

import java.io.PrintStream;

public class FileName
{
  public static String getExtension(String paramString)
  {
    Object localObject = paramString.split("\\.");
    System.out.println("size=" + localObject.length);
    if (localObject.length <= 1) {
      localObject = "";
    } else {
      localObject = "." + localObject[(-1 + localObject.length)];
    }
    return localObject;
  }
  
  public static int getExtensionPosition(String paramString)
  {
    String[] arrayOfString = paramString.split("\\.");
    System.out.println("size=" + arrayOfString.length);
    int i;
    if (arrayOfString.length <= 1)
    {
      i = -1;
    }
    else
    {
      i = i[(-1 + i.length)].length();
      i = -1 + (paramString.length() - i);
    }
    return i;
  }
  
  public static String newFileName(String paramString, int paramInt)
  {
    int i = getExtensionPosition(paramString);
    String str;
    if (i == -1) {
      str = paramString + "(1)";
    } else {
      str = paramString.substring(0, str) + "(" + paramInt + ")" + "." + paramString.substring(str + 1);
    }
    return str;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.FileName
 * JD-Core Version:    0.7.0.1
 */