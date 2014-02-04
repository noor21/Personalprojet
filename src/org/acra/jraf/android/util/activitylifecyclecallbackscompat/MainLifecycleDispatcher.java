package org.acra.jraf.android.util.activitylifecyclecallbackscompat;

import android.app.Activity;
import android.os.Bundle;
import java.util.ArrayList;

public class MainLifecycleDispatcher
  implements ActivityLifecycleCallbacksCompat
{
  private static final MainLifecycleDispatcher INSTANCE = new MainLifecycleDispatcher();
  private ArrayList<ActivityLifecycleCallbacksCompat> mActivityLifecycleCallbacks = new ArrayList();
  
  private Object[] collectActivityLifecycleCallbacks()
  {
    Object[] arrayOfObject = null;
    synchronized (this.mActivityLifecycleCallbacks)
    {
      if (this.mActivityLifecycleCallbacks.size() > 0) {
        arrayOfObject = this.mActivityLifecycleCallbacks.toArray();
      }
      return arrayOfObject;
    }
  }
  
  public static MainLifecycleDispatcher get()
  {
    return INSTANCE;
  }
  
  public void onActivityCreated(Activity paramActivity, Bundle paramBundle)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityCreated(paramActivity, paramBundle);
    }
  }
  
  public void onActivityDestroyed(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityDestroyed(paramActivity);
    }
  }
  
  public void onActivityPaused(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityPaused(paramActivity);
    }
  }
  
  public void onActivityResumed(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityResumed(paramActivity);
    }
  }
  
  public void onActivitySaveInstanceState(Activity paramActivity, Bundle paramBundle)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivitySaveInstanceState(paramActivity, paramBundle);
    }
  }
  
  public void onActivityStarted(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityStarted(paramActivity);
    }
  }
  
  public void onActivityStopped(Activity paramActivity)
  {
    Object[] arrayOfObject = collectActivityLifecycleCallbacks();
    int j;
    if (arrayOfObject != null) {
      j = arrayOfObject.length;
    }
    for (int i = 0;; i++)
    {
      if (i >= j) {
        return;
      }
      ((ActivityLifecycleCallbacksCompat)arrayOfObject[i]).onActivityStopped(paramActivity);
    }
  }
  
  void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    synchronized (this.mActivityLifecycleCallbacks)
    {
      this.mActivityLifecycleCallbacks.add(paramActivityLifecycleCallbacksCompat);
      return;
    }
  }
  
  void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacksCompat paramActivityLifecycleCallbacksCompat)
  {
    synchronized (this.mActivityLifecycleCallbacks)
    {
      this.mActivityLifecycleCallbacks.remove(paramActivityLifecycleCallbacksCompat);
      return;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.jraf.android.util.activitylifecyclecallbackscompat.MainLifecycleDispatcher
 * JD-Core Version:    0.7.0.1
 */