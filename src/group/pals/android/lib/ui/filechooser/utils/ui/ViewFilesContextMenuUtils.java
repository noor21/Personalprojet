package group.pals.android.lib.ui.filechooser.utils.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import group.pals.android.lib.ui.filechooser.IFileAdapter;
import group.pals.android.lib.ui.filechooser.IFileDataModel;
import group.pals.android.lib.ui.filechooser.R.layout;
import group.pals.android.lib.ui.filechooser.R.string;
import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.FilterMode;
import group.pals.android.lib.ui.filechooser.utils.history.History;
import java.util.ArrayList;
import java.util.List;

public class ViewFilesContextMenuUtils
{
  public static void doShowHistoryContents(Context paramContext, IFileProvider paramIFileProvider, History<IFile> paramHistory, IFile paramIFile, TaskListener paramTaskListener)
  {
    if (!paramHistory.isEmpty())
    {
      final AlertDialog localAlertDialog = Dlg.newDlg(paramContext);
      localAlertDialog.setButton(-2, null, null);
      localAlertDialog.setIcon(17301659);
      localAlertDialog.setTitle(R.string.afc_title_history);
      Object localObject = new ArrayList();
      ArrayList localArrayList = paramHistory.items();
      for (int j = -1 + localArrayList.size(); j >= 0; j--)
      {
        IFile localIFile = (IFile)localArrayList.get(j);
        if (localIFile != paramIFile)
        {
          int k = 0;
          int i = 0;
          while (i < ((List)localObject).size()) {
            if (!localIFile.equalsToPath(((IFileDataModel)((List)localObject).get(i)).getFile())) {
              i++;
            } else {
              k = 1;
            }
          }
          if (k == 0) {
            ((List)localObject).add(new IFileDataModel(localIFile));
          }
        }
      }
      final IFileAdapter localIFileAdapter = new IFileAdapter(paramContext, (List)localObject, IFileProvider.FilterMode.DirectoriesOnly, false);
      localObject = (ListView)LayoutInflater.from(paramContext).inflate(R.layout.afc_listview_files, null);
      ((ListView)localObject).setBackgroundResource(0);
      ((ListView)localObject).setFastScrollEnabled(true);
      ((ListView)localObject).setAdapter(localIFileAdapter);
      ((ListView)localObject).setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
        {
          if (ViewFilesContextMenuUtils.this != null)
          {
            localAlertDialog.dismiss();
            ViewFilesContextMenuUtils.this.onFinish(true, localIFileAdapter.getItem(paramAnonymousInt).getFile());
          }
        }
      });
      localAlertDialog.setView((View)localObject);
      localAlertDialog.setButton(-1, paramContext.getString(R.string.afc_cmd_clear), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          paramAnonymousDialogInterface.cancel();
          ViewFilesContextMenuUtils.this.clear();
        }
      });
      localAlertDialog.setOnCancelListener(new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          if (ViewFilesContextMenuUtils.this != null) {
            ViewFilesContextMenuUtils.this.onFinish(true, null);
          }
        }
      });
      localAlertDialog.show();
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.ui.ViewFilesContextMenuUtils
 * JD-Core Version:    0.7.0.1
 */