package net.simonvt.menudrawer.compat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

final class ActionBarHelperNative
{
  private static final String TAG = "ActionBarHelperNative";
  private static final int[] THEME_ATTRS;
  
  static
  {
    int[] arrayOfInt = new int[1];
    arrayOfInt[0] = 16843531;
    THEME_ATTRS = arrayOfInt;
  }
  
  public static Object getIndicatorInfo(Activity paramActivity)
  {
    return new SetIndicatorInfo(paramActivity);
  }
  
  public static Drawable getThemeUpIndicator(Object paramObject, Activity paramActivity)
  {
    TypedArray localTypedArray = paramActivity.obtainStyledAttributes(THEME_ATTRS);
    Drawable localDrawable = localTypedArray.getDrawable(0);
    localTypedArray.recycle();
    return localDrawable;
  }
  
  public static void setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
  {
    Object localObject = (SetIndicatorInfo)paramObject;
    if (((SetIndicatorInfo)localObject).setHomeAsUpIndicator != null) {}
    try
    {
      ActionBar localActionBar = paramActivity.getActionBar();
      Method localMethod = ((SetIndicatorInfo)localObject).setHomeActionContentDescription;
      localObject = new Object[1];
      localObject[0] = Integer.valueOf(paramInt);
      localMethod.invoke(localActionBar, (Object[])localObject);
      label49:
      return;
    }
    catch (Throwable localThrowable)
    {
      break label49;
    }
  }
  
  public static void setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
  {
    Object localObject = (SetIndicatorInfo)paramObject;
    if (((SetIndicatorInfo)localObject).setHomeAsUpIndicator != null) {}
    try
    {
      ActionBar localActionBar = paramActivity.getActionBar();
      Method localMethod = ((SetIndicatorInfo)localObject).setHomeAsUpIndicator;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = paramDrawable;
      localMethod.invoke(localActionBar, arrayOfObject);
      localObject = ((SetIndicatorInfo)localObject).setHomeActionContentDescription;
      arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      ((Method)localObject).invoke(localActionBar, arrayOfObject);
      for (;;)
      {
        label79:
        return;
        if (((SetIndicatorInfo)localObject).upIndicatorView != null) {
          ((SetIndicatorInfo)localObject).upIndicatorView.setImageDrawable(paramDrawable);
        }
      }
    }
    catch (Throwable localThrowable)
    {
      break label79;
    }
  }
  
  public static void setDisplayHomeAsUpEnabled(Activity paramActivity, boolean paramBoolean)
  {
    ActionBar localActionBar = paramActivity.getActionBar();
    if (localActionBar != null) {
      localActionBar.setDisplayHomeAsUpEnabled(paramBoolean);
    }
  }
  
  private static class SetIndicatorInfo
  {
    public Method setHomeActionContentDescription;
    public Method setHomeAsUpIndicator;
    public ImageView upIndicatorView;
    
    SetIndicatorInfo(Activity paramActivity)
    {
      label122:
      for (;;)
      {
        Object localObject2;
        try
        {
          localObject1 = new Class[1];
          localObject1[0] = Drawable.class;
          this.setHomeAsUpIndicator = ActionBar.class.getDeclaredMethod("setHomeAsUpIndicator", (Class[])localObject1);
          localObject1 = new Class[1];
          localObject1[0] = Integer.TYPE;
          this.setHomeActionContentDescription = ActionBar.class.getDeclaredMethod("setHomeActionContentDescription", (Class[])localObject1);
          return;
        }
        catch (Throwable localThrowable)
        {
          localObject1 = paramActivity.findViewById(16908332);
          if (localObject1 == null) {
            continue;
          }
          localObject2 = (ViewGroup)((View)localObject1).getParent();
          if (((ViewGroup)localObject2).getChildCount() != 2) {
            continue;
          }
          localObject1 = ((ViewGroup)localObject2).getChildAt(0);
          localObject2 = ((ViewGroup)localObject2).getChildAt(1);
          if (((View)localObject1).getId() != 16908332) {}
        }
        for (Object localObject1 = localObject2;; localObject1 = localObject1)
        {
          if (!(localObject1 instanceof ImageView)) {
            break label122;
          }
          this.upIndicatorView = ((ImageView)localObject1);
          break;
        }
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.compat.ActionBarHelperNative
 * JD-Core Version:    0.7.0.1
 */