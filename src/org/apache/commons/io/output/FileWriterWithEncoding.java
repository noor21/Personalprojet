package org.apache.commons.io.output;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

public class FileWriterWithEncoding
  extends Writer
{
  private final Writer out;
  
  public FileWriterWithEncoding(File paramFile, String paramString)
    throws IOException
  {
    this(paramFile, paramString, false);
  }
  
  public FileWriterWithEncoding(File paramFile, String paramString, boolean paramBoolean)
    throws IOException
  {
    this.out = initWriter(paramFile, paramString, paramBoolean);
  }
  
  public FileWriterWithEncoding(File paramFile, Charset paramCharset)
    throws IOException
  {
    this(paramFile, paramCharset, false);
  }
  
  public FileWriterWithEncoding(File paramFile, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    this.out = initWriter(paramFile, paramCharset, paramBoolean);
  }
  
  public FileWriterWithEncoding(File paramFile, CharsetEncoder paramCharsetEncoder)
    throws IOException
  {
    this(paramFile, paramCharsetEncoder, false);
  }
  
  public FileWriterWithEncoding(File paramFile, CharsetEncoder paramCharsetEncoder, boolean paramBoolean)
    throws IOException
  {
    this.out = initWriter(paramFile, paramCharsetEncoder, paramBoolean);
  }
  
  public FileWriterWithEncoding(String paramString1, String paramString2)
    throws IOException
  {
    this(new File(paramString1), paramString2, false);
  }
  
  public FileWriterWithEncoding(String paramString1, String paramString2, boolean paramBoolean)
    throws IOException
  {
    this(new File(paramString1), paramString2, paramBoolean);
  }
  
  public FileWriterWithEncoding(String paramString, Charset paramCharset)
    throws IOException
  {
    this(new File(paramString), paramCharset, false);
  }
  
  public FileWriterWithEncoding(String paramString, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    this(new File(paramString), paramCharset, paramBoolean);
  }
  
  public FileWriterWithEncoding(String paramString, CharsetEncoder paramCharsetEncoder)
    throws IOException
  {
    this(new File(paramString), paramCharsetEncoder, false);
  }
  
  public FileWriterWithEncoding(String paramString, CharsetEncoder paramCharsetEncoder, boolean paramBoolean)
    throws IOException
  {
    this(new File(paramString), paramCharsetEncoder, paramBoolean);
  }
  
  /* Error */
  private static Writer initWriter(File paramFile, java.lang.Object paramObject, boolean paramBoolean)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: ifnonnull +13 -> 14
    //   4: new 45	java/lang/NullPointerException
    //   7: dup
    //   8: ldc 47
    //   10: invokespecial 48	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   13: athrow
    //   14: aload_1
    //   15: ifnonnull +13 -> 28
    //   18: new 45	java/lang/NullPointerException
    //   21: dup
    //   22: ldc 50
    //   24: invokespecial 48	java/lang/NullPointerException:<init>	(Ljava/lang/String;)V
    //   27: athrow
    //   28: aload_0
    //   29: invokevirtual 54	java/io/File:exists	()Z
    //   32: istore_3
    //   33: aconst_null
    //   34: astore 4
    //   36: new 56	java/io/FileOutputStream
    //   39: dup
    //   40: aload_0
    //   41: iload_2
    //   42: invokespecial 59	java/io/FileOutputStream:<init>	(Ljava/io/File;Z)V
    //   45: astore 4
    //   47: aload_1
    //   48: instanceof 61
    //   51: ifeq +21 -> 72
    //   54: new 63	java/io/OutputStreamWriter
    //   57: dup
    //   58: aload 4
    //   60: aload_1
    //   61: checkcast 61	java/nio/charset/Charset
    //   64: invokespecial 66	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/nio/charset/Charset;)V
    //   67: astore 5
    //   69: goto +112 -> 181
    //   72: aload_1
    //   73: instanceof 68
    //   76: ifeq +21 -> 97
    //   79: new 63	java/io/OutputStreamWriter
    //   82: dup
    //   83: aload 4
    //   85: aload_1
    //   86: checkcast 68	java/nio/charset/CharsetEncoder
    //   89: invokespecial 71	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/nio/charset/CharsetEncoder;)V
    //   92: astore 5
    //   94: goto +87 -> 181
    //   97: new 63	java/io/OutputStreamWriter
    //   100: dup
    //   101: aload 4
    //   103: aload_1
    //   104: checkcast 73	java/lang/String
    //   107: invokespecial 76	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;Ljava/lang/String;)V
    //   110: astore_3
    //   111: aload_3
    //   112: astore 5
    //   114: goto +67 -> 181
    //   117: astore 5
    //   119: aconst_null
    //   120: invokestatic 82	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   123: aload 4
    //   125: invokestatic 85	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   128: iload_3
    //   129: ifne +8 -> 137
    //   132: aload_0
    //   133: invokestatic 91	org/apache/commons/io/FileUtils:deleteQuietly	(Ljava/io/File;)Z
    //   136: pop
    //   137: aload 5
    //   139: athrow
    //   140: astore 5
    //   142: aconst_null
    //   143: invokestatic 82	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Writer;)V
    //   146: aload 4
    //   148: invokestatic 85	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   151: iload_3
    //   152: ifne +8 -> 160
    //   155: aload_0
    //   156: invokestatic 91	org/apache/commons/io/FileUtils:deleteQuietly	(Ljava/io/File;)Z
    //   159: pop
    //   160: aload 5
    //   162: athrow
    //   163: astore 5
    //   165: aload 4
    //   167: astore 4
    //   169: goto -27 -> 142
    //   172: astore 5
    //   174: aload 4
    //   176: astore 4
    //   178: goto -59 -> 119
    //   181: aload 5
    //   183: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	184	0	paramFile	File
    //   0	184	1	paramObject	java.lang.Object
    //   0	184	2	paramBoolean	boolean
    //   32	1	3	bool	boolean
    //   110	42	3	localOutputStreamWriter1	java.io.OutputStreamWriter
    //   34	143	4	localFileOutputStream	java.io.FileOutputStream
    //   67	46	5	localOutputStreamWriter2	java.io.OutputStreamWriter
    //   117	21	5	localIOException1	IOException
    //   140	21	5	localRuntimeException1	java.lang.RuntimeException
    //   163	1	5	localRuntimeException2	java.lang.RuntimeException
    //   172	10	5	localIOException2	IOException
    // Exception table:
    //   from	to	target	type
    //   36	47	117	java/io/IOException
    //   36	47	140	java/lang/RuntimeException
    //   47	111	163	java/lang/RuntimeException
    //   47	111	172	java/io/IOException
  }
  
  public void close()
    throws IOException
  {
    this.out.close();
  }
  
  public void flush()
    throws IOException
  {
    this.out.flush();
  }
  
  public void write(int paramInt)
    throws IOException
  {
    this.out.write(paramInt);
  }
  
  public void write(String paramString)
    throws IOException
  {
    this.out.write(paramString);
  }
  
  public void write(String paramString, int paramInt1, int paramInt2)
    throws IOException
  {
    this.out.write(paramString, paramInt1, paramInt2);
  }
  
  public void write(char[] paramArrayOfChar)
    throws IOException
  {
    this.out.write(paramArrayOfChar);
  }
  
  public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
    throws IOException
  {
    this.out.write(paramArrayOfChar, paramInt1, paramInt2);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.output.FileWriterWithEncoding
 * JD-Core Version:    0.7.0.1
 */