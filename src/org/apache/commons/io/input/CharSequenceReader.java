package org.apache.commons.io.input;

import java.io.Reader;
import java.io.Serializable;

public class CharSequenceReader
  extends Reader
  implements Serializable
{
  private final CharSequence charSequence;
  private int idx;
  private int mark;
  
  public CharSequenceReader(CharSequence paramCharSequence)
  {
    if (paramCharSequence == null) {
      paramCharSequence = "";
    }
    this.charSequence = paramCharSequence;
  }
  
  public void close()
  {
    this.idx = 0;
    this.mark = 0;
  }
  
  public void mark(int paramInt)
  {
    this.mark = this.idx;
  }
  
  public boolean markSupported()
  {
    return true;
  }
  
  public int read()
  {
    int i;
    if (this.idx < this.charSequence.length())
    {
      CharSequence localCharSequence = this.charSequence;
      i = this.idx;
      this.idx = (i + 1);
      i = localCharSequence.charAt(i);
    }
    else
    {
      i = -1;
    }
    return i;
  }
  
  public int read(char[] paramArrayOfChar, int paramInt1, int paramInt2)
  {
    if (this.idx < this.charSequence.length())
    {
      if (paramArrayOfChar != null)
      {
        if ((paramInt2 >= 0) && (paramInt1 >= 0) && (paramInt1 + paramInt2 <= paramArrayOfChar.length))
        {
          k = 0;
          for (int j = 0; j < paramInt2; j++)
          {
            int i = read();
            if (i == -1) {
              break;
            }
            paramArrayOfChar[(paramInt1 + j)] = ((char)i);
            k++;
          }
        }
        throw new IndexOutOfBoundsException("Array Size=" + paramArrayOfChar.length + ", offset=" + paramInt1 + ", length=" + paramInt2);
      }
      throw new NullPointerException("Character array is missing");
    }
    int k = -1;
    return k;
  }
  
  public void reset()
  {
    this.idx = this.mark;
  }
  
  public long skip(long paramLong)
  {
    if (paramLong >= 0L)
    {
      long l;
      if (this.idx < this.charSequence.length())
      {
        int i = (int)Math.min(this.charSequence.length(), paramLong + this.idx);
        int j = i - this.idx;
        this.idx = i;
        l = j;
      }
      else
      {
        l = -1L;
      }
      return l;
    }
    throw new IllegalArgumentException("Number of characters to skip is less than zero: " + paramLong);
  }
  
  public String toString()
  {
    return this.charSequence.toString();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.CharSequenceReader
 * JD-Core Version:    0.7.0.1
 */