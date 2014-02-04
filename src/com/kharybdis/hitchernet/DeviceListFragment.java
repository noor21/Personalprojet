package com.kharybdis.hitchernet;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.FilterMode;
import java.util.Collection;
import java.util.List;

public class DeviceListFragment
  extends ListFragment
{
  private static final String TAG = "hitchernetDeviceListFragment";
  public static String justSelectedDeviceAddress;
  private WifiP2pDevice device;
  ProgressBar lastSpinnedProgressBar;
  View mContentView = null;
  ProgressBar mainProgressBar;
  ProgressDialog progressDialog = null;
  private boolean showUploadDownloadProgressBar = false;
  TextView tvEmpty;
  
  private static String getDeviceStatus(int paramInt)
  {
    String str;
    switch (paramInt)
    {
    default: 
      Log.d("hitchernetDeviceListFragment", "Peer status : unknown");
      str = "Unknown";
      break;
    case 0: 
      Log.d("hitchernetDeviceListFragment", "Peer status :connected");
      str = "Connected";
      break;
    case 1: 
      Log.d("hitchernetDeviceListFragment", "Peer status :invited");
      str = "Invited";
      break;
    case 2: 
      Log.d("hitchernetDeviceListFragment", "peer status : failed");
      str = "Failed";
      break;
    case 3: 
      Log.d("hitchernetDeviceListFragment", "Peer status : available");
      str = "Available";
      break;
    case 4: 
      Log.d("hitchernetDeviceListFragment", "Peer status : unavailable");
      str = "Unavailable";
    }
    return str;
  }
  
  public void clearPeers()
  {
    PeersInfo.clearPeers();
    WiFiPeerListAdapter localWiFiPeerListAdapter = (WiFiPeerListAdapter)getListAdapter();
    if (localWiFiPeerListAdapter != null)
    {
      localWiFiPeerListAdapter.notifyDataSetChanged();
      getListView().invalidate();
      getListView().setAdapter(null);
    }
    Log.v("hitchernetDeviceListFragment", "peers cleared");
  }
  
  public WifiP2pDevice getDevice()
  {
    return this.device;
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    setListAdapter(new WiFiPeerListAdapter(PeersInfo.getPeers()));
    if (PeersInfo.getPeers().size() != 0) {
      this.mainProgressBar.setVisibility(8);
    }
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    switch (paramInt1)
    {
    }
    for (;;)
    {
      return;
      if (paramInt2 == -1) {
        if (paramIntent != null)
        {
          ((IFileProvider.FilterMode)paramIntent.getSerializableExtra(FileChooserActivity._FilterMode));
          paramIntent.getBooleanExtra(FileChooserActivity._SaveDialog, false);
          List localList = (List)paramIntent.getSerializableExtra(FileChooserActivity._Results);
          if ((localList == null) || (localList.size() != 0)) {
            try
            {
              ((DeviceClickListener)getActivity()).startFileTransfer(localList, justSelectedDeviceAddress);
            }
            catch (Exception localException)
            {
              Log.e("FileSelectorTestActivity", "File select error", localException);
            }
          }
        }
        else
        {
          Toast.makeText(getActivity(), "Nothing Selected", 1).show();
        }
      }
    }
  }
  
  public void onConnectionFailure(int paramInt)
  {
    if (this.lastSpinnedProgressBar != null) {
      this.lastSpinnedProgressBar.setVisibility(8);
    }
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    this.mContentView = paramLayoutInflater.inflate(2130903051, null);
    ((TextView)this.mContentView.findViewById(2131099695)).setText(getResources().getString(2131165291) + '\n' + Build.MANUFACTURER + " - " + Build.PRODUCT);
    ((TextView)this.mContentView.findViewById(2131099696)).setText(Globals.thisDeviceName);
    this.mainProgressBar = ((ProgressBar)this.mContentView.findViewById(2131099697));
    this.tvEmpty = ((TextView)this.mContentView.findViewById(2131099698));
    return this.mContentView;
  }
  
  public void onListItemClick(ListView paramListView, final View paramView, final int paramInt, long paramLong)
  {
    Peer localPeer = (Peer)getListAdapter().getItem(paramInt);
    final WifiP2pDevice localWifiP2pDevice = localPeer.getWifiP2pDevice();
    if ((localWifiP2pDevice.status == 0) && (localPeer.isAvailable()))
    {
      new AlertDialog.Builder(getActivity()).setTitle("Disconnect?").setMessage("Any ongoing transfer will be cancelled.").setPositiveButton("Yes", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          Log.v("hitchernetDeviceListFragment", "should disconnect");
          ((DeviceListFragment.DeviceClickListener)DeviceListFragment.this.getActivity()).disconnect();
          DeviceListFragment.this.mainProgressBar.setVisibility(0);
          ((Peer)PeersInfo.getPeers().get(paramInt)).setDisconnected(true);
          ((Peer)PeersInfo.getPeers().get(paramInt)).setAvailability(false);
        }
      }).setNegativeButton("No", null).show();
    }
    else if (localPeer.getWifiP2pDevice().status != 1)
    {
      Log.v("hitchernetDeviceListFragment", "List item clicked");
      ((ProgressBar)paramView.findViewById(2131099697)).setVisibility(0);
      ((DeviceClickListener)getActivity()).connectP2p(localWifiP2pDevice);
    }
    else
    {
      new AlertDialog.Builder(getActivity()).setTitle("Already Invited.").setMessage("DO you still want to re connect?").setPositiveButton("Reconnect", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ProgressBar localProgressBar = (ProgressBar)paramView.findViewById(2131099697);
          localProgressBar.setVisibility(0);
          DeviceListFragment.this.lastSpinnedProgressBar = localProgressBar;
          ((DeviceListFragment.DeviceClickListener)DeviceListFragment.this.getActivity()).connectP2p(localWifiP2pDevice);
        }
      }).setNegativeButton("Cancel Connect", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          ((DeviceListFragment.DeviceClickListener)DeviceListFragment.this.getActivity()).cancelConnect();
          ((ProgressBar)paramView.findViewById(2131099697)).setVisibility(8);
        }
      }).show();
    }
  }
  
  public void onPeersAvailable(WifiP2pDeviceList paramWifiP2pDeviceList)
  {
    this.mainProgressBar.setVisibility(8);
    Log.v("hitchernetDeviceListFragment", "no of peers = " + paramWifiP2pDeviceList.getDeviceList().size());
    Log.v("hitchernetDeviceListFragment", "showing all peers in the list now");
    if (getListAdapter() == null)
    {
      Log.v("hitchernetDeviceListFragment", "couldnt show devices in the list because adapter was null");
    }
    else
    {
      setListAdapter(new WiFiPeerListAdapter(PeersInfo.getPeers()));
      ((WiFiPeerListAdapter)getListAdapter()).notifyDataSetChanged();
      getListView().invalidate();
    }
    if (PeersInfo.getPeers().size() == 0)
    {
      Log.d("hitchernetDeviceListFragment", "No devices found");
      this.mainProgressBar.setVisibility(0);
    }
  }
  
  public void refreshAdapter()
  {
    WiFiPeerListAdapter localWiFiPeerListAdapter = (WiFiPeerListAdapter)getListAdapter();
    if (localWiFiPeerListAdapter != null) {
      localWiFiPeerListAdapter.notifyDataSetChanged();
    }
  }
  
  public void showProgressBar(boolean paramBoolean)
  {
    if (!paramBoolean)
    {
      if (this.mainProgressBar != null) {
        this.mainProgressBar.setVisibility(8);
      }
      this.tvEmpty.setVisibility(8);
    }
    else
    {
      if (this.mainProgressBar != null) {
        this.mainProgressBar.setVisibility(0);
      }
      this.tvEmpty.setVisibility(8);
    }
  }
  
  public void updatePeers(Collection<WifiP2pDevice> paramCollection)
  {
    this.mainProgressBar.setVisibility(8);
    this.tvEmpty.setVisibility(8);
    if (getListAdapter() != null)
    {
      setListAdapter(new WiFiPeerListAdapter(PeersInfo.getPeers()));
      ((WiFiPeerListAdapter)getListAdapter()).notifyDataSetChanged();
      getListView().invalidate();
    }
  }
  
  public void updateThisDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    this.device = paramWifiP2pDevice;
    ((TextView)this.mContentView.findViewById(2131099696)).setText(Globals.thisDeviceName);
  }
  
  public static abstract interface DeviceActionListener
  {
    public abstract void cancelConnect();
    
    public abstract void cancelDisconnect();
    
    public abstract void connect(WifiP2pConfig paramWifiP2pConfig);
    
    public abstract void disconnect();
  }
  
  static abstract interface DeviceClickListener
  {
    public abstract void cancelConnect();
    
    public abstract void connectP2p(WifiP2pDevice paramWifiP2pDevice);
    
    public abstract void disconnect();
    
    public abstract void startFileTransfer(List<LocalFile> paramList, String paramString);
  }
  
  private class WiFiPeerListAdapter
    extends BaseAdapter
  {
    private List<Peer> items;
    
    public WiFiPeerListAdapter()
    {
      Object localObject;
      this.items = localObject;
    }
    
    private void setProgressBars(View paramView, Peer paramPeer)
    {
      ProgressBar localProgressBar = (ProgressBar)paramView.findViewById(2131099728);
      if ((paramPeer.UploadProgressPercent >= 0) && (paramPeer.UploadProgressPercent < 100))
      {
        if (DeviceListFragment.this.showUploadDownloadProgressBar) {
          localProgressBar.setVisibility(0);
        }
        localProgressBar.setProgress(paramPeer.UploadProgressPercent);
      }
      else
      {
        localProgressBar.setVisibility(4);
      }
      localProgressBar = (ProgressBar)paramView.findViewById(2131099729);
      if ((paramPeer.DownloadProgressPercent >= 0) && (paramPeer.DownloadProgressPercent < 100))
      {
        if (DeviceListFragment.this.showUploadDownloadProgressBar) {
          localProgressBar.setVisibility(0);
        }
        localProgressBar.setProgress(paramPeer.DownloadProgressPercent);
      }
      else
      {
        localProgressBar.setVisibility(4);
      }
    }
    
    public int getCount()
    {
      return this.items.size();
    }
    
    public Object getItem(int paramInt)
    {
      return this.items.get(paramInt);
    }
    
    public long getItemId(int paramInt)
    {
      return 0L;
    }
    
    public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
    {
      View localView = ((LayoutInflater)DeviceListFragment.this.getActivity().getSystemService("layout_inflater")).inflate(2130903069, null);
      Object localObject1 = (Peer)this.items.get(paramInt);
      if ((((Peer)localObject1).lastDeviceStatus != ((Peer)localObject1).getWifiP2pDevice().status) && (((Peer)localObject1).getWifiP2pDevice().status != 1))
      {
        Log.v("hitchernetDeviceListFragment", "last status =" + ((Peer)localObject1).lastDeviceStatus);
        Log.v("hitchernetDeviceListFragment", "current=" + ((Peer)localObject1).getWifiP2pDevice().status);
        ((ProgressBar)localView.findViewById(2131099697)).setVisibility(8);
      }
      if (((Peer)localObject1).getWifiP2pDevice().status == 1)
      {
        Log.v("hitchernetDeviceListFragment", "current peer status is invited");
        ((ProgressBar)localView.findViewById(2131099697)).setVisibility(0);
      }
      Object localObject2 = ((Peer)localObject1).getWifiP2pDevice();
      Object localObject3 = (ImageView)localView.findViewById(2131099704);
      if (!((Peer)localObject1).isAvailable()) {
        ((ImageView)localObject3).setVisibility(0);
      } else {
        ((ImageView)localObject3).setVisibility(4);
      }
      ((Peer)localObject1).isDisconnected();
      if (localObject2 != null)
      {
        Object localObject4 = (TextView)localView.findViewById(2131099701);
        localObject3 = (TextView)localView.findViewById(2131099702);
        if (localObject4 != null) {
          ((TextView)localObject4).setText(((WifiP2pDevice)localObject2).deviceName);
        }
        if (localObject3 != null)
        {
          if (((Peer)localObject1).isAvailable()) {
            localObject4 = DeviceListFragment.getDeviceStatus(((WifiP2pDevice)localObject2).status);
          } else {
            localObject4 = "Unavailable";
          }
          ((TextView)localObject3).setText((CharSequence)localObject4);
        }
        if ((((WifiP2pDevice)localObject2).status != 0) || (!((Peer)localObject1).isAvailable()))
        {
          localObject1 = (ImageView)localView.findViewById(2131099703);
          ((ImageView)localObject1).setEnabled(false);
          ((ImageView)localObject1).setVisibility(8);
          ((ImageView)localView.findViewById(2131099699)).setImageResource(2130837552);
          ((ImageView)localView.findViewById(2131099700)).setImageResource(2130837536);
        }
        else
        {
          localObject3 = (ImageView)localView.findViewById(2131099703);
          ((ImageView)localView.findViewById(2131099699)).setImageResource(2130837553);
          ((ImageView)localView.findViewById(2131099700)).setImageResource(2130837537);
          ((ImageView)localObject3).setEnabled(true);
          ((ImageView)localObject3).setVisibility(0);
          localObject2 = ((WifiP2pDevice)localObject2).deviceAddress;
          setProgressBars(localView, (Peer)localObject1);
          ((ImageView)localObject3).setOnClickListener(new View.OnClickListener()
          {
            public void onClick(View paramAnonymousView)
            {
              DeviceListFragment.justSelectedDeviceAddress = this.val$currentDeviceMac;
              Intent localIntent = new Intent(DeviceListFragment.this.getActivity(), FileChooserActivity.class);
              localIntent.putExtra(FileChooserActivity._MultiSelection, true);
              localIntent.putExtra("deviceMac", this.val$currentDeviceMac);
              DeviceListFragment.this.startActivityForResult(localIntent, 20);
            }
          });
        }
      }
      return localView;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.DeviceListFragment
 * JD-Core Version:    0.7.0.1
 */