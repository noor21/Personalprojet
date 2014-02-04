package org.apache.commons.io.input;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import org.apache.commons.io.Charsets;

public class ReversedLinesFileReader
  implements Closeable
{
  private final int avoidNewlineSplitBufferSize;
  private final int blockSize;
  private final int byteDecrement;
  private FilePart currentFilePart;
  private final Charset encoding;
  private final byte[][] newLineSequences;
  private final RandomAccessFile randomAccessFile;
  private final long totalBlockCount;
  private final long totalByteLength;
  private boolean trailingNewlineOfFileSkipped = false;
  
  public ReversedLinesFileReader(File paramFile)
    throws IOException
  {
    this(paramFile, 4096, Charset.defaultCharset().toString());
  }
  
  public ReversedLinesFileReader(File paramFile, int paramInt, String paramString)
    throws IOException
  {
    this(paramFile, paramInt, Charsets.toCharset(paramString));
  }
  
  public ReversedLinesFileReader(File paramFile, int paramInt, Charset paramCharset)
    throws IOException
  {
    this.blockSize = paramInt;
    this.encoding = paramCharset;
    this.randomAccessFile = new RandomAccessFile(paramFile, "r");
    this.totalByteLength = this.randomAccessFile.length();
    int i = (int)(this.totalByteLength % paramInt);
    if (i <= 0)
    {
      this.totalBlockCount = (this.totalByteLength / paramInt);
      if (this.totalByteLength > 0L) {
        i = paramInt;
      }
    }
    else
    {
      this.totalBlockCount = (1L + this.totalByteLength / paramInt);
    }
    this.currentFilePart = new FilePart(this.totalBlockCount, i, null, null);
    Object localObject = Charsets.toCharset(paramCharset);
    if (((Charset)localObject).newEncoder().maxBytesPerChar() != 1.0F)
    {
      if (localObject != Charset.forName("UTF-8"))
      {
        if (localObject != Charset.forName("Shift_JIS"))
        {
          if ((localObject != Charset.forName("UTF-16BE")) && (localObject != Charset.forName("UTF-16LE")))
          {
            if (localObject != Charset.forName("UTF-16")) {
              throw new UnsupportedEncodingException("Encoding " + paramCharset + " is not supported yet (feel free to submit a patch)");
            }
            throw new UnsupportedEncodingException("For UTF-16, you need to specify the byte order (use UTF-16BE or UTF-16LE)");
          }
          this.byteDecrement = 2;
        }
        else
        {
          this.byteDecrement = 1;
        }
      }
      else {
        this.byteDecrement = 1;
      }
    }
    else {
      this.byteDecrement = 1;
    }
    localObject = new byte[3][];
    localObject[0] = "\r\n".getBytes(paramCharset);
    localObject[1] = "\n".getBytes(paramCharset);
    localObject[2] = "\r".getBytes(paramCharset);
    this.newLineSequences = ((byte[][])localObject);
    this.avoidNewlineSplitBufferSize = this.newLineSequences[0].length;
  }
  
  public void close()
    throws IOException
  {
    this.randomAccessFile.close();
  }
  
  public String readLine()
    throws IOException
  {
    for (String str = this.currentFilePart.readLine();; str = this.currentFilePart.readLine()) {
      if (str == null)
      {
        this.currentFilePart = this.currentFilePart.rollOver();
        if (this.currentFilePart != null) {}
      }
      else
      {
        if (("".equals(str)) && (!this.trailingNewlineOfFileSkipped))
        {
          this.trailingNewlineOfFileSkipped = true;
          str = readLine();
        }
        return str;
      }
    }
  }
  
  private class FilePart
  {
    private int currentLastBytePos;
    private final byte[] data;
    private byte[] leftOver;
    private final long no;
    
    private FilePart(long paramLong, int paramInt, byte[] paramArrayOfByte)
      throws IOException
    {
      this.no = paramLong;
      int i;
      if (paramArrayOfByte == null) {
        i = 0;
      } else {
        i = paramArrayOfByte.length;
      }
      this.data = new byte[paramInt + i];
      long l = (paramLong - 1L) * ReversedLinesFileReader.this.blockSize;
      if (paramLong > 0L)
      {
        ReversedLinesFileReader.this.randomAccessFile.seek(l);
        if (ReversedLinesFileReader.this.randomAccessFile.read(this.data, 0, paramInt) != paramInt) {}
      }
      else
      {
        if (paramArrayOfByte != null) {
          System.arraycopy(paramArrayOfByte, 0, this.data, paramInt, paramArrayOfByte.length);
        }
        this.currentLastBytePos = (-1 + this.data.length);
        this.leftOver = null;
        return;
      }
      throw new IllegalStateException("Count of requested bytes and actually read bytes don't match");
    }
    
    private void createLeftOver()
    {
      int i = 1 + this.currentLastBytePos;
      if (i <= 0)
      {
        this.leftOver = null;
      }
      else
      {
        this.leftOver = new byte[i];
        System.arraycopy(this.data, 0, this.leftOver, 0, i);
      }
      this.currentLastBytePos = -1;
    }
    
    private int getNewLineMatchByteCount(byte[] paramArrayOfByte, int paramInt)
    {
      int j = 0;
      byte[][] arrayOfByte = ReversedLinesFileReader.this.newLineSequences;
      int k = arrayOfByte.length;
      int i = 0;
      byte[] arrayOfByte1;
      int m;
      if (i < k)
      {
        arrayOfByte1 = arrayOfByte[i];
        m = 1;
      }
      for (int n = -1 + arrayOfByte1.length;; n--)
      {
        if (n < 0)
        {
          if (m == 0)
          {
            i++;
            break;
          }
          j = arrayOfByte1.length;
          return j;
        }
        int i1 = paramInt + n - (-1 + arrayOfByte1.length);
        if ((i1 < 0) || (paramArrayOfByte[i1] != arrayOfByte1[n])) {
          i1 = 0;
        } else {
          i1 = 1;
        }
        m &= i1;
      }
    }
    
    private String readLine()
      throws IOException
    {
      Object localObject = null;
      int k;
      if (this.no != 1L) {
        k = 0;
      } else {
        k = 1;
      }
      int j = this.currentLastBytePos;
      String str;
      while (j > -1) {
        if ((k != 0) || (j >= ReversedLinesFileReader.this.avoidNewlineSplitBufferSize))
        {
          int i = getNewLineMatchByteCount(this.data, j);
          if (i <= 0)
          {
            j -= ReversedLinesFileReader.this.byteDecrement;
            if (j < 0) {
              createLeftOver();
            }
          }
          else
          {
            int n = j + 1;
            int m = 1 + (this.currentLastBytePos - n);
            if (m >= 0)
            {
              byte[] arrayOfByte = new byte[m];
              System.arraycopy(this.data, n, arrayOfByte, 0, m);
              str = new String(arrayOfByte, ReversedLinesFileReader.this.encoding);
              this.currentLastBytePos = (j - i);
            }
            else
            {
              throw new IllegalStateException("Unexpected negative line length=" + str);
            }
          }
        }
        else
        {
          createLeftOver();
        }
      }
      if ((k != 0) && (this.leftOver != null))
      {
        str = new String(this.leftOver, ReversedLinesFileReader.this.encoding);
        this.leftOver = null;
      }
      return str;
    }
    
    private FilePart rollOver()
      throws IOException
    {
      if (this.currentLastBytePos <= -1)
      {
        FilePart localFilePart;
        if (this.no <= 1L)
        {
          if (this.leftOver == null) {
            localFilePart = null;
          } else {
            throw new IllegalStateException("Unexpected leftover of the last block: leftOverOfThisFilePart=" + new String(this.leftOver, ReversedLinesFileReader.this.encoding));
          }
        }
        else {
          localFilePart = new FilePart(ReversedLinesFileReader.this, this.no - 1L, ReversedLinesFileReader.this.blockSize, this.leftOver);
        }
        return localFilePart;
      }
      throw new IllegalStateException("Current currentLastCharPos unexpectedly positive... last readLine() should have returned something! currentLastCharPos=" + this.currentLastBytePos);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.ReversedLinesFileReader
 * JD-Core Version:    0.7.0.1
 */