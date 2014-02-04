package com.github.espiandev.showcaseview.anim;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.AnimatorSet.Builder;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.view.View;

public class AnimationUtils
{
  private static final String ALPHA = "alpha";
  private static final String COORD_X = "x";
  private static final String COORD_Y = "y";
  public static final int DEFAULT_DURATION = 300;
  private static final int INSTANT = 0;
  private static final float INVISIBLE = 0.0F;
  private static final float VISIBLE = 1.0F;
  
  public static ObjectAnimator createFadeInAnimation(Object paramObject, int paramInt, AnimationStartListener paramAnimationStartListener)
  {
    Object localObject = new float[1];
    localObject[0] = 1.0F;
    localObject = ObjectAnimator.ofFloat(paramObject, "alpha", (float[])localObject);
    ((ObjectAnimator)localObject).setDuration(paramInt).addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator) {}
      
      public void onAnimationEnd(Animator paramAnonymousAnimator) {}
      
      public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
      
      public void onAnimationStart(Animator paramAnonymousAnimator)
      {
        AnimationUtils.this.onAnimationStart();
      }
    });
    return localObject;
  }
  
  public static ObjectAnimator createFadeInAnimation(Object paramObject, AnimationStartListener paramAnimationStartListener)
  {
    return createFadeInAnimation(paramObject, 300, paramAnimationStartListener);
  }
  
  public static ObjectAnimator createFadeOutAnimation(Object paramObject, int paramInt, AnimationEndListener paramAnimationEndListener)
  {
    Object localObject = new float[1];
    localObject[0] = 0.0F;
    localObject = ObjectAnimator.ofFloat(paramObject, "alpha", (float[])localObject);
    ((ObjectAnimator)localObject).setDuration(paramInt).addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator) {}
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        AnimationUtils.this.onAnimationEnd();
      }
      
      public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
      
      public void onAnimationStart(Animator paramAnonymousAnimator) {}
    });
    return localObject;
  }
  
  public static ObjectAnimator createFadeOutAnimation(Object paramObject, AnimationEndListener paramAnimationEndListener)
  {
    return createFadeOutAnimation(paramObject, 300, paramAnimationEndListener);
  }
  
  public static AnimatorSet createMovementAnimation(View paramView, float paramFloat1, float paramFloat2)
  {
    Object localObject1 = new float[2];
    localObject1[0] = 0.0F;
    localObject1[1] = 1.0F;
    localObject1 = ObjectAnimator.ofFloat(paramView, "alpha", (float[])localObject1).setDuration(500L);
    Object localObject2 = new float[1];
    localObject2[0] = paramFloat1;
    localObject2 = ObjectAnimator.ofFloat(paramView, "x", (float[])localObject2).setDuration(0L);
    Object localObject3 = new float[1];
    localObject3[0] = paramFloat2;
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "y", (float[])localObject3).setDuration(0L);
    localObject3 = new AnimatorSet();
    ((AnimatorSet)localObject3).play((Animator)localObject2).with(localObjectAnimator).before((Animator)localObject1);
    return localObject3;
  }
  
  public static AnimatorSet createMovementAnimation(View paramView, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, AnimationEndListener paramAnimationEndListener)
  {
    paramView.setAlpha(0.0F);
    Object localObject1 = new float[2];
    localObject1[0] = 0.0F;
    localObject1[1] = 1.0F;
    localObject1 = ObjectAnimator.ofFloat(paramView, "alpha", (float[])localObject1).setDuration(500L);
    Object localObject2 = new float[1];
    localObject2[0] = (paramFloat1 + paramFloat3);
    localObject2 = ObjectAnimator.ofFloat(paramView, "x", (float[])localObject2).setDuration(0L);
    Object localObject3 = new float[1];
    localObject3[0] = (paramFloat2 + paramFloat4);
    localObject3 = ObjectAnimator.ofFloat(paramView, "y", (float[])localObject3).setDuration(0L);
    Object localObject4 = new float[1];
    localObject4[0] = (paramFloat1 + paramFloat5);
    localObject4 = ObjectAnimator.ofFloat(paramView, "x", (float[])localObject4).setDuration(1000L);
    Object localObject5 = new float[1];
    localObject5[0] = (paramFloat2 + paramFloat6);
    localObject5 = ObjectAnimator.ofFloat(paramView, "y", (float[])localObject5).setDuration(1000L);
    ((ObjectAnimator)localObject4).setStartDelay(1000L);
    ((ObjectAnimator)localObject5).setStartDelay(1000L);
    Object localObject6 = new float[1];
    localObject6[0] = 0.0F;
    localObject6 = ObjectAnimator.ofFloat(paramView, "alpha", (float[])localObject6).setDuration(500L);
    ((ObjectAnimator)localObject6).setStartDelay(2500L);
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.play((Animator)localObject2).with((Animator)localObject3).before((Animator)localObject1).before((Animator)localObject4).with((Animator)localObject5).before((Animator)localObject6);
    new Handler().postDelayed(new Runnable()
    {
      public void run()
      {
        AnimationUtils.this.onAnimationEnd();
      }
    }, 3000L);
    return localAnimatorSet;
  }
  
  public static float getX(View paramView)
  {
    return paramView.getX();
  }
  
  public static float getY(View paramView)
  {
    return paramView.getY();
  }
  
  public static void hide(View paramView)
  {
    paramView.setAlpha(0.0F);
  }
  
  public static abstract interface AnimationEndListener
  {
    public abstract void onAnimationEnd();
  }
  
  public static abstract interface AnimationStartListener
  {
    public abstract void onAnimationStart();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.github.espiandev.showcaseview.anim.AnimationUtils
 * JD-Core Version:    0.7.0.1
 */