package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.annotation.TargetApi;
import android.app.Application;
import android.os.Build.VERSION;

public class ApplicationHelper
{
  public static final boolean PRE_ICS;
  
  static
  {
    boolean bool;
    if (Build.VERSION.SDK_INT >= 14) {
      bool = false;
    } else {
      bool = true;
    }
    PRE_ICS = bool;
  }
  
  @TargetApi(14)
  private static void postIcsRegisterActivityLifecycleCallbacks(Application paramApplication, ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    paramApplication.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(paramActivityLifecycleCallbacksCompat));
  }
  
  @TargetApi(14)
  private static void postIcsUnregisterActivityLifecycleCallbacks(Application paramApplication, ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    paramApplication.unregisterActivityLifecycleCallbacks(new ActivityLifecycleCallbacksWrapper(paramActivityLifecycleCallbacksCompat));
  }
  
  private static void preIcsRegisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    MainLifecycleDispatcher.get().registerActivityLifecycleCallbacks(paramActivityLifecycleCallbacksCompat);
  }
  
  private static void preIcsUnregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    MainLifecycleDispatcher.get().unregisterActivityLifecycleCallbacks(paramActivityLifecycleCallbacksCompat);
  }
  
  public static void registerActivityLifecycleCallbacks(Application paramApplication, ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    if (!PRE_ICS) {
      postIcsRegisterActivityLifecycleCallbacks(paramApplication, paramActivityLifecycleCallbacksCompat);
    } else {
      preIcsRegisterActivityLifecycleCallbacks(paramActivityLifecycleCallbacksCompat);
    }
  }
  
  public void unregisterActivityLifecycleCallbacks(Application paramApplication, ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    if (!PRE_ICS) {
      postIcsUnregisterActivityLifecycleCallbacks(paramApplication, paramActivityLifecycleCallbacksCompat);
    } else {
      preIcsUnregisterActivityLifecycleCallbacks(paramActivityLifecycleCallbacksCompat);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.jraf.android.util.activitylifecyclecallbackscompat.ApplicationHelper
 * JD-Core Version:    0.7.0.1
 */