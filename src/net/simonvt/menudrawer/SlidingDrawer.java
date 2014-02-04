package net.simonvt.menudrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public class SlidingDrawer
  extends DraggableDrawer
{
  private static final String TAG = "OverlayDrawer";
  
  SlidingDrawer(Activity paramActivity, int paramInt)
  {
    super(paramActivity, paramInt);
  }
  
  public SlidingDrawer(Context paramContext)
  {
    super(paramContext);
  }
  
  public SlidingDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public SlidingDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  private boolean isContentTouch(int paramInt1, int paramInt2)
  {
    boolean bool = false;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (ViewHelper.getLeft(this.mContentContainer) >= paramInt1) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case LEFT: 
      if (ViewHelper.getTop(this.mContentContainer) >= paramInt2) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case RIGHT: 
      if (ViewHelper.getRight(this.mContentContainer) <= paramInt1) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    case TOP: 
      if (ViewHelper.getBottom(this.mContentContainer) <= paramInt2) {
        bool = false;
      } else {
        bool = true;
      }
      break;
    }
    return bool;
  }
  
  private void offsetMenu(int paramInt)
  {
    int i = 4;
    if ((this.mOffsetMenu) && (this.mMenuSize != 0))
    {
      int n = getWidth();
      int j = getHeight();
      int i1 = this.mMenuSize;
      int i2 = (int)(this.mOffsetPixels / Math.abs(this.mOffsetPixels));
      i2 = (int)(-0.25F * ((1.0F - Math.abs(this.mOffsetPixels) / i1) * i1) * i2);
      BuildLayerFrameLayout localBuildLayerFrameLayout1;
      BuildLayerFrameLayout localBuildLayerFrameLayout2;
      switch (this.mPosition)
      {
      default: 
        break;
      case BOTTOM: 
        if (!USE_TRANSLATIONS)
        {
          this.mMenuContainer.offsetLeftAndRight(i2 - this.mMenuContainer.getLeft());
          localBuildLayerFrameLayout1 = this.mMenuContainer;
          if (paramInt != 0) {
            i = 0;
          }
          localBuildLayerFrameLayout1.setVisibility(i);
        }
        else if (paramInt <= 0)
        {
          this.mMenuContainer.setTranslationX(-i1);
        }
        else
        {
          this.mMenuContainer.setTranslationX(i2);
        }
        break;
      case LEFT: 
        if (!USE_TRANSLATIONS)
        {
          this.mMenuContainer.offsetTopAndBottom(i2 - this.mMenuContainer.getTop());
          localBuildLayerFrameLayout1 = this.mMenuContainer;
          if (paramInt != 0) {
            i = 0;
          }
          localBuildLayerFrameLayout1.setVisibility(i);
        }
        else if (paramInt <= 0)
        {
          this.mMenuContainer.setTranslationY(-i1);
        }
        else
        {
          this.mMenuContainer.setTranslationY(i2);
        }
        break;
      case RIGHT: 
        if (!USE_TRANSLATIONS)
        {
          int k = i2 - (this.mMenuContainer.getRight() - n);
          this.mMenuContainer.offsetLeftAndRight(k);
          localBuildLayerFrameLayout2 = this.mMenuContainer;
          if (paramInt != 0) {
            i = 0;
          }
          localBuildLayerFrameLayout2.setVisibility(i);
        }
        else if (paramInt == 0)
        {
          this.mMenuContainer.setTranslationX(i1);
        }
        else
        {
          this.mMenuContainer.setTranslationX(i2);
        }
        break;
      case TOP: 
        if (!USE_TRANSLATIONS)
        {
          int m = i2 - (this.mMenuContainer.getBottom() - localBuildLayerFrameLayout2);
          this.mMenuContainer.offsetTopAndBottom(m);
          BuildLayerFrameLayout localBuildLayerFrameLayout3 = this.mMenuContainer;
          if (paramInt != 0) {
            i = 0;
          }
          localBuildLayerFrameLayout3.setVisibility(i);
        }
        else if (paramInt == 0)
        {
          this.mMenuContainer.setTranslationY(i1);
        }
        else
        {
          this.mMenuContainer.setTranslationY(i2);
        }
        break;
      }
    }
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
      this.mMenuOverlay.setBounds(0, 0, i, k);
      break;
    case LEFT: 
      this.mMenuOverlay.setBounds(0, 0, j, i);
      break;
    case RIGHT: 
      this.mMenuOverlay.setBounds(j + i, 0, j, k);
      break;
    case TOP: 
      this.mMenuOverlay.setBounds(0, k + i, j, k);
    }
    this.mMenuOverlay.setAlpha((int)(185.0F * (1.0F - f)));
    this.mMenuOverlay.draw(paramCanvas);
  }
  
  protected void initDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super.initDrawer(paramContext, paramAttributeSet, paramInt);
    super.addView(this.mMenuContainer, -1, new ViewGroup.LayoutParams(-1, -1));
    super.addView(this.mContentContainer, -1, new ViewGroup.LayoutParams(-1, -1));
  }
  
  protected void initPeekScroller()
  {
    int i;
    switch (this.mPosition)
    {
    default: 
      i = this.mMenuSize / 3;
      this.mPeekScroller.startScroll(0, 0, i, 0, 5000);
      break;
    case RIGHT: 
    case TOP: 
      i = -this.mMenuSize / 3;
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
      if (((!this.mMenuVisible) && (this.mInitialMotionX <= this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionX >= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case LEFT: 
      if (((!this.mMenuVisible) && (this.mInitialMotionY <= this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionY >= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case RIGHT: 
      i = getWidth();
      int j = (int)this.mInitialMotionX;
      if (((!this.mMenuVisible) && (j >= i - this.mTouchSize)) || ((this.mMenuVisible) && (j <= i + this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case TOP: 
      i = getHeight();
      if (((!this.mMenuVisible) && (this.mInitialMotionY >= i - this.mTouchSize)) || ((this.mMenuVisible) && (this.mInitialMotionY <= i + this.mOffsetPixels))) {
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
                  float f2 = paramMotionEvent.getX(m);
                  float f3 = f2 - this.mLastMotionX;
                  float f5 = paramMotionEvent.getY(m);
                  float f4 = f5 - this.mLastMotionY;
                  if (checkTouchSlop(f3, f4)) {
                    if ((this.mOnInterceptMoveEventListener == null) || ((this.mTouchMode != 2) && (!this.mMenuVisible)) || (!canChildrenScroll((int)f3, (int)f4, (int)f2, (int)f5)))
                    {
                      if (onMoveAllowDrag((int)f2, (int)f5, f3, f4))
                      {
                        setDrawerState(2);
                        this.mIsDragging = true;
                        this.mLastMotionX = f2;
                        this.mLastMotionY = f5;
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
    int i = paramInt3 - paramInt1;
    int k = paramInt4 - paramInt2;
    if (!USE_TRANSLATIONS)
    {
      int j = (int)this.mOffsetPixels;
      if ((this.mPosition != Position.LEFT) && (this.mPosition != Position.RIGHT)) {
        this.mContentContainer.layout(0, j, i, k + j);
      } else {
        this.mContentContainer.layout(j, 0, i + j, k);
      }
    }
    else
    {
      this.mContentContainer.layout(0, 0, i, k);
    }
    switch (this.mPosition)
    {
    case BOTTOM: 
      this.mMenuContainer.layout(0, 0, this.mMenuSize, k);
      break;
    case LEFT: 
      this.mMenuContainer.layout(0, 0, i, this.mMenuSize);
      break;
    case RIGHT: 
      this.mMenuContainer.layout(i - this.mMenuSize, 0, i, k);
      break;
    case TOP: 
      this.mMenuContainer.layout(0, k - this.mMenuSize, i, k);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    if ((i == 1073741824) && (j == 1073741824))
    {
      i = View.MeasureSpec.getSize(paramInt1);
      j = View.MeasureSpec.getSize(paramInt2);
      if (this.mOffsetPixels == -1.0F) {
        openMenu(false);
      }
      switch (this.mPosition)
      {
      case RIGHT: 
      default: 
        m = getChildMeasureSpec(paramInt1, 0, this.mMenuSize);
        k = getChildMeasureSpec(paramInt1, 0, j);
        break;
      case LEFT: 
      case TOP: 
        m = getChildMeasureSpec(paramInt1, 0, i);
        k = getChildMeasureSpec(paramInt2, 0, this.mMenuSize);
      }
      this.mMenuContainer.measure(m, k);
      int m = getChildMeasureSpec(paramInt1, 0, i);
      int k = getChildMeasureSpec(paramInt1, 0, j);
      this.mContentContainer.measure(m, k);
      setMeasuredDimension(i, j);
      updateTouchAreaSize();
      return;
    }
    throw new IllegalStateException("Must measure with an exact size");
  }
  
  protected boolean onMoveAllowDrag(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
  {
    boolean bool = false;
    int i;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (((!this.mMenuVisible) && (this.mInitialMotionX <= this.mTouchSize) && (paramFloat1 > 0.0F)) || ((this.mMenuVisible) && (paramInt1 >= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case LEFT: 
      if (((!this.mMenuVisible) && (this.mInitialMotionY <= this.mTouchSize) && (paramFloat2 > 0.0F)) || ((this.mMenuVisible) && (paramInt2 >= this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case RIGHT: 
      i = getWidth();
      if (((!this.mMenuVisible) && (this.mInitialMotionX >= i - this.mTouchSize) && (paramFloat1 < 0.0F)) || ((this.mMenuVisible) && (paramInt1 <= i + this.mOffsetPixels))) {
        bool = true;
      }
      break;
    case TOP: 
      i = getHeight();
      if (((!this.mMenuVisible) && (this.mInitialMotionY >= i - this.mTouchSize) && (paramFloat2 < 0.0F)) || ((this.mMenuVisible) && (paramInt2 <= i + this.mOffsetPixels))) {
        bool = true;
      }
      break;
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
      case RIGHT: 
      default: 
        this.mContentContainer.offsetLeftAndRight(paramInt - this.mContentContainer.getLeft());
        break;
      case LEFT: 
      case TOP: 
        this.mContentContainer.offsetTopAndBottom(paramInt - this.mContentContainer.getTop());
        break;
      }
    } else {
      switch (this.mPosition)
      {
      case RIGHT: 
      default: 
        this.mContentContainer.setTranslationX(paramInt);
        break;
      case LEFT: 
      case TOP: 
        this.mContentContainer.setTranslationY(paramInt);
      }
    }
    offsetMenu(paramInt);
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
            f5 = paramMotionEvent.getY(j);
            float f2 = f5 - this.mLastMotionY;
            this.mLastMotionX = f3;
            this.mLastMotionY = f5;
            onMoveEvent(f4, f2);
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
    int k = (int)this.mOffsetPixels;
    int j;
    switch (this.mPosition)
    {
    case BOTTOM: 
      if (!this.mIsDragging)
      {
        if ((this.mMenuVisible) && (paramInt1 > k)) {
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
        if ((this.mMenuVisible) && (paramInt2 > k)) {
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
      j = getWidth();
      if (!this.mIsDragging)
      {
        if ((this.mMenuVisible) && (paramInt1 < j + k)) {
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
        if ((this.mMenuVisible) && (paramInt2 < k + getHeight())) {
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
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.SlidingDrawer
 * JD-Core Version:    0.7.0.1
 */