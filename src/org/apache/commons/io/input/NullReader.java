package org.apache.commons.io.input;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class NullReader
  extends Reader
{
  private boolean eof;
  private long mark = -1L;
  private final boolean markSupported;
  private long position;
  private long readlimit;
  private final long size;
  private final boolean throwEofException;
  
  public NullReader(long paramLong)
  {
    this(paramLong, true, false);
  }
  
  public NullReader(long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.size = paramLong;
    this.markSupported = paramBoolean1;
    this.throwEofException = paramBoolean2;
  }
  
  private int doEndOfFile()
    throws EOFException
  {
    this.eof = true;
    if (!this.throwEofException) {
      return -1;
    }
    throw new EOFException();
  }
  
  public void close()
    throws IOException
  {
    this.eof = false;
    this.position = 0L;
    this.mark = -1L;
  }
  
  public long getPosition()
  {
    return this.position;
  }
  
  public long getSize()
  {
    return this.size;
  }
  
  /**
   * @deprecated
   */
  public void mark(int paramInt)
  {
    try
    {
      if (!this.markSupported) {
        throw new UnsupportedOperationException("Mark not supported");
      }
    }
    finally {}
    this.mark = this.position;
    this.readlimit = paramInt;
  }
  
  public boolean markSupported()
  {
    return this.markSupported;
  }
  
  protected int processChar()
  {
    return 0;
  }
  
  protected void processChars(char[] paramArrayOfChar, int paramInt1, int paramInt2) {}
  
  public int read()
    throws IOException
  {
    if (!this.eof)
    {
      int i;
      if (this.position != this.size)
      {
        this.position = (1L + this.position);
        i = processChar();
      }
      else
      {
        i = doEndOfFile();
      }
      return i;
    }
    throw new IOException("Read after end of file");
  }
  
  public int read(char[] paramArrayOfChar)
    throws IOException
  {
    return read(paramArrayOfChar, 0, paramArrayOfChar.length);
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    if (!this.eof)
    {
      int i;
      if (this.position != this.size)
      {
        this.position += paramInt2;
        i = paramInt2;
        if (this.position > this.size)
        {
          i = paramInt2 - (int)(this.position - this.size);
          this.position = this.size;
        }
        processChars(paramArrayOfChar, paramInt1, i);
      }
      else
      {
        i = doEndOfFile();
      }
      return i;
    }
    throw new IOException("Read after end of file");
  }
  
  /**
   * @deprecated
   */
  public void reset()
    throws IOException
  {
    try
    {
      if (!this.markSupported) {
        throw new UnsupportedOperationException("Mark not supported");
      }
    }
    finally {}
    if (this.mark < 0L) {
      throw new IOException("No position has been marked");
    }
    if (this.position > this.mark + this.readlimit) {
      throw new IOException("Marked position [" + this.mark + "] is no longer valid - passed the read limit [" + this.readlimit + "]");
    }
    this.position = this.mark;
    this.eof = false;
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    if (!this.eof)
    {
      long l;
      if (this.position != this.size)
      {
        this.position = (paramLong + this.position);
        l = paramLong;
        if (this.position > this.size)
        {
          l = paramLong - (this.position - this.size);
          this.position = this.size;
        }
      }
      else
      {
        l = doEndOfFile();
      }
      return l;
    }
    throw new IOException("Skip after end of file");
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.NullReader
 * JD-Core Version:    0.7.0.1
 */