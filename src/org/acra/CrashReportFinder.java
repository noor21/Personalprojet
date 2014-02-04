package org.acra;

import android.content.Context;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;

final class CrashReportFinder
{
  private final Context context;
  
  public CrashReportFinder(Context paramContext)
  {
    this.context = paramContext;
  }
  
  public String[] getCrashReportFiles()
  {
    Object localObject;
    if (this.context != null)
    {
      localObject = this.context.getFilesDir();
      if (localObject != null)
      {
        Log.d(ACRA.LOG_TAG, "Looking for error files in " + ((File)localObject).getAbsolutePath());
        localObject = ((File)localObject).list(new FilenameFilter()
        {
          public boolean accept(File paramAnonymousFile, String paramAnonymousString)
          {
            return paramAnonymousString.endsWith(".stacktrace");
          }
        });
        if (localObject == null) {
          localObject = new String[0];
        }
      }
      else
      {
        Log.w(ACRA.LOG_TAG, "Application files directory does not exist! The application may not be installed correctly. Please try reinstalling.");
        localObject = new String[0];
      }
    }
    else
    {
      Log.e(ACRA.LOG_TAG, "Trying to get ACRA reports but ACRA is not initialized.");
      localObject = new String[0];
    }
    return localObject;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.CrashReportFinder
 * JD-Core Version:    0.7.0.1
 */