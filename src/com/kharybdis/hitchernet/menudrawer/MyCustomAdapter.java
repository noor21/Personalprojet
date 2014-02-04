package com.kharybdis.hitchernet.menudrawer;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class MyCustomAdapter
  extends BaseExpandableListAdapter
{
  private LayoutInflater inflater;
  private ArrayList<Parent> mParent;
  
  public MyCustomAdapter(Context paramContext, ArrayList<Parent> paramArrayList)
  {
    this.mParent = paramArrayList;
    this.inflater = LayoutInflater.from(paramContext);
  }
  
  public Object getChild(int paramInt1, int paramInt2)
  {
    return ((Parent)this.mParent.get(paramInt1)).getArrayChildren().get(paramInt2);
  }
  
  public long getChildId(int paramInt1, int paramInt2)
  {
    return paramInt2;
  }
  
  public View getChildView(int paramInt1, int paramInt2, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = this.inflater.inflate(2130903062, paramViewGroup, false);
    }
    TextView localTextView = (TextView)paramView.findViewById(2131099720);
    if ((paramInt1 != 0) && (paramInt1 != 1) && (paramInt1 != 6))
    {
      localTextView.setPaintFlags(0x8 | localTextView.getPaintFlags());
      localTextView.setText((CharSequence)((Parent)this.mParent.get(paramInt1)).getArrayChildren().get(paramInt2));
    }
    else
    {
      localTextView.setPaintFlags(0xFFFFFFF7 & localTextView.getPaintFlags());
      localTextView.setText((CharSequence)((Parent)this.mParent.get(paramInt1)).getArrayChildren().get(paramInt2));
    }
    return paramView;
  }
  
  public int getChildrenCount(int paramInt)
  {
    return ((Parent)this.mParent.get(paramInt)).getArrayChildren().size();
  }
  
  public Object getGroup(int paramInt)
  {
    return ((Parent)this.mParent.get(paramInt)).getTitle();
  }
  
  public int getGroupCount()
  {
    return this.mParent.size();
  }
  
  public long getGroupId(int paramInt)
  {
    return paramInt;
  }
  
  public View getGroupView(int paramInt, boolean paramBoolean, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null) {
      paramView = this.inflater.inflate(2130903063, paramViewGroup, false);
    }
    ((TextView)paramView.findViewById(2131099722)).setText(getGroup(paramInt).toString());
    return paramView;
  }
  
  public boolean hasStableIds()
  {
    return true;
  }
  
  public boolean isChildSelectable(int paramInt1, int paramInt2)
  {
    return true;
  }
  
  public void registerDataSetObserver(DataSetObserver paramDataSetObserver)
  {
    super.registerDataSetObserver(paramDataSetObserver);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.menudrawer.MyCustomAdapter
 * JD-Core Version:    0.7.0.1
 */