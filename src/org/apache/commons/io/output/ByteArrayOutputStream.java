package org.apache.commons.io.output;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.input.ClosedInputStream;

public class ByteArrayOutputStream
  extends OutputStream
{
  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  private final List<byte[]> buffers = new ArrayList();
  private int count;
  private byte[] currentBuffer;
  private int currentBufferIndex;
  private int filledBufferSum;
  
  public ByteArrayOutputStream()
  {
    this(1024);
  }
  
  public ByteArrayOutputStream(int paramInt)
  {
    if (paramInt < 0) {
      throw new IllegalArgumentException("Negative initial size: " + paramInt);
    }
    try
    {
      needNewBuffer(paramInt);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  private void needNewBuffer(int paramInt)
  {
    if (this.currentBufferIndex >= -1 + this.buffers.size())
    {
      int i;
      if (this.currentBuffer != null)
      {
        i = Math.max(this.currentBuffer.length << 1, paramInt - this.filledBufferSum);
        this.filledBufferSum += this.currentBuffer.length;
      }
      else
      {
        i = paramInt;
        this.filledBufferSum = 0;
      }
      this.currentBufferIndex = (1 + this.currentBufferIndex);
      this.currentBuffer = new byte[i];
      this.buffers.add(this.currentBuffer);
    }
    else
    {
      this.filledBufferSum += this.currentBuffer.length;
      this.currentBufferIndex = (1 + this.currentBufferIndex);
      this.currentBuffer = ((byte[])this.buffers.get(this.currentBufferIndex));
    }
  }
  
  private InputStream toBufferedInputStream()
  {
    int j = this.count;
    Object localObject;
    if (j != 0)
    {
      ArrayList localArrayList = new ArrayList(this.buffers.size());
      Iterator localIterator = this.buffers.iterator();
      do
      {
        if (!localIterator.hasNext()) {
          break;
        }
        localObject = (byte[])localIterator.next();
        int i = Math.min(localObject.length, j);
        localArrayList.add(new ByteArrayInputStream((byte[])localObject, 0, i));
        j -= i;
      } while (j != 0);
      localObject = new SequenceInputStream(Collections.enumeration(localArrayList));
    }
    else
    {
      localObject = new ClosedInputStream();
    }
    return localObject;
  }
  
  public static InputStream toBufferedInputStream(InputStream paramInputStream)
    throws IOException
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    localByteArrayOutputStream.write(paramInputStream);
    return localByteArrayOutputStream.toBufferedInputStream();
  }
  
  public void close()
    throws IOException
  {}
  
  /**
   * @deprecated
   */
  public void reset()
  {
    try
    {
      this.count = 0;
      this.filledBufferSum = 0;
      this.currentBufferIndex = 0;
      this.currentBuffer = ((byte[])this.buffers.get(this.currentBufferIndex));
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
  public int size()
  {
    try
    {
      int i = this.count;
      return i;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  /* Error */
  /**
   * @deprecated
   */
  public byte[] toByteArray()
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: getfield 84	org/apache/commons/io/output/ByteArrayOutputStream:count	I
    //   6: istore_1
    //   7: iload_1
    //   8: ifne +11 -> 19
    //   11: getstatic 18	org/apache/commons/io/output/ByteArrayOutputStream:EMPTY_BYTE_ARRAY	[B
    //   14: astore_3
    //   15: aload_0
    //   16: monitorexit
    //   17: aload_3
    //   18: areturn
    //   19: iload_1
    //   20: newarray byte
    //   22: astore_3
    //   23: iconst_0
    //   24: istore 4
    //   26: aload_0
    //   27: getfield 29	org/apache/commons/io/output/ByteArrayOutputStream:buffers	Ljava/util/List;
    //   30: invokeinterface 89 1 0
    //   35: astore 5
    //   37: aload 5
    //   39: invokeinterface 95 1 0
    //   44: ifeq -29 -> 15
    //   47: aload 5
    //   49: invokeinterface 99 1 0
    //   54: checkcast 80	[B
    //   57: astore 6
    //   59: aload 6
    //   61: arraylength
    //   62: iload_1
    //   63: invokestatic 102	java/lang/Math:min	(II)I
    //   66: istore_2
    //   67: aload 6
    //   69: iconst_0
    //   70: aload_3
    //   71: iload 4
    //   73: iload_2
    //   74: invokestatic 141	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
    //   77: iload 4
    //   79: iload_2
    //   80: iadd
    //   81: istore 4
    //   83: iload_1
    //   84: iload_2
    //   85: isub
    //   86: istore_1
    //   87: iload_1
    //   88: ifne -51 -> 37
    //   91: goto -76 -> 15
    //   94: astore_1
    //   95: aload_0
    //   96: monitorexit
    //   97: aload_1
    //   98: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	99	0	this	ByteArrayOutputStream
    //   6	82	1	i	int
    //   94	4	1	localObject	Object
    //   66	20	2	j	int
    //   14	57	3	arrayOfByte1	byte[]
    //   24	58	4	k	int
    //   35	13	5	localIterator	Iterator
    //   57	11	6	arrayOfByte2	byte[]
    // Exception table:
    //   from	to	target	type
    //   2	15	94	finally
    //   19	77	94	finally
  }
  
  public String toString()
  {
    return new String(toByteArray());
  }
  
  public String toString(String paramString)
    throws UnsupportedEncodingException
  {
    return new String(toByteArray(), paramString);
  }
  
  /**
   * @deprecated
   */
  public int write(InputStream paramInputStream)
    throws IOException
  {
    int j = 0;
    try
    {
      int i = this.count - this.filledBufferSum;
      for (int k = paramInputStream.read(this.currentBuffer, i, this.currentBuffer.length - i); k != -1; k = k)
      {
        j += k;
        i += k;
        this.count = (k + this.count);
        if (i == this.currentBuffer.length)
        {
          needNewBuffer(this.currentBuffer.length);
          i = 0;
        }
        k = paramInputStream.read(this.currentBuffer, i, this.currentBuffer.length - i);
      }
      return j;
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
  public void write(int paramInt)
  {
    try
    {
      int i = this.count - this.filledBufferSum;
      if (i == this.currentBuffer.length)
      {
        needNewBuffer(1 + this.count);
        i = 0;
      }
      this.currentBuffer[i] = ((byte)paramInt);
      this.count = (1 + this.count);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
  
  public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    if ((paramInt1 < 0) || (paramInt1 > paramArrayOfByte.length) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length) || (paramInt1 + paramInt2 < 0)) {
      throw new IndexOutOfBoundsException();
    }
    if (paramInt2 == 0) {}
    for (;;)
    {
      return;
      try
      {
        int i = paramInt2 + this.count;
        int m = paramInt2;
        int j = this.count - this.filledBufferSum;
        while (m > 0)
        {
          int k = Math.min(m, this.currentBuffer.length - j);
          System.arraycopy(paramArrayOfByte, paramInt1 + paramInt2 - m, this.currentBuffer, j, k);
          m -= k;
          if (m > 0)
          {
            needNewBuffer(i);
            j = 0;
          }
        }
        this.count = i;
      }
      finally
      {
        localObject = finally;
        throw localObject;
      }
    }
  }
  
  /**
   * @deprecated
   */
  public void writeTo(OutputStream paramOutputStream)
    throws IOException
  {
    try
    {
      int i = this.count;
      Iterator localIterator = this.buffers.iterator();
      do
      {
        if (!localIterator.hasNext()) {
          break;
        }
        byte[] arrayOfByte = (byte[])localIterator.next();
        int j = Math.min(arrayOfByte.length, i);
        paramOutputStream.write(arrayOfByte, 0, j);
        i -= j;
      } while (i != 0);
      return;
    }
    finally
    {
      localObject = finally;
      throw localObject;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.ByteArrayOutputStream
 * JD-Core Version:    0.7.0.1
 */