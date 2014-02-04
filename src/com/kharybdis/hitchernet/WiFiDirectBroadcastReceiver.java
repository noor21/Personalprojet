package com.kharybdis.hitchernet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;

public class WiFiDirectBroadcastReceiver
  extends BroadcastReceiver
{
  private static final String TAG = "hitchernetBroadcastReceiver";
  private WifiP2pManager.Channel channel;
  private WifiP2pManager manager;
  private FileTransferService service;
  private boolean wasConnected = false;
  
  public WiFiDirectBroadcastReceiver(WifiP2pManager paramWifiP2pManager, WifiP2pManager.Channel paramChannel, FileTransferService paramFileTransferService)
  {
    this.manager = paramWifiP2pManager;
    this.channel = paramChannel;
    this.service = paramFileTransferService;
  }
  
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    String str = paramIntent.getAction();
    Log.v("hitchernetBroadcastReceiver", str);
    if (!"android.net.wifi.p2p.STATE_CHANGED".equals(str))
    {
      if (!"android.net.wifi.p2p.PEERS_CHANGED".equals(str))
      {
        Object localObject1;
        if (!"android.net.wifi.p2p.CONNECTION_STATE_CHANGE".equals(str))
        {
          if (!"android.net.wifi.p2p.THIS_DEVICE_CHANGED".equals(str))
          {
            if ("android.net.wifi.p2p.DISCOVERY_STATE_CHANGE".equals(str))
            {
              int i = paramIntent.getIntExtra("discoveryState", -1);
              if (i != 2)
              {
                if (i != 1) {
                  Log.v("hitchernetBroadcastReceiver", "discovery no idea");
                } else {
                  Log.v("hitchernetBroadcastReceiver", "discovery has stopped");
                }
              }
              else {
                Log.v("hitchernetBroadcastReceiver", "discovery has started");
              }
            }
          }
          else
          {
            Log.v("hitchernetBroadcastReceiver", "Detail of this device is changed");
            localObject1 = (WifiP2pDevice)paramIntent.getParcelableExtra("wifiP2pDevice");
            Globals.thisDeviceName = ((WifiP2pDevice)localObject1).deviceName;
            Globals.thisDeviceAddress = ((WifiP2pDevice)localObject1).deviceAddress;
            new Preferences(this.service).setThisDeviceName(((WifiP2pDevice)localObject1).deviceName);
            this.service.updateDevice((WifiP2pDevice)localObject1);
          }
        }
        else
        {
          Log.v("hitchernetBroadcastReceiver", "Connection changed");
          if (this.manager != null)
          {
            localObject1 = (NetworkInfo)paramIntent.getParcelableExtra("networkInfo");
            Object localObject2 = (WifiP2pInfo)paramIntent.getParcelableExtra("wifiP2pInfo");
            StringBuilder localStringBuilder = new StringBuilder("is group formed=");
            if (!((WifiP2pInfo)localObject2).groupFormed) {
              localObject2 = "no";
            } else {
              localObject2 = "yes";
            }
            Log.v("hitchernetBroadcastReceiver", (String)localObject2);
            if (!((NetworkInfo)localObject1).isConnected())
            {
              if (this.wasConnected)
              {
                this.wasConnected = false;
                Log.v("hitchernetBroadcastReceiver", "Restart Registration because it was connected just before. It means it is disconnected by pressing disconnect");
                this.service.startRegistrationAndDiscovery();
              }
              Log.v("hitchernetBroadcastReceiver", "disconnected");
            }
            else
            {
              this.wasConnected = true;
              Log.v("hitchernetBroadcastReceiver", "device is connected  , Now request connection information");
              Log.v("hitchernetBroadcastReceiver", "ip = " + IP.getIPAddress1());
              this.manager.requestConnectionInfo(this.channel, this.service);
            }
          }
          else
          {
            Log.v("hitchernetBroadcastReceiver", "manager is null");
          }
        }
      }
      else
      {
        Log.v("hitchernetBroadcastReceiver", "peers changed broadcast received. finding  devices..");
        Log.d("hitchernetBroadcastReceiver", "P2P peers changed");
        if (this.manager != null) {
          this.manager.requestPeers(this.channel, this.service);
        }
      }
    }
    else
    {
      Log.d("hitchernetBroadcastReceiver", "Track: WDB - or");
      Log.v("hitchernetBroadcastReceiver", "intent received");
      int j = paramIntent.getIntExtra("wifi_p2p_state", -1);
      if (j != 2)
      {
        Log.v("hitchernetBroadcastReceiver", "wifi direct is disabled");
        this.service.setWifiP2PEnabled(false);
        Log.v("hitchernetBroadcastReceiver", "wifi direct is not enabled");
        this.service.resetData();
      }
      else
      {
        Log.v("hitchernetBroadcastReceiver", "wifi direct is enabled");
        this.service.setWifiP2PEnabled(true);
      }
      Log.d("hitchernetBroadcastReceiver", "P2P state changed - " + j);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.WiFiDirectBroadcastReceiver
 * JD-Core Version:    0.7.0.1
 */