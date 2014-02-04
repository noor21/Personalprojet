package com.nineoldandroids.animation;

public class TimeAnimator
  extends ValueAnimator
{
  private TimeListener mListener;
  private long mPreviousTime = -1L;
  
  void animateValue(float paramFloat) {}
  
  boolean animationFrame(long paramLong)
  {
    long l2 = 0L;
    if (this.mPlayingState == 0)
    {
      this.mPlayingState = 1;
      if (this.mSeekTime >= l2)
      {
        this.mStartTime = (paramLong - this.mSeekTime);
        this.mSeekTime = -1L;
      }
      else
      {
        this.mStartTime = paramLong;
      }
    }
    if (this.mListener != null)
    {
      long l1 = paramLong - this.mStartTime;
      if (this.mPreviousTime >= l2) {
        l2 = paramLong - this.mPreviousTime;
      }
      this.mPreviousTime = paramLong;
      this.mListener.onTimeUpdate(this, l1, l2);
    }
    return false;
  }
  
  void initAnimation() {}
  
  public void setTimeListener(TimeListener paramTimeListener)
  {
    this.mListener = paramTimeListener;
  }
  
  public static abstract interface TimeListener
  {
    public abstract void onTimeUpdate(TimeAnimator paramTimeAnimator, long paramLong1, long paramLong2);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.TimeAnimator
 * JD-Core Version:    0.7.0.1
 */