package android.support.v4.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

class ActivityCompatJB
{
  public static void startActivity(Context paramContext, Intent paramIntent, Bundle paramBundle)
  {
    paramContext.startActivity(paramIntent, paramBundle);
  }
  
  public static void startActivityForResult(Activity paramActivity, Intent paramIntent, int paramInt, Bundle paramBundle)
  {
    paramActivity.startActivityForResult(paramIntent, paramInt, paramBundle);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.ActivityCompatJB
 * JD-Core Version:    0.7.0.1
 */