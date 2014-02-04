package com.haarman.listviewanimations.swinginadapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nineoldandroids.animation.Animator;

public abstract class SingleAnimationAdapter
  extends AnimationAdapter
{
  public SingleAnimationAdapter(BaseAdapter paramBaseAdapter)
  {
    super(paramBaseAdapter);
  }
  
  protected abstract Animator getAnimator(ViewGroup paramViewGroup, View paramView);
  
  public Animator[] getAnimators(ViewGroup paramViewGroup, View paramView)
  {
    Animator localAnimator = getAnimator(paramViewGroup, paramView);
    Animator[] arrayOfAnimator = new Animator[1];
    arrayOfAnimator[0] = localAnimator;
    return arrayOfAnimator;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.SingleAnimationAdapter
 * JD-Core Version:    0.7.0.1
 */