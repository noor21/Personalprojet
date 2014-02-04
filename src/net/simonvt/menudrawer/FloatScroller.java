package net.simonvt.menudrawer;

import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

class FloatScroller
{
  private float mCurr;
  private float mDeltaX;
  private int mDuration;
  private float mDurationReciprocal;
  private float mFinal;
  private boolean mFinished = true;
  private Interpolator mInterpolator;
  private float mStart;
  private long mStartTime;
  
  public FloatScroller(Interpolator paramInterpolator)
  {
    this.mInterpolator = paramInterpolator;
  }
  
  public void abortAnimation()
  {
    this.mCurr = this.mFinal;
    this.mFinished = true;
  }
  
  public boolean computeScrollOffset()
  {
    boolean bool = true;
    if (!this.mFinished)
    {
      int i = (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
      if (i >= this.mDuration)
      {
        this.mCurr = this.mFinal;
        this.mFinished = bool;
      }
      else
      {
        float f = i * this.mDurationReciprocal;
        f = this.mInterpolator.getInterpolation(f);
        this.mCurr = (this.mStart + f * this.mDeltaX);
      }
    }
    else
    {
      bool = false;
    }
    return bool;
  }
  
  public void extendDuration(int paramInt)
  {
    this.mDuration = (paramInt + timePassed());
    this.mDurationReciprocal = (1.0F / this.mDuration);
    this.mFinished = false;
  }
  
  public final void forceFinished(boolean paramBoolean)
  {
    this.mFinished = paramBoolean;
  }
  
  public final float getCurr()
  {
    return this.mCurr;
  }
  
  public final int getDuration()
  {
    return this.mDuration;
  }
  
  public final float getFinal()
  {
    return this.mFinal;
  }
  
  public final float getStart()
  {
    return this.mStart;
  }
  
  public final boolean isFinished()
  {
    return this.mFinished;
  }
  
  public void setFinal(float paramFloat)
  {
    this.mFinal = paramFloat;
    this.mDeltaX = (this.mFinal - this.mStart);
    this.mFinished = false;
  }
  
  public void startScroll(float paramFloat1, float paramFloat2, int paramInt)
  {
    this.mFinished = false;
    this.mDuration = paramInt;
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStart = paramFloat1;
    this.mFinal = (paramFloat1 + paramFloat2);
    this.mDeltaX = paramFloat2;
    this.mDurationReciprocal = (1.0F / this.mDuration);
  }
  
  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.FloatScroller
 * JD-Core Version:    0.7.0.1
 */