package org.apache.commons.io.filefilter;

import java.io.File;
import java.io.Serializable;

public class SizeFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private final boolean acceptLarger;
  private final long size;
  
  public SizeFileFilter(long paramLong)
  {
    this(paramLong, true);
  }
  
  public SizeFileFilter(long paramLong, boolean paramBoolean)
  {
    if (paramLong >= 0L)
    {
      this.size = paramLong;
      this.acceptLarger = paramBoolean;
      return;
    }
    throw new IllegalArgumentException("The size must be non-negative");
  }
  
  public boolean accept(File paramFile)
  {
    boolean bool1 = true;
    boolean bool2;
    if (paramFile.length() >= this.size) {
      bool2 = false;
    } else {
      bool2 = bool1;
    }
    if (!this.acceptLarger) {
      bool1 = bool2;
    } else if (bool2) {
      bool1 = false;
    }
    return bool1;
  }
  
  public String toString()
  {
    String str;
    if (!this.acceptLarger) {
      str = "<";
    } else {
      str = ">=";
    }
    return super.toString() + "(" + str + this.size + ")";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.SizeFileFilter
 * JD-Core Version:    0.7.0.1
 */