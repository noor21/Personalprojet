package com.github.espiandev.showcaseview;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class ShowcaseViews
{
  private final Activity activity;
  private OnShowcaseAcknowledged showcaseAcknowledgedListener = new OnShowcaseAcknowledged()
  {
    public void onShowCaseAcknowledged(ShowcaseView paramAnonymousShowcaseView) {}
  };
  private final int showcaseTemplateId;
  private final List<ShowcaseView> views = new ArrayList();
  
  public ShowcaseViews(Activity paramActivity, int paramInt)
  {
    this.activity = paramActivity;
    this.showcaseTemplateId = paramInt;
  }
  
  public ShowcaseViews(Activity paramActivity, int paramInt, OnShowcaseAcknowledged paramOnShowcaseAcknowledged)
  {
    this(paramActivity, paramInt);
    this.showcaseAcknowledgedListener = paramOnShowcaseAcknowledged;
  }
  
  private View.OnClickListener createShowcaseViewDismissListener(final ShowcaseView paramShowcaseView)
  {
    new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        paramShowcaseView.hide();
        if (!ShowcaseViews.this.views.isEmpty()) {
          ShowcaseViews.this.show();
        } else {
          ShowcaseViews.this.showcaseAcknowledgedListener.onShowCaseAcknowledged(paramShowcaseView);
        }
      }
    };
  }
  
  private boolean showcaseActionBar(ItemViewProperties paramItemViewProperties)
  {
    boolean bool;
    if (paramItemViewProperties.itemType <= -1) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
  
  public void addView(ItemViewProperties paramItemViewProperties)
  {
    Object localObject = new ShowcaseViewBuilder(this.activity).setText(paramItemViewProperties.titleResId, paramItemViewProperties.messageResId).setShowcaseIndicatorScale(paramItemViewProperties.scale);
    if (!showcaseActionBar(paramItemViewProperties))
    {
      if (paramItemViewProperties.id != -2202) {
        ((ShowcaseViewBuilder)localObject).setShowcaseView(this.activity.findViewById(paramItemViewProperties.id));
      } else {
        ((ShowcaseViewBuilder)localObject).setShowcaseNoView();
      }
    }
    else {
      ((ShowcaseViewBuilder)localObject).setShowcaseItem(paramItemViewProperties.itemType, paramItemViewProperties.id, this.activity);
    }
    localObject = ((ShowcaseViewBuilder)localObject).build();
    ((ShowcaseView)localObject).overrideButtonClick(createShowcaseViewDismissListener((ShowcaseView)localObject));
    this.views.add(localObject);
  }
  
  public boolean hasViews()
  {
    boolean bool;
    if (!this.views.isEmpty()) {
      bool = true;
    } else {
      bool = false;
    }
    return bool;
  }
  
  public void show()
  {
    if (!this.views.isEmpty())
    {
      ShowcaseView localShowcaseView = (ShowcaseView)this.views.get(0);
      ((ViewGroup)this.activity.getWindow().getDecorView()).addView(localShowcaseView);
      System.out.println("Removed");
      this.views.remove(0);
    }
  }
  
  public static class ItemViewProperties
  {
    private static final float DEFAULT_SCALE = 1.0F;
    public static final int ID_NOT_IN_ACTIONBAR = -1;
    public static final int ID_NO_SHOWCASE = -2202;
    public static final int ID_OVERFLOW = 2;
    public static final int ID_SPINNER = 0;
    public static final int ID_TITLE = 1;
    protected final int id;
    protected final int itemType;
    protected final int messageResId;
    protected final float scale;
    protected final int titleResId;
    
    public ItemViewProperties(int paramInt1, int paramInt2)
    {
      this(-2202, paramInt1, paramInt2, -1, 1.0F);
    }
    
    public ItemViewProperties(int paramInt1, int paramInt2, int paramInt3)
    {
      this(paramInt1, paramInt2, paramInt3, -1, 1.0F);
    }
    
    public ItemViewProperties(int paramInt1, int paramInt2, int paramInt3, float paramFloat)
    {
      this(paramInt1, paramInt2, paramInt3, -1, paramFloat);
    }
    
    public ItemViewProperties(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
    {
      this(paramInt1, paramInt2, paramInt3, paramInt4, 1.0F);
    }
    
    public ItemViewProperties(int paramInt1, int paramInt2, int paramInt3, int paramInt4, float paramFloat)
    {
      this.id = paramInt1;
      this.titleResId = paramInt2;
      this.messageResId = paramInt3;
      this.itemType = paramInt4;
      this.scale = paramFloat;
    }
  }
  
  public static abstract interface OnShowcaseAcknowledged
  {
    public abstract void onShowCaseAcknowledged(ShowcaseView paramShowcaseView);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.github.espiandev.showcaseview.ShowcaseViews
 * JD-Core Version:    0.7.0.1
 */