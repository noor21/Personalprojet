package group.pals.android.lib.ui.filechooser.utils.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import group.pals.android.lib.ui.filechooser.R.id;
import group.pals.android.lib.ui.filechooser.R.layout;

public class ContextMenuUtils
{
  public static void showContextMenu(Context paramContext, int paramInt1, int paramInt2, Integer[] paramArrayOfInteger, OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    String str;
    if (paramInt2 <= 0) {
      str = null;
    } else {
      str = paramContext.getString(paramInt2);
    }
    showContextMenu(paramContext, paramInt1, str, paramArrayOfInteger, paramOnMenuItemClickListener);
  }
  
  public static void showContextMenu(Context paramContext, int paramInt, String paramString, final Integer[] paramArrayOfInteger, final OnMenuItemClickListener paramOnMenuItemClickListener)
  {
    Object localObject = new MenuItemAdapter(paramContext, paramArrayOfInteger);
    View localView = LayoutInflater.from(paramContext).inflate(R.layout.afc_context_menu_view, null);
    ListView localListView = (ListView)localView.findViewById(R.id.afc_context_menu_view_listview_menu);
    localListView.setAdapter((ListAdapter)localObject);
    localObject = Dlg.newDlg(paramContext);
    ((AlertDialog)localObject).setButton(-2, null, null);
    ((AlertDialog)localObject).setCanceledOnTouchOutside(true);
    if (paramInt > 0) {
      ((AlertDialog)localObject).setIcon(paramInt);
    }
    ((AlertDialog)localObject).setTitle(paramString);
    ((AlertDialog)localObject).setView(localView);
    if (paramOnMenuItemClickListener != null) {
      localListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          ContextMenuUtils.this.dismiss();
          paramOnMenuItemClickListener.onClick(paramArrayOfInteger[paramAnonymousInt].intValue());
        }
      });
    }
    ((AlertDialog)localObject).show();
  }
  
  public static abstract interface OnMenuItemClickListener
  {
    public abstract void onClick(int paramInt);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.ui.ContextMenuUtils
 * JD-Core Version:    0.7.0.1
 */