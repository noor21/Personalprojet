package org.acra.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.acra.ACRA;

public final class ToastSender
{
  public static void sendToast(Context paramContext, int paramInt1, int paramInt2)
  {
    try
    {
      Toast.makeText(paramContext, paramInt1, paramInt2).show();
      return;
    }
    catch (RuntimeException localRuntimeException)
    {
      for (;;)
      {
        Log.e(ACRA.LOG_TAG, "Could not send crash Toast", localRuntimeException);
      }
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     org.acra.util.ToastSender
 * JD-Core Version:    0.7.0.1
 */