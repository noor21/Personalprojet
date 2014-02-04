package com.kharybdis.hitchernet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

public class TestBroadcast
  extends BroadcastReceiver
{
  public void onReceive(Context paramContext, Intent paramIntent)
  {
    paramIntent.setClass(paramContext, FileTransferService.class);
    LocalBroadcastManager.getInstance(paramContext).sendBroadcast(paramIntent);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.TestBroadcast
 * JD-Core Version:    0.7.0.1
 */