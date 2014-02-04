package org.acra;

import android.content.Context;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import org.acra.collector.CrashReportData;

final class CrashReportPersister
{
  private static final int CONTINUE = 3;
  private static final int IGNORE = 5;
  private static final int KEY_DONE = 4;
  private static final String LINE_SEPARATOR = "\n";
  private static final int NONE = 0;
  private static final int SLASH = 1;
  private static final int UNICODE = 2;
  private final Context context;
  
  CrashReportPersister(Context paramContext)
  {
    this.context = paramContext;
  }
  
  private void dumpString(StringBuilder paramStringBuilder, String paramString, boolean paramBoolean)
  {
    int i = 0;
    if ((!paramBoolean) && (paramString.length() < 0) && (paramString.charAt(0) == ' ')) {
      paramStringBuilder.append("\\ ");
    }
    for (i = 0 + 1;; i++)
    {
      if (i >= paramString.length()) {
        return;
      }
      int j = paramString.charAt(i);
      switch (j)
      {
      case 11: 
      default: 
        if (("\\#!=:".indexOf(j) >= 0) || ((paramBoolean) && (j == 32))) {
          paramStringBuilder.append('\\');
        }
        if ((j < 32) || (j > 126))
        {
          String str = Integer.toHexString(j);
          paramStringBuilder.append("\\u");
          for (j = 0;; j++)
          {
            if (j >= 4 - str.length())
            {
              paramStringBuilder.append(str);
              break;
            }
            paramStringBuilder.append("0");
          }
        }
        paramStringBuilder.append(j);
        break;
      case 9: 
        paramStringBuilder.append("\\t");
        break;
      case 10: 
        paramStringBuilder.append("\\n");
        break;
      case 12: 
        paramStringBuilder.append("\\f");
        break;
      case 13: 
        paramStringBuilder.append("\\r");
      }
    }
  }
  
  private boolean isEbcdic(BufferedInputStream paramBufferedInputStream)
    throws IOException
  {
    boolean bool = false;
    int i;
    do
    {
      i = (byte)paramBufferedInputStream.read();
      if ((i == -1) || (i == 35) || (i == 10) || (i == 61)) {
        break;
      }
    } while (i != 21);
    bool = true;
    return bool;
  }
  
  /**
   * @deprecated
   */
  private CrashReportData load(Reader paramReader)
    throws IOException
  {
    int i = 0;
    int n = 0;
    int i1 = 0;
    Object localObject1;
    int j;
    int m;
    Object localObject2;
    String str3;
    label215:
    label235:
    int i5;
    label297:
    String str2;
    for (;;)
    {
      int i2;
      try
      {
        localObject1 = new char[40];
        j = -1;
        m = 1;
        CrashReportData localCrashReportData1 = new CrashReportData();
        localObject2 = new BufferedReader(paramReader, 8192);
        i2 = 0;
        i4 = ((BufferedReader)localObject2).read();
        if (i4 == -1)
        {
          if ((i != 2) || (i1 > 4)) {
            break label804;
          }
          throw new IllegalArgumentException("luni.08");
        }
      }
      finally {}
      int i4 = (char)i4;
      if (i2 == localObject1.length)
      {
        char[] arrayOfChar = new char[2 * localObject1.length];
        System.arraycopy(localObject1, 0, arrayOfChar, 0, i2);
        localObject1 = arrayOfChar;
      }
      if (i == 2)
      {
        str3 = Character.digit(i4, 16);
        if (str3 >= 0)
        {
          n = str3 + (n << 4);
          i1++;
          if (i1 < 4) {
            continue;
          }
        }
        while (i1 > 4)
        {
          i = 0;
          str3 = i2 + 1;
          localObject1[i2] = ((char)n);
          if ((i4 == 10) || (i4 == 133)) {
            break label425;
          }
          i2 = str3;
          break;
        }
        throw new IllegalArgumentException("luni.09");
        str3 = i2 + 1;
        localObject1[i2] = i4;
        i2 = str3;
        continue;
        if (!Character.isWhitespace(i4)) {
          break label789;
        }
        if (i != 3) {
          break label668;
        }
        i = 5;
        break label668;
        do
        {
          i5 = ((BufferedReader)localObject2).read();
          if (i5 == -1) {
            break;
          }
          i5 = (char)i5;
          if ((i5 == 13) || (i5 == 10)) {
            break;
          }
        } while (i5 != 133);
        continue;
        str2 = new String((char[])localObject1, 0, i2);
        localCrashReportData2.put(Enum.valueOf(ReportField.class, str2.substring(0, j)), str2.substring(j));
        break label749;
      }
    }
    for (;;)
    {
      String str1;
      if (j >= 0)
      {
        localObject2 = new String((char[])localObject1, 0, str2);
        localObject1 = (ReportField)Enum.valueOf(ReportField.class, ((String)localObject2).substring(0, j));
        str1 = ((String)localObject2).substring(j);
        if (i == 1) {
          str1 = str1 + "��";
        }
        localCrashReportData2.put((Enum)localObject1, str1);
      }
      return localCrashReportData2;
      label425:
      str2 = str3;
      if (i == 1)
      {
        i = 0;
        switch (i5)
        {
        }
      }
      label668:
      label749:
      int k;
      int i3;
      for (;;)
      {
        m = 0;
        if (i != 4) {
          break label215;
        }
        str1 = str2;
        i = 0;
        break label215;
        i = 3;
        break;
        i = 5;
        break;
        i5 = 8;
        continue;
        i5 = 12;
        continue;
        i5 = 10;
        continue;
        i5 = 13;
        continue;
        i5 = 9;
        continue;
        i = 2;
        i1 = 0;
        n = 0;
        break;
        switch (i5)
        {
        default: 
          if ((str2 == 0) || (str2 == str1) || (i == 5)) {
            break;
          }
          if (str1 == -1) {
            i = 4;
          }
          break;
        case 33: 
        case 35: 
          if (m == 0) {
            break label235;
          }
          break;
        case 10: 
          if (i == 3) {
            i = 5;
          }
          break;
        case 13: 
        case 133: 
          i = 0;
          m = 1;
          if ((str2 > 0) || ((str2 == 0) && (str1 == 0)))
          {
            if (str1 != -1) {
              break label297;
            }
            str1 = str2;
            break label297;
          }
          k = -1;
          i3 = 0;
          break;
        case 92: 
          if (i == 4) {
            k = i3;
          }
          i = 1;
          break;
        case 58: 
        case 61: 
          if (k != -1) {
            break label235;
          }
          i = 0;
          k = i3;
          break;
          label789:
          if ((i == 5) || (i == 3)) {
            i = 0;
          }
          break;
        }
      }
      label804:
      if ((k == -1) && (i3 > 0)) {
        k = i3;
      }
    }
  }
  
  /* Error */
  public CrashReportData load(String paramString)
    throws IOException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 30	org/acra/CrashReportPersister:context	Landroid/content/Context;
    //   4: aload_1
    //   5: invokevirtual 157	android/content/Context:openFileInput	(Ljava/lang/String;)Ljava/io/FileInputStream;
    //   8: astore_2
    //   9: aload_2
    //   10: ifnonnull +30 -> 40
    //   13: new 99	java/lang/IllegalArgumentException
    //   16: dup
    //   17: new 46	java/lang/StringBuilder
    //   20: dup
    //   21: invokespecial 144	java/lang/StringBuilder:<init>	()V
    //   24: ldc 159
    //   26: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   29: aload_1
    //   30: invokevirtual 50	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
    //   33: invokevirtual 150	java/lang/StringBuilder:toString	()Ljava/lang/String;
    //   36: invokespecial 104	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
    //   39: athrow
    //   40: new 83	java/io/BufferedInputStream
    //   43: dup
    //   44: aload_2
    //   45: sipush 8192
    //   48: invokespecial 162	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   51: astore 4
    //   53: aload 4
    //   55: ldc 163
    //   57: invokevirtual 167	java/io/BufferedInputStream:mark	(I)V
    //   60: aload_0
    //   61: aload 4
    //   63: invokespecial 169	org/acra/CrashReportPersister:isEbcdic	(Ljava/io/BufferedInputStream;)Z
    //   66: istore_3
    //   67: aload 4
    //   69: invokevirtual 172	java/io/BufferedInputStream:reset	()V
    //   72: iload_3
    //   73: ifne +27 -> 100
    //   76: aload_0
    //   77: new 174	java/io/InputStreamReader
    //   80: dup
    //   81: aload 4
    //   83: ldc 176
    //   85: invokespecial 179	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;Ljava/lang/String;)V
    //   88: invokespecial 181	org/acra/CrashReportPersister:load	(Ljava/io/Reader;)Lorg/acra/collector/CrashReportData;
    //   91: astore_3
    //   92: aload_3
    //   93: astore_3
    //   94: aload_2
    //   95: invokevirtual 186	java/io/FileInputStream:close	()V
    //   98: aload_3
    //   99: areturn
    //   100: aload_0
    //   101: new 174	java/io/InputStreamReader
    //   104: dup
    //   105: aload 4
    //   107: invokespecial 189	java/io/InputStreamReader:<init>	(Ljava/io/InputStream;)V
    //   110: invokespecial 181	org/acra/CrashReportPersister:load	(Ljava/io/Reader;)Lorg/acra/collector/CrashReportData;
    //   113: astore_3
    //   114: aload_3
    //   115: astore_3
    //   116: aload_2
    //   117: invokevirtual 186	java/io/FileInputStream:close	()V
    //   120: goto -22 -> 98
    //   123: astore_3
    //   124: aload_2
    //   125: invokevirtual 186	java/io/FileInputStream:close	()V
    //   128: aload_3
    //   129: athrow
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	130	0	this	CrashReportPersister
    //   0	130	1	paramString	String
    //   8	117	2	localFileInputStream	java.io.FileInputStream
    //   66	7	3	bool	boolean
    //   91	25	3	localCrashReportData	CrashReportData
    //   123	6	3	localObject	Object
    //   51	55	4	localBufferedInputStream	BufferedInputStream
    // Exception table:
    //   from	to	target	type
    //   40	92	123	finally
    //   100	114	123	finally
  }
  
  public void store(CrashReportData paramCrashReportData, String paramString)
    throws IOException
  {
    FileOutputStream localFileOutputStream = this.context.openFileOutput(paramString, 0);
    OutputStreamWriter localOutputStreamWriter;
    try
    {
      StringBuilder localStringBuilder = new StringBuilder(200);
      localOutputStreamWriter = new OutputStreamWriter(localFileOutputStream, "ISO8859_1");
      Iterator localIterator = paramCrashReportData.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        dumpString(localStringBuilder, ((ReportField)localEntry.getKey()).toString(), true);
        localStringBuilder.append('=');
        dumpString(localStringBuilder, (String)localEntry.getValue(), false);
        localStringBuilder.append("\n");
        localOutputStreamWriter.write(localStringBuilder.toString());
        localStringBuilder.setLength(0);
      }
    }
    finally
    {
      localFileOutputStream.close();
    }
    localFileOutputStream.close();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.CrashReportPersister
 * JD-Core Version:    0.7.0.1
 */