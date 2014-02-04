package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.app.Activity;
import android.os.Bundle;

public abstract interface ActivityLifecycleCallbacksCompat
{
  public abstract void onActivityCreated(Activity paramActivity, Bundle paramBundle);
  
  public abstract void onActivityDestroyed(Activity paramActivity);
  
  public abstract void onActivityPaused(Activity paramActivity);
  
  public abstract void onActivityResumed(Activity paramActivity);
  
  public abstract void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle);
  
  public abstract void onActivityStarted(Activity paramActivity);
  
  public abstract void onActivityStopped(Activity paramActivity);
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksCompat
 * JD-Core Version:    0.7.0.1
 */