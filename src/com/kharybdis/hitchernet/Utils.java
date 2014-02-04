package com.kharybdis.hitchernet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import java.io.File;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import org.apache.http.conn.util.InetAddressUtils;

public class Utils
{
  private static final String TAG = "hitchernetUtils";
  private static final String p2pInt = "p2p-p2p0";
  
  private String fileExt(String paramString)
  {
    if (paramString.indexOf("?") > -1) {
      paramString = paramString.substring(0, paramString.indexOf("?"));
    }
    String str;
    if (paramString.lastIndexOf(".") != -1)
    {
      str = paramString.substring(paramString.lastIndexOf("."));
      if (str.indexOf("%") > -1) {
        str = str.substring(0, str.indexOf("%"));
      }
      if (str.indexOf("/") > -1) {
        str = str.substring(0, str.indexOf("/"));
      }
      str = str.toLowerCase();
    }
    else
    {
      str = null;
    }
    return str;
  }
  
  /* Error */
  private static String getIPFromMac(String paramString)
  {
    // Byte code:
    //   0: aconst_null
    //   1: astore_1
    //   2: new 52	java/io/BufferedReader
    //   5: dup
    //   6: new 54	java/io/FileReader
    //   9: dup
    //   10: ldc 56
    //   12: invokespecial 59	java/io/FileReader:<init>	(Ljava/lang/String;)V
    //   15: invokespecial 62	java/io/BufferedReader:<init>	(Ljava/io/Reader;)V
    //   18: astore_1
    //   19: aload_1
    //   20: invokevirtual 65	java/io/BufferedReader:readLine	()Ljava/lang/String;
    //   23: astore_2
    //   24: aload_2
    //   25: ifnonnull +11 -> 36
    //   28: aload_1
    //   29: invokevirtual 68	java/io/BufferedReader:close	()V
    //   32: aconst_null
    //   33: astore_2
    //   34: aload_2
    //   35: areturn
    //   36: aload_2
    //   37: ldc 70
    //   39: invokevirtual 74	java/lang/String:split	(Ljava/lang/String;)[Ljava/lang/String;
    //   42: astore_2
    //   43: aload_2
    //   44: ifnull -25 -> 19
    //   47: aload_2
    //   48: arraylength
    //   49: iconst_4
    //   50: if_icmplt -31 -> 19
    //   53: aload_2
    //   54: iconst_5
    //   55: aaload
    //   56: ldc 76
    //   58: invokevirtual 80	java/lang/String:matches	(Ljava/lang/String;)Z
    //   61: ifeq -42 -> 19
    //   64: aload_2
    //   65: iconst_3
    //   66: aaload
    //   67: aload_0
    //   68: invokevirtual 80	java/lang/String:matches	(Ljava/lang/String;)Z
    //   71: ifeq -52 -> 19
    //   74: aload_2
    //   75: iconst_0
    //   76: aaload
    //   77: astore_2
    //   78: aload_1
    //   79: invokevirtual 68	java/io/BufferedReader:close	()V
    //   82: goto -48 -> 34
    //   85: astore_1
    //   86: aload_1
    //   87: invokevirtual 83	java/io/IOException:printStackTrace	()V
    //   90: goto -8 -> 82
    //   93: astore_2
    //   94: aload_2
    //   95: invokevirtual 84	java/lang/Exception:printStackTrace	()V
    //   98: aload_1
    //   99: invokevirtual 68	java/io/BufferedReader:close	()V
    //   102: goto -70 -> 32
    //   105: astore_1
    //   106: aload_1
    //   107: invokevirtual 83	java/io/IOException:printStackTrace	()V
    //   110: goto -78 -> 32
    //   113: astore_2
    //   114: aload_1
    //   115: invokevirtual 68	java/io/BufferedReader:close	()V
    //   118: aload_2
    //   119: athrow
    //   120: astore_1
    //   121: aload_1
    //   122: invokevirtual 83	java/io/IOException:printStackTrace	()V
    //   125: goto -7 -> 118
    //   128: astore_1
    //   129: aload_1
    //   130: invokevirtual 83	java/io/IOException:printStackTrace	()V
    //   133: goto -101 -> 32
    //   136: astore_2
    //   137: aload_1
    //   138: astore_1
    //   139: goto -25 -> 114
    //   142: astore_2
    //   143: aload_1
    //   144: astore_1
    //   145: goto -51 -> 94
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	148	0	paramString	String
    //   1	78	1	localBufferedReader	java.io.BufferedReader
    //   85	14	1	localIOException1	java.io.IOException
    //   105	10	1	localIOException2	java.io.IOException
    //   120	2	1	localIOException3	java.io.IOException
    //   128	10	1	localIOException4	java.io.IOException
    //   138	7	1	localIOException5	java.io.IOException
    //   23	55	2	localObject1	Object
    //   93	2	2	localException1	Exception
    //   113	6	2	localObject2	Object
    //   136	1	2	localObject3	Object
    //   142	1	2	localException2	Exception
    // Exception table:
    //   from	to	target	type
    //   78	82	85	java/io/IOException
    //   2	19	93	java/lang/Exception
    //   98	102	105	java/io/IOException
    //   2	19	113	finally
    //   94	98	113	finally
    //   114	118	120	java/io/IOException
    //   28	32	128	java/io/IOException
    //   19	24	136	finally
    //   36	78	136	finally
    //   19	24	142	java/lang/Exception
    //   36	78	142	java/lang/Exception
  }
  
  private static String getLocalIPAddress()
  {
    String str;
    try
    {
      do
      {
        Enumeration localEnumeration2 = NetworkInterface.getNetworkInterfaces();
        Enumeration localEnumeration1;
        while (!localEnumeration1.hasMoreElements())
        {
          if (!localEnumeration2.hasMoreElements()) {
            break;
          }
          localEnumeration1 = ((NetworkInterface)localEnumeration2.nextElement()).getInetAddresses();
        }
        localObject = (InetAddress)localEnumeration1.nextElement();
      } while (((InetAddress)localObject).isLoopbackAddress());
      Object localObject = ((InetAddress)localObject).getHostAddress();
      Log.i("hitchernetUtils", "***** IP=" + (String)localObject);
    }
    catch (SocketException localSocketException)
    {
      Log.e("hitchernetUtils", localSocketException.toString());
      str = null;
    }
    return str;
  }
  
  private static String getLocalIpv4Address()
  {
    String str;
    try
    {
      do
      {
        Enumeration localEnumeration1 = NetworkInterface.getNetworkInterfaces();
        Enumeration localEnumeration2;
        while (!localEnumeration2.hasMoreElements())
        {
          if (!localEnumeration1.hasMoreElements()) {
            break;
          }
          localEnumeration2 = ((NetworkInterface)localEnumeration1.nextElement()).getInetAddresses();
        }
        localObject = (InetAddress)localEnumeration2.nextElement();
        Log.v("hitchernetUtils", "ip1--:" + localObject);
      } while ((((InetAddress)localObject).isLoopbackAddress()) || (!InetAddressUtils.isIPv4Address(((InetAddress)localObject).getHostAddress())));
      Object localObject = ((InetAddress)localObject).getHostAddress().toString();
      localObject = localObject;
    }
    catch (Exception localException)
    {
      Log.e("IP Address", localException.toString());
      str = null;
    }
    return str;
  }
  
  private static String getMacAddress(Context paramContext)
  {
    String str = ((WifiManager)paramContext.getSystemService("wifi")).getConnectionInfo().getMacAddress();
    Log.v("hitchernetUtils", "mac address retrieved from phone=" + str);
    return str;
  }
  
  public static void toast(Context paramContext, String paramString)
  {
    Toast.makeText(paramContext, paramString, 0).show();
  }
  
  public void startActivityChooser(Context paramContext, String paramString)
  {
    Object localObject = MimeTypeMap.getSingleton();
    Intent localIntent = new Intent("android.intent.action.VIEW");
    System.out.println("file path = " + paramString);
    try
    {
      localObject = ((MimeTypeMap)localObject).getMimeTypeFromExtension(fileExt(new File(paramString).toString()).substring(1));
      System.out.println("mime type = =" + (String)localObject);
      localIntent.setDataAndType(Uri.fromFile(new File(paramString)), (String)localObject);
      localIntent.setFlags(268435456);
      paramContext.startActivity(localIntent);
      return;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Toast.makeText(paramContext, "Sorry, We didn't find an appropriate application to open this file.", 0).show();
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.Utils
 * JD-Core Version:    0.7.0.1
 */