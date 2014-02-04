package net.simonvt.menudrawer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;

public class StaticDrawer
  extends MenuDrawer
{
  public StaticDrawer(Context paramContext)
  {
    super(paramContext);
  }
  
  public StaticDrawer(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public StaticDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public void closeMenu(boolean paramBoolean) {}
  
  protected void drawOverlay(Canvas paramCanvas) {}
  
  public boolean getOffsetMenuEnabled()
  {
    return false;
  }
  
  public int getTouchBezelSize()
  {
    return 0;
  }
  
  public int getTouchMode()
  {
    return 0;
  }
  
  protected void initDrawer(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super.initDrawer(paramContext, paramAttributeSet, paramInt);
    super.addView(this.mMenuContainer, -1, new ViewGroup.LayoutParams(-1, -1));
    super.addView(this.mContentContainer, -1, new ViewGroup.LayoutParams(-1, -1));
    this.mIsStatic = true;
  }
  
  public boolean isDrawerIndicatorEnabled()
  {
    return false;
  }
  
  public boolean isMenuVisible()
  {
    return true;
  }
  
  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    int j = paramInt3 - paramInt1;
    int i = paramInt4 - paramInt2;
    switch (this.mPosition)
    {
    case BOTTOM: 
      this.mMenuContainer.layout(0, 0, this.mMenuSize, i);
      this.mContentContainer.layout(this.mMenuSize, 0, j, i);
      break;
    case LEFT: 
      this.mMenuContainer.layout(0, 0, j, this.mMenuSize);
      this.mContentContainer.layout(0, this.mMenuSize, j, i);
      break;
    case RIGHT: 
      this.mMenuContainer.layout(j - this.mMenuSize, 0, j, i);
      this.mContentContainer.layout(0, 0, j - this.mMenuSize, i);
      break;
    case TOP: 
      this.mMenuContainer.layout(0, i - this.mMenuSize, j, i);
      this.mContentContainer.layout(0, 0, j, i - this.mMenuSize);
    }
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    int i = View.MeasureSpec.getMode(paramInt1);
    int j = View.MeasureSpec.getMode(paramInt2);
    if ((i == 1073741824) && (j == 1073741824))
    {
      j = View.MeasureSpec.getSize(paramInt1);
      i = View.MeasureSpec.getSize(paramInt2);
      int k;
      int n;
      int m;
      switch (this.mPosition)
      {
      case BOTTOM: 
      case RIGHT: 
        k = View.MeasureSpec.makeMeasureSpec(i, 1073741824);
        n = this.mMenuSize;
        m = View.MeasureSpec.makeMeasureSpec(n, 1073741824);
        n = View.MeasureSpec.makeMeasureSpec(j - n, 1073741824);
        this.mContentContainer.measure(n, k);
        this.mMenuContainer.measure(m, k);
        break;
      case LEFT: 
      case TOP: 
        k = View.MeasureSpec.makeMeasureSpec(j, 1073741824);
        n = this.mMenuSize;
        m = View.MeasureSpec.makeMeasureSpec(n, 1073741824);
        n = View.MeasureSpec.makeMeasureSpec(i - n, 1073741824);
        this.mContentContainer.measure(k, n);
        this.mMenuContainer.measure(k, m);
      }
      setMeasuredDimension(j, i);
      return;
    }
    throw new IllegalStateException("Must measure with an exact size");
  }
  
  public void openMenu(boolean paramBoolean) {}
  
  public void peekDrawer() {}
  
  public void peekDrawer(long paramLong) {}
  
  public void peekDrawer(long paramLong1, long paramLong2) {}
  
  public void setDrawerIndicatorEnabled(boolean paramBoolean) {}
  
  public void setHardwareLayerEnabled(boolean paramBoolean) {}
  
  public void setMenuSize(int paramInt)
  {
    this.mMenuSize = paramInt;
    requestLayout();
    invalidate();
  }
  
  public void setOffsetMenuEnabled(boolean paramBoolean) {}
  
  public void setSlideDrawable(int paramInt) {}
  
  public void setSlideDrawable(Drawable paramDrawable) {}
  
  public void setTouchBezelSize(int paramInt) {}
  
  public void setTouchMode(int paramInt) {}
  
  public void setupUpIndicator(Activity paramActivity) {}
  
  public void toggleMenu(boolean paramBoolean) {}
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.StaticDrawer
 * JD-Core Version:    0.7.0.1
 */