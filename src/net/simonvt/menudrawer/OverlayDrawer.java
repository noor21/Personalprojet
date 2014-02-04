package net.simonvt.menudrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public class OverlayDrawer
  extends DraggableDrawer
{
  private static final String TAG = "OverlayDrawer";
  private int mPeekSize;
  private Runnable mRevealRunnable = new Runnable()
  {
    public void run()
    {
      int i;
      switch (OverlayDrawer.this.mPosition)
      {
      default: 
        i = OverlayDrawer.this.mPeekSize;
        break;
      case RIGHT: 
      case TOP: 
        i = -OverlayDrawer.this.mPeekSize;
      }
      OverlayDrawer.this.animateOffsetTo(i, 250);
    }
  };
  
  OverlayDrawer(Activity paramActivity, int paramInt)
  {
    super(paramActivity, paramInt);
  }
  
  public OverlayDrawer(Context paramContext)
  {
    super(paramContext);
  }
  
  public OverlayDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public OverlayDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private boolean isContentTouch(int paramInt1, int paramInt2)
  {
    boolean bool = false;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (ViewHelper.getRight(this.mMenuContainer) >= paramInt1) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case LEFT: 
      if (ViewHelper.getBottom(this.mMenuContainer) >= paramInt2) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case RIGHT: 
      if (ViewHelper.getLeft(this.mMenuContainer) <= paramInt1) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case TOP: 
      if (ViewHelper.getTop(this.mMenuContainer) <= paramInt2) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    }
    return bool;
  }
  
  private void onPointerUp(MotionEvent paramMotionEvent)
  {
    int i = paramMotionEvent.getActionIndex();
    if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
    {
      if (i != 0) {
        i = 0;
      } else {
        i = 1;
      }
      this.mLastMotionX = paramMotionEvent.getX(i);
      this.mActivePointerId = paramMotionEvent.getPointerId(i);
      if (this.mVelocityTracker != null) {
        this.mVelocityTracker.clear();
      }
    }
  }
  
  protected boolean checkTouchSlop(float paramFloat1, float paramFloat2)
  {
    boolean bool = true;
    switch (this.mPosition)
    {
    case RIGHT: 
    default: 
      if ((Math.abs(paramFloat1) <= this.mTouchSlop) || (Math.abs(paramFloat1) <= Math.abs(paramFloat2))) {
        bool = false;
      }
      break;
    case LEFT: 
    case TOP: 
      if ((Math.abs(paramFloat2) <= this.mTouchSlop) || (Math.abs(paramFloat2) <= Math.abs(paramFloat1))) {
        bool = false;
      }
      break;
    }
    return bool;
  }
  
  public void closeMenu(boolean paramBoolean)
  {
    animateOffsetTo(0, 0, paramBoolean);
  }
  
  protected void drawOverlay(Canvas paramCanvas)
  {
    int j = getWidth();
    int k = getHeight();
    int i = (int)this.mOffsetPixels;
    float f = Math.abs(this.mOffsetPixels) / this.mMenuSize;
    switch (this.mPosition)
    {
    case BOTTOM: 
      this.mMenuOverlay.setBounds(i, 0, j, k);
      break;
    case LEFT: 
      this.mMenuOverlay.setBounds(0, i, j, k);
      break;
    case RIGHT: 
      this.mMenuOverlay.setBounds(0, 0, j + i, k);
      break;
    case TOP: 
      this.mMenuOverlay.setBounds(0, 0, j, k + i);
    }
    this.mMenuOverlay.setAlpha((int)(185.0F * f));
    this.mMenuOverlay.draw(paramCanvas);
  }
  
  protected GradientDrawable.Orientation getDropShadowOrientation()
  {
    GradientDrawable.Orientation localOrientation;
    switch (this.mPosition)
    {
    default: 
      localOrientation = GradientDrawable.Orientation.LEFT_RIGHT;
      break;
    case LEFT: 
      localOrientation = GradientDrawable.Orientation.TOP_BOTTOM;
      break;
    case RIGHT: 
      localOrientation = GradientDrawable.Orientation.RIGHT_LEFT;
      break;
    case TOP: 
      localOrientation = GradientDrawable.Orientation.BOTTOM_TOP;
    }
    return localOrientation;
  }
  
  protected void initDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super.initDrawer(paramContext, paramAttributeSet, paramInt);
    super.addView(this.mContentContainer, -1, new ViewGroup.LayoutParams(-1, -1));
    if (USE_TRANSLATIONS) {
      this.mContentContainer.setLayerType(0, null);
    }
    this.mContentContainer.setHardwareLayersEnabled(false);
    super.addView(this.mMenuContainer, -1, new ViewGroup.LayoutParams(-1, -1));
    this.mPeekSize = dpToPx(20);
  }
  
  protected void initPeekScroller()
  {
    int i;
    switch (this.mPosition)
    {
    default: 
      i = this.mPeekSize;
      this.mPeekScroller.startScroll(0, 0, i, 0, 5000);
      break;
    case RIGHT: 
    case TOP: 
      i = -this.mPeekSize;
      this.mPeekScroller.startScroll(0, 0, i, 0, 5000);
    }
  }
  
  protected boolean onDownAllowDrag(int paramInt1, int paramInt2)
  {
    boolean bool = false;
    int i;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (((!this.mMenuVisible) && (this.mInitialMotionX <= this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionX <= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case LEFT: 
      if (((!this.mMenuVisible) && (this.mInitialMotionY <= this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionY <= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case RIGHT: 
      i = getWidth();
      int j = (int)this.mInitialMotionX;
      if (((!this.mMenuVisible) && (j >= i - this.mTouchSize)) || ((this.mMenuVisible) && (j >= i + this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case TOP: 
      i = getHeight();
      if (((!this.mMenuVisible) && (this.mInitialMotionY >= i - this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionY >= i + this.mOffsetPixels))) {
        bool = true;
      }
      break;
    }
    return bool;
  }
  
  public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent)
  {
    int k = 0xFF & paramMotionEvent.getAction();
    boolean bool2;
    if ((k != 1) && (k != 3))
    {
      if ((k == 0) && (this.mMenuVisible) && (isCloseEnough()))
      {
        setOffsetPixels(0.0F);
        stopAnimation();
        endPeek();
        setDrawerState(0);
        this.mIsDragging = false;
      }
      if (this.mMenuVisible)
      {
        int i = 0;
        if (this.mActivePointerId != -1)
        {
          i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
          if (i == -1) {
            i = 0;
          }
        }
        if (isContentTouch((int)paramMotionEvent.getX(i), (int)paramMotionEvent.getY(i))) {}
      }
      else
      {
        if ((this.mMenuVisible) || (this.mIsDragging) || (this.mTouchMode != 0))
        {
          if ((k == 0) || (!this.mIsDragging))
          {
            int j;
            switch (k)
            {
            case 0: 
              float f1 = paramMotionEvent.getX();
              this.mInitialMotionX = f1;
              this.mLastMotionX = f1;
              f1 = paramMotionEvent.getY();
              this.mInitialMotionY = f1;
              this.mLastMotionY = f1;
              boolean bool1 = onDownAllowDrag((int)this.mLastMotionX, (int)this.mLastMotionY);
              this.mActivePointerId = paramMotionEvent.getPointerId(0);
              if (bool1)
              {
                if (!this.mMenuVisible) {
                  bool1 = false;
                } else {
                  j = 8;
                }
                setDrawerState(j);
                stopAnimation();
                endPeek();
                if (!this.mMenuVisible) {
                  postDelayed(this.mRevealRunnable, 160L);
                }
                this.mIsDragging = false;
              }
              break;
            case 2: 
              j = this.mActivePointerId;
              if (j != -1)
              {
                int m = paramMotionEvent.findPointerIndex(j);
                if (m != -1)
                {
                  float f3 = paramMotionEvent.getX(m);
                  float f2 = f3 - this.mLastMotionX;
                  float f4 = paramMotionEvent.getY(m);
                  float f5 = f4 - this.mLastMotionY;
                  if (checkTouchSlop(f2, f5)) {
                    if ((this.mOnInterceptMoveEventListener == null) || ((this.mTouchMode != 2) && (!this.mMenuVisible)) || (!canChildrenScroll((int)f2, (int)f5, (int)f3, (int)f4)))
                    {
                      if (onMoveAllowDrag((int)f3, (int)f4, f2, f5))
                      {
                        endPeek();
                        stopAnimation();
                        setDrawerState(2);
                        this.mIsDragging = true;
                        this.mLastMotionX = f3;
                        this.mLastMotionY = f4;
                      }
                    }
                    else
                    {
                      endDrag();
                      requestDisallowInterceptTouchEvent(true);
                      return false;
                    }
                  }
                }
                else
                {
                  this.mIsDragging = false;
                  this.mActivePointerId = -1;
                  endDrag();
                  closeMenu(true);
                  bool2 = false;
                }
              }
              break;
            case 6: 
              onPointerUp(paramMotionEvent);
              this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId));
              this.mLastMotionY = paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId));
            }
            if (this.mVelocityTracker == null) {
              this.mVelocityTracker = VelocityTracker.obtain();
            }
            this.mVelocityTracker.addMovement(paramMotionEvent);
            return this.mIsDragging;
          }
          return true;
        }
        return false;
      }
      bool2 = true;
    }
    else
    {
      this.mActivePointerId = -1;
      this.mIsDragging = false;
      if (this.mVelocityTracker != null)
      {
        this.mVelocityTracker.recycle();
        this.mVelocityTracker = null;
      }
      if (Math.abs(this.mOffsetPixels) <= this.mMenuSize / 2) {
        closeMenu();
      } else {
        openMenu();
      }
      bool2 = false;
    }
    return bool2;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int k = paramInt3 - paramInt1;
    int m = paramInt4 - paramInt2;
    this.mContentContainer.layout(0, 0, k, m);
    if (!USE_TRANSLATIONS)
    {
      int j = (int)this.mOffsetPixels;
      int i = this.mMenuSize;
      switch (this.mPosition)
      {
      default: 
        break;
      case BOTTOM: 
        this.mMenuContainer.layout(j + -i, 0, j, m);
        break;
      case LEFT: 
        this.mMenuContainer.layout(0, j + -i, k, j);
        break;
      case RIGHT: 
        this.mMenuContainer.layout(k + j, 0, j + (k + i), m);
        break;
      case TOP: 
        this.mMenuContainer.layout(0, m + j, k, j + (m + i));
        break;
      }
    }
    else
    {
      switch (this.mPosition)
      {
      case BOTTOM: 
        this.mMenuContainer.layout(0, 0, this.mMenuSize, m);
        break;
      case LEFT: 
        this.mMenuContainer.layout(0, 0, k, this.mMenuSize);
        break;
      case RIGHT: 
        this.mMenuContainer.layout(k - this.mMenuSize, 0, k, m);
        break;
      case TOP: 
        this.mMenuContainer.layout(0, m - this.mMenuSize, k, m);
      }
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int j = View.MeasureSpec.getMode(paramInt1);
    int i = View.MeasureSpec.getMode(paramInt2);
    if ((j == 1073741824) && (i == 1073741824))
    {
      j = View.MeasureSpec.getSize(paramInt1);
      i = View.MeasureSpec.getSize(paramInt2);
      if (this.mOffsetPixels == -1.0F) {
        openMenu(false);
      }
      switch (this.mPosition)
      {
      case RIGHT: 
      default: 
        k = getChildMeasureSpec(paramInt1, 0, this.mMenuSize);
        m = getChildMeasureSpec(paramInt1, 0, i);
        break;
      case LEFT: 
      case TOP: 
        k = getChildMeasureSpec(paramInt1, 0, j);
        m = getChildMeasureSpec(paramInt2, 0, this.mMenuSize);
      }
      this.mMenuContainer.measure(k, m);
      int k = getChildMeasureSpec(paramInt1, 0, j);
      int m = getChildMeasureSpec(paramInt1, 0, i);
      this.mContentContainer.measure(k, m);
      setMeasuredDimension(j, i);
      updateTouchAreaSize();
      return;
    }
    throw new IllegalStateException("Must measure with an exact size");
  }
  
  protected boolean onMoveAllowDrag(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    boolean bool = true;
    if ((!this.mMenuVisible) || (this.mTouchMode != 2))
    {
      int i;
      switch (this.mPosition)
      {
      default: 
        bool = false;
        break;
      case BOTTOM: 
        if (((this.mMenuVisible) || (this.mInitialMotionX > this.mTouchSize) || (paramFloat1 <= 0.0F)) && ((!this.mMenuVisible) || (paramInt1 > this.mOffsetPixels)) && ((Math.abs(this.mOffsetPixels) > this.mPeekSize) || (!this.mMenuVisible))) {
          bool = false;
        }
        break;
      case LEFT: 
        if (((this.mMenuVisible) || (this.mInitialMotionY > this.mTouchSize) || (paramFloat2 <= 0.0F)) && ((!this.mMenuVisible) || (paramInt1 > this.mOffsetPixels)) && ((Math.abs(this.mOffsetPixels) > this.mPeekSize) || (!this.mMenuVisible))) {
          bool = false;
        }
        break;
      case RIGHT: 
        i = getWidth();
        if (((this.mMenuVisible) || (this.mInitialMotionX < i - this.mTouchSize) || (paramFloat1 >= 0.0F)) && ((!this.mMenuVisible) || (paramInt1 < i - this.mOffsetPixels)) && ((Math.abs(this.mOffsetPixels) > this.mPeekSize) || (!this.mMenuVisible))) {
          bool = false;
        }
        break;
      case TOP: 
        i = getHeight();
        if (((this.mMenuVisible) || (this.mInitialMotionY < i - this.mTouchSize) || (paramFloat2 >= 0.0F)) && ((!this.mMenuVisible) || (paramInt1 < i - this.mOffsetPixels)) && ((Math.abs(this.mOffsetPixels) > this.mPeekSize) || (!this.mMenuVisible))) {
          bool = false;
        }
        break;
      }
    }
    return bool;
  }
  
  protected void onMoveEvent(float paramFloat1, float paramFloat2)
  {
    switch (this.mPosition)
    {
    case BOTTOM: 
      setOffsetPixels(Math.min(Math.max(paramFloat1 + this.mOffsetPixels, 0.0F), this.mMenuSize));
      break;
    case LEFT: 
      setOffsetPixels(Math.min(Math.max(paramFloat2 + this.mOffsetPixels, 0.0F), this.mMenuSize));
      break;
    case RIGHT: 
      setOffsetPixels(Math.max(Math.min(paramFloat1 + this.mOffsetPixels, 0.0F), -this.mMenuSize));
      break;
    case TOP: 
      setOffsetPixels(Math.max(Math.min(paramFloat2 + this.mOffsetPixels, 0.0F), -this.mMenuSize));
    }
  }
  
  protected void onOffsetPixelsChanged(int paramInt)
  {
    if (!USE_TRANSLATIONS) {
      switch (this.mPosition)
      {
      default: 
        break;
      case BOTTOM: 
        this.mMenuContainer.offsetLeftAndRight(paramInt - this.mMenuContainer.getRight());
        break;
      case LEFT: 
        this.mMenuContainer.offsetTopAndBottom(paramInt - this.mMenuContainer.getBottom());
        break;
      case RIGHT: 
        this.mMenuContainer.offsetLeftAndRight(paramInt - (this.mMenuContainer.getLeft() - getWidth()));
        break;
      case TOP: 
        this.mMenuContainer.offsetTopAndBottom(paramInt - (this.mMenuContainer.getTop() - getHeight()));
        break;
      }
    } else {
      switch (this.mPosition)
      {
      case BOTTOM: 
        this.mMenuContainer.setTranslationX(paramInt - this.mMenuSize);
        break;
      case LEFT: 
        this.mMenuContainer.setTranslationY(paramInt - this.mMenuSize);
        break;
      case RIGHT: 
        this.mMenuContainer.setTranslationX(paramInt + this.mMenuSize);
        break;
      case TOP: 
        this.mMenuContainer.setTranslationY(paramInt + this.mMenuSize);
      }
    }
    invalidate();
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    onOffsetPixelsChanged((int)this.mOffsetPixels);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int m = 0;
    boolean bool2;
    if ((this.mMenuVisible) || (this.mIsDragging) || (this.mTouchMode != 0))
    {
      int i = 0xFF & paramMotionEvent.getAction();
      if (this.mVelocityTracker == null) {
        this.mVelocityTracker = VelocityTracker.obtain();
      }
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int j;
      switch (i)
      {
      case 0: 
        float f1 = paramMotionEvent.getX();
        this.mInitialMotionX = f1;
        this.mLastMotionX = f1;
        f1 = paramMotionEvent.getY();
        this.mInitialMotionY = f1;
        this.mLastMotionY = f1;
        boolean bool1 = onDownAllowDrag((int)this.mLastMotionX, (int)this.mLastMotionY);
        this.mActivePointerId = paramMotionEvent.getPointerId(0);
        if (bool1)
        {
          stopAnimation();
          endPeek();
          if (!this.mMenuVisible) {
            peekDrawer(160L, 0L);
          }
          startLayerTranslation();
        }
        break;
      case 1: 
      case 3: 
        j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (j == -1) {
          j = 0;
        }
        onUpEvent((int)paramMotionEvent.getX(j), (int)paramMotionEvent.getY(j));
        this.mActivePointerId = -1;
        this.mIsDragging = false;
        break;
      case 2: 
        j = paramMotionEvent.findPointerIndex(this.mActivePointerId);
        if (j != -1)
        {
          float f5;
          float f3;
          float f4;
          if (!this.mIsDragging)
          {
            float f6 = paramMotionEvent.getX(j);
            f5 = f6 - this.mLastMotionX;
            f3 = paramMotionEvent.getY(j);
            f4 = f3 - this.mLastMotionY;
            if (checkTouchSlop(f5, f4)) {
              if (!onMoveAllowDrag((int)f6, (int)f3, f5, f4))
              {
                this.mInitialMotionX = f6;
                this.mInitialMotionY = f3;
              }
              else
              {
                endPeek();
                stopAnimation();
                setDrawerState(2);
                this.mIsDragging = true;
                this.mLastMotionX = f6;
                this.mLastMotionY = f3;
              }
            }
          }
          if (this.mIsDragging)
          {
            startLayerTranslation();
            f3 = paramMotionEvent.getX(j);
            f4 = f3 - this.mLastMotionX;
            float f2 = paramMotionEvent.getY(j);
            f5 = f2 - this.mLastMotionY;
            this.mLastMotionX = f3;
            this.mLastMotionY = f2;
            onMoveEvent(f4, f5);
          }
        }
        else
        {
          this.mIsDragging = false;
          this.mActivePointerId = -1;
          endDrag();
          closeMenu(true);
        }
        break;
      case 5: 
        int k = (0xFF00 & paramMotionEvent.getAction()) >> 8;
        this.mLastMotionX = paramMotionEvent.getX(k);
        this.mLastMotionY = paramMotionEvent.getY(k);
        this.mActivePointerId = paramMotionEvent.getPointerId(k);
        break;
      case 6: 
        onPointerUp(paramMotionEvent);
        this.mLastMotionX = paramMotionEvent.getX(paramMotionEvent.findPointerIndex(this.mActivePointerId));
        this.mLastMotionY = paramMotionEvent.getY(paramMotionEvent.findPointerIndex(this.mActivePointerId));
      }
      bool2 = true;
    }
    return bool2;
  }
  
  protected void onUpEvent(int paramInt1, int paramInt2)
  {
    int i = 0;
    ((int)this.mOffsetPixels);
    int j;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (!this.mIsDragging)
      {
        if (this.mMenuVisible) {
          closeMenu();
        }
      }
      else
      {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        j = (int)getXVelocity(this.mVelocityTracker);
        this.mLastMotionX = paramInt1;
        if (j > 0) {
          i = this.mMenuSize;
        }
        animateOffsetTo(i, j, true);
      }
      break;
    case LEFT: 
      if (!this.mIsDragging)
      {
        if (this.mMenuVisible) {
          closeMenu();
        }
      }
      else
      {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        j = (int)getYVelocity(this.mVelocityTracker);
        this.mLastMotionY = paramInt2;
        if (j > 0) {
          i = this.mMenuSize;
        }
        animateOffsetTo(i, j, true);
      }
      break;
    case RIGHT: 
      getWidth();
      if (!this.mIsDragging)
      {
        if (this.mMenuVisible) {
          closeMenu();
        }
      }
      else
      {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        j = (int)getXVelocity(this.mVelocityTracker);
        this.mLastMotionX = paramInt1;
        if (j <= 0) {
          i = -this.mMenuSize;
        }
        animateOffsetTo(i, j, true);
      }
      break;
    case TOP: 
      if (!this.mIsDragging)
      {
        if (this.mMenuVisible) {
          closeMenu();
        }
      }
      else
      {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        j = (int)getYVelocity(this.mVelocityTracker);
        this.mLastMotionY = paramInt2;
        if (j < 0) {
          i = -this.mMenuSize;
        }
        animateOffsetTo(i, j, true);
      }
      break;
    }
  }
  
  public void openMenu(boolean paramBoolean)
  {
    int i = 0;
    switch (this.mPosition)
    {
    case BOTTOM: 
    case LEFT: 
      i = this.mMenuSize;
      break;
    case RIGHT: 
    case TOP: 
      i = -this.mMenuSize;
    }
    animateOffsetTo(i, 0, paramBoolean);
  }
  
  protected void startLayerTranslation()
  {
    if ((USE_TRANSLATIONS) && (this.mHardwareLayersEnabled) && (!this.mLayerTypeHardware))
    {
      this.mLayerTypeHardware = true;
      this.mMenuContainer.setLayerType(2, null);
    }
  }
  
  protected void stopAnimation()
  {
    super.stopAnimation();
    removeCallbacks(this.mRevealRunnable);
  }
  
  protected void stopLayerTranslation()
  {
    if (this.mLayerTypeHardware)
    {
      this.mLayerTypeHardware = false;
      this.mMenuContainer.setLayerType(0, null);
    }
  }
  
  protected void updateDropShadowRect()
  {
    int i = (int)(Math.abs(this.mOffsetPixels) / this.mMenuSize * this.mDropShadowSize);
    switch (this.mPosition)
    {
    case BOTTOM: 
      this.mDropShadowRect.top = 0;
      this.mDropShadowRect.bottom = getHeight();
      this.mDropShadowRect.left = ViewHelper.getRight(this.mMenuContainer);
      this.mDropShadowRect.right = (i + this.mDropShadowRect.left);
      break;
    case LEFT: 
      this.mDropShadowRect.left = 0;
      this.mDropShadowRect.right = getWidth();
      this.mDropShadowRect.top = ViewHelper.getBottom(this.mMenuContainer);
      this.mDropShadowRect.bottom = (i + this.mDropShadowRect.top);
      break;
    case RIGHT: 
      this.mDropShadowRect.top = 0;
      this.mDropShadowRect.bottom = getHeight();
      this.mDropShadowRect.right = ViewHelper.getLeft(this.mMenuContainer);
      this.mDropShadowRect.left = (this.mDropShadowRect.right - i);
      break;
    case TOP: 
      this.mDropShadowRect.left = 0;
      this.mDropShadowRect.right = getWidth();
      this.mDropShadowRect.bottom = ViewHelper.getTop(this.mMenuContainer);
      this.mDropShadowRect.top = (this.mDropShadowRect.bottom - i);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.OverlayDrawer
 * JD-Core Version:    0.7.0.1
 */