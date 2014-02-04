package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class PrefixFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOCase caseSensitivity;
  private final String[] prefixes;
  
  public PrefixFileFilter(String paramString)
  {
    this(paramString, IOCase.SENSITIVE);
  }
  
  public PrefixFileFilter(String paramString, IOCase paramIOCase)
  {
    if (paramString != null)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      this.prefixes = arrayOfString;
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The prefix must not be null");
  }
  
  public PrefixFileFilter(List<String> paramList)
  {
    this(paramList, IOCase.SENSITIVE);
  }
  
  public PrefixFileFilter(List<String> paramList, IOCase paramIOCase)
  {
    if (paramList != null)
    {
      this.prefixes = ((String[])paramList.toArray(new String[paramList.size()]));
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The list of prefixes must not be null");
  }
  
  public PrefixFileFilter(String[] paramArrayOfString)
  {
    this(paramArrayOfString, IOCase.SENSITIVE);
  }
  
  public PrefixFileFilter(String[] paramArrayOfString, IOCase paramIOCase)
  {
    if (paramArrayOfString != null)
    {
      this.prefixes = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, this.prefixes, 0, paramArrayOfString.length);
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The array of prefixes must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    String str2 = paramFile.getName();
    String[] arrayOfString = this.prefixes;
    int k = arrayOfString.length;
    for (int j = 0;; j++)
    {
      if (j >= k)
      {
        int i = 0;
        return ???;
      }
      String str1 = arrayOfString[j];
      if (this.caseSensitivity.checkStartsWith(str2, str1)) {
        break;
      }
    }
    boolean bool = true;
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    String[] arrayOfString = this.prefixes;
    int i = arrayOfString.length;
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return 0;
      }
      String str = arrayOfString[j];
      if (this.caseSensitivity.checkStartsWith(paramString, str)) {
        break;
      }
    }
    i = 1;
    return i;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(super.toString());
    localStringBuilder.append("(");
    if (this.prefixes != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.prefixes.length)
      {
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append(this.prefixes[i]);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.PrefixFileFilter
 * JD-Core Version:    0.7.0.1
 */