package org.acra.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.SparseArray;
import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.acra.ACRA;
import org.acra.log.ACRALog;

public final class ReportUtils
{
  public static String getApplicationFilePath(Context paramContext)
  {
    Object localObject = paramContext.getFilesDir();
    if (localObject == null)
    {
      Log.w(ACRA.LOG_TAG, "Couldn't retrieve ApplicationFilePath for : " + paramContext.getPackageName());
      localObject = "Couldn't retrieve ApplicationFilePath";
    }
    else
    {
      localObject = ((File)localObject).getAbsolutePath();
    }
    return localObject;
  }
  
  public static long getAvailableInternalMemorySize()
  {
    StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
    return localStatFs.getBlockSize() * localStatFs.getAvailableBlocks();
  }
  
  public static String getDeviceId(Context paramContext)
  {
    try
    {
      str = ((TelephonyManager)paramContext.getSystemService("phone")).getDeviceId();
      str = str;
    }
    catch (RuntimeException localRuntimeException)
    {
      for (;;)
      {
        String str;
        Log.w(ACRA.LOG_TAG, "Couldn't retrieve DeviceId for : " + paramContext.getPackageName(), localRuntimeException);
        Object localObject = null;
      }
    }
    return str;
  }
  
  public static String getLocalIpAddress()
  {
    localStringBuilder = new StringBuilder();
    int i = 1;
    try
    {
      Enumeration localEnumeration2 = NetworkInterface.getNetworkInterfaces();
      while (localEnumeration2.hasMoreElements())
      {
        Enumeration localEnumeration1 = ((NetworkInterface)localEnumeration2.nextElement()).getInetAddresses();
        while (localEnumeration1.hasMoreElements())
        {
          InetAddress localInetAddress = (InetAddress)localEnumeration1.nextElement();
          if (!localInetAddress.isLoopbackAddress())
          {
            if (i == 0) {
              localStringBuilder.append('\n');
            }
            localStringBuilder.append(localInetAddress.getHostAddress().toString());
            i = 0;
          }
        }
      }
      return localStringBuilder.toString();
    }
    catch (SocketException localSocketException)
    {
      ACRA.log.w(ACRA.LOG_TAG, localSocketException.toString());
    }
  }
  
  public static long getTotalInternalMemorySize()
  {
    StatFs localStatFs = new StatFs(Environment.getDataDirectory().getPath());
    return localStatFs.getBlockSize() * localStatFs.getBlockCount();
  }
  
  public static String sparseArrayToString(SparseArray<?> paramSparseArray)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    if (paramSparseArray != null)
    {
      localStringBuilder.append('{');
      for (int i = 0;; str++)
      {
        if (i >= paramSparseArray.size())
        {
          localStringBuilder.append('}');
          str = localStringBuilder.toString();
          break;
        }
        localStringBuilder.append(paramSparseArray.keyAt(str));
        localStringBuilder.append(" => ");
        if (paramSparseArray.valueAt(str) != null) {
          localStringBuilder.append(paramSparseArray.valueAt(str).toString());
        } else {
          localStringBuilder.append("null");
        }
        if (str < -1 + paramSparseArray.size()) {
          localStringBuilder.append(", ");
        }
      }
    }
    String str = "null";
    return str;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.ReportUtils
 * JD-Core Version:    0.7.0.1
 */