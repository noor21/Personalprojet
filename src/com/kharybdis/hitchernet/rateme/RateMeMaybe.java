package com.kharybdis.hitchernet.rateme;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

public class RateMeMaybe
  implements RateMeMaybeFragment.RMMFragInterface
{
  private static final String TAG = "RateMeMaybe";
  private Activity mActivity;
  private String mDialogMessage;
  private String mDialogTitle;
  private Boolean mHandleCancelAsNeutral = Boolean.valueOf(true);
  private int mIcon;
  private OnRMMUserChoiceListener mListener;
  private int mMinDaysUntilInitialPrompt = 0;
  private int mMinDaysUntilNextPrompt = 0;
  private int mMinLaunchesUntilInitialPrompt = 0;
  private int mMinLaunchesUntilNextPrompt = 0;
  private String mNegativeBtn;
  private String mNeutralBtn;
  private String mPositiveBtn;
  private SharedPreferences mPreferences;
  private Boolean mRunWithoutPlayStore = Boolean.valueOf(false);
  
  public RateMeMaybe(Activity paramActivity)
  {
    this.mActivity = paramActivity;
    this.mPreferences = this.mActivity.getSharedPreferences("rate_me_maybe", 0);
  }
  
  private String getApplicationName()
  {
    Object localObject = this.mActivity.getApplicationContext().getPackageManager();
    try
    {
      localObject = (String)((PackageManager)localObject).getApplicationLabel(((PackageManager)localObject).getApplicationInfo(this.mActivity.getPackageName(), 0));
      return localObject;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        localObject = "(unknown)";
      }
    }
  }
  
  private Boolean isPlayStoreInstalled()
  {
    localObject = this.mActivity.getPackageManager();
    try
    {
      ((PackageManager)localObject).getApplicationInfo("com.android.vending", 0);
      localObject = Boolean.valueOf(true);
      localObject = localObject;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      for (;;)
      {
        localObject = Boolean.valueOf(false);
      }
    }
    return localObject;
  }
  
  public static void resetData(Activity paramActivity)
  {
    paramActivity.getSharedPreferences("rate_me_maybe", 0).edit().clear().commit();
    Log.d("RateMeMaybe", "Cleared RateMeMaybe shared preferences.");
  }
  
  private void showDialog()
  {
    if (this.mActivity.getFragmentManager().findFragmentByTag("rmmFragment") == null)
    {
      RateMeMaybeFragment localRateMeMaybeFragment = new RateMeMaybeFragment();
      localRateMeMaybeFragment.setData(getIcon(), getDialogTitle(), getDialogMessage(), getPositiveBtn(), getNeutralBtn(), getNegativeBtn(), this);
      localRateMeMaybeFragment.show(this.mActivity.getFragmentManager(), "rmmFragment");
    }
  }
  
  public void _handleCancel()
  {
    if (!this.mHandleCancelAsNeutral.booleanValue()) {
      _handleNegativeChoice();
    } else {
      _handleNeutralChoice();
    }
  }
  
  public void _handleNegativeChoice()
  {
    SharedPreferences.Editor localEditor = this.mPreferences.edit();
    localEditor.putBoolean("PREF_DONT_SHOW_AGAIN", true);
    localEditor.commit();
    if (this.mListener != null) {
      this.mListener.handleNegative();
    }
  }
  
  public void _handleNeutralChoice()
  {
    if (this.mListener != null) {
      this.mListener.handleNeutral();
    }
  }
  
  public void _handlePositiveChoice()
  {
    SharedPreferences.Editor localEditor = this.mPreferences.edit();
    localEditor.putBoolean("PREF_DONT_SHOW_AGAIN", true);
    localEditor.commit();
    try
    {
      this.mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + this.mActivity.getPackageName())));
      if (this.mListener != null) {
        this.mListener.handlePositive();
      }
      return;
    }
    catch (ActivityNotFoundException localActivityNotFoundException)
    {
      for (;;)
      {
        Toast.makeText(this.mActivity, "Could not launch Play Store!", 0).show();
      }
    }
  }
  
  public void forceShow()
  {
    showDialog();
  }
  
  public String getDialogMessage()
  {
    String str;
    if (this.mDialogMessage != null) {
      str = this.mDialogMessage.replace("%totalLaunchCount%", String.valueOf(this.mPreferences.getInt("PREF_TOTAL_LAUNCH_COUNT", 0)));
    } else {
      str = "If you like using " + getApplicationName() + ", it would be great" + " if you took a moment to rate it in the Play Store. Thank you!";
    }
    return str;
  }
  
  public String getDialogTitle()
  {
    String str;
    if (this.mDialogTitle != null) {
      str = this.mDialogTitle;
    } else {
      str = "Rate " + getApplicationName();
    }
    return str;
  }
  
  public int getIcon()
  {
    return this.mIcon;
  }
  
  public String getNegativeBtn()
  {
    String str;
    if (this.mNegativeBtn != null) {
      str = this.mNegativeBtn;
    } else {
      str = "Never";
    }
    return str;
  }
  
  public String getNeutralBtn()
  {
    String str;
    if (this.mNeutralBtn != null) {
      str = this.mNeutralBtn;
    } else {
      str = "Not now";
    }
    return str;
  }
  
  public String getPositiveBtn()
  {
    String str;
    if (this.mPositiveBtn != null) {
      str = this.mPositiveBtn;
    } else {
      str = "Rate it";
    }
    return str;
  }
  
  public void run()
  {
    if (!this.mPreferences.getBoolean("PREF_DONT_SHOW_AGAIN", false)) {
      if (!isPlayStoreInstalled().booleanValue())
      {
        Log.d("RateMeMaybe", "No Play Store installed on device.");
        if (!this.mRunWithoutPlayStore.booleanValue()) {}
      }
      else
      {
        SharedPreferences.Editor localEditor = this.mPreferences.edit();
        int i = 1 + this.mPreferences.getInt("PREF_TOTAL_LAUNCH_COUNT", 0);
        localEditor.putInt("PREF_TOTAL_LAUNCH_COUNT", i);
        long l2 = System.currentTimeMillis();
        long l1 = this.mPreferences.getLong("PREF_TIME_OF_ABSOLUTE_FIRST_LAUNCH", 0L);
        if (l1 == 0L)
        {
          l1 = l2;
          localEditor.putLong("PREF_TIME_OF_ABSOLUTE_FIRST_LAUNCH", l1);
        }
        long l3 = this.mPreferences.getLong("PREF_TIME_OF_LAST_PROMPT", 0L);
        int j = 1 + this.mPreferences.getInt("PREF_LAUNCHES_SINCE_LAST_PROMPT", 0);
        localEditor.putInt("PREF_LAUNCHES_SINCE_LAST_PROMPT", j);
        if ((i < this.mMinLaunchesUntilInitialPrompt) || (l2 - l1 < 86400000L * this.mMinDaysUntilInitialPrompt))
        {
          localEditor.commit();
        }
        else if ((l3 != 0L) && ((j < this.mMinLaunchesUntilNextPrompt) || (l2 - l3 < 86400000L * this.mMinDaysUntilNextPrompt)))
        {
          localEditor.commit();
        }
        else
        {
          localEditor.putLong("PREF_TIME_OF_LAST_PROMPT", l2);
          localEditor.putInt("PREF_LAUNCHES_SINCE_LAST_PROMPT", 0);
          localEditor.commit();
          showDialog();
        }
      }
    }
  }
  
  public void setAdditionalListener(OnRMMUserChoiceListener paramOnRMMUserChoiceListener)
  {
    this.mListener = paramOnRMMUserChoiceListener;
  }
  
  public void setDialogMessage(String paramString)
  {
    this.mDialogMessage = paramString;
  }
  
  public void setDialogTitle(String paramString)
  {
    this.mDialogTitle = paramString;
  }
  
  public void setHandleCancelAsNeutral(Boolean paramBoolean)
  {
    this.mHandleCancelAsNeutral = paramBoolean;
  }
  
  public void setIcon(int paramInt)
  {
    this.mIcon = paramInt;
  }
  
  public void setNegativeBtn(String paramString)
  {
    this.mNegativeBtn = paramString;
  }
  
  public void setNeutralBtn(String paramString)
  {
    this.mNeutralBtn = paramString;
  }
  
  public void setPositiveBtn(String paramString)
  {
    this.mPositiveBtn = paramString;
  }
  
  public void setPromptMinimums(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.mMinLaunchesUntilInitialPrompt = paramInt1;
    this.mMinDaysUntilInitialPrompt = paramInt2;
    this.mMinLaunchesUntilNextPrompt = paramInt3;
    this.mMinDaysUntilNextPrompt = paramInt4;
  }
  
  public void setRunWithoutPlayStore(Boolean paramBoolean)
  {
    this.mRunWithoutPlayStore = paramBoolean;
  }
  
  public static abstract interface OnRMMUserChoiceListener
  {
    public abstract void handleNegative();
    
    public abstract void handleNeutral();
    
    public abstract void handlePositive();
  }
  
  static class PREF
  {
    private static final String DONT_SHOW_AGAIN = "PREF_DONT_SHOW_AGAIN";
    public static final String LAUNCHES_SINCE_LAST_PROMPT = "PREF_LAUNCHES_SINCE_LAST_PROMPT";
    public static final String NAME = "rate_me_maybe";
    public static final String TIME_OF_ABSOLUTE_FIRST_LAUNCH = "PREF_TIME_OF_ABSOLUTE_FIRST_LAUNCH";
    public static final String TIME_OF_LAST_PROMPT = "PREF_TIME_OF_LAST_PROMPT";
    public static final String TOTAL_LAUNCH_COUNT = "PREF_TOTAL_LAUNCH_COUNT";
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.rateme.RateMeMaybe
 * JD-Core Version:    0.7.0.1
 */