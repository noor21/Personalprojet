package com.haarman.listviewanimations.swinginadapters.prepared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nineoldandroids.animation.Animator;

public class AlphaInAnimationAdapter
  extends AnimationAdapter
{
  public AlphaInAnimationAdapter(BaseAdapter paramBaseAdapter)
  {
    super(paramBaseAdapter);
  }
  
  protected long getAnimationDelayMillis()
  {
    return 100L;
  }
  
  protected long getAnimationDurationMillis()
  {
    return 300L;
  }
  
  public Animator[] getAnimators(ViewGroup paramViewGroup, View paramView)
  {
    return new Animator[0];
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.swinginadapters.prepared.AlphaInAnimationAdapter
 * JD-Core Version:    0.7.0.1
 */