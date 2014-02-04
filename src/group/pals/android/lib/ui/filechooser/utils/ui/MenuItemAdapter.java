package group.pals.android.lib.ui.filechooser.utils.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import group.pals.android.lib.ui.filechooser.R.dimen;
import group.pals.android.lib.ui.filechooser.R.layout;

public class MenuItemAdapter
  extends BaseAdapter
{
  private final Context mContext;
  private final int mItemPaddingLeft;
  private final Integer[] mItems;
  private final int mPadding;
  
  public MenuItemAdapter(Context paramContext, Integer[] paramArrayOfInteger)
  {
    this.mContext = paramContext;
    this.mItems = paramArrayOfInteger;
    this.mPadding = this.mContext.getResources().getDimensionPixelSize(R.dimen.afc_5dp);
    this.mItemPaddingLeft = this.mContext.getResources().getDimensionPixelSize(R.dimen.afc_context_menu_item_padding_left);
  }
  
  public int getCount()
  {
    return this.mItems.length;
  }
  
  public Object getItem(int paramInt)
  {
    return this.mItems[paramInt];
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = LayoutInflater.from(this.mContext).inflate(R.layout.afc_context_menu_tiem, null);
    }
    ((TextView)paramView).setText(this.mItems[paramInt].intValue());
    paramView.setPadding(this.mItemPaddingLeft, this.mPadding, this.mPadding, this.mPadding);
    return paramView;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.ui.MenuItemAdapter
 * JD-Core Version:    0.7.0.1
 */