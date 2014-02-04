package com.kharybdis.hitchernet;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.DecimalFormat;

public class ProgressDialogFragment
  extends DialogFragment
{
  Dialog dialog;
  ProgressBar pBar;
  TextView tvFileName;
  TextView tvMessage;
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    this.dialog = new Dialog(getActivity());
    View localView = ((LayoutInflater)getActivity().getSystemService("layout_inflater")).inflate(2130903068, null);
    this.pBar = ((ProgressBar)localView.findViewById(2131099726));
    this.tvMessage = ((TextView)localView.findViewById(2131099727));
    this.tvFileName = ((TextView)localView.findViewById(2131099714));
    this.pBar.setProgress(0);
    this.pBar.setMax(100);
    this.dialog.setTitle("Please Wait...");
    this.tvMessage.setText("Please Wait...");
    this.dialog.setContentView(localView);
    return this.dialog;
  }
  
  public void updateAsFinished()
  {
    this.pBar.setProgress(100);
    this.tvFileName.setText("File Transfer Completed");
  }
  
  public void updateDialog(long paramLong, double paramDouble, String paramString1, boolean paramBoolean1, boolean paramBoolean2, float paramFloat, String paramString2)
  {
    String str1;
    if (!paramBoolean2) {
      str1 = "Transferring";
    } else {
      str1 = "Downloading";
    }
    DecimalFormat localDecimalFormat = new DecimalFormat("0.00");
    if (!paramString2.equalsIgnoreCase("")) {
      str2 = paramString2 + " remaining";
    } else {
      str2 = "";
    }
    String str2 = localDecimalFormat.format(paramDouble) + "% @ " + localDecimalFormat.format(paramFloat) + " Mbps  " + str2;
    if (paramBoolean1) {
      str2 = "File Transfer Completed";
    }
    this.dialog.setTitle(str1 + "...");
    this.tvFileName.setText(paramString1);
    if (paramDouble == 0.0D) {
      str2 = "";
    }
    this.tvMessage.setText(str2);
    this.pBar.setProgress((int)paramDouble);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.ProgressDialogFragment
 * JD-Core Version:    0.7.0.1
 */