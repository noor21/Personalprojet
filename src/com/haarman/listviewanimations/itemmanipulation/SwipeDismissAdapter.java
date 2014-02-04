package com.haarman.listviewanimations.itemmanipulation;

import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.BaseAdapterDecorator;

public class SwipeDismissAdapter
  extends BaseAdapterDecorator
{
  private OnDismissCallback mCallback;
  
  public SwipeDismissAdapter(BaseAdapter paramBaseAdapter, OnDismissCallback paramOnDismissCallback)
  {
    super(paramBaseAdapter);
    this.mCallback = paramOnDismissCallback;
  }
  
  public void setAbsListView(AbsListView paramAbsListView)
  {
    super.setAbsListView(paramAbsListView);
    paramAbsListView.setOnTouchListener(new SwipeDismissListViewTouchListener(paramAbsListView, this.mCallback));
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.SwipeDismissAdapter
 * JD-Core Version:    0.7.0.1
 */