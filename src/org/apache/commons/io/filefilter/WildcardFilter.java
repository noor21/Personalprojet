package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

@Deprecated
public class WildcardFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final String[] wildcards;
  
  public WildcardFilter(String paramString)
  {
    if (paramString != null)
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = paramString;
      this.wildcards = arrayOfString;
      return;
    }
    throw new IllegalArgumentException("The wildcard must not be null");
  }
  
  public WildcardFilter(List<String> paramList)
  {
    if (paramList != null)
    {
      this.wildcards = ((String[])paramList.toArray(new String[paramList.size()]));
      return;
    }
    throw new IllegalArgumentException("The wildcard list must not be null");
  }
  
  public WildcardFilter(String[] paramArrayOfString)
  {
    if (paramArrayOfString != null)
    {
      this.wildcards = new String[paramArrayOfString.length];
      System.arraycopy(paramArrayOfString, 0, this.wildcards, 0, paramArrayOfString.length);
      return;
    }
    throw new IllegalArgumentException("The wildcard array must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool = false;
    if (!paramFile.isDirectory())
    {
      String[] arrayOfString = this.wildcards;
      int i = arrayOfString.length;
      int j = 0;
      while (j < i)
      {
        String str = arrayOfString[j];
        if (!FilenameUtils.wildcardMatch(paramFile.getName(), str)) {
          j++;
        } else {
          bool = true;
        }
      }
    }
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    boolean bool = false;
    if ((paramFile == null) || (!new File(paramFile, paramString).isDirectory()))
    {
      String[] arrayOfString = this.wildcards;
      int i = arrayOfString.length;
      int j = 0;
      while (j < i) {
        if (!FilenameUtils.wildcardMatch(paramString, arrayOfString[j])) {
          j++;
        } else {
          bool = true;
        }
      }
    }
    return bool;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.WildcardFilter
 * JD-Core Version:    0.7.0.1
 */