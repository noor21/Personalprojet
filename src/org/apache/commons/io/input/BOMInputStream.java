package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.io.ByteOrderMark;

public class BOMInputStream
  extends ProxyInputStream
{
  private static final Comparator<ByteOrderMark> ByteOrderMarkLengthComparator = new Comparator()
  {
    public int compare(ByteOrderMark paramAnonymousByteOrderMark1, ByteOrderMark paramAnonymousByteOrderMark2)
    {
      int i = paramAnonymousByteOrderMark1.length();
      int j = paramAnonymousByteOrderMark2.length();
      if (i <= j)
      {
        if (j <= i) {
          i = 0;
        } else {
          i = 1;
        }
      }
      else {
        i = -1;
      }
      return i;
    }
  };
  private final List<ByteOrderMark> boms;
  private ByteOrderMark byteOrderMark;
  private int fbIndex;
  private int fbLength;
  private int[] firstBytes;
  private final boolean include;
  private int markFbIndex;
  private boolean markedAtStart;
  
  public BOMInputStream(InputStream paramInputStream)
  {
    this(paramInputStream, false, arrayOfByteOrderMark);
  }
  
  public BOMInputStream(InputStream paramInputStream, boolean paramBoolean)
  {
    this(paramInputStream, paramBoolean, arrayOfByteOrderMark);
  }
  
  public BOMInputStream(InputStream paramInputStream, boolean paramBoolean, ByteOrderMark... paramVarArgs)
  {
    super(paramInputStream);
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      this.include = paramBoolean;
      Arrays.sort(paramVarArgs, ByteOrderMarkLengthComparator);
      this.boms = Arrays.asList(paramVarArgs);
      return;
    }
    throw new IllegalArgumentException("No BOMs specified");
  }
  
  public BOMInputStream(InputStream paramInputStream, ByteOrderMark... paramVarArgs)
  {
    this(paramInputStream, false, paramVarArgs);
  }
  
  private ByteOrderMark find()
  {
    Iterator localIterator = this.boms.iterator();
    ByteOrderMark localByteOrderMark;
    do
    {
      if (!localIterator.hasNext())
      {
        localByteOrderMark = null;
        break;
      }
      localByteOrderMark = (ByteOrderMark)localIterator.next();
    } while (!matches(localByteOrderMark));
    return localByteOrderMark;
  }
  
  private boolean matches(ByteOrderMark paramByteOrderMark)
  {
    for (int i = 0;; i++)
    {
      if (i >= paramByteOrderMark.length()) {
        return 1;
      }
      if (paramByteOrderMark.get(i) != this.firstBytes[i]) {
        break;
      }
    }
    i = 0;
    return i;
  }
  
  private int readFirstBytes()
    throws IOException
  {
    getBOM();
    int j;
    if (this.fbIndex >= this.fbLength)
    {
      int i = -1;
    }
    else
    {
      int[] arrayOfInt = this.firstBytes;
      int k = this.fbIndex;
      this.fbIndex = (k + 1);
      j = arrayOfInt[k];
    }
    return j;
  }
  
  public ByteOrderMark getBOM()
    throws IOException
  {
    if (this.firstBytes == null)
    {
      this.fbLength = 0;
      this.firstBytes = new int[((ByteOrderMark)this.boms.get(0)).length()];
      for (int i = 0; i < this.firstBytes.length; i++)
      {
        this.firstBytes[i] = this.in.read();
        this.fbLength = (1 + this.fbLength);
        if (this.firstBytes[i] < 0) {
          break;
        }
      }
      this.byteOrderMark = find();
      if ((this.byteOrderMark != null) && (!this.include)) {
        if (this.byteOrderMark.length() >= this.firstBytes.length) {
          this.fbLength = 0;
        } else {
          this.fbIndex = this.byteOrderMark.length();
        }
      }
    }
    return this.byteOrderMark;
  }
  
  public String getBOMCharsetName()
    throws IOException
  {
    getBOM();
    String str;
    if (this.byteOrderMark != null) {
      str = this.byteOrderMark.getCharsetName();
    } else {
      str = null;
    }
    return str;
  }
  
  public boolean hasBOM()
    throws IOException
  {
    boolean bool;
    if (getBOM() == null) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean hasBOM(ByteOrderMark paramByteOrderMark)
    throws IOException
  {
    if (this.boms.contains(paramByteOrderMark))
    {
      boolean bool;
      if ((this.byteOrderMark == null) || (!getBOM().equals(paramByteOrderMark))) {
        bool = false;
      } else {
        bool = true;
      }
      return bool;
    }
    throw new IllegalArgumentException("Stream not configure to detect " + paramByteOrderMark);
  }
  
  /* Error */
  /**
   * @deprecated
   */
  public void mark(int paramInt)
  {
    // Byte code:
    //   0: aload_0
    //   1: monitorenter
    //   2: aload_0
    //   3: aload_0
    //   4: getfield 104	org/apache/commons/io/input/BOMInputStream:fbIndex	I
    //   7: putfield 154	org/apache/commons/io/input/BOMInputStream:markFbIndex	I
    //   10: aload_0
    //   11: getfield 96	org/apache/commons/io/input/BOMInputStream:firstBytes	[I
    //   14: ifnonnull +21 -> 35
    //   17: iconst_1
    //   18: istore_2
    //   19: aload_0
    //   20: iload_2
    //   21: putfield 156	org/apache/commons/io/input/BOMInputStream:markedAtStart	Z
    //   24: aload_0
    //   25: getfield 113	org/apache/commons/io/input/BOMInputStream:in	Ljava/io/InputStream;
    //   28: iload_1
    //   29: invokevirtual 158	java/io/InputStream:mark	(I)V
    //   32: aload_0
    //   33: monitorexit
    //   34: return
    //   35: iconst_0
    //   36: istore_2
    //   37: goto -18 -> 19
    //   40: astore_2
    //   41: aload_0
    //   42: monitorexit
    //   43: aload_2
    //   44: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	45	0	this	BOMInputStream
    //   0	45	1	paramInt	int
    //   18	19	2	bool	boolean
    //   40	4	2	localObject	Object
    // Exception table:
    //   from	to	target	type
    //   2	32	40	finally
  }
  
  public int read()
    throws IOException
  {
    int i = readFirstBytes();
    if (i < 0) {
      i = this.in.read();
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
    int i = 0;
    int m = 0;
    int j = paramInt1;
    for (;;)
    {
      if ((paramInt2 <= 0) || (m < 0))
      {
        j = this.in.read(paramArrayOfByte, j, paramInt2);
        if (j >= 0) {
          i += j;
        } else if (i <= 0) {
          i = -1;
        }
        return i;
      }
      m = readFirstBytes();
      if (m >= 0)
      {
        int k = j + 1;
        paramArrayOfByte[j] = ((byte)(m & 0xFF));
        paramInt2--;
        i++;
        j = k;
      }
    }
  }
  
  /**
   * @deprecated
   */
  public void reset()
    throws IOException
  {
    try
    {
      this.fbIndex = this.markFbIndex;
      if (this.markedAtStart) {
        this.firstBytes = null;
      }
      this.in.reset();
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
    for (;;)
    {
      if ((paramLong <= 0L) || (readFirstBytes() < 0)) {
        return this.in.skip(paramLong);
      }
      paramLong -= 1L;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.BOMInputStream
 * JD-Core Version:    0.7.0.1
 */