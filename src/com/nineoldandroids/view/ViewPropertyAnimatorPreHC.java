package com.nineoldandroids.view;

import android.view.View;
import android.view.animation.Interpolator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.nineoldandroids.view.animation.AnimatorProxy;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

class ViewPropertyAnimatorPreHC
  extends ViewPropertyAnimator
{
  private static final int ALPHA = 512;
  private static final int NONE = 0;
  private static final int ROTATION = 16;
  private static final int ROTATION_X = 32;
  private static final int ROTATION_Y = 64;
  private static final int SCALE_X = 4;
  private static final int SCALE_Y = 8;
  private static final int TRANSFORM_MASK = 511;
  private static final int TRANSLATION_X = 1;
  private static final int TRANSLATION_Y = 2;
  private static final int X = 128;
  private static final int Y = 256;
  private Runnable mAnimationStarter = new Runnable()
  {
    public void run()
    {
      ViewPropertyAnimatorPreHC.this.startAnimation();
    }
  };
  private AnimatorEventListener mAnimatorEventListener = new AnimatorEventListener(null);
  private HashMap<Animator, PropertyBundle> mAnimatorMap = new HashMap();
  private long mDuration;
  private boolean mDurationSet = false;
  private Interpolator mInterpolator;
  private boolean mInterpolatorSet = false;
  private Animator.AnimatorListener mListener = null;
  ArrayList<NameValuesHolder> mPendingAnimations = new ArrayList();
  private final AnimatorProxy mProxy;
  private long mStartDelay = 0L;
  private boolean mStartDelaySet = false;
  private final WeakReference<View> mView;
  
  ViewPropertyAnimatorPreHC(View paramView)
  {
    this.mView = new WeakReference(paramView);
    this.mProxy = AnimatorProxy.wrap(paramView);
  }
  
  private void animateProperty(int paramInt, float paramFloat)
  {
    float f = getValue(paramInt);
    animatePropertyBy(paramInt, f, paramFloat - f);
  }
  
  private void animatePropertyBy(int paramInt, float paramFloat)
  {
    animatePropertyBy(paramInt, getValue(paramInt), paramFloat);
  }
  
  private void animatePropertyBy(int paramInt, float paramFloat1, float paramFloat2)
  {
    if (this.mAnimatorMap.size() > 0)
    {
      localObject = null;
      Iterator localIterator = this.mAnimatorMap.keySet().iterator();
      while (localIterator.hasNext())
      {
        Animator localAnimator = (Animator)localIterator.next();
        PropertyBundle localPropertyBundle = (PropertyBundle)this.mAnimatorMap.get(localAnimator);
        if ((localPropertyBundle.cancel(paramInt)) && (localPropertyBundle.mPropertyMask == 0)) {
          localObject = localAnimator;
        }
      }
      if (localObject != null) {
        ((Animator)localObject).cancel();
      }
    }
    Object localObject = new NameValuesHolder(paramInt, paramFloat1, paramFloat2);
    this.mPendingAnimations.add(localObject);
    localObject = (View)this.mView.get();
    if (localObject != null)
    {
      ((View)localObject).removeCallbacks(this.mAnimationStarter);
      ((View)localObject).post(this.mAnimationStarter);
    }
  }
  
  private float getValue(int paramInt)
  {
    float f;
    switch (paramInt)
    {
    default: 
      f = 0.0F;
      break;
    case 1: 
      f = this.mProxy.getTranslationX();
      break;
    case 2: 
      f = this.mProxy.getTranslationY();
      break;
    case 4: 
      f = this.mProxy.getScaleX();
      break;
    case 8: 
      f = this.mProxy.getScaleY();
      break;
    case 16: 
      f = this.mProxy.getRotation();
      break;
    case 32: 
      f = this.mProxy.getRotationX();
      break;
    case 64: 
      f = this.mProxy.getRotationY();
      break;
    case 128: 
      f = this.mProxy.getX();
      break;
    case 256: 
      f = this.mProxy.getY();
      break;
    case 512: 
      f = this.mProxy.getAlpha();
    }
    return f;
  }
  
  private void setValue(int paramInt, float paramFloat)
  {
    switch (paramInt)
    {
    case 1: 
      this.mProxy.setTranslationX(paramFloat);
      break;
    case 2: 
      this.mProxy.setTranslationY(paramFloat);
      break;
    case 4: 
      this.mProxy.setScaleX(paramFloat);
      break;
    case 8: 
      this.mProxy.setScaleY(paramFloat);
      break;
    case 16: 
      this.mProxy.setRotation(paramFloat);
      break;
    case 32: 
      this.mProxy.setRotationX(paramFloat);
      break;
    case 64: 
      this.mProxy.setRotationY(paramFloat);
      break;
    case 128: 
      this.mProxy.setX(paramFloat);
      break;
    case 256: 
      this.mProxy.setY(paramFloat);
      break;
    case 512: 
      this.mProxy.setAlpha(paramFloat);
    }
  }
  
  private void startAnimation()
  {
    float[] arrayOfFloat = new float[1];
    arrayOfFloat[0] = 1.0F;
    ValueAnimator localValueAnimator = ValueAnimator.ofFloat(arrayOfFloat);
    ArrayList localArrayList = (ArrayList)this.mPendingAnimations.clone();
    this.mPendingAnimations.clear();
    int j = 0;
    int i = localArrayList.size();
    for (int k = 0;; k++)
    {
      if (k >= i)
      {
        this.mAnimatorMap.put(localValueAnimator, new PropertyBundle(j, localArrayList));
        localValueAnimator.addUpdateListener(this.mAnimatorEventListener);
        localValueAnimator.addListener(this.mAnimatorEventListener);
        if (this.mStartDelaySet) {
          localValueAnimator.setStartDelay(this.mStartDelay);
        }
        if (this.mDurationSet) {
          localValueAnimator.setDuration(this.mDuration);
        }
        if (this.mInterpolatorSet) {
          localValueAnimator.setInterpolator(this.mInterpolator);
        }
        localValueAnimator.start();
        return;
      }
      j |= ((NameValuesHolder)localArrayList.get(k)).mNameConstant;
    }
  }
  
  public ViewPropertyAnimator alpha(float paramFloat)
  {
    animateProperty(512, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator alphaBy(float paramFloat)
  {
    animatePropertyBy(512, paramFloat);
    return this;
  }
  
  public void cancel()
  {
    Object localObject;
    if (this.mAnimatorMap.size() > 0) {
      localObject = ((HashMap)this.mAnimatorMap.clone()).keySet().iterator();
    }
    for (;;)
    {
      if (!((Iterator)localObject).hasNext())
      {
        this.mPendingAnimations.clear();
        localObject = (View)this.mView.get();
        if (localObject != null) {
          ((View)localObject).removeCallbacks(this.mAnimationStarter);
        }
        return;
      }
      ((Animator)((Iterator)localObject).next()).cancel();
    }
  }
  
  public long getDuration()
  {
    long l;
    if (!this.mDurationSet) {
      l = new ValueAnimator().getDuration();
    } else {
      l = this.mDuration;
    }
    return l;
  }
  
  public long getStartDelay()
  {
    long l;
    if (!this.mStartDelaySet) {
      l = 0L;
    } else {
      l = this.mStartDelay;
    }
    return l;
  }
  
  public ViewPropertyAnimator rotation(float paramFloat)
  {
    animateProperty(16, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationBy(float paramFloat)
  {
    animatePropertyBy(16, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationX(float paramFloat)
  {
    animateProperty(32, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationXBy(float paramFloat)
  {
    animatePropertyBy(32, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationY(float paramFloat)
  {
    animateProperty(64, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator rotationYBy(float paramFloat)
  {
    animatePropertyBy(64, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleX(float paramFloat)
  {
    animateProperty(4, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleXBy(float paramFloat)
  {
    animatePropertyBy(4, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleY(float paramFloat)
  {
    animateProperty(8, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator scaleYBy(float paramFloat)
  {
    animatePropertyBy(8, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator setDuration(long paramLong)
  {
    if (paramLong >= 0L)
    {
      this.mDurationSet = true;
      this.mDuration = paramLong;
      return this;
    }
    throw new IllegalArgumentException("Animators cannot have negative duration: " + paramLong);
  }
  
  public ViewPropertyAnimator setInterpolator(Interpolator paramInterpolator)
  {
    this.mInterpolatorSet = true;
    this.mInterpolator = paramInterpolator;
    return this;
  }
  
  public ViewPropertyAnimator setListener(Animator.AnimatorListener paramAnimatorListener)
  {
    this.mListener = paramAnimatorListener;
    return this;
  }
  
  public ViewPropertyAnimator setStartDelay(long paramLong)
  {
    if (paramLong >= 0L)
    {
      this.mStartDelaySet = true;
      this.mStartDelay = paramLong;
      return this;
    }
    throw new IllegalArgumentException("Animators cannot have negative duration: " + paramLong);
  }
  
  public void start()
  {
    startAnimation();
  }
  
  public ViewPropertyAnimator translationX(float paramFloat)
  {
    animateProperty(1, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationXBy(float paramFloat)
  {
    animatePropertyBy(1, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationY(float paramFloat)
  {
    animateProperty(2, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator translationYBy(float paramFloat)
  {
    animatePropertyBy(2, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator x(float paramFloat)
  {
    animateProperty(128, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator xBy(float paramFloat)
  {
    animatePropertyBy(128, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator y(float paramFloat)
  {
    animateProperty(256, paramFloat);
    return this;
  }
  
  public ViewPropertyAnimator yBy(float paramFloat)
  {
    animatePropertyBy(256, paramFloat);
    return this;
  }
  
  private class AnimatorEventListener
    implements Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener
  {
    private AnimatorEventListener() {}
    
    public void onAnimationCancel(Animator paramAnimator)
    {
      if (ViewPropertyAnimatorPreHC.this.mListener != null) {
        ViewPropertyAnimatorPreHC.this.mListener.onAnimationCancel(paramAnimator);
      }
    }
    
    public void onAnimationEnd(Animator paramAnimator)
    {
      if (ViewPropertyAnimatorPreHC.this.mListener != null) {
        ViewPropertyAnimatorPreHC.this.mListener.onAnimationEnd(paramAnimator);
      }
      ViewPropertyAnimatorPreHC.this.mAnimatorMap.remove(paramAnimator);
      if (ViewPropertyAnimatorPreHC.this.mAnimatorMap.isEmpty()) {
        ViewPropertyAnimatorPreHC.access$202(ViewPropertyAnimatorPreHC.this, null);
      }
    }
    
    public void onAnimationRepeat(Animator paramAnimator)
    {
      if (ViewPropertyAnimatorPreHC.this.mListener != null) {
        ViewPropertyAnimatorPreHC.this.mListener.onAnimationRepeat(paramAnimator);
      }
    }
    
    public void onAnimationStart(Animator paramAnimator)
    {
      if (ViewPropertyAnimatorPreHC.this.mListener != null) {
        ViewPropertyAnimatorPreHC.this.mListener.onAnimationStart(paramAnimator);
      }
    }
    
    public void onAnimationUpdate(ValueAnimator paramValueAnimator)
    {
      float f1 = paramValueAnimator.getAnimatedFraction();
      ViewPropertyAnimatorPreHC.PropertyBundle localPropertyBundle = (ViewPropertyAnimatorPreHC.PropertyBundle)ViewPropertyAnimatorPreHC.this.mAnimatorMap.get(paramValueAnimator);
      if ((0x1FF & localPropertyBundle.mPropertyMask) != 0)
      {
        View localView2 = (View)ViewPropertyAnimatorPreHC.this.mView.get();
        if (localView2 != null) {
          localView2.invalidate();
        }
      }
      ArrayList localArrayList = localPropertyBundle.mNameValuesHolder;
      int i;
      if (localArrayList != null) {
        i = localArrayList.size();
      }
      for (int j = 0;; j++)
      {
        View localView1;
        if (j >= i)
        {
          localView1 = (View)ViewPropertyAnimatorPreHC.this.mView.get();
          if (localView1 != null) {
            localView1.invalidate();
          }
          return;
        }
        ViewPropertyAnimatorPreHC.NameValuesHolder localNameValuesHolder = (ViewPropertyAnimatorPreHC.NameValuesHolder)localArrayList.get(j);
        float f2 = localNameValuesHolder.mFromValue + localView1 * localNameValuesHolder.mDeltaValue;
        ViewPropertyAnimatorPreHC.this.setValue(localNameValuesHolder.mNameConstant, f2);
      }
    }
  }
  
  private static class NameValuesHolder
  {
    float mDeltaValue;
    float mFromValue;
    int mNameConstant;
    
    NameValuesHolder(int paramInt, float paramFloat1, float paramFloat2)
    {
      this.mNameConstant = paramInt;
      this.mFromValue = paramFloat1;
      this.mDeltaValue = paramFloat2;
    }
  }
  
  private static class PropertyBundle
  {
    ArrayList<ViewPropertyAnimatorPreHC.NameValuesHolder> mNameValuesHolder;
    int mPropertyMask;
    
    PropertyBundle(int paramInt, ArrayList<ViewPropertyAnimatorPreHC.NameValuesHolder> paramArrayList)
    {
      this.mPropertyMask = paramInt;
      this.mNameValuesHolder = paramArrayList;
    }
    
    boolean cancel(int paramInt)
    {
      if (((paramInt & this.mPropertyMask) != 0) && (this.mNameValuesHolder != null)) {
        i = this.mNameValuesHolder.size();
      }
      for (int j = 0;; j++)
      {
        if (j >= i) {
          return 0;
        }
        if (((ViewPropertyAnimatorPreHC.NameValuesHolder)this.mNameValuesHolder.get(j)).mNameConstant == paramInt) {
          break;
        }
      }
      this.mNameValuesHolder.remove(j);
      this.mPropertyMask &= (paramInt ^ 0xFFFFFFFF);
      int i = 1;
      return i;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.nineoldandroids.view.ViewPropertyAnimatorPreHC
 * JD-Core Version:    0.7.0.1
 */