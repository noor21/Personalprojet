package group.pals.android.lib.ui.filechooser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.io.IFileFilter;
import group.pals.android.lib.ui.filechooser.prefs.DisplayPrefs;
import group.pals.android.lib.ui.filechooser.prefs.DisplayPrefs.FileTimeDisplay;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.FilterMode;
import group.pals.android.lib.ui.filechooser.utils.Converter;
import group.pals.android.lib.ui.filechooser.utils.DateUtils;
import group.pals.android.lib.ui.filechooser.utils.FileUtils;
import group.pals.android.lib.ui.filechooser.utils.ui.ContextMenuUtils;
import group.pals.android.lib.ui.filechooser.utils.ui.ContextMenuUtils.OnMenuItemClickListener;
import group.pals.android.lib.ui.filechooser.utils.ui.LoadingDialog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IFileAdapter
  extends BaseAdapter
{
  public static final String _ClassName = IFileAdapter.class.getName();
  private final Integer[] mAdvancedSelectionOptions;
  private final View.OnLongClickListener mCheckboxSelectionOnLongClickListener = new View.OnLongClickListener()
  {
    public boolean onLongClick(final View paramAnonymousView)
    {
      ContextMenuUtils.showContextMenu(paramAnonymousView.getContext(), 0, R.string.afc_title_advanced_selection, IFileAdapter.this.mAdvancedSelectionOptions, new ContextMenuUtils.OnMenuItemClickListener()
      {
        public void onClick(final int paramAnonymous2Int)
        {
          new LoadingDialog(paramAnonymousView.getContext(), R.string.afc_msg_loading, false)
          {
            protected Object doInBackground(Void... paramAnonymous3VarArgs)
            {
              if (paramAnonymous2Int != R.string.afc_cmd_advanced_selection_all)
              {
                if (paramAnonymous2Int != R.string.afc_cmd_advanced_selection_none)
                {
                  if (paramAnonymous2Int != R.string.afc_cmd_advanced_selection_invert)
                  {
                    if (paramAnonymous2Int != R.string.afc_cmd_select_all_files)
                    {
                      if (paramAnonymous2Int == R.string.afc_cmd_select_all_folders) {
                        IFileAdapter.this.selectAll(false, new IFileFilter()
                        {
                          public boolean accept(IFile paramAnonymous4IFile)
                          {
                            return paramAnonymous4IFile.isDirectory();
                          }
                        });
                      }
                    }
                    else {
                      IFileAdapter.this.selectAll(false, new IFileFilter()
                      {
                        public boolean accept(IFile paramAnonymous4IFile)
                        {
                          return paramAnonymous4IFile.isFile();
                        }
                      });
                    }
                  }
                  else {
                    IFileAdapter.this.invertSelection(false);
                  }
                }
                else {
                  IFileAdapter.this.selectNone(false);
                }
              }
              else {
                IFileAdapter.this.selectAll(false, null);
              }
              return null;
            }
            
            protected void onPostExecute(Object paramAnonymous3Object)
            {
              super.onPostExecute(paramAnonymous3Object);
              IFileAdapter.this.notifyDataSetChanged();
            }
          }.execute(new Void[0]);
        }
      });
      return true;
    }
  };
  private final Context mContext;
  private List<IFileDataModel> mData;
  private final DisplayPrefs.FileTimeDisplay mFileTimeDisplay;
  private final IFileProvider.FilterMode mFilterMode;
  private LayoutInflater mInflater;
  private boolean mMultiSelection;
  
  public IFileAdapter(Context paramContext, List<IFileDataModel> paramList, IFileProvider.FilterMode paramFilterMode, boolean paramBoolean)
  {
    this.mContext = paramContext;
    this.mData = paramList;
    this.mInflater = LayoutInflater.from(this.mContext);
    this.mFilterMode = paramFilterMode;
    this.mMultiSelection = paramBoolean;
    Integer[] arrayOfInteger;
    switch (this.mFilterMode)
    {
    default: 
      arrayOfInteger = new Integer[5];
      arrayOfInteger[0] = Integer.valueOf(R.string.afc_cmd_advanced_selection_all);
      arrayOfInteger[1] = Integer.valueOf(R.string.afc_cmd_advanced_selection_none);
      arrayOfInteger[2] = Integer.valueOf(R.string.afc_cmd_advanced_selection_invert);
      arrayOfInteger[3] = Integer.valueOf(R.string.afc_cmd_select_all_files);
      arrayOfInteger[4] = Integer.valueOf(R.string.afc_cmd_select_all_folders);
      this.mAdvancedSelectionOptions = arrayOfInteger;
      break;
    case DirectoriesOnly: 
    case FilesAndDirectories: 
      arrayOfInteger = new Integer[3];
      arrayOfInteger[0] = Integer.valueOf(R.string.afc_cmd_advanced_selection_all);
      arrayOfInteger[1] = Integer.valueOf(R.string.afc_cmd_advanced_selection_none);
      arrayOfInteger[2] = Integer.valueOf(R.string.afc_cmd_advanced_selection_invert);
      this.mAdvancedSelectionOptions = arrayOfInteger;
    }
    this.mFileTimeDisplay = new DisplayPrefs.FileTimeDisplay(DisplayPrefs.isShowTimeForOldDaysThisYear(this.mContext), DisplayPrefs.isShowTimeForOldDays(this.mContext));
  }
  
  private void updateView(ViewGroup paramViewGroup, View paramView, Bag paramBag, final IFileDataModel paramIFileDataModel, IFile paramIFile)
  {
    paramBag.mTxtFileName.setSingleLine(paramViewGroup instanceof GridView);
    paramBag.mImageIcon.setImageResource(FileUtils.getResIcon(paramIFile));
    paramBag.mTxtFileName.setText(paramIFile.getName());
    if (!paramIFileDataModel.isTobeDeleted()) {
      paramBag.mTxtFileName.setPaintFlags(0xFFFFFFEF & paramBag.mTxtFileName.getPaintFlags());
    } else {
      paramBag.mTxtFileName.setPaintFlags(0x10 | paramBag.mTxtFileName.getPaintFlags());
    }
    String str = DateUtils.formatDate(this.mContext, paramIFile.lastModified(), this.mFileTimeDisplay);
    if (!paramIFile.isDirectory())
    {
      TextView localTextView = paramBag.mTxtFileInfo;
      Object[] arrayOfObject = new Object[2];
      arrayOfObject[0] = Converter.sizeToStr(paramIFile.length());
      arrayOfObject[1] = str;
      localTextView.setText(String.format("%s, %s", arrayOfObject));
    }
    else
    {
      paramBag.mTxtFileInfo.setText(str);
    }
    if (!this.mMultiSelection)
    {
      paramBag.mCheckboxSelection.setVisibility(8);
    }
    else if ((!IFileProvider.FilterMode.FilesOnly.equals(this.mFilterMode)) || (!paramIFile.isDirectory()))
    {
      paramBag.mCheckboxSelection.setVisibility(0);
      paramBag.mCheckboxSelection.setFocusable(false);
      paramBag.mCheckboxSelection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
      {
        public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
        {
          paramIFileDataModel.setSelected(paramAnonymousBoolean);
        }
      });
      paramBag.mCheckboxSelection.setOnLongClickListener(this.mCheckboxSelectionOnLongClickListener);
      paramBag.mCheckboxSelection.setChecked(paramIFileDataModel.isSelected());
    }
    else
    {
      paramBag.mCheckboxSelection.setVisibility(8);
    }
  }
  
  public void add(IFileDataModel paramIFileDataModel)
  {
    if (this.mData != null) {
      this.mData.add(paramIFileDataModel);
    }
  }
  
  public void clear()
  {
    if (this.mData != null) {
      this.mData.clear();
    }
  }
  
  public int getCount()
  {
    int i;
    if (this.mData == null) {
      i = 0;
    } else {
      i = this.mData.size();
    }
    return i;
  }
  
  public IFileDataModel getItem(int paramInt)
  {
    IFileDataModel localIFileDataModel;
    if (this.mData == null) {
      localIFileDataModel = null;
    } else {
      localIFileDataModel = (IFileDataModel)this.mData.get(paramInt);
    }
    return localIFileDataModel;
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public ArrayList<IFileDataModel> getSelectedItems()
  {
    ArrayList localArrayList = new ArrayList();
    for (int i = 0; i < getCount(); i++) {
      if (getItem(i).isSelected()) {
        localArrayList.add(getItem(i));
      }
    }
    return localArrayList;
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    IFileDataModel localIFileDataModel = getItem(paramInt);
    Bag localBag;
    if (paramView != null)
    {
      localBag = (Bag)paramView.getTag();
    }
    else
    {
      paramView = this.mInflater.inflate(R.layout.afc_file_item, null);
      localBag = new Bag(null);
      localBag.mImageIcon = ((ImageView)paramView.findViewById(R.id.afc_file_item_imageview_icon));
      localBag.mTxtFileName = ((TextView)paramView.findViewById(R.id.afc_file_item_textview_filename));
      localBag.mTxtFileInfo = ((TextView)paramView.findViewById(R.id.afc_file_item_textview_file_info));
      localBag.mCheckboxSelection = ((CheckBox)paramView.findViewById(R.id.afc_file_item_checkbox_selection));
      paramView.setTag(localBag);
    }
    IFile localIFile = localIFileDataModel.getFile();
    updateView(paramViewGroup, paramView, localBag, localIFileDataModel, localIFile);
    return paramView;
  }
  
  public void invertSelection(boolean paramBoolean)
  {
    for (int i = 0; i < getCount(); i++)
    {
      IFileDataModel localIFileDataModel = getItem(i);
      boolean bool;
      if (!localIFileDataModel.isSelected()) {
        bool = true;
      } else {
        bool = false;
      }
      localIFileDataModel.setSelected(bool);
    }
    if (paramBoolean) {
      notifyDataSetChanged();
    }
  }
  
  public boolean isMultiSelection()
  {
    return this.mMultiSelection;
  }
  
  public void notifyDataSetChanged()
  {
    updateEnvironments();
    super.notifyDataSetChanged();
  }
  
  public void remove(IFileDataModel paramIFileDataModel)
  {
    if (this.mData != null) {
      this.mData.remove(paramIFileDataModel);
    }
  }
  
  public void removeAll(Collection<IFileDataModel> paramCollection)
  {
    if (this.mData != null) {
      this.mData.removeAll(paramCollection);
    }
  }
  
  public void selectAll(boolean paramBoolean, IFileFilter paramIFileFilter)
  {
    for (int i = 0; i < getCount(); i++)
    {
      IFileDataModel localIFileDataModel = getItem(i);
      boolean bool;
      if (paramIFileFilter != null) {
        bool = paramIFileFilter.accept(localIFileDataModel.getFile());
      } else {
        bool = true;
      }
      localIFileDataModel.setSelected(bool);
    }
    if (paramBoolean) {
      notifyDataSetChanged();
    }
  }
  
  public void selectNone(boolean paramBoolean)
  {
    for (int i = 0; i < getCount(); i++) {
      getItem(i).setSelected(false);
    }
    if (paramBoolean) {
      notifyDataSetChanged();
    }
  }
  
  public void setMultiSelection(boolean paramBoolean)
  {
    if (this.mMultiSelection != paramBoolean)
    {
      this.mMultiSelection = paramBoolean;
      if (!this.mMultiSelection)
      {
        if (getCount() > 0) {
          for (int i = 0; i < this.mData.size(); i++) {
            ((IFileDataModel)this.mData.get(i)).setSelected(false);
          }
        }
      }
      else {
        notifyDataSetChanged();
      }
    }
  }
  
  public void updateEnvironments()
  {
    this.mFileTimeDisplay.setShowTimeForOldDaysThisYear(DisplayPrefs.isShowTimeForOldDaysThisYear(this.mContext));
    this.mFileTimeDisplay.setShowTimeForOldDays(DisplayPrefs.isShowTimeForOldDays(this.mContext));
  }
  
  private static final class Bag
  {
    CheckBox mCheckboxSelection;
    ImageView mImageIcon;
    TextView mTxtFileInfo;
    TextView mTxtFileName;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.IFileAdapter
 * JD-Core Version:    0.7.0.1
 */