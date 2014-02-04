package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class NotFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final IOFileFilter filter;
  
  public NotFileFilter(IOFileFilter paramIOFileFilter)
  {
    if (paramIOFileFilter != null)
    {
      this.filter = paramIOFileFilter;
      return;
    }
    throw new IllegalArgumentException("The filter must not be null");
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool;
    if (this.filter.accept(paramFile)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean accept(File paramFile, String paramString)
  {
    boolean bool;
    if (this.filter.accept(paramFile, paramString)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public String toString()
  {
    return super.toString() + "(" + this.filter.toString() + ")";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.NotFileFilter
 * JD-Core Version:    0.7.0.1
 */