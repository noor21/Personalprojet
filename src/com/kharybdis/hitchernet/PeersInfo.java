package com.kharybdis.hitchernet;

import android.net.wifi.p2p.WifiP2pDevice;
import android.util.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class PeersInfo
{
  private static final String TAG = "hitchernetPeersInfo";
  private static List<Peer> peersList = new ArrayList();
  
  public static void addPeers(Collection<WifiP2pDevice> paramCollection)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)localIterator.next();
      localArrayList.add(new Peer(localWifiP2pDevice));
      if (!ifPresentUpdate(localWifiP2pDevice)) {
        peersList.add(new Peer(localWifiP2pDevice));
      }
    }
    unsetDisconnected();
  }
  
  public static void clearPeers()
  {
    Log.v("hitchernetPeersInfo", "Clear peer list");
    peersList = new ArrayList();
  }
  
  public static String getDeviceNameWithMac(String paramString)
  {
    String str = "";
    Iterator localIterator = peersList.iterator();
    while (localIterator.hasNext())
    {
      Peer localPeer = (Peer)localIterator.next();
      if (localPeer.getWifiP2pDevice().deviceAddress.equalsIgnoreCase(paramString)) {
        str = localPeer.getWifiP2pDevice().deviceName;
      }
    }
    return str;
  }
  
  public static List<Peer> getPeers()
  {
    Collections.sort(peersList, new CustomComparator(null));
    return peersList;
  }
  
  private static boolean ifPresentUpdate(WifiP2pDevice paramWifiP2pDevice)
  {
    Log.v("hitchernetPeersInfo", "is present so update it");
    int i = 0;
    int j = 0;
    Log.v("hitchernetPeersInfo", "peer size = " + peersList.size());
    Iterator localIterator = peersList.iterator();
    while (localIterator.hasNext()) {
      if (!((Peer)localIterator.next()).getWifiP2pDevice().deviceAddress.equalsIgnoreCase(paramWifiP2pDevice.deviceAddress)) {
        i++;
      } else {
        j = 1;
      }
    }
    if (j == 0)
    {
      i = 0;
    }
    else
    {
      ((Peer)peersList.get(i)).updateWifiP2pDevice(paramWifiP2pDevice);
      ((Peer)peersList.get(i)).setAvailability(true);
      Log.v("hitchernetPeersInfo", ((Peer)peersList.get(i)).getWifiP2pDevice().deviceName + " is available");
      i = 1;
    }
    return i;
  }
  
  private static void unsetDisconnected()
  {
    for (int i = 0; i < peersList.size(); i++)
    {
      ((Peer)peersList.get(i)).setDisconnected(false);
      Log.v("hitchernetPeersInfo", "disconnected= false");
    }
  }
  
  private static void updateAvailability(List<Peer> paramList)
  {
    Log.v("hitchernetPeersInfo", "update the availability of devices");
    Object localObject = new ArrayList();
    Integer localInteger = Integer.valueOf(0);
    Iterator localIterator2 = peersList.iterator();
    while (localIterator2.hasNext())
    {
      Peer localPeer2 = (Peer)localIterator2.next();
      int i = 0;
      localIterator1 = paramList.iterator();
      while (localIterator1.hasNext())
      {
        Peer localPeer1 = (Peer)localIterator1.next();
        if (localPeer2.getWifiP2pDevice().deviceAddress.equalsIgnoreCase(localPeer1.getWifiP2pDevice().deviceAddress))
        {
          Log.v("hitchernetPeersInfo", "found" + localPeer2.getWifiP2pDevice().deviceName);
          localPeer2.setDisconnected(false);
          i = 1;
        }
      }
      if (i == 0)
      {
        ((List)localObject).add(localInteger);
        Log.v("hitchernetPeersInfo", "not found" + localPeer2.getWifiP2pDevice().deviceName);
      }
      localInteger = Integer.valueOf(1 + localInteger.intValue());
    }
    Iterator localIterator1 = ((List)localObject).iterator();
    while (localIterator1.hasNext())
    {
      localObject = (Integer)localIterator1.next();
      ((Peer)peersList.get(((Integer)localObject).intValue())).setAvailability(false);
      Log.v("hitchernetPeersInfo", "available= false for device with name=" + ((Peer)peersList.get(((Integer)localObject).intValue())).getWifiP2pDevice().deviceName);
    }
  }
  
  public static void updatePeerPercent(boolean paramBoolean, String paramString, int paramInt)
  {
    Iterator localIterator = peersList.iterator();
    while (localIterator.hasNext())
    {
      Peer localPeer = (Peer)localIterator.next();
      Log.v("hitchernetPeersInfo", localPeer.getWifiP2pDevice().deviceName + "  " + localPeer.getWifiP2pDevice().deviceAddress + "  " + paramString);
      if (localPeer.getWifiP2pDevice().deviceAddress.equalsIgnoreCase(paramString))
      {
        if (!paramBoolean)
        {
          localPeer.DownloadProgressPercent = paramInt;
          return;
        }
        localPeer.UploadProgressPercent = paramInt;
        return;
      }
    }
    Log.v("hitchernetPeersInfo", "device not found");
  }
  
  public static void updatePeerStatus(Collection<WifiP2pDevice> paramCollection)
  {
    Log.v("hitchernetPeersInfo", "update peer status");
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
    {
      WifiP2pDevice localWifiP2pDevice = (WifiP2pDevice)localIterator.next();
      Log.v("hitchernetPeersInfo", "device = " + localWifiP2pDevice.deviceName);
      localArrayList.add(new Peer(localWifiP2pDevice));
      ifPresentUpdate(localWifiP2pDevice);
    }
    updateAvailability(localArrayList);
  }
  
  private static class CustomComparator
    implements Comparator<Peer>
  {
    public int compare(Peer paramPeer1, Peer paramPeer2)
    {
      return paramPeer1.getId() - paramPeer2.getId();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.PeersInfo
 * JD-Core Version:    0.7.0.1
 */