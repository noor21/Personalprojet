package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class CharSequenceInputStream
  extends InputStream
{
  private final ByteBuffer bbuf;
  private final CharBuffer cbuf;
  private final CharsetEncoder encoder;
  private int mark;
  
  public CharSequenceInputStream(CharSequence paramCharSequence, String paramString)
  {
    this(paramCharSequence, paramString, 2048);
  }
  
  public CharSequenceInputStream(CharSequence paramCharSequence, String paramString, int paramInt)
  {
    this(paramCharSequence, Charset.forName(paramString), paramInt);
  }
  
  public CharSequenceInputStream(CharSequence paramCharSequence, Charset paramCharset)
  {
    this(paramCharSequence, paramCharset, 2048);
  }
  
  public CharSequenceInputStream(CharSequence paramCharSequence, Charset paramCharset, int paramInt)
  {
    this.encoder = paramCharset.newEncoder().onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
    this.bbuf = ByteBuffer.allocate(paramInt);
    this.bbuf.flip();
    this.cbuf = CharBuffer.wrap(paramCharSequence);
    this.mark = -1;
  }
  
  private void fillBuffer()
    throws CharacterCodingException
  {
    this.bbuf.compact();
    CoderResult localCoderResult = this.encoder.encode(this.cbuf, this.bbuf, true);
    if (localCoderResult.isError()) {
      localCoderResult.throwException();
    }
    this.bbuf.flip();
  }
  
  public int available()
    throws IOException
  {
    return this.cbuf.remaining();
  }
  
  public void close()
    throws IOException
  {}
  
  /**
   * @deprecated
   */
  public void mark(int paramInt)
  {
    try
    {
      this.mark = this.cbuf.position();
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
    return true;
  }
  
  public int read()
    throws IOException
  {
    while (!this.bbuf.hasRemaining())
    {
      fillBuffer();
      if ((!this.bbuf.hasRemaining()) && (!this.cbuf.hasRemaining())) {
        return -1;
      }
    }
    int i = 0xFF & this.bbuf.get();
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
    int i = -1;
    if (paramArrayOfByte != null)
    {
      if ((paramInt2 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfByte.length))
      {
        if (paramInt2 != 0)
        {
          if ((this.bbuf.hasRemaining()) || (this.cbuf.hasRemaining()))
          {
            int k = 0;
            for (;;)
            {
              if (paramInt2 > 0)
              {
                if (!this.bbuf.hasRemaining())
                {
                  fillBuffer();
                  if ((this.bbuf.hasRemaining()) || (this.cbuf.hasRemaining())) {
                    continue;
                  }
                }
              }
              else
              {
                if ((k == 0) && (!this.cbuf.hasRemaining())) {
                  k = i;
                }
                i = k;
                break;
              }
              int j = Math.min(this.bbuf.remaining(), paramInt2);
              this.bbuf.get(paramArrayOfByte, paramInt1, j);
              paramInt1 += j;
              paramInt2 -= j;
              k += j;
            }
          }
        }
        else {
          i = 0;
        }
        return i;
      }
      throw new IndexOutOfBoundsException("Array Size=" + paramArrayOfByte.length + ", offset=" + paramInt1 + ", length=" + paramInt2);
    }
    throw new NullPointerException("Byte array is null");
  }
  
  /**
   * @deprecated
   */
  public void reset()
    throws IOException
  {
    try
    {
      if (this.mark != -1)
      {
        this.cbuf.position(this.mark);
        this.mark = -1;
      }
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public long skip(long paramLong)
    throws IOException
  {
    for (int i = 0;; i++)
    {
      if ((paramLong <= 0L) || (!this.cbuf.hasRemaining())) {
        return i;
      }
      this.cbuf.get();
      paramLong -= 1L;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.CharSequenceInputStream
 * JD-Core Version:    0.7.0.1
 */