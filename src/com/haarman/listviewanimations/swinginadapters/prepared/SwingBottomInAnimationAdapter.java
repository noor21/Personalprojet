package com.haarman.listviewanimations.swinginadapters.prepared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.swinginadapters.SingleAnimationAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;

public class SwingBottomInAnimationAdapter
  extends SingleAnimationAdapter
{
  private final long mAnimationDelayMillis;
  private final long mAnimationDurationMillis;
  
  public SwingBottomInAnimationAdapter(BaseAdapter paramBaseAdapter)
  {
    this(paramBaseAdapter, 100L, 300L);
  }
  
  public SwingBottomInAnimationAdapter(BaseAdapter paramBaseAdapter, long paramLong)
  {
    this(paramBaseAdapter, paramLong, 300L);
  }
  
  public SwingBottomInAnimationAdapter(BaseAdapter paramBaseAdapter, long paramLong1, long paramLong2)
  {
    super(paramBaseAdapter);
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
  
  protected Animator getAnimator(ViewGroup paramViewGroup, View paramView)
  {
    float[] arrayOfFloat = new float[2];
    arrayOfFloat[0] = 500.0F;
    arrayOfFloat[1] = 0.0F;
    return ObjectAnimator.ofFloat(paramView, "translationY", arrayOfFloat);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter
 * JD-Core Version:    0.7.0.1
 */