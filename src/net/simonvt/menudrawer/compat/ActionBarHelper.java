package net.simonvt.menudrawer.compat;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;

public final class ActionBarHelper
{
  private static final String TAG = "ActionBarHelper";
  private Activity mActivity;
  private Object mIndicatorInfo;
  private boolean mUsesSherlock;
  
  public ActionBarHelper(Activity paramActivity)
  {
    this.mActivity = paramActivity;
    try
    {
      paramActivity.getClass().getMethod("getSupportActionBar", new Class[0]);
      this.mUsesSherlock = true;
      label28:
      this.mIndicatorInfo = getIndicatorInfo();
      return;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      break label28;
    }
  }
  
  private Object getIndicatorInfo()
  {
    Object localObject;
    if ((!this.mUsesSherlock) || (Build.VERSION.SDK_INT >= 14))
    {
      if (Build.VERSION.SDK_INT < 11) {
        localObject = null;
      } else {
        localObject = ActionBarHelperNative.getIndicatorInfo(this.mActivity);
      }
    }
    else {
      localObject = ActionBarHelperSherlock.getIndicatorInfo(this.mActivity);
    }
    return localObject;
  }
  
  public Drawable getThemeUpIndicator()
  {
    Drawable localDrawable;
    if ((!this.mUsesSherlock) || (Build.VERSION.SDK_INT >= 14))
    {
      if (Build.VERSION.SDK_INT < 11) {
        localDrawable = null;
      } else {
        localDrawable = ActionBarHelperNative.getThemeUpIndicator(this.mIndicatorInfo, this.mActivity);
      }
    }
    else {
      localDrawable = ActionBarHelperSherlock.getThemeUpIndicator(this.mIndicatorInfo);
    }
    return localDrawable;
  }
  
  public void setActionBarDescription(int paramInt)
  {
    if ((!this.mUsesSherlock) || (Build.VERSION.SDK_INT >= 14))
    {
      if (Build.VERSION.SDK_INT >= 11) {
        ActionBarHelperNative.setActionBarDescription(this.mIndicatorInfo, this.mActivity, paramInt);
      }
    }
    else {
      ActionBarHelperSherlock.setActionBarDescription(this.mIndicatorInfo, this.mActivity, paramInt);
    }
  }
  
  public void setActionBarUpIndicator(Drawable paramDrawable, int paramInt)
  {
    if ((!this.mUsesSherlock) || (Build.VERSION.SDK_INT >= 14))
    {
      if (Build.VERSION.SDK_INT >= 11) {
        ActionBarHelperNative.setActionBarUpIndicator(this.mIndicatorInfo, this.mActivity, paramDrawable, paramInt);
      }
    }
    else {
      ActionBarHelperSherlock.setActionBarUpIndicator(this.mIndicatorInfo, this.mActivity, paramDrawable, paramInt);
    }
  }
  
  public void setDisplayShowHomeAsUpEnabled(boolean paramBoolean)
  {
    if ((!this.mUsesSherlock) || (Build.VERSION.SDK_INT >= 14))
    {
      if (Build.VERSION.SDK_INT >= 11) {
        ActionBarHelperNative.setDisplayHomeAsUpEnabled(this.mActivity, paramBoolean);
      }
    }
    else {
      ActionBarHelperSherlock.setDisplayHomeAsUpEnabled(this.mIndicatorInfo, paramBoolean);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.compat.ActionBarHelper
 * JD-Core Version:    0.7.0.1
 */