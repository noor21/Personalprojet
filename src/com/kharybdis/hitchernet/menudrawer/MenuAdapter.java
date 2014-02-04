package com.kharybdis.hitchernet.menudrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;

public class MenuAdapter
  extends BaseAdapter
{
  private int mActivePosition = -1;
  private Context mContext;
  private List<Object> mItems;
  private MenuListener mListener;
  
  public MenuAdapter(Context paramContext, List<Object> paramList)
  {
    this.mContext = paramContext;
    this.mItems = paramList;
  }
  
  public boolean areAllItemsEnabled()
  {
    return false;
  }
  
  public int getCount()
  {
    return this.mItems.size();
  }
  
  public Object getItem(int paramInt)
  {
    return this.mItems.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int getItemViewType(int paramInt)
  {
    int i;
    if (!(getItem(paramInt) instanceof Item)) {
      i = 1;
    } else {
      i = 0;
    }
    return i;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    View localView = paramView;
    Object localObject = getItem(paramInt);
    if (!(localObject instanceof Category))
    {
      if (localView == null) {
        localView = LayoutInflater.from(this.mContext).inflate(2130903066, paramViewGroup, false);
      }
      TextView localTextView = (TextView)localView;
      localTextView.setText(((Item)localObject).mTitle);
      localTextView.setCompoundDrawablesWithIntrinsicBounds(((Item)localObject).mIconRes, 0, 0, 0);
    }
    else
    {
      if (localView == null) {
        localView = LayoutInflater.from(this.mContext).inflate(2130903065, paramViewGroup, false);
      }
      ((TextView)localView).setText(((Category)localObject).mTitle);
    }
    localView.setTag(2131099660, Integer.valueOf(paramInt));
    if (paramInt == this.mActivePosition) {
      this.mListener.onActiveViewChanged(localView);
    }
    return localView;
  }
  
  public int getViewTypeCount()
  {
    return 2;
  }
  
  public boolean isEnabled(int paramInt)
  {
    return getItem(paramInt) instanceof Item;
  }
  
  public void setActivePosition(int paramInt)
  {
    this.mActivePosition = paramInt;
  }
  
  public void setListener(MenuListener paramMenuListener)
  {
    this.mListener = paramMenuListener;
  }
  
  public static abstract interface MenuListener
  {
    public abstract void onActiveViewChanged(View paramView);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.menudrawer.MenuAdapter
 * JD-Core Version:    0.7.0.1
 */