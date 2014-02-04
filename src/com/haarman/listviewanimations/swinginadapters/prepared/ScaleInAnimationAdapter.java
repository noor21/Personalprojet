package com.haarman.listviewanimations.swinginadapters.prepared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class ScaleInAnimationAdapter
  extends AnimationAdapter
{
  private static final float DEFAULTSCALEFROM = 0.8F;
  private long mAnimationDelayMillis;
  private long mAnimationDurationMillis;
  private float mScaleFrom;
  
  public ScaleInAnimationAdapter(BaseAdapter paramBaseAdapter)
  {
    this(paramBaseAdapter, 0.8F);
  }
  
  public ScaleInAnimationAdapter(BaseAdapter paramBaseAdapter, float paramFloat)
  {
    this(paramBaseAdapter, paramFloat, 100L, 300L);
  }
  
  public ScaleInAnimationAdapter(BaseAdapter paramBaseAdapter, float paramFloat, long paramLong1, long paramLong2)
  {
    super(paramBaseAdapter);
    this.mScaleFrom = paramFloat;
    this.mAnimationDelayMillis = paramLong1;
    this.mAnimationDurationMillis = paramLong2;
  }
  
  protected long getAnimationDelayMillis()
  {
    return this.mAnimationDelayMillis;
  }
  
  protected long getAnimationDurationMillis()
  {
    return this.mAnimationDurationMillis;
  }
  
  public Animator[] getAnimators(ViewGroup paramViewGroup, View paramView)
  {
    Object localObject1 = new float[2];
    localObject1[0] = this.mScaleFrom;
    localObject1[1] = 1.0F;
    localObject1 = ObjectAnimator.ofFloat(paramView, "scaleX", (float[])localObject1);
    Object localObject2 = new float[2];
    localObject2[0] = this.mScaleFrom;
    localObject2[1] = 1.0F;
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "scaleY", (float[])localObject2);
    localObject2 = new ObjectAnimator[2];
    localObject2[0] = localObject1;
    localObject2[1] = localObjectAnimator;
    return localObject2;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.prepared.ScaleInAnimationAdapter
 * JD-Core Version:    0.7.0.1
 */