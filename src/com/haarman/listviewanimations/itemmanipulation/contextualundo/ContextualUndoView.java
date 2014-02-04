package com.haarman.listviewanimations.itemmanipulation.contextualundo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

@SuppressLint({"ViewConstructor"})
public class ContextualUndoView
  extends FrameLayout
{
  private View mContentView;
  private long mItemId;
  private View mUndoView;
  
  public ContextualUndoView(Context paramContext, int paramInt)
  {
    super(paramContext);
    initUndo(paramInt);
  }
  
  private void initUndo(int paramInt)
  {
    this.mUndoView = View.inflate(getContext(), paramInt, null);
    addView(this.mUndoView);
  }
  
  public void displayContentView()
  {
    this.mContentView.setVisibility(0);
    this.mUndoView.setVisibility(8);
  }
  
  public void displayUndo()
  {
    this.mContentView.setVisibility(8);
    this.mUndoView.setVisibility(0);
  }
  
  public View getContentView()
  {
    return this.mContentView;
  }
  
  public long getItemId()
  {
    return this.mItemId;
  }
  
  public boolean isContentDisplayed()
  {
    boolean bool;
    if (this.mContentView.getVisibility() != 0) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void setItemId(long paramLong)
  {
    this.mItemId = paramLong;
  }
  
  public void updateContentView(View paramView)
  {
    if (this.mContentView == null) {
      addView(paramView);
    }
    this.mContentView = paramView;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoView
 * JD-Core Version:    0.7.0.1
 */