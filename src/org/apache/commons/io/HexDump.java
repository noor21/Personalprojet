package org.apache.commons.io;

import java.io.IOException;
import java.io.OutputStream;

public class HexDump
{
  public static final String EOL = System.getProperty("line.separator");
  private static final char[] _hexcodes;
  private static final int[] _shifts;
  
  static
  {
    Object localObject = new char[16];
    localObject[0] = 48;
    localObject[1] = 49;
    localObject[2] = 50;
    localObject[3] = 51;
    localObject[4] = 52;
    localObject[5] = 53;
    localObject[6] = 54;
    localObject[7] = 55;
    localObject[8] = 56;
    localObject[9] = 57;
    localObject[10] = 65;
    localObject[11] = 66;
    localObject[12] = 67;
    localObject[13] = 68;
    localObject[14] = 69;
    localObject[15] = 70;
    _hexcodes = (char[])localObject;
    localObject = new int[8];
    localObject[0] = 28;
    localObject[1] = 24;
    localObject[2] = 20;
    localObject[3] = 16;
    localObject[4] = 12;
    localObject[5] = 8;
    localObject[6] = 4;
    localObject[7] = 0;
    _shifts = (int[])localObject;
  }
  
  private static StringBuilder dump(StringBuilder paramStringBuilder, byte paramByte)
  {
    for (int i = 0;; i++)
    {
      if (i >= 2) {
        return paramStringBuilder;
      }
      paramStringBuilder.append(_hexcodes[(0xF & paramByte >> _shifts[(i + 6)])]);
    }
  }
  
  private static StringBuilder dump(StringBuilder paramStringBuilder, long paramLong)
  {
    for (int i = 0;; i++)
    {
      if (i >= 8) {
        return paramStringBuilder;
      }
      paramStringBuilder.append(_hexcodes[(0xF & (int)(paramLong >> _shifts[i]))]);
    }
  }
  
  public static void dump(byte[] paramArrayOfByte, long paramLong, OutputStream paramOutputStream, int paramInt)
    throws IOException, ArrayIndexOutOfBoundsException, IllegalArgumentException
  {
    if ((paramInt >= 0) && (paramInt < paramArrayOfByte.length))
    {
      if (paramOutputStream != null)
      {
        long l = paramLong + paramInt;
        StringBuilder localStringBuilder = new StringBuilder(74);
        int i = paramInt;
        if (i >= paramArrayOfByte.length) {
          return;
        }
        int j = paramArrayOfByte.length - i;
        if (j > 16) {
          j = 16;
        }
        dump(localStringBuilder, l).append(' ');
        for (int k = 0;; k++)
        {
          if (k >= 16) {
            for (k = 0;; k++)
            {
              if (k >= j)
              {
                localStringBuilder.append(EOL);
                paramOutputStream.write(localStringBuilder.toString().getBytes());
                paramOutputStream.flush();
                localStringBuilder.setLength(0);
                l += j;
                i += 16;
                break;
              }
              if ((paramArrayOfByte[(k + i)] < 32) || (paramArrayOfByte[(k + i)] >= 127)) {
                localStringBuilder.append('.');
              } else {
                localStringBuilder.append((char)paramArrayOfByte[(k + i)]);
              }
            }
          }
          if (k >= j) {
            localStringBuilder.append("  ");
          } else {
            dump(localStringBuilder, paramArrayOfByte[(k + i)]);
          }
          localStringBuilder.append(' ');
        }
      }
      throw new IllegalArgumentException("cannot write to nullstream");
    }
    throw new ArrayIndexOutOfBoundsException("illegal index: " + paramInt + " into array of length " + paramArrayOfByte.length);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.HexDump
 * JD-Core Version:    0.7.0.1
 */