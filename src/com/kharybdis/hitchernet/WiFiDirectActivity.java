package com.kharybdis.hitchernet;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import com.github.espiandev.showcaseview.ShowcaseView.ConfigOptions;
import com.github.espiandev.showcaseview.ShowcaseViews;
import com.kharybdis.hitchernet.menudrawer.BaseListSample;
import com.kharybdis.hitchernet.menudrawer.Item;
import com.kharybdis.hitchernet.menudrawer.MenuAdapter;
import com.kharybdis.hitchernet.rateme.Rate;
import com.kharybdis.hitchernet.showcase.WiFiDirectActivityDemo;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnInterceptMoveEventListener;
import net.simonvt.menudrawer.Position;

public class WiFiDirectActivity
  extends BaseListSample
  implements FileTransferService.Notifier, FileTransferService.ProgressBehaviourListener, FileTransferService.ActivityNotifier, DeviceListFragment.DeviceClickListener, SettingFragment.DeviceRenamer
{
  private static final int JELLYBEAN = 16;
  private static final float SHOWCASE_KITTEN_SCALE = 1.2F;
  private static final float SHOWCASE_LIKE_SCALE = 0.5F;
  private static final String STATE_CURRENT_FRAGMENT = "net.simonvt.menudrawer.samples.FragmentSample";
  private static final String TAG = "hitchernetWifiDirectActivity";
  ActionBar actionBar;
  private BroadcastReceiver clientServiceListener = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (!paramAnonymousIntent.getAction().equals("intent_progress_notify"))
      {
        if (!paramAnonymousIntent.getAction().equals("IOExceptionOccurred"))
        {
          if (paramAnonymousIntent.getAction().equals("showProgressDialog"))
          {
            Log.v("hitchernetWifiDirectActivity", "Progress dialog show");
            WiFiDirectActivity.this.showProgressDialog();
          }
        }
        else {
          Log.e("hitchernetWifiDirectActivity", "ERROR_IO_EXCEPTION please try again");
        }
      }
      else
      {
        double d = paramAnonymousIntent.getDoubleExtra("percent", 0.0D);
        String str1 = paramAnonymousIntent.getStringExtra("fileName");
        boolean bool1 = paramAnonymousIntent.getBooleanExtra("isCompleted", false);
        boolean bool2 = paramAnonymousIntent.getBooleanExtra("isDownloading", false);
        float f = paramAnonymousIntent.getFloatExtra("speedMbps", 0.0F);
        String str2 = paramAnonymousIntent.getStringExtra("timeRemaining");
        long l = paramAnonymousIntent.getLongExtra("rowId", -1L);
        WiFiDirectActivity.this.updateDialog(l, d, str1, bool1, bool2, f, str2);
        WiFiDirectActivity.this.fragmentList.refreshAdapter();
      }
    }
  };
  private AlertDialog enableWifiDialog;
  private FrameLayout fragmentContainer;
  private DeviceListFragment fragmentList;
  private HashMap<String, List<Fragment>> fragmentStack = new HashMap();
  private Handler handler = new Handler()
  {
    public void handleMessage(Message paramAnonymousMessage)
    {
      Bundle localBundle = paramAnonymousMessage.getData();
      if (WiFiDirectActivity.this.pDialogFragment != null) {
        WiFiDirectActivity.this.pDialogFragment.updateDialog(localBundle.getLong("rowId"), localBundle.getDouble("percent"), localBundle.getString("fileName"), localBundle.getBoolean("isCompleted"), localBundle.getBoolean("isDownloading"), localBundle.getFloat("speedMbps"), localBundle.getString("timeRemaining"));
      }
    }
  };
  private InboxOutboxFragment inboxFragment;
  private final IntentFilter intentsClientService = new IntentFilter();
  boolean mBound = false;
  private ServiceConnection mConnection = new ServiceConnection()
  {
    public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
    {
      FileTransferService.LocalBinder localLocalBinder = (FileTransferService.LocalBinder)paramAnonymousIBinder;
      WiFiDirectActivity.this.mService = localLocalBinder.getService();
      WiFiDirectActivity.this.mBound = true;
      WiFiDirectActivity.this.logv("Service connected");
      WiFiDirectActivity.this.mService.setActivityNotifier(WiFiDirectActivity.this);
    }
    
    public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
    {
      WiFiDirectActivity.this.logv("service disconnected");
      WiFiDirectActivity.this.mBound = false;
    }
  };
  private String mCurrentFragmentTag;
  private String mCurrentMenuTag = "";
  private FragmentManager mFragmentManager;
  private FragmentTransaction mFragmentTransaction;
  ShowcaseView.ConfigOptions mOptions = new ShowcaseView.ConfigOptions();
  FileTransferService mService;
  ShowcaseViews mViews;
  private InboxOutboxFragment outboxFragment;
  private ProgressDialogFragment pDialogFragment;
  BroadcastReceiver receiver2 = new BroadcastReceiver()
  {
    public void onReceive(Context paramAnonymousContext, Intent paramAnonymousIntent)
    {
      if (!paramAnonymousIntent.getAction().equalsIgnoreCase("com.kharybdis.hitchernet.DISMISS_DIALOG"))
      {
        if (paramAnonymousIntent.getAction().equals("com.kharybdis.hitchernet.filetransferservice.fileAbsoulutePath"))
        {
          Uri localUri = Uri.parse("file://" + paramAnonymousIntent.getStringExtra("filePath"));
          WiFiDirectActivity.this.alertWithOptions(localUri, paramAnonymousIntent.getStringExtra("fileMime"));
        }
      }
      else
      {
        Log.v("hitchernetWifiDirectActivity", "sticky broadcast received");
        if (WiFiDirectActivity.this.pDialogFragment != null) {
          WiFiDirectActivity.this.pDialogFragment.updateAsFinished();
        }
        Rate.showRatePrompt(WiFiDirectActivity.this);
      }
      WiFiDirectActivity.this.removeStickyBroadcast(paramAnonymousIntent);
    }
  };
  private AlertDialog restartWifiDirectDialog;
  private boolean showRefreshButton = true;
  
  private void addActionsToIntentFilters()
  {
    this.intentsClientService.addAction("intent_progress_notify");
    this.intentsClientService.addAction("com.kharybdis.hitchernet.DISMISS_DIALOG");
    this.intentsClientService.addAction("com.kharybdis.hitchernet.filetransferservice.fileAbsoulutePath");
    this.intentsClientService.addAction("IOExceptionOccurred");
  }
  
  private void addFrag(Item paramItem)
  {
    if (this.mCurrentFragmentTag != null) {
      detachFragment(getFragment(this.mCurrentFragmentTag));
    }
    attachFragment(this.fragmentContainer.getId(), getFragment(paramItem.mUniqueCode), paramItem.mUniqueCode);
    this.mCurrentFragmentTag = paramItem.mUniqueCode;
    commitTransactions();
    addFragmentToStack(getFragment(this.mCurrentFragmentTag));
  }
  
  private void alertWithOptions(final Uri paramUri, String paramString)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
    localBuilder.setTitle("Open File?");
    localBuilder.setMessage("Saved Location:" + paramUri.getPath());
    localBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        new Utils().startActivityChooser(WiFiDirectActivity.this, paramUri.getPath());
      }
    });
    localBuilder.setNegativeButton("No", null);
    localBuilder.show();
  }
  
  private void createStacks()
  {
    createStackForMenu("HOME");
    createStackForMenu("INBOX");
    createStackForMenu("OUTBOX");
    createStackForMenu("SETTINGS");
  }
  
  private Fragment getFragment(String paramString)
  {
    return getFragment(paramString, -1, null);
  }
  
  private Fragment getFragment(String paramString1, int paramInt, String paramString2)
  {
    Log.v("hitchernetWifiDirectActivity", "inside get fragment");
    Object localObject = this.mFragmentManager.findFragmentByTag(paramString1);
    if (localObject == null) {
      if (!paramString1.equals("HOME"))
      {
        if (!paramString1.equals("INBOX"))
        {
          if (!paramString1.equals("OUTBOX"))
          {
            if (paramString1.equals("SETTINGS")) {
              localObject = new SettingFragment();
            }
          }
          else {
            localObject = this.outboxFragment;
          }
        }
        else
        {
          Log.v("hitchernetWifiDirectActivity", "inbox fragment");
          localObject = this.inboxFragment;
        }
      }
      else
      {
        Log.v("hitchernetWifiDirectActivity", "device list fragment");
        localObject = this.fragmentList;
      }
    }
    Log.v("hitchernetWifiDirectActivity", "tag= " + paramString1);
    return localObject;
  }
  
  private void menuDrawerInitialization(Bundle paramBundle)
  {
    this.mMenuDrawer.setContentView(2130903054);
    this.mMenuDrawer.setTouchMode(2);
    this.mMenuDrawer.setSlideDrawable(2130837582);
    this.mMenuDrawer.setDrawerIndicatorEnabled(true);
    this.fragmentContainer = ((FrameLayout)findViewById(2131099709));
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setIcon(2131296266);
    this.mFragmentManager = getFragmentManager();
    if (paramBundle == null)
    {
      this.mCurrentFragmentTag = ((Item)this.mAdapter.getItem(0)).mUniqueCode;
      this.mCurrentMenuTag = ((Item)this.mAdapter.getItem(0)).mUniqueCode;
      attachFragment(this.fragmentContainer.getId(), getFragment(this.mCurrentFragmentTag), this.mCurrentFragmentTag);
      commitTransactions();
    }
    else
    {
      this.mCurrentFragmentTag = paramBundle.getString("net.simonvt.menudrawer.samples.FragmentSample");
    }
    this.mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener()
    {
      public boolean isViewDraggable(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        return paramAnonymousView instanceof SeekBar;
      }
    });
    createStacks();
  }
  
  private void onRefreshClicked()
  {
    new AlertDialog.Builder(this).setTitle("Are you sure you want to Refresh?").setMessage("Any ongoing connections and transfer might be interrupted").setPositiveButton("Yes,Refresh", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        if (WiFiDirectActivity.this.mService.isWifiP2PEnabled())
        {
          WiFiDirectActivity.this.fragmentList.clearPeers();
          WiFiDirectActivity.this.fragmentList.showProgressBar(true);
          WiFiDirectActivity.this.mService.startRegistrationAndDiscovery();
        }
        else
        {
          Toast.makeText(WiFiDirectActivity.this, 2131165289, 0).show();
          WiFiDirectActivity.this.startWifiDirect();
        }
      }
    }).setNegativeButton("No", null).show();
  }
  
  private void restartWifiDirect()
  {
    if (this.restartWifiDirectDialog == null)
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      final int i = Build.VERSION.SDK_INT;
      String str;
      if (i >= 16) {
        str = "WiFi";
      } else {
        str = "Wifi Direct?";
      }
      localBuilder.setTitle("Restart " + str);
      localBuilder.setMessage(str + " is Busy and you need to restart it to work properly.");
      localBuilder.setPositiveButton("Restart", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (i >= 16)
          {
            WifiManager localWifiManager = (WifiManager)WiFiDirectActivity.this.getSystemService("wifi");
            if (localWifiManager.isWifiEnabled())
            {
              Log.v("hitchernetWifiDirectActivity", "set to true");
              localWifiManager.setWifiEnabled(false);
              localWifiManager.setWifiEnabled(true);
              Log.v("hitchernetWifiDirectActivity", "set to false");
            }
          }
          else
          {
            WiFiDirectActivity.this.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
          }
        }
      });
      localBuilder.setNegativeButton("No", null);
      this.restartWifiDirectDialog = localBuilder.create();
    }
    if (!this.restartWifiDirectDialog.isShowing()) {
      this.restartWifiDirectDialog.show();
    }
  }
  
  private void startWifiDirect()
  {
    if ((this.restartWifiDirectDialog == null) || (!this.restartWifiDirectDialog.isShowing()))
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
      final int i = Build.VERSION.SDK_INT;
      String str;
      if (i >= 16) {
        str = "WiFi";
      } else {
        str = "Wifi Direct";
      }
      localBuilder.setTitle("Enable " + str + "?");
      localBuilder.setMessage(str + " should be started for  this application to work.");
      localBuilder.setPositiveButton("Start", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (i >= 16)
          {
            Utils.toast(WiFiDirectActivity.this, "Starting WiFi");
            WifiManager localWifiManager = (WifiManager)WiFiDirectActivity.this.getSystemService("wifi");
            if (!localWifiManager.isWifiEnabled()) {
              localWifiManager.setWifiEnabled(true);
            }
          }
          else
          {
            WiFiDirectActivity.this.startActivity(new Intent("android.settings.WIRELESS_SETTINGS"));
          }
        }
      });
      localBuilder.setNegativeButton("No", null);
      localBuilder.create();
      localBuilder.show();
    }
  }
  
  private void turnOnWifi()
  {
    WifiManager localWifiManager = (WifiManager)getSystemService("wifi");
    if (!localWifiManager.isWifiEnabled())
    {
      localWifiManager.setWifiEnabled(true);
      Utils.toast(this, "Turning on WiFi");
    }
  }
  
  public void addFragmentToStack(Fragment paramFragment)
  {
    ((List)this.fragmentStack.get(this.mCurrentMenuTag)).add(paramFragment);
  }
  
  protected void attachFragment(int paramInt, Fragment paramFragment, String paramString)
  {
    Log.v("hitchernetWifiDirectActivity", "inside attach fragment");
    if (paramFragment != null) {
      if (!paramFragment.isDetached())
      {
        if (!paramFragment.isAdded())
        {
          Log.v("hitchernetWifiDirectActivity", "else if");
          ensureTransaction();
          this.mFragmentTransaction.add(paramInt, paramFragment, paramString);
        }
      }
      else
      {
        Log.v("hitchernetWifiDirectActivity", "if");
        ensureTransaction();
        this.mFragmentTransaction.attach(paramFragment);
      }
    }
  }
  
  public void cancelConnect()
  {
    if (this.mBound) {
      this.mService.cancelConnect();
    }
  }
  
  protected void commitTransactions()
  {
    commitTransactions(false);
  }
  
  public void commitTransactions(boolean paramBoolean)
  {
    if ((this.mFragmentTransaction != null) && (!this.mFragmentTransaction.isEmpty()))
    {
      this.mFragmentTransaction.commit();
      this.mFragmentTransaction = null;
      Log.v("hitchernetWifiDirectActivity", "committed");
    }
  }
  
  public void connectP2p(WifiP2pDevice paramWifiP2pDevice)
  {
    if (this.mBound) {
      this.mService.connectP2p(paramWifiP2pDevice);
    }
  }
  
  public void createStackForMenu(String paramString)
  {
    ArrayList localArrayList = new ArrayList();
    this.fragmentStack.put(paramString, localArrayList);
  }
  
  protected void detachFragment(Fragment paramFragment)
  {
    if ((paramFragment != null) && (!paramFragment.isDetached()))
    {
      ensureTransaction();
      this.mFragmentTransaction.detach(paramFragment);
      Log.v("hitchernetWifiDirectActivity", "fragment detached");
    }
  }
  
  public void disconnect()
  {
    if (this.mBound) {
      this.mService.disconnect();
    }
  }
  
  public void dismisDialog()
  {
    dismissProgressDialog();
  }
  
  public void dismissProgressDialog()
  {
    Log.v("hitchernetWifiDirectActivity", "Dismiss Dialog");
    ProgressDialogFragment localProgressDialogFragment = (ProgressDialogFragment)getFragmentManager().findFragmentByTag("pDialog");
    if (localProgressDialogFragment != null) {
      localProgressDialogFragment.dismiss();
    }
  }
  
  protected FragmentTransaction ensureTransaction()
  {
    if (this.mFragmentTransaction == null)
    {
      this.mFragmentTransaction = this.mFragmentManager.beginTransaction();
      this.mFragmentTransaction.setTransition(8194);
    }
    return this.mFragmentTransaction;
  }
  
  protected int getDragMode()
  {
    return 0;
  }
  
  protected Position getDrawerPosition()
  {
    return Position.LEFT;
  }
  
  public void logv(String paramString)
  {
    Log.v("hitchernet" + getClass().getSimpleName(), paramString);
  }
  
  public void notifier(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2)
  {
    if (this.pDialogFragment != null) {
      this.pDialogFragment.updateDialog(paramLong, paramDouble, paramString1, paramBoolean1, paramBoolean2, paramFloat, paramString2);
    }
  }
  
  public void onBackPressed()
  {
    int i = this.mMenuDrawer.getDrawerState();
    if ((i != 8) && (i != 4)) {
      new AlertDialog.Builder(this).setTitle("Really Exit?").setMessage("Are you sure you want to exit?").setNegativeButton("No", null).setPositiveButton("Yes", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          WiFiDirectActivity.this.onBackPressed();
        }
      }).create().show();
    } else {
      this.mMenuDrawer.closeMenu();
    }
  }
  
  public void onConnectionInfoAvailable(WifiP2pInfo paramWifiP2pInfo)
  {
    logv("Connection information is available");
    if (this.fragmentList.isVisible()) {
      this.fragmentList.refreshAdapter();
    }
  }
  
  public void onConnectionTryFailure(int paramInt)
  {
    if (this.fragmentList.isVisible()) {
      this.fragmentList.onConnectionFailure(paramInt);
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.actionBar = getActionBar();
    this.actionBar.setDisplayShowTitleEnabled(false);
    this.fragmentList = new DeviceListFragment();
    this.inboxFragment = InboxOutboxFragment.newInstance(true);
    this.outboxFragment = InboxOutboxFragment.newInstance(false);
    menuDrawerInitialization(paramBundle);
    this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837562));
    addActionsToIntentFilters();
    bindService(new Intent(this, FileTransferService.class), this.mConnection, 1);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    if ((this.showRefreshButton) && (PeersInfo.getPeers().size() == 0)) {
      if ((!this.mBound) || (!this.mService.isDiscoveryGoingOn))
      {
        Log.v("hitchernetWifiDirectActivity", "on create menu. start discovery since no peers are available");
        if (this.mBound) {
          this.mService.startRegistrationAndDiscovery();
        }
      }
      else
      {
        showProgress(true);
      }
    }
    paramMenu.getItem(0).setVisible(this.showRefreshButton);
    return true;
  }
  
  protected void onDestroy()
  {
    super.onDestroy();
    if (this.mBound)
    {
      unbindService(this.mConnection);
      this.mBound = false;
    }
  }
  
  public boolean onDiscoveryClicked(boolean paramBoolean)
  {
    Log.v("hitchernetWifiDirectActivity", "refresh pressed");
    if (this.mService.isWifiP2PEnabled())
    {
      this.mService.startRegistrationAndDiscovery();
      Log.v("hitchernetWifiDirectActivity", "discovering peers ...");
    }
    else
    {
      Toast.makeText(this, 2131165289, 0).show();
      startWifiDirect();
    }
    return true;
  }
  
  public void onFailure(int paramInt)
  {
    Log.v("hitchernetWifiDirectActivity", "on failure reached" + paramInt);
    if (this.fragmentList.isVisible()) {
      this.fragmentList.showProgressBar(false);
    }
    if (paramInt == 2) {
      restartWifiDirect();
    }
    if (paramInt != 3) {
      new AlertDialog.Builder(this).setTitle("Refresh ?").setMessage("Refresh").setPositiveButton("OK", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          if (WiFiDirectActivity.this.mBound) {
            WiFiDirectActivity.this.mService.initializeManagerAndChannel();
          }
        }
      }).setNegativeButton("No", null);
    } else {
      restartWifiDirect();
    }
  }
  
  protected void onMenuItemClicked(int paramInt, Item paramItem)
  {
    this.mMenuDrawer.closeMenu();
    if (paramInt != 0)
    {
      if (paramInt != 1)
      {
        if (paramInt != 2)
        {
          if (paramInt != 3)
          {
            if (paramInt == 4)
            {
              startActivity(new Intent(this, WiFiDirectActivityDemo.class));
              return;
            }
          }
          else
          {
            this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837598));
            this.actionBar.setDisplayShowTitleEnabled(true);
            this.actionBar.setDisplayShowTitleEnabled(false);
          }
        }
        else
        {
          this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837591));
          this.actionBar.setDisplayShowTitleEnabled(true);
          this.actionBar.setDisplayShowTitleEnabled(false);
        }
      }
      else
      {
        this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837573));
        this.actionBar.setDisplayShowTitleEnabled(true);
        this.actionBar.setDisplayShowTitleEnabled(false);
      }
    }
    else
    {
      this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837562));
      this.actionBar.setDisplayShowTitleEnabled(true);
      this.actionBar.setDisplayShowTitleEnabled(false);
      if (PeersInfo.getPeers().size() == 0) {
        if ((!this.mBound) || (!this.mService.isDiscoveryGoingOn))
        {
          Log.v("hitchernetWifiDirectActivity", "on create menu. start discovery since no peers are available");
          this.mService.startRegistrationAndDiscovery();
        }
        else
        {
          showProgress(true);
        }
      }
    }
    if (!this.mCurrentMenuTag.equals(paramItem.mUniqueCode))
    {
      this.mCurrentMenuTag = paramItem.mUniqueCode;
      if (!this.mCurrentMenuTag.equals("HOME")) {
        this.showRefreshButton = false;
      } else {
        this.showRefreshButton = true;
      }
      addFrag(paramItem);
      invalidateOptionsMenu();
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    Log.v("item id=", paramMenuItem.getItemId());
    boolean bool;
    switch (paramMenuItem.getItemId())
    {
    case 16908332: 
      this.mMenuDrawer.toggleMenu();
      bool = true;
      break;
    case 2131099739: 
      onRefreshClicked();
    default: 
      bool = super.onOptionsItemSelected(paramMenuItem);
    }
    return bool;
  }
  
  public void onPeersAvailable(WifiP2pDeviceList paramWifiP2pDeviceList)
  {
    if (this.fragmentList.isVisible()) {
      this.fragmentList.onPeersAvailable(paramWifiP2pDeviceList);
    }
  }
  
  public void onPeersFound(Collection<WifiP2pDevice> paramCollection)
  {
    if ((this.fragmentList == null) || (!this.fragmentList.isVisible())) {
      logv("fragment is null ");
    } else {
      this.fragmentList.updatePeers(paramCollection);
    }
  }
  
  public void onResetData()
  {
    resetData();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("net.simonvt.menudrawer.samples.FragmentSample", this.mCurrentFragmentTag);
  }
  
  protected void onStart()
  {
    super.onStart();
    registerReceiver(this.receiver2, new IntentFilter("com.kharybdis.hitchernet.DISMISS_DIALOG"));
    registerReceiver(this.receiver2, new IntentFilter("com.kharybdis.hitchernet.filetransferservice.fileAbsoulutePath"));
    LocalBroadcastManager.getInstance(this).registerReceiver(this.clientServiceListener, this.intentsClientService);
    Globals.isActivityAlive = true;
  }
  
  protected void onStop()
  {
    unregisterReceiver(this.receiver2);
    LocalBroadcastManager.getInstance(this).unregisterReceiver(this.clientServiceListener);
    Globals.isActivityAlive = false;
    super.onStop();
  }
  
  public void onWDDisabled(boolean paramBoolean)
  {
    if ((this.restartWifiDirectDialog == null) || (!this.restartWifiDirectDialog.isShowing())) {
      if (!paramBoolean)
      {
        if ((this.enableWifiDialog != null) && (this.enableWifiDialog.isShowing())) {
          this.enableWifiDialog.dismiss();
        }
      }
      else if ((this.enableWifiDialog == null) || (!this.enableWifiDialog.isShowing()))
      {
        this.enableWifiDialog = new AlertDialog.Builder(this).setTitle("Please Turn On WiFi Direct").setMessage("We need Wifi/Wifi Direct to be turned on for this application to work.").setPositiveButton("Turn On", new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
          {
            WiFiDirectActivity.this.turnOnWifi();
          }
        }).setNegativeButton("No", null).create();
        this.enableWifiDialog.show();
      }
    }
  }
  
  public void renameDevice(String paramString, WifiP2pManager.ActionListener paramActionListener)
  {
    if (this.mBound) {
      this.mService.rename(paramString, paramActionListener);
    }
  }
  
  public void resetData()
  {
    Log.v("hitchernetWifiDirectActivity", "Resetting data");
    if ((this.fragmentList != null) && (this.fragmentList.isVisible()))
    {
      Log.e("hitchernetWifiDirectActivity", "should clear here");
      this.fragmentList.clearPeers();
    }
  }
  
  public void showDialog(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2)
  {
    Log.v("hitchernetWifiDirectActivity", "show dialog");
    showProgressDialog();
  }
  
  public void showProgress(boolean paramBoolean)
  {
    if (this.fragmentList.isVisible()) {
      this.fragmentList.showProgressBar(paramBoolean);
    }
  }
  
  public void showProgressDialog()
  {
    FragmentManager localFragmentManager = getFragmentManager();
    this.pDialogFragment = new ProgressDialogFragment();
    this.pDialogFragment.show(localFragmentManager, "pDialog");
  }
  
  public void startFileTransfer(List<LocalFile> paramList, String paramString)
  {
    if (this.mBound) {
      this.mService.startFileTransfer(paramList, paramString);
    }
  }
  
  public void updateDevice(WifiP2pDevice paramWifiP2pDevice)
  {
    if (this.fragmentList.isVisible()) {
      this.fragmentList.updateThisDevice(paramWifiP2pDevice);
    }
  }
  
  public void updateDialog(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2)
  {
    Object localObject2 = new Message();
    Object localObject1 = new Bundle();
    ((Bundle)localObject1).putDouble("percent", paramDouble);
    ((Bundle)localObject1).putString("fileName", paramString1);
    ((Bundle)localObject1).putBoolean("isCompleted", paramBoolean1);
    ((Bundle)localObject1).putBoolean("isDownloading", paramBoolean2);
    ((Bundle)localObject1).putFloat("speedMbps", paramFloat);
    ((Bundle)localObject1).putString("timeRemaining", paramString2);
    ((Message)localObject2).setData((Bundle)localObject1);
    this.handler.sendMessage((Message)localObject2);
    if (!paramBoolean2) {
      localObject1 = this.outboxFragment;
    } else {
      localObject1 = this.inboxFragment;
    }
    if (((InboxOutboxFragment)localObject1).isVisible())
    {
      localObject1 = (InboxOutboxAdapter)((InboxOutboxFragment)localObject1).getListAdapter();
      localObject2 = DbHelper.getInstance(this);
      boolean bool;
      if (!paramBoolean2) {
        bool = false;
      } else {
        bool = true;
      }
      ((InboxOutboxAdapter)localObject1).data = ((DbHelper)localObject2).getData(bool);
      ((InboxOutboxAdapter)localObject1).notifyDataSetChanged();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.WiFiDirectActivity
 * JD-Core Version:    0.7.0.1
 */