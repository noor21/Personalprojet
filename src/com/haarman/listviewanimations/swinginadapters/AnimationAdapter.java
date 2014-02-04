package com.haarman.listviewanimations.swinginadapters;

import android.os.Build.VERSION;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import com.haarman.listviewanimations.BaseAdapterDecorator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

public abstract class AnimationAdapter
  extends BaseAdapterDecorator
{
  protected static final long DEFAULTANIMATIONDELAYMILLIS = 100L;
  protected static final long DEFAULTANIMATIONDURATIONMILLIS = 300L;
  private static final long INITIALDELAYMILLIS = 150L;
  private long mAnimationStartMillis = -1L;
  private SparseArray<AnimationInfo> mAnimators = new SparseArray();
  private boolean mHasParentAnimationAdapter;
  private int mLastAnimatedPosition = -1;
  
  public AnimationAdapter(BaseAdapter paramBaseAdapter)
  {
    super(paramBaseAdapter);
    if ((paramBaseAdapter instanceof AnimationAdapter)) {
      ((AnimationAdapter)paramBaseAdapter).setHasParentAnimationAdapter(true);
    }
  }
  
  private void animateView(int paramInt, ViewGroup paramViewGroup, View paramView)
  {
    if (this.mAnimationStartMillis == -1L) {
      this.mAnimationStartMillis = System.currentTimeMillis();
    }
    hideView(paramView);
    Animator[] arrayOfAnimator2;
    if (!(this.mDecoratedBaseAdapter instanceof AnimationAdapter)) {
      arrayOfAnimator2 = new Animator[0];
    } else {
      arrayOfAnimator2 = ((AnimationAdapter)this.mDecoratedBaseAdapter).getAnimators(paramViewGroup, paramView);
    }
    Animator[] arrayOfAnimator1 = getAnimators(paramViewGroup, paramView);
    Object localObject = new float[2];
    localObject[0] = 0.0F;
    localObject[1] = 1.0F;
    localObject = ObjectAnimator.ofFloat(paramView, "alpha", (float[])localObject);
    AnimatorSet localAnimatorSet = new AnimatorSet();
    localAnimatorSet.playTogether(concatAnimators(arrayOfAnimator2, arrayOfAnimator1, (Animator)localObject));
    localAnimatorSet.setStartDelay(calculateAnimationDelay());
    localAnimatorSet.setDuration(getAnimationDurationMillis());
    localAnimatorSet.start();
    this.mAnimators.put(paramView.hashCode(), new AnimationInfo(paramInt, localAnimatorSet));
  }
  
  private void animateViewIfNecessary(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if ((paramInt > this.mLastAnimatedPosition) && (!this.mHasParentAnimationAdapter))
    {
      animateView(paramInt, paramViewGroup, paramView);
      this.mLastAnimatedPosition = paramInt;
    }
  }
  
  private long calculateAnimationDelay()
  {
    long l;
    if (1 + (getAbsListView().getLastVisiblePosition() - getAbsListView().getFirstVisiblePosition()) >= this.mLastAnimatedPosition)
    {
      l = (1 + this.mLastAnimatedPosition) * getAnimationDelayMillis() + (150L + this.mAnimationStartMillis) - System.currentTimeMillis();
    }
    else
    {
      l = getAnimationDelayMillis();
      if (((getAbsListView() instanceof GridView)) && (Build.VERSION.SDK_INT >= 11)) {
        l += getAnimationDelayMillis() * ((1 + this.mLastAnimatedPosition) % ((GridView)getAbsListView()).getNumColumns());
      }
    }
    return Math.max(0L, l);
  }
  
  private Animator[] concatAnimators(Animator[] paramArrayOfAnimator1, Animator[] paramArrayOfAnimator2, Animator paramAnimator)
  {
    Animator[] arrayOfAnimator = new Animator[1 + (paramArrayOfAnimator1.length + paramArrayOfAnimator2.length)];
    for (int i = 0; i < paramArrayOfAnimator2.length; i++) {
      arrayOfAnimator[i] = paramArrayOfAnimator2[i];
    }
    for (int j = 0; j < paramArrayOfAnimator1.length; j++)
    {
      arrayOfAnimator[i] = paramArrayOfAnimator1[j];
      i++;
    }
    arrayOfAnimator[(-1 + arrayOfAnimator.length)] = paramAnimator;
    return arrayOfAnimator;
  }
  
  private void hideView(View paramView)
  {
    Object localObject = new float[1];
    localObject[0] = 0.0F;
    ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(paramView, "alpha", (float[])localObject);
    localObject = new AnimatorSet();
    ((AnimatorSet)localObject).play(localObjectAnimator);
    ((AnimatorSet)localObject).setDuration(0L);
    ((AnimatorSet)localObject).start();
  }
  
  protected abstract long getAnimationDelayMillis();
  
  protected abstract long getAnimationDurationMillis();
  
  public abstract Animator[] getAnimators(ViewGroup paramViewGroup, View paramView);
  
  public final View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = 0;
    if (!this.mHasParentAnimationAdapter)
    {
      if (getAbsListView() == null) {
        break label110;
      }
      if (paramView != null)
      {
        int j = paramView.hashCode();
        localObject = (AnimationInfo)this.mAnimators.get(j);
        if (localObject != null) {
          if (((AnimationInfo)localObject).position == paramInt)
          {
            i = 1;
          }
          else
          {
            ((AnimationInfo)localObject).animator.end();
            this.mAnimators.remove(j);
          }
        }
      }
    }
    Object localObject = super.getView(paramInt, paramView, paramViewGroup);
    if ((!this.mHasParentAnimationAdapter) && (i == 0)) {
      animateViewIfNecessary(paramInt, (View)localObject, paramViewGroup);
    }
    return localObject;
    label110:
    throw new IllegalStateException("Call setListView() on this AnimationAdapter before setAdapter()!");
  }
  
  public void reset()
  {
    this.mAnimators.clear();
    this.mLastAnimatedPosition = -1;
    this.mAnimationStartMillis = -1L;
    if ((getDecoratedBaseAdapter() instanceof AnimationAdapter)) {
      ((AnimationAdapter)getDecoratedBaseAdapter()).reset();
    }
  }
  
  public void setHasParentAnimationAdapter(boolean paramBoolean)
  {
    this.mHasParentAnimationAdapter = paramBoolean;
  }
  
  private class AnimationInfo
  {
    public Animator animator;
    public int position;
    
    public AnimationInfo(int paramInt, Animator paramAnimator)
    {
      this.position = paramInt;
      this.animator = paramAnimator;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.AnimationAdapter
 * JD-Core Version:    0.7.0.1
 */