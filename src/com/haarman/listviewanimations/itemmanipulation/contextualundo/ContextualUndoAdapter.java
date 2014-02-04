package com.haarman.listviewanimations.itemmanipulation.contextualundo;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.RecyclerListener;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.BaseAdapterDecorator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContextualUndoAdapter
  extends BaseAdapterDecorator
  implements ContextualUndoListViewTouchListener.Callback
{
  private Map<View, Animator> mActiveAnimators = new ConcurrentHashMap();
  private final int mAnimationTime = 150;
  private long mCurrentRemovedId;
  private ContextualUndoView mCurrentRemovedView;
  private DeleteItemCallback mDeleteItemCallback;
  private final int mUndoActionId;
  private final int mUndoLayoutId;
  
  public ContextualUndoAdapter(BaseAdapter paramBaseAdapter, int paramInt1, int paramInt2)
  {
    super(paramBaseAdapter);
    this.mUndoLayoutId = paramInt1;
    this.mUndoActionId = paramInt2;
    this.mCurrentRemovedId = -1L;
  }
  
  private void clearCurrentRemovedView()
  {
    this.mCurrentRemovedView = null;
    this.mCurrentRemovedId = -1L;
  }
  
  private void performRemoval()
  {
    Object localObject = new int[2];
    localObject[0] = this.mCurrentRemovedView.getHeight();
    localObject[1] = 1;
    localObject = ValueAnimator.ofInt((int[])localObject).setDuration(150L);
    ((ValueAnimator)localObject).addListener(new RemoveViewAnimatorListenerAdapter(this.mCurrentRemovedView));
    ((ValueAnimator)localObject).addUpdateListener(new RemoveViewAnimatorUpdateListener(this.mCurrentRemovedView));
    ((ValueAnimator)localObject).start();
    this.mActiveAnimators.put(this.mCurrentRemovedView, localObject);
    clearCurrentRemovedView();
  }
  
  private void removePreviousContextualUndoIfPresent()
  {
    if (this.mCurrentRemovedView != null) {
      performRemoval();
    }
  }
  
  private void restoreViewPosition(View paramView)
  {
    ViewHelper.setAlpha(paramView, 1.0F);
    ViewHelper.setTranslationX(paramView, 0.0F);
  }
  
  private void setCurrentRemovedView(ContextualUndoView paramContextualUndoView)
  {
    this.mCurrentRemovedView = paramContextualUndoView;
    this.mCurrentRemovedId = paramContextualUndoView.getItemId();
  }
  
  public final View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ContextualUndoView localContextualUndoView = (ContextualUndoView)paramView;
    if (localContextualUndoView == null)
    {
      localContextualUndoView = new ContextualUndoView(paramViewGroup.getContext(), this.mUndoLayoutId);
      localContextualUndoView.findViewById(this.mUndoActionId).setOnClickListener(new UndoListener(localContextualUndoView));
    }
    localContextualUndoView.updateContentView(super.getView(paramInt, localContextualUndoView.getContentView(), paramViewGroup));
    long l = getItemId(paramInt);
    if (l != this.mCurrentRemovedId)
    {
      localContextualUndoView.displayContentView();
    }
    else
    {
      localContextualUndoView.displayUndo();
      this.mCurrentRemovedView = localContextualUndoView;
    }
    localContextualUndoView.setItemId(l);
    return localContextualUndoView;
  }
  
  public void onListScrolled()
  {
    if (this.mCurrentRemovedView != null) {
      performRemoval();
    }
  }
  
  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    this.mCurrentRemovedId = ((Bundle)paramParcelable).getLong("mCurrentRemovedId", -1L);
  }
  
  public Parcelable onSaveInstanceState()
  {
    Bundle localBundle = new Bundle();
    localBundle.putLong("mCurrentRemovedId", this.mCurrentRemovedId);
    return localBundle;
  }
  
  public void onViewSwiped(View paramView, int paramInt)
  {
    ContextualUndoView localContextualUndoView = (ContextualUndoView)paramView;
    if (!localContextualUndoView.isContentDisplayed())
    {
      if (this.mCurrentRemovedView != null) {
        performRemoval();
      }
    }
    else
    {
      restoreViewPosition(localContextualUndoView);
      localContextualUndoView.displayUndo();
      removePreviousContextualUndoIfPresent();
      setCurrentRemovedView(localContextualUndoView);
    }
  }
  
  public void setAbsListView(AbsListView paramAbsListView)
  {
    super.setAbsListView(paramAbsListView);
    ContextualUndoListViewTouchListener localContextualUndoListViewTouchListener = new ContextualUndoListViewTouchListener(paramAbsListView, this);
    paramAbsListView.setOnTouchListener(localContextualUndoListViewTouchListener);
    paramAbsListView.setOnScrollListener(localContextualUndoListViewTouchListener.makeScrollListener());
    paramAbsListView.setRecyclerListener(new RecycleViewListener(null));
  }
  
  public void setDeleteItemCallback(DeleteItemCallback paramDeleteItemCallback)
  {
    this.mDeleteItemCallback = paramDeleteItemCallback;
  }
  
  public static abstract interface DeleteItemCallback
  {
    public abstract void deleteItem(int paramInt);
  }
  
  private class RecycleViewListener
    implements AbsListView.RecyclerListener
  {
    private RecycleViewListener() {}
    
    public void onMovedToScrapHeap(View paramView)
    {
      Animator localAnimator = (Animator)ContextualUndoAdapter.this.mActiveAnimators.get(paramView);
      if (localAnimator != null) {
        localAnimator.cancel();
      }
    }
  }
  
  private class RemoveViewAnimatorListenerAdapter
    extends AnimatorListenerAdapter
  {
    private final View mDismissView;
    private final int mOriginalHeight;
    
    public RemoveViewAnimatorListenerAdapter(View paramView)
    {
      this.mDismissView = paramView;
      this.mOriginalHeight = paramView.getHeight();
    }
    
    private void deleteCurrentItem()
    {
      ContextualUndoView localContextualUndoView = (ContextualUndoView)this.mDismissView;
      int i = ContextualUndoAdapter.this.getAbsListView().getPositionForView(localContextualUndoView);
      ContextualUndoAdapter.this.mDeleteItemCallback.deleteItem(i);
    }
    
    private void restoreViewDimension(View paramView)
    {
      ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
      localLayoutParams.height = this.mOriginalHeight;
      paramView.setLayoutParams(localLayoutParams);
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      ContextualUndoAdapter.this.mActiveAnimators.remove(this.mDismissView);
      ContextualUndoAdapter.this.restoreViewPosition(this.mDismissView);
      restoreViewDimension(this.mDismissView);
      deleteCurrentItem();
    }
  }
  
  private class RemoveViewAnimatorUpdateListener
    implements ValueAnimator.AnimatorUpdateListener
  {
    private final View mDismissView;
    private final ViewGroup.LayoutParams mLayoutParams;
    
    public RemoveViewAnimatorUpdateListener(View paramView)
    {
      this.mDismissView = paramView;
      this.mLayoutParams = paramView.getLayoutParams();
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      this.mLayoutParams.height = ((Integer)paramValueAnimator.getAnimatedValue()).intValue();
      this.mDismissView.setLayoutParams(this.mLayoutParams);
    }
  }
  
  private class UndoListener
    implements View.OnClickListener
  {
    private final ContextualUndoView mContextualUndoView;
    
    public UndoListener(ContextualUndoView paramContextualUndoView)
    {
      this.mContextualUndoView = paramContextualUndoView;
    }
    
    private void animateViewComingBack()
    {
      ViewPropertyAnimator.animate(this.mContextualUndoView).translationX(0.0F).setDuration(150L).setListener(null);
    }
    
    private void moveViewOffScreen()
    {
      ViewHelper.setTranslationX(this.mContextualUndoView, this.mContextualUndoView.getWidth());
    }
    
    public void onClick(View paramView)
    {
      ContextualUndoAdapter.this.clearCurrentRemovedView();
      this.mContextualUndoView.displayContentView();
      moveViewOffScreen();
      animateViewComingBack();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoAdapter
 * JD-Core Version:    0.7.0.1
 */