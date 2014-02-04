package net.simonvt.menudrawer;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.ViewConfiguration;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

class Scroller
{
  private static final float ALPHA = 800.0F;
  private static final float DECELERATION_RATE = (float)(Math.log(0.75D) / Math.log(0.9D));
  private static final int DEFAULT_DURATION = 250;
  private static final float END_TENSION = 0.6F;
  private static final int FLING_MODE = 1;
  private static final int NB_SAMPLES = 100;
  private static final int SCROLL_MODE = 0;
  private static final float[] SPLINE = new float[101];
  private static final float START_TENSION = 0.4F;
  private static float sViscousFluidNormalize = 1.0F / viscousFluid(1.0F);
  private static float sViscousFluidScale;
  private int mCurrX;
  private int mCurrY;
  private float mDeceleration;
  private float mDeltaX;
  private float mDeltaY;
  private int mDuration;
  private float mDurationReciprocal;
  private int mFinalX;
  private int mFinalY;
  private boolean mFinished = true;
  private boolean mFlywheel;
  private Interpolator mInterpolator;
  private int mMaxX;
  private int mMaxY;
  private int mMinX;
  private int mMinY;
  private int mMode;
  private final float mPpi;
  private long mStartTime;
  private int mStartX;
  private int mStartY;
  private float mVelocity;
  
  static
  {
    float f1 = 0.0F;
    for (int i = 0; i <= 100; i++)
    {
      float f4 = i / 100.0F;
      float f5 = 1.0F;
      float f3;
      float f6;
      for (;;)
      {
        f3 = f1 + (f5 - f1) / 2.0F;
        f6 = 3.0F * f3 * (1.0F - f3);
        f2 = f6 * (0.4F * (1.0F - f3) + 0.6F * f3) + f3 * (f3 * f3);
        if (Math.abs(f2 - f4) < 1.E-005D) {
          break;
        }
        if (f2 <= f4) {
          f1 = f3;
        } else {
          f5 = f3;
        }
      }
      float f2 = f6 + f3 * (f3 * f3);
      SPLINE[i] = f2;
    }
    SPLINE[100] = 1.0F;
    sViscousFluidScale = 8.0F;
    sViscousFluidNormalize = 1.0F;
  }
  
  public Scroller(Context paramContext)
  {
    this(paramContext, null);
  }
  
  public Scroller(Context paramContext, Interpolator paramInterpolator)
  {
    this(paramContext, paramInterpolator, bool);
  }
  
  public Scroller(Context paramContext, Interpolator paramInterpolator, boolean paramBoolean)
  {
    this.mInterpolator = paramInterpolator;
    this.mPpi = (160.0F * paramContext.getResources().getDisplayMetrics().density);
    this.mDeceleration = computeDeceleration(ViewConfiguration.getScrollFriction());
    this.mFlywheel = paramBoolean;
  }
  
  private float computeDeceleration(float paramFloat)
  {
    return paramFloat * (386.0878F * this.mPpi);
  }
  
  static float viscousFluid(float paramFloat)
  {
    float f = paramFloat * sViscousFluidScale;
    if (f >= 1.0F) {
      f = 0.3678795F + (1.0F - (float)Math.exp(1.0F - f)) * (1.0F - 0.3678795F);
    } else {
      f -= 1.0F - (float)Math.exp(-f);
    }
    return f * sViscousFluidNormalize;
  }
  
  public void abortAnimation()
  {
    this.mCurrX = this.mFinalX;
    this.mCurrY = this.mFinalY;
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
        this.mCurrX = this.mFinalX;
        this.mCurrY = this.mFinalY;
        this.mFinished = bool;
      }
      else
      {
        float f1;
        switch (this.mMode)
        {
        default: 
          break;
        case 0: 
          f1 = i * this.mDurationReciprocal;
          if (this.mInterpolator != null) {
            f1 = this.mInterpolator.getInterpolation(f1);
          } else {
            f1 = viscousFluid(f1);
          }
          this.mCurrX = (this.mStartX + Math.round(f1 * this.mDeltaX));
          this.mCurrY = (this.mStartY + Math.round(f1 * this.mDeltaY));
          break;
        case 1: 
          f1 = f1 / this.mDuration;
          int j = (int)(100.0F * f1);
          float f3 = j / 100.0F;
          float f4 = (j + 1) / 100.0F;
          float f2 = SPLINE[j];
          float f5 = SPLINE[(j + 1)];
          f1 = f2 + (f1 - f3) / (f4 - f3) * (f5 - f2);
          this.mCurrX = (this.mStartX + Math.round(f1 * (this.mFinalX - this.mStartX)));
          this.mCurrX = Math.min(this.mCurrX, this.mMaxX);
          this.mCurrX = Math.max(this.mCurrX, this.mMinX);
          this.mCurrY = (this.mStartY + Math.round(f1 * (this.mFinalY - this.mStartY)));
          this.mCurrY = Math.min(this.mCurrY, this.mMaxY);
          this.mCurrY = Math.max(this.mCurrY, this.mMinY);
          if ((this.mCurrX != this.mFinalX) || (this.mCurrY != this.mFinalY)) {
            break;
          }
          this.mFinished = bool;
          break;
        }
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
  
  public void fling(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8)
  {
    float f1;
    if ((this.mFlywheel) && (!this.mFinished))
    {
      f1 = getCurrVelocity();
      float f3 = this.mFinalX - this.mStartX;
      float f2 = this.mFinalY - this.mStartY;
      f4 = FloatMath.sqrt(f3 * f3 + f2 * f2);
      f3 /= f4;
      f2 /= f4;
      f3 *= f1;
      f1 = f2 * f1;
      if ((Math.signum(paramInt3) == Math.signum(f3)) && (Math.signum(paramInt4) == Math.signum(f1)))
      {
        paramInt3 = (int)(f3 + paramInt3);
        paramInt4 = (int)(f1 + paramInt4);
      }
    }
    this.mMode = 1;
    this.mFinished = false;
    float f4 = FloatMath.sqrt(paramInt3 * paramInt3 + paramInt4 * paramInt4);
    this.mVelocity = f4;
    double d = Math.log(0.4F * f4 / 800.0F);
    this.mDuration = ((int)(1000.0D * Math.exp(d / (DECELERATION_RATE - 1.0D))));
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStartX = paramInt1;
    this.mStartY = paramInt2;
    if (f4 != 0.0F) {
      f1 = paramInt3 / f4;
    } else {
      f1 = 1.0F;
    }
    if (f4 != 0.0F) {
      f4 = paramInt4 / f4;
    } else {
      f4 = 1.0F;
    }
    int i = (int)(800.0D * Math.exp(d * (DECELERATION_RATE / (DECELERATION_RATE - 1.0D))));
    this.mMinX = paramInt5;
    this.mMaxX = paramInt6;
    this.mMinY = paramInt7;
    this.mMaxY = paramInt8;
    this.mFinalX = (paramInt1 + Math.round(f1 * i));
    this.mFinalX = Math.min(this.mFinalX, this.mMaxX);
    this.mFinalX = Math.max(this.mFinalX, this.mMinX);
    this.mFinalY = (paramInt2 + Math.round(f4 * i));
    this.mFinalY = Math.min(this.mFinalY, this.mMaxY);
    this.mFinalY = Math.max(this.mFinalY, this.mMinY);
  }
  
  public final void forceFinished(boolean paramBoolean)
  {
    this.mFinished = paramBoolean;
  }
  
  public float getCurrVelocity()
  {
    return this.mVelocity - this.mDeceleration * timePassed() / 2000.0F;
  }
  
  public final int getCurrX()
  {
    return this.mCurrX;
  }
  
  public final int getCurrY()
  {
    return this.mCurrY;
  }
  
  public final int getDuration()
  {
    return this.mDuration;
  }
  
  public final int getFinalX()
  {
    return this.mFinalX;
  }
  
  public final int getFinalY()
  {
    return this.mFinalY;
  }
  
  public final int getStartX()
  {
    return this.mStartX;
  }
  
  public final int getStartY()
  {
    return this.mStartY;
  }
  
  public final boolean isFinished()
  {
    return this.mFinished;
  }
  
  public boolean isScrollingInDirection(float paramFloat1, float paramFloat2)
  {
    boolean bool;
    if ((this.mFinished) || (Math.signum(paramFloat1) != Math.signum(this.mFinalX - this.mStartX)) || (Math.signum(paramFloat2) != Math.signum(this.mFinalY - this.mStartY))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void setFinalX(int paramInt)
  {
    this.mFinalX = paramInt;
    this.mDeltaX = (this.mFinalX - this.mStartX);
    this.mFinished = false;
  }
  
  public void setFinalY(int paramInt)
  {
    this.mFinalY = paramInt;
    this.mDeltaY = (this.mFinalY - this.mStartY);
    this.mFinished = false;
  }
  
  public final void setFriction(float paramFloat)
  {
    this.mDeceleration = computeDeceleration(paramFloat);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    startScroll(paramInt1, paramInt2, paramInt3, paramInt4, 250);
  }
  
  public void startScroll(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
  {
    this.mMode = 0;
    this.mFinished = false;
    this.mDuration = paramInt5;
    this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
    this.mStartX = paramInt1;
    this.mStartY = paramInt2;
    this.mFinalX = (paramInt1 + paramInt3);
    this.mFinalY = (paramInt2 + paramInt4);
    this.mDeltaX = paramInt3;
    this.mDeltaY = paramInt4;
    this.mDurationReciprocal = (1.0F / this.mDuration);
  }
  
  public int timePassed()
  {
    return (int)(AnimationUtils.currentAnimationTimeMillis() - this.mStartTime);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.Scroller
 * JD-Core Version:    0.7.0.1
 */