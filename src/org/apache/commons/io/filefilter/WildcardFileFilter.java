package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;

public class WildcardFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOCase caseSensitivity;
  private final String[] wildcards;
  
  public WildcardFileFilter(String paramString)
  {
    this(paramString, null);
  }
  
  public WildcardFileFilter(String paramString, IOCase paramIOCase)
  {
    if (paramString != null)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      this.wildcards = arrayOfString;
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The wildcard must not be null");
  }
  
  public WildcardFileFilter(List<String> paramList)
  {
    this(paramList, null);
  }
  
  public WildcardFileFilter(List<String> paramList, IOCase paramIOCase)
  {
    if (paramList != null)
    {
      this.wildcards = ((String[])paramList.toArray(new String[paramList.size()]));
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The wildcard list must not be null");
  }
  
  public WildcardFileFilter(String[] paramArrayOfString)
  {
    this(paramArrayOfString, null);
  }
  
  public WildcardFileFilter(String[] paramArrayOfString, IOCase paramIOCase)
  {
    if (paramArrayOfString != null)
    {
      this.wildcards = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, this.wildcards, 0, paramArrayOfString.length);
      if (paramIOCase == null) {
        paramIOCase = IOCase.SENSITIVE;
      }
      this.caseSensitivity = paramIOCase;
      return;
    }
    throw new IllegalArgumentException("The wildcard array must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    String str = paramFile.getName();
    String[] arrayOfString = this.wildcards;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return false;
      }
      if (FilenameUtils.wildcardMatch(bool, arrayOfString[i], this.caseSensitivity)) {
        break;
      }
    }
    boolean bool = true;
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    String[] arrayOfString = this.wildcards;
    int j = arrayOfString.length;
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return 0;
      }
      if (FilenameUtils.wildcardMatch(paramString, arrayOfString[i], this.caseSensitivity)) {
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
    if (this.wildcards != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.wildcards.length)
      {
        localStringBuilder.append(")");
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append(this.wildcards[i]);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.WildcardFileFilter
 * JD-Core Version:    0.7.0.1
 */