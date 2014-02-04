package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;

public class ViewGroupCompat
{
  static final ViewGroupCompatImpl IMPL;
  
  static
  {
    int i = Build.VERSION.SDK_INT;
    if (i < 14)
    {
      if (i < 11) {
        IMPL = new ViewGroupCompatStubImpl();
      } else {
        IMPL = new ViewGroupCompatHCImpl();
      }
    }
    else {
      IMPL = new ViewGroupCompatIcsImpl();
    }
  }
  
  public static boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
  {
    return IMPL.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent);
  }
  
  public static void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean)
  {
    IMPL.setMotionEventSplittingEnabled(paramViewGroup, paramBoolean);
  }
  
  static class ViewGroupCompatIcsImpl
    extends ViewGroupCompat.ViewGroupCompatHCImpl
  {
    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return ViewGroupCompatIcs.onRequestSendAccessibilityEvent(paramViewGroup, paramView, paramAccessibilityEvent);
    }
  }
  
  static class ViewGroupCompatHCImpl
    extends ViewGroupCompat.ViewGroupCompatStubImpl
  {
    public void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean)
    {
      ViewGroupCompatHC.setMotionEventSplittingEnabled(paramViewGroup, paramBoolean);
    }
  }
  
  static class ViewGroupCompatStubImpl
    implements ViewGroupCompat.ViewGroupCompatImpl
  {
    public boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent)
    {
      return true;
    }
    
    public void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean) {}
  }
  
  static abstract interface ViewGroupCompatImpl
  {
    public abstract boolean onRequestSendAccessibilityEvent(ViewGroup paramViewGroup, View paramView, AccessibilityEvent paramAccessibilityEvent);
    
    public abstract void setMotionEventSplittingEnabled(ViewGroup paramViewGroup, boolean paramBoolean);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.ViewGroupCompat
 * JD-Core Version:    0.7.0.1
 */