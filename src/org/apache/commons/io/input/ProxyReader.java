package org.apache.commons.io.input;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

public abstract class ProxyReader
  extends FilterReader
{
  public ProxyReader(Reader paramReader)
  {
    super(paramReader);
  }
  
  protected void afterRead(int paramInt)
    throws IOException
  {}
  
  protected void beforeRead(int paramInt)
    throws IOException
  {}
  
  public void close()
    throws IOException
  {
    try
    {
      this.in.close();
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
      }
    }
  }
  
  protected void handleIOException(IOException paramIOException)
    throws IOException
  {
    throw paramIOException;
  }
  
  /**
   * @deprecated
   */
  public void mark(int paramInt)
    throws IOException
  {
    try
    {
      this.in.mark(paramInt);
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
      }
    }
    finally {}
  }
  
  public boolean markSupported()
  {
    return this.in.markSupported();
  }
  
  public int read()
    throws IOException
  {
    int i = 1;
    try
    {
      beforeRead(1);
      j = this.in.read();
      if (j != -1) {}
      for (;;)
      {
        afterRead(i);
        return j;
        i = -1;
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
        int j = -1;
      }
    }
  }
  
  public int read(CharBuffer paramCharBuffer)
    throws IOException
  {
    if (paramCharBuffer != null) {}
    try
    {
      for (int i = paramCharBuffer.length();; i = 0)
      {
        beforeRead(i);
        i = this.in.read(paramCharBuffer);
        afterRead(i);
        return i;
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
        int j = -1;
      }
    }
  }
  
  public int read(char[] paramArrayOfChar)
    throws IOException
  {
    if (paramArrayOfChar != null) {}
    try
    {
      for (int i = paramArrayOfChar.length;; i = 0)
      {
        beforeRead(i);
        i = this.in.read(paramArrayOfChar);
        afterRead(i);
        return i;
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
        int j = -1;
      }
    }
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      beforeRead(paramInt2);
      int i = this.in.read(paramArrayOfChar, paramInt1, paramInt2);
      afterRead(i);
      return i;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
        int j = -1;
      }
    }
  }
  
  public boolean ready()
    throws IOException
  {
    try
    {
      bool = this.in.ready();
      bool = bool;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        boolean bool;
        handleIOException(localIOException);
        int i = 0;
      }
    }
    return bool;
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
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
      }
    }
    finally {}
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    try
    {
      l1 = this.in.skip(paramLong);
      l1 = l1;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        long l1;
        handleIOException(localIOException);
        long l2 = 0L;
      }
    }
    return l1;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ProxyReader
 * JD-Core Version:    0.7.0.1
 */