package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.IOCase;

public class SuffixFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOCase caseSensitivity;
  private final String[] suffixes;
  
  public SuffixFileFilter(String paramString)
  {
    this(paramString, IOCase.SENSITIVE);
  }
  
  public SuffixFileFilter(String paramString, IOCase paramIOCase)
  {
    if (paramString != null)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      this.suffixes = arrayOfString;
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The suffix must not be null");
  }
  
  public SuffixFileFilter(List<String> paramList)
  {
    this(paramList, IOCase.SENSITIVE);
  }
  
  public SuffixFileFilter(List<String> paramList, IOCase paramIOCase)
  {
    if (paramList != null)
    {
      this.suffixes = ((String[])paramList.toArray(new String[paramList.size()]));
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The list of suffixes must not be null");
  }
  
  public SuffixFileFilter(String[] paramArrayOfString)
  {
    this(paramArrayOfString, IOCase.SENSITIVE);
  }
  
  public SuffixFileFilter(String[] paramArrayOfString, IOCase paramIOCase)
  {
    if (paramArrayOfString != null)
    {
      this.suffixes = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, this.suffixes, 0, paramArrayOfString.length);
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The array of suffixes must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    String str1 = paramFile.getName();
    String[] arrayOfString = this.suffixes;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return 0;
      }
      String str2 = arrayOfString[i];
      if (this.caseSensitivity.checkEndsWith(str1, str2)) {
        break;
      }
    }
    i = 1;
    return i;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    String[] arrayOfString = this.suffixes;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return false;
      }
      String str = bool[i];
      if (this.caseSensitivity.checkEndsWith(paramString, str)) {
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
    if (this.suffixes != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.suffixes.length)
      {
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append(this.suffixes[i]);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.SuffixFileFilter
 * JD-Core Version:    0.7.0.1
 */