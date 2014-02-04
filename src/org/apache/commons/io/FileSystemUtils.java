package org.apache.commons.io;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class FileSystemUtils
{
  private static final String DF;
  private static final int INIT_PROBLEM = -1;
  private static final FileSystemUtils INSTANCE = new FileSystemUtils();
  private static final int OS = 0;
  private static final int OTHER = 0;
  private static final int POSIX_UNIX = 3;
  private static final int UNIX = 2;
  private static final int WINDOWS = 1;
  
  static
  {
    String str1 = "df";
    int i;
    try
    {
      String str2 = System.getProperty("os.name");
      if (str2 == null) {
        throw new IOException("os.name not found");
      }
    }
    catch (Exception localException)
    {
      i = -1;
    }
    for (;;)
    {
      OS = i;
      DF = str1;
      return;
      String str3 = i.toLowerCase(Locale.ENGLISH);
      int j;
      if (str3.indexOf("windows") != -1)
      {
        j = 1;
      }
      else
      {
        if ((j.indexOf("linux") == -1) && (j.indexOf("mpe/ix") == -1) && (j.indexOf("freebsd") == -1) && (j.indexOf("irix") == -1) && (j.indexOf("digital unix") == -1) && (j.indexOf("unix") == -1) && (j.indexOf("mac os x") == -1))
        {
          if ((j.indexOf("sun os") == -1) && (j.indexOf("sunos") == -1) && (j.indexOf("solaris") == -1))
          {
            if (j.indexOf("hp-ux") == -1)
            {
              j = j.indexOf("aix");
              if (j == -1) {}
            }
            else
            {
              j = 3;
              continue;
            }
            j = 0;
          }
        }
        else
        {
          j = 2;
          continue;
        }
        j = 3;
        str1 = "/usr/xpg4/bin/df";
      }
    }
  }
  
  @Deprecated
  public static long freeSpace(String paramString)
    throws IOException
  {
    return INSTANCE.freeSpaceOS(paramString, OS, false, -1L);
  }
  
  public static long freeSpaceKb()
    throws IOException
  {
    return freeSpaceKb(-1L);
  }
  
  public static long freeSpaceKb(long paramLong)
    throws IOException
  {
    return freeSpaceKb(new File(".").getAbsolutePath(), paramLong);
  }
  
  public static long freeSpaceKb(String paramString)
    throws IOException
  {
    return freeSpaceKb(paramString, -1L);
  }
  
  public static long freeSpaceKb(String paramString, long paramLong)
    throws IOException
  {
    return INSTANCE.freeSpaceOS(paramString, OS, true, paramLong);
  }
  
  long freeSpaceOS(String paramString, int paramInt, boolean paramBoolean, long paramLong)
    throws IOException
  {
    if (paramString != null)
    {
      long l;
      switch (paramInt)
      {
      default: 
        throw new IllegalStateException("Exception caught when determining operating system");
      case 0: 
        throw new IllegalStateException("Unsupported operating system");
      case 1: 
        if (!paramBoolean) {
          l = freeSpaceWindows(paramString, paramLong);
        } else {
          l = freeSpaceWindows(paramString, paramLong) / 1024L;
        }
        break;
      case 2: 
        l = freeSpaceUnix(paramString, paramBoolean, false, paramLong);
        break;
      case 3: 
        l = freeSpaceUnix(paramString, paramBoolean, true, paramLong);
      }
      return l;
    }
    throw new IllegalArgumentException("Path must not be empty");
  }
  
  long freeSpaceUnix(String paramString, boolean paramBoolean1, boolean paramBoolean2, long paramLong)
    throws IOException
  {
    if (paramString.length() != 0)
    {
      Object localObject2 = "-";
      if (paramBoolean1) {
        localObject2 = (String)localObject2 + "k";
      }
      if (paramBoolean2) {
        localObject2 = (String)localObject2 + "P";
      }
      if (((String)localObject2).length() <= 1)
      {
        localObject1 = new String[2];
        localObject1[0] = DF;
        localObject1[1] = paramString;
      }
      else
      {
        localObject1 = new String[3];
        localObject1[0] = DF;
        localObject1[1] = localObject2;
        localObject1[2] = paramString;
      }
      Object localObject1 = performCommand((String[])localObject1, 3, paramLong);
      if (((List)localObject1).size() >= 2)
      {
        localObject2 = new StringTokenizer((String)((List)localObject1).get(1), " ");
        if (((StringTokenizer)localObject2).countTokens() >= 4)
        {
          ((StringTokenizer)localObject2).nextToken();
        }
        else
        {
          if ((((StringTokenizer)localObject2).countTokens() != 1) || (((List)localObject1).size() < 3)) {
            throw new IOException("Command line '" + DF + "' did not return data as expected " + "for path '" + paramString + "'- check path is valid");
          }
          localObject2 = new StringTokenizer((String)((List)localObject1).get(2), " ");
        }
        ((StringTokenizer)localObject2).nextToken();
        ((StringTokenizer)localObject2).nextToken();
        return parseBytes(((StringTokenizer)localObject2).nextToken(), paramString);
      }
      throw new IOException("Command line '" + DF + "' did not return info as expected " + "for path '" + paramString + "'- response was " + localObject1);
    }
    throw new IllegalArgumentException("Path must not be empty");
  }
  
  long freeSpaceWindows(String paramString, long paramLong)
    throws IOException
  {
    String str = FilenameUtils.normalize(paramString, false);
    if ((str.length() > 0) && (str.charAt(0) != '"')) {
      str = "\"" + str + "\"";
    }
    Object localObject = new String[3];
    localObject[0] = "cmd.exe";
    localObject[1] = "/C";
    localObject[2] = ("dir /a /-c " + str);
    List localList = performCommand((String[])localObject, 2147483647, paramLong);
    for (int i = -1 + localList.size();; i--)
    {
      if (i < 0) {
        throw new IOException("Command line 'dir /-c' did not return any info for path '" + str + "'");
      }
      localObject = (String)localList.get(i);
      if (((String)localObject).length() > 0) {
        break;
      }
    }
    return parseDir((String)localObject, str);
  }
  
  Process openProcess(String[] paramArrayOfString)
    throws IOException
  {
    return Runtime.getRuntime().exec(paramArrayOfString);
  }
  
  long parseBytes(String paramString1, String paramString2)
    throws IOException
  {
    try
    {
      long l = Long.parseLong(paramString1);
      if (l < 0L) {
        throw new IOException("Command line '" + DF + "' did not find free space in response " + "for path '" + paramString2 + "'- check path is valid");
      }
    }
    catch (NumberFormatException localNumberFormatException)
    {
      throw new IOExceptionWithCause("Command line '" + DF + "' did not return numeric data as expected " + "for path '" + paramString2 + "'- check path is valid", localNumberFormatException);
    }
    return localNumberFormatException;
  }
  
  long parseDir(String paramString1, String paramString2)
    throws IOException
  {
    int i = 0;
    int j = 0;
    int m = -1 + paramString1.length();
    while (m >= 0) {
      if (!Character.isDigit(paramString1.charAt(m))) {
        m--;
      } else {
        j = m + 1;
      }
    }
    int k;
    while (m >= 0)
    {
      k = paramString1.charAt(m);
      if ((Character.isDigit(k)) || (k == 44) || (k == 46)) {
        m--;
      } else {
        i = m + 1;
      }
    }
    if (m >= 0)
    {
      StringBuilder localStringBuilder = new StringBuilder(paramString1.substring(i, j));
      for (k = 0;; k++)
      {
        if (k >= localStringBuilder.length()) {
          return parseBytes(localStringBuilder.toString(), paramString2);
        }
        if ((localStringBuilder.charAt(k) == ',') || (localStringBuilder.charAt(k) == '.'))
        {
          j = k - 1;
          localStringBuilder.deleteCharAt(k);
          k = j;
        }
      }
    }
    throw new IOException("Command line 'dir /-c' did not return valid info for path '" + paramString2 + "'");
  }
  
  /* Error */
  List<String> performCommand(String[] paramArrayOfString, int paramInt, long paramLong)
    throws IOException
  {
    // Byte code:
    //   0: new 285	java/util/ArrayList
    //   3: dup
    //   4: bipush 20
    //   6: invokespecial 288	java/util/ArrayList:<init>	(I)V
    //   9: astore 11
    //   11: aconst_null
    //   12: astore 8
    //   14: aconst_null
    //   15: astore 5
    //   17: aconst_null
    //   18: astore 7
    //   20: aconst_null
    //   21: astore 6
    //   23: aconst_null
    //   24: astore 9
    //   26: lload_3
    //   27: invokestatic 294	org/apache/commons/io/ThreadMonitor:start	(J)Ljava/lang/Thread;
    //   30: astore 10
    //   32: aload_0
    //   33: aload_1
    //   34: invokevirtual 296	org/apache/commons/io/FileSystemUtils:openProcess	([Ljava/lang/String;)Ljava/lang/Process;
    //   37: astore 8
    //   39: aload 8
    //   41: invokevirtual 302	java/lang/Process:getInputStream	()Ljava/io/InputStream;
    //   44: astore 5
    //   46: aload 8
    //   48: invokevirtual 306	java/lang/Process:getOutputStream	()Ljava/io/OutputStream;
    //   51: astore 7
    //   53: aload 8
    //   55: invokevirtual 309	java/lang/Process:getErrorStream	()Ljava/io/InputStream;
    //   58: astore 6
    //   60: new 311	java/io/BufferedReader
    //   63: dup
    //   64: new 313	java/io/InputStreamReader
    //   67: dup
    //   68: aload 5
    //   70: invokespecial 316	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   73: invokespecial 319	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   76: astore 9
    //   78: aload 9
    //   80: invokevirtual 322	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   83: astore 12
    //   85: aload 12
    //   87: ifnull +43 -> 130
    //   90: aload 11
    //   92: invokeinterface 171 1 0
    //   97: iload_2
    //   98: if_icmpge +32 -> 130
    //   101: aload 11
    //   103: aload 12
    //   105: getstatic 56	java/util/Locale:ENGLISH	Ljava/util/Locale;
    //   108: invokevirtual 62	java/lang/String:toLowerCase	(Ljava/util/Locale;)Ljava/lang/String;
    //   111: invokevirtual 325	java/lang/String:trim	()Ljava/lang/String;
    //   114: invokeinterface 329 2 0
    //   119: pop
    //   120: aload 9
    //   122: invokevirtual 322	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   125: astore 12
    //   127: goto -42 -> 85
    //   130: aload 8
    //   132: invokevirtual 332	java/lang/Process:waitFor	()I
    //   135: pop
    //   136: aload 10
    //   138: invokestatic 336	org/apache/commons/io/ThreadMonitor:stop	(Ljava/lang/Thread;)V
    //   141: aload 8
    //   143: invokevirtual 339	java/lang/Process:exitValue	()I
    //   146: ifeq +132 -> 278
    //   149: new 41	java/io/IOException
    //   152: dup
    //   153: new 150	java/lang/StringBuilder
    //   156: dup
    //   157: invokespecial 151	java/lang/StringBuilder:<init>	()V
    //   160: ldc_w 341
    //   163: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   166: aload 8
    //   168: invokevirtual 339	java/lang/Process:exitValue	()I
    //   171: invokevirtual 343	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
    //   174: ldc_w 345
    //   177: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   180: aload_1
    //   181: invokestatic 351	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   184: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   187: invokevirtual 160	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   190: invokespecial 46	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   193: athrow
    //   194: astore 10
    //   196: aload 9
    //   198: astore 9
    //   200: new 257	org/apache/commons/io/IOExceptionWithCause
    //   203: dup
    //   204: new 150	java/lang/StringBuilder
    //   207: dup
    //   208: invokespecial 151	java/lang/StringBuilder:<init>	()V
    //   211: ldc_w 353
    //   214: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   217: aload_1
    //   218: invokestatic 351	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   221: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   224: ldc_w 355
    //   227: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   230: lload_3
    //   231: invokevirtual 358	java/lang/StringBuilder:append	(J)Ljava/lang/StringBuilder;
    //   234: invokevirtual 160	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   237: aload 10
    //   239: invokespecial 262	org/apache/commons/io/IOExceptionWithCause:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   242: athrow
    //   243: astore 10
    //   245: aload 5
    //   247: invokestatic 363	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   250: aload 7
    //   252: invokestatic 366	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   255: aload 6
    //   257: invokestatic 363	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   260: aload 9
    //   262: invokestatic 368	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   265: aload 8
    //   267: ifnull +8 -> 275
    //   270: aload 8
    //   272: invokevirtual 371	java/lang/Process:destroy	()V
    //   275: aload 10
    //   277: athrow
    //   278: aload 11
    //   280: invokeinterface 375 1 0
    //   285: ifeq +43 -> 328
    //   288: new 41	java/io/IOException
    //   291: dup
    //   292: new 150	java/lang/StringBuilder
    //   295: dup
    //   296: invokespecial 151	java/lang/StringBuilder:<init>	()V
    //   299: ldc_w 377
    //   302: invokevirtual 155	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   305: aload_1
    //   306: invokestatic 351	java/util/Arrays:asList	([Ljava/lang/Object;)Ljava/util/List;
    //   309: invokevirtual 207	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
    //   312: invokevirtual 160	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   315: invokespecial 46	java/io/IOException:<init>	(Ljava/lang/String;)V
    //   318: athrow
    //   319: astore 10
    //   321: aload 9
    //   323: astore 9
    //   325: goto -80 -> 245
    //   328: aload 5
    //   330: invokestatic 363	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   333: aload 7
    //   335: invokestatic 366	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/OutputStream;)V
    //   338: aload 6
    //   340: invokestatic 363	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/InputStream;)V
    //   343: aload 9
    //   345: invokestatic 368	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Reader;)V
    //   348: aload 8
    //   350: ifnull +8 -> 358
    //   353: aload 8
    //   355: invokevirtual 371	java/lang/Process:destroy	()V
    //   358: aload 11
    //   360: areturn
    //   361: astore 10
    //   363: goto -163 -> 200
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	366	0	this	FileSystemUtils
    //   0	366	1	paramArrayOfString	String[]
    //   0	366	2	paramInt	int
    //   0	366	3	paramLong	long
    //   15	314	5	localInputStream1	java.io.InputStream
    //   21	318	6	localInputStream2	java.io.InputStream
    //   18	316	7	localOutputStream	java.io.OutputStream
    //   12	342	8	localProcess	Process
    //   24	320	9	localBufferedReader	java.io.BufferedReader
    //   30	107	10	localThread	java.lang.Thread
    //   194	44	10	localInterruptedException1	java.lang.InterruptedException
    //   243	33	10	localObject1	Object
    //   319	1	10	localObject2	Object
    //   361	1	10	localInterruptedException2	java.lang.InterruptedException
    //   9	350	11	localArrayList	java.util.ArrayList
    //   83	43	12	str	String
    // Exception table:
    //   from	to	target	type
    //   78	194	194	java/lang/InterruptedException
    //   278	319	194	java/lang/InterruptedException
    //   26	78	243	finally
    //   200	243	243	finally
    //   78	194	319	finally
    //   278	319	319	finally
    //   26	78	361	java/lang/InterruptedException
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.FileSystemUtils
 * JD-Core Version:    0.7.0.1
 */