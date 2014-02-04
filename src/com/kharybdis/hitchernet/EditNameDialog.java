package com.kharybdis.hitchernet;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class EditNameDialog
  extends DialogFragment
  implements TextView.OnEditorActionListener
{
  private EditText mEditText;
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    Object localObject = new AlertDialog.Builder(getActivity());
    View localView = ((LayoutInflater)getActivity().getSystemService("layout_inflater")).inflate(2130903056, null);
    this.mEditText = ((EditText)localView.findViewById(2131099711));
    this.mEditText.setText(new Preferences(getActivity()).getThisDeviceName());
    this.mEditText.requestFocus();
    this.mEditText.setOnEditorActionListener(this);
    ((AlertDialog.Builder)localObject).setTitle("Rename Device");
    ((AlertDialog.Builder)localObject).setPositiveButton("Rename", new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        EditNameDialog.this.performRename();
      }
    });
    ((AlertDialog.Builder)localObject).setNegativeButton("Cancel", null);
    ((AlertDialog.Builder)localObject).setView(localView);
    localObject = ((AlertDialog.Builder)localObject).create();
    ((Dialog)localObject).getWindow().setSoftInputMode(4);
    return localObject;
  }
  
  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool;
    if (6 != paramInt)
    {
      bool = false;
    }
    else
    {
      performRename();
      bool = true;
    }
    return bool;
  }
  
  public void performRename()
  {
    SettingFragment localSettingFragment = (SettingFragment)getFragmentManager().findFragmentByTag("SETTINGS");
    if (!this.mEditText.getText().toString().trim().equals(""))
    {
      localSettingFragment.onFinishEditDialog(this.mEditText.getText().toString());
      dismiss();
    }
    else
    {
      Toast.makeText(getActivity(), "Device Name cannot be empty", 1).show();
    }
  }
  
  public static abstract interface EditNameDialogListener
  {
    public abstract void onFinishEditDialog(String paramString);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.EditNameDialog
 * JD-Core Version:    0.7.0.1
 */