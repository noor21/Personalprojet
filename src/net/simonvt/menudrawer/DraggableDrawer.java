package net.simonvt.menudrawer;

import android.app.Activity;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

public abstract class DraggableDrawer
  extends MenuDrawer
{
  private static final int CLOSE_ENOUGH = 3;
  private static final long DEFAULT_PEEK_DELAY = 10000L;
  private static final long DEFAULT_PEEK_START_DELAY = 5000L;
  protected static final int INVALID_POINTER = -1;
  protected static final int MAX_MENU_OVERLAY_ALPHA = 185;
  protected static final int PEEK_DURATION = 5000;
  private static final Interpolator PEEK_INTERPOLATOR = new PeekInterpolator();
  private static final String STATE_MENU_VISIBLE = "net.simonvt.menudrawer.MenuDrawer.menuVisible";
  protected int mActivePointerId = -1;
  protected int mCloseEnough;
  private final Runnable mDragRunnable = new Runnable()
  {
    public void run()
    {
      DraggableDrawer.this.postAnimationInvalidate();
    }
  };
  protected float mInitialMotionX;
  protected float mInitialMotionY;
  protected boolean mIsDragging;
  protected float mLastMotionX = -1.0F;
  protected float mLastMotionY = -1.0F;
  protected boolean mLayerTypeHardware;
  protected int mMaxVelocity;
  protected boolean mOffsetMenu = true;
  protected long mPeekDelay;
  protected final Runnable mPeekRunnable = new Runnable()
  {
    public void run()
    {
      DraggableDrawer.this.peekDrawerInvalidate();
    }
  };
  protected Scroller mPeekScroller;
  private Runnable mPeekStartRunnable;
  private Scroller mScroller;
  protected int mTouchSlop;
  protected VelocityTracker mVelocityTracker;
  
  DraggableDrawer(Activity paramActivity, int paramInt)
  {
    super(paramActivity, paramInt);
  }
  
  public DraggableDrawer(Context paramContext)
  {
    super(paramContext);
  }
  
  public DraggableDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public DraggableDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private void completeAnimation()
  {
    this.mScroller.abortAnimation();
    int i = this.mScroller.getFinalX();
    setOffsetPixels(i);
    if (i != 0) {
      i = 8;
    } else {
      i = 0;
    }
    setDrawerState(i);
    stopLayerTranslation();
  }
  
  private void completePeek()
  {
    this.mPeekScroller.abortAnimation();
    setOffsetPixels(0.0F);
    setDrawerState(0);
    stopLayerTranslation();
  }
  
  private void peekDrawerInvalidate()
  {
    if (this.mPeekScroller.computeScrollOffset())
    {
      int i = (int)this.mOffsetPixels;
      int j = this.mPeekScroller.getCurrX();
      if (j != i) {
        setOffsetPixels(j);
      }
      if (!this.mPeekScroller.isFinished()) {
        break label88;
      }
      if (this.mPeekDelay > 0L)
      {
        this.mPeekStartRunnable = new Runnable()
        {
          public void run()
          {
            DraggableDrawer.this.startPeek();
          }
        };
        postDelayed(this.mPeekStartRunnable, this.mPeekDelay);
      }
    }
    completePeek();
    return;
    label88:
    postOnAnimation(this.mPeekRunnable);
  }
  
  private void postAnimationInvalidate()
  {
    if (this.mScroller.computeScrollOffset())
    {
      int i = (int)this.mOffsetPixels;
      int j = this.mScroller.getCurrX();
      if (j != i) {
        setOffsetPixels(j);
      }
      if (j != this.mScroller.getFinalX()) {}
    }
    else
    {
      completeAnimation();
      return;
    }
    postOnAnimation(this.mDragRunnable);
  }
  
  private int supportGetTranslationX(View paramView)
  {
    int i;
    if (Build.VERSION.SDK_INT < 11) {
      i = 0;
    } else {
      i = (int)paramView.getTranslationX();
    }
    return i;
  }
  
  private int supportGetTranslationY(View paramView)
  {
    int i;
    if (Build.VERSION.SDK_INT < 11) {
      i = 0;
    } else {
      i = (int)paramView.getTranslationY();
    }
    return i;
  }
  
  protected void animateOffsetTo(int paramInt1, int paramInt2)
  {
    int i = (int)this.mOffsetPixels;
    int j = paramInt1 - i;
    if (j <= 0)
    {
      setDrawerState(1);
      this.mScroller.startScroll(i, 0, j, 0, paramInt2);
    }
    else
    {
      setDrawerState(4);
      this.mScroller.startScroll(i, 0, j, 0, paramInt2);
    }
    startLayerTranslation();
    postAnimationInvalidate();
  }
  
  protected void animateOffsetTo(int paramInt1, int paramInt2, boolean paramBoolean)
  {
    endDrag();
    endPeek();
    int i = paramInt1 - (int)this.mOffsetPixels;
    if ((i != 0) && (paramBoolean))
    {
      int j = Math.abs(paramInt2);
      if (j <= 0) {
        i = (int)(600.0F * Math.abs(i / this.mMenuSize));
      } else {
        i = 4 * Math.round(1000.0F * Math.abs(i / j));
      }
      animateOffsetTo(paramInt1, Math.min(i, this.mMaxAnimationDuration));
    }
    else
    {
      setOffsetPixels(paramInt1);
      if (paramInt1 != 0) {
        i = 8;
      } else {
        i = 0;
      }
      setDrawerState(i);
      stopLayerTranslation();
    }
  }
  
  protected boolean canChildScrollHorizontally(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    int i;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int k = -1 + localViewGroup.getChildCount();
      while (k >= 0)
      {
        View localView = localViewGroup.getChildAt(k);
        int n = localView.getLeft() + supportGetTranslationX(localView);
        int m = localView.getRight() + supportGetTranslationX(localView);
        int j = localView.getTop() + supportGetTranslationY(localView);
        i = localView.getBottom() + supportGetTranslationY(localView);
        if ((paramInt2 < n) || (paramInt2 >= m) || (paramInt3 < j) || (paramInt3 >= i) || (!canChildScrollHorizontally(localView, true, paramInt1, paramInt2 - n, paramInt3 - j)))
        {
          k--;
        }
        else
        {
          i = 1;
          break label185;
        }
      }
    }
    if ((!paramBoolean) || (!this.mOnInterceptMoveEventListener.isViewDraggable(paramView, paramInt1, paramInt2, paramInt3))) {
      i = 0;
    } else {
      i = 1;
    }
    label185:
    return i;
  }
  
  protected boolean canChildScrollVertically(View paramView, boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool;
    if ((paramView instanceof ViewGroup))
    {
      ViewGroup localViewGroup = (ViewGroup)paramView;
      int n = -1 + localViewGroup.getChildCount();
      while (n >= 0)
      {
        View localView = localViewGroup.getChildAt(n);
        int k = localView.getLeft() + supportGetTranslationX(localView);
        int i = localView.getRight() + supportGetTranslationX(localView);
        int j = localView.getTop() + supportGetTranslationY(localView);
        int m = localView.getBottom() + supportGetTranslationY(localView);
        if ((paramInt2 < k) || (paramInt2 >= i) || (paramInt3 < j) || (paramInt3 >= m) || (!canChildScrollVertically(localView, true, paramInt1, paramInt2 - k, paramInt3 - j)))
        {
          n--;
        }
        else
        {
          bool = true;
          break label185;
        }
      }
    }
    if ((!paramBoolean) || (!this.mOnInterceptMoveEventListener.isViewDraggable(paramView, paramInt1, paramInt2, paramInt3))) {
      bool = false;
    } else {
      bool = true;
    }
    label185:
    return bool;
  }
  
  protected boolean canChildrenScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    boolean bool = false;
    switch (this.mPosition)
    {
    case BOTTOM: 
    case RIGHT: 
      if (this.mMenuVisible) {
        bool = canChildScrollHorizontally(this.mMenuContainer, false, paramInt1, paramInt3 - ViewHelper.getLeft(this.mMenuContainer), paramInt4 - ViewHelper.getTop(this.mContentContainer));
      } else {
        bool = canChildScrollHorizontally(this.mContentContainer, false, paramInt1, paramInt3 - ViewHelper.getLeft(this.mContentContainer), paramInt4 - ViewHelper.getTop(this.mContentContainer));
      }
      break;
    case LEFT: 
    case TOP: 
      if (this.mMenuVisible) {
        bool = canChildScrollVertically(this.mMenuContainer, false, paramInt2, paramInt3 - ViewHelper.getLeft(this.mMenuContainer), paramInt4 - ViewHelper.getTop(this.mContentContainer));
      } else {
        bool = canChildScrollVertically(this.mContentContainer, false, paramInt2, paramInt3 - ViewHelper.getLeft(this.mContentContainer), paramInt4 - ViewHelper.getTop(this.mContentContainer));
      }
      break;
    }
    return bool;
  }
  
  protected void endDrag()
  {
    this.mIsDragging = false;
    if (this.mVelocityTracker != null)
    {
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
    }
  }
  
  protected void endPeek()
  {
    removeCallbacks(this.mPeekStartRunnable);
    removeCallbacks(this.mPeekRunnable);
    stopLayerTranslation();
  }
  
  public boolean getOffsetMenuEnabled()
  {
    return this.mOffsetMenu;
  }
  
  public int getTouchBezelSize()
  {
    return this.mTouchBezelSize;
  }
  
  public int getTouchMode()
  {
    return this.mTouchMode;
  }
  
  protected float getXVelocity(VelocityTracker paramVelocityTracker)
  {
    float f;
    if (Build.VERSION.SDK_INT < 8) {
      f = paramVelocityTracker.getXVelocity();
    } else {
      f = paramVelocityTracker.getXVelocity(this.mActivePointerId);
    }
    return f;
  }
  
  protected float getYVelocity(VelocityTracker paramVelocityTracker)
  {
    float f;
    if (Build.VERSION.SDK_INT < 8) {
      f = paramVelocityTracker.getYVelocity();
    } else {
      f = paramVelocityTracker.getYVelocity(this.mActivePointerId);
    }
    return f;
  }
  
  protected void initDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super.initDrawer(paramContext, paramAttributeSet, paramInt);
    ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
    this.mTouchSlop = localViewConfiguration.getScaledTouchSlop();
    this.mMaxVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
    this.mScroller = new Scroller(paramContext, MenuDrawer.SMOOTH_INTERPOLATOR);
    this.mPeekScroller = new Scroller(paramContext, PEEK_INTERPOLATOR);
    this.mCloseEnough = dpToPx(3);
  }
  
  protected abstract void initPeekScroller();
  
  protected boolean isCloseEnough()
  {
    boolean bool;
    if (Math.abs(this.mOffsetPixels) > this.mCloseEnough) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public boolean isMenuVisible()
  {
    return this.mMenuVisible;
  }
  
  protected abstract void onOffsetPixelsChanged(int paramInt);
  
  public void peekDrawer()
  {
    peekDrawer(5000L, 10000L);
  }
  
  public void peekDrawer(long paramLong)
  {
    peekDrawer(5000L, paramLong);
  }
  
  public void peekDrawer(long paramLong1, long paramLong2)
  {
    if (paramLong1 >= 0L)
    {
      if (paramLong2 >= 0L)
      {
        removeCallbacks(this.mPeekRunnable);
        removeCallbacks(this.mPeekStartRunnable);
        this.mPeekDelay = paramLong2;
        this.mPeekStartRunnable = new Runnable()
        {
          public void run()
          {
            DraggableDrawer.this.startPeek();
          }
        };
        postDelayed(this.mPeekStartRunnable, paramLong1);
        return;
      }
      throw new IllegalArgumentException("delay must be zero or larger");
    }
    throw new IllegalArgumentException("startDelay must be zero or larger.");
  }
  
  public void restoreState(Parcelable paramParcelable)
  {
    int i = 0;
    super.restoreState(paramParcelable);
    boolean bool = ((Bundle)paramParcelable).getBoolean("net.simonvt.menudrawer.MenuDrawer.menuVisible");
    if (!bool) {
      setOffsetPixels(0.0F);
    } else {
      openMenu(false);
    }
    if (bool) {
      i = 8;
    }
    this.mDrawerState = i;
  }
  
  void saveState(Bundle paramBundle)
  {
    boolean bool;
    if ((this.mDrawerState == 8) || (this.mDrawerState == 4)) {
      bool = true;
    } else {
      bool = false;
    }
    paramBundle.putBoolean("net.simonvt.menudrawer.MenuDrawer.menuVisible", bool);
  }
  
  public void setHardwareLayerEnabled(boolean paramBoolean)
  {
    if (paramBoolean != this.mHardwareLayersEnabled)
    {
      this.mHardwareLayersEnabled = paramBoolean;
      this.mMenuContainer.setHardwareLayersEnabled(paramBoolean);
      this.mContentContainer.setHardwareLayersEnabled(paramBoolean);
      stopLayerTranslation();
    }
  }
  
  public void setMenuSize(int paramInt)
  {
    this.mMenuSize = paramInt;
    if ((this.mDrawerState == 8) || (this.mDrawerState == 4)) {
      setOffsetPixels(this.mMenuSize);
    }
    requestLayout();
    invalidate();
  }
  
  public void setOffsetMenuEnabled(boolean paramBoolean)
  {
    if (paramBoolean != this.mOffsetMenu)
    {
      this.mOffsetMenu = paramBoolean;
      requestLayout();
      invalidate();
    }
  }
  
  protected void setOffsetPixels(float paramFloat)
  {
    int j = (int)this.mOffsetPixels;
    int i = (int)paramFloat;
    this.mOffsetPixels = paramFloat;
    if (this.mSlideDrawable != null)
    {
      float f = Math.abs(this.mOffsetPixels) / this.mMenuSize;
      this.mSlideDrawable.setOffset(f);
      updateUpContentDescription();
    }
    if (i != j)
    {
      onOffsetPixelsChanged(i);
      boolean bool;
      if (i == 0) {
        bool = false;
      } else {
        bool = true;
      }
      this.mMenuVisible = bool;
      dispatchOnDrawerSlide(Math.abs(i) / this.mMenuSize, i);
    }
  }
  
  public void setTouchBezelSize(int paramInt)
  {
    this.mTouchBezelSize = paramInt;
  }
  
  public void setTouchMode(int paramInt)
  {
    if (this.mTouchMode != paramInt)
    {
      this.mTouchMode = paramInt;
      updateTouchAreaSize();
    }
  }
  
  protected void startLayerTranslation()
  {
    if ((USE_TRANSLATIONS) && (this.mHardwareLayersEnabled) && (!this.mLayerTypeHardware))
    {
      this.mLayerTypeHardware = true;
      this.mContentContainer.setLayerType(2, null);
      this.mMenuContainer.setLayerType(2, null);
    }
  }
  
  protected void startPeek()
  {
    initPeekScroller();
    startLayerTranslation();
    peekDrawerInvalidate();
  }
  
  protected void stopAnimation()
  {
    removeCallbacks(this.mDragRunnable);
    this.mScroller.abortAnimation();
    stopLayerTranslation();
  }
  
  protected void stopLayerTranslation()
  {
    if (this.mLayerTypeHardware)
    {
      this.mLayerTypeHardware = false;
      this.mContentContainer.setLayerType(0, null);
      this.mMenuContainer.setLayerType(0, null);
    }
  }
  
  public void toggleMenu(boolean paramBoolean)
  {
    if ((this.mDrawerState != 8) && (this.mDrawerState != 4))
    {
      if ((this.mDrawerState == 0) || (this.mDrawerState == 1)) {
        openMenu(paramBoolean);
      }
    }
    else {
      closeMenu(paramBoolean);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.DraggableDrawer
 * JD-Core Version:    0.7.0.1
 */