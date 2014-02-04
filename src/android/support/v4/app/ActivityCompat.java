package android.support.v4.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

public class ActivityCompat
  extends ContextCompat
{
  public static boolean invalidateOptionsMenu(Activity paramActivity)
  {
    boolean bool;
    if (Build.VERSION.SDK_INT < 11)
    {
      bool = false;
    }
    else
    {
      ActivityCompatHoneycomb.invalidateOptionsMenu(paramActivity);
      bool = true;
    }
    return bool;
  }
  
  public static void startActivity(Activity paramActivity, Intent paramIntent, Bundle paramBundle)
  {
    if (Build.VERSION.SDK_INT < 16) {
      paramActivity.startActivity(paramIntent);
    } else {
      ActivityCompatJB.startActivity(paramActivity, paramIntent, paramBundle);
    }
  }
  
  public static void startActivityForResult(Activity paramActivity, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    if (Build.VERSION.SDK_INT < 16) {
      paramActivity.startActivityForResult(paramIntent, paramInt);
    } else {
      ActivityCompatJB.startActivityForResult(paramActivity, paramIntent, paramInt, paramBundle);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActivityCompat
 * JD-Core Version:    0.7.0.1
 */