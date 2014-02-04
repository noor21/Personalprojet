package org.apache.commons.io.output;

import java.io.OutputStream;

public class CountingOutputStream
  extends ProxyOutputStream
{
  private long count = 0L;
  
  public CountingOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }
  
  /**
   * @deprecated
   */
  protected void beforeWrite(int paramInt)
  {
    try
    {
      this.count += paramInt;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /**
   * @deprecated
   */
  public long getByteCount()
  {
    try
    {
      long l = this.count;
      return l;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int getCount()
  {
    long l = getByteCount();
    if (l <= 2147483647L) {
      return (int)l;
    }
    throw new ArithmeticException("The byte count " + l + " is too large to be converted to an int");
  }
  
  /**
   * @deprecated
   */
  public long resetByteCount()
  {
    try
    {
      long l = this.count;
      this.count = 0L;
      return l;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public int resetCount()
  {
    long l = resetByteCount();
    if (l <= 2147483647L) {
      return (int)l;
    }
    throw new ArithmeticException("The byte count " + l + " is too large to be converted to an int");
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.CountingOutputStream
 * JD-Core Version:    0.7.0.1
 */