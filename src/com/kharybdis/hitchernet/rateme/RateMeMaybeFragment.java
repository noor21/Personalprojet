package com.kharybdis.hitchernet.rateme;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class RateMeMaybeFragment
  extends DialogFragment
  implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener
{
  private int customIcon;
  private RMMFragInterface mInterface;
  private String message;
  private String negativeBtn;
  private String neutralBtn;
  private String positiveBtn;
  private String title;
  
  public void onCancel(DialogInterface paramDialogInterface)
  {
    this.mInterface._handleCancel();
  }
  
  public void onClick(DialogInterface paramDialogInterface, int paramInt)
  {
    switch (paramInt)
    {
    case -3: 
      this.mInterface._handleNeutralChoice();
      break;
    case -2: 
      this.mInterface._handleNegativeChoice();
      break;
    case -1: 
      this.mInterface._handlePositiveChoice();
    }
  }
  
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setRetainInstance(true);
  }
  
  public Dialog onCreateDialog(Bundle paramBundle)
  {
    AlertDialog.Builder localBuilder = new AlertDialog.Builder(getActivity());
    if (this.customIcon != 0) {
      localBuilder.setIcon(this.customIcon);
    }
    localBuilder.setTitle(this.title);
    localBuilder.setMessage(this.message);
    localBuilder.setPositiveButton(this.positiveBtn, this);
    localBuilder.setNeutralButton(this.neutralBtn, this);
    localBuilder.setNegativeButton(this.negativeBtn, this);
    return localBuilder.create();
  }
  
  public void onDestroyView()
  {
    Dialog localDialog = getDialog();
    if ((localDialog != null) && (getRetainInstance())) {
      localDialog.setDismissMessage(null);
    }
    super.onDestroyView();
  }
  
  public void setData(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, RMMFragInterface paramRMMFragInterface)
  {
    this.customIcon = paramInt;
    this.title = paramString1;
    this.message = paramString2;
    this.positiveBtn = paramString3;
    this.neutralBtn = paramString4;
    this.negativeBtn = paramString5;
    this.mInterface = paramRMMFragInterface;
  }
  
  public static abstract interface RMMFragInterface
  {
    public abstract void _handleCancel();
    
    public abstract void _handleNegativeChoice();
    
    public abstract void _handleNeutralChoice();
    
    public abstract void _handlePositiveChoice();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.rateme.RateMeMaybeFragment
 * JD-Core Version:    0.7.0.1
 */