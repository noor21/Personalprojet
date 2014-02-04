package com.nineoldandroids.animation;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AndroidRuntimeException;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ValueAnimator
  extends Animator
{
  static final int ANIMATION_FRAME = 1;
  static final int ANIMATION_START = 0;
  private static final long DEFAULT_FRAME_DELAY = 10L;
  public static final int INFINITE = -1;
  public static final int RESTART = 1;
  public static final int REVERSE = 2;
  static final int RUNNING = 1;
  static final int SEEKED = 2;
  static final int STOPPED;
  private static ThreadLocal<AnimationHandler> sAnimationHandler = new ThreadLocal();
  private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations = new ThreadLocal()
  {
    protected ArrayList<ValueAnimator> initialValue()
    {
      return new ArrayList();
    }
  };
  private static final Interpolator sDefaultInterpolator;
  private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims;
  private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims;
  private static final TypeEvaluator sFloatEvaluator = new FloatEvaluator();
  private static long sFrameDelay = 10L;
  private static final TypeEvaluator sIntEvaluator;
  private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations = new ThreadLocal()
  {
    protected ArrayList<ValueAnimator> initialValue()
    {
      return new ArrayList();
    }
  };
  private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims;
  private float mCurrentFraction = 0.0F;
  private int mCurrentIteration = 0;
  private long mDelayStartTime;
  private long mDuration = 300L;
  boolean mInitialized = false;
  private Interpolator mInterpolator = sDefaultInterpolator;
  private boolean mPlayingBackwards = false;
  int mPlayingState = 0;
  private int mRepeatCount = 0;
  private int mRepeatMode = 1;
  private boolean mRunning = false;
  long mSeekTime = -1L;
  private long mStartDelay = 0L;
  long mStartTime;
  private boolean mStarted = false;
  private boolean mStartedDelay = false;
  private ArrayList<AnimatorUpdateListener> mUpdateListeners = null;
  PropertyValuesHolder[] mValues;
  HashMap<String, PropertyValuesHolder> mValuesMap;
  
  static
  {
    sDelayedAnims = new ThreadLocal()
    {
      protected ArrayList<ValueAnimator> initialValue()
      {
        return new ArrayList();
      }
    };
    sEndingAnims = new ThreadLocal()
    {
      protected ArrayList<ValueAnimator> initialValue()
      {
        return new ArrayList();
      }
    };
    sReadyAnims = new ThreadLocal()
    {
      protected ArrayList<ValueAnimator> initialValue()
      {
        return new ArrayList();
      }
    };
    sDefaultInterpolator = new AccelerateDecelerateInterpolator();
    sIntEvaluator = new IntEvaluator();
  }
  
  public static void clearAllAnimations()
  {
    ((ArrayList)sAnimations.get()).clear();
    ((ArrayList)sPendingAnimations.get()).clear();
    ((ArrayList)sDelayedAnims.get()).clear();
  }
  
  private boolean delayedAnimationFrame(long paramLong)
  {
    int i = 1;
    if (this.mStartedDelay)
    {
      long l = paramLong - this.mDelayStartTime;
      if (l > this.mStartDelay)
      {
        this.mStartTime = (paramLong - (l - this.mStartDelay));
        this.mPlayingState = i;
        break label61;
      }
    }
    else
    {
      this.mStartedDelay = i;
      this.mDelayStartTime = paramLong;
    }
    i = 0;
    label61:
    return i;
  }
  
  private void endAnimation()
  {
    ((ArrayList)sAnimations.get()).remove(this);
    ((ArrayList)sPendingAnimations.get()).remove(this);
    ((ArrayList)sDelayedAnims.get()).remove(this);
    this.mPlayingState = 0;
    ArrayList localArrayList;
    int i;
    if ((this.mRunning) && (this.mListeners != null))
    {
      localArrayList = (ArrayList)this.mListeners.clone();
      i = localArrayList.size();
    }
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        this.mRunning = false;
        this.mStarted = false;
        return;
      }
      ((Animator.AnimatorListener)localArrayList.get(j)).onAnimationEnd(this);
    }
  }
  
  public static int getCurrentAnimationsCount()
  {
    return ((ArrayList)sAnimations.get()).size();
  }
  
  public static long getFrameDelay()
  {
    return sFrameDelay;
  }
  
  public static ValueAnimator ofFloat(float... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setFloatValues(paramVarArgs);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofInt(int... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setIntValues(paramVarArgs);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofObject(TypeEvaluator paramTypeEvaluator, Object... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setObjectValues(paramVarArgs);
    localValueAnimator.setEvaluator(paramTypeEvaluator);
    return localValueAnimator;
  }
  
  public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... paramVarArgs)
  {
    ValueAnimator localValueAnimator = new ValueAnimator();
    localValueAnimator.setValues(paramVarArgs);
    return localValueAnimator;
  }
  
  public static void setFrameDelay(long paramLong)
  {
    sFrameDelay = paramLong;
  }
  
  private void start(boolean paramBoolean)
  {
    if (Looper.myLooper() != null)
    {
      this.mPlayingBackwards = paramBoolean;
      this.mCurrentIteration = 0;
      this.mPlayingState = 0;
      this.mStarted = true;
      this.mStartedDelay = false;
      ((ArrayList)sPendingAnimations.get()).add(this);
      ArrayList localArrayList;
      int j;
      if (this.mStartDelay == 0L)
      {
        setCurrentPlayTime(getCurrentPlayTime());
        this.mPlayingState = 0;
        this.mRunning = true;
        if (this.mListeners != null)
        {
          localArrayList = (ArrayList)this.mListeners.clone();
          j = localArrayList.size();
        }
      }
      AnimationHandler localAnimationHandler;
      for (int i = 0;; localAnimationHandler++)
      {
        if (i >= j)
        {
          localAnimationHandler = (AnimationHandler)sAnimationHandler.get();
          if (localAnimationHandler == null)
          {
            localAnimationHandler = new AnimationHandler(null);
            sAnimationHandler.set(localAnimationHandler);
          }
          localAnimationHandler.sendEmptyMessage(0);
          return;
        }
        ((Animator.AnimatorListener)localArrayList.get(localAnimationHandler)).onAnimationStart(this);
      }
    }
    throw new AndroidRuntimeException("Animators may only be run on Looper threads");
  }
  
  private void startAnimation()
  {
    initAnimation();
    ((ArrayList)sAnimations.get()).add(this);
    ArrayList localArrayList;
    int j;
    if ((this.mStartDelay > 0L) && (this.mListeners != null))
    {
      localArrayList = (ArrayList)this.mListeners.clone();
      j = localArrayList.size();
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((Animator.AnimatorListener)localArrayList.get(i)).onAnimationStart(this);
    }
  }
  
  public void addUpdateListener(AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    if (this.mUpdateListeners == null) {
      this.mUpdateListeners = new ArrayList();
    }
    this.mUpdateListeners.add(paramAnimatorUpdateListener);
  }
  
  void animateValue(float paramFloat)
  {
    float f = this.mInterpolator.getInterpolation(paramFloat);
    this.mCurrentFraction = f;
    int k = this.mValues.length;
    for (int i = 0;; i++)
    {
      int j;
      if (i >= k)
      {
        if (this.mUpdateListeners != null) {
          j = this.mUpdateListeners.size();
        }
        for (i = 0;; i++)
        {
          if (i >= j) {
            return;
          }
          ((AnimatorUpdateListener)this.mUpdateListeners.get(i)).onAnimationUpdate(this);
        }
      }
      this.mValues[i].calculateValue(j);
    }
  }
  
  boolean animationFrame(long paramLong)
  {
    boolean bool = false;
    if (this.mPlayingState == 0)
    {
      this.mPlayingState = 1;
      if (this.mSeekTime >= 0L)
      {
        this.mStartTime = (paramLong - this.mSeekTime);
        this.mSeekTime = -1L;
      }
      else
      {
        this.mStartTime = paramLong;
      }
    }
    float f;
    int i;
    switch (this.mPlayingState)
    {
    case 1: 
    case 2: 
      if (this.mDuration <= 0L) {
        f = 1.0F;
      } else {
        f = (float)(paramLong - this.mStartTime) / (float)this.mDuration;
      }
      if (f >= 1.0F) {
        if ((this.mCurrentIteration >= this.mRepeatCount) && (this.mRepeatCount != -1))
        {
          bool = true;
          f = Math.min(f, 1.0F);
        }
        else if (this.mListeners != null)
        {
          i = this.mListeners.size();
        }
      }
      break;
    }
    for (int j = 0;; j++)
    {
      if (j >= i)
      {
        if (this.mRepeatMode == 2)
        {
          if (!this.mPlayingBackwards) {
            i = 1;
          } else {
            i = 0;
          }
          this.mPlayingBackwards = i;
        }
        this.mCurrentIteration += (int)f;
        f %= 1.0F;
        this.mStartTime += this.mDuration;
        if (this.mPlayingBackwards) {
          f = 1.0F - f;
        }
        animateValue(f);
        return bool;
      }
      ((Animator.AnimatorListener)this.mListeners.get(j)).onAnimationRepeat(this);
    }
  }
  
  public void cancel()
  {
    Iterator localIterator;
    if ((this.mPlayingState != 0) || (((ArrayList)sPendingAnimations.get()).contains(this)) || (((ArrayList)sDelayedAnims.get()).contains(this))) {
      if ((this.mRunning) && (this.mListeners != null)) {
        localIterator = ((ArrayList)this.mListeners.clone()).iterator();
      }
    }
    for (;;)
    {
      if (!localIterator.hasNext())
      {
        endAnimation();
        return;
      }
      ((Animator.AnimatorListener)localIterator.next()).onAnimationCancel(this);
    }
  }
  
  public ValueAnimator clone()
  {
    ValueAnimator localValueAnimator = (ValueAnimator)super.clone();
    int k;
    if (this.mUpdateListeners != null)
    {
      ArrayList localArrayList = this.mUpdateListeners;
      localValueAnimator.mUpdateListeners = new ArrayList();
      k = localArrayList.size();
    }
    for (int i = 0;; i++)
    {
      int j;
      if (i >= k)
      {
        localValueAnimator.mSeekTime = -1L;
        localValueAnimator.mPlayingBackwards = false;
        localValueAnimator.mCurrentIteration = 0;
        localValueAnimator.mInitialized = false;
        localValueAnimator.mPlayingState = 0;
        localValueAnimator.mStartedDelay = false;
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = this.mValues;
        if (arrayOfPropertyValuesHolder != null)
        {
          j = arrayOfPropertyValuesHolder.length;
          localValueAnimator.mValues = new PropertyValuesHolder[j];
          localValueAnimator.mValuesMap = new HashMap(j);
        }
        for (i = 0;; i++)
        {
          if (i >= j) {
            return localValueAnimator;
          }
          PropertyValuesHolder localPropertyValuesHolder = arrayOfPropertyValuesHolder[i].clone();
          localValueAnimator.mValues[i] = localPropertyValuesHolder;
          localValueAnimator.mValuesMap.put(localPropertyValuesHolder.getPropertyName(), localPropertyValuesHolder);
        }
      }
      localValueAnimator.mUpdateListeners.add(j.get(i));
    }
  }
  
  public void end()
  {
    if ((((ArrayList)sAnimations.get()).contains(this)) || (((ArrayList)sPendingAnimations.get()).contains(this)))
    {
      if (!this.mInitialized) {
        initAnimation();
      }
    }
    else
    {
      this.mStartedDelay = false;
      startAnimation();
    }
    if ((this.mRepeatCount <= 0) || ((0x1 & this.mRepeatCount) != 1)) {
      animateValue(1.0F);
    } else {
      animateValue(0.0F);
    }
    endAnimation();
  }
  
  public float getAnimatedFraction()
  {
    return this.mCurrentFraction;
  }
  
  public Object getAnimatedValue()
  {
    Object localObject;
    if ((this.mValues == null) || (this.mValues.length <= 0)) {
      localObject = null;
    } else {
      localObject = this.mValues[0].getAnimatedValue();
    }
    return localObject;
  }
  
  public Object getAnimatedValue(String paramString)
  {
    Object localObject = (PropertyValuesHolder)this.mValuesMap.get(paramString);
    if (localObject == null) {
      localObject = null;
    } else {
      localObject = ((PropertyValuesHolder)localObject).getAnimatedValue();
    }
    return localObject;
  }
  
  public long getCurrentPlayTime()
  {
    long l;
    if ((this.mInitialized) && (this.mPlayingState != 0)) {
      l = AnimationUtils.currentAnimationTimeMillis() - this.mStartTime;
    } else {
      l = 0L;
    }
    return l;
  }
  
  public long getDuration()
  {
    return this.mDuration;
  }
  
  public Interpolator getInterpolator()
  {
    return this.mInterpolator;
  }
  
  public int getRepeatCount()
  {
    return this.mRepeatCount;
  }
  
  public int getRepeatMode()
  {
    return this.mRepeatMode;
  }
  
  public long getStartDelay()
  {
    return this.mStartDelay;
  }
  
  public PropertyValuesHolder[] getValues()
  {
    return this.mValues;
  }
  
  void initAnimation()
  {
    int j;
    if (!this.mInitialized) {
      j = this.mValues.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j)
      {
        this.mInitialized = true;
        return;
      }
      this.mValues[i].init();
    }
  }
  
  public boolean isRunning()
  {
    int i = 1;
    if ((this.mPlayingState != i) && (!this.mRunning)) {
      i = 0;
    }
    return i;
  }
  
  public boolean isStarted()
  {
    return this.mStarted;
  }
  
  public void removeAllUpdateListeners()
  {
    if (this.mUpdateListeners != null)
    {
      this.mUpdateListeners.clear();
      this.mUpdateListeners = null;
    }
  }
  
  public void removeUpdateListener(AnimatorUpdateListener paramAnimatorUpdateListener)
  {
    if (this.mUpdateListeners != null)
    {
      this.mUpdateListeners.remove(paramAnimatorUpdateListener);
      if (this.mUpdateListeners.size() == 0) {
        this.mUpdateListeners = null;
      }
    }
  }
  
  public void reverse()
  {
    boolean bool;
    if (this.mPlayingBackwards) {
      bool = false;
    } else {
      bool = true;
    }
    this.mPlayingBackwards = bool;
    if (this.mPlayingState != 1)
    {
      start(true);
    }
    else
    {
      long l1 = AnimationUtils.currentAnimationTimeMillis();
      long l2 = l1 - this.mStartTime;
      this.mStartTime = (l1 - (this.mDuration - l2));
    }
  }
  
  public void setCurrentPlayTime(long paramLong)
  {
    initAnimation();
    long l = AnimationUtils.currentAnimationTimeMillis();
    if (this.mPlayingState != 1)
    {
      this.mSeekTime = paramLong;
      this.mPlayingState = 2;
    }
    this.mStartTime = (l - paramLong);
    animationFrame(l);
  }
  
  public ValueAnimator setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      this.mDuration = paramLong;
      return this;
    }
    throw new IllegalArgumentException("Animators cannot have negative duration: " + paramLong);
  }
  
  public void setEvaluator(TypeEvaluator paramTypeEvaluator)
  {
    if ((paramTypeEvaluator != null) && (this.mValues != null) && (this.mValues.length > 0)) {
      this.mValues[0].setEvaluator(paramTypeEvaluator);
    }
  }
  
  public void setFloatValues(float... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((this.mValues != null) && (this.mValues.length != 0))
      {
        this.mValues[0].setFloatValues(paramVarArgs);
      }
      else
      {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = new PropertyValuesHolder[1];
        arrayOfPropertyValuesHolder[0] = PropertyValuesHolder.ofFloat("", paramVarArgs);
        setValues(arrayOfPropertyValuesHolder);
      }
      this.mInitialized = false;
    }
  }
  
  public void setIntValues(int... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((this.mValues != null) && (this.mValues.length != 0))
      {
        this.mValues[0].setIntValues(paramVarArgs);
      }
      else
      {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = new PropertyValuesHolder[1];
        arrayOfPropertyValuesHolder[0] = PropertyValuesHolder.ofInt("", paramVarArgs);
        setValues(arrayOfPropertyValuesHolder);
      }
      this.mInitialized = false;
    }
  }
  
  public void setInterpolator(Interpolator paramInterpolator)
  {
    if (paramInterpolator == null) {
      this.mInterpolator = new LinearInterpolator();
    } else {
      this.mInterpolator = paramInterpolator;
    }
  }
  
  public void setObjectValues(Object... paramVarArgs)
  {
    if ((paramVarArgs != null) && (paramVarArgs.length != 0))
    {
      if ((this.mValues != null) && (this.mValues.length != 0))
      {
        this.mValues[0].setObjectValues(paramVarArgs);
      }
      else
      {
        PropertyValuesHolder[] arrayOfPropertyValuesHolder = new PropertyValuesHolder[1];
        arrayOfPropertyValuesHolder[0] = PropertyValuesHolder.ofObject("", (TypeEvaluator)null, paramVarArgs);
        setValues(arrayOfPropertyValuesHolder);
      }
      this.mInitialized = false;
    }
  }
  
  public void setRepeatCount(int paramInt)
  {
    this.mRepeatCount = paramInt;
  }
  
  public void setRepeatMode(int paramInt)
  {
    this.mRepeatMode = paramInt;
  }
  
  public void setStartDelay(long paramLong)
  {
    this.mStartDelay = paramLong;
  }
  
  public void setValues(PropertyValuesHolder... paramVarArgs)
  {
    int j = paramVarArgs.length;
    this.mValues = paramVarArgs;
    this.mValuesMap = new HashMap(j);
    for (int i = 0;; i++)
    {
      if (i >= j)
      {
        this.mInitialized = false;
        return;
      }
      PropertyValuesHolder localPropertyValuesHolder = paramVarArgs[i];
      this.mValuesMap.put(localPropertyValuesHolder.getPropertyName(), localPropertyValuesHolder);
    }
  }
  
  public void start()
  {
    start(false);
  }
  
  public String toString()
  {
    String str = "ValueAnimator@" + Integer.toHexString(hashCode());
    if (this.mValues != null) {}
    for (int i = 0;; i++)
    {
      if (i >= this.mValues.length) {
        return str;
      }
      str = str + "\n    " + this.mValues[i].toString();
    }
  }
  
  public static abstract interface AnimatorUpdateListener
  {
    public abstract void onAnimationUpdate(ValueAnimator paramValueAnimator);
  }
  
  private static class AnimationHandler
    extends Handler
  {
    public void handleMessage(Message paramMessage)
    {
      int i = 1;
      ArrayList localArrayList1 = (ArrayList)ValueAnimator.sAnimations.get();
      ArrayList localArrayList2 = (ArrayList)ValueAnimator.sDelayedAnims.get();
      ArrayList localArrayList5;
      switch (paramMessage.what)
      {
      case 0: 
        localArrayList5 = (ArrayList)ValueAnimator.sPendingAnimations.get();
        if ((localArrayList1.size() > 0) || (localArrayList2.size() > 0)) {
          i = 0;
        }
        break;
      }
      for (;;)
      {
        int n;
        if (localArrayList5.size() <= 0)
        {
          long l = AnimationUtils.currentAnimationTimeMillis();
          ArrayList localArrayList4 = (ArrayList)ValueAnimator.sReadyAnims.get();
          localArrayList3 = (ArrayList)ValueAnimator.sEndingAnims.get();
          int i1 = localArrayList2.size();
          for (n = 0;; n++)
          {
            int m;
            if (n >= i1)
            {
              n = localArrayList4.size();
              if (n > 0) {}
              ValueAnimator localValueAnimator2;
              for (i1 = 0;; localValueAnimator2++)
              {
                if (i1 >= n)
                {
                  localArrayList4.clear();
                  m = localArrayList1.size();
                  n = 0;
                  for (;;)
                  {
                    if (n >= m)
                    {
                      if (localArrayList3.size() > 0) {}
                      for (m = 0;; m++)
                      {
                        if (m >= localArrayList3.size())
                        {
                          localArrayList3.clear();
                          if ((i != 0) && ((!localArrayList1.isEmpty()) || (!localArrayList2.isEmpty()))) {
                            sendEmptyMessageDelayed(1, Math.max(0L, ValueAnimator.sFrameDelay - (AnimationUtils.currentAnimationTimeMillis() - l)));
                          }
                          return;
                        }
                        ((ValueAnimator)localArrayList3.get(m)).endAnimation();
                      }
                    }
                    localValueAnimator2 = (ValueAnimator)localArrayList1.get(n);
                    if (localValueAnimator2.animationFrame(l)) {
                      localArrayList3.add(localValueAnimator2);
                    }
                    if (localArrayList1.size() != m)
                    {
                      m--;
                      localArrayList3.remove(localValueAnimator2);
                    }
                    else
                    {
                      n++;
                    }
                  }
                }
                localValueAnimator3 = (ValueAnimator)m.get(localValueAnimator2);
                localValueAnimator3.startAnimation();
                ValueAnimator.access$802(localValueAnimator3, true);
                localArrayList2.remove(localValueAnimator3);
              }
            }
            ValueAnimator localValueAnimator3 = (ValueAnimator)localArrayList2.get(n);
            if (localValueAnimator3.delayedAnimationFrame(l)) {
              m.add(localValueAnimator3);
            }
          }
        }
        ArrayList localArrayList3 = (ArrayList)n.clone();
        n.clear();
        int j = localArrayList3.size();
        for (int k = 0; k < j; k++)
        {
          ValueAnimator localValueAnimator1 = (ValueAnimator)localArrayList3.get(k);
          if (localValueAnimator1.mStartDelay != 0L) {
            localArrayList2.add(localValueAnimator1);
          } else {
            localValueAnimator1.startAnimation();
          }
        }
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.animation.ValueAnimator
 * JD-Core Version:    0.7.0.1
 */