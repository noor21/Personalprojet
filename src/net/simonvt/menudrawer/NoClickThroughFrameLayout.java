package net.simonvt.menudrawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

class NoClickThroughFrameLayout
  extends BuildLayerFrameLayout
{
  public NoClickThroughFrameLayout(Context paramContext)
  {
    super(paramContext);
  }
  
  public NoClickThroughFrameLayout(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }
  
  public NoClickThroughFrameLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
  }
  
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return true;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.NoClickThroughFrameLayout
 * JD-Core Version:    0.7.0.1
 */