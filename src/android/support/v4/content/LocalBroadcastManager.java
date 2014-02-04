package android.support.v4.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class LocalBroadcastManager
{
  private static final boolean DEBUG = false;
  static final int MSG_EXEC_PENDING_BROADCASTS = 1;
  private static final String TAG = "LocalBroadcastManager";
  private static LocalBroadcastManager mInstance;
  private static final Object mLock = new Object();
  private final HashMap<String, ArrayList<ReceiverRecord>> mActions = new HashMap();
  private final Context mAppContext;
  private final Handler mHandler;
  private final ArrayList<BroadcastRecord> mPendingBroadcasts = new ArrayList();
  private final HashMap<BroadcastReceiver, ArrayList<IntentFilter>> mReceivers = new HashMap();
  
  private LocalBroadcastManager(Context paramContext)
  {
    this.mAppContext = paramContext;
    this.mHandler = new Handler(paramContext.getMainLooper())
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        switch (paramAnonymousMessage.what)
        {
        default: 
          super.handleMessage(paramAnonymousMessage);
          break;
        case 1: 
          LocalBroadcastManager.this.executePendingBroadcasts();
        }
      }
    };
  }
  
  private void executePendingBroadcasts()
  {
    for (;;)
    {
      int j;
      synchronized (this.mReceivers)
      {
        int i = this.mPendingBroadcasts.size();
        if (i <= 0) {
          return;
        }
        BroadcastRecord[] arrayOfBroadcastRecord = new BroadcastRecord[i];
        this.mPendingBroadcasts.toArray(arrayOfBroadcastRecord);
        this.mPendingBroadcasts.clear();
        j = 0;
        if (j >= arrayOfBroadcastRecord.length) {
          continue;
        }
        BroadcastRecord localBroadcastRecord = arrayOfBroadcastRecord[j];
        int k = 0;
        if (k < localBroadcastRecord.receivers.size())
        {
          ((ReceiverRecord)localBroadcastRecord.receivers.get(k)).receiver.onReceive(this.mAppContext, localBroadcastRecord.intent);
          k++;
        }
      }
      j++;
    }
  }
  
  public static LocalBroadcastManager getInstance(Context paramContext)
  {
    synchronized (mLock)
    {
      if (mInstance == null) {
        mInstance = new LocalBroadcastManager(paramContext.getApplicationContext());
      }
      LocalBroadcastManager localLocalBroadcastManager = mInstance;
      return localLocalBroadcastManager;
    }
  }
  
  public void registerReceiver(BroadcastReceiver paramBroadcastReceiver, IntentFilter paramIntentFilter)
  {
    synchronized (this.mReceivers)
    {
      ReceiverRecord localReceiverRecord = new ReceiverRecord(paramIntentFilter, paramBroadcastReceiver);
      ArrayList localArrayList = (ArrayList)this.mReceivers.get(paramBroadcastReceiver);
      if (localArrayList == null)
      {
        localArrayList = new ArrayList(1);
        this.mReceivers.put(paramBroadcastReceiver, localArrayList);
      }
      localArrayList.add(paramIntentFilter);
      for (int i = 0; i < paramIntentFilter.countActions(); i++)
      {
        String str = paramIntentFilter.getAction(i);
        localArrayList = (ArrayList)this.mActions.get(str);
        if (localArrayList == null)
        {
          localArrayList = new ArrayList(1);
          this.mActions.put(str, localArrayList);
        }
        localArrayList.add(localReceiverRecord);
      }
      return;
    }
  }
  
  public boolean sendBroadcast(Intent paramIntent)
  {
    Object localObject1;
    for (;;)
    {
      int j;
      int m;
      int i;
      synchronized (this.mReceivers)
      {
        String str1 = paramIntent.getAction();
        String str3 = paramIntent.resolveTypeIfNeeded(this.mAppContext.getContentResolver());
        Uri localUri = paramIntent.getData();
        String str2 = paramIntent.getScheme();
        Set localSet = paramIntent.getCategories();
        if ((0x8 & paramIntent.getFlags()) == 0) {
          break label498;
        }
        k = 1;
        if (k != 0) {
          Log.v("LocalBroadcastManager", "Resolving type " + str3 + " scheme " + str2 + " of intent " + paramIntent);
        }
        ArrayList localArrayList3 = (ArrayList)this.mActions.get(paramIntent.getAction());
        if (localArrayList3 != null)
        {
          if (k == 0) {
            continue;
          }
          Log.v("LocalBroadcastManager", "Action list: " + localArrayList3);
          continue;
          if (j >= localArrayList3.size()) {
            break label532;
          }
          localObject2 = (ReceiverRecord)localArrayList3.get(j);
          if (k != 0) {
            Log.v("LocalBroadcastManager", "Matching against filter " + ((ReceiverRecord)localObject2).filter);
          }
          if (((ReceiverRecord)localObject2).broadcasting)
          {
            if (k != 0) {
              Log.v("LocalBroadcastManager", "  Filter's target already added");
            }
          }
          else
          {
            m = ((ReceiverRecord)localObject2).filter.match(str1, str3, str2, localUri, localSet, "LocalBroadcastManager");
            if (m >= 0)
            {
              if (k != 0) {
                Log.v("LocalBroadcastManager", "  Filter matched!  match=0x" + Integer.toHexString(m));
              }
              ArrayList localArrayList1;
              if (localArrayList1 == null) {
                localArrayList1 = new ArrayList();
              }
              localArrayList1.add(localObject2);
              ((ReceiverRecord)localObject2).broadcasting = true;
            }
          }
        }
      }
      continue;
      label498:
      int k = 0;
      continue;
      Object localObject2 = "action";
      continue;
      localObject2 = "category";
      continue;
      localObject2 = "data";
      continue;
      localObject2 = "type";
      continue;
      label532:
      if (localObject1 != null) {
        j = 0;
      }
    }
    return localObject1;
  }
  
  public void sendBroadcastSync(Intent paramIntent)
  {
    if (sendBroadcast(paramIntent)) {
      executePendingBroadcasts();
    }
  }
  
  public void unregisterReceiver(BroadcastReceiver paramBroadcastReceiver)
  {
    for (;;)
    {
      int k;
      int m;
      synchronized (this.mReceivers)
      {
        ArrayList localArrayList2 = (ArrayList)this.mReceivers.remove(paramBroadcastReceiver);
        if (localArrayList2 != null) {
          break label167;
        }
        break label166;
        int i;
        if (i < localArrayList2.size())
        {
          IntentFilter localIntentFilter = (IntentFilter)localArrayList2.get(i);
          k = 0;
          if (k >= localIntentFilter.countActions()) {
            break label184;
          }
          String str = localIntentFilter.getAction(k);
          ArrayList localArrayList1 = (ArrayList)this.mActions.get(str);
          if (localArrayList1 == null) {
            break label178;
          }
          m = 0;
          if (m < localArrayList1.size())
          {
            if (((ReceiverRecord)localArrayList1.get(m)).receiver != paramBroadcastReceiver) {
              break label172;
            }
            localArrayList1.remove(m);
            m--;
            break label172;
          }
          if (localArrayList1.size() > 0) {
            break label178;
          }
          this.mActions.remove(str);
          break label178;
        }
      }
      label166:
      return;
      label167:
      int j = 0;
      continue;
      label172:
      m++;
      continue;
      label178:
      k++;
      continue;
      label184:
      j++;
    }
  }
  
  private static class BroadcastRecord
  {
    final Intent intent;
    final ArrayList<LocalBroadcastManager.ReceiverRecord> receivers;
    
    BroadcastRecord(Intent paramIntent, ArrayList<LocalBroadcastManager.ReceiverRecord> paramArrayList)
    {
      this.intent = paramIntent;
      this.receivers = paramArrayList;
    }
  }
  
  private static class ReceiverRecord
  {
    boolean broadcasting;
    final IntentFilter filter;
    final BroadcastReceiver receiver;
    
    ReceiverRecord(IntentFilter paramIntentFilter, BroadcastReceiver paramBroadcastReceiver)
    {
      this.filter = paramIntentFilter;
      this.receiver = paramBroadcastReceiver;
    }
    
    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder(128);
      localStringBuilder.append("Receiver{");
      localStringBuilder.append(this.receiver);
      localStringBuilder.append(" filter=");
      localStringBuilder.append(this.filter);
      localStringBuilder.append("}");
      return localStringBuilder.toString();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.content.LocalBroadcastManager
 * JD-Core Version:    0.7.0.1
 */