package org.apache.commons.io.filefilter;

import java.io.Serializable;

public class MagicNumberFileFilter
  extends AbstractFileFilter
  implements Serializable
{
  private static final long serialVersionUID = -547733176983104172L;
  private final long byteOffset;
  private final byte[] magicNumbers;
  
  public MagicNumberFileFilter(String paramString)
  {
    this(paramString, 0L);
  }
  
  public MagicNumberFileFilter(String paramString, long paramLong)
  {
    if (paramString != null)
    {
      if (paramString.length() != 0)
      {
        if (paramLong >= 0L)
        {
          this.magicNumbers = paramString.getBytes();
          this.byteOffset = paramLong;
          return;
        }
        throw new IllegalArgumentException("The offset cannot be negative");
      }
      throw new IllegalArgumentException("The magic number must contain at least one byte");
    }
    throw new IllegalArgumentException("The magic number cannot be null");
  }
  
  public MagicNumberFileFilter(byte[] paramArrayOfByte)
  {
    this(paramArrayOfByte, 0L);
  }
  
  public MagicNumberFileFilter(byte[] paramArrayOfByte, long paramLong)
  {
    if (paramArrayOfByte != null)
    {
      if (paramArrayOfByte.length != 0)
      {
        if (paramLong >= 0L)
        {
          this.magicNumbers = new byte[paramArrayOfByte.length];
          System.arraycopy(paramArrayOfByte, 0, this.magicNumbers, 0, paramArrayOfByte.length);
          this.byteOffset = paramLong;
          return;
        }
        throw new IllegalArgumentException("The offset cannot be negative");
      }
      throw new IllegalArgumentException("The magic number must contain at least one byte");
    }
    throw new IllegalArgumentException("The magic number cannot be null");
  }
  
  /* Error */
  public boolean accept(java.io.File paramFile)
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aload_1
    //   4: ifnull +71 -> 75
    //   7: aload_1
    //   8: invokevirtual 67	java/io/File:isFile	()Z
    //   11: ifeq +64 -> 75
    //   14: aload_1
    //   15: invokevirtual 70	java/io/File:canRead	()Z
    //   18: ifeq +57 -> 75
    //   21: aconst_null
    //   22: astore_2
    //   23: aload_0
    //   24: getfield 35	org/apache/commons/io/filefilter/MagicNumberFileFilter:magicNumbers	[B
    //   27: arraylength
    //   28: newarray byte
    //   30: astore_3
    //   31: new 72	java/io/RandomAccessFile
    //   34: dup
    //   35: aload_1
    //   36: ldc 74
    //   38: invokespecial 77	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   41: astore_2
    //   42: aload_2
    //   43: aload_0
    //   44: getfield 37	org/apache/commons/io/filefilter/MagicNumberFileFilter:byteOffset	J
    //   47: invokevirtual 81	java/io/RandomAccessFile:seek	(J)V
    //   50: aload_2
    //   51: aload_3
    //   52: invokevirtual 85	java/io/RandomAccessFile:read	([B)I
    //   55: istore 6
    //   57: aload_0
    //   58: getfield 35	org/apache/commons/io/filefilter/MagicNumberFileFilter:magicNumbers	[B
    //   61: arraylength
    //   62: istore 5
    //   64: iload 6
    //   66: iload 5
    //   68: if_icmpeq +10 -> 78
    //   71: aload_2
    //   72: invokestatic 91	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   75: iload 4
    //   77: ireturn
    //   78: aload_0
    //   79: getfield 35	org/apache/commons/io/filefilter/MagicNumberFileFilter:magicNumbers	[B
    //   82: aload_3
    //   83: invokestatic 97	java/util/Arrays:equals	([B[B)Z
    //   86: istore_3
    //   87: iload_3
    //   88: istore 4
    //   90: aload_2
    //   91: invokestatic 91	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   94: goto -19 -> 75
    //   97: pop
    //   98: aload_2
    //   99: invokestatic 91	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   102: goto -27 -> 75
    //   105: astore_3
    //   106: aload_2
    //   107: invokestatic 91	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   110: aload_3
    //   111: athrow
    //   112: astore_3
    //   113: aload_2
    //   114: astore_2
    //   115: goto -9 -> 106
    //   118: pop
    //   119: aload_2
    //   120: astore_2
    //   121: goto -23 -> 98
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	124	0	this	MagicNumberFileFilter
    //   0	124	1	paramFile	java.io.File
    //   22	99	2	localRandomAccessFile	java.io.RandomAccessFile
    //   30	53	3	arrayOfByte	byte[]
    //   86	2	3	bool1	boolean
    //   105	6	3	localObject1	java.lang.Object
    //   112	1	3	localObject2	java.lang.Object
    //   1	88	4	bool2	boolean
    //   62	7	5	i	int
    //   55	14	6	j	int
    //   97	1	10	localIOException1	java.io.IOException
    //   118	1	11	localIOException2	java.io.IOException
    // Exception table:
    //   from	to	target	type
    //   23	42	97	java/io/IOException
    //   23	42	105	finally
    //   42	64	112	finally
    //   78	87	112	finally
    //   42	64	118	java/io/IOException
    //   78	87	118	java/io/IOException
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder(super.toString());
    localStringBuilder.append("(");
    localStringBuilder.append(new String(this.magicNumbers));
    localStringBuilder.append(",");
    localStringBuilder.append(this.byteOffset);
    localStringBuilder.append(")");
    return localStringBuilder.toString();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.filefilter.MagicNumberFileFilter
 * JD-Core Version:    0.7.0.1
 */