package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class NameFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOCase caseSensitivity;
  private final String[] names;
  
  public NameFileFilter(String paramString)
  {
    this(paramString, null);
  }
  
  public NameFileFilter(String paramString, IOCase paramIOCase)
  {
    if (paramString != null)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      this.names = arrayOfString;
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The wildcard must not be null");
  }
  
  public NameFileFilter(List<String> paramList)
  {
    this(paramList, null);
  }
  
  public NameFileFilter(List<String> paramList, IOCase paramIOCase)
  {
    if (paramList != null)
    {
      this.names = ((String[])paramList.toArray(new String[paramList.size()]));
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The list of names must not be null");
  }
  
  public NameFileFilter(String[] paramArrayOfString)
  {
    this(paramArrayOfString, null);
  }
  
  public NameFileFilter(String[] paramArrayOfString, IOCase paramIOCase)
  {
    if (paramArrayOfString != null)
    {
      this.names = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, this.names, 0, paramArrayOfString.length);
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The array of names must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    String str1 = paramFile.getName();
    String[] arrayOfString = this.names;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return false;
      }
      String str2 = bool[i];
      if (this.caseSensitivity.checkEquals(str1, str2)) {
        break;
      }
    }
    boolean bool = true;
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    String[] arrayOfString = this.names;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return false;
      }
      String str = bool[i];
      if (this.caseSensitivity.checkEquals(paramString, str)) {
        break;
      }
    }
    boolean bool = true;
    return bool;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append("(");
    if (this.names != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.names.length)
      {
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append(this.names[i]);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.NameFileFilter
 * JD-Core Version:    0.7.0.1
 */