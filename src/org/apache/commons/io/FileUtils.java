package org.apache.commons.io;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.Checksum;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.output.NullOutputStream;

public class FileUtils
{
  public static final File[] EMPTY_FILE_ARRAY = new File[0];
  private static final long FILE_COPY_BUFFER_SIZE = 31457280L;
  public static final long ONE_EB = 1152921504606846976L;
  public static final BigInteger ONE_EB_BI;
  public static final long ONE_GB = 1073741824L;
  public static final BigInteger ONE_GB_BI;
  public static final long ONE_KB = 1024L;
  public static final BigInteger ONE_KB_BI = BigInteger.valueOf(1024L);
  public static final long ONE_MB = 1048576L;
  public static final BigInteger ONE_MB_BI = ONE_KB_BI.multiply(ONE_KB_BI);
  public static final long ONE_PB = 1125899906842624L;
  public static final BigInteger ONE_PB_BI;
  public static final long ONE_TB = 1099511627776L;
  public static final BigInteger ONE_TB_BI;
  public static final BigInteger ONE_YB;
  public static final BigInteger ONE_ZB;
  private static final Charset UTF8 = Charset.forName("UTF-8");
  
  static
  {
    ONE_GB_BI = ONE_KB_BI.multiply(ONE_MB_BI);
    ONE_TB_BI = ONE_KB_BI.multiply(ONE_GB_BI);
    ONE_PB_BI = ONE_KB_BI.multiply(ONE_TB_BI);
    ONE_EB_BI = ONE_KB_BI.multiply(ONE_PB_BI);
    ONE_ZB = BigInteger.valueOf(1024L).multiply(BigInteger.valueOf(1152921504606846976L));
    ONE_YB = ONE_KB_BI.multiply(ONE_ZB);
  }
  
  public static String byteCountToDisplaySize(long paramLong)
  {
    return byteCountToDisplaySize(BigInteger.valueOf(paramLong));
  }
  
  public static String byteCountToDisplaySize(BigInteger paramBigInteger)
  {
    String str;
    if (paramBigInteger.divide(ONE_EB_BI).compareTo(BigInteger.ZERO) <= 0)
    {
      if (paramBigInteger.divide(ONE_PB_BI).compareTo(BigInteger.ZERO) <= 0)
      {
        if (paramBigInteger.divide(ONE_TB_BI).compareTo(BigInteger.ZERO) <= 0)
        {
          if (paramBigInteger.divide(ONE_GB_BI).compareTo(BigInteger.ZERO) <= 0)
          {
            if (paramBigInteger.divide(ONE_MB_BI).compareTo(BigInteger.ZERO) <= 0)
            {
              if (paramBigInteger.divide(ONE_KB_BI).compareTo(BigInteger.ZERO) <= 0) {
                str = String.valueOf(paramBigInteger) + " bytes";
              } else {
                str = String.valueOf(paramBigInteger.divide(ONE_KB_BI)) + " KB";
              }
            }
            else {
              str = String.valueOf(paramBigInteger.divide(ONE_MB_BI)) + " MB";
            }
          }
          else {
            str = String.valueOf(paramBigInteger.divide(ONE_GB_BI)) + " GB";
          }
        }
        else {
          str = String.valueOf(paramBigInteger.divide(ONE_TB_BI)) + " TB";
        }
      }
      else {
        str = String.valueOf(paramBigInteger.divide(ONE_PB_BI)) + " PB";
      }
    }
    else {
      str = String.valueOf(paramBigInteger.divide(ONE_EB_BI)) + " EB";
    }
    return str;
  }
  
  private static void checkDirectory(File paramFile)
  {
    if (paramFile.exists())
    {
      if (paramFile.isDirectory()) {
        return;
      }
      throw new IllegalArgumentException(paramFile + " is not a directory");
    }
    throw new IllegalArgumentException(paramFile + " does not exist");
  }
  
  public static Checksum checksum(File paramFile, Checksum paramChecksum)
    throws IOException
  {
    if (paramFile.isDirectory()) {
      throw new IllegalArgumentException("Checksums can't be computed on directories");
    }
    localCheckedInputStream = null;
    try
    {
      localCheckedInputStream = new CheckedInputStream(new FileInputStream(paramFile), paramChecksum);
      IOUtils.closeQuietly(localCheckedInputStream);
    }
    finally
    {
      try
      {
        IOUtils.copy(localCheckedInputStream, new NullOutputStream());
        IOUtils.closeQuietly(localCheckedInputStream);
        return paramChecksum;
      }
      finally
      {
        localCheckedInputStream = localCheckedInputStream;
      }
      localObject1 = finally;
    }
    throw localObject1;
  }
  
  public static long checksumCRC32(File paramFile)
    throws IOException
  {
    CRC32 localCRC32 = new CRC32();
    checksum(paramFile, localCRC32);
    return localCRC32.getValue();
  }
  
  public static void cleanDirectory(File paramFile)
    throws IOException
  {
    if (!paramFile.exists()) {
      throw new IllegalArgumentException(paramFile + " does not exist");
    }
    if (!paramFile.isDirectory()) {
      throw new IllegalArgumentException(paramFile + " is not a directory");
    }
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile == null) {
      throw new IOException("Failed to list contents of " + paramFile);
    }
    Object localObject = null;
    int j = arrayOfFile.length;
    int i = 0;
    IOException localIOException2;
    for (;;)
    {
      if (i < j)
      {
        File localFile = arrayOfFile[i];
        try
        {
          forceDelete(localFile);
          i++;
        }
        catch (IOException localIOException1)
        {
          for (;;)
          {
            localIOException2 = localIOException1;
          }
        }
      }
    }
    if (localIOException2 != null) {
      throw localIOException2;
    }
  }
  
  private static void cleanDirectoryOnExit(File paramFile)
    throws IOException
  {
    if (!paramFile.exists()) {
      throw new IllegalArgumentException(paramFile + " does not exist");
    }
    if (!paramFile.isDirectory()) {
      throw new IllegalArgumentException(paramFile + " is not a directory");
    }
    File[] arrayOfFile = paramFile.listFiles();
    if (arrayOfFile == null) {
      throw new IOException("Failed to list contents of " + paramFile);
    }
    Object localObject = null;
    int j = arrayOfFile.length;
    int i = 0;
    IOException localIOException2;
    for (;;)
    {
      if (i < j)
      {
        File localFile = arrayOfFile[i];
        try
        {
          forceDeleteOnExit(localFile);
          i++;
        }
        catch (IOException localIOException1)
        {
          for (;;)
          {
            localIOException2 = localIOException1;
          }
        }
      }
    }
    if (localIOException2 != null) {
      throw localIOException2;
    }
  }
  
  /* Error */
  public static boolean contentEquals(File paramFile1, File paramFile2)
    throws IOException
  {
    // Byte code:
    //   0: iconst_0
    //   1: istore 4
    //   3: aload_0
    //   4: invokevirtual 135	java/io/File:exists	()Z
    //   7: istore_2
    //   8: iload_2
    //   9: aload_1
    //   10: invokevirtual 135	java/io/File:exists	()Z
    //   13: if_icmpeq +6 -> 19
    //   16: iload 4
    //   18: ireturn
    //   19: iload_2
    //   20: ifne +9 -> 29
    //   23: iconst_1
    //   24: istore 4
    //   26: goto -10 -> 16
    //   29: aload_0
    //   30: invokevirtual 138	java/io/File:isDirectory	()Z
    //   33: ifne +10 -> 43
    //   36: aload_1
    //   37: invokevirtual 138	java/io/File:isDirectory	()Z
    //   40: ifeq +13 -> 53
    //   43: new 154	java/io/IOException
    //   46: dup
    //   47: ldc 208
    //   49: invokespecial 197	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   52: athrow
    //   53: aload_0
    //   54: invokevirtual 211	java/io/File:length	()J
    //   57: aload_1
    //   58: invokevirtual 211	java/io/File:length	()J
    //   61: lcmp
    //   62: ifne -46 -> 16
    //   65: aload_0
    //   66: invokevirtual 215	java/io/File:getCanonicalFile	()Ljava/io/File;
    //   69: aload_1
    //   70: invokevirtual 215	java/io/File:getCanonicalFile	()Ljava/io/File;
    //   73: invokevirtual 219	java/io/File:equals	(Ljava/lang/Object;)Z
    //   76: ifeq +9 -> 85
    //   79: iconst_1
    //   80: istore 4
    //   82: goto -66 -> 16
    //   85: aconst_null
    //   86: astore_2
    //   87: aconst_null
    //   88: astore_3
    //   89: new 160	java/io/FileInputStream
    //   92: dup
    //   93: aload_0
    //   94: invokespecial 162	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   97: astore_2
    //   98: new 160	java/io/FileInputStream
    //   101: dup
    //   102: aload_1
    //   103: invokespecial 162	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   106: astore_3
    //   107: aload_2
    //   108: aload_3
    //   109: invokestatic 222	org/apache/commons/io/IOUtils:contentEquals	(Ljava/io/InputStream;Ljava/io/InputStream;)Z
    //   112: istore 4
    //   114: iload 4
    //   116: istore 4
    //   118: aload_2
    //   119: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   122: aload_3
    //   123: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   126: goto -110 -> 16
    //   129: astore 4
    //   131: aload_2
    //   132: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   135: aload_3
    //   136: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   139: aload 4
    //   141: athrow
    //   142: astore 4
    //   144: aload_2
    //   145: astore_2
    //   146: goto -15 -> 131
    //   149: astore 4
    //   151: aload_3
    //   152: astore_3
    //   153: aload_2
    //   154: astore_2
    //   155: goto -24 -> 131
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	158	0	paramFile1	File
    //   0	158	1	paramFile2	File
    //   7	13	2	bool1	boolean
    //   86	69	2	localFileInputStream1	FileInputStream
    //   88	65	3	localFileInputStream2	FileInputStream
    //   1	116	4	bool2	boolean
    //   129	11	4	localObject1	Object
    //   142	1	4	localObject2	Object
    //   149	1	4	localObject3	Object
    // Exception table:
    //   from	to	target	type
    //   89	98	129	finally
    //   98	107	142	finally
    //   107	114	149	finally
  }
  
  public static boolean contentEqualsIgnoreEOL(File paramFile1, File paramFile2, String paramString)
    throws IOException
  {
    boolean bool2 = true;
    boolean bool1 = paramFile1.exists();
    if (bool1 != paramFile2.exists()) {
      bool2 = false;
    }
    do
    {
      do
      {
        return bool2;
      } while (!bool1);
      if ((paramFile1.isDirectory()) || (paramFile2.isDirectory())) {
        throw new IOException("Can't compare directories, only files");
      }
    } while (paramFile1.getCanonicalFile().equals(paramFile2.getCanonicalFile()));
    localInputStreamReader2 = null;
    InputStreamReader localInputStreamReader1 = null;
    if (paramString == null) {}
    for (;;)
    {
      for (;;)
      {
        try
        {
          localInputStreamReader2 = new InputStreamReader(new FileInputStream(paramFile1));
        }
        finally {}
        try
        {
          localInputStreamReader1 = new InputStreamReader(new FileInputStream(paramFile2));
          localInputStreamReader1 = localInputStreamReader1;
          localInputStreamReader2 = localInputStreamReader2;
          bool2 = IOUtils.contentEqualsIgnoreEOL(localInputStreamReader2, localInputStreamReader1);
          bool2 = bool2;
          IOUtils.closeQuietly(localInputStreamReader2);
          IOUtils.closeQuietly(localInputStreamReader1);
          break;
        }
        finally
        {
          for (;;)
          {
            localInputStreamReader2 = localInputStreamReader2;
          }
        }
      }
      localInputStreamReader2 = new InputStreamReader(new FileInputStream(paramFile1), paramString);
      localInputStreamReader1 = new InputStreamReader(new FileInputStream(paramFile2), paramString);
      localInputStreamReader1 = localInputStreamReader1;
      localInputStreamReader2 = localInputStreamReader2;
    }
    IOUtils.closeQuietly(localInputStreamReader2);
    IOUtils.closeQuietly(localInputStreamReader1);
    throw localObject1;
  }
  
  public static File[] convertFileCollectionToFileArray(Collection<File> paramCollection)
  {
    return (File[])paramCollection.toArray(new File[paramCollection.size()]);
  }
  
  public static void copyDirectory(File paramFile1, File paramFile2)
    throws IOException
  {
    copyDirectory(paramFile1, paramFile2, true);
  }
  
  public static void copyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter)
    throws IOException
  {
    copyDirectory(paramFile1, paramFile2, paramFileFilter, true);
  }
  
  public static void copyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter, boolean paramBoolean)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if (paramFile1.exists())
        {
          if (paramFile1.isDirectory())
          {
            if (!paramFile1.getCanonicalPath().equals(paramFile2.getCanonicalPath()))
            {
              ArrayList localArrayList = null;
              File[] arrayOfFile2;
              int i;
              if (paramFile2.getCanonicalPath().startsWith(paramFile1.getCanonicalPath()))
              {
                File[] arrayOfFile1;
                if (paramFileFilter != null) {
                  arrayOfFile1 = paramFile1.listFiles(paramFileFilter);
                } else {
                  arrayOfFile1 = paramFile1.listFiles();
                }
                if ((arrayOfFile1 != null) && (arrayOfFile1.length > 0))
                {
                  localArrayList = new ArrayList(arrayOfFile1.length);
                  arrayOfFile2 = arrayOfFile1;
                  i = arrayOfFile2.length;
                }
              }
              for (int j = 0;; j++)
              {
                if (j >= i)
                {
                  doCopyDirectory(paramFile1, paramFile2, paramFileFilter, paramBoolean, localArrayList);
                  return;
                }
                localArrayList.add(new File(paramFile2, arrayOfFile2[j].getName()).getCanonicalPath());
              }
            }
            throw new IOException("Source '" + paramFile1 + "' and destination '" + paramFile2 + "' are the same");
          }
          throw new IOException("Source '" + paramFile1 + "' exists but is not a directory");
        }
        throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
      }
      throw new NullPointerException("Destination must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void copyDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    copyDirectory(paramFile1, paramFile2, null, paramBoolean);
  }
  
  public static void copyDirectoryToDirectory(File paramFile1, File paramFile2)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if ((!paramFile1.exists()) || (paramFile1.isDirectory()))
      {
        if (paramFile2 != null)
        {
          if ((!paramFile2.exists()) || (paramFile2.isDirectory()))
          {
            copyDirectory(paramFile1, new File(paramFile2, paramFile1.getName()), true);
            return;
          }
          throw new IllegalArgumentException("Destination '" + paramFile2 + "' is not a directory");
        }
        throw new NullPointerException("Destination must not be null");
      }
      throw new IllegalArgumentException("Source '" + paramFile2 + "' is not a directory");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static long copyFile(File paramFile, OutputStream paramOutputStream)
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(paramFile);
    try
    {
      long l = IOUtils.copyLarge(localFileInputStream, paramOutputStream);
      return l;
    }
    finally
    {
      localFileInputStream.close();
    }
  }
  
  public static void copyFile(File paramFile1, File paramFile2)
    throws IOException
  {
    copyFile(paramFile1, paramFile2, true);
  }
  
  public static void copyFile(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if (paramFile1.exists())
        {
          if (!paramFile1.isDirectory())
          {
            if (!paramFile1.getCanonicalPath().equals(paramFile2.getCanonicalPath()))
            {
              File localFile = paramFile2.getParentFile();
              if ((localFile == null) || (localFile.mkdirs()) || (localFile.isDirectory()))
              {
                if ((!paramFile2.exists()) || (paramFile2.canWrite()))
                {
                  doCopyFile(paramFile1, paramFile2, paramBoolean);
                  return;
                }
                throw new IOException("Destination '" + paramFile2 + "' exists but is read-only");
              }
              throw new IOException("Destination '" + localFile + "' directory cannot be created");
            }
            throw new IOException("Source '" + paramFile1 + "' and destination '" + paramFile2 + "' are the same");
          }
          throw new IOException("Source '" + paramFile1 + "' exists but is a directory");
        }
        throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
      }
      throw new NullPointerException("Destination must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void copyFileToDirectory(File paramFile1, File paramFile2)
    throws IOException
  {
    copyFileToDirectory(paramFile1, paramFile2, true);
  }
  
  public static void copyFileToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    if (paramFile2 != null)
    {
      if ((!paramFile2.exists()) || (paramFile2.isDirectory()))
      {
        copyFile(paramFile1, new File(paramFile2, paramFile1.getName()), paramBoolean);
        return;
      }
      throw new IllegalArgumentException("Destination '" + paramFile2 + "' is not a directory");
    }
    throw new NullPointerException("Destination must not be null");
  }
  
  /* Error */
  public static void copyInputStreamToFile(InputStream paramInputStream, File paramFile)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokestatic 353	org/apache/commons/io/FileUtils:openOutputStream	(Ljava/io/File;)Ljava/io/FileOutputStream;
    //   4: astore_2
    //   5: aload_0
    //   6: aload_2
    //   7: invokestatic 174	org/apache/commons/io/IOUtils:copy	(Ljava/io/InputStream;Ljava/io/OutputStream;)I
    //   10: pop
    //   11: aload_2
    //   12: invokevirtual 356	java/io/FileOutputStream:close	()V
    //   15: aload_2
    //   16: invokestatic 359	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   19: aload_0
    //   20: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   23: return
    //   24: astore_3
    //   25: aload_2
    //   26: invokestatic 359	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   29: aload_3
    //   30: athrow
    //   31: astore_2
    //   32: aload_0
    //   33: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   36: aload_2
    //   37: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	38	0	paramInputStream	InputStream
    //   0	38	1	paramFile	File
    //   4	22	2	localFileOutputStream	FileOutputStream
    //   31	6	2	localObject1	Object
    //   24	6	3	localObject2	Object
    // Exception table:
    //   from	to	target	type
    //   5	15	24	finally
    //   0	5	31	finally
    //   15	19	31	finally
    //   25	31	31	finally
  }
  
  public static void copyURLToFile(URL paramURL, File paramFile)
    throws IOException
  {
    copyInputStreamToFile(paramURL.openStream(), paramFile);
  }
  
  public static void copyURLToFile(URL paramURL, File paramFile, int paramInt1, int paramInt2)
    throws IOException
  {
    URLConnection localURLConnection = paramURL.openConnection();
    localURLConnection.setConnectTimeout(paramInt1);
    localURLConnection.setReadTimeout(paramInt2);
    copyInputStreamToFile(localURLConnection.getInputStream(), paramFile);
  }
  
  /* Error */
  static String decodeUrl(String paramString)
  {
    // Byte code:
    //   0: aload_0
    //   1: astore_1
    //   2: aload_0
    //   3: ifnull +237 -> 240
    //   6: aload_0
    //   7: bipush 37
    //   9: invokevirtual 393	java/lang/String:indexOf	(I)I
    //   12: iflt +228 -> 240
    //   15: aload_0
    //   16: invokevirtual 395	java/lang/String:length	()I
    //   19: istore_3
    //   20: new 397	java/lang/StringBuffer
    //   23: dup
    //   24: invokespecial 398	java/lang/StringBuffer:<init>	()V
    //   27: astore_1
    //   28: iload_3
    //   29: invokestatic 404	java/nio/ByteBuffer:allocate	(I)Ljava/nio/ByteBuffer;
    //   32: astore_2
    //   33: iconst_0
    //   34: istore 4
    //   36: iload 4
    //   38: iload_3
    //   39: if_icmpge +196 -> 235
    //   42: aload_0
    //   43: iload 4
    //   45: invokevirtual 408	java/lang/String:charAt	(I)C
    //   48: bipush 37
    //   50: if_icmpne +126 -> 176
    //   53: iload 4
    //   55: iconst_1
    //   56: iadd
    //   57: istore 5
    //   59: iload 4
    //   61: iconst_3
    //   62: iadd
    //   63: istore 6
    //   65: aload_2
    //   66: aload_0
    //   67: iload 5
    //   69: iload 6
    //   71: invokevirtual 412	java/lang/String:substring	(II)Ljava/lang/String;
    //   74: bipush 16
    //   76: invokestatic 418	java/lang/Integer:parseInt	(Ljava/lang/String;I)I
    //   79: i2b
    //   80: invokevirtual 422	java/nio/ByteBuffer:put	(B)Ljava/nio/ByteBuffer;
    //   83: pop
    //   84: iinc 4 3
    //   87: iload 4
    //   89: iload_3
    //   90: if_icmpge +18 -> 108
    //   93: aload_0
    //   94: iload 4
    //   96: invokevirtual 408	java/lang/String:charAt	(I)C
    //   99: istore 5
    //   101: iload 5
    //   103: bipush 37
    //   105: if_icmpeq -52 -> 53
    //   108: aload_2
    //   109: invokevirtual 425	java/nio/ByteBuffer:position	()I
    //   112: ifle -76 -> 36
    //   115: aload_2
    //   116: invokevirtual 429	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
    //   119: pop
    //   120: aload_1
    //   121: getstatic 81	org/apache/commons/io/FileUtils:UTF8	Ljava/nio/charset/Charset;
    //   124: aload_2
    //   125: invokevirtual 433	java/nio/charset/Charset:decode	(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;
    //   128: invokevirtual 436	java/nio/CharBuffer:toString	()Ljava/lang/String;
    //   131: invokevirtual 439	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   134: pop
    //   135: aload_2
    //   136: invokevirtual 442	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   139: pop
    //   140: goto -104 -> 36
    //   143: pop
    //   144: aload_2
    //   145: invokevirtual 425	java/nio/ByteBuffer:position	()I
    //   148: ifle +28 -> 176
    //   151: aload_2
    //   152: invokevirtual 429	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
    //   155: pop
    //   156: aload_1
    //   157: getstatic 81	org/apache/commons/io/FileUtils:UTF8	Ljava/nio/charset/Charset;
    //   160: aload_2
    //   161: invokevirtual 433	java/nio/charset/Charset:decode	(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;
    //   164: invokevirtual 436	java/nio/CharBuffer:toString	()Ljava/lang/String;
    //   167: invokevirtual 439	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   170: pop
    //   171: aload_2
    //   172: invokevirtual 442	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   175: pop
    //   176: iload 4
    //   178: iconst_1
    //   179: iadd
    //   180: istore 5
    //   182: aload_1
    //   183: aload_0
    //   184: iload 4
    //   186: invokevirtual 408	java/lang/String:charAt	(I)C
    //   189: invokevirtual 445	java/lang/StringBuffer:append	(C)Ljava/lang/StringBuffer;
    //   192: pop
    //   193: iload 5
    //   195: istore 4
    //   197: goto -161 -> 36
    //   200: astore_3
    //   201: aload_2
    //   202: invokevirtual 425	java/nio/ByteBuffer:position	()I
    //   205: ifle +28 -> 233
    //   208: aload_2
    //   209: invokevirtual 429	java/nio/ByteBuffer:flip	()Ljava/nio/Buffer;
    //   212: pop
    //   213: aload_1
    //   214: getstatic 81	org/apache/commons/io/FileUtils:UTF8	Ljava/nio/charset/Charset;
    //   217: aload_2
    //   218: invokevirtual 433	java/nio/charset/Charset:decode	(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;
    //   221: invokevirtual 436	java/nio/CharBuffer:toString	()Ljava/lang/String;
    //   224: invokevirtual 439	java/lang/StringBuffer:append	(Ljava/lang/String;)Ljava/lang/StringBuffer;
    //   227: pop
    //   228: aload_2
    //   229: invokevirtual 442	java/nio/ByteBuffer:clear	()Ljava/nio/Buffer;
    //   232: pop
    //   233: aload_3
    //   234: athrow
    //   235: aload_1
    //   236: invokevirtual 446	java/lang/StringBuffer:toString	()Ljava/lang/String;
    //   239: astore_1
    //   240: aload_1
    //   241: areturn
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	242	0	paramString	String
    //   1	240	1	localObject1	Object
    //   32	197	2	localByteBuffer	java.nio.ByteBuffer
    //   19	72	3	i	int
    //   200	34	3	localObject2	Object
    //   34	162	4	j	int
    //   57	137	5	k	int
    //   63	7	6	m	int
    //   143	1	8	localRuntimeException	RuntimeException
    // Exception table:
    //   from	to	target	type
    //   65	101	143	java/lang/RuntimeException
    //   65	101	200	finally
  }
  
  public static void deleteDirectory(File paramFile)
    throws IOException
  {
    if (paramFile.exists())
    {
      if (!isSymlink(paramFile)) {
        cleanDirectory(paramFile);
      }
      if (!paramFile.delete()) {}
    }
    else
    {
      return;
    }
    throw new IOException("Unable to delete directory " + paramFile + ".");
  }
  
  private static void deleteDirectoryOnExit(File paramFile)
    throws IOException
  {
    if (paramFile.exists())
    {
      paramFile.deleteOnExit();
      if (!isSymlink(paramFile)) {
        cleanDirectoryOnExit(paramFile);
      }
    }
  }
  
  public static boolean deleteQuietly(File paramFile)
  {
    boolean bool = false;
    if (paramFile == null) {}
    for (;;)
    {
      return bool;
      try
      {
        if (paramFile.isDirectory()) {
          cleanDirectory(paramFile);
        }
        try
        {
          label19:
          bool = paramFile.delete();
          bool = bool;
        }
        catch (Exception localException1) {}
      }
      catch (Exception localException2)
      {
        break label19;
      }
    }
  }
  
  public static boolean directoryContains(File paramFile1, File paramFile2)
    throws IOException
  {
    boolean bool = false;
    if (paramFile1 != null)
    {
      if (paramFile1.isDirectory())
      {
        if ((paramFile2 != null) && (paramFile1.exists()) && (paramFile2.exists())) {
          bool = FilenameUtils.directoryContains(paramFile1.getCanonicalPath(), paramFile2.getCanonicalPath());
        }
        return bool;
      }
      throw new IllegalArgumentException("Not a directory: " + paramFile1);
    }
    throw new IllegalArgumentException("Directory must not be null");
  }
  
  private static void doCopyDirectory(File paramFile1, File paramFile2, FileFilter paramFileFilter, boolean paramBoolean, List<String> paramList)
    throws IOException
  {
    File[] arrayOfFile1;
    if (paramFileFilter != null) {
      arrayOfFile1 = paramFile1.listFiles(paramFileFilter);
    } else {
      arrayOfFile1 = paramFile1.listFiles();
    }
    if (arrayOfFile1 != null)
    {
      if (!paramFile2.exists())
      {
        if ((!paramFile2.mkdirs()) && (!paramFile2.isDirectory())) {
          throw new IOException("Destination '" + paramFile2 + "' directory cannot be created");
        }
      }
      else {
        if (!paramFile2.isDirectory()) {
          break label239;
        }
      }
      if (paramFile2.canWrite())
      {
        File[] arrayOfFile2 = arrayOfFile1;
        int i = arrayOfFile2.length;
        for (int j = 0;; j++)
        {
          if (j >= i)
          {
            if (paramBoolean) {
              paramFile2.setLastModified(paramFile1.lastModified());
            }
            return;
          }
          arrayOfFile1 = arrayOfFile2[j];
          File localFile = new File(paramFile2, arrayOfFile1.getName());
          if ((paramList == null) || (!paramList.contains(arrayOfFile1.getCanonicalPath()))) {
            if (!arrayOfFile1.isDirectory()) {
              doCopyFile(arrayOfFile1, localFile, paramBoolean);
            } else {
              doCopyDirectory(arrayOfFile1, localFile, paramFileFilter, paramBoolean, paramList);
            }
          }
        }
      }
      throw new IOException("Destination '" + paramFile2 + "' cannot be written to");
      label239:
      throw new IOException("Destination '" + paramFile2 + "' exists but is not a directory");
    }
    throw new IOException("Failed to list contents of " + paramFile1);
  }
  
  /* Error */
  private static void doCopyFile(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    // Byte code:
    //   0: aload_1
    //   1: invokevirtual 135	java/io/File:exists	()Z
    //   4: ifeq +44 -> 48
    //   7: aload_1
    //   8: invokevirtual 138	java/io/File:isDirectory	()Z
    //   11: ifeq +37 -> 48
    //   14: new 154	java/io/IOException
    //   17: dup
    //   18: new 101	java/lang/StringBuilder
    //   21: dup
    //   22: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   25: ldc_w 313
    //   28: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   31: aload_1
    //   32: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   35: ldc_w 344
    //   38: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   41: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   44: invokespecial 197	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   47: athrow
    //   48: aconst_null
    //   49: astore 5
    //   51: aconst_null
    //   52: astore 6
    //   54: aconst_null
    //   55: astore 4
    //   57: aconst_null
    //   58: astore_3
    //   59: new 160	java/io/FileInputStream
    //   62: dup
    //   63: aload_0
    //   64: invokespecial 162	java/io/FileInputStream:<init>	(Ljava/io/File;)V
    //   67: astore 5
    //   69: new 355	java/io/FileOutputStream
    //   72: dup
    //   73: aload_1
    //   74: invokespecial 492	java/io/FileOutputStream:<init>	(Ljava/io/File;)V
    //   77: astore 6
    //   79: aload 5
    //   81: invokevirtual 496	java/io/FileInputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   84: astore 4
    //   86: aload 6
    //   88: invokevirtual 497	java/io/FileOutputStream:getChannel	()Ljava/nio/channels/FileChannel;
    //   91: astore_3
    //   92: aload 4
    //   94: invokevirtual 501	java/nio/channels/FileChannel:size	()J
    //   97: lstore 7
    //   99: ldc2_w 502
    //   102: lstore 9
    //   104: goto +170 -> 274
    //   107: aload_3
    //   108: aload 4
    //   110: lload 9
    //   112: lload 11
    //   114: invokevirtual 507	java/nio/channels/FileChannel:transferFrom	(Ljava/nio/channels/ReadableByteChannel;JJ)J
    //   117: lstore 11
    //   119: lload 9
    //   121: lload 11
    //   123: ladd
    //   124: lstore 9
    //   126: goto +148 -> 274
    //   129: lload 7
    //   131: lload 9
    //   133: lsub
    //   134: lstore 11
    //   136: goto -29 -> 107
    //   139: aload_3
    //   140: invokestatic 510	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   143: aload 6
    //   145: invokestatic 359	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   148: aload 4
    //   150: invokestatic 510	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   153: aload 5
    //   155: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   158: aload_0
    //   159: invokevirtual 211	java/io/File:length	()J
    //   162: aload_1
    //   163: invokevirtual 211	java/io/File:length	()J
    //   166: lcmp
    //   167: ifeq +71 -> 238
    //   170: new 154	java/io/IOException
    //   173: dup
    //   174: new 101	java/lang/StringBuilder
    //   177: dup
    //   178: invokespecial 102	java/lang/StringBuilder:<init>	()V
    //   181: ldc_w 512
    //   184: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   187: aload_0
    //   188: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   191: ldc_w 514
    //   194: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   197: aload_1
    //   198: invokevirtual 143	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   201: ldc_w 516
    //   204: invokevirtual 111	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   207: invokevirtual 117	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   210: invokespecial 197	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   213: athrow
    //   214: astore 7
    //   216: aload_3
    //   217: invokestatic 510	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   220: aload 6
    //   222: invokestatic 359	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   225: aload 4
    //   227: invokestatic 510	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   230: aload 5
    //   232: invokestatic 178	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   235: aload 7
    //   237: athrow
    //   238: iload_2
    //   239: ifeq +12 -> 251
    //   242: aload_1
    //   243: aload_0
    //   244: invokevirtual 482	java/io/File:lastModified	()J
    //   247: invokevirtual 486	java/io/File:setLastModified	(J)Z
    //   250: pop
    //   251: return
    //   252: astore 7
    //   254: aload 5
    //   256: astore 5
    //   258: goto -42 -> 216
    //   261: astore 7
    //   263: aload 6
    //   265: astore 6
    //   267: aload 5
    //   269: astore 5
    //   271: goto -55 -> 216
    //   274: lload 9
    //   276: lload 7
    //   278: lcmp
    //   279: ifge -140 -> 139
    //   282: lload 7
    //   284: lload 9
    //   286: lsub
    //   287: ldc2_w 9
    //   290: lcmp
    //   291: ifle -162 -> 129
    //   294: ldc2_w 9
    //   297: lstore 11
    //   299: goto -192 -> 107
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	302	0	paramFile1	File
    //   0	302	1	paramFile2	File
    //   0	302	2	paramBoolean	boolean
    //   58	159	3	localFileChannel1	java.nio.channels.FileChannel
    //   55	171	4	localFileChannel2	java.nio.channels.FileChannel
    //   49	221	5	localFileInputStream	FileInputStream
    //   52	214	6	localFileOutputStream	FileOutputStream
    //   97	33	7	l1	long
    //   214	22	7	localObject1	Object
    //   252	1	7	localObject2	Object
    //   261	22	7	localObject3	Object
    //   102	183	9	l2	long
    //   112	1	11	localObject4	Object
    //   117	181	11	l3	long
    // Exception table:
    //   from	to	target	type
    //   59	69	214	finally
    //   69	79	252	finally
    //   79	119	261	finally
  }
  
  public static void forceDelete(File paramFile)
    throws IOException
  {
    if (!paramFile.isDirectory())
    {
      boolean bool = paramFile.exists();
      if (!paramFile.delete())
      {
        if (bool) {
          throw new IOException("Unable to delete file: " + paramFile);
        }
        throw new FileNotFoundException("File does not exist: " + paramFile);
      }
    }
    else
    {
      deleteDirectory(paramFile);
    }
  }
  
  public static void forceDeleteOnExit(File paramFile)
    throws IOException
  {
    if (!paramFile.isDirectory()) {
      paramFile.deleteOnExit();
    } else {
      deleteDirectoryOnExit(paramFile);
    }
  }
  
  public static void forceMkdir(File paramFile)
    throws IOException
  {
    if (!paramFile.exists())
    {
      if ((!paramFile.mkdirs()) && (!paramFile.isDirectory())) {
        throw new IOException("Unable to create directory " + paramFile);
      }
    }
    else {
      if (!paramFile.isDirectory()) {
        break label57;
      }
    }
    return;
    label57:
    throw new IOException("File " + paramFile + " exists and is " + "not a directory. Unable to create directory.");
  }
  
  public static File getFile(File paramFile, String... paramVarArgs)
  {
    if (paramFile != null)
    {
      if (paramVarArgs != null)
      {
        int i = paramVarArgs.length;
        int j = 0;
        for (File localFile = paramFile;; localFile = localFile)
        {
          if (j >= i) {
            return localFile;
          }
          localFile = new File(localFile, paramVarArgs[j]);
          j++;
        }
      }
      throw new NullPointerException("names must not be null");
    }
    throw new NullPointerException("directorydirectory must not be null");
  }
  
  public static File getFile(String... paramVarArgs)
  {
    if (paramVarArgs != null)
    {
      int i = paramVarArgs.length;
      int j = 0;
      for (File localFile = null;; localFile = localFile)
      {
        if (j >= i) {
          return localFile;
        }
        String str = paramVarArgs[j];
        if (localFile != null) {
          localFile = new File(localFile, str);
        } else {
          localFile = new File(str);
        }
        j++;
      }
    }
    throw new NullPointerException("names must not be null");
  }
  
  public static File getTempDirectory()
  {
    return new File(getTempDirectoryPath());
  }
  
  public static String getTempDirectoryPath()
  {
    return System.getProperty("java.io.tmpdir");
  }
  
  public static File getUserDirectory()
  {
    return new File(getUserDirectoryPath());
  }
  
  public static String getUserDirectoryPath()
  {
    return System.getProperty("user.home");
  }
  
  private static void innerListFiles(Collection<File> paramCollection, File paramFile, IOFileFilter paramIOFileFilter, boolean paramBoolean)
  {
    File[] arrayOfFile = paramFile.listFiles(paramIOFileFilter);
    int i;
    if (arrayOfFile != null) {
      i = arrayOfFile.length;
    }
    for (int j = 0;; j++)
    {
      if (j >= i) {
        return;
      }
      File localFile = arrayOfFile[j];
      if (!localFile.isDirectory())
      {
        paramCollection.add(localFile);
      }
      else
      {
        if (paramBoolean) {
          paramCollection.add(localFile);
        }
        innerListFiles(paramCollection, localFile, paramIOFileFilter, paramBoolean);
      }
    }
  }
  
  public static boolean isFileNewer(File paramFile, long paramLong)
  {
    boolean bool = false;
    if (paramFile != null)
    {
      if ((paramFile.exists()) && (paramFile.lastModified() > paramLong)) {
        bool = true;
      }
      return bool;
    }
    throw new IllegalArgumentException("No specified file");
  }
  
  public static boolean isFileNewer(File paramFile1, File paramFile2)
  {
    if (paramFile2 != null)
    {
      if (paramFile2.exists()) {
        return isFileNewer(paramFile1, paramFile2.lastModified());
      }
      throw new IllegalArgumentException("The reference file '" + paramFile2 + "' doesn't exist");
    }
    throw new IllegalArgumentException("No specified reference file");
  }
  
  public static boolean isFileNewer(File paramFile, Date paramDate)
  {
    if (paramDate != null) {
      return isFileNewer(paramFile, paramDate.getTime());
    }
    throw new IllegalArgumentException("No specified date");
  }
  
  public static boolean isFileOlder(File paramFile, long paramLong)
  {
    boolean bool = false;
    if (paramFile != null)
    {
      if ((paramFile.exists()) && (paramFile.lastModified() < paramLong)) {
        bool = true;
      }
      return bool;
    }
    throw new IllegalArgumentException("No specified file");
  }
  
  public static boolean isFileOlder(File paramFile1, File paramFile2)
  {
    if (paramFile2 != null)
    {
      if (paramFile2.exists()) {
        return isFileOlder(paramFile1, paramFile2.lastModified());
      }
      throw new IllegalArgumentException("The reference file '" + paramFile2 + "' doesn't exist");
    }
    throw new IllegalArgumentException("No specified reference file");
  }
  
  public static boolean isFileOlder(File paramFile, Date paramDate)
  {
    if (paramDate != null) {
      return isFileOlder(paramFile, paramDate.getTime());
    }
    throw new IllegalArgumentException("No specified date");
  }
  
  public static boolean isSymlink(File paramFile)
    throws IOException
  {
    boolean bool = false;
    if (paramFile != null)
    {
      if (!FilenameUtils.isSystemWindows())
      {
        File localFile;
        if (paramFile.getParent() != null) {
          localFile = new File(paramFile.getParentFile().getCanonicalFile(), paramFile.getName());
        } else {
          localFile = paramFile;
        }
        if (!localFile.getCanonicalFile().equals(localFile.getAbsoluteFile())) {
          bool = true;
        }
      }
      return bool;
    }
    throw new NullPointerException("File must not be null");
  }
  
  public static Iterator<File> iterateFiles(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    return listFiles(paramFile, paramIOFileFilter1, paramIOFileFilter2).iterator();
  }
  
  public static Iterator<File> iterateFiles(File paramFile, String[] paramArrayOfString, boolean paramBoolean)
  {
    return listFiles(paramFile, paramArrayOfString, paramBoolean).iterator();
  }
  
  public static Iterator<File> iterateFilesAndDirs(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    return listFilesAndDirs(paramFile, paramIOFileFilter1, paramIOFileFilter2).iterator();
  }
  
  public static LineIterator lineIterator(File paramFile)
    throws IOException
  {
    return lineIterator(paramFile, null);
  }
  
  public static LineIterator lineIterator(File paramFile, String paramString)
    throws IOException
  {
    Object localObject = null;
    try
    {
      localObject = openInputStream(paramFile);
      localObject = IOUtils.lineIterator((InputStream)localObject, paramString);
      return localObject;
    }
    catch (IOException localIOException)
    {
      IOUtils.closeQuietly((InputStream)localObject);
      throw localIOException;
    }
    catch (RuntimeException localRuntimeException)
    {
      IOUtils.closeQuietly((InputStream)localObject);
      throw localRuntimeException;
    }
  }
  
  public static Collection<File> listFiles(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    validateListFilesParameters(paramFile, paramIOFileFilter1);
    IOFileFilter localIOFileFilter2 = setUpEffectiveFileFilter(paramIOFileFilter1);
    IOFileFilter localIOFileFilter1 = setUpEffectiveDirFilter(paramIOFileFilter2);
    LinkedList localLinkedList = new LinkedList();
    IOFileFilter[] arrayOfIOFileFilter = new IOFileFilter[2];
    arrayOfIOFileFilter[0] = localIOFileFilter2;
    arrayOfIOFileFilter[1] = localIOFileFilter1;
    innerListFiles(localLinkedList, paramFile, FileFilterUtils.or(arrayOfIOFileFilter), false);
    return localLinkedList;
  }
  
  public static Collection<File> listFiles(File paramFile, String[] paramArrayOfString, boolean paramBoolean)
  {
    Object localObject;
    if (paramArrayOfString != null) {
      localObject = new SuffixFileFilter(toSuffixes(paramArrayOfString));
    } else {
      localObject = TrueFileFilter.INSTANCE;
    }
    IOFileFilter localIOFileFilter;
    if (!paramBoolean) {
      localIOFileFilter = FalseFileFilter.INSTANCE;
    } else {
      localIOFileFilter = TrueFileFilter.INSTANCE;
    }
    return listFiles(paramFile, (IOFileFilter)localObject, localIOFileFilter);
  }
  
  public static Collection<File> listFilesAndDirs(File paramFile, IOFileFilter paramIOFileFilter1, IOFileFilter paramIOFileFilter2)
  {
    validateListFilesParameters(paramFile, paramIOFileFilter1);
    IOFileFilter localIOFileFilter2 = setUpEffectiveFileFilter(paramIOFileFilter1);
    IOFileFilter localIOFileFilter1 = setUpEffectiveDirFilter(paramIOFileFilter2);
    LinkedList localLinkedList = new LinkedList();
    if (paramFile.isDirectory()) {
      localLinkedList.add(paramFile);
    }
    IOFileFilter[] arrayOfIOFileFilter = new IOFileFilter[2];
    arrayOfIOFileFilter[0] = localIOFileFilter2;
    arrayOfIOFileFilter[1] = localIOFileFilter1;
    innerListFiles(localLinkedList, paramFile, FileFilterUtils.or(arrayOfIOFileFilter), true);
    return localLinkedList;
  }
  
  public static void moveDirectory(File paramFile1, File paramFile2)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if (paramFile1.exists())
        {
          if (paramFile1.isDirectory())
          {
            if (!paramFile2.exists())
            {
              if (!paramFile1.renameTo(paramFile2))
              {
                if (paramFile2.getCanonicalPath().startsWith(paramFile1.getCanonicalPath())) {
                  break label112;
                }
                copyDirectory(paramFile1, paramFile2);
                deleteDirectory(paramFile1);
                if (paramFile1.exists()) {}
              }
              else
              {
                return;
              }
              throw new IOException("Failed to delete original directory '" + paramFile1 + "' after copy to '" + paramFile2 + "'");
              label112:
              throw new IOException("Cannot move directory: " + paramFile1 + " to a subdirectory of itself: " + paramFile2);
            }
            throw new FileExistsException("Destination '" + paramFile2 + "' already exists");
          }
          throw new IOException("Source '" + paramFile1 + "' is not a directory");
        }
        throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
      }
      throw new NullPointerException("Destination must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void moveDirectoryToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if ((!paramFile2.exists()) && (paramBoolean)) {
          paramFile2.mkdirs();
        }
        if (paramFile2.exists())
        {
          if (paramFile2.isDirectory())
          {
            moveDirectory(paramFile1, new File(paramFile2, paramFile1.getName()));
            return;
          }
          throw new IOException("Destination '" + paramFile2 + "' is not a directory");
        }
        throw new FileNotFoundException("Destination directory '" + paramFile2 + "' does not exist [createDestDir=" + paramBoolean + "]");
      }
      throw new NullPointerException("Destination directory must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void moveFile(File paramFile1, File paramFile2)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if (paramFile1.exists())
        {
          if (!paramFile1.isDirectory())
          {
            if (!paramFile2.exists())
            {
              if (!paramFile2.isDirectory())
              {
                if (!paramFile1.renameTo(paramFile2))
                {
                  copyFile(paramFile1, paramFile2);
                  if (!paramFile1.delete()) {}
                }
                else
                {
                  return;
                }
                deleteQuietly(paramFile2);
                throw new IOException("Failed to delete original file '" + paramFile1 + "' after copy to '" + paramFile2 + "'");
              }
              throw new IOException("Destination '" + paramFile2 + "' is a directory");
            }
            throw new FileExistsException("Destination '" + paramFile2 + "' already exists");
          }
          throw new IOException("Source '" + paramFile1 + "' is a directory");
        }
        throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
      }
      throw new NullPointerException("Destination must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void moveFileToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if ((!paramFile2.exists()) && (paramBoolean)) {
          paramFile2.mkdirs();
        }
        if (paramFile2.exists())
        {
          if (paramFile2.isDirectory())
          {
            moveFile(paramFile1, new File(paramFile2, paramFile1.getName()));
            return;
          }
          throw new IOException("Destination '" + paramFile2 + "' is not a directory");
        }
        throw new FileNotFoundException("Destination directory '" + paramFile2 + "' does not exist [createDestDir=" + paramBoolean + "]");
      }
      throw new NullPointerException("Destination directory must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static void moveToDirectory(File paramFile1, File paramFile2, boolean paramBoolean)
    throws IOException
  {
    if (paramFile1 != null)
    {
      if (paramFile2 != null)
      {
        if (paramFile1.exists())
        {
          if (!paramFile1.isDirectory()) {
            moveFileToDirectory(paramFile1, paramFile2, paramBoolean);
          } else {
            moveDirectoryToDirectory(paramFile1, paramFile2, paramBoolean);
          }
          return;
        }
        throw new FileNotFoundException("Source '" + paramFile1 + "' does not exist");
      }
      throw new NullPointerException("Destination must not be null");
    }
    throw new NullPointerException("Source must not be null");
  }
  
  public static FileInputStream openInputStream(File paramFile)
    throws IOException
  {
    if (!paramFile.exists()) {
      throw new FileNotFoundException("File '" + paramFile + "' does not exist");
    }
    if (!paramFile.isDirectory())
    {
      if (paramFile.canRead()) {
        return new FileInputStream(paramFile);
      }
      throw new IOException("File '" + paramFile + "' cannot be read");
    }
    throw new IOException("File '" + paramFile + "' exists but is a directory");
  }
  
  public static FileOutputStream openOutputStream(File paramFile)
    throws IOException
  {
    return openOutputStream(paramFile, false);
  }
  
  public static FileOutputStream openOutputStream(File paramFile, boolean paramBoolean)
    throws IOException
  {
    if (!paramFile.exists())
    {
      File localFile = paramFile.getParentFile();
      if ((localFile != null) && (!localFile.mkdirs()) && (!localFile.isDirectory())) {
        throw new IOException("Directory '" + localFile + "' could not be created");
      }
    }
    else
    {
      if (paramFile.isDirectory()) {
        break label122;
      }
      if (!paramFile.canWrite()) {
        break label88;
      }
    }
    return new FileOutputStream(paramFile, paramBoolean);
    label88:
    throw new IOException("File '" + paramFile + "' cannot be written to");
    label122:
    throw new IOException("File '" + paramFile + "' exists but is a directory");
  }
  
  public static byte[] readFileToByteArray(File paramFile)
    throws IOException
  {
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = openInputStream(paramFile);
      byte[] arrayOfByte = IOUtils.toByteArray(localFileInputStream, paramFile.length());
      return arrayOfByte;
    }
    finally
    {
      IOUtils.closeQuietly(localFileInputStream);
    }
  }
  
  public static String readFileToString(File paramFile)
    throws IOException
  {
    return readFileToString(paramFile, Charset.defaultCharset());
  }
  
  public static String readFileToString(File paramFile, String paramString)
    throws IOException
  {
    return readFileToString(paramFile, Charsets.toCharset(paramString));
  }
  
  public static String readFileToString(File paramFile, Charset paramCharset)
    throws IOException
  {
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = openInputStream(paramFile);
      String str = IOUtils.toString(localFileInputStream, Charsets.toCharset(paramCharset));
      return str;
    }
    finally
    {
      IOUtils.closeQuietly(localFileInputStream);
    }
  }
  
  public static List<String> readLines(File paramFile)
    throws IOException
  {
    return readLines(paramFile, Charset.defaultCharset());
  }
  
  public static List<String> readLines(File paramFile, String paramString)
    throws IOException
  {
    return readLines(paramFile, Charsets.toCharset(paramString));
  }
  
  public static List<String> readLines(File paramFile, Charset paramCharset)
    throws IOException
  {
    FileInputStream localFileInputStream = null;
    try
    {
      localFileInputStream = openInputStream(paramFile);
      List localList = IOUtils.readLines(localFileInputStream, Charsets.toCharset(paramCharset));
      return localList;
    }
    finally
    {
      IOUtils.closeQuietly(localFileInputStream);
    }
  }
  
  private static IOFileFilter setUpEffectiveDirFilter(IOFileFilter paramIOFileFilter)
  {
    Object localObject;
    if (paramIOFileFilter != null)
    {
      localObject = new IOFileFilter[2];
      localObject[0] = paramIOFileFilter;
      localObject[1] = DirectoryFileFilter.INSTANCE;
      localObject = FileFilterUtils.and((IOFileFilter[])localObject);
    }
    else
    {
      localObject = FalseFileFilter.INSTANCE;
    }
    return localObject;
  }
  
  private static IOFileFilter setUpEffectiveFileFilter(IOFileFilter paramIOFileFilter)
  {
    IOFileFilter[] arrayOfIOFileFilter = new IOFileFilter[2];
    arrayOfIOFileFilter[0] = paramIOFileFilter;
    arrayOfIOFileFilter[1] = FileFilterUtils.notFileFilter(DirectoryFileFilter.INSTANCE);
    return FileFilterUtils.and(arrayOfIOFileFilter);
  }
  
  public static long sizeOf(File paramFile)
  {
    if (paramFile.exists())
    {
      long l;
      if (!paramFile.isDirectory()) {
        l = paramFile.length();
      } else {
        l = sizeOfDirectory(paramFile);
      }
      return l;
    }
    throw new IllegalArgumentException(paramFile + " does not exist");
  }
  
  public static BigInteger sizeOfAsBigInteger(File paramFile)
  {
    if (paramFile.exists())
    {
      BigInteger localBigInteger;
      if (!paramFile.isDirectory()) {
        localBigInteger = BigInteger.valueOf(paramFile.length());
      } else {
        localBigInteger = sizeOfDirectoryAsBigInteger(paramFile);
      }
      return localBigInteger;
    }
    throw new IllegalArgumentException(paramFile + " does not exist");
  }
  
  public static long sizeOfDirectory(File paramFile)
  {
    checkDirectory(paramFile);
    File[] arrayOfFile = paramFile.listFiles();
    long l1;
    if (arrayOfFile == null) {
      l1 = 0L;
    }
    label73:
    label77:
    for (;;)
    {
      return l1;
      l1 = 0L;
      int i = arrayOfFile.length;
      int j = 0;
      for (;;)
      {
        for (;;)
        {
          if (j >= i) {
            break label77;
          }
          File localFile = arrayOfFile[j];
          try
          {
            if (!isSymlink(localFile))
            {
              long l2 = sizeOf(localFile);
              l1 += l2;
              if (l1 < 0L) {
                break;
              }
            }
            j++;
          }
          catch (IOException localIOException)
          {
            break label73;
          }
        }
      }
    }
  }
  
  public static BigInteger sizeOfDirectoryAsBigInteger(File paramFile)
  {
    checkDirectory(paramFile);
    File[] arrayOfFile = paramFile.listFiles();
    BigInteger localBigInteger;
    if (arrayOfFile == null) {
      localBigInteger = BigInteger.ZERO;
    }
    for (;;)
    {
      return localBigInteger;
      localBigInteger = BigInteger.ZERO;
      int i = arrayOfFile.length;
      int j = 0;
      while (j < i)
      {
        File localFile = arrayOfFile[j];
        try
        {
          if (!isSymlink(localFile))
          {
            localBigInteger = localBigInteger.add(BigInteger.valueOf(sizeOf(localFile)));
            localBigInteger = localBigInteger;
          }
          label68:
          j++;
        }
        catch (IOException localIOException)
        {
          break label68;
        }
      }
    }
  }
  
  public static File toFile(URL paramURL)
  {
    File localFile;
    if ((paramURL != null) && ("file".equalsIgnoreCase(paramURL.getProtocol()))) {
      localFile = new File(decodeUrl(paramURL.getFile().replace('/', File.separatorChar)));
    } else {
      localFile = null;
    }
    return localFile;
  }
  
  public static File[] toFiles(URL[] paramArrayOfURL)
  {
    File[] arrayOfFile;
    int i;
    if ((paramArrayOfURL != null) && (paramArrayOfURL.length != 0))
    {
      arrayOfFile = new File[paramArrayOfURL.length];
      i = 0;
    }
    while (i < paramArrayOfURL.length)
    {
      URL localURL = paramArrayOfURL[i];
      if (localURL != null)
      {
        if (localURL.getProtocol().equals("file")) {
          arrayOfFile[i] = toFile(localURL);
        }
      }
      else
      {
        i++;
        continue;
      }
      throw new IllegalArgumentException("URL could not be converted to a File: " + localURL);
      arrayOfFile = EMPTY_FILE_ARRAY;
    }
    return arrayOfFile;
  }
  
  private static String[] toSuffixes(String[] paramArrayOfString)
  {
    String[] arrayOfString = new String[paramArrayOfString.length];
    for (int i = 0;; i++)
    {
      if (i >= paramArrayOfString.length) {
        return arrayOfString;
      }
      arrayOfString[i] = ("." + paramArrayOfString[i]);
    }
  }
  
  public static URL[] toURLs(File[] paramArrayOfFile)
    throws IOException
  {
    URL[] arrayOfURL = new URL[paramArrayOfFile.length];
    for (int i = 0;; i++)
    {
      if (i >= arrayOfURL.length) {
        return arrayOfURL;
      }
      arrayOfURL[i] = paramArrayOfFile[i].toURI().toURL();
    }
  }
  
  public static void touch(File paramFile)
    throws IOException
  {
    if (!paramFile.exists()) {
      IOUtils.closeQuietly(openOutputStream(paramFile));
    }
    if (paramFile.setLastModified(System.currentTimeMillis())) {
      return;
    }
    throw new IOException("Unable to set the last modification time for " + paramFile);
  }
  
  private static void validateListFilesParameters(File paramFile, IOFileFilter paramIOFileFilter)
  {
    if (paramFile.isDirectory())
    {
      if (paramIOFileFilter != null) {
        return;
      }
      throw new NullPointerException("Parameter 'fileFilter' is null");
    }
    throw new IllegalArgumentException("Parameter 'directory' is not a directory");
  }
  
  public static boolean waitFor(File paramFile, int paramInt)
  {
    int i = 0;
    int j = 0;
    int k;
    if (!paramFile.exists())
    {
      k = j + 1;
      if (j < 10) {
        break label60;
      }
      j = 0;
      k = i + 1;
      if (i > paramInt)
      {
        i = 0;
        label36:
        return i;
      }
      i = k;
    }
    for (;;)
    {
      try
      {
        Thread.sleep(100L);
      }
      catch (InterruptedException localInterruptedException) {}catch (Exception localException) {}
      i = 1;
      break label36;
      label60:
      j = k;
    }
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence)
    throws IOException
  {
    write(paramFile, paramCharSequence, Charset.defaultCharset(), false);
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence, String paramString)
    throws IOException
  {
    write(paramFile, paramCharSequence, paramString, false);
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence, String paramString, boolean paramBoolean)
    throws IOException
  {
    write(paramFile, paramCharSequence, Charsets.toCharset(paramString), paramBoolean);
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence, Charset paramCharset)
    throws IOException
  {
    write(paramFile, paramCharSequence, paramCharset, false);
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    String str;
    if (paramCharSequence != null) {
      str = paramCharSequence.toString();
    } else {
      str = null;
    }
    writeStringToFile(paramFile, str, paramCharset, paramBoolean);
  }
  
  public static void write(File paramFile, CharSequence paramCharSequence, boolean paramBoolean)
    throws IOException
  {
    write(paramFile, paramCharSequence, Charset.defaultCharset(), paramBoolean);
  }
  
  public static void writeByteArrayToFile(File paramFile, byte[] paramArrayOfByte)
    throws IOException
  {
    writeByteArrayToFile(paramFile, paramArrayOfByte, false);
  }
  
  public static void writeByteArrayToFile(File paramFile, byte[] paramArrayOfByte, boolean paramBoolean)
    throws IOException
  {
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = openOutputStream(paramFile, paramBoolean);
      localFileOutputStream.write(paramArrayOfByte);
      localFileOutputStream.close();
      return;
    }
    finally
    {
      IOUtils.closeQuietly(localFileOutputStream);
    }
  }
  
  public static void writeLines(File paramFile, String paramString, Collection<?> paramCollection)
    throws IOException
  {
    writeLines(paramFile, paramString, paramCollection, null, false);
  }
  
  public static void writeLines(File paramFile, String paramString1, Collection<?> paramCollection, String paramString2)
    throws IOException
  {
    writeLines(paramFile, paramString1, paramCollection, paramString2, false);
  }
  
  public static void writeLines(File paramFile, String paramString1, Collection<?> paramCollection, String paramString2, boolean paramBoolean)
    throws IOException
  {
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = openOutputStream(paramFile, paramBoolean);
      BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
      IOUtils.writeLines(paramCollection, paramString2, localBufferedOutputStream, paramString1);
      localBufferedOutputStream.flush();
      localFileOutputStream.close();
      return;
    }
    finally
    {
      IOUtils.closeQuietly(localFileOutputStream);
    }
  }
  
  public static void writeLines(File paramFile, String paramString, Collection<?> paramCollection, boolean paramBoolean)
    throws IOException
  {
    writeLines(paramFile, paramString, paramCollection, null, paramBoolean);
  }
  
  public static void writeLines(File paramFile, Collection<?> paramCollection)
    throws IOException
  {
    writeLines(paramFile, null, paramCollection, null, false);
  }
  
  public static void writeLines(File paramFile, Collection<?> paramCollection, String paramString)
    throws IOException
  {
    writeLines(paramFile, null, paramCollection, paramString, false);
  }
  
  public static void writeLines(File paramFile, Collection<?> paramCollection, String paramString, boolean paramBoolean)
    throws IOException
  {
    writeLines(paramFile, null, paramCollection, paramString, paramBoolean);
  }
  
  public static void writeLines(File paramFile, Collection<?> paramCollection, boolean paramBoolean)
    throws IOException
  {
    writeLines(paramFile, null, paramCollection, null, paramBoolean);
  }
  
  public static void writeStringToFile(File paramFile, String paramString)
    throws IOException
  {
    writeStringToFile(paramFile, paramString, Charset.defaultCharset(), false);
  }
  
  public static void writeStringToFile(File paramFile, String paramString1, String paramString2)
    throws IOException
  {
    writeStringToFile(paramFile, paramString1, paramString2, false);
  }
  
  public static void writeStringToFile(File paramFile, String paramString1, String paramString2, boolean paramBoolean)
    throws IOException
  {
    writeStringToFile(paramFile, paramString1, Charsets.toCharset(paramString2), paramBoolean);
  }
  
  public static void writeStringToFile(File paramFile, String paramString, Charset paramCharset)
    throws IOException
  {
    writeStringToFile(paramFile, paramString, paramCharset, false);
  }
  
  public static void writeStringToFile(File paramFile, String paramString, Charset paramCharset, boolean paramBoolean)
    throws IOException
  {
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = openOutputStream(paramFile, paramBoolean);
      IOUtils.write(paramString, localFileOutputStream, paramCharset);
      localFileOutputStream.close();
      return;
    }
    finally
    {
      IOUtils.closeQuietly(localFileOutputStream);
    }
  }
  
  public static void writeStringToFile(File paramFile, String paramString, boolean paramBoolean)
    throws IOException
  {
    writeStringToFile(paramFile, paramString, Charset.defaultCharset(), paramBoolean);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.FileUtils
 * JD-Core Version:    0.7.0.1
 */