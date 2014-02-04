package com.kharybdis.hitchernet;

import android.app.AlertDialog.Builder;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import de.timroes.swipetodismiss.SwipeDismissList;
import de.timroes.swipetodismiss.SwipeDismissList.OnDismissCallback;
import de.timroes.swipetodismiss.SwipeDismissList.Undoable;
import java.util.HashMap;

public class InboxOutboxFragment
  extends ListFragment
{
  public static final String BUNDLE_KEY_INBOX = "INBOX";
  private static final String TAG = InboxOutboxFragment.class.getSimpleName();
  SwipeDismissList.OnDismissCallback callback = new SwipeDismissList.OnDismissCallback()
  {
    public SwipeDismissList.Undoable onDismiss(AbsListView paramAnonymousAbsListView, final int paramAnonymousInt)
    {
      final HashMap localHashMap = (HashMap)InboxOutboxFragment.this.mAdapter.getItem(paramAnonymousInt);
      InboxOutboxFragment.this.mAdapter.removeItem(paramAnonymousInt);
      InboxOutboxFragment.this.mAdapter.notifyDataSetChanged();
      new SwipeDismissList.Undoable()
      {
        private void finallyDeleteFromSomeStorage(HashMap<String, String> paramAnonymous2HashMap)
        {
          DbHelper.getInstance(InboxOutboxFragment.this.getActivity()).deleteItemWithId((String)paramAnonymous2HashMap.get("_ID"));
        }
        
        public void discard()
        {
          Log.v(InboxOutboxFragment.TAG, "DELETE");
          finallyDeleteFromSomeStorage(localHashMap);
        }
        
        public String getTitle()
        {
          Log.v(InboxOutboxFragment.TAG, "title called");
          return "Deleted";
        }
        
        public void undo()
        {
          InboxOutboxFragment.this.mAdapter.insertItem(localHashMap, paramAnonymousInt);
          InboxOutboxFragment.this.mAdapter.notifyDataSetChanged();
        }
      };
    }
  };
  boolean fromInbox;
  InboxOutboxAdapter mAdapter;
  SwipeDismissList swipeList;
  
  public static final InboxOutboxFragment newInstance(boolean paramBoolean)
  {
    InboxOutboxFragment localInboxOutboxFragment = new InboxOutboxFragment();
    Bundle localBundle = new Bundle();
    localBundle.putBoolean("INBOX", paramBoolean);
    localInboxOutboxFragment.setArguments(localBundle);
    return localInboxOutboxFragment;
  }
  
  private void openFile(int paramInt)
  {
    final String str = ((InboxOutboxAdapter)getListAdapter()).getFilePath(paramInt);
    Object localObject = MimeTypeMap.getFileExtensionFromUrl(str);
    MimeTypeMap.getSingleton().getMimeTypeFromExtension((String)localObject);
    if (str != null)
    {
      localObject = new AlertDialog.Builder(getActivity());
      ((AlertDialog.Builder)localObject).setTitle("Open File?");
      ((AlertDialog.Builder)localObject).setMessage("Saved Location:" + str);
      ((AlertDialog.Builder)localObject).setPositiveButton("Yes", new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          new Utils().startActivityChooser(InboxOutboxFragment.this.getActivity(), str);
        }
      });
      ((AlertDialog.Builder)localObject).setNegativeButton("No", null);
      ((AlertDialog.Builder)localObject).show();
    }
    else
    {
      Toast.makeText(getActivity(), "File Not Found", 1).show();
    }
  }
  
  public void onActivityCreated(Bundle paramBundle)
  {
    super.onActivityCreated(paramBundle);
    this.mAdapter = new InboxOutboxAdapter(DbHelper.getInstance(getActivity()).getData(this.fromInbox), getActivity(), this.fromInbox);
    setListAdapter(this.mAdapter);
    this.swipeList = new SwipeDismissList(getListView(), this.callback);
    this.swipeList.setAutoHideDelay(1000);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.fromInbox = getArguments().getBoolean("INBOX");
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    return paramLayoutInflater.inflate(2130903059, null);
  }
  
  public void onListItemClick(ListView paramListView, View paramView, int paramInt, long paramLong)
  {
    int i = ((InboxOutboxAdapter)getListAdapter()).getPercent(paramInt);
    if ((!this.fromInbox) || (i >= 100))
    {
      openFile(paramInt);
    }
    else
    {
      Log.v("hitchernet", "percentage less than 100");
      Toast.makeText(getActivity(), "File download is not yet completed", 1).show();
    }
  }
  
  public void onPause()
  {
    super.onPause();
    this.swipeList.discardUndo();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.InboxOutboxFragment
 * JD-Core Version:    0.7.0.1
 */