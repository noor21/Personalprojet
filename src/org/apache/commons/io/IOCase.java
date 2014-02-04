package org.apache.commons.io;

import java.io.Serializable;

public final class IOCase
  implements Serializable
{
  public static final IOCase INSENSITIVE;
  public static final IOCase SENSITIVE;
  public static final IOCase SYSTEM;
  private static final long serialVersionUID = -6343169151696340687L;
  private final String name;
  private final transient boolean sensitive;
  
  static
  {
    boolean bool = true;
    SENSITIVE = new IOCase("Sensitive", bool);
    INSENSITIVE = new IOCase("Insensitive", false);
    if (FilenameUtils.isSystemWindows()) {
      bool = false;
    }
    SYSTEM = new IOCase("System", bool);
  }
  
  private IOCase(String paramString, boolean paramBoolean)
  {
    this.name = paramString;
    this.sensitive = paramBoolean;
  }
  
  public static IOCase forName(String paramString)
  {
    IOCase localIOCase;
    if (!SENSITIVE.name.equals(paramString))
    {
      if (!INSENSITIVE.name.equals(paramString))
      {
        if (!SYSTEM.name.equals(paramString)) {
          throw new IllegalArgumentException("Invalid IOCase name: " + paramString);
        }
        localIOCase = SYSTEM;
      }
      else
      {
        localIOCase = INSENSITIVE;
      }
    }
    else {
      localIOCase = SENSITIVE;
    }
    return localIOCase;
  }
  
  private Object readResolve()
  {
    return forName(this.name);
  }
  
  public int checkCompareTo(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      int i;
      if (!this.sensitive) {
        i = paramString1.compareToIgnoreCase(paramString2);
      } else {
        i = paramString1.compareTo(paramString2);
      }
      return i;
    }
    throw new NullPointerException("The strings must not be null");
  }
  
  public boolean checkEndsWith(String paramString1, String paramString2)
  {
    int i = paramString2.length();
    boolean bool;
    if (this.sensitive) {
      bool = false;
    } else {
      bool = true;
    }
    return paramString1.regionMatches(bool, paramString1.length() - i, paramString2, 0, i);
  }
  
  public boolean checkEquals(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      boolean bool;
      if (!this.sensitive) {
        bool = paramString1.equalsIgnoreCase(paramString2);
      } else {
        bool = paramString1.equals(paramString2);
      }
      return bool;
    }
    throw new NullPointerException("The strings must not be null");
  }
  
  public int checkIndexOf(String paramString1, int paramInt, String paramString2)
  {
    int i = paramString1.length() - paramString2.length();
    if (i >= paramInt) {}
    for (int j = paramInt;; j++)
    {
      if (j > i)
      {
        j = -1;
        break;
      }
      if (checkRegionMatches(paramString1, j, paramString2)) {
        break;
      }
    }
    return j;
  }
  
  public boolean checkRegionMatches(String paramString1, int paramInt, String paramString2)
  {
    boolean bool;
    if (this.sensitive) {
      bool = false;
    } else {
      bool = true;
    }
    return paramString1.regionMatches(bool, paramInt, paramString2, 0, paramString2.length());
  }
  
  public boolean checkStartsWith(String paramString1, String paramString2)
  {
    boolean bool;
    if (this.sensitive) {
      bool = false;
    } else {
      bool = true;
    }
    return paramString1.regionMatches(bool, 0, paramString2, 0, paramString2.length());
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public boolean isCaseSensitive()
  {
    return this.sensitive;
  }
  
  public String toString()
  {
    return this.name;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.IOCase
 * JD-Core Version:    0.7.0.1
 */