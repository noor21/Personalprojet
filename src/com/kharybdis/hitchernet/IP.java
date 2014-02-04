package com.kharybdis.hitchernet;

import android.util.Log;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.conn.util.InetAddressUtils;

public class IP
{
  private static final String TAG = "hitchernetIP";
  
  public static String bytesToHex(byte[] paramArrayOfByte)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < paramArrayOfByte.length; i++)
    {
      int j = 0xFF & paramArrayOfByte[i];
      if (j < 16) {
        localStringBuilder.append("0");
      }
      localStringBuilder.append(Integer.toHexString(j).toUpperCase());
    }
    return localStringBuilder.toString();
  }
  
  private static String getIPAddress(boolean paramBoolean)
  {
    label33:
    label35:
    do
    {
      try
      {
        localIterator = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
        boolean bool1 = localIterator.hasNext();
        if (bool1) {
          break label35;
        }
      }
      catch (Exception localException)
      {
        for (;;)
        {
          Iterator localIterator;
          Object localObject2;
          Object localObject1;
          Log.v("hitchernetIP", "error in parsing");
        }
      }
      Log.v("hitchernetIP", "returning empty");
      localObject2 = null;
      return localObject2;
      localObject1 = (NetworkInterface)localIterator.next();
    } while (!getMACAddress(((NetworkInterface)localObject1).getName()).equalsIgnoreCase(Globals.thisDeviceAddress));
    Log.v("hitchernetIP", "interface=" + ((NetworkInterface)localObject1).getName() + "mac = " + getMACAddress(((NetworkInterface)localObject1).getName()));
    localObject1 = Collections.list(((NetworkInterface)localObject1).getInetAddresses()).iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (InetAddress)((Iterator)localObject1).next();
      Log.v("hitchernetIP", "inside");
      if (!((InetAddress)localObject2).isLoopbackAddress())
      {
        Log.v("hitchernetIP", "isnt loopback");
        localObject2 = ((InetAddress)localObject2).getHostAddress().toUpperCase();
        Log.v("hitchernetIP", "ip=" + (String)localObject2);
        boolean bool2 = InetAddressUtils.isIPv4Address((String)localObject2);
        if (paramBoolean)
        {
          if (!bool2) {
            continue;
          }
          break label33;
        }
        if (!bool2)
        {
          Log.v("hitchernetIP", "delim");
          int i = ((String)localObject2).indexOf('%');
          if (i < 0) {
            break label33;
          }
          localObject2 = ((String)localObject2).substring(0, i);
          break label33;
        }
        Log.v("hitchernetIP", "failed here");
      }
    }
  }
  
  public static String getIPAddress1()
  {
    try
    {
      localIterator = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
      boolean bool = localIterator.hasNext();
      if (bool) {
        break label36;
      }
    }
    catch (Exception localException)
    {
      for (;;)
      {
        Iterator localIterator;
        Object localObject2;
        label36:
        Log.v("hitchernetIP", "error in parsing");
      }
    }
    Log.v("hitchernetIP", "returning null ip address");
    localObject2 = "";
    for (;;)
    {
      return localObject2;
      Object localObject1 = (NetworkInterface)localIterator.next();
      getMACAddress(((NetworkInterface)localObject1).getName()).equalsIgnoreCase(Globals.thisDeviceAddress);
      if (!((NetworkInterface)localObject1).getName().contains("p2p")) {
        break;
      }
      Log.v("hitchernetIP", ((NetworkInterface)localObject1).getName() + "   " + getMACAddress(((NetworkInterface)localObject1).getName()));
      localObject1 = Collections.list(((NetworkInterface)localObject1).getInetAddresses()).iterator();
      do
      {
        do
        {
          if (!((Iterator)localObject1).hasNext()) {
            break;
          }
          localObject2 = (InetAddress)((Iterator)localObject1).next();
        } while (((InetAddress)localObject2).isLoopbackAddress());
        localObject2 = ((InetAddress)localObject2).getHostAddress().toUpperCase();
        Log.v("hitchernetIP", "ip=" + (String)localObject2);
      } while ((!InetAddressUtils.isIPv4Address((String)localObject2)) || (!((String)localObject2).contains("192.168.49.")));
      Log.v("hitchernetIP", "ip = " + (String)localObject2);
    }
  }
  
  public static String getMACAddress(String paramString)
  {
    try
    {
      localObject1 = Collections.list(NetworkInterface.getNetworkInterfaces()).iterator();
      boolean bool = ((Iterator)localObject1).hasNext();
      if (bool) {}
    }
    catch (Exception localException)
    {
      Object localObject1;
      label23:
      Object localObject2;
      label68:
      StringBuilder localStringBuilder;
      int i;
      break label23;
    }
    for (localObject1 = "";; localObject1 = "")
    {
      return localObject1;
      localObject2 = (NetworkInterface)((Iterator)localObject1).next();
      if ((paramString != null) && (!((NetworkInterface)localObject2).getName().equalsIgnoreCase(paramString))) {
        break;
      }
      localObject1 = ((NetworkInterface)localObject2).getHardwareAddress();
      if (localObject1 != null) {
        break label68;
      }
    }
    localStringBuilder = new StringBuilder();
    for (i = 0;; i++)
    {
      if (i >= localObject1.length)
      {
        if (localStringBuilder.length() > 0) {
          localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
        }
        localObject1 = localStringBuilder.toString();
        break;
      }
      localObject2 = new Object[1];
      localObject2[0] = Byte.valueOf(localObject1[i]);
      localStringBuilder.append(String.format("%02X:", (Object[])localObject2));
    }
  }
  
  public static byte[] getUTF8Bytes(String paramString)
  {
    try
    {
      arrayOfByte = paramString.getBytes("UTF-8");
      arrayOfByte = arrayOfByte;
    }
    catch (Exception localException)
    {
      for (;;)
      {
        byte[] arrayOfByte = null;
      }
    }
    return arrayOfByte;
  }
  
  /* Error */
  public static String loadFileAsString(String paramString)
    throws java.io.IOException
  {
    // Byte code:
    //   0: new 193	java/io/BufferedInputStream
    //   3: dup
    //   4: new 195	java/io/FileInputStream
    //   7: dup
    //   8: aload_0
    //   9: invokespecial 196	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
    //   12: sipush 1024
    //   15: invokespecial 199	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
    //   18: astore_1
    //   19: new 201	java/io/ByteArrayOutputStream
    //   22: dup
    //   23: sipush 1024
    //   26: invokespecial 204	java/io/ByteArrayOutputStream:<init>	(I)V
    //   29: astore_2
    //   30: sipush 1024
    //   33: newarray byte
    //   35: astore 5
    //   37: iconst_0
    //   38: istore 6
    //   40: iconst_0
    //   41: istore_3
    //   42: aload_1
    //   43: aload 5
    //   45: invokevirtual 208	java/io/BufferedInputStream:read	([B)I
    //   48: istore 4
    //   50: iload 4
    //   52: bipush 255
    //   54: if_icmpne +28 -> 82
    //   57: iload 6
    //   59: ifeq +90 -> 149
    //   62: new 31	java/lang/String
    //   65: dup
    //   66: aload_2
    //   67: invokevirtual 211	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   70: ldc 185
    //   72: invokespecial 214	java/lang/String:<init>	([BLjava/lang/String;)V
    //   75: astore_2
    //   76: aload_1
    //   77: invokevirtual 217	java/io/BufferedInputStream:close	()V
    //   80: aload_2
    //   81: areturn
    //   82: iload_3
    //   83: ifne +47 -> 130
    //   86: aload 5
    //   88: iconst_0
    //   89: baload
    //   90: bipush 239
    //   92: if_icmpne +38 -> 130
    //   95: aload 5
    //   97: iconst_1
    //   98: baload
    //   99: bipush 187
    //   101: if_icmpne +29 -> 130
    //   104: aload 5
    //   106: iconst_2
    //   107: baload
    //   108: bipush 191
    //   110: if_icmpne +20 -> 130
    //   113: iconst_1
    //   114: istore 6
    //   116: aload_2
    //   117: aload 5
    //   119: iconst_3
    //   120: iload 4
    //   122: iconst_3
    //   123: isub
    //   124: invokevirtual 221	java/io/ByteArrayOutputStream:write	([BII)V
    //   127: goto +45 -> 172
    //   130: aload_2
    //   131: aload 5
    //   133: iconst_0
    //   134: iload 4
    //   136: invokevirtual 221	java/io/ByteArrayOutputStream:write	([BII)V
    //   139: goto +33 -> 172
    //   142: astore_2
    //   143: aload_1
    //   144: invokevirtual 217	java/io/BufferedInputStream:close	()V
    //   147: aload_2
    //   148: athrow
    //   149: new 31	java/lang/String
    //   152: dup
    //   153: aload_2
    //   154: invokevirtual 211	java/io/ByteArrayOutputStream:toByteArray	()[B
    //   157: invokespecial 224	java/lang/String:<init>	([B)V
    //   160: astore_2
    //   161: goto -85 -> 76
    //   164: pop
    //   165: goto -85 -> 80
    //   168: pop
    //   169: goto -22 -> 147
    //   172: iload_3
    //   173: iload 4
    //   175: iadd
    //   176: istore_3
    //   177: goto -135 -> 42
    // Local variable table:
    //   start	length	slot	name	signature
    //   0	180	0	paramString	String
    //   18	126	1	localBufferedInputStream	java.io.BufferedInputStream
    //   29	102	2	localObject1	Object
    //   142	12	2	localObject2	Object
    //   160	1	2	str	String
    //   41	136	3	i	int
    //   48	128	4	j	int
    //   35	97	5	arrayOfByte	byte[]
    //   38	77	6	k	int
    //   164	1	9	localException1	Exception
    //   168	1	10	localException2	Exception
    // Exception table:
    //   from	to	target	type
    //   19	76	142	finally
    //   86	139	142	finally
    //   149	161	142	finally
    //   76	80	164	java/lang/Exception
    //   143	147	168	java/lang/Exception
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.IP
 * JD-Core Version:    0.7.0.1
 */