package org.apache.commons.io;

import java.io.Serializable;

public class ByteOrderMark
  implements Serializable
{
  public static final ByteOrderMark UTF_16BE;
  public static final ByteOrderMark UTF_16LE;
  public static final ByteOrderMark UTF_32BE;
  public static final ByteOrderMark UTF_32LE;
  public static final ByteOrderMark UTF_8;
  private static final long serialVersionUID = 1L;
  private final int[] bytes;
  private final String charsetName;
  
  static
  {
    int[] arrayOfInt = new int[3];
    arrayOfInt[0] = 239;
    arrayOfInt[1] = 187;
    arrayOfInt[2] = 191;
    UTF_8 = new ByteOrderMark("UTF-8", arrayOfInt);
    arrayOfInt = new int[2];
    arrayOfInt[0] = 254;
    arrayOfInt[1] = 255;
    UTF_16BE = new ByteOrderMark("UTF-16BE", arrayOfInt);
    arrayOfInt = new int[2];
    arrayOfInt[0] = 255;
    arrayOfInt[1] = 254;
    UTF_16LE = new ByteOrderMark("UTF-16LE", arrayOfInt);
    arrayOfInt = new int[4];
    arrayOfInt[0] = 0;
    arrayOfInt[1] = 0;
    arrayOfInt[2] = 254;
    arrayOfInt[3] = 255;
    UTF_32BE = new ByteOrderMark("UTF-32BE", arrayOfInt);
    arrayOfInt = new int[4];
    arrayOfInt[0] = 255;
    arrayOfInt[1] = 254;
    arrayOfInt[2] = 0;
    arrayOfInt[3] = 0;
    UTF_32LE = new ByteOrderMark("UTF-32LE", arrayOfInt);
  }
  
  public ByteOrderMark(String paramString, int... paramVarArgs)
  {
    if ((paramString != null) && (paramString.length() != 0))
    {
      if ((paramVarArgs != null) && (paramVarArgs.length != 0))
      {
        this.charsetName = paramString;
        this.bytes = new int[paramVarArgs.length];
        System.arraycopy(paramVarArgs, 0, this.bytes, 0, paramVarArgs.length);
        return;
      }
      throw new IllegalArgumentException("No bytes specified");
    }
    throw new IllegalArgumentException("No charsetName specified");
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    ByteOrderMark localByteOrderMark;
    if ((paramObject instanceof ByteOrderMark))
    {
      localByteOrderMark = (ByteOrderMark)paramObject;
      if (this.bytes.length != localByteOrderMark.length()) {}
    }
    for (int i = 0;; i++)
    {
      if (i >= this.bytes.length) {
        bool = true;
      } else {
        if (this.bytes[i] == localByteOrderMark.get(i)) {
          continue;
        }
      }
      return bool;
    }
  }
  
  public int get(int paramInt)
  {
    return this.bytes[paramInt];
  }
  
  public byte[] getBytes()
  {
    byte[] arrayOfByte = new byte[this.bytes.length];
    for (int i = 0;; i++)
    {
      if (i >= this.bytes.length) {
        return arrayOfByte;
      }
      arrayOfByte[i] = ((byte)this.bytes[i]);
    }
  }
  
  public String getCharsetName()
  {
    return this.charsetName;
  }
  
  public int hashCode()
  {
    int j = getClass().hashCode();
    int[] arrayOfInt = this.bytes;
    int k = arrayOfInt.length;
    for (int i = 0;; i++)
    {
      if (i >= k) {
        return j;
      }
      j += arrayOfInt[i];
    }
  }
  
  public int length()
  {
    return this.bytes.length;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(getClass().getSimpleName());
    localStringBuilder.append('[');
    localStringBuilder.append(this.charsetName);
    localStringBuilder.append(": ");
    for (int i = 0;; i++)
    {
      if (i >= this.bytes.length)
      {
        localStringBuilder.append(']');
        return localStringBuilder.toString();
      }
      if (i > 0) {
        localStringBuilder.append(",");
      }
      localStringBuilder.append("0x");
      localStringBuilder.append(Integer.toHexString(0xFF & this.bytes[i]).toUpperCase());
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.ByteOrderMark
 * JD-Core Version:    0.7.0.1
 */