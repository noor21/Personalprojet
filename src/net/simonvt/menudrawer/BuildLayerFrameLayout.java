package net.simonvt.menudrawer;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.FrameLayout;

class BuildLayerFrameLayout
  extends FrameLayout
{
  private boolean mAttached;
  private boolean mChanged;
  private boolean mFirst = true;
  private boolean mHardwareLayersEnabled = true;
  
  public BuildLayerFrameLayout(Context paramContext)
  {
    super(paramContext);
    if (MenuDrawer.USE_TRANSLATIONS) {
      setLayerType(2, null);
    }
  }
  
  public BuildLayerFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    if (MenuDrawer.USE_TRANSLATIONS) {
      setLayerType(2, null);
    }
  }
  
  public BuildLayerFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    if (MenuDrawer.USE_TRANSLATIONS) {
      setLayerType(2, null);
    }
  }
  
  protected void dispatchDraw(Canvas paramCanvas)
  {
    super.dispatchDraw(paramCanvas);
    if ((this.mChanged) && (MenuDrawer.USE_TRANSLATIONS))
    {
      post(new Runnable()
      {
        public void run()
        {
          if ((BuildLayerFrameLayout.this.mAttached) && ((BuildLayerFrameLayout.this.getLayerType() != 2) || (BuildLayerFrameLayout.this.mFirst)))
          {
            BuildLayerFrameLayout.this.mFirst = false;
            BuildLayerFrameLayout.this.setLayerType(2, null);
            BuildLayerFrameLayout.this.buildLayer();
            BuildLayerFrameLayout.this.setLayerType(0, null);
          }
        }
      });
      this.mChanged = false;
    }
  }
  
  protected void onAttachedToWindow()
  {
    super.onAttachedToWindow();
    this.mAttached = true;
  }
  
  protected void onDetachedFromWindow()
  {
    super.onDetachedFromWindow();
    this.mAttached = false;
  }
  
  protected void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if ((MenuDrawer.USE_TRANSLATIONS) && (this.mHardwareLayersEnabled)) {
      post(new Runnable()
      {
        public void run()
        {
          BuildLayerFrameLayout.this.mChanged = true;
          BuildLayerFrameLayout.this.invalidate();
        }
      });
    }
  }
  
  void setHardwareLayersEnabled(boolean paramBoolean)
  {
    this.mHardwareLayersEnabled = paramBoolean;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.BuildLayerFrameLayout
 * JD-Core Version:    0.7.0.1
 */