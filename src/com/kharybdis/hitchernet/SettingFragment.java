package com.kharybdis.hitchernet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import group.pals.android.lib.ui.filechooser.FileChooserActivity;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.FilterMode;
import java.io.File;
import java.util.List;

public class SettingFragment
  extends Fragment
  implements EditNameDialog.EditNameDialogListener
{
  private static final String TAG = SettingFragment.class.getSimpleName();
  DeviceRenamer deviceRenamer;
  Preferences prefs;
  TextView tvDeviceName;
  TextView tvDirectory;
  
  private void showEditDialog()
  {
    FragmentManager localFragmentManager = getFragmentManager();
    new EditNameDialog().show(localFragmentManager, "fragment_edit_name");
  }
  
  public void onActivityResult(int paramInt1, int paramInt2, Intent paramIntent)
  {
    super.onActivityResult(paramInt1, paramInt2, paramIntent);
    if (paramInt1 == 20)
    {
      Object localObject = TAG;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(paramInt2);
      Log.i((String)localObject, String.format("Return from DirChooser with result %d", arrayOfObject));
      if (paramInt2 != -1)
      {
        this.tvDirectory.setText(this.prefs.getFileSavingLocation());
      }
      else
      {
        ((IFileProvider.FilterMode)paramIntent.getSerializableExtra(FileChooserActivity._FilterMode));
        paramIntent.getBooleanExtra(FileChooserActivity._SaveDialog, false);
        localObject = (List)paramIntent.getSerializableExtra(FileChooserActivity._Results);
        if ((localObject == null) || (((List)localObject).size() != 0))
        {
          localObject = ((LocalFile)((List)localObject).get(0)).getAbsolutePath() + File.separator;
          Toast.makeText(getActivity(), (CharSequence)localObject, 1).show();
          this.tvDirectory.setText((CharSequence)localObject);
          this.prefs.setFileSaveLocation((String)localObject);
        }
      }
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    this.prefs = new Preferences(getActivity());
    this.deviceRenamer = ((DeviceRenamer)getActivity());
  }
  
  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130903070, null);
    LinearLayout localLinearLayout1 = (LinearLayout)localView.findViewById(2131099730);
    LinearLayout localLinearLayout2 = (LinearLayout)localView.findViewById(2131099733);
    this.tvDirectory = ((TextView)localView.findViewById(2131099732));
    this.tvDeviceName = ((TextView)localView.findViewById(2131099701));
    this.tvDirectory.setText(this.prefs.getFileSavingLocation());
    this.tvDeviceName.setText(this.prefs.getThisDeviceName());
    localLinearLayout1.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        Intent localIntent = new Intent(SettingFragment.this.getActivity(), FileChooserActivity.class);
        localIntent.putExtra(FileChooserActivity._FilterMode, IFileProvider.FilterMode.DirectoriesOnly);
        localIntent.putExtra(FileChooserActivity._SelectFile, SettingFragment.this.prefs.getFileSavingLocation());
        SettingFragment.this.startActivityForResult(localIntent, 20);
      }
    });
    localLinearLayout2.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        SettingFragment.this.showEditDialog();
      }
    });
    return localView;
  }
  
  public void onFinishEditDialog(final String paramString)
  {
    this.deviceRenamer.renameDevice(paramString, new WifiP2pManager.ActionListener()
    {
      public void onFailure(int paramAnonymousInt)
      {
        Toast.makeText(SettingFragment.this.getActivity(), "Failed to Rename", 1).show();
      }
      
      public void onSuccess()
      {
        Toast.makeText(SettingFragment.this.getActivity(), "Successfully Renamed", 1).show();
        SettingFragment.this.tvDeviceName.setText(paramString);
      }
    });
  }
  
  public static abstract interface DeviceRenamer
  {
    public abstract void renameDevice(String paramString, WifiP2pManager.ActionListener paramActionListener);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.SettingFragment
 * JD-Core Version:    0.7.0.1
 */