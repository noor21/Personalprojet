package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class ActivityLifecycleCallbacksWrapper
  implements Application.ActivityLifecycleCallbacks
{
  private ActivityLifecycleCallbacksCompat mCallback;
  
  public ActivityLifecycleCallbacksWrapper(ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    this.mCallback = paramActivityLifecycleCallbacksCompat;
  }
  
  public void onActivityCreated(Activity paramActivity, Bundle paramBundle)
  {
    this.mCallback.onActivityCreated(paramActivity, paramBundle);
  }
  
  public void onActivityDestroyed(Activity paramActivity)
  {
    this.mCallback.onActivityDestroyed(paramActivity);
  }
  
  public void onActivityPaused(Activity paramActivity)
  {
    this.mCallback.onActivityPaused(paramActivity);
  }
  
  public void onActivityResumed(Activity paramActivity)
  {
    this.mCallback.onActivityResumed(paramActivity);
  }
  
  public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle)
  {
    this.mCallback.onActivitySaveInstanceState(paramActivity, paramBundle);
  }
  
  public void onActivityStarted(Activity paramActivity)
  {
    this.mCallback.onActivityStarted(paramActivity);
  }
  
  public void onActivityStopped(Activity paramActivity)
  {
    this.mCallback.onActivityStopped(paramActivity);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.jraf.android.util.activitylifecyclecallbackscompat.ActivityLifecycleCallbacksWrapper
 * JD-Core Version:    0.7.0.1
 */