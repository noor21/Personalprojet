package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;

public class BoundedInputStream
  extends InputStream
{
  private final InputStream in;
  private long mark = -1L;
  private final long max;
  private long pos = 0L;
  private boolean propagateClose = true;
  
  public BoundedInputStream(InputStream paramInputStream)
  {
    this(paramInputStream, -1L);
  }
  
  public BoundedInputStream(InputStream paramInputStream, long paramLong)
  {
    this.max = paramLong;
    this.in = paramInputStream;
  }
  
  public int available()
    throws IOException
  {
    int i;
    if ((this.max < 0L) || (this.pos < this.max)) {
      i = this.in.available();
    } else {
      i = 0;
    }
    return i;
  }
  
  public void close()
    throws IOException
  {
    if (this.propagateClose) {
      this.in.close();
    }
  }
  
  public boolean isPropagateClose()
  {
    return this.propagateClose;
  }
  
  /**
   * @deprecated
   */
  public void mark(int paramInt)
  {
    try
    {
      this.in.mark(paramInt);
      this.mark = this.pos;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public boolean markSupported()
  {
    return this.in.markSupported();
  }
  
  public int read()
    throws IOException
  {
    int i;
    if ((this.max < 0L) || (this.pos < this.max))
    {
      i = this.in.read();
      this.pos = (1L + this.pos);
    }
    else
    {
      i = -1;
    }
    return i;
  }
  
  public int read(byte[] paramArrayOfByte)
    throws IOException
  {
    return read(paramArrayOfByte, 0, paramArrayOfByte.length);
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i;
    if ((this.max < 0L) || (this.pos < this.max))
    {
      long l;
      if (this.max < 0L) {
        l = paramInt2;
      } else {
        l = Math.min(paramInt2, this.max - this.pos);
      }
      i = this.in.read(paramArrayOfByte, paramInt1, (int)l);
      if (i != -1) {
        this.pos += i;
      } else {
        i = -1;
      }
    }
    else
    {
      i = -1;
    }
    return i;
  }
  
  /**
   * @deprecated
   */
  public void reset()
    throws IOException
  {
    try
    {
      this.in.reset();
      this.pos = this.mark;
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void setPropagateClose(boolean paramBoolean)
  {
    this.propagateClose = paramBoolean;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    if (this.max < 0L) {
      l = paramLong;
    } else {
      l = Math.min(paramLong, this.max - this.pos);
    }
    long l = this.in.skip(l);
    this.pos = (l + this.pos);
    return l;
  }
  
  public String toString()
  {
    return this.in.toString();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.BoundedInputStream
 * JD-Core Version:    0.7.0.1
 */