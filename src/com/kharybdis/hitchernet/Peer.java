package com.kharybdis.hitchernet;

import android.net.wifi.p2p.WifiP2pDevice;

public class Peer
{
  private static int COUNTER = 0;
  public int DownloadProgressPercent = -1;
  public int UploadProgressPercent = -1;
  private int id = 0;
  String instanceName = null;
  private boolean isAvailable = false;
  private boolean isDisconnected = false;
  public int lastDeviceStatus;
  String serviceRegistrationType = null;
  private WifiP2pDevice wifiP2pDevice;
  
  public Peer(WifiP2pDevice paramWifiP2pDevice)
  {
    this.wifiP2pDevice = paramWifiP2pDevice;
    this.lastDeviceStatus = paramWifiP2pDevice.status;
    COUNTER = 1 + COUNTER;
    this.id = COUNTER;
  }
  
  private boolean isJustDisconnected()
  {
    boolean bool;
    if ((this.lastDeviceStatus != 0) || (this.wifiP2pDevice.status == 0)) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public WifiP2pDevice getWifiP2pDevice()
  {
    return this.wifiP2pDevice;
  }
  
  public boolean isAvailable()
  {
    return this.isAvailable;
  }
  
  public boolean isDisconnected()
  {
    return this.isDisconnected;
  }
  
  public void setAvailability(boolean paramBoolean)
  {
    this.isAvailable = paramBoolean;
  }
  
  public void setDisconnected(boolean paramBoolean)
  {
    this.isDisconnected = paramBoolean;
  }
  
  public void updateWifiP2pDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    this.lastDeviceStatus = this.wifiP2pDevice.status;
    this.wifiP2pDevice = paramWifiP2pDevice;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.Peer
 * JD-Core Version:    0.7.0.1
 */