package com.kharybdis.hitchernet.menudrawer;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import net.simonvt.menudrawer.MenuDrawer;
import net.simonvt.menudrawer.MenuDrawer.Type;
import net.simonvt.menudrawer.Position;

public abstract class BaseListSample
  extends Activity
  implements MenuAdapter.MenuListener
{
  private static final String STATE_ACTIVE_POSITION = "net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition";
  private int mActivePosition = 0;
  protected MenuAdapter mAdapter;
  private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      BaseListSample.this.mActivePosition = paramAnonymousInt;
      BaseListSample.this.mMenuDrawer.setActiveView(paramAnonymousView, paramAnonymousInt);
      BaseListSample.this.mAdapter.setActivePosition(paramAnonymousInt);
      BaseListSample.this.onMenuItemClicked(paramAnonymousInt, (Item)BaseListSample.this.mAdapter.getItem(paramAnonymousInt));
    }
  };
  protected ListView mList;
  protected MenuDrawer mMenuDrawer;
  
  protected abstract int getDragMode();
  
  protected abstract Position getDrawerPosition();
  
  public void onActiveViewChanged(View paramView)
  {
    this.mMenuDrawer.setActiveView(paramView, this.mActivePosition);
  }
  
  protected void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    if (paramBundle != null) {
      this.mActivePosition = paramBundle.getInt("net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition");
    }
    this.mMenuDrawer = MenuDrawer.attach(this, MenuDrawer.Type.BEHIND, getDrawerPosition(), 0);
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(new Item("HOME", "HOME", 2130837564));
    localArrayList.add(new Item("INBOX", "INBOX", 2130837572));
    localArrayList.add(new Item("OUTBOX", "OUTBOX", 2130837590));
    localArrayList.add(new Item("SETTINGS", "SETTINGS", 2130837597));
    localArrayList.add(new Item("HELP", "HELP", 2130837563));
    this.mList = new ListView(this);
    this.mAdapter = new MenuAdapter(this, localArrayList);
    this.mAdapter.setListener(this);
    this.mAdapter.setActivePosition(this.mActivePosition);
    this.mList.setAdapter(this.mAdapter);
    this.mList.setOnItemClickListener(this.mItemClickListener);
    this.mMenuDrawer.setMenuView(this.mList);
  }
  
  protected abstract void onMenuItemClicked(int paramInt, Item paramItem);
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    super.onSaveInstanceState(paramBundle);
    paramBundle.putInt("net.simonvt.menudrawer.samples.LeftDrawerSample.activePosition", this.mActivePosition);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.menudrawer.BaseListSample
 * JD-Core Version:    0.7.0.1
 */