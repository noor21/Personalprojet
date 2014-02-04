package com.haarman.listviewanimations.itemmanipulation.contextualundo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class ContextualUndoListViewTouchListener
  implements View.OnTouchListener
{
  private long mAnimationTime;
  private Callback mCallback;
  private int mDownPosition;
  private View mDownView;
  private float mDownX;
  private AbsListView mListView;
  private int mMaxFlingVelocity;
  private int mMinFlingVelocity;
  private boolean mPaused;
  private int mSlop;
  private boolean mSwiping;
  private VelocityTracker mVelocityTracker;
  private int mViewWidth = 1;
  
  public ContextualUndoListViewTouchListener(AbsListView paramAbsListView, Callback paramCallback)
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramAbsListView.getContext());
    this.mSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
    this.mMaxFlingVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mAnimationTime = paramAbsListView.getContext().getResources().getInteger(17694720);
    this.mListView = paramAbsListView;
    this.mCallback = paramCallback;
  }
  
  public AbsListView.OnScrollListener makeScrollListener()
  {
    new AbsListView.OnScrollListener()
    {
      public void onScroll(AbsListView paramAnonymousAbsListView, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      
      public void onScrollStateChanged(AbsListView paramAnonymousAbsListView, int paramAnonymousInt)
      {
        int i = 1;
        ContextualUndoListViewTouchListener localContextualUndoListViewTouchListener = ContextualUndoListViewTouchListener.this;
        if (paramAnonymousInt == i) {
          i = 0;
        }
        localContextualUndoListViewTouchListener.setEnabled(i);
        if (ContextualUndoListViewTouchListener.this.mPaused) {
          ContextualUndoListViewTouchListener.this.mCallback.onListScrolled();
        }
      }
    };
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (this.mViewWidth < 2) {
      this.mViewWidth = this.mListView.getWidth();
    }
    int i;
    int m;
    Object localObject;
    float f2;
    switch (paramMotionEvent.getActionMasked())
    {
    case 0: 
      if (!this.mPaused)
      {
        Rect localRect = new Rect();
        int j = this.mListView.getChildCount();
        int[] arrayOfInt = new int[2];
        this.mListView.getLocationOnScreen(arrayOfInt);
        i = (int)paramMotionEvent.getRawX() - arrayOfInt[0];
        m = (int)paramMotionEvent.getRawY() - arrayOfInt[1];
        int n = 0;
        while (n < j)
        {
          View localView2 = this.mListView.getChildAt(n);
          localView2.getHitRect(localRect);
          if (!localRect.contains(i, m)) {
            n++;
          } else {
            this.mDownView = localView2;
          }
        }
        if (this.mDownView != null)
        {
          this.mDownX = paramMotionEvent.getRawX();
          this.mDownPosition = this.mListView.getPositionForView(this.mDownView);
          this.mVelocityTracker = VelocityTracker.obtain();
          this.mVelocityTracker.addMovement(paramMotionEvent);
        }
        paramView.onTouchEvent(paramMotionEvent);
        i = 1;
      }
      else
      {
        i = 0;
      }
      break;
    case 1: 
      if (this.mVelocityTracker != null)
      {
        float f1 = paramMotionEvent.getRawX() - this.mDownX;
        this.mVelocityTracker.addMovement(paramMotionEvent);
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float f4 = Math.abs(this.mVelocityTracker.getXVelocity());
        float f3 = Math.abs(this.mVelocityTracker.getYVelocity());
        m = 0;
        i = 0;
        if (Math.abs(f1) <= this.mViewWidth / 2)
        {
          if ((this.mMinFlingVelocity <= f4) && (f4 <= this.mMaxFlingVelocity) && (f3 < f4))
          {
            m = 1;
            if (this.mVelocityTracker.getXVelocity() <= 0.0F) {
              i = 0;
            } else {
              i = 1;
            }
          }
        }
        else
        {
          m = 1;
          if (f1 <= 0.0F) {
            i = 0;
          } else {
            i = 1;
          }
        }
        if (m == 0)
        {
          ViewPropertyAnimator.animate(this.mDownView).translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener(null);
        }
        else
        {
          final View localView1 = this.mDownView;
          final int k = this.mDownPosition;
          ViewPropertyAnimator localViewPropertyAnimator = ViewPropertyAnimator.animate(this.mDownView);
          if (i == 0) {
            i = -this.mViewWidth;
          } else {
            i = this.mViewWidth;
          }
          localObject = localViewPropertyAnimator.translationX(i).alpha(0.0F).setDuration(this.mAnimationTime);
          AnimatorListenerAdapter local2 = new AnimatorListenerAdapter()
          {
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
              ContextualUndoListViewTouchListener.this.mCallback.onViewSwiped(localView1, k);
            }
          };
          ((ViewPropertyAnimator)localObject).setListener(local2);
        }
        this.mVelocityTracker = null;
        this.mDownX = 0.0F;
        this.mDownView = null;
        this.mDownPosition = -1;
        this.mSwiping = false;
      }
      break;
    case 2: 
      if ((this.mVelocityTracker != null) && (!this.mPaused))
      {
        this.mVelocityTracker.addMovement(paramMotionEvent);
        f2 = paramMotionEvent.getRawX() - this.mDownX;
        if (Math.abs(f2) > this.mSlop)
        {
          this.mSwiping = true;
          this.mListView.requestDisallowInterceptTouchEvent(true);
          localObject = MotionEvent.obtain(paramMotionEvent);
          ((MotionEvent)localObject).setAction(0x3 | paramMotionEvent.getActionIndex() << 8);
          this.mListView.onTouchEvent((MotionEvent)localObject);
        }
        if (this.mSwiping) {
          break label623;
        }
      }
      break;
    }
    boolean bool = false;
    return bool;
    label623:
    ViewHelper.setTranslationX(this.mDownView, f2);
    ViewHelper.setAlpha(this.mDownView, Math.max(0.0F, Math.min(1.0F, 1.0F - 2.0F * Math.abs(f2) / this.mViewWidth)));
    bool = true;
    return bool;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    boolean bool;
    if (!paramBoolean) {
      bool = true;
    } else {
      bool = false;
    }
    this.mPaused = bool;
  }
  
  public static abstract interface Callback
  {
    public abstract void onListScrolled();
    
    public abstract void onViewSwiped(View paramView, int paramInt);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.contextualundo.ContextualUndoListViewTouchListener
 * JD-Core Version:    0.7.0.1
 */