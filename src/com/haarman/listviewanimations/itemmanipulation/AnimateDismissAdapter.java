package com.haarman.listviewanimations.itemmanipulation;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import com.haarman.listviewanimations.BaseAdapterDecorator;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class AnimateDismissAdapter<T>
  extends BaseAdapterDecorator
{
  private OnDismissCallback mCallback;
  
  public AnimateDismissAdapter(BaseAdapter paramBaseAdapter, OnDismissCallback paramOnDismissCallback)
  {
    super(paramBaseAdapter);
    this.mCallback = paramOnDismissCallback;
  }
  
  private Animator createAnimatorForView(final View paramView)
  {
    final ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
    int i = paramView.getHeight();
    Object localObject = new int[2];
    localObject[0] = i;
    localObject[1] = 0;
    localObject = ValueAnimator.ofInt((int[])localObject);
    ((ValueAnimator)localObject).addListener(new Animator.AnimatorListener()
    {
      public void onAnimationCancel(Animator paramAnonymousAnimator) {}
      
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        localLayoutParams.height = 0;
        paramView.setLayoutParams(localLayoutParams);
      }
      
      public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
      
      public void onAnimationStart(Animator paramAnonymousAnimator) {}
    });
    ((ValueAnimator)localObject).addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
    {
      public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
      {
        localLayoutParams.height = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
        paramView.setLayoutParams(localLayoutParams);
      }
    });
    return localObject;
  }
  
  private List<View> getVisibleViewsForPositions(Collection<Integer> paramCollection)
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < getAbsListView().getChildCount(); i++)
    {
      View localView = getAbsListView().getChildAt(i);
      if (paramCollection.contains(Integer.valueOf(getAbsListView().getPositionForView(localView)))) {
        localArrayList.add(localView);
      }
    }
    return localArrayList;
  }
  
  private void invokeCallback(Collection<Integer> paramCollection)
  {
    ArrayList localArrayList = new ArrayList(paramCollection);
    Collections.sort(localArrayList);
    int[] arrayOfInt = new int[localArrayList.size()];
    for (int i = 0; i < localArrayList.size(); i++) {
      arrayOfInt[i] = ((Integer)localArrayList.get(-1 + localArrayList.size() - i)).intValue();
    }
    this.mCallback.onDismiss(getAbsListView(), arrayOfInt);
  }
  
  public void animateDismiss(int paramInt)
  {
    Integer[] arrayOfInteger = new Integer[1];
    arrayOfInteger[0] = Integer.valueOf(paramInt);
    animateDismiss(Arrays.asList(arrayOfInteger));
  }
  
  public void animateDismiss(Collection<Integer> paramCollection)
  {
    final ArrayList localArrayList2 = new ArrayList(paramCollection);
    if (getAbsListView() != null)
    {
      Object localObject = getVisibleViewsForPositions(localArrayList2);
      if (((List)localObject).isEmpty())
      {
        invokeCallback(localArrayList2);
      }
      else
      {
        ArrayList localArrayList1 = new ArrayList();
        localObject = ((List)localObject).iterator();
        while (((Iterator)localObject).hasNext()) {
          localArrayList1.add(createAnimatorForView((View)((Iterator)localObject).next()));
        }
        AnimatorSet localAnimatorSet = new AnimatorSet();
        Animator[] arrayOfAnimator = new Animator[localArrayList1.size()];
        for (int i = 0; i < arrayOfAnimator.length; i++) {
          arrayOfAnimator[i] = ((Animator)localArrayList1.get(i));
        }
        localAnimatorSet.playTogether(arrayOfAnimator);
        localAnimatorSet.addListener(new Animator.AnimatorListener()
        {
          public void onAnimationCancel(Animator paramAnonymousAnimator) {}
          
          public void onAnimationEnd(Animator paramAnonymousAnimator)
          {
            AnimateDismissAdapter.this.invokeCallback(localArrayList2);
          }
          
          public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
          
          public void onAnimationStart(Animator paramAnonymousAnimator) {}
        });
        localAnimatorSet.start();
      }
      return;
    }
    throw new IllegalStateException("Call setListView() on this AnimateDismissAdapter before calling setAdapter()!");
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.AnimateDismissAdapter
 * JD-Core Version:    0.7.0.1
 */