package net.simonvt.menudrawer.compat;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.lang.reflect.Method;

final class ActionBarHelperSherlock
{
  private static final String TAG = "ActionBarHelperSherlock";
  
  public static Object getIndicatorInfo(Activity paramActivity)
  {
    return new SetIndicatorInfo(paramActivity);
  }
  
  public static Drawable getThemeUpIndicator(Object paramObject)
  {
    Object localObject = (SetIndicatorInfo)paramObject;
    if (((SetIndicatorInfo)localObject).mUpIndicatorView == null) {
      localObject = null;
    } else {
      localObject = ((SetIndicatorInfo)localObject).mUpIndicatorView.getDrawable();
    }
    return localObject;
  }
  
  public static void setActionBarDescription(Object paramObject, Activity paramActivity, int paramInt)
  {
    SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)paramObject;
    if (localSetIndicatorInfo.mUpIndicatorView != null)
    {
      String str;
      if (paramInt != 0) {
        str = paramActivity.getString(paramInt);
      } else {
        str = null;
      }
      localSetIndicatorInfo.mUpIndicatorView.setContentDescription(str);
    }
  }
  
  public static void setActionBarUpIndicator(Object paramObject, Activity paramActivity, Drawable paramDrawable, int paramInt)
  {
    SetIndicatorInfo localSetIndicatorInfo = (SetIndicatorInfo)paramObject;
    if (localSetIndicatorInfo.mUpIndicatorView != null)
    {
      localSetIndicatorInfo.mUpIndicatorView.setImageDrawable(paramDrawable);
      String str;
      if (paramInt != 0) {
        str = paramActivity.getString(paramInt);
      } else {
        str = null;
      }
      localSetIndicatorInfo.mUpIndicatorView.setContentDescription(str);
    }
  }
  
  public static void setDisplayHomeAsUpEnabled(Object paramObject, boolean paramBoolean)
  {
    Object localObject = (SetIndicatorInfo)paramObject;
    if (((SetIndicatorInfo)localObject).mHomeAsUpEnabled != null) {}
    try
    {
      Method localMethod = ((SetIndicatorInfo)localObject).mHomeAsUpEnabled;
      localObject = ((SetIndicatorInfo)localObject).mActionBar;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Boolean.valueOf(paramBoolean);
      localMethod.invoke(localObject, arrayOfObject);
      label44:
      return;
    }
    catch (Throwable localThrowable)
    {
      break label44;
    }
  }
  
  private static class SetIndicatorInfo
  {
    public Object mActionBar;
    public Method mHomeAsUpEnabled;
    public ImageView mUpIndicatorView;
    
    SetIndicatorInfo(Activity paramActivity)
    {
      try
      {
        Object localObject = paramActivity.getPackageName();
        this.mUpIndicatorView = ((ImageView)((ViewGroup)paramActivity.findViewById(paramActivity.getResources().getIdentifier("abs__home", "id", (String)localObject)).getParent()).findViewById(paramActivity.getResources().getIdentifier("abs__up", "id", (String)localObject)));
        this.mActionBar = paramActivity.getClass().getMethod("getSupportActionBar", new Class[0]).invoke(paramActivity, null);
        Class localClass = this.mActionBar.getClass();
        localObject = new Class[1];
        localObject[0] = Boolean.TYPE;
        this.mHomeAsUpEnabled = localClass.getMethod("setDisplayHomeAsUpEnabled", (Class[])localObject);
        label105:
        return;
      }
      catch (Throwable localThrowable)
      {
        break label105;
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.compat.ActionBarHelperSherlock
 * JD-Core Version:    0.7.0.1
 */