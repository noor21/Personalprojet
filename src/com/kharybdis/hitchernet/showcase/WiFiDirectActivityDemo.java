package com.kharybdis.hitchernet.showcase;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.github.espiandev.showcaseview.ShowcaseView;
import com.github.espiandev.showcaseview.ShowcaseView.ConfigOptions;
import com.github.espiandev.showcaseview.ShowcaseView.OnShowcaseEventListener;
import com.kharybdis.hitchernet.Preferences;
import com.kharybdis.hitchernet.WiFiDirectActivity;
import com.kharybdis.hitchernet.menudrawer.BaseListSample;
import com.kharybdis.hitchernet.menudrawer.Item;
import java.io.PrintStream;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.OnInterceptMoveEventListener;
import net.simonvt.menudrawer.Position;

public class WiFiDirectActivityDemo
  extends BaseListSample
{
  private static final float SHOWCASE_KITTEN_SCALE = 1.2F;
  private static final float SHOWCASE_LIKE_SCALE = 0.5F;
  private static final String STATE_CURRENT_FRAGMENT = "net.simonvt.menudrawer.samples.FragmentSample";
  private static final String TAG = WiFiDirectActivityDemo.class.getSimpleName();
  ActionBar actionBar;
  Actions actions = new Actions();
  private FrameLayout fragmentContainer;
  private String mCurrentFragmentTag;
  private String mCurrentMenuTag = "";
  ShowcaseView.ConfigOptions mOptions = new ShowcaseView.ConfigOptions();
  private boolean showRefreshButton = true;
  ShowcaseView svConnnect;
  ShowcaseView svFile;
  ShowcaseView svHome;
  ShowcaseView svInbox;
  ShowcaseView svInboxScreen;
  ShowcaseView svMainMenu;
  ShowcaseView svOutbox;
  ShowcaseView svOutboxScreen;
  ShowcaseView svRefresh;
  ShowcaseView svSettingDevice;
  ShowcaseView svSettingFolder;
  ShowcaseView svSettings;
  ShowcaseView svTutorial;
  
  private void addScreen(String paramString)
  {
    int i;
    if (!TextUtils.equals(paramString, "HOME"))
    {
      if (!TextUtils.equals(paramString, "INBOX"))
      {
        if (!TextUtils.equals(paramString, "OUTBOX"))
        {
          if (!TextUtils.equals(paramString, "SETTINGS")) {
            i = 2130903052;
          } else {
            i = 2130903070;
          }
        }
        else {
          i = 2130903067;
        }
      }
      else {
        i = 2130903058;
      }
    }
    else {
      i = 2130903052;
    }
    View localView = getLayoutInflater().inflate(i, null);
    this.fragmentContainer.addView(localView);
  }
  
  private void menuDrawerInitialization(Bundle paramBundle)
  {
    this.mMenuDrawer.setContentView(2130903055);
    this.mMenuDrawer.setTouchMode(0);
    this.mMenuDrawer.setSlideDrawable(2130837582);
    this.mMenuDrawer.setDrawerIndicatorEnabled(true);
    this.fragmentContainer = ((FrameLayout)findViewById(2131099709));
    View localView = getLayoutInflater().inflate(2130903054, null);
    this.fragmentContainer.addView(localView);
    getActionBar().setDisplayHomeAsUpEnabled(true);
    getActionBar().setIcon(2131296266);
    this.mMenuDrawer.setOnInterceptMoveEventListener(new MenuDrawer.OnInterceptMoveEventListener()
    {
      public boolean isViewDraggable(View paramAnonymousView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        return paramAnonymousView instanceof SeekBar;
      }
    });
  }
  
  private void onRefreshClicked()
  {
    addScreen("HOME");
    setAppropriateAction();
  }
  
  private void setAppropriateAction()
  {
    Object localObject;
    switch (this.actions.getSelected())
    {
    case 1: 
      this.actions.setSelected(2);
      this.svConnnect = ShowcaseView.insertShowcaseView((ImageView)findViewById(2131099700), this, getString(2131165254), getString(2131165255), this.mOptions);
      this.svConnnect.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svConnnect.show();
      break;
    case 2: 
      this.actions.setSelected(3);
      localObject = (ImageView)findViewById(2131099703);
      this.svFile = ShowcaseView.insertShowcaseView((View)localObject, this, getString(2131165256), getString(2131165257), this.mOptions);
      this.svFile.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svFile.show();
      ((ImageView)findViewById(2131099699)).setImageResource(2130837553);
      ((ImageView)localObject).setVisibility(0);
      ((TextView)findViewById(2131099702)).setText("CONNECTED");
      break;
    case 3: 
      this.actions.setSelected(4);
      this.svMainMenu = ShowcaseView.insertShowcaseViewWithType(0, 16908332, this, 2131165258, 2131165259, this.mOptions);
      this.svMainMenu.setShowcaseItem(0, 0, this);
      this.svMainMenu.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          System.out.println("main menu showcase");
          WiFiDirectActivityDemo.this.mMenuDrawer.openMenu(false);
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svMainMenu.show();
      break;
    case 4: 
      this.actions.setSelected(6);
      this.svInbox = ShowcaseView.insertShowcaseView(((ViewGroup)this.mMenuDrawer.getMenuView()).getChildAt(1), this, getString(2131165260), getString(2131165261), this.mOptions);
      this.svInbox.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svInbox.show();
      break;
    case 6: 
      this.actions.setSelected(10);
      localObject = getLayoutInflater().inflate(2130903058, null);
      this.fragmentContainer.addView((View)localObject);
      this.mMenuDrawer.closeMenu(false);
      this.svInboxScreen = ShowcaseView.insertShowcaseView(findViewById(2131099716), this, getString(2131165262), getString(2131165263), this.mOptions);
      this.svInboxScreen.show();
      this.svInboxScreen.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.mMenuDrawer.openMenu(false);
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      break;
    case 7: 
      this.actions.setSelected(11);
      localObject = getLayoutInflater().inflate(2130903067, null);
      this.fragmentContainer.addView((View)localObject);
      this.mMenuDrawer.closeMenu(false);
      this.svOutboxScreen = ShowcaseView.insertShowcaseView(findViewById(2131099716), this, getString(2131165266), getString(2131165267), this.mOptions);
      this.svOutboxScreen.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.onBackPressed();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svOutboxScreen.show();
      break;
    case 8: 
      this.actions.setSelected(15);
      localObject = getLayoutInflater().inflate(2130903071, null);
      this.fragmentContainer.addView((View)localObject);
      this.svSettingFolder = ShowcaseView.insertShowcaseView(findViewById(2131099732), this, getString(2131165272), getString(2131165273), this.mOptions);
      this.svSettingFolder.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svSettingFolder.show();
      break;
    case 10: 
      this.actions.setSelected(7);
      this.svOutbox = ShowcaseView.insertShowcaseView(((ViewGroup)this.mMenuDrawer.getMenuView()).getChildAt(2), this, getString(2131165264), getString(2131165265), this.mOptions);
      this.svOutbox.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svOutbox.show();
      break;
    case 11: 
      this.actions.setSelected(8);
      this.svSettings = ShowcaseView.insertShowcaseView(((ViewGroup)this.mMenuDrawer.getMenuView()).getChildAt(3), this, getString(2131165268), getString(2131165269), this.mOptions);
      this.svSettings.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.mMenuDrawer.closeMenu(false);
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svSettings.show();
      break;
    case 14: 
      this.actions.setSelected(9);
      this.svTutorial = ShowcaseView.insertShowcaseView(((ViewGroup)this.mMenuDrawer.getMenuView()).getChildAt(4), this, getString(2131165276), getString(2131165277), this.mOptions);
      this.svTutorial.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.onBackPressed();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svTutorial.show();
      break;
    case 15: 
      this.actions.setSelected(14);
      this.svSettingDevice = ShowcaseView.insertShowcaseView(findViewById(2131099735), this, getString(2131165270), getString(2131165271), this.mOptions);
      this.svSettingDevice.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
      {
        public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
        {
          WiFiDirectActivityDemo.this.mMenuDrawer.openMenu(false);
          WiFiDirectActivityDemo.this.setAppropriateAction();
        }
        
        public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
      });
      this.svSettingDevice.show();
    }
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
  
  public void onBackPressed()
  {
    if (!getIntent().getBooleanExtra("isFirstStart", false))
    {
      finish();
    }
    else
    {
      new Preferences(this).setFirstStart(false);
      finish();
      startActivity(new Intent(this, WiFiDirectActivity.class));
    }
  }
  
  public void onConnectClicked(View paramView)
  {
    this.svConnnect.hide();
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.actionBar = getActionBar();
    this.actionBar.setDisplayShowTitleEnabled(false);
    menuDrawerInitialization(paramBundle);
    this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837562));
    this.mOptions.block = true;
    this.mOptions.hideOnClickOutside = false;
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(2131558400, paramMenu);
    paramMenu.getItem(0).setVisible(this.showRefreshButton);
    this.svRefresh = ShowcaseView.insertShowcaseViewWithType(3, 2131099739, this, 2131165252, 2131165253, this.mOptions);
    this.svRefresh.setOnShowcaseEventListener(new ShowcaseView.OnShowcaseEventListener()
    {
      public void onShowcaseViewHide(ShowcaseView paramAnonymousShowcaseView)
      {
        WiFiDirectActivityDemo.this.onRefreshClicked();
      }
      
      public void onShowcaseViewShow(ShowcaseView paramAnonymousShowcaseView) {}
    });
    this.svRefresh.setShowcaseItem(3, 2131099739, this);
    return true;
  }
  
  public void onDeviceClicked(View paramView)
  {
    System.out.println("device clicked");
    this.svSettingDevice.hide();
  }
  
  public void onFileSelectClicked(View paramView)
  {
    this.svFile.hide();
  }
  
  public void onFolderClicked(View paramView)
  {
    System.out.println("folder clicked");
    this.svSettingFolder.hide();
  }
  
  public void onInboxClicked(View paramView)
  {
    System.out.println("inbox");
    this.svInboxScreen.hide();
  }
  
  protected void onMenuItemClicked(int paramInt, Item paramItem)
  {
    if (paramInt != 0)
    {
      if (paramInt != 1)
      {
        if (paramInt != 2)
        {
          if (paramInt != 3)
          {
            if (paramInt == 4) {
              this.svTutorial.hide();
            }
          }
          else
          {
            this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837598));
            this.actionBar.setDisplayShowTitleEnabled(true);
            this.actionBar.setDisplayShowTitleEnabled(false);
            this.svSettings.hide();
          }
        }
        else
        {
          this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837591));
          this.actionBar.setDisplayShowTitleEnabled(true);
          this.actionBar.setDisplayShowTitleEnabled(false);
          this.svOutbox.hide();
        }
      }
      else
      {
        this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837573));
        this.actionBar.setDisplayShowTitleEnabled(true);
        this.actionBar.setDisplayShowTitleEnabled(false);
        this.svInbox.hide();
      }
    }
    else
    {
      this.actionBar.setBackgroundDrawable(getResources().getDrawable(2130837562));
      this.actionBar.setDisplayShowTitleEnabled(true);
      this.actionBar.setDisplayShowTitleEnabled(false);
      this.svHome.hide();
    }
    if (!this.mCurrentMenuTag.equals(paramItem.mUniqueCode))
    {
      this.mCurrentMenuTag = paramItem.mUniqueCode;
      if (!this.mCurrentMenuTag.equals("HOME")) {
        this.showRefreshButton = false;
      } else {
        this.showRefreshButton = true;
      }
      addScreen(paramItem.mUniqueCode);
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    switch (paramMenuItem.getItemId())
    {
    case 16908332: 
      this.mMenuDrawer.toggleMenu(false);
      this.svMainMenu.hide();
      break;
    case 2131099739: 
      this.svRefresh.hide();
    }
    return true;
  }
  
  public void onOutboxClicked(View paramView)
  {
    System.out.println("outbox clicked");
    this.svOutboxScreen.hide();
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putString("net.simonvt.menudrawer.samples.FragmentSample", this.mCurrentFragmentTag);
  }
  
  public class Actions
  {
    public static final int CONNECT_CLICKED = 2;
    public static final int FILE_CLICKED = 3;
    public static final int HOME_CLICKED = 5;
    public static final int INBOX_CLICKED = 6;
    public static final int INBOX_SHOWN = 10;
    public static final int MAIN_MENU_CLICKED = 4;
    public static final int OUTBOX_CLICKED = 7;
    public static final int OUTBOX_SHOWN = 11;
    public static final int REFRESH_CLICKED = 1;
    public static final int SETTINGS_CLICKED = 8;
    public static final int SETTINGS_DEVICE = 14;
    public static final int SETTINGS_SAVE_LOCATION = 15;
    public static final int SETTINGS_SHOWN = 12;
    public static final int TUTORIAL_CLICKED = 9;
    private int selected = 1;
    
    public Actions() {}
    
    public int getSelected()
    {
      return this.selected;
    }
    
    public void setSelected(int paramInt)
    {
      this.selected = paramInt;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.showcase.WiFiDirectActivityDemo
 * JD-Core Version:    0.7.0.1
 */