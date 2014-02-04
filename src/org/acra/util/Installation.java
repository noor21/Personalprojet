package org.acra.util;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;
import org.acra.ACRA;

public class Installation
{
  private static final String INSTALLATION = "ACRA-INSTALLATION";
  private static String sID;
  
  /**
   * @deprecated
   */
  public static String id(Context paramContext)
  {
    try
    {
      Object localObject1;
      if (sID == null) {
        localObject1 = new File(paramContext.getFilesDir(), "ACRA-INSTALLATION");
      }
      try
      {
        if (!((File)localObject1).exists()) {
          writeInstallationFile((File)localObject1);
        }
        sID = readInstallationFile((File)localObject1);
        localObject1 = sID;
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Couldn't retrieve InstallationId for " + paramContext.getPackageName(), localIOException);
          String str1 = "Couldn't retrieve InstallationId";
        }
      }
      catch (RuntimeException localRuntimeException)
      {
        for (;;)
        {
          Log.w(ACRA.LOG_TAG, "Couldn't retrieve InstallationId for " + paramContext.getPackageName(), localRuntimeException);
          String str2 = "Couldn't retrieve InstallationId";
        }
      }
      return localObject1;
    }
    finally {}
  }
  
  private static String readInstallationFile(File paramFile)
    throws IOException
  {
    RandomAccessFile localRandomAccessFile = new RandomAccessFile(paramFile, "r");
    byte[] arrayOfByte = new byte[(int)localRandomAccessFile.length()];
    try
    {
      localRandomAccessFile.readFully(arrayOfByte);
      return new String(arrayOfByte);
    }
    finally
    {
      localRandomAccessFile.close();
    }
  }
  
  private static void writeInstallationFile(File paramFile)
    throws IOException
  {
    FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
    try
    {
      localFileOutputStream.write(UUID.randomUUID().toString().getBytes());
      return;
    }
    finally
    {
      localFileOutputStream.close();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.Installation
 * JD-Core Version:    0.7.0.1
 */