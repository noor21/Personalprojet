package org.apache.commons.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;

@Deprecated
public class CopyUtils
{
  private static final int DEFAULT_BUFFER_SIZE = 4096;
  
  public static int copy(InputStream paramInputStream, OutputStream paramOutputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4096];
    int i = 0;
    for (;;)
    {
      int j = paramInputStream.read(arrayOfByte);
      if (-1 == j) {
        return i;
      }
      paramOutputStream.write(arrayOfByte, 0, j);
      i += j;
    }
  }
  
  public static int copy(Reader paramReader, Writer paramWriter)
    throws IOException
  {
    char[] arrayOfChar = new char[4096];
    int j = 0;
    for (;;)
    {
      int i = paramReader.read(arrayOfChar);
      if (-1 == i) {
        return j;
      }
      paramWriter.write(arrayOfChar, 0, i);
      j += i;
    }
  }
  
  public static void copy(InputStream paramInputStream, Writer paramWriter)
    throws IOException
  {
    copy(new InputStreamReader(paramInputStream), paramWriter);
  }
  
  public static void copy(InputStream paramInputStream, Writer paramWriter, String paramString)
    throws IOException
  {
    copy(new InputStreamReader(paramInputStream, paramString), paramWriter);
  }
  
  public static void copy(Reader paramReader, OutputStream paramOutputStream)
    throws IOException
  {
    OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(paramOutputStream);
    copy(paramReader, localOutputStreamWriter);
    localOutputStreamWriter.flush();
  }
  
  public static void copy(String paramString, OutputStream paramOutputStream)
    throws IOException
  {
    StringReader localStringReader = new StringReader(paramString);
    OutputStreamWriter localOutputStreamWriter = new OutputStreamWriter(paramOutputStream);
    copy(localStringReader, localOutputStreamWriter);
    localOutputStreamWriter.flush();
  }
  
  public static void copy(String paramString, Writer paramWriter)
    throws IOException
  {
    paramWriter.write(paramString);
  }
  
  public static void copy(byte[] paramArrayOfByte, OutputStream paramOutputStream)
    throws IOException
  {
    paramOutputStream.write(paramArrayOfByte);
  }
  
  public static void copy(byte[] paramArrayOfByte, Writer paramWriter)
    throws IOException
  {
    copy(new ByteArrayInputStream(paramArrayOfByte), paramWriter);
  }
  
  public static void copy(byte[] paramArrayOfByte, Writer paramWriter, String paramString)
    throws IOException
  {
    copy(new ByteArrayInputStream(paramArrayOfByte), paramWriter, paramString);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.CopyUtils
 * JD-Core Version:    0.7.0.1
 */