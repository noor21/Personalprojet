package android.support.v4.view;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnDoubleTapListener;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;

public class GestureDetectorCompat
{
  private final GestureDetectorCompatImpl mImpl;
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener)
  {
    this(paramContext, paramOnGestureListener, null);
  }
  
  public GestureDetectorCompat(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
  {
    if (Build.VERSION.SDK_INT <= 17) {
      this.mImpl = new GestureDetectorCompatImplBase(paramContext, paramOnGestureListener, paramHandler);
    } else {
      this.mImpl = new GestureDetectorCompatImplJellybeanMr2(paramContext, paramOnGestureListener, paramHandler);
    }
  }
  
  public boolean isLongpressEnabled()
  {
    return this.mImpl.isLongpressEnabled();
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.mImpl.onTouchEvent(paramMotionEvent);
  }
  
  public void setIsLongpressEnabled(boolean paramBoolean)
  {
    this.mImpl.setIsLongpressEnabled(paramBoolean);
  }
  
  public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
  {
    this.mImpl.setOnDoubleTapListener(paramOnDoubleTapListener);
  }
  
  static class GestureDetectorCompatImplJellybeanMr2
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private final GestureDetector mDetector;
    
    public GestureDetectorCompatImplJellybeanMr2(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      this.mDetector = new GestureDetector(paramContext, paramOnGestureListener, paramHandler);
    }
    
    public boolean isLongpressEnabled()
    {
      return this.mDetector.isLongpressEnabled();
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      return this.mDetector.onTouchEvent(paramMotionEvent);
    }
    
    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mDetector.setIsLongpressEnabled(paramBoolean);
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDetector.setOnDoubleTapListener(paramOnDoubleTapListener);
    }
  }
  
  static class GestureDetectorCompatImplBase
    implements GestureDetectorCompat.GestureDetectorCompatImpl
  {
    private static final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();
    private static final int LONGPRESS_TIMEOUT = ;
    private static final int LONG_PRESS = 2;
    private static final int SHOW_PRESS = 1;
    private static final int TAP = 3;
    private static final int TAP_TIMEOUT = ViewConfiguration.getTapTimeout();
    private boolean mAlwaysInBiggerTapRegion;
    private boolean mAlwaysInTapRegion;
    private MotionEvent mCurrentDownEvent;
    private boolean mDeferConfirmSingleTap;
    private GestureDetector.OnDoubleTapListener mDoubleTapListener;
    private int mDoubleTapSlopSquare;
    private float mDownFocusX;
    private float mDownFocusY;
    private final Handler mHandler;
    private boolean mInLongPress;
    private boolean mIsDoubleTapping;
    private boolean mIsLongpressEnabled;
    private float mLastFocusX;
    private float mLastFocusY;
    private final GestureDetector.OnGestureListener mListener;
    private int mMaximumFlingVelocity;
    private int mMinimumFlingVelocity;
    private MotionEvent mPreviousUpEvent;
    private boolean mStillDown;
    private int mTouchSlopSquare;
    private VelocityTracker mVelocityTracker;
    
    public GestureDetectorCompatImplBase(Context paramContext, GestureDetector.OnGestureListener paramOnGestureListener, Handler paramHandler)
    {
      if (paramHandler == null) {
        this.mHandler = new GestureHandler();
      } else {
        this.mHandler = new GestureHandler(paramHandler);
      }
      this.mListener = paramOnGestureListener;
      if ((paramOnGestureListener instanceof GestureDetector.OnDoubleTapListener)) {
        setOnDoubleTapListener((GestureDetector.OnDoubleTapListener)paramOnGestureListener);
      }
      init(paramContext);
    }
    
    private void cancel()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mVelocityTracker.recycle();
      this.mVelocityTracker = null;
      this.mIsDoubleTapping = false;
      this.mStillDown = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress) {
        this.mInLongPress = false;
      }
    }
    
    private void cancelTaps()
    {
      this.mHandler.removeMessages(1);
      this.mHandler.removeMessages(2);
      this.mHandler.removeMessages(3);
      this.mIsDoubleTapping = false;
      this.mAlwaysInTapRegion = false;
      this.mAlwaysInBiggerTapRegion = false;
      this.mDeferConfirmSingleTap = false;
      if (this.mInLongPress) {
        this.mInLongPress = false;
      }
    }
    
    private void dispatchLongPress()
    {
      this.mHandler.removeMessages(3);
      this.mDeferConfirmSingleTap = false;
      this.mInLongPress = true;
      this.mListener.onLongPress(this.mCurrentDownEvent);
    }
    
    private void init(Context paramContext)
    {
      if (paramContext != null)
      {
        if (this.mListener != null)
        {
          this.mIsLongpressEnabled = true;
          ViewConfiguration localViewConfiguration = ViewConfiguration.get(paramContext);
          int j = localViewConfiguration.getScaledTouchSlop();
          int i = localViewConfiguration.getScaledDoubleTapSlop();
          this.mMinimumFlingVelocity = localViewConfiguration.getScaledMinimumFlingVelocity();
          this.mMaximumFlingVelocity = localViewConfiguration.getScaledMaximumFlingVelocity();
          this.mTouchSlopSquare = (j * j);
          this.mDoubleTapSlopSquare = (i * i);
          return;
        }
        throw new IllegalArgumentException("OnGestureListener must not be null");
      }
      throw new IllegalArgumentException("Context must not be null");
    }
    
    private boolean isConsideredDoubleTap(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, MotionEvent paramMotionEvent3)
    {
      boolean bool = false;
      if ((this.mAlwaysInBiggerTapRegion) && (paramMotionEvent3.getEventTime() - paramMotionEvent2.getEventTime() <= DOUBLE_TAP_TIMEOUT))
      {
        int i = (int)paramMotionEvent1.getX() - (int)paramMotionEvent3.getX();
        int j = (int)paramMotionEvent1.getY() - (int)paramMotionEvent3.getY();
        if (i * i + j * j < this.mDoubleTapSlopSquare) {
          bool = true;
        }
      }
      return bool;
    }
    
    public boolean isLongpressEnabled()
    {
      return this.mIsLongpressEnabled;
    }
    
    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      int k = paramMotionEvent.getAction();
      if (this.mVelocityTracker == null) {
        this.mVelocityTracker = VelocityTracker.obtain();
      }
      this.mVelocityTracker.addMovement(paramMotionEvent);
      int i1;
      if ((k & 0xFF) != 6) {
        i1 = 0;
      } else {
        i1 = 1;
      }
      float f9;
      if (i1 == 0) {
        f9 = -1;
      } else {
        f9 = MotionEventCompat.getActionIndex(paramMotionEvent);
      }
      float f4 = 0.0F;
      float f1 = 0.0F;
      int i = MotionEventCompat.getPointerCount(paramMotionEvent);
      float f8;
      for (int m = 0;; f8++)
      {
        boolean bool1;
        int j;
        if (m >= i)
        {
          if (i1 == 0) {
            m = i;
          } else {
            m = i - 1;
          }
          f4 /= m;
          float f7 = f1 / m;
          bool1 = false;
          VelocityTracker localVelocityTracker;
          float f6;
          float f3;
          switch (k & 0xFF)
          {
          case 0: 
            if (this.mDoubleTapListener != null)
            {
              boolean bool2 = this.mHandler.hasMessages(3);
              if (bool2) {
                this.mHandler.removeMessages(3);
              }
              if ((this.mCurrentDownEvent == null) || (this.mPreviousUpEvent == null) || (!bool2) || (!isConsideredDoubleTap(this.mCurrentDownEvent, this.mPreviousUpEvent, paramMotionEvent)))
              {
                this.mHandler.sendEmptyMessageDelayed(3, DOUBLE_TAP_TIMEOUT);
              }
              else
              {
                this.mIsDoubleTapping = true;
                bool1 = false | this.mDoubleTapListener.onDoubleTap(this.mCurrentDownEvent) | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
              }
            }
            this.mLastFocusX = f4;
            this.mDownFocusX = f4;
            this.mLastFocusY = f7;
            this.mDownFocusY = f7;
            if (this.mCurrentDownEvent != null) {
              this.mCurrentDownEvent.recycle();
            }
            this.mCurrentDownEvent = MotionEvent.obtain(paramMotionEvent);
            this.mAlwaysInTapRegion = true;
            this.mAlwaysInBiggerTapRegion = true;
            this.mStillDown = true;
            this.mInLongPress = false;
            this.mDeferConfirmSingleTap = false;
            if (this.mIsLongpressEnabled)
            {
              this.mHandler.removeMessages(2);
              this.mHandler.sendEmptyMessageAtTime(2, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT + LONGPRESS_TIMEOUT);
            }
            this.mHandler.sendEmptyMessageAtTime(1, this.mCurrentDownEvent.getDownTime() + TAP_TIMEOUT);
            bool1 |= this.mListener.onDown(paramMotionEvent);
            break;
          case 1: 
            this.mStillDown = false;
            MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
            if (!this.mIsDoubleTapping)
            {
              if (!this.mInLongPress)
              {
                if (!this.mAlwaysInTapRegion)
                {
                  localVelocityTracker = this.mVelocityTracker;
                  k = MotionEventCompat.getPointerId(paramMotionEvent, 0);
                  localVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
                  f4 = VelocityTrackerCompat.getYVelocity(localVelocityTracker, k);
                  f6 = VelocityTrackerCompat.getXVelocity(localVelocityTracker, k);
                  if ((Math.abs(f4) > this.mMinimumFlingVelocity) || (Math.abs(f6) > this.mMinimumFlingVelocity)) {
                    bool1 = this.mListener.onFling(this.mCurrentDownEvent, paramMotionEvent, f6, f4);
                  }
                }
                else
                {
                  bool1 = this.mListener.onSingleTapUp(paramMotionEvent);
                  if ((this.mDeferConfirmSingleTap) && (this.mDoubleTapListener != null)) {
                    this.mDoubleTapListener.onSingleTapConfirmed(paramMotionEvent);
                  }
                }
              }
              else
              {
                this.mHandler.removeMessages(3);
                this.mInLongPress = false;
              }
            }
            else {
              bool1 = false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
            }
            if (this.mPreviousUpEvent != null) {
              this.mPreviousUpEvent.recycle();
            }
            this.mPreviousUpEvent = localMotionEvent;
            if (this.mVelocityTracker != null)
            {
              this.mVelocityTracker.recycle();
              this.mVelocityTracker = null;
            }
            this.mIsDoubleTapping = false;
            this.mDeferConfirmSingleTap = false;
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            break;
          case 2: 
            if (!this.mInLongPress)
            {
              f6 = this.mLastFocusX - f4;
              f3 = this.mLastFocusY - localVelocityTracker;
              if (!this.mIsDoubleTapping)
              {
                if (!this.mAlwaysInTapRegion)
                {
                  if ((Math.abs(f6) >= 1.0F) || (Math.abs(f3) >= 1.0F))
                  {
                    bool1 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f6, f3);
                    this.mLastFocusX = f4;
                    this.mLastFocusY = localVelocityTracker;
                  }
                }
                else
                {
                  i1 = (int)(f4 - this.mDownFocusX);
                  f9 = (int)(localVelocityTracker - this.mDownFocusY);
                  i1 = i1 * i1 + f9 * f9;
                  if (i1 > this.mTouchSlopSquare)
                  {
                    bool1 = this.mListener.onScroll(this.mCurrentDownEvent, paramMotionEvent, f6, f3);
                    this.mLastFocusX = f4;
                    this.mLastFocusY = localVelocityTracker;
                    this.mAlwaysInTapRegion = false;
                    this.mHandler.removeMessages(3);
                    this.mHandler.removeMessages(1);
                    this.mHandler.removeMessages(2);
                  }
                  if (i1 > this.mTouchSlopSquare) {
                    this.mAlwaysInBiggerTapRegion = false;
                  }
                }
              }
              else {
                bool1 = false | this.mDoubleTapListener.onDoubleTapEvent(paramMotionEvent);
              }
            }
            break;
          case 3: 
            cancel();
            break;
          case 5: 
            this.mLastFocusX = f4;
            this.mDownFocusX = f4;
            this.mLastFocusY = localVelocityTracker;
            this.mDownFocusY = localVelocityTracker;
            cancelTaps();
            break;
          case 6: 
            this.mLastFocusX = f4;
            this.mDownFocusX = f4;
            this.mLastFocusY = localVelocityTracker;
            this.mDownFocusY = localVelocityTracker;
            this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumFlingVelocity);
            j = MotionEventCompat.getActionIndex(paramMotionEvent);
            int n = MotionEventCompat.getPointerId(paramMotionEvent, j);
            f6 = VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, n);
            f8 = VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, n);
            f9 = 0;
            while (f9 < f3)
            {
              if (f9 != j)
              {
                i1 = MotionEventCompat.getPointerId(paramMotionEvent, f9);
                if (f6 * VelocityTrackerCompat.getXVelocity(this.mVelocityTracker, i1) + f8 * VelocityTrackerCompat.getYVelocity(this.mVelocityTracker, i1) < 0.0F) {}
              }
              else
              {
                f9++;
                continue;
              }
              this.mVelocityTracker.clear();
            }
          }
          return bool1;
        }
        if (f9 != f8)
        {
          float f5;
          j += MotionEventCompat.getX(paramMotionEvent, f8);
          float f2;
          bool1 += MotionEventCompat.getY(paramMotionEvent, f8);
        }
      }
    }
    
    public void setIsLongpressEnabled(boolean paramBoolean)
    {
      this.mIsLongpressEnabled = paramBoolean;
    }
    
    public void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener)
    {
      this.mDoubleTapListener = paramOnDoubleTapListener;
    }
    
    private class GestureHandler
      extends Handler
    {
      GestureHandler() {}
      
      GestureHandler(Handler paramHandler)
      {
        super();
      }
      
      public void handleMessage(Message paramMessage)
      {
        switch (paramMessage.what)
        {
        default: 
          throw new RuntimeException("Unknown message " + paramMessage);
        case 1: 
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.mListener.onShowPress(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
          break;
        case 2: 
          GestureDetectorCompat.GestureDetectorCompatImplBase.this.dispatchLongPress();
          break;
        case 3: 
          if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener != null) {
            if (GestureDetectorCompat.GestureDetectorCompatImplBase.this.mStillDown) {
              GestureDetectorCompat.GestureDetectorCompatImplBase.access$502(GestureDetectorCompat.GestureDetectorCompatImplBase.this, true);
            } else {
              GestureDetectorCompat.GestureDetectorCompatImplBase.this.mDoubleTapListener.onSingleTapConfirmed(GestureDetectorCompat.GestureDetectorCompatImplBase.this.mCurrentDownEvent);
            }
          }
          break;
        }
      }
    }
  }
  
  static abstract interface GestureDetectorCompatImpl
  {
    public abstract boolean isLongpressEnabled();
    
    public abstract boolean onTouchEvent(MotionEvent paramMotionEvent);
    
    public abstract void setIsLongpressEnabled(boolean paramBoolean);
    
    public abstract void setOnDoubleTapListener(GestureDetector.OnDoubleTapListener paramOnDoubleTapListener);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.GestureDetectorCompat
 * JD-Core Version:    0.7.0.1
 */