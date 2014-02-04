package com.haarman.listviewanimations.swinginadapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorInflater;

public abstract class ResourceAnimationAdapter<T>
  extends AnimationAdapter
{
  private Context mContext;
  
  public ResourceAnimationAdapter(BaseAdapter paramBaseAdapter, Context paramContext)
  {
    super(paramBaseAdapter);
    this.mContext = paramContext;
  }
  
  protected abstract int getAnimationResourceId();
  
  public Animator[] getAnimators(ViewGroup paramViewGroup, View paramView)
  {
    Animator[] arrayOfAnimator = new Animator[1];
    arrayOfAnimator[0] = AnimatorInflater.loadAnimator(this.mContext, getAnimationResourceId());
    return arrayOfAnimator;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.ResourceAnimationAdapter
 * JD-Core Version:    0.7.0.1
 */