package com.haarman.listviewanimations.itemmanipulation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.haarman.listviewanimations.ArrayAdapter;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import java.util.ArrayList;
import java.util.List;

public abstract class ExpandableListItemAdapter<T>
  extends ArrayAdapter<T>
{
  private static final int DEFAULTCONTENTPARENTRESID = 10001;
  private static final int DEFAULTTITLEPARENTRESID = 10000;
  private int mContentParentResId;
  private Context mContext;
  private int mTitleParentResId;
  private int mViewLayoutResId;
  private List<Long> mVisibleIds;
  
  protected ExpandableListItemAdapter(Context paramContext)
  {
    this(paramContext, null);
  }
  
  protected ExpandableListItemAdapter(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    this(paramContext, paramInt1, paramInt2, paramInt3, null);
  }
  
  protected ExpandableListItemAdapter(Context paramContext, int paramInt1, int paramInt2, int paramInt3, List<T> paramList)
  {
    super(paramList);
    this.mContext = paramContext;
    this.mViewLayoutResId = paramInt1;
    this.mTitleParentResId = paramInt2;
    this.mContentParentResId = paramInt3;
    this.mVisibleIds = new ArrayList();
  }
  
  protected ExpandableListItemAdapter(Context paramContext, List<T> paramList)
  {
    super(paramList);
    this.mContext = paramContext;
    this.mTitleParentResId = 10000;
    this.mContentParentResId = 10001;
    this.mVisibleIds = new ArrayList();
  }
  
  private ViewGroup createView(ViewGroup paramViewGroup)
  {
    Object localObject;
    if (this.mViewLayoutResId != 0) {
      localObject = (ViewGroup)LayoutInflater.from(this.mContext).inflate(this.mViewLayoutResId, paramViewGroup, false);
    } else {
      localObject = new RootView(this.mContext);
    }
    return localObject;
  }
  
  public abstract View getContentView(int paramInt, View paramView, ViewGroup paramViewGroup);
  
  public abstract View getTitleView(int paramInt, View paramView, ViewGroup paramViewGroup);
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    ViewGroup localViewGroup = (ViewGroup)paramView;
    ViewHolder localViewHolder;
    if (localViewGroup != null)
    {
      localViewHolder = (ViewHolder)localViewGroup.getTag();
    }
    else
    {
      localViewGroup = createView(paramViewGroup);
      localViewHolder = new ViewHolder(null);
      localViewHolder.titleParent = ((ViewGroup)localViewGroup.findViewById(this.mTitleParentResId));
      localViewHolder.contentParent = ((ViewGroup)localViewGroup.findViewById(this.mContentParentResId));
      localViewGroup.setOnClickListener(new TitleViewOnClickListener(localViewHolder.contentParent, null));
      localViewGroup.setTag(localViewHolder);
    }
    View localView = getTitleView(paramInt, localViewHolder.titleView, localViewHolder.titleParent);
    if (!localView.equals(localViewHolder.titleView))
    {
      localViewHolder.titleParent.removeAllViews();
      localViewHolder.titleParent.addView(localView);
    }
    localViewHolder.titleView = localView;
    Object localObject = getContentView(paramInt, localViewHolder.contentView, localViewHolder.contentParent);
    if (!localObject.equals(localViewHolder.contentView))
    {
      localViewHolder.contentParent.removeAllViews();
      localViewHolder.contentParent.addView((View)localObject);
    }
    localViewHolder.titleView = localView;
    localObject = localViewHolder.contentParent;
    int i;
    if (!this.mVisibleIds.contains(Long.valueOf(getItemId(paramInt)))) {
      i = 8;
    } else {
      i = 0;
    }
    ((ViewGroup)localObject).setVisibility(i);
    localViewHolder.contentParent.setTag(Long.valueOf(getItemId(paramInt)));
    return localViewGroup;
  }
  
  private static class RootView
    extends LinearLayout
  {
    private ViewGroup mContentViewGroup;
    private ViewGroup mTitleViewGroup;
    
    public RootView(Context paramContext)
    {
      super();
      init();
    }
    
    private void init()
    {
      setOrientation(1);
      this.mTitleViewGroup = new FrameLayout(getContext());
      this.mTitleViewGroup.setId(10000);
      addView(this.mTitleViewGroup);
      this.mContentViewGroup = new FrameLayout(getContext());
      this.mContentViewGroup.setId(10001);
      addView(this.mContentViewGroup);
    }
  }
  
  private class TitleViewOnClickListener
    implements View.OnClickListener
  {
    private View mContentParent;
    
    private TitleViewOnClickListener(View paramView)
    {
      this.mContentParent = paramView;
    }
    
    private void animateCollapsing()
    {
      ValueAnimator localValueAnimator = createHeightAnimator(this.mContentParent.getHeight(), 0);
      localValueAnimator.addListener(new Animator.AnimatorListener()
      {
        public void onAnimationCancel(Animator paramAnonymousAnimator) {}
        
        public void onAnimationEnd(Animator paramAnonymousAnimator)
        {
          ExpandableListItemAdapter.TitleViewOnClickListener.this.mContentParent.setVisibility(8);
        }
        
        public void onAnimationRepeat(Animator paramAnonymousAnimator) {}
        
        public void onAnimationStart(Animator paramAnonymousAnimator) {}
      });
      localValueAnimator.start();
    }
    
    private void animateExpanding()
    {
      this.mContentParent.setVisibility(0);
      int i = View.MeasureSpec.makeMeasureSpec(0, 0);
      int j = View.MeasureSpec.makeMeasureSpec(0, 0);
      this.mContentParent.measure(i, j);
      createHeightAnimator(0, this.mContentParent.getMeasuredHeight()).start();
    }
    
    private ValueAnimator createHeightAnimator(int paramInt1, int paramInt2)
    {
      Object localObject = new int[2];
      localObject[0] = paramInt1;
      localObject[1] = paramInt2;
      localObject = ValueAnimator.ofInt((int[])localObject);
      ((ValueAnimator)localObject).addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
      {
        public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
        {
          int i = ((Integer)paramAnonymousValueAnimator.getAnimatedValue()).intValue();
          ViewGroup.LayoutParams localLayoutParams = ExpandableListItemAdapter.TitleViewOnClickListener.this.mContentParent.getLayoutParams();
          localLayoutParams.height = i;
          ExpandableListItemAdapter.TitleViewOnClickListener.this.mContentParent.setLayoutParams(localLayoutParams);
        }
      });
      return localObject;
    }
    
    public void onClick(View paramView)
    {
      int i;
      if (this.mContentParent.getVisibility() != 0) {
        i = 0;
      } else {
        i = 1;
      }
      if (i == 0)
      {
        animateExpanding();
        ExpandableListItemAdapter.this.mVisibleIds.add((Long)this.mContentParent.getTag());
      }
      else
      {
        animateCollapsing();
        ExpandableListItemAdapter.this.mVisibleIds.remove(this.mContentParent.getTag());
      }
    }
  }
  
  private static class ViewHolder
  {
    ViewGroup contentParent;
    View contentView;
    ViewGroup titleParent;
    View titleView;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.itemmanipulation.ExpandableListItemAdapter
 * JD-Core Version:    0.7.0.1
 */