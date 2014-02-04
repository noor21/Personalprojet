package com.kharybdis.hitchernet;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdServiceResponseListener;
import android.net.wifi.p2p.WifiP2pManager.DnsSdTxtRecordListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.Time;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

public class FileTransferService
  extends Service
  implements WifiP2pManager.ChannelListener, DeviceListFragment.DeviceActionListener, DeviceListFragment.DeviceClickListener, WifiP2pManager.ConnectionInfoListener, WifiP2pManager.PeerListListener
{
  public static final String ACTION_SEND_FILE = "org.bajratech.hitchernet.SEND_FILE";
  private static int BUFFER_INPUT_OUTPUT_STREAM_SIZE = 1048576;
  public static final String EXTRAS_FILE_PATH = "file_url";
  public static final String EXTRAS_PORT = "go_port";
  private static final String EXTRAS_TARGET_DEVICE_MAC = "target Device address";
  private static final String EXTRAS_THIS_DEVICE_MAC = "MAC_ADDRESS";
  public static final String EXTRAS__IP_ADDRESS = "go_host";
  public static final String FILE_MIME_TYPE = "image/jpeg";
  public static final String FILE_NAME = "HitcherNet";
  public static final String FILE_SIZE = "filesize";
  public static final String FILE_TYPE = "jpg";
  public static final String INTENT_ACTION_NOTIFICATION_CANCELLED = "notification_cancelled";
  private static final String INTENT_ACTION_NOTIFICATION_CLICKED = "notificationClicked";
  public static final String INTENT_DISMISS_DIALOG = "com.kharybdis.hitchernet.DISMISS_DIALOG";
  public static final String INTENT_ERROR_IO_EXCEPTION = "IOExceptionOccurred";
  public static final String INTENT_EXTRAS_IP_ADDRESS = "ip_address";
  public static final String INTENT_EXTRAS_MAC_ADDRESS = "mac_address";
  public static final String INTENT_FILE_PATH = "com.kharybdis.hitchernet.filetransferservice.fileAbsoulutePath";
  public static final String INTENT_PROGRESS = "PROGESS_VALUE";
  public static final String INTENT_PROGRESS_NOTIFY = "intent_progress_notify";
  public static final String INTENT_SEND_TO_CLIENT = "sendToClient";
  public static final String INTENT_SEND_TO_SERVER = "sendToServer";
  public static final String INTENT_SHOW_DIALOG = "showProgressDialog";
  public static final String IP_SERVER = "192.168.49.1";
  public static int PORT = 8988;
  public static final String SERVICE_INSTANCE = "hitchernet";
  public static final String SERVICE_REG_TYPE = "_presence._tcp";
  private static int SIZE = 0;
  private static final int SOCKET_TIMEOUT = 100000;
  private static final int SOCKET_TIMEOUT_FOR_META = 5000;
  private static final String TAG = "hitchernetFileTransferService";
  private static int TCP_BUFFER_SIZE = 10485760;
  public static final String TXTRECORD_PROP_AVAILABLE = "available";
  private static boolean USE_TCP_BUFFER = false;
  private static String fileName;
  private ActivityNotifier activityNotifier;
  private WifiP2pManager.Channel channel;
  boolean clientMetaRunning = false;
  boolean clientRunning = false;
  private DbHelper dbHelper;
  WiFiDirectBroadcastReceiver directReceiver;
  boolean isDiscoveryGoingOn = false;
  boolean isTransferOngoing = false;
  boolean isWifiP2pEnabled = false;
  private final IBinder mBinder = new LocalBinder();
  NotificationCompat.Builder mBuilder;
  private WifiP2pManager manager;
  int notificationCounter = 0;
  NotificationManager notificationManager;
  private BroadcastReceiver notificationReceiver = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if ((!paramAnonymousIntent.getAction().equals("notificationClicked")) && (TextUtils.equals(paramAnonymousIntent.getAction(), "notification_cancelled")))
      {
        int i = paramAnonymousIntent.getIntExtra("notificationId", -1);
        FileTransferService.this.notifyData.put(Integer.valueOf(i), Boolean.valueOf(false));
      }
    }
  };
  private HashMap<Integer, Boolean> notifyData = new HashMap();
  private boolean retryChannel = false;
  boolean serverRunning = false;
  ServerSocket serverSocket;
  WifiP2pDnsSdServiceInfo serviceInfo;
  private WifiP2pDnsSdServiceRequest serviceRequest;
  private final IntentFilter wifiDirectIntentFilter = new IntentFilter();
  
  static
  {
    SIZE = 524288;
  }
  
  private void addActionsToIntentFilters(IntentFilter paramIntentFilter)
  {
    paramIntentFilter.addAction("android.net.wifi.p2p.STATE_CHANGED");
    paramIntentFilter.addAction("android.net.wifi.p2p.PEERS_CHANGED");
    paramIntentFilter.addAction("android.net.wifi.p2p.CONNECTION_STATE_CHANGE");
    paramIntentFilter.addAction("android.net.wifi.p2p.THIS_DEVICE_CHANGED");
    paramIntentFilter.addAction("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE");
    paramIntentFilter.addAction("notification_cancelled");
  }
  
  private void discoverService()
  {
    this.manager.setDnsSdResponseListeners(this.channel, new WifiP2pManager.DnsSdServiceResponseListener()new WifiP2pManager.DnsSdTxtRecordListener
    {
      public void onDnsSdServiceAvailable(String paramAnonymousString1, String paramAnonymousString2, WifiP2pDevice paramAnonymousWifiP2pDevice)
      {
        Log.v("hitchernetFileTransferService", "on dnssd service available");
        FileTransferService.this.isDiscoveryGoingOn = false;
        if (paramAnonymousString1.equalsIgnoreCase("hitchernet"))
        {
          Log.v("hitchernetFileTransferService", "instance available");
          FileTransferService.this.logv("peer found" + paramAnonymousWifiP2pDevice.deviceName);
          Log.d("hitchernetFileTransferService", "onBonjourServiceAvailable " + paramAnonymousString1);
          FileTransferService.this.logv("peer available");
          ArrayList localArrayList = new ArrayList();
          localArrayList.add(paramAnonymousWifiP2pDevice);
          Log.v("hitchernetFileTransferService", "before size=" + PeersInfo.getPeers().size());
          PeersInfo.addPeers(localArrayList);
          Log.v("hitchernetFileTransferService", "after size = " + PeersInfo.getPeers().size());
          if ((FileTransferService.this.activityNotifier != null) && (Globals.isActivityAlive))
          {
            FileTransferService.this.activityNotifier.onPeersFound(localArrayList);
            FileTransferService.this.activityNotifier.showProgress(false);
          }
        }
      }
    }, new WifiP2pManager.DnsSdTxtRecordListener()
    {
      public void onDnsSdTxtRecordAvailable(String paramAnonymousString, Map<String, String> paramAnonymousMap, WifiP2pDevice paramAnonymousWifiP2pDevice)
      {
        FileTransferService.this.logv("record available");
        Log.d("hitchernetFileTransferService", paramAnonymousWifiP2pDevice.deviceName + " is " + (String)paramAnonymousMap.get("available"));
      }
    });
    this.serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
    Log.v("hitchernetFileTransferService", "add service request");
    this.manager.addServiceRequest(this.channel, this.serviceRequest, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Log.v("hitchernetFileTransferService", "service request");
        FileTransferService.this.logv("Failed adding service discovery request");
        FileTransferService.this.isDiscoveryGoingOn = false;
        FileTransferService.this.logv(FileTransferService.this.getErrorMsg(paramAnonymousInt));
        if ((FileTransferService.this.activityNotifier != null) && (Globals.isActivityAlive)) {
          FileTransferService.this.activityNotifier.onFailure(paramAnonymousInt);
        }
        FileTransferService.this.activityNotifier.showProgress(false);
      }
      
      public void onSuccess()
      {
        Log.v("hitchernetFileTransferService", "service request added successfully. Now request for service discovery");
        FileTransferService.this.manager.discoverServices(FileTransferService.this.channel, new WifiP2pManager.ActionListener()
        {
          public void onFailure(int paramAnonymous2Int)
          {
            FileTransferService.this.logv("Service discovery failed");
            FileTransferService.this.isDiscoveryGoingOn = false;
            FileTransferService.this.logv(FileTransferService.this.getErrorMsg(paramAnonymous2Int));
            if ((FileTransferService.this.activityNotifier != null) && (Globals.isActivityAlive)) {
              FileTransferService.this.activityNotifier.onFailure(paramAnonymous2Int);
            }
          }
          
          public void onSuccess()
          {
            FileTransferService.this.logv("successfully added");
            FileTransferService.this.logv("Service discovery initiated");
          }
        });
      }
    });
  }
  
  private PendingIntent getContentIntent()
  {
    Intent localIntent = new Intent(this, WiFiDirectActivity.class);
    localIntent.setAction("notificationClicked");
    return PendingIntent.getActivity(this, 0, localIntent, 134217728);
  }
  
  private PendingIntent getDeleteIntent(int paramInt)
  {
    Intent localIntent = new Intent(this, TestBroadcast.class);
    localIntent.putExtra("notificationId", paramInt);
    localIntent.setAction("notification_cancelled");
    return PendingIntent.getBroadcast(this, 0, localIntent, 268435456);
  }
  
  public static String getPath(Context paramContext, Uri paramUri)
    throws URISyntaxException
  {
    if ("content".equalsIgnoreCase(paramUri.getScheme()))
    {
      localObject = new String[1];
      localObject[0] = "_data";
    }
    for (;;)
    {
      try
      {
        localObject = paramContext.getContentResolver().query(paramUri, (String[])localObject, null, null, null);
        int i = ((Cursor)localObject).getColumnIndexOrThrow("_data");
        if (!((Cursor)localObject).moveToFirst()) {
          continue;
        }
        localObject = ((Cursor)localObject).getString(i);
        localObject = localObject;
      }
      catch (Exception localException)
      {
        localObject = null;
        continue;
      }
      return localObject;
      if (!"file".equalsIgnoreCase(paramUri.getScheme())) {
        continue;
      }
      localObject = paramUri.getPath();
    }
  }
  
  private static float getPercent(long paramLong1, long paramLong2)
  {
    return (float)(100.0D * (paramLong1 / paramLong2));
  }
  
  private String getTime(long paramLong)
  {
    long l3 = paramLong / 3600L;
    long l1 = paramLong % 3600L;
    long l2 = l1 / 60L;
    l1 %= 60L;
    if (l3 == 0L) {
      localObject2 = "";
    } else {
      localObject2 = localObject2 + " hr ";
    }
    Object localObject2 = new StringBuilder(String.valueOf(localObject2));
    if (l2 == 0L) {
      localObject1 = "";
    } else {
      localObject1 = localObject1 + " min ";
    }
    Object localObject1 = new StringBuilder(String.valueOf((String)localObject1));
    String str;
    if (l1 == 0L) {
      str = "";
    } else {
      str = str + " sec ";
    }
    return str;
  }
  
  private long insertDb(String paramString1, String paramString2, String paramString3, boolean paramBoolean, String paramString4)
  {
    Time localTime = new Time();
    localTime.setToNow();
    Object localObject = new StringBuilder("inserting to ");
    String str;
    if (!paramBoolean) {
      str = "outbox";
    } else {
      str = "inbox";
    }
    Log.v("hitchernetFileTransferService", str);
    localObject = new HashMap();
    ((HashMap)localObject).put("fileName", paramString1);
    ((HashMap)localObject).put("size", paramString3);
    if (!paramBoolean) {
      str = "0";
    } else {
      str = "1";
    }
    ((HashMap)localObject).put("in_inbox", str);
    ((HashMap)localObject).put("progressPercent", "0");
    ((HashMap)localObject).put("createdAt", localTime.format("%Y-%m-%d     %H:%M:%S"));
    ((HashMap)localObject).put("deviceName", paramString4);
    ((HashMap)localObject).put("speedMbps", "");
    ((HashMap)localObject).put("timeRemaining", "");
    ((HashMap)localObject).put("filePath", paramString2);
    return this.dbHelper.insertData((HashMap)localObject);
  }
  
  private void startClientMeta()
  {
    Log.v("hitchernetFileTransferService", "Determine whether to send info to server or not");
    if ((Globals.thisDeviceIP == null) || (!IP.getIPAddress1().equals(Globals.thisDeviceIP)))
    {
      Log.v("hitchernetFileTransferService", "ip is changed, so send to server");
      Globals.thisDeviceIP = IP.getIPAddress1();
      Intent localIntent = new Intent();
      localIntent.putExtra("ip_address", Globals.thisDeviceIP);
      localIntent.putExtra("go_host", "192.168.49.1");
      localIntent.putExtra("go_port", PORT);
      localIntent.putExtra("mac_address", Globals.thisDeviceAddress);
      if (!this.clientMetaRunning)
      {
        Log.v("hitchernetFileTransferService", "trying to send meta info");
        Object localObject2;
        Object localObject1;
        if (Build.VERSION.SDK_INT < 11)
        {
          localObject2 = new AsyncClientMeta(null);
          localObject1 = new Intent[1];
          localObject1[0] = localIntent;
          ((AsyncClientMeta)localObject2).execute((Object[])localObject1);
        }
        else
        {
          localObject1 = new AsyncClientMeta(null);
          Executor localExecutor = AsyncTask.THREAD_POOL_EXECUTOR;
          localObject2 = new Intent[1];
          localObject2[0] = localIntent;
          ((AsyncClientMeta)localObject1).executeOnExecutor(localExecutor, (Object[])localObject2);
        }
      }
    }
  }
  
  private void startServer()
  {
    if (this.serverRunning)
    {
      Log.v("hitchernetFileTransferService", "Server is already running");
    }
    else
    {
      Log.v("hitchernetFileTransferService", "server started at " + IP.getIPAddress1());
      new AsyncMainServer(null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
    }
  }
  
  public void cancelConnect()
  {
    this.manager.cancelConnect(this.channel, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Toast.makeText(FileTransferService.this, "Connection Cancellation Failed", 0).show();
      }
      
      public void onSuccess()
      {
        Toast.makeText(FileTransferService.this, "Connection Cancelled", 0).show();
      }
    });
  }
  
  public void cancelDisconnect()
  {
    logv("cancel disconnect. No actions set for now");
  }
  
  public void connect(WifiP2pConfig paramWifiP2pConfig)
  {
    logv("connect");
    this.manager.connect(this.channel, paramWifiP2pConfig, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Log.v("hitchernetFileTransferService", "failed connection");
        Toast.makeText(FileTransferService.this, "Connection failed. Retry.", 0).show();
      }
      
      public void onSuccess()
      {
        Log.v("hitchernetFileTransferService", "connection successful");
      }
    });
  }
  
  public void connectP2p(WifiP2pDevice paramWifiP2pDevice)
  {
    logv("connect p2p");
    WifiP2pConfig localWifiP2pConfig = new WifiP2pConfig();
    localWifiP2pConfig.deviceAddress = paramWifiP2pDevice.deviceAddress;
    localWifiP2pConfig.wps.setup = 0;
    this.manager.connect(this.channel, localWifiP2pConfig, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Log.v("hitchernetFileTransferService", "failed connecting to service");
        Toast.makeText(FileTransferService.this, "Failed to connect with device", 1).show();
        if ((FileTransferService.this.activityNotifier != null) && (Globals.isActivityAlive)) {
          FileTransferService.this.activityNotifier.onConnectionTryFailure(paramAnonymousInt);
        }
        Log.v("hitchernetFileTransferService", FileTransferService.this.getErrorMsg(paramAnonymousInt));
      }
      
      public void onSuccess()
      {
        Log.v("hitchernetFileTransferService", "connecting to service");
      }
    });
  }
  
  public void connected(WifiP2pInfo paramWifiP2pInfo)
  {
    if (!paramWifiP2pInfo.groupFormed) {
      Log.v("hitchernetFileTransferService", "group not formed");
    }
    String str = IP.getIPAddress1();
    if ((str != null) && (!str.trim().equalsIgnoreCase("")))
    {
      startServer();
      if (!paramWifiP2pInfo.isGroupOwner) {
        startClientMeta();
      }
    }
    else
    {
      Log.v("hitchernetFileTransferService", "ip address is not available, ignore the ");
    }
  }
  
  public boolean copyFile(long paramLong1, InputStream paramInputStream, OutputStream paramOutputStream, long paramLong2, boolean paramBoolean, String paramString, Notifier paramNotifier, ProgressBehaviourListener paramProgressBehaviourListener)
  {
    long l2 = System.currentTimeMillis();
    this.isTransferOngoing = true;
    byte[] arrayOfByte = new byte[SIZE];
    Integer localInteger = Integer.valueOf(0);
    long l1 = 0L;
    float f1 = 0.0F;
    int j = 0;
    try
    {
      Log.d("hitchernetFileTransferService", "File write initiated");
      Time localTime1 = new Time();
      localTime1.setToNow();
      long l3 = localTime1.toMillis(true);
      long l5 = l3;
      paramNotifier.notifier(paramLong1, 0.0D, paramString, false, paramBoolean, 0.0F, "");
      if (paramProgressBehaviourListener != null)
      {
        paramProgressBehaviourListener.updateDialog(paramLong1, 0.0D, paramString, true, paramBoolean, 0.0F, "");
        break label690;
        for (;;)
        {
          int k = paramInputStream.read(arrayOfByte);
          if (k == -1)
          {
            l1 = System.currentTimeMillis() - l2;
            Log.v("hitchernetFileTransferService", "--------------------------------------------");
            Log.v("hitchernetFileTransferService", "tcp buffer=" + TCP_BUFFER_SIZE);
            Log.v("hitchernetFileTransferService", "input buffer=" + BUFFER_INPUT_OUTPUT_STREAM_SIZE);
            Log.v("hitchernetFileTransferService", "---------------size =" + SIZE + "----------------------------------------");
            Log.v("hitchernetFileTransferService", "use tcp buffer=" + USE_TCP_BUFFER);
            Log.v("hitchernetFileTransferService", "Time taken=" + l1 / 1000L + " s");
            paramNotifier.notifier(paramLong1, 100.0D, paramString, true, paramBoolean, f1, "");
            if (paramProgressBehaviourListener != null) {
              paramProgressBehaviourListener.updateDialog(paramLong1, 100.0D, paramString, true, paramBoolean, f1, "");
            }
            paramOutputStream.close();
            paramInputStream.close();
            Log.v("hitchernetFileTransferService", "----------------total size in bytes transferred" + paramLong2 + "----------------");
            if (paramProgressBehaviourListener != null) {
              paramProgressBehaviourListener.dismisDialog();
            }
            this.isTransferOngoing = false;
            boolean bool = true;
            return bool;
          }
          localInteger = Integer.valueOf(k + localInteger.intValue());
          Object localObject3;
          localObject3 += k;
          Object localObject1;
          localObject1 += k;
          paramOutputStream.write(arrayOfByte, 0, k);
          double d = getPercent(localInteger.intValue(), paramLong2);
          Time localTime2 = new Time();
          localTime2.setToNow();
          long l7 = localTime2.toMillis(true);
          long l8 = (l7 - l3) / 1000L;
          if (l8 >= 1L)
          {
            l3 = l7;
            float f2 = 8.0F * (float)l6 / (1024.0F * (1024.0F * (float)l8));
            j++;
            Object localObject2;
            localObject2 += f2;
            (f3 / j);
            l6 = (l7 - l5) / 1000L;
            float f4 = (float)(8L * l4) / (float)(1048576L * l6);
            String str = getTime(((float)(8L * (paramLong2 - localInteger.intValue()) / 1048576L) / f2));
            l6 = 0L;
            if (paramNotifier != null)
            {
              if (paramNotifier != null) {
                paramNotifier.notifier(paramLong1, d, paramString, false, paramBoolean, f4, str);
              }
              if (paramProgressBehaviourListener != null) {
                paramProgressBehaviourListener.updateDialog(paramLong1, d, paramString, false, paramBoolean, f4, str);
              }
            }
          }
          l1 += 1L;
        }
      }
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        Log.d("hitchernetFileTransferService", localIOException.toString());
        this.isTransferOngoing = false;
        int i = 0;
        continue;
        label690:
        long l6 = 0L;
        float f3 = 0.0F;
        long l4 = 0L;
      }
    }
  }
  
  public void disconnect()
  {
    Log.v("hitchernetFileTransferService", "inside disconnect");
    this.manager.removeGroup(this.channel, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Log.d("hitchernetFileTransferService", "Disconnect failed. Reason :" + paramAnonymousInt);
      }
      
      public void onSuccess() {}
    });
  }
  
  public String getErrorMsg(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default: 
      str = "Error Message: " + "not known";
      break;
    case 0: 
      str = "Error Message: " + "Passed with onFailure(int). Indicates that the operation failed due to an internal error.";
      break;
    case 1: 
      str = "Error Message: " + "not supported";
      break;
    case 2: 
      str = "Error Message: " + " the framework is busy and unable to service the request";
      break;
    case 3: 
      str = "Error Message: " + "Passed with onFailure(int). Indicates that the discoverServices(WifiP2pManager.Channel, WifiP2pManager.ActionListener) failed because no service requests are added. Use addServiceRequest(WifiP2pManager.Channel, WifiP2pServiceRequest, WifiP2pManager.ActionListener) to add a service request.";
    }
    return str;
  }
  
  public void initializeManagerAndChannel()
  {
    
    if (this.serverSocket != null) {}
    try
    {
      this.serverSocket.close();
      if ((this.manager != null) && (this.channel != null)) {
        this.manager.removeGroup(this.channel, null);
      }
      this.manager = ((WifiP2pManager)getSystemService("wifip2p"));
      this.channel = this.manager.initialize(this, getMainLooper(), null);
      return;
    }
    catch (IOException localIOException)
    {
      for (;;)
      {
        localIOException.printStackTrace();
      }
    }
  }
  
  public boolean isWifiP2PEnabled()
  {
    return this.isWifiP2pEnabled;
  }
  
  public void logv(String paramString)
  {
    Log.v("hitchernet" + getClass().getSimpleName(), paramString);
  }
  
  public IBinder onBind(Intent paramIntent)
  {
    Log.v("hitchernetFileTransferService", "on bind");
    return this.mBinder;
  }
  
  public void onChannelDisconnected()
  {
    logv("Channed disconnected");
    if ((this.manager == null) || (this.retryChannel))
    {
      Toast.makeText(this, "Severe! Channel is probably lost premanently. Try Disable/Re-Enable P2P.", 1).show();
    }
    else
    {
      if (this.activityNotifier != null) {
        this.activityNotifier.onResetData();
      }
      this.retryChannel = true;
      this.manager.initialize(this, getMainLooper(), this);
    }
  }
  
  public void onConnectionInfoAvailable(WifiP2pInfo paramWifiP2pInfo)
  {
    if (this.activityNotifier != null) {
      this.activityNotifier.onConnectionInfoAvailable(paramWifiP2pInfo);
    }
    connected(paramWifiP2pInfo);
  }
  
  public void onCreate()
  {
    super.onCreate();
    Log.v("hitchernetFileTransferService", "service created");
    initializeManagerAndChannel();
    this.notificationManager = ((NotificationManager)getSystemService("notification"));
    this.mBuilder = new NotificationCompat.Builder(this);
    addActionsToIntentFilters(this.wifiDirectIntentFilter);
    this.dbHelper = DbHelper.getInstance(this);
    this.directReceiver = new WiFiDirectBroadcastReceiver(this.manager, this.channel, this);
    registerReceiver(this.directReceiver, this.wifiDirectIntentFilter);
    IntentFilter localIntentFilter = new IntentFilter();
    localIntentFilter.addAction("notification_cancelled");
    localIntentFilter.addAction("notificationClicked");
    LocalBroadcastManager.getInstance(this).registerReceiver(this.notificationReceiver, localIntentFilter);
    if (isWifiP2PEnabled())
    {
      Log.v("hitchernetFileTransferService", "p2p is enabled");
      startRegistrationAndDiscovery();
    }
  }
  
  public void onDestroy()
  {
    logv("close server socket");
    if (this.serverSocket != null) {}
    try
    {
      this.serverSocket.close();
      label21:
      unregisterReceiver(this.directReceiver);
      LocalBroadcastManager.getInstance(this).unregisterReceiver(this.notificationReceiver);
      logv("destroyed");
      super.onDestroy();
      return;
    }
    catch (IOException localIOException)
    {
      break label21;
    }
  }
  
  public void onPeersAvailable(WifiP2pDeviceList paramWifiP2pDeviceList)
  {
    PeersInfo.updatePeerStatus(paramWifiP2pDeviceList.getDeviceList());
    if ((this.activityNotifier != null) && (Globals.isActivityAlive)) {
      this.activityNotifier.onPeersAvailable(paramWifiP2pDeviceList);
    }
  }
  
  public void rename(String paramString, WifiP2pManager.ActionListener paramActionListener)
  {
    try
    {
      Object localObject = new Class[3];
      localObject[0] = WifiP2pManager.Channel.class;
      localObject[1] = String.class;
      localObject[2] = WifiP2pManager.ActionListener.class;
      localObject = this.manager.getClass().getDeclaredMethod("setDeviceName", (Class[])localObject);
      ((Method)localObject).setAccessible(true);
      WifiP2pManager localWifiP2pManager = this.manager;
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = this.channel;
      arrayOfObject[1] = paramString;
      arrayOfObject[2] = paramActionListener;
      ((Method)localObject).invoke(localWifiP2pManager, arrayOfObject);
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      for (;;)
      {
        localNoSuchMethodException.printStackTrace();
      }
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      for (;;)
      {
        localIllegalArgumentException.printStackTrace();
      }
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      for (;;)
      {
        localIllegalAccessException.printStackTrace();
      }
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      for (;;)
      {
        localInvocationTargetException.printStackTrace();
      }
    }
  }
  
  public void resetData()
  {
    
    if ((this.activityNotifier != null) && (Globals.isActivityAlive)) {
      this.activityNotifier.onResetData();
    }
  }
  
  public void setActivityNotifier(ActivityNotifier paramActivityNotifier)
  {
    this.activityNotifier = paramActivityNotifier;
    logv("activity notifier is set");
    if (!isWifiP2PEnabled())
    {
      paramActivityNotifier.onWDDisabled(true);
      paramActivityNotifier.showProgress(false);
    }
  }
  
  public void setWifiP2PEnabled(boolean paramBoolean)
  {
    this.isWifiP2pEnabled = paramBoolean;
    if (paramBoolean)
    {
      if ((this.activityNotifier != null) && (Globals.isActivityAlive)) {
        this.activityNotifier.onWDDisabled(false);
      }
      Log.v("hitchernetFileTransferService", "enabled. now initiate connection");
      startRegistrationAndDiscovery();
    }
    else if ((this.activityNotifier != null) && (Globals.isActivityAlive))
    {
      this.activityNotifier.onWDDisabled(true);
      this.activityNotifier.showProgress(false);
    }
  }
  
  public void showNotification(NotificationManager paramNotificationManager, NotificationCompat.Builder paramBuilder, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2, int paramInt)
  {
    if (this.notifyData.containsKey(Integer.valueOf(paramInt)))
    {
      if (!((Boolean)this.notifyData.get(Integer.valueOf(paramInt))).booleanValue()) {
        return;
      }
    }
    else {
      this.notifyData.put(Integer.valueOf(paramInt), Boolean.valueOf(true));
    }
    String str;
    if (!paramBoolean2) {
      str = "Transferring";
    } else {
      str = "Downloading";
    }
    DecimalFormat localDecimalFormat = new DecimalFormat("0.00");
    Object localObject = "";
    if (paramString2 != null) {
      if (!paramString2.equalsIgnoreCase("")) {
        localObject = paramString2 + " remaining";
      } else {
        localObject = "";
      }
    }
    localObject = localDecimalFormat.format(paramDouble) + "% @ " + localDecimalFormat.format(paramFloat) + " Mbps  " + (String)localObject;
    if (paramBoolean1) {
      localObject = "File Transfer Completed";
    }
    paramBuilder.setContentTitle(str + " " + paramString1);
    if (paramDouble == 0.0D) {
      localObject = "";
    }
    paramBuilder.setContentText((CharSequence)localObject);
    paramBuilder.setSmallIcon(2130837568);
    localObject = getResources();
    int i;
    if (!paramBoolean2) {
      i = 2130837590;
    } else {
      i = 2130837572;
    }
    paramBuilder.setLargeIcon(BitmapFactory.decodeResource((Resources)localObject, i));
    paramBuilder.setProgress(100, (int)paramDouble, false);
    paramBuilder.setDeleteIntent(getDeleteIntent(paramInt));
    Log.v("hitchernetFileTransferService", "delete intent is set");
    paramNotificationManager.notify(paramInt, paramBuilder.build());
  }
  
  public void startFileTransfer(List<LocalFile> paramList, String paramString)
  {
    Object localObject1 = paramList.iterator();
    while (((Iterator)localObject1).hasNext())
    {
      Object localObject5 = ((LocalFile)((Iterator)localObject1).next()).getAbsolutePath();
      Object localObject4 = IP.getIPAddress1();
      Log.d("hitchernetFileTransferService", "LOCAL IP -> " + (String)localObject4);
      Log.d("hitchernetFileTransferService", "IP SERVER -> 192.168.49.1");
      File localFile = new File((String)localObject5);
      fileName = localFile.getName();
      Log.d("hitchernetFileTransferService", "File name----------- " + fileName);
      if (localFile.isFile())
      {
        Object localObject2 = MimeTypeMap.getFileExtensionFromUrl((String)localObject5);
        Object localObject3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)localObject2);
        long l = localFile.length();
        if (localObject3 == null) {
          localObject3 = "*/*";
        }
        Log.d("hitchernetFileTransferService", "File type ------ " + (String)localObject2);
        Log.d("hitchernetFileTransferService", "File mime ------ " + (String)localObject3);
        Log.v("hitchernetFileTransferService", "progress behaviour listener show dialog");
        localObject2 = new Intent();
        ((Intent)localObject2).putExtra("MAC_ADDRESS", Globals.thisDeviceAddress);
        ((Intent)localObject2).putExtra("target Device address", paramString);
        ((Intent)localObject2).putExtra("file_url", (String)localObject5);
        ((Intent)localObject2).putExtra("HitcherNet", fileName);
        ((Intent)localObject2).putExtra("filesize", l);
        ((Intent)localObject2).putExtra("image/jpeg", (String)localObject3);
        if (!((String)localObject4).equals("192.168.49.1"))
        {
          ((Intent)localObject2).putExtra("go_host", "192.168.49.1");
          Log.d("hitchernetFileTransferService", "Case 2: Sending to IP -> 192.168.49.1");
        }
        else
        {
          Log.v("hitchernetFileTransferService", "local ip is equal to server ip");
          Log.v("hitchernetFileTransferService", "just selected device mac=" + paramString);
          localObject3 = new Preferences(this).getIPFromMac(paramString);
          Log.v("hitchernetFileTransferService", "its ip = " + (String)localObject3);
          ((Intent)localObject2).putExtra("go_host", (String)localObject3);
          Log.v("hitchernetFileTransferService", "will try to send to " + (String)localObject3);
          if (((String)localObject3).equals("")) {
            break label514;
          }
        }
        ((Intent)localObject2).putExtra("go_port", PORT);
        Log.v("hitchernetFileTransferService", "send broadcast");
        Log.v("hitchernetFileTransferService", "client is started" + IP.getIPAddress1());
        localObject5 = new AsyncClient(null);
        localObject3 = AsyncTask.THREAD_POOL_EXECUTOR;
        localObject4 = new Intent[1];
        localObject4[0] = localObject2;
        ((AsyncClient)localObject5).executeOnExecutor((Executor)localObject3, (Object[])localObject4);
        continue;
        label514:
        Utils.toast(this, "IP Address couldn't be determined.Please re-connect to send the file");
        return;
      }
      else
      {
        Toast.makeText(this, "Sorry,Please select the file again", 1).show();
        return;
      }
    }
    if (paramList.size() < 2) {
      localObject1 = "Sending file";
    } else {
      localObject1 = "files queued";
    }
    Toast.makeText(this, (CharSequence)localObject1, 1).show();
  }
  
  public void startRegistrationAndDiscovery()
  {
    if ((this.manager != null) && (this.channel != null)) {
      this.manager.removeGroup(this.channel, null);
    }
    if ((this.activityNotifier != null) && (Globals.isActivityAlive))
    {
      this.activityNotifier.showProgress(true);
      this.activityNotifier.onResetData();
    }
    if (this.serviceRequest != null) {
      this.manager.removeServiceRequest(this.channel, this.serviceRequest, new WifiP2pManager.ActionListener()
      {
        public void onFailure(int paramAnonymousInt)
        {
          FileTransferService.this.logv("failed to remove");
        }
        
        public void onSuccess()
        {
          FileTransferService.this.logv("successfully removed");
        }
      });
    }
    if (this.serviceInfo != null) {
      this.manager.removeLocalService(this.channel, this.serviceInfo, new WifiP2pManager.ActionListener()
      {
        public void onFailure(int paramAnonymousInt)
        {
          FileTransferService.this.logv("failed to remove local service");
        }
        
        public void onSuccess()
        {
          FileTransferService.this.logv("local service removed");
        }
      });
    }
    Log.v("hitchernetFileTransferService", "start registration and discovery");
    HashMap localHashMap = new HashMap();
    localHashMap.put("available", "visible");
    this.serviceInfo = WifiP2pDnsSdServiceInfo.newInstance("hitchernet", "_presence._tcp", localHashMap);
    Log.v("hitchernetFileTransferService", " add Local Service ");
    if ((this.activityNotifier != null) && (Globals.isActivityAlive)) {
      this.activityNotifier.showProgress(true);
    }
    this.isDiscoveryGoingOn = true;
    this.manager.addLocalService(this.channel, this.serviceInfo, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Log.v("hitchernetFileTransferService", "Failed to add local service");
        FileTransferService.this.logv("Failed to add a service");
        FileTransferService.this.isDiscoveryGoingOn = false;
        if ((FileTransferService.this.activityNotifier != null) && (Globals.isActivityAlive))
        {
          FileTransferService.this.activityNotifier.onFailure(paramAnonymousInt);
          FileTransferService.this.activityNotifier.showProgress(false);
        }
      }
      
      public void onSuccess()
      {
        FileTransferService.this.logv("Local Service Successfully added. Now discover service");
        FileTransferService.this.discoverService();
      }
    });
  }
  
  public void updateDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    if ((this.activityNotifier != null) && (Globals.isActivityAlive)) {
      this.activityNotifier.updateDevice(paramWifiP2pDevice);
    }
  }
  
  public static abstract interface ActivityNotifier
  {
    public abstract void onConnectionInfoAvailable(WifiP2pInfo paramWifiP2pInfo);
    
    public abstract void onConnectionTryFailure(int paramInt);
    
    public abstract void onFailure(int paramInt);
    
    public abstract void onPeersAvailable(WifiP2pDeviceList paramWifiP2pDeviceList);
    
    public abstract void onPeersFound(Collection<WifiP2pDevice> paramCollection);
    
    public abstract void onResetData();
    
    public abstract void onWDDisabled(boolean paramBoolean);
    
    public abstract void showProgress(boolean paramBoolean);
    
    public abstract void updateDevice(WifiP2pDevice paramWifiP2pDevice);
  }
  
  private class AsyncClient
    extends AsyncTask<Intent, Void, Void>
  {
    String fileName;
    boolean isCompleted;
    boolean isDownloading;
    boolean isSuccessful = false;
    int notifiyId;
    double percent;
    long rowId;
    private boolean sendMessage = true;
    float speedMbps;
    String targetDeviceMac;
    String thisDeviceMac;
    String timeRemaining;
    
    private AsyncClient() {}
    
    /* Error */
    private void clientTask(Intent paramIntent)
    {
      // Byte code:
      //   0: ldc 52
      //   2: ldc 54
      //   4: invokestatic 60	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   7: pop
      //   8: aload_0
      //   9: aload_1
      //   10: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   13: ldc 68
      //   15: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   18: putfield 76	com/kharybdis/hitchernet/FileTransferService$AsyncClient:thisDeviceMac	Ljava/lang/String;
      //   21: aload_0
      //   22: aload_1
      //   23: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   26: ldc 78
      //   28: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   31: putfield 80	com/kharybdis/hitchernet/FileTransferService$AsyncClient:targetDeviceMac	Ljava/lang/String;
      //   34: aload_1
      //   35: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   38: ldc 82
      //   40: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   43: astore 6
      //   45: aload_1
      //   46: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   49: ldc 84
      //   51: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   54: astore 9
      //   56: aload_1
      //   57: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   60: ldc 86
      //   62: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   65: astore 5
      //   67: aload_1
      //   68: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   71: ldc 88
      //   73: invokevirtual 74	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   76: astore 7
      //   78: aload_1
      //   79: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   82: ldc 90
      //   84: invokevirtual 94	android/os/Bundle:getLong	(Ljava/lang/String;)J
      //   87: lstore_3
      //   88: new 96	java/net/Socket
      //   91: dup
      //   92: invokespecial 97	java/net/Socket:<init>	()V
      //   95: astore_2
      //   96: aload_1
      //   97: invokevirtual 66	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   100: ldc 99
      //   102: invokevirtual 103	android/os/Bundle:getInt	(Ljava/lang/String;)I
      //   105: istore 8
      //   107: ldc 52
      //   109: new 105	java/lang/StringBuilder
      //   112: dup
      //   113: ldc 107
      //   115: invokespecial 110	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   118: aload 9
      //   120: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   123: ldc 116
      //   125: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   128: iload 8
      //   130: invokevirtual 119	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
      //   133: invokevirtual 123	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   136: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   139: pop
      //   140: invokestatic 130	com/kharybdis/hitchernet/FileTransferService:access$1	()Z
      //   143: ifeq +10 -> 153
      //   146: aload_2
      //   147: invokestatic 134	com/kharybdis/hitchernet/FileTransferService:access$2	()I
      //   150: invokevirtual 138	java/net/Socket:setSendBufferSize	(I)V
      //   153: aload_2
      //   154: aconst_null
      //   155: invokevirtual 142	java/net/Socket:bind	(Ljava/net/SocketAddress;)V
      //   158: aload_2
      //   159: new 144	java/net/InetSocketAddress
      //   162: dup
      //   163: aload 9
      //   165: iload 8
      //   167: invokespecial 147	java/net/InetSocketAddress:<init>	(Ljava/lang/String;I)V
      //   170: ldc 148
      //   172: invokevirtual 152	java/net/Socket:connect	(Ljava/net/SocketAddress;I)V
      //   175: aload_0
      //   176: iconst_1
      //   177: putfield 41	com/kharybdis/hitchernet/FileTransferService$AsyncClient:sendMessage	Z
      //   180: aload_0
      //   181: aconst_null
      //   182: invokevirtual 156	com/kharybdis/hitchernet/FileTransferService$AsyncClient:publishProgress	([Ljava/lang/Object;)V
      //   185: aload_0
      //   186: aload_0
      //   187: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   190: aload 5
      //   192: aload 6
      //   194: new 105	java/lang/StringBuilder
      //   197: dup
      //   198: lload_3
      //   199: invokestatic 162	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   202: invokespecial 110	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   205: invokevirtual 123	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   208: iconst_0
      //   209: aload_0
      //   210: getfield 80	com/kharybdis/hitchernet/FileTransferService$AsyncClient:targetDeviceMac	Ljava/lang/String;
      //   213: invokestatic 167	com/kharybdis/hitchernet/PeersInfo:getDeviceNameWithMac	(Ljava/lang/String;)Ljava/lang/String;
      //   216: invokestatic 171	com/kharybdis/hitchernet/FileTransferService:access$3	(Lcom/kharybdis/hitchernet/FileTransferService;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZLjava/lang/String;)J
      //   219: putfield 173	com/kharybdis/hitchernet/FileTransferService$AsyncClient:rowId	J
      //   222: ldc 52
      //   224: new 105	java/lang/StringBuilder
      //   227: dup
      //   228: ldc 175
      //   230: invokespecial 110	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   233: aload_2
      //   234: invokevirtual 178	java/net/Socket:isConnected	()Z
      //   237: invokevirtual 181	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   240: invokevirtual 123	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   243: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   246: pop
      //   247: new 183	java/io/BufferedOutputStream
      //   250: dup
      //   251: aload_2
      //   252: invokevirtual 187	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   255: invokestatic 190	com/kharybdis/hitchernet/FileTransferService:access$4	()I
      //   258: invokespecial 193	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;I)V
      //   261: astore 8
      //   263: aconst_null
      //   264: astore 9
      //   266: new 195	java/io/BufferedWriter
      //   269: dup
      //   270: new 197	java/io/OutputStreamWriter
      //   273: dup
      //   274: aload 8
      //   276: invokespecial 200	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
      //   279: invokespecial 203	java/io/BufferedWriter:<init>	(Ljava/io/Writer;)V
      //   282: astore 10
      //   284: ldc 52
      //   286: new 105	java/lang/StringBuilder
      //   289: dup
      //   290: ldc 205
      //   292: invokespecial 110	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   295: aload 5
      //   297: invokevirtual 114	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   300: invokevirtual 123	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   303: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   306: pop
      //   307: aload 10
      //   309: ldc 207
      //   311: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   314: aload 10
      //   316: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   319: aload 10
      //   321: aload_0
      //   322: getfield 76	com/kharybdis/hitchernet/FileTransferService$AsyncClient:thisDeviceMac	Ljava/lang/String;
      //   325: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   328: aload 10
      //   330: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   333: aload 10
      //   335: getstatic 218	com/kharybdis/hitchernet/Globals:thisDeviceName	Ljava/lang/String;
      //   338: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   341: aload 10
      //   343: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   346: aload 10
      //   348: aload 5
      //   350: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   353: aload 10
      //   355: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   358: aload 10
      //   360: lload_3
      //   361: invokestatic 162	java/lang/String:valueOf	(J)Ljava/lang/String;
      //   364: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   367: aload 10
      //   369: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   372: aload 10
      //   374: aload 7
      //   376: invokevirtual 210	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   379: aload 10
      //   381: invokevirtual 213	java/io/BufferedWriter:newLine	()V
      //   384: aload 10
      //   386: invokevirtual 221	java/io/BufferedWriter:flush	()V
      //   389: ldc 52
      //   391: ldc 223
      //   393: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   396: pop
      //   397: new 225	java/io/BufferedInputStream
      //   400: dup
      //   401: new 227	java/io/FileInputStream
      //   404: dup
      //   405: aload 6
      //   407: invokespecial 228	java/io/FileInputStream:<init>	(Ljava/lang/String;)V
      //   410: invokestatic 190	com/kharybdis/hitchernet/FileTransferService:access$4	()I
      //   413: invokespecial 231	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;I)V
      //   416: astore 6
      //   418: aload 6
      //   420: astore 9
      //   422: aload_0
      //   423: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   426: aload_0
      //   427: getfield 173	com/kharybdis/hitchernet/FileTransferService$AsyncClient:rowId	J
      //   430: aload 9
      //   432: aload 8
      //   434: lload_3
      //   435: iconst_0
      //   436: aload 5
      //   438: new 7	com/kharybdis/hitchernet/FileTransferService$AsyncClient$1
      //   441: dup
      //   442: aload_0
      //   443: invokespecial 234	com/kharybdis/hitchernet/FileTransferService$AsyncClient$1:<init>	(Lcom/kharybdis/hitchernet/FileTransferService$AsyncClient;)V
      //   446: aconst_null
      //   447: invokevirtual 238	com/kharybdis/hitchernet/FileTransferService:copyFile	(JLjava/io/InputStream;Ljava/io/OutputStream;JZLjava/lang/String;Lcom/kharybdis/hitchernet/FileTransferService$Notifier;Lcom/kharybdis/hitchernet/FileTransferService$ProgressBehaviourListener;)Z
      //   450: pop
      //   451: ldc 52
      //   453: ldc 240
      //   455: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   458: pop
      //   459: aload_0
      //   460: iconst_1
      //   461: putfield 39	com/kharybdis/hitchernet/FileTransferService$AsyncClient:isSuccessful	Z
      //   464: aload_2
      //   465: ifnull +16 -> 481
      //   468: aload_0
      //   469: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   472: ldc 242
      //   474: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   477: aload_2
      //   478: invokevirtual 248	java/net/Socket:close	()V
      //   481: return
      //   482: astore 6
      //   484: ldc 52
      //   486: aload 6
      //   488: invokevirtual 249	java/io/FileNotFoundException:toString	()Ljava/lang/String;
      //   491: invokestatic 126	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   494: pop
      //   495: aload_0
      //   496: iconst_0
      //   497: putfield 39	com/kharybdis/hitchernet/FileTransferService$AsyncClient:isSuccessful	Z
      //   500: goto -78 -> 422
      //   503: astore_3
      //   504: ldc 52
      //   506: aload_3
      //   507: invokevirtual 252	java/io/IOException:getMessage	()Ljava/lang/String;
      //   510: invokestatic 255	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   513: pop
      //   514: aload_3
      //   515: invokevirtual 258	java/io/IOException:printStackTrace	()V
      //   518: aload_0
      //   519: iconst_0
      //   520: putfield 39	com/kharybdis/hitchernet/FileTransferService$AsyncClient:isSuccessful	Z
      //   523: aload_2
      //   524: ifnull -43 -> 481
      //   527: aload_0
      //   528: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   531: ldc 242
      //   533: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   536: aload_2
      //   537: invokevirtual 248	java/net/Socket:close	()V
      //   540: goto -59 -> 481
      //   543: astore_2
      //   544: aload_0
      //   545: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   548: ldc_w 260
      //   551: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   554: aload_2
      //   555: invokevirtual 258	java/io/IOException:printStackTrace	()V
      //   558: goto -77 -> 481
      //   561: astore_3
      //   562: aload_2
      //   563: ifnull +16 -> 579
      //   566: aload_0
      //   567: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   570: ldc 242
      //   572: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   575: aload_2
      //   576: invokevirtual 248	java/net/Socket:close	()V
      //   579: aload_3
      //   580: athrow
      //   581: astore_2
      //   582: aload_0
      //   583: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   586: ldc_w 260
      //   589: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   592: aload_2
      //   593: invokevirtual 258	java/io/IOException:printStackTrace	()V
      //   596: goto -17 -> 579
      //   599: astore_2
      //   600: aload_0
      //   601: getfield 34	com/kharybdis/hitchernet/FileTransferService$AsyncClient:this$0	Lcom/kharybdis/hitchernet/FileTransferService;
      //   604: ldc_w 260
      //   607: invokevirtual 245	com/kharybdis/hitchernet/FileTransferService:logv	(Ljava/lang/String;)V
      //   610: aload_2
      //   611: invokevirtual 258	java/io/IOException:printStackTrace	()V
      //   614: goto -133 -> 481
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	617	0	this	AsyncClient
      //   0	617	1	paramIntent	Intent
      //   95	442	2	localSocket	Socket
      //   543	33	2	localIOException1	IOException
      //   581	12	2	localIOException2	IOException
      //   599	12	2	localIOException3	IOException
      //   87	348	3	l	long
      //   503	12	3	localIOException4	IOException
      //   561	19	3	localObject1	Object
      //   65	372	5	str1	String
      //   43	376	6	localObject2	Object
      //   482	5	6	localFileNotFoundException	java.io.FileNotFoundException
      //   76	299	7	str2	String
      //   105	61	8	i	int
      //   261	172	8	localBufferedOutputStream	java.io.BufferedOutputStream
      //   54	377	9	localObject3	Object
      //   282	103	10	localBufferedWriter	java.io.BufferedWriter
      // Exception table:
      //   from	to	target	type
      //   397	418	482	java/io/FileNotFoundException
      //   107	397	503	java/io/IOException
      //   397	418	503	java/io/IOException
      //   422	464	503	java/io/IOException
      //   484	500	503	java/io/IOException
      //   527	540	543	java/io/IOException
      //   107	397	561	finally
      //   397	418	561	finally
      //   422	464	561	finally
      //   484	500	561	finally
      //   504	523	561	finally
      //   566	579	581	java/io/IOException
      //   468	481	599	java/io/IOException
    }
    
    protected Void doInBackground(Intent... paramVarArgs)
    {
      Log.v("hitchernetFileTransferService", "in background");
      clientTask(paramVarArgs[0]);
      return null;
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      Intent localIntent = new Intent("com.kharybdis.hitchernet.DISMISS_DIALOG");
      FileTransferService.this.sendStickyBroadcast(localIntent);
      Log.v("hitchernetFileTransferService", "try to dismis dialog");
      if (!this.isSuccessful) {
        Utils.toast(FileTransferService.this, "Sorry, Please send again ");
      }
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      FileTransferService localFileTransferService = FileTransferService.this;
      localFileTransferService.notificationCounter = (1 + localFileTransferService.notificationCounter);
      this.notifiyId = FileTransferService.this.notificationCounter;
    }
    
    protected void onProgressUpdate(Void... paramVarArgs)
    {
      PeersInfo.updatePeerPercent(true, this.targetDeviceMac, (int)this.percent);
      FileTransferService.this.dbHelper.updateRowWithId(this.rowId, (int)this.percent, this.speedMbps, this.timeRemaining);
      Intent localIntent = new Intent("intent_progress_notify");
      localIntent.putExtra("percent", this.percent);
      localIntent.putExtra("fileName", this.fileName);
      localIntent.putExtra("isCompleted", this.isCompleted);
      localIntent.putExtra("isDownloading", this.isDownloading);
      localIntent.putExtra("speedMbps", this.speedMbps);
      localIntent.putExtra("timeRemaining", this.timeRemaining);
      localIntent.setClass(FileTransferService.this.getApplicationContext(), WiFiDirectActivity.class);
      LocalBroadcastManager.getInstance(FileTransferService.this.getApplicationContext()).sendBroadcast(localIntent);
      FileTransferService.this.showNotification(FileTransferService.this.notificationManager, FileTransferService.this.mBuilder, this.percent, this.fileName, this.isCompleted, this.isDownloading, this.speedMbps, this.timeRemaining, this.notifiyId);
    }
  }
  
  private class AsyncClientMeta
    extends AsyncTask<Intent, Void, Void>
  {
    private AsyncClientMeta() {}
    
    /* Error */
    private boolean clientTask(Intent paramIntent)
    {
      // Byte code:
      //   0: ldc 26
      //   2: ldc 28
      //   4: invokestatic 34	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   7: pop
      //   8: aload_1
      //   9: invokevirtual 40	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   12: ldc 42
      //   14: invokevirtual 48	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   17: astore 4
      //   19: ldc 26
      //   21: new 50	java/lang/StringBuilder
      //   24: dup
      //   25: ldc 52
      //   27: invokespecial 55	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   30: aload 4
      //   32: invokevirtual 59	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
      //   35: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   38: invokestatic 34	android/util/Log:v	(Ljava/lang/String;Ljava/lang/String;)I
      //   41: pop
      //   42: aload_1
      //   43: invokevirtual 40	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   46: ldc 65
      //   48: invokevirtual 48	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   51: astore_3
      //   52: aload_1
      //   53: invokevirtual 40	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   56: ldc 67
      //   58: invokevirtual 48	android/os/Bundle:getString	(Ljava/lang/String;)Ljava/lang/String;
      //   61: astore 6
      //   63: new 69	java/net/Socket
      //   66: dup
      //   67: invokespecial 70	java/net/Socket:<init>	()V
      //   70: astore_2
      //   71: aload_1
      //   72: invokevirtual 40	android/content/Intent:getExtras	()Landroid/os/Bundle;
      //   75: ldc 72
      //   77: invokevirtual 76	android/os/Bundle:getInt	(Ljava/lang/String;)I
      //   80: istore 5
      //   82: ldc 26
      //   84: ldc 78
      //   86: invokestatic 81	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   89: pop
      //   90: invokestatic 85	com/kharybdis/hitchernet/FileTransferService:access$1	()Z
      //   93: ifeq +10 -> 103
      //   96: aload_2
      //   97: invokestatic 89	com/kharybdis/hitchernet/FileTransferService:access$2	()I
      //   100: invokevirtual 93	java/net/Socket:setSendBufferSize	(I)V
      //   103: aload_2
      //   104: aconst_null
      //   105: invokevirtual 97	java/net/Socket:bind	(Ljava/net/SocketAddress;)V
      //   108: aload_2
      //   109: new 99	java/net/InetSocketAddress
      //   112: dup
      //   113: aload 6
      //   115: iload 5
      //   117: invokespecial 102	java/net/InetSocketAddress:<init>	(Ljava/lang/String;I)V
      //   120: sipush 5000
      //   123: invokevirtual 106	java/net/Socket:connect	(Ljava/net/SocketAddress;I)V
      //   126: ldc 26
      //   128: new 50	java/lang/StringBuilder
      //   131: dup
      //   132: ldc 108
      //   134: invokespecial 55	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
      //   137: aload_2
      //   138: invokevirtual 111	java/net/Socket:isConnected	()Z
      //   141: invokevirtual 114	java/lang/StringBuilder:append	(Z)Ljava/lang/StringBuilder;
      //   144: invokevirtual 63	java/lang/StringBuilder:toString	()Ljava/lang/String;
      //   147: invokestatic 81	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   150: pop
      //   151: new 116	java/io/BufferedOutputStream
      //   154: dup
      //   155: aload_2
      //   156: invokevirtual 120	java/net/Socket:getOutputStream	()Ljava/io/OutputStream;
      //   159: invokestatic 123	com/kharybdis/hitchernet/FileTransferService:access$4	()I
      //   162: invokespecial 126	java/io/BufferedOutputStream:<init>	(Ljava/io/OutputStream;I)V
      //   165: astore 6
      //   167: new 128	java/io/BufferedWriter
      //   170: dup
      //   171: new 130	java/io/OutputStreamWriter
      //   174: dup
      //   175: aload 6
      //   177: invokespecial 133	java/io/OutputStreamWriter:<init>	(Ljava/io/OutputStream;)V
      //   180: invokestatic 123	com/kharybdis/hitchernet/FileTransferService:access$4	()I
      //   183: invokespecial 136	java/io/BufferedWriter:<init>	(Ljava/io/Writer;I)V
      //   186: astore 5
      //   188: aload 5
      //   190: ldc 138
      //   192: invokevirtual 141	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   195: aload 5
      //   197: invokevirtual 144	java/io/BufferedWriter:newLine	()V
      //   200: aload 5
      //   202: aload_3
      //   203: invokevirtual 141	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   206: aload 5
      //   208: invokevirtual 144	java/io/BufferedWriter:newLine	()V
      //   211: aload 5
      //   213: aload 4
      //   215: invokevirtual 141	java/io/BufferedWriter:write	(Ljava/lang/String;)V
      //   218: aload 5
      //   220: invokevirtual 144	java/io/BufferedWriter:newLine	()V
      //   223: aload 5
      //   225: invokevirtual 147	java/io/BufferedWriter:flush	()V
      //   228: aload 6
      //   230: invokevirtual 150	java/io/BufferedOutputStream:close	()V
      //   233: iconst_1
      //   234: istore_3
      //   235: ldc 26
      //   237: ldc 152
      //   239: invokestatic 81	android/util/Log:d	(Ljava/lang/String;Ljava/lang/String;)I
      //   242: pop
      //   243: aload_2
      //   244: ifnull +14 -> 258
      //   247: aload_2
      //   248: invokevirtual 111	java/net/Socket:isConnected	()Z
      //   251: ifeq +7 -> 258
      //   254: aload_2
      //   255: invokevirtual 153	java/net/Socket:close	()V
      //   258: iload_3
      //   259: ireturn
      //   260: astore_3
      //   261: ldc 26
      //   263: aload_3
      //   264: invokevirtual 156	java/io/IOException:getMessage	()Ljava/lang/String;
      //   267: invokestatic 159	android/util/Log:e	(Ljava/lang/String;Ljava/lang/String;)I
      //   270: pop
      //   271: iconst_0
      //   272: istore_3
      //   273: aload_2
      //   274: ifnull -16 -> 258
      //   277: aload_2
      //   278: invokevirtual 111	java/net/Socket:isConnected	()Z
      //   281: ifeq -23 -> 258
      //   284: aload_2
      //   285: invokevirtual 153	java/net/Socket:close	()V
      //   288: goto -30 -> 258
      //   291: astore_2
      //   292: aload_2
      //   293: invokevirtual 162	java/io/IOException:printStackTrace	()V
      //   296: goto -38 -> 258
      //   299: astore_3
      //   300: aload_2
      //   301: ifnull +14 -> 315
      //   304: aload_2
      //   305: invokevirtual 111	java/net/Socket:isConnected	()Z
      //   308: ifeq +7 -> 315
      //   311: aload_2
      //   312: invokevirtual 153	java/net/Socket:close	()V
      //   315: aload_3
      //   316: athrow
      //   317: astore_2
      //   318: aload_2
      //   319: invokevirtual 162	java/io/IOException:printStackTrace	()V
      //   322: goto -7 -> 315
      //   325: astore_2
      //   326: aload_2
      //   327: invokevirtual 162	java/io/IOException:printStackTrace	()V
      //   330: goto -72 -> 258
      // Local variable table:
      //   start	length	slot	name	signature
      //   0	333	0	this	AsyncClientMeta
      //   0	333	1	paramIntent	Intent
      //   70	215	2	localSocket	Socket
      //   291	21	2	localIOException1	IOException
      //   317	2	2	localIOException2	IOException
      //   325	2	2	localIOException3	IOException
      //   51	152	3	str1	String
      //   234	25	3	bool	boolean
      //   260	4	3	localIOException4	IOException
      //   272	1	3	i	int
      //   299	17	3	localObject1	Object
      //   17	197	4	str2	String
      //   80	36	5	j	int
      //   186	38	5	localBufferedWriter	java.io.BufferedWriter
      //   61	168	6	localObject2	Object
      // Exception table:
      //   from	to	target	type
      //   82	243	260	java/io/IOException
      //   284	288	291	java/io/IOException
      //   82	243	299	finally
      //   261	271	299	finally
      //   311	315	317	java/io/IOException
      //   254	258	325	java/io/IOException
    }
    
    protected Void doInBackground(Intent... paramVarArgs)
    {
      Log.v("hitchernetFileTransferService", "client meta : in background");
      FileTransferService.this.clientMetaRunning = true;
      for (;;)
      {
        if (clientTask(paramVarArgs[0]))
        {
          FileTransferService.this.clientMetaRunning = false;
          Log.v("hitchernetFileTransferService", "Sent infomation to server");
          return null;
        }
        try
        {
          Thread.sleep(3000L);
          Log.v("hitchernetFileTransferService", "Failed to send Meta, Sending again");
        }
        catch (InterruptedException localInterruptedException)
        {
          for (;;)
          {
            localInterruptedException.printStackTrace();
          }
        }
      }
    }
  }
  
  private class AsyncMainServer
    extends AsyncTask<Void, Socket, Void>
  {
    private int maxConnections = 100;
    
    private AsyncMainServer() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      try
      {
        FileTransferService.this.serverRunning = true;
        FileTransferService.this.serverSocket = new ServerSocket();
        if (FileTransferService.USE_TCP_BUFFER) {
          FileTransferService.this.serverSocket.setReceiveBufferSize(FileTransferService.TCP_BUFFER_SIZE);
        }
        InetSocketAddress localInetSocketAddress = new InetSocketAddress(FileTransferService.PORT);
        FileTransferService.this.serverSocket.bind(localInetSocketAddress);
        Log.d("hitchernetFileTransferService", "Server: Socket opened");
        Log.d("hitchernetFileTransferService", "Waiting to receive file");
        Socket[] arrayOfSocket1;
        Socket[] arrayOfSocket2;
        for (int i = 0;; arrayOfSocket2 = arrayOfSocket1)
        {
          arrayOfSocket1 = i + 1;
          if ((i >= this.maxConnections) && (this.maxConnections != 0)) {
            break;
          }
          Socket localSocket = FileTransferService.this.serverSocket.accept();
          arrayOfSocket2 = new Socket[1];
          arrayOfSocket2[0] = localSocket;
          onProgressUpdate(arrayOfSocket2);
          Log.v("test", "new thread spawned");
        }
        return null;
      }
      catch (SocketException localSocketException)
      {
        localSocketException.printStackTrace();
      }
      catch (IOException localIOException)
      {
        localIOException.printStackTrace();
      }
    }
    
    protected void onProgressUpdate(Socket... paramVarArgs)
    {
      new FileTransferService.AsyncServer(FileTransferService.this, null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paramVarArgs);
    }
  }
  
  private class AsyncServer
    extends AsyncTask<Socket, Void, Void>
  {
    String connectedDeviceMac;
    File f;
    String fileMime;
    String fileName;
    String fileSizeInBytes;
    boolean fullyTransferred = false;
    boolean isCompleted = false;
    boolean isDownloading = true;
    boolean isMetaConnection = false;
    boolean isSuccessful;
    int notifyId;
    double percent;
    long rowId;
    float speedMbps;
    String timeRemaining;
    
    private AsyncServer() {}
    
    private void serverTask(Socket paramSocket)
    {
      Log.v("hitchernetFileTransferService", "server task");
      try
      {
        Log.d("hitchernetFileTransferService", "Server: connection done");
        Log.v("hitchernetFileTransferService", "receive buffer size = " + paramSocket.getReceiveBufferSize());
        Log.v("hitchernetFileTransferService", "send buffersize=" + paramSocket.getSendBufferSize());
        BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramSocket.getInputStream(), FileTransferService.BUFFER_INPUT_OUTPUT_STREAM_SIZE);
        localObject = new BufferedReader(new InputStreamReader(localBufferedInputStream), FileTransferService.BUFFER_INPUT_OUTPUT_STREAM_SIZE);
        str1 = ((BufferedReader)localObject).readLine();
        if (str1 != null)
        {
          if (str1.equalsIgnoreCase("meta"))
          {
            this.isMetaConnection = true;
            str1 = ((BufferedReader)localObject).readLine();
            localObject = ((BufferedReader)localObject).readLine();
            Log.v("hitchernetFileTransferService", "ipAddress" + str1);
            Log.v("hitchernetFileTransferService", "mac = " + (String)localObject);
            new Preferences(FileTransferService.this).setIPForMac((String)localObject, str1);
            localBufferedInputStream.close();
            this.isSuccessful = true;
            FileTransferService.this.serverRunning = false;
            return;
          }
          this.connectedDeviceMac = ((BufferedReader)localObject).readLine();
          str1 = ((BufferedReader)localObject).readLine();
          this.fileName = ((BufferedReader)localObject).readLine();
          this.fileSizeInBytes = ((BufferedReader)localObject).readLine();
          this.fileMime = ((BufferedReader)localObject).readLine();
          localObject = new Preferences(FileTransferService.this).getFileSavingLocation();
          this.f = new File(localObject + this.fileName);
          File localFile = new File(this.f.getParent());
          if (!localFile.exists()) {
            localFile.mkdirs();
          }
          str2 = this.fileName;
          int i = 1;
          while (this.f.exists())
          {
            str2 = FileName.newFileName(this.fileName, i);
            i++;
            this.f = new File(localObject + str2);
          }
        }
      }
      catch (IOException localIOException)
      {
        for (;;)
        {
          Object localObject;
          String str1;
          String str2;
          this.isSuccessful = false;
          Log.e("hitchernetFileTransferService", localIOException.getMessage());
          continue;
          this.f.createNewFile();
          this.fileName = str2;
          this.rowId = FileTransferService.this.insertDb(this.fileName, localObject + this.fileName, this.fileSizeInBytes, true, str1);
          Log.d("hitchernetFileTransferService", "server: copying files " + this.f.toString());
          Log.v("hitchernetFileTransferService", "size of file =" + this.fileSizeInBytes);
          long l2 = Long.parseLong(this.fileSizeInBytes);
          Log.d("hitchernetFileTransferService", "size in long" + l2);
          Log.d("hitchernetFileTransferService", "file mime = " + this.fileMime);
          FileTransferService localFileTransferService = FileTransferService.this;
          long l1 = this.rowId;
          FileOutputStream localFileOutputStream = new FileOutputStream(this.f);
          String str3 = this.fileName;
          FileTransferService.Notifier local1 = new FileTransferService.Notifier()
          {
            public void notifier(long paramAnonymousLong, double paramAnonymousDouble, String paramAnonymousString1, boolean paramAnonymousBoolean1, boolean paramAnonymousBoolean2, float paramAnonymousFloat, String paramAnonymousString2)
            {
              FileTransferService.AsyncServer.this.percent = paramAnonymousDouble;
              FileTransferService.AsyncServer.this.fileName = paramAnonymousString1;
              FileTransferService.AsyncServer.this.isCompleted = paramAnonymousBoolean1;
              FileTransferService.AsyncServer.this.isDownloading = paramAnonymousBoolean2;
              FileTransferService.AsyncServer.this.speedMbps = paramAnonymousFloat;
              FileTransferService.AsyncServer.this.timeRemaining = paramAnonymousString2;
              FileTransferService.AsyncServer.this.rowId = paramAnonymousLong;
              FileTransferService.AsyncServer.this.onProgressUpdate(new Void[0]);
            }
          };
          localFileTransferService.copyFile(l1, localIOException, localFileOutputStream, l2, true, str3, local1, null);
          Log.v("hitchernetFileTransferService", "expected file size=" + l2);
          Log.v("hitchernetFileTransferService", "received file size=" + this.f.length());
          if (l2 == this.f.length())
          {
            this.fullyTransferred = true;
          }
          else
          {
            this.fullyTransferred = false;
            continue;
            this.isSuccessful = false;
          }
        }
      }
    }
    
    protected Void doInBackground(Socket... paramVarArgs)
    {
      serverTask(paramVarArgs[0]);
      return null;
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      if (!this.isMetaConnection)
      {
        Intent localIntent;
        if (!this.isSuccessful)
        {
          localIntent = new Intent("IOExceptionOccurred");
          localIntent.setClass(FileTransferService.this.getApplicationContext(), WiFiDirectActivity.class);
          LocalBroadcastManager.getInstance(FileTransferService.this.getApplicationContext()).sendBroadcast(localIntent);
        }
        else
        {
          localIntent = new Intent("com.kharybdis.hitchernet.filetransferservice.fileAbsoulutePath");
          localIntent.putExtra("filePath", this.f.getAbsolutePath());
          localIntent.putExtra("fileMime", this.fileMime);
          Log.v("hitchernetFileTransferService", "send sticky broadcast");
          FileTransferService.this.sendStickyBroadcast(localIntent);
        }
      }
      else
      {
        Log.v("hitchernetFileTransferService", "server started at " + IP.getIPAddress1());
      }
    }
    
    protected void onPreExecute()
    {
      super.onPreExecute();
      FileTransferService localFileTransferService = FileTransferService.this;
      localFileTransferService.notificationCounter = (1 + localFileTransferService.notificationCounter);
      this.notifyId = FileTransferService.this.notificationCounter;
    }
    
    protected void onProgressUpdate(Void... paramVarArgs)
    {
      PeersInfo.updatePeerPercent(false, this.connectedDeviceMac, (int)this.percent);
      FileTransferService.this.dbHelper.updateRowWithId(this.rowId, (int)this.percent, this.speedMbps, this.timeRemaining);
      Intent localIntent = new Intent("intent_progress_notify");
      localIntent.putExtra("percent", this.percent);
      localIntent.putExtra("fileName", this.fileName);
      localIntent.putExtra("isCompleted", this.isCompleted);
      localIntent.putExtra("isDownloading", this.isDownloading);
      localIntent.putExtra("speedMbps", this.speedMbps);
      localIntent.putExtra("timeRemaining", this.timeRemaining);
      localIntent.setClass(FileTransferService.this.getApplicationContext(), WiFiDirectActivity.class);
      LocalBroadcastManager.getInstance(FileTransferService.this.getApplicationContext()).sendBroadcast(localIntent);
      FileTransferService.this.showNotification(FileTransferService.this.notificationManager, FileTransferService.this.mBuilder, this.percent, this.fileName, this.isCompleted, this.isDownloading, this.speedMbps, this.timeRemaining, this.notifyId);
    }
  }
  
  private class AsyncTimer
    extends AsyncTask<Void, Void, Void>
  {
    private AsyncTimer() {}
    
    protected Void doInBackground(Void... paramVarArgs)
    {
      try
      {
        Thread.sleep(5L);
        return null;
      }
      catch (InterruptedException localInterruptedException)
      {
        for (;;)
        {
          localInterruptedException.printStackTrace();
        }
      }
    }
    
    protected void onPostExecute(Void paramVoid)
    {
      super.onPostExecute(paramVoid);
      if ((Globals.isActivityAlive) || (FileTransferService.this.isWifiP2pEnabled)) {
        new AsyncTimer(FileTransferService.this).execute(new Void[0]);
      } else {
        FileTransferService.this.stopSelf();
      }
    }
  }
  
  public class LocalBinder
    extends Binder
  {
    public LocalBinder() {}
    
    FileTransferService getService()
    {
      return FileTransferService.this;
    }
  }
  
  public static abstract interface Notifier
  {
    public abstract void notifier(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2);
  }
  
  public static abstract interface ProgressBehaviourListener
  {
    public abstract void dismisDialog();
    
    public abstract void showDialog(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2);
    
    public abstract void updateDialog(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2);
  }
  
  public class WifiP2PConfig
    extends WifiP2pConfig
  {
    public String extra;
    
    public WifiP2PConfig() {}
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.FileTransferService
 * JD-Core Version:    0.7.0.1
 */