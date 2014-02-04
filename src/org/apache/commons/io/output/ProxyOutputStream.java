package org.apache.commons.io.output;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProxyOutputStream
  extends FilterOutputStream
{
  public ProxyOutputStream(OutputStream paramOutputStream)
  {
    super(paramOutputStream);
  }
  
  protected void afterWrite(int paramInt)
    throws IOException
  {}
  
  protected void beforeWrite(int paramInt)
    throws IOException
  {}
  
  public void close()
    throws IOException
  {
    try
    {
      this.out.close();
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
  
  public void flush()
    throws IOException
  {
    try
    {
      this.out.flush();
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
  
  public void write(int paramInt)
    throws IOException
  {
    try
    {
      beforeWrite(1);
      this.out.write(paramInt);
      afterWrite(1);
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
  
  public void write(byte[] paramArrayOfByte)
    throws IOException
  {
    if (paramArrayOfByte != null) {}
    try
    {
      for (int i = paramArrayOfByte.length;; i = 0)
      {
        beforeWrite(i);
        this.out.write(paramArrayOfByte);
        afterWrite(i);
        return;
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        handleIOException(localIOException);
      }
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    try
    {
      beforeWrite(paramInt2);
      this.out.write(paramArrayOfByte, paramInt1, paramInt2);
      afterWrite(paramInt2);
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
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.ProxyOutputStream
 * JD-Core Version:    0.7.0.1
 */