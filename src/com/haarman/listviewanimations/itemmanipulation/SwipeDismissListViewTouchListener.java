package com.haarman.listviewanimations.itemmanipulation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@SuppressLint({"Recycle"})
public class SwipeDismissListViewTouchListener
  implements View.OnTouchListener
{
  private long mAnimationTime;
  private OnDismissCallback mCallback;
  private int mDismissAnimationRefCount = 0;
  private int mDownPosition;
  private View mDownView;
  private float mDownX;
  private float mDownY;
  private AbsListView mListView;
  private int mMaxFlingVelocity;
  private int mMinFlingVelocity;
  private boolean mPaused;
  private List<PendingDismissData> mPendingDismisses = new ArrayList();
  private int mSlop;
  private boolean mSwiping;
  private VelocityTracker mVelocityTracker;
  private int mViewWidth = 1;
  
  public SwipeDismissListViewTouchListener(AbsListView paramAbsListView, OnDismissCallback paramOnDismissCallback)
  {
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramAbsListView.getContext());
    this.mSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMinFlingVelocity = (16 * localViewConfiguration.getScaledMinimumFlingVelocity());
    this.mMaxFlingVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mAnimationTime = paramAbsListView.getContext().getResources().getInteger(17694720);
    this.mListView = paramAbsListView;
    this.mCallback = paramOnDismissCallback;
  }
  
  private void performDismiss(final View paramView, int paramInt)
  {
    final ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    int i = paramView.getHeight();
    Object localObject = new int[2];
    localObject[0] = i;
    localObject[1] = 1;
    localObject = ValueAnimator.ofInt((int[])localObject).setDuration(this.mAnimationTime);
    ((ValueAnimator)localObject).addListener(new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        Object localObject = SwipeDismissListViewTouchListener.this;
        ((SwipeDismissListViewTouchListener)localObject).mDismissAnimationRefCount = (-1 + ((SwipeDismissListViewTouchListener)localObject).mDismissAnimationRefCount);
        if (SwipeDismissListViewTouchListener.this.mDismissAnimationRefCount == 0)
        {
          Collections.sort(SwipeDismissListViewTouchListener.this.mPendingDismisses);
          localObject = new int[SwipeDismissListViewTouchListener.this.mPendingDismisses.size()];
          for (int i = -1 + SwipeDismissListViewTouchListener.this.mPendingDismisses.size(); i >= 0; i--) {
            localObject[i] = ((SwipeDismissListViewTouchListener.PendingDismissData)SwipeDismissListViewTouchListener.this.mPendingDismisses.get(i)).position;
          }
          SwipeDismissListViewTouchListener.this.mCallback.onDismiss(SwipeDismissListViewTouchListener.this.mListView, (int[])localObject);
          Iterator localIterator = SwipeDismissListViewTouchListener.this.mPendingDismisses.iterator();
          while (localIterator.hasNext())
          {
            SwipeDismissListViewTouchListener.PendingDismissData localPendingDismissData = (SwipeDismissListViewTouchListener.PendingDismissData)localIterator.next();
            ViewHelper.setAlpha(localPendingDismissData.view, 1.0F);
            ViewHelper.setTranslationX(localPendingDismissData.view, 0.0F);
            localObject = localPendingDismissData.view.getLayoutParams();
            ((ViewGroup.LayoutParams)localObject).height = 0;
            localPendingDismissData.view.setLayoutParams((ViewGroup.LayoutParams)localObject);
          }
          SwipeDismissListViewTouchListener.this.mPendingDismisses.clear();
        }
      }
    });
    ((ValueAnimator)localObject).addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        localLayoutParams.height = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
        paramView.setLayoutParams(localLayoutParams);
      }
    });
    this.mPendingDismisses.add(new PendingDismissData(paramInt, paramView));
    ((ValueAnimator)localObject).start();
  }
  
  public boolean onTouch(View paramView, MotionEvent paramMotionEvent)
  {
    if (this.mViewWidth < 2) {
      this.mViewWidth = this.mListView.getWidth();
    }
    int i;
    int j;
    switch (paramMotionEvent.getActionMasked())
    {
    case 0: 
      if (!this.mPaused)
      {
        Rect localRect = new Rect();
        i = this.mListView.getChildCount();
        Object localObject2 = new int[2];
        this.mListView.getLocationOnScreen((int[])localObject2);
        j = (int)paramMotionEvent.getRawX() - localObject2[0];
        int n = (int)paramMotionEvent.getRawY() - localObject2[1];
        int m = 0;
        while (m < i)
        {
          localObject2 = this.mListView.getChildAt(m);
          ((View)localObject2).getHitRect(localRect);
          if (!localRect.contains(j, n)) {
            m++;
          } else {
            this.mDownView = ((View)localObject2);
          }
        }
        if (this.mDownView != null)
        {
          this.mDownX = paramMotionEvent.getRawX();
          this.mDownY = paramMotionEvent.getRawY();
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
        float f5 = paramMotionEvent.getRawX() - this.mDownX;
        this.mVelocityTracker.addMovement(paramMotionEvent);
        this.mVelocityTracker.computeCurrentVelocity(1000);
        float f3 = Math.abs(this.mVelocityTracker.getXVelocity());
        float f4 = Math.abs(this.mVelocityTracker.getYVelocity());
        j = 0;
        i = 0;
        if (Math.abs(f5) <= this.mViewWidth / 2)
        {
          if ((this.mMinFlingVelocity <= f3) && (f3 <= this.mMaxFlingVelocity) && (f4 < f3))
          {
            j = 1;
            if (this.mVelocityTracker.getXVelocity() <= 0.0F) {
              i = 0;
            } else {
              i = 1;
            }
          }
        }
        else
        {
          j = 1;
          if (f5 <= 0.0F) {
            i = 0;
          } else {
            i = 1;
          }
        }
        if (j == 0)
        {
          ViewPropertyAnimator.animate(this.mDownView).translationX(0.0F).alpha(1.0F).setDuration(this.mAnimationTime).setListener(null);
        }
        else
        {
          Object localObject1 = this.mDownView;
          final int k = this.mDownPosition;
          this.mDismissAnimationRefCount = (1 + this.mDismissAnimationRefCount);
          ViewPropertyAnimator localViewPropertyAnimator2 = ViewPropertyAnimator.animate(this.mDownView);
          if (i == 0) {
            i = -this.mViewWidth;
          } else {
            i = this.mViewWidth;
          }
          ViewPropertyAnimator localViewPropertyAnimator1 = localViewPropertyAnimator2.translationX(i).alpha(0.0F).setDuration(this.mAnimationTime);
          localObject1 = new AnimatorListenerAdapter()
          {
            public void onAnimationEnd(Animator paramAnonymousAnimator)
            {
              SwipeDismissListViewTouchListener.this.performDismiss(this.val$downView, k);
            }
          };
          localViewPropertyAnimator1.setListener((Animator.AnimatorListener)localObject1);
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
        float f1 = paramMotionEvent.getRawX() - this.mDownX;
        float f2 = paramMotionEvent.getRawY() - this.mDownY;
        if ((Math.abs(f1) > this.mSlop) && (Math.abs(f1) > Math.abs(f2)))
        {
          this.mSwiping = true;
          this.mListView.requestDisallowInterceptTouchEvent(true);
          MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
          localMotionEvent.setAction(0x3 | paramMotionEvent.getActionIndex() << 8);
          this.mListView.onTouchEvent(localMotionEvent);
        }
        if (this.mSwiping) {
          break label667;
        }
      }
      break;
    }
    boolean bool = false;
    return bool;
    label667:
    ViewHelper.setTranslationX(this.mDownView, bool);
    ViewHelper.setAlpha(this.mDownView, Math.max(0.0F, Math.min(1.0F, 1.0F - 2.0F * Math.abs(bool) / this.mViewWidth)));
    bool = true;
    return bool;
  }
  
  class PendingDismissData
    implements Comparable<PendingDismissData>
  {
    public int position;
    public View view;
    
    public PendingDismissData(int paramInt, View paramView)
    {
      this.position = paramInt;
      this.view = paramView;
    }
    
    public int compareTo(PendingDismissData paramPendingDismissData)
    {
      return paramPendingDismissData.position - this.position;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.SwipeDismissListViewTouchListener
 * JD-Core Version:    0.7.0.1
 */