package org.apache.commons.io.input;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class Tailer
  implements Runnable
{
  private static final int DEFAULT_BUFSIZE = 4096;
  private static final int DEFAULT_DELAY_MILLIS = 1000;
  private static final String RAF_MODE = "r";
  private final long delayMillis;
  private final boolean end;
  private final File file;
  private final byte[] inbuf;
  private final TailerListener listener;
  private final boolean reOpen;
  private volatile boolean run = true;
  
  public Tailer(File paramFile, TailerListener paramTailerListener)
  {
    this(paramFile, paramTailerListener, 1000L);
  }
  
  public Tailer(File paramFile, TailerListener paramTailerListener, long paramLong)
  {
    this(paramFile, paramTailerListener, paramLong, false);
  }
  
  public Tailer(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean)
  {
    this(paramFile, paramTailerListener, paramLong, paramBoolean, 4096);
  }
  
  public Tailer(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean, int paramInt)
  {
    this(paramFile, paramTailerListener, paramLong, paramBoolean, false, paramInt);
  }
  
  public Tailer(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    this(paramFile, paramTailerListener, paramLong, paramBoolean1, paramBoolean2, 4096);
  }
  
  public Tailer(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    this.file = paramFile;
    this.delayMillis = paramLong;
    this.end = paramBoolean1;
    this.inbuf = new byte[paramInt];
    this.listener = paramTailerListener;
    paramTailerListener.init(this);
    this.reOpen = paramBoolean2;
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener)
  {
    return create(paramFile, paramTailerListener, 1000L, false);
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener, long paramLong)
  {
    return create(paramFile, paramTailerListener, paramLong, false);
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean)
  {
    return create(paramFile, paramTailerListener, paramLong, paramBoolean, 4096);
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean, int paramInt)
  {
    Tailer localTailer = new Tailer(paramFile, paramTailerListener, paramLong, paramBoolean, paramInt);
    Thread localThread = new Thread(localTailer);
    localThread.setDaemon(true);
    localThread.start();
    return localTailer;
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean1, boolean paramBoolean2)
  {
    return create(paramFile, paramTailerListener, paramLong, paramBoolean1, paramBoolean2, 4096);
  }
  
  public static Tailer create(File paramFile, TailerListener paramTailerListener, long paramLong, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
  {
    Tailer localTailer = new Tailer(paramFile, paramTailerListener, paramLong, paramBoolean1, paramBoolean2, paramInt);
    Thread localThread = new Thread(localTailer);
    localThread.setDaemon(true);
    localThread.start();
    return localTailer;
  }
  
  private long readLines(RandomAccessFile paramRandomAccessFile)
    throws IOException
  {
    StringBuilder localStringBuilder = new StringBuilder();
    long l2 = paramRandomAccessFile.getFilePointer();
    long l1 = l2;
    int m = 0;
    int k;
    if (this.run)
    {
      k = paramRandomAccessFile.read(this.inbuf);
      if (k != -1) {}
    }
    else
    {
      paramRandomAccessFile.seek(l1);
      return l1;
    }
    for (int i = 0;; i++)
    {
      if (i >= k)
      {
        l2 = paramRandomAccessFile.getFilePointer();
        break;
      }
      int j = this.inbuf[i];
      switch (j)
      {
      case 11: 
      case 12: 
      default: 
        if (m != 0)
        {
          m = 0;
          this.listener.handle(localStringBuilder.toString());
          localStringBuilder.setLength(0);
          l1 = 1L + (l2 + i);
        }
        localStringBuilder.append((char)j);
        break;
      case 10: 
        m = 0;
        this.listener.handle(localStringBuilder.toString());
        localStringBuilder.setLength(0);
        l1 = 1L + (l2 + i);
        break;
      case 13: 
        if (m != 0) {
          localStringBuilder.append('\r');
        }
        m = 1;
      }
    }
  }
  
  public long getDelay()
  {
    return this.delayMillis;
  }
  
  public File getFile()
  {
    return this.file;
  }
  
  /* Error */
  public void run()
  {
    // Byte code:
    //   0: ldc2_w 141
    //   3: lstore 4
    //   5: ldc2_w 141
    //   8: lstore_2
    //   9: aconst_null
    //   10: astore_1
    //   11: aload_0
    //   12: getfield 49	org/apache/commons/io/input/Tailer:run	Z
    //   15: istore 6
    //   17: iload 6
    //   19: ifeq +95 -> 114
    //   22: aload_1
    //   23: ifnonnull +91 -> 114
    //   26: new 101	java/io/RandomAccessFile
    //   29: dup
    //   30: aload_0
    //   31: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   34: ldc 15
    //   36: invokespecial 145	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   39: astore_1
    //   40: aload_1
    //   41: ifnonnull +36 -> 77
    //   44: aload_0
    //   45: getfield 53	org/apache/commons/io/input/Tailer:delayMillis	J
    //   48: invokestatic 148	java/lang/Thread:sleep	(J)V
    //   51: aload_1
    //   52: astore_1
    //   53: goto -42 -> 11
    //   56: pop
    //   57: aload_0
    //   58: getfield 59	org/apache/commons/io/input/Tailer:listener	Lorg/apache/commons/io/input/TailerListener;
    //   61: invokeinterface 151 1 0
    //   66: aload_1
    //   67: astore_1
    //   68: goto -28 -> 40
    //   71: pop
    //   72: aload_1
    //   73: astore_1
    //   74: goto -63 -> 11
    //   77: aload_0
    //   78: getfield 55	org/apache/commons/io/input/Tailer:end	Z
    //   81: ifeq +26 -> 107
    //   84: aload_0
    //   85: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   88: invokevirtual 156	java/io/File:length	()J
    //   91: lstore_2
    //   92: invokestatic 161	java/lang/System:currentTimeMillis	()J
    //   95: lstore 4
    //   97: aload_1
    //   98: lload_2
    //   99: invokevirtual 113	java/io/RandomAccessFile:seek	(J)V
    //   102: aload_1
    //   103: astore_1
    //   104: goto -93 -> 11
    //   107: ldc2_w 141
    //   110: lstore_2
    //   111: goto -19 -> 92
    //   114: aload_0
    //   115: getfield 49	org/apache/commons/io/input/Tailer:run	Z
    //   118: ifeq +191 -> 309
    //   121: aload_0
    //   122: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   125: lload 4
    //   127: invokestatic 167	org/apache/commons/io/FileUtils:isFileNewer	(Ljava/io/File;J)Z
    //   130: istore 8
    //   132: aload_0
    //   133: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   136: invokevirtual 156	java/io/File:length	()J
    //   139: lstore 6
    //   141: lload 6
    //   143: lload_2
    //   144: lcmp
    //   145: ifge +60 -> 205
    //   148: aload_0
    //   149: getfield 59	org/apache/commons/io/input/Tailer:listener	Lorg/apache/commons/io/input/TailerListener;
    //   152: invokeinterface 170 1 0
    //   157: aload_1
    //   158: astore 6
    //   160: new 101	java/io/RandomAccessFile
    //   163: dup
    //   164: aload_0
    //   165: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   168: ldc 15
    //   170: invokespecial 145	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   173: astore_1
    //   174: ldc2_w 141
    //   177: lstore_2
    //   178: aload 6
    //   180: invokestatic 176	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   183: aload_1
    //   184: astore_1
    //   185: goto -71 -> 114
    //   188: pop
    //   189: aload_1
    //   190: astore_1
    //   191: aload_0
    //   192: getfield 59	org/apache/commons/io/input/Tailer:listener	Lorg/apache/commons/io/input/TailerListener;
    //   195: invokeinterface 151 1 0
    //   200: aload_1
    //   201: astore_1
    //   202: goto -88 -> 114
    //   205: lload 6
    //   207: lload_2
    //   208: lcmp
    //   209: ifle +70 -> 279
    //   212: aload_0
    //   213: aload_1
    //   214: invokespecial 178	org/apache/commons/io/input/Tailer:readLines	(Ljava/io/RandomAccessFile;)J
    //   217: lstore_2
    //   218: invokestatic 161	java/lang/System:currentTimeMillis	()J
    //   221: lstore 4
    //   223: aload_0
    //   224: getfield 67	org/apache/commons/io/input/Tailer:reOpen	Z
    //   227: ifeq +7 -> 234
    //   230: aload_1
    //   231: invokestatic 176	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   234: aload_0
    //   235: getfield 53	org/apache/commons/io/input/Tailer:delayMillis	J
    //   238: invokestatic 148	java/lang/Thread:sleep	(J)V
    //   241: aload_0
    //   242: getfield 49	org/apache/commons/io/input/Tailer:run	Z
    //   245: ifeq +116 -> 361
    //   248: aload_0
    //   249: getfield 67	org/apache/commons/io/input/Tailer:reOpen	Z
    //   252: ifeq +109 -> 361
    //   255: new 101	java/io/RandomAccessFile
    //   258: dup
    //   259: aload_0
    //   260: getfield 51	org/apache/commons/io/input/Tailer:file	Ljava/io/File;
    //   263: ldc 15
    //   265: invokespecial 145	java/io/RandomAccessFile:<init>	(Ljava/io/File;Ljava/lang/String;)V
    //   268: astore_1
    //   269: aload_1
    //   270: lload_2
    //   271: invokevirtual 113	java/io/RandomAccessFile:seek	(J)V
    //   274: aload_1
    //   275: astore_1
    //   276: goto -162 -> 114
    //   279: iload 8
    //   281: ifeq -58 -> 223
    //   284: aload_1
    //   285: ldc2_w 141
    //   288: invokevirtual 113	java/io/RandomAccessFile:seek	(J)V
    //   291: aload_0
    //   292: aload_1
    //   293: invokespecial 178	org/apache/commons/io/input/Tailer:readLines	(Ljava/io/RandomAccessFile;)J
    //   296: lstore_2
    //   297: invokestatic 161	java/lang/System:currentTimeMillis	()J
    //   300: lstore 4
    //   302: lload 4
    //   304: lstore 4
    //   306: goto -83 -> 223
    //   309: aload_1
    //   310: invokestatic 176	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   313: aload_1
    //   314: pop
    //   315: return
    //   316: astore_2
    //   317: aload_1
    //   318: astore_1
    //   319: aload_0
    //   320: getfield 59	org/apache/commons/io/input/Tailer:listener	Lorg/apache/commons/io/input/TailerListener;
    //   323: aload_2
    //   324: invokeinterface 181 2 0
    //   329: aload_1
    //   330: invokestatic 176	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   333: goto -18 -> 315
    //   336: astore_2
    //   337: aload_1
    //   338: astore_1
    //   339: aload_1
    //   340: invokestatic 176	org/apache/commons/io/IOUtils:closeQuietly	(Ljava/io/Closeable;)V
    //   343: aload_2
    //   344: athrow
    //   345: astore_2
    //   346: goto -7 -> 339
    //   349: astore_2
    //   350: goto -31 -> 319
    //   353: pop
    //   354: goto -113 -> 241
    //   357: pop
    //   358: goto -167 -> 191
    //   361: aload_1
    //   362: astore_1
    //   363: goto -89 -> 274
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	366	0	this	Tailer
    //   10	353	1	localRandomAccessFile1	RandomAccessFile
    //   8	289	2	l1	long
    //   316	8	2	localException1	java.lang.Exception
    //   336	8	2	localObject1	Object
    //   345	1	2	localObject2	Object
    //   349	1	2	localException2	java.lang.Exception
    //   3	302	4	l2	long
    //   15	3	6	bool1	boolean
    //   139	3	6	l3	long
    //   158	48	6	localRandomAccessFile2	RandomAccessFile
    //   130	150	8	bool2	boolean
    //   56	1	12	localFileNotFoundException1	java.io.FileNotFoundException
    //   71	1	13	localInterruptedException1	java.lang.InterruptedException
    //   188	1	14	localFileNotFoundException2	java.io.FileNotFoundException
    //   353	1	15	localInterruptedException2	java.lang.InterruptedException
    //   357	1	16	localFileNotFoundException3	java.io.FileNotFoundException
    // Exception table:
    //   from	to	target	type
    //   26	40	56	java/io/FileNotFoundException
    //   44	51	71	java/lang/InterruptedException
    //   160	174	188	java/io/FileNotFoundException
    //   11	17	316	java/lang/Exception
    //   26	40	316	java/lang/Exception
    //   57	66	316	java/lang/Exception
    //   114	157	316	java/lang/Exception
    //   160	174	316	java/lang/Exception
    //   212	234	316	java/lang/Exception
    //   234	241	316	java/lang/Exception
    //   241	269	316	java/lang/Exception
    //   284	302	316	java/lang/Exception
    //   11	17	336	finally
    //   26	40	336	finally
    //   57	66	336	finally
    //   114	157	336	finally
    //   160	174	336	finally
    //   212	234	336	finally
    //   234	241	336	finally
    //   241	269	336	finally
    //   284	302	336	finally
    //   44	51	345	finally
    //   77	102	345	finally
    //   178	183	345	finally
    //   191	200	345	finally
    //   269	274	345	finally
    //   319	329	345	finally
    //   44	51	349	java/lang/Exception
    //   77	102	349	java/lang/Exception
    //   178	183	349	java/lang/Exception
    //   191	200	349	java/lang/Exception
    //   269	274	349	java/lang/Exception
    //   234	241	353	java/lang/InterruptedException
    //   178	183	357	java/io/FileNotFoundException
  }
  
  public void stop()
  {
    this.run = false;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.apache.commons.io.input.Tailer
 * JD-Core Version:    0.7.0.1
 */