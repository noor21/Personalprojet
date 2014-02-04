package group.pals.android.lib.ui.filechooser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.io.IFileFilter;
import group.pals.android.lib.ui.filechooser.prefs.DisplayPrefs;
import group.pals.android.lib.ui.filechooser.services.FileProviderService.LocalBinder;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.FilterMode;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.SortOrder;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.SortType;
import group.pals.android.lib.ui.filechooser.services.LocalFileProvider;
import group.pals.android.lib.ui.filechooser.utils.ActivityCompat;
import group.pals.android.lib.ui.filechooser.utils.E;
import group.pals.android.lib.ui.filechooser.utils.FileComparator;
import group.pals.android.lib.ui.filechooser.utils.FileUtils;
import group.pals.android.lib.ui.filechooser.utils.Ui;
import group.pals.android.lib.ui.filechooser.utils.Utils;
import group.pals.android.lib.ui.filechooser.utils.history.History;
import group.pals.android.lib.ui.filechooser.utils.history.HistoryFilter;
import group.pals.android.lib.ui.filechooser.utils.history.HistoryListener;
import group.pals.android.lib.ui.filechooser.utils.history.HistoryStore;
import group.pals.android.lib.ui.filechooser.utils.ui.Dlg;
import group.pals.android.lib.ui.filechooser.utils.ui.LoadingDialog;
import group.pals.android.lib.ui.filechooser.utils.ui.TaskListener;
import group.pals.android.lib.ui.filechooser.utils.ui.ViewFilesContextMenuUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class FileChooserActivity
  extends Activity
{
  private static final int[] _BtnSortIds;
  public static final String _ClassName = FileChooserActivity.class.getName();
  static final String _CurrentLocation;
  public static final String _DefaultFilename;
  public static final String _DisplayHiddenFiles;
  public static final String _DoubleTapToChooseFiles;
  public static final String _FileProviderClass;
  public static final String _FilterMode;
  static final String _FullHistory;
  static final String _History;
  public static final String _MaxFileCount;
  public static final String _MultiSelection;
  public static final String _RegexFilenameFilter;
  public static final String _Results;
  public static final String _Rootpath;
  public static final String _SaveDialog;
  public static final String _SelectFile;
  public static final String _Theme = _ClassName + ".theme";
  private final View.OnLongClickListener mBtnGoBackForwardOnLongClickListener = new View.OnLongClickListener()
  {
    public boolean onLongClick(View paramAnonymousView)
    {
      ViewFilesContextMenuUtils.doShowHistoryContents(FileChooserActivity.this, FileChooserActivity.this.mFileProvider, FileChooserActivity.this.mFullHistory, FileChooserActivity.this.getLocation(), new TaskListener()
      {
        public void onFinish(boolean paramAnonymous2Boolean, Object paramAnonymous2Object)
        {
          FileChooserActivity.this.mHistory.removeAll(new HistoryFilter()
          {
            public boolean accept(IFile paramAnonymous3IFile)
            {
              boolean bool;
              if (FileChooserActivity.this.mFullHistory.indexOf(paramAnonymous3IFile) >= 0) {
                bool = false;
              } else {
                bool = true;
              }
              return bool;
            }
          });
          if (!(paramAnonymous2Object instanceof IFile))
          {
            if (FileChooserActivity.this.mHistory.isEmpty())
            {
              FileChooserActivity.this.mHistory.push(FileChooserActivity.this.getLocation());
              FileChooserActivity.this.mFullHistory.push(FileChooserActivity.this.getLocation());
            }
          }
          else {
            FileChooserActivity.this.setLocation((IFile)paramAnonymous2Object, new TaskListener()
            {
              public void onFinish(boolean paramAnonymous3Boolean, Object paramAnonymous3Object)
              {
                if (paramAnonymous3Boolean) {
                  FileChooserActivity.this.mHistory.notifyHistoryChanged();
                }
              }
            });
          }
        }
      });
      return false;
    }
  };
  private final View.OnClickListener mBtnGoBackOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      IFile localIFile1 = FileChooserActivity.this.getLocation();
      IFile localIFile2;
      for (;;)
      {
        localIFile2 = (IFile)FileChooserActivity.this.mHistory.prevOf(localIFile1);
        if (!localIFile1.equalsToPath(localIFile2)) {
          break;
        }
        FileChooserActivity.this.mHistory.remove(localIFile2);
      }
      if (localIFile2 == null) {
        FileChooserActivity.this.mViewGoBack.setEnabled(false);
      } else {
        FileChooserActivity.this.setLocation(localIFile2, new TaskListener()
        {
          public void onFinish(boolean paramAnonymous2Boolean, Object paramAnonymous2Object)
          {
            if (paramAnonymous2Boolean)
            {
              ImageView localImageView = FileChooserActivity.this.mViewGoBack;
              boolean bool;
              if (FileChooserActivity.this.mHistory.prevOf(FileChooserActivity.this.getLocation()) == null) {
                bool = false;
              } else {
                bool = true;
              }
              localImageView.setEnabled(bool);
              FileChooserActivity.this.mViewGoForward.setEnabled(true);
              FileChooserActivity.this.mFullHistory.push((IFile)paramAnonymous2Object);
            }
          }
        });
      }
    }
  };
  private final View.OnClickListener mBtnGoForwardOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      IFile localIFile2 = FileChooserActivity.this.getLocation();
      IFile localIFile1;
      for (;;)
      {
        localIFile1 = (IFile)FileChooserActivity.this.mHistory.nextOf(localIFile2);
        if (!localIFile2.equalsToPath(localIFile1)) {
          break;
        }
        FileChooserActivity.this.mHistory.remove(localIFile1);
      }
      if (localIFile1 == null) {
        FileChooserActivity.this.mViewGoForward.setEnabled(false);
      } else {
        FileChooserActivity.this.setLocation(localIFile1, new TaskListener()
        {
          public void onFinish(boolean paramAnonymous2Boolean, Object paramAnonymous2Object)
          {
            boolean bool = true;
            if (paramAnonymous2Boolean)
            {
              FileChooserActivity.this.mViewGoBack.setEnabled(bool);
              ImageView localImageView = FileChooserActivity.this.mViewGoForward;
              if (FileChooserActivity.this.mHistory.nextOf(FileChooserActivity.this.getLocation()) == null) {
                bool = false;
              }
              localImageView.setEnabled(bool);
              FileChooserActivity.this.mFullHistory.push((IFile)paramAnonymous2Object);
            }
          }
        });
      }
    }
  };
  private final View.OnClickListener mBtnLocationOnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      if ((paramAnonymousView.getTag() instanceof IFile)) {
        FileChooserActivity.this.goTo((IFile)paramAnonymousView.getTag());
      }
    }
  };
  private final View.OnLongClickListener mBtnLocationOnLongClickListener = new View.OnLongClickListener()
  {
    public boolean onLongClick(View paramAnonymousView)
    {
      if ((!IFileProvider.FilterMode.FilesOnly.equals(FileChooserActivity.this.mFileProvider.getFilterMode())) && (!FileChooserActivity.this.mIsSaveDialog))
      {
        FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
        IFile[] arrayOfIFile = new IFile[1];
        arrayOfIFile[0] = ((IFile)paramAnonymousView.getTag());
        localFileChooserActivity.doFinish(arrayOfIFile);
      }
      return false;
    }
  };
  private Button mBtnOk;
  private final View.OnClickListener mBtnOk_OpenDialog_OnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      ArrayList localArrayList = new ArrayList();
      for (int i = 0; i < ((ListAdapter)FileChooserActivity.this.mViewFiles.getAdapter()).getCount(); i++)
      {
        Object localObject = ((ListAdapter)FileChooserActivity.this.mViewFiles.getAdapter()).getItem(i);
        if ((localObject instanceof IFileDataModel))
        {
          localObject = (IFileDataModel)localObject;
          if (((IFileDataModel)localObject).isSelected()) {
            localArrayList.add(((IFileDataModel)localObject).getFile());
          }
        }
      }
      FileChooserActivity.this.doFinish((ArrayList)localArrayList);
    }
  };
  private final View.OnClickListener mBtnOk_SaveDialog_OnClickListener = new View.OnClickListener()
  {
    public void onClick(View paramAnonymousView)
    {
      Ui.hideSoftKeyboard(FileChooserActivity.this, FileChooserActivity.this.mTxtSaveas.getWindowToken());
      String str = FileChooserActivity.this.mTxtSaveas.getText().toString().trim();
      FileChooserActivity.this.doCheckSaveasFilenameAndFinish(str);
    }
  };
  private boolean mDoubleTapToChooseFiles;
  private IFileAdapter mFileAdapter;
  private IFileProvider mFileProvider;
  private Class<?> mFileProviderServiceClass;
  private TextView mFooterView;
  private History<IFile> mFullHistory;
  private History<IFile> mHistory;
  private boolean mIsMultiSelection;
  private boolean mIsSaveDialog;
  private GestureDetector mListviewFilesGestureDetector;
  private IFile mRoot;
  private ServiceConnection mServiceConnection;
  private final TextView.OnEditorActionListener mTxtFilenameOnEditorActionListener = new TextView.OnEditorActionListener()
  {
    public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
    {
      boolean bool;
      if (paramAnonymousInt != 6)
      {
        bool = false;
      }
      else
      {
        Ui.hideSoftKeyboard(FileChooserActivity.this, FileChooserActivity.this.mTxtSaveas.getWindowToken());
        FileChooserActivity.this.mBtnOk.performClick();
        bool = true;
      }
      return bool;
    }
  };
  private TextView mTxtFullDirName;
  private EditText mTxtSaveas;
  private AbsListView mViewFiles;
  private ViewGroup mViewFilesContainer;
  private final AdapterView.OnItemClickListener mViewFilesOnItemClickListener = new AdapterView.OnItemClickListener()
  {
    public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      IFileDataModel localIFileDataModel = FileChooserActivity.this.mFileAdapter.getItem(paramAnonymousInt);
      if (!localIFileDataModel.getFile().isDirectory())
      {
        if (FileChooserActivity.this.mIsSaveDialog) {
          FileChooserActivity.this.mTxtSaveas.setText(localIFileDataModel.getFile().getName());
        }
        if ((!FileChooserActivity.this.mDoubleTapToChooseFiles) && (!FileChooserActivity.this.mIsMultiSelection)) {
          if (!FileChooserActivity.this.mIsSaveDialog)
          {
            FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
            IFile[] arrayOfIFile = new IFile[1];
            arrayOfIFile[0] = localIFileDataModel.getFile();
            localFileChooserActivity.doFinish(arrayOfIFile);
          }
          else
          {
            FileChooserActivity.this.doCheckSaveasFilenameAndFinish(localIFileDataModel.getFile().getName());
          }
        }
      }
      else
      {
        FileChooserActivity.this.goTo(localIFileDataModel.getFile());
      }
    }
  };
  private final AdapterView.OnItemLongClickListener mViewFilesOnItemLongClickListener = new AdapterView.OnItemLongClickListener()
  {
    public boolean onItemLongClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
    {
      IFileDataModel localIFileDataModel = FileChooserActivity.this.mFileAdapter.getItem(paramAnonymousInt);
      if ((!FileChooserActivity.this.mDoubleTapToChooseFiles) && (!FileChooserActivity.this.mIsSaveDialog) && (!FileChooserActivity.this.mIsMultiSelection) && (localIFileDataModel.getFile().isDirectory()) && ((IFileProvider.FilterMode.DirectoriesOnly.equals(FileChooserActivity.this.mFileProvider.getFilterMode())) || (IFileProvider.FilterMode.FilesAndDirectories.equals(FileChooserActivity.this.mFileProvider.getFilterMode()))))
      {
        FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
        IFile[] arrayOfIFile = new IFile[1];
        arrayOfIFile[0] = localIFileDataModel.getFile();
        localFileChooserActivity.doFinish(arrayOfIFile);
      }
      return true;
    }
  };
  private ImageView mViewGoBack;
  private ImageView mViewGoForward;
  private ViewGroup mViewLocations;
  private HorizontalScrollView mViewLocationsContainer;
  
  static
  {
    _Rootpath = _ClassName + ".rootpath";
    _FileProviderClass = _ClassName + ".file_provider_class";
    _FilterMode = IFileProvider.FilterMode.class.getName();
    _MaxFileCount = _ClassName + ".max_file_count";
    _MultiSelection = _ClassName + ".multi_selection";
    _RegexFilenameFilter = _ClassName + ".regex_filename_filter";
    _DisplayHiddenFiles = _ClassName + ".display_hidden_files";
    _DoubleTapToChooseFiles = _ClassName + ".double_tap_to_choose_files";
    _SelectFile = _ClassName + ".select_file";
    _SaveDialog = _ClassName + ".save_dialog";
    _DefaultFilename = _ClassName + ".default_filename";
    _Results = _ClassName + ".results";
    _CurrentLocation = _ClassName + ".current_location";
    _History = _ClassName + ".history";
    _FullHistory = History.class.getName() + "_full";
    int[] arrayOfInt = new int[6];
    arrayOfInt[0] = R.id.afc_settings_sort_view_button_sort_by_name_asc;
    arrayOfInt[1] = R.id.afc_settings_sort_view_button_sort_by_name_desc;
    arrayOfInt[2] = R.id.afc_settings_sort_view_button_sort_by_size_asc;
    arrayOfInt[3] = R.id.afc_settings_sort_view_button_sort_by_size_desc;
    arrayOfInt[4] = R.id.afc_settings_sort_view_button_sort_by_date_asc;
    arrayOfInt[5] = R.id.afc_settings_sort_view_button_sort_by_date_desc;
    _BtnSortIds = arrayOfInt;
  }
  
  private void bindService(final Bundle paramBundle)
  {
    if (startService(new Intent(this, this.mFileProviderServiceClass)) != null)
    {
      this.mServiceConnection = new ServiceConnection()
      {
        public void onServiceConnected(ComponentName paramAnonymousComponentName, IBinder paramAnonymousIBinder)
        {
          try
          {
            FileChooserActivity.this.mFileProvider = ((FileProviderService.LocalBinder)paramAnonymousIBinder).getService();
            return;
          }
          catch (Throwable localThrowable)
          {
            for (;;)
            {
              Log.e(FileChooserActivity._ClassName, "mServiceConnection.onServiceConnected() -> " + localThrowable);
            }
          }
        }
        
        public void onServiceDisconnected(ComponentName paramAnonymousComponentName)
        {
          FileChooserActivity.this.mFileProvider = null;
        }
      };
      bindService(new Intent(this, this.mFileProviderServiceClass), this.mServiceConnection, 1);
      new LoadingDialog(this, R.string.afc_msg_loading, false)
      {
        private static final int _MaxWaitTime = 3000;
        private static final int _WaitTime = 200;
        
        protected Object doInBackground(Void... paramAnonymousVarArgs)
        {
          int i = 0;
          if (FileChooserActivity.this.mFileProvider != null) {}
          for (;;)
          {
            for (;;)
            {
              return null;
              i += 200;
              try
              {
                Thread.sleep(200L);
                if (i < 3000) {
                  break;
                }
              }
              catch (InterruptedException localInterruptedException) {}
            }
          }
        }
        
        protected void onPostExecute(Object paramAnonymousObject)
        {
          super.onPostExecute(paramAnonymousObject);
          if (FileChooserActivity.this.mFileProvider != null)
          {
            FileChooserActivity.this.setupService();
            FileChooserActivity.this.setupHeader();
            FileChooserActivity.this.setupViewFiles();
            FileChooserActivity.this.setupFooter();
            IFile localIFile1;
            if (paramBundle == null) {
              localIFile1 = null;
            } else {
              localIFile1 = (IFile)paramBundle.get(FileChooserActivity._CurrentLocation);
            }
            IFile localIFile2 = null;
            if (localIFile1 == null)
            {
              localIFile2 = (IFile)FileChooserActivity.this.getIntent().getParcelableExtra(FileChooserActivity._SelectFile);
              if ((localIFile2 != null) && (localIFile2.exists())) {
                localIFile1 = localIFile2.parentFile();
              }
              if (localIFile1 == null) {
                localIFile2 = null;
              }
            }
            if ((localIFile1 == null) && (DisplayPrefs.isRememberLastLocation(FileChooserActivity.this)))
            {
              localObject = DisplayPrefs.getLastLocation(FileChooserActivity.this);
              if (localObject != null) {
                localIFile1 = FileChooserActivity.this.mFileProvider.fromPath((String)localObject);
              }
            }
            Object localObject = localIFile2;
            FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
            if ((localIFile1 == null) || (!localIFile1.isDirectory())) {
              localIFile1 = FileChooserActivity.this.mRoot;
            }
            localFileChooserActivity.setLocation(localIFile1, new TaskListener()
            {
              public void onFinish(boolean paramAnonymous2Boolean, Object paramAnonymous2Object)
              {
                if ((paramAnonymous2Boolean) && (this.val$_selectedFile != null) && (this.val$_selectedFile.isFile()) && (FileChooserActivity.this.mIsSaveDialog)) {
                  FileChooserActivity.this.mTxtSaveas.setText(this.val$_selectedFile.getName());
                }
                int i;
                if ((this.val$savedInstanceState == null) || (!paramAnonymous2Object.equals(this.val$savedInstanceState.get(FileChooserActivity._CurrentLocation)))) {
                  i = 0;
                } else {
                  i = 1;
                }
                if (i == 0)
                {
                  FileChooserActivity.this.mHistory.push((IFile)paramAnonymous2Object);
                  FileChooserActivity.this.mFullHistory.push((IFile)paramAnonymous2Object);
                }
                else
                {
                  FileChooserActivity.this.mHistory.notifyHistoryChanged();
                }
              }
            }, localIFile2);
          }
          else
          {
            FileChooserActivity.this.doShowCannotConnectToServiceAndFinish();
          }
        }
      }.execute(new Void[0]);
    }
    else
    {
      doShowCannotConnectToServiceAndFinish();
    }
  }
  
  private void createIFileAdapter()
  {
    if (this.mFileAdapter != null) {
      this.mFileAdapter.clear();
    }
    this.mFileAdapter = new IFileAdapter(this, new ArrayList(), this.mFileProvider.getFilterMode(), this.mIsMultiSelection);
    if (!(this.mViewFiles instanceof ListView)) {
      ((GridView)this.mViewFiles).setAdapter(this.mFileAdapter);
    } else {
      ((ListView)this.mViewFiles).setAdapter(this.mFileAdapter);
    }
  }
  
  private void createLocationButtons(IFile paramIFile)
  {
    this.mViewLocations.setTag(paramIFile);
    this.mViewLocations.removeAllViews();
    LinearLayout.LayoutParams localLayoutParams2 = new LinearLayout.LayoutParams(-2, -2);
    localLayoutParams2.gravity = 17;
    LinearLayout.LayoutParams localLayoutParams1 = null;
    LayoutInflater localLayoutInflater = getLayoutInflater();
    int i = getResources().getDimensionPixelSize(R.dimen.afc_5dp);
    Rect localRect1;
    Rect localRect2;
    for (int j = 0; paramIFile != null; localRect2 = localRect1)
    {
      Object localObject = (TextView)localLayoutInflater.inflate(R.layout.afc_button_location, null);
      String str;
      if (paramIFile.parentFile() == null) {
        str = getString(R.string.afc_root);
      } else {
        str = paramIFile.getName();
      }
      ((TextView)localObject).setText(str);
      ((TextView)localObject).setTag(paramIFile);
      ((TextView)localObject).setOnClickListener(this.mBtnLocationOnClickListener);
      ((TextView)localObject).setOnLongClickListener(this.mBtnLocationOnLongClickListener);
      this.mViewLocations.addView((View)localObject, 0, localLayoutParams2);
      localRect1 = j + 1;
      if (j == 0)
      {
        localRect2 = new Rect();
        ((TextView)localObject).getPaint().getTextBounds(paramIFile.getName(), 0, paramIFile.getName().length(), localRect2);
        if (localRect2.width() < getResources().getDimensionPixelSize(R.dimen.afc_button_location_max_width) - ((TextView)localObject).getPaddingLeft() - ((TextView)localObject).getPaddingRight())
        {
          this.mTxtFullDirName.setVisibility(8);
        }
        else
        {
          this.mTxtFullDirName.setText(paramIFile.getName());
          this.mTxtFullDirName.setVisibility(0);
        }
      }
      paramIFile = paramIFile.parentFile();
      if (paramIFile != null)
      {
        localObject = localLayoutInflater.inflate(R.layout.afc_view_locations_divider, null);
        if (localLayoutParams1 == null)
        {
          localLayoutParams1 = new LinearLayout.LayoutParams(i, i);
          localLayoutParams1.gravity = 17;
          localLayoutParams1.setMargins(i, i, i, i);
        }
        this.mViewLocations.addView((View)localObject, 0, localLayoutParams1);
      }
    }
    this.mViewLocationsContainer.postDelayed(new Runnable()
    {
      public void run()
      {
        FileChooserActivity.this.mViewLocationsContainer.fullScroll(66);
      }
    }, 100L);
  }
  
  private void doCheckSaveasFilenameAndFinish(String paramString)
  {
    if (paramString.length() != 0)
    {
      Object localObject = this.mFileProvider.fromPath(getLocation().getAbsolutePath() + File.separator + paramString);
      if (FileUtils.isFilenameValid(paramString))
      {
        if (!((IFile)localObject).isFile())
        {
          if (!((IFile)localObject).isDirectory())
          {
            IFile[] arrayOfIFile = new IFile[1];
            arrayOfIFile[0] = localObject;
            doFinish(arrayOfIFile);
          }
          else
          {
            int i = R.string.afc_pmsg_filename_is_directory;
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = ((IFile)localObject).getName();
            Dlg.toast(this, getString(i, arrayOfObject2), 0);
          }
        }
        else
        {
          int k = R.string.afc_pmsg_confirm_replace_file;
          Object[] arrayOfObject1 = new Object[1];
          arrayOfObject1[0] = ((IFile)localObject).getName();
          Dlg.confirmYesno(this, getString(k, arrayOfObject1), new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
            {
              FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
              IFile[] arrayOfIFile = new IFile[1];
              arrayOfIFile[0] = this.val$_file;
              localFileChooserActivity.doFinish(arrayOfIFile);
            }
          });
        }
      }
      else
      {
        int j = R.string.afc_pmsg_filename_is_invalid;
        localObject = new Object[1];
        localObject[0] = paramString;
        Dlg.toast(this, getString(j, (Object[])localObject), 0);
      }
    }
    else
    {
      Dlg.toast(this, R.string.afc_msg_filename_is_empty, 0);
    }
  }
  
  private void doCreateNewDir()
  {
    Object localObject1;
    if ((this.mFileProvider instanceof LocalFileProvider))
    {
      localObject1 = new String[1];
      localObject1[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
      if (!Utils.hasPermissions(this, (String[])localObject1)) {}
    }
    else
    {
      final AlertDialog localAlertDialog = Dlg.newDlg(this);
      Object localObject2 = getLayoutInflater().inflate(R.layout.afc_simple_text_input_view, null);
      localObject1 = (EditText)((View)localObject2).findViewById(R.id.afc_simple_text_input_view_text1);
      ((EditText)localObject1).setHint(R.string.afc_hint_folder_name);
      ((EditText)localObject1).setOnEditorActionListener(new TextView.OnEditorActionListener()
      {
        public boolean onEditorAction(TextView paramAnonymousTextView, int paramAnonymousInt, KeyEvent paramAnonymousKeyEvent)
        {
          boolean bool;
          if (paramAnonymousInt != 6)
          {
            bool = false;
          }
          else
          {
            Ui.hideSoftKeyboard(FileChooserActivity.this, this.val$_textFile.getWindowToken());
            localAlertDialog.getButton(-1).performClick();
            bool = true;
          }
          return bool;
        }
      });
      localAlertDialog.setView((View)localObject2);
      localAlertDialog.setTitle(R.string.afc_cmd_new_folder);
      localAlertDialog.setIcon(17301555);
      localAlertDialog.setButton(-1, getString(17039370), new DialogInterface.OnClickListener()
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          String str = this.val$_textFile.getText().toString().trim();
          FileChooserActivity localFileChooserActivity;
          Object localObject2;
          if (FileUtils.isFilenameValid(str))
          {
            Object localObject1 = FileChooserActivity.this.mFileProvider;
            Object[] arrayOfObject1 = new Object[2];
            arrayOfObject1[0] = FileChooserActivity.this.getLocation().getAbsolutePath();
            arrayOfObject1[1] = str;
            if (!((IFileProvider)localObject1).fromPath(String.format("%s/%s", arrayOfObject1)).mkdir())
            {
              localObject1 = FileChooserActivity.this;
              localFileChooserActivity = FileChooserActivity.this;
              int i = R.string.afc_pmsg_cannot_create_folder;
              localObject2 = new Object[1];
              localObject2[0] = str;
              Dlg.toast((Context)localObject1, localFileChooserActivity.getString(i, (Object[])localObject2), 0);
            }
            else
            {
              Dlg.toast(FileChooserActivity.this, FileChooserActivity.this.getString(R.string.afc_msg_done), 0);
              FileChooserActivity.this.setLocation(FileChooserActivity.access$0(FileChooserActivity.this), null);
            }
          }
          else
          {
            localFileChooserActivity = FileChooserActivity.this;
            localObject2 = FileChooserActivity.this;
            int j = R.string.afc_pmsg_filename_is_invalid;
            Object[] arrayOfObject2 = new Object[1];
            arrayOfObject2[0] = str;
            Dlg.toast(localFileChooserActivity, ((FileChooserActivity)localObject2).getString(j, arrayOfObject2), 0);
          }
        }
      });
      localAlertDialog.show();
      localObject2 = localAlertDialog.getButton(-1);
      ((Button)localObject2).setEnabled(false);
      ((EditText)localObject1).addTextChangedListener(new TextWatcher()
      {
        public void afterTextChanged(Editable paramAnonymousEditable)
        {
          this.val$_btnOk.setEnabled(FileUtils.isFilenameValid(paramAnonymousEditable.toString().trim()));
        }
        
        public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
        
        public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {}
      });
      return;
    }
    Dlg.toast(this, R.string.afc_msg_app_doesnot_have_permission_to_create_files, 0);
  }
  
  private void doDeleteFile(final IFileDataModel paramIFileDataModel)
  {
    if ((this.mFileProvider instanceof LocalFileProvider))
    {
      String[] arrayOfString = new String[1];
      arrayOfString[0] = "android.permission.WRITE_EXTERNAL_STORAGE";
      if (!Utils.hasPermissions(this, arrayOfString)) {}
    }
    else
    {
      int i = R.string.afc_pmsg_confirm_delete_file;
      Object[] arrayOfObject = new Object[2];
      String str;
      if (!paramIFileDataModel.getFile().isFile()) {
        str = getString(R.string.afc_folder);
      } else {
        str = getString(R.string.afc_file);
      }
      arrayOfObject[0] = str;
      arrayOfObject[1] = paramIFileDataModel.getFile().getName();
      Dlg.confirmYesno(this, getString(i, arrayOfObject), new DialogInterface.OnClickListener()new DialogInterface.OnCancelListener
      {
        public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          FileChooserActivity localFileChooserActivity2 = FileChooserActivity.this;
          FileChooserActivity localFileChooserActivity1 = FileChooserActivity.this;
          int i = R.string.afc_pmsg_deleting_file;
          Object[] arrayOfObject = new Object[2];
          String str;
          if (!paramIFileDataModel.getFile().isFile()) {
            str = FileChooserActivity.this.getString(R.string.afc_folder);
          } else {
            str = FileChooserActivity.this.getString(R.string.afc_file);
          }
          arrayOfObject[0] = str;
          arrayOfObject[1] = paramIFileDataModel.getFile().getName();
          new LoadingDialog(localFileChooserActivity2, localFileChooserActivity1.getString(i, arrayOfObject), true)
          {
            private final boolean _isFile;
            private Thread mThread;
            
            private void notifyFileDeleted()
            {
              FileChooserActivity.this.mFileAdapter.remove(this.val$data);
              FileChooserActivity.this.mFileAdapter.notifyDataSetChanged();
              FileChooserActivity.this.refreshHistories();
              FileChooserActivity localFileChooserActivity2 = FileChooserActivity.this;
              FileChooserActivity localFileChooserActivity1 = FileChooserActivity.this;
              int i = R.string.afc_pmsg_file_has_been_deleted;
              Object[] arrayOfObject = new Object[2];
              String str;
              if (!this._isFile) {
                str = FileChooserActivity.this.getString(R.string.afc_folder);
              } else {
                str = FileChooserActivity.this.getString(R.string.afc_file);
              }
              arrayOfObject[0] = str;
              arrayOfObject[1] = this.val$data.getFile().getName();
              Dlg.toast(localFileChooserActivity2, localFileChooserActivity1.getString(i, arrayOfObject), 0);
            }
            
            protected Object doInBackground(Void... paramAnonymous2VarArgs)
            {
              for (;;)
              {
                if (!this.mThread.isAlive()) {
                  return null;
                }
                try
                {
                  this.mThread.join(10L);
                }
                catch (InterruptedException localInterruptedException)
                {
                  this.mThread.interrupt();
                }
              }
            }
            
            protected void onCancelled()
            {
              this.mThread.interrupt();
              if (!this.val$data.getFile().exists())
              {
                notifyFileDeleted();
              }
              else
              {
                FileChooserActivity.this.notifyDataModelNotDeleted(this.val$data);
                Dlg.toast(FileChooserActivity.this, R.string.afc_msg_cancelled, 0);
              }
              super.onCancelled();
            }
            
            protected void onPostExecute(Object paramAnonymous2Object)
            {
              super.onPostExecute(paramAnonymous2Object);
              if (!this.val$data.getFile().exists())
              {
                notifyFileDeleted();
              }
              else
              {
                FileChooserActivity.this.notifyDataModelNotDeleted(this.val$data);
                FileChooserActivity localFileChooserActivity1 = FileChooserActivity.this;
                FileChooserActivity localFileChooserActivity2 = FileChooserActivity.this;
                int i = R.string.afc_pmsg_cannot_delete_file;
                Object[] arrayOfObject = new Object[2];
                String str;
                if (!this.val$data.getFile().isFile()) {
                  str = FileChooserActivity.this.getString(R.string.afc_folder);
                } else {
                  str = FileChooserActivity.this.getString(R.string.afc_file);
                }
                arrayOfObject[0] = str;
                arrayOfObject[1] = this.val$data.getFile().getName();
                Dlg.toast(localFileChooserActivity1, localFileChooserActivity2.getString(i, arrayOfObject), 0);
              }
            }
            
            protected void onPreExecute()
            {
              super.onPreExecute();
              this.mThread.start();
            }
          }.execute(new Void[0]);
        }
      }, new DialogInterface.OnCancelListener()
      {
        public void onCancel(DialogInterface paramAnonymousDialogInterface)
        {
          FileChooserActivity.this.notifyDataModelNotDeleted(paramIFileDataModel);
        }
      });
      return;
    }
    notifyDataModelNotDeleted(paramIFileDataModel);
    Dlg.toast(this, R.string.afc_msg_app_doesnot_have_permission_to_delete_files, 0);
  }
  
  private void doFinish(ArrayList<IFile> paramArrayList)
  {
    if ((paramArrayList != null) && (!paramArrayList.isEmpty()))
    {
      Intent localIntent = new Intent();
      localIntent.putExtra(_Results, paramArrayList);
      localIntent.putExtra(_FilterMode, this.mFileProvider.getFilterMode());
      localIntent.putExtra(_SaveDialog, this.mIsSaveDialog);
      setResult(-1, localIntent);
      if ((!DisplayPrefs.isRememberLastLocation(this)) || (getLocation() == null)) {
        DisplayPrefs.setLastLocation(this, null);
      } else {
        DisplayPrefs.setLastLocation(this, getLocation().getAbsolutePath());
      }
      finish();
    }
    else
    {
      setResult(0);
      finish();
    }
  }
  
  private void doFinish(IFile... paramVarArgs)
  {
    ArrayList localArrayList = new ArrayList();
    int i = paramVarArgs.length;
    for (int j = 0; j < i; j++) {
      localArrayList.add(paramVarArgs[j]);
    }
    doFinish((ArrayList)localArrayList);
  }
  
  private void doGoHome()
  {
    goTo(this.mRoot.clone());
  }
  
  private void doReloadCurrentLocation()
  {
    setLocation(getLocation(), null);
  }
  
  private void doResortViewFiles()
  {
    final AlertDialog localAlertDialog = Dlg.newDlg(this);
    int j = 0;
    switch (DisplayPrefs.getSortType(this))
    {
    case SortByDate: 
      j = 0;
      break;
    case SortByName: 
      j = 2;
      break;
    case SortBySize: 
      j = 4;
    }
    if (!DisplayPrefs.isSortAscending(this)) {
      j++;
    }
    View.OnClickListener local18 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        localAlertDialog.dismiss();
        FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
        if (paramAnonymousView.getId() != R.id.afc_settings_sort_view_button_sort_by_name_asc)
        {
          if (paramAnonymousView.getId() != R.id.afc_settings_sort_view_button_sort_by_name_desc)
          {
            if (paramAnonymousView.getId() != R.id.afc_settings_sort_view_button_sort_by_size_asc)
            {
              if (paramAnonymousView.getId() != R.id.afc_settings_sort_view_button_sort_by_size_desc)
              {
                if (paramAnonymousView.getId() != R.id.afc_settings_sort_view_button_sort_by_date_asc)
                {
                  if (paramAnonymousView.getId() == R.id.afc_settings_sort_view_button_sort_by_date_desc)
                  {
                    DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortByDate);
                    DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(false));
                  }
                }
                else
                {
                  DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortByDate);
                  DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(true));
                }
              }
              else
              {
                DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortBySize);
                DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(false));
              }
            }
            else
            {
              DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortBySize);
              DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(true));
            }
          }
          else
          {
            DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortByName);
            DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(false));
          }
        }
        else
        {
          DisplayPrefs.setSortType(localFileChooserActivity, IFileProvider.SortType.SortByName);
          DisplayPrefs.setSortAscending(localFileChooserActivity, Boolean.valueOf(true));
        }
        FileChooserActivity.this.resortViewFiles();
      }
    };
    View localView = getLayoutInflater().inflate(R.layout.afc_settings_sort_view, null);
    for (int i = 0; i < _BtnSortIds.length; i++)
    {
      Button localButton = (Button)localView.findViewById(_BtnSortIds[i]);
      localButton.setOnClickListener(local18);
      if (i == j)
      {
        localButton.setEnabled(false);
        if (Build.VERSION.SDK_INT >= 11) {
          localButton.setText(R.string.afc_ellipsize);
        }
      }
    }
    localAlertDialog.setTitle(R.string.afc_title_sort_by);
    localAlertDialog.setView(localView);
    localAlertDialog.show();
  }
  
  private void doShowCannotConnectToServiceAndFinish()
  {
    Dlg.showError(this, R.string.afc_msg_cannot_connect_to_file_provider_service, new DialogInterface.OnCancelListener()
    {
      public void onCancel(DialogInterface paramAnonymousDialogInterface)
      {
        FileChooserActivity.this.setResult(0);
        FileChooserActivity.this.finish();
      }
    });
  }
  
  private void doSwitchViewType()
  {
    new LoadingDialog(this, R.string.afc_msg_loading, false)
    {
      protected Object doInBackground(Void... paramAnonymousVarArgs)
      {
        return null;
      }
      
      protected void onPreExecute()
      {
        super.onPreExecute();
        switch (DisplayPrefs.getViewType(FileChooserActivity.this))
        {
        case Grid: 
          DisplayPrefs.setViewType(FileChooserActivity.this, FileChooserActivity.ViewType.Grid);
          break;
        case List: 
          DisplayPrefs.setViewType(FileChooserActivity.this, FileChooserActivity.ViewType.List);
        }
        FileChooserActivity.this.setupViewFiles();
        if (Build.VERSION.SDK_INT >= 11) {
          ActivityCompat.invalidateOptionsMenu(FileChooserActivity.this);
        }
        FileChooserActivity.this.doReloadCurrentLocation();
      }
    }.execute(new Void[0]);
  }
  
  private IFile getLocation()
  {
    return (IFile)this.mViewLocations.getTag();
  }
  
  private boolean goTo(final IFile paramIFile)
  {
    boolean bool;
    if (!paramIFile.equalsToPath(getLocation()))
    {
      setLocation(paramIFile, new TaskListener()
      {
        IFile mLastPath = FileChooserActivity.this.getLocation();
        
        public void onFinish(boolean paramAnonymousBoolean, Object paramAnonymousObject)
        {
          if (paramAnonymousBoolean)
          {
            FileChooserActivity.this.mHistory.truncateAfter(this.mLastPath);
            FileChooserActivity.this.mHistory.push(paramIFile);
            FileChooserActivity.this.mFullHistory.push(paramIFile);
          }
        }
      });
      bool = true;
    }
    else
    {
      bool = false;
    }
    return bool;
  }
  
  private void initGestureDetector()
  {
    this.mListviewFilesGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener()
    {
      private Object getData(float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        int i = getSubViewId(paramAnonymousFloat1, paramAnonymousFloat2);
        Object localObject;
        if (i < 0) {
          localObject = null;
        } else {
          localObject = FileChooserActivity.this.mViewFiles.getItemAtPosition(localObject + FileChooserActivity.this.mViewFiles.getFirstVisiblePosition());
        }
        return localObject;
      }
      
      private IFileDataModel getDataModel(MotionEvent paramAnonymousMotionEvent)
      {
        Object localObject = getData(paramAnonymousMotionEvent.getX(), paramAnonymousMotionEvent.getY());
        if (!(localObject instanceof IFileDataModel)) {
          localObject = null;
        } else {
          localObject = (IFileDataModel)localObject;
        }
        return localObject;
      }
      
      private int getSubViewId(float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        Rect localRect = new Rect();
        for (int i = 0; i < FileChooserActivity.this.mViewFiles.getChildCount(); i++)
        {
          FileChooserActivity.this.mViewFiles.getChildAt(i).getHitRect(localRect);
          if (localRect.contains((int)paramAnonymousFloat1, (int)paramAnonymousFloat2)) {
            break label63;
          }
        }
        i = -1;
        label63:
        return i;
      }
      
      public boolean onDoubleTap(MotionEvent paramAnonymousMotionEvent)
      {
        boolean bool = false;
        if ((FileChooserActivity.this.mDoubleTapToChooseFiles) && (!FileChooserActivity.this.mIsMultiSelection))
        {
          IFileDataModel localIFileDataModel = getDataModel(paramAnonymousMotionEvent);
          if ((localIFileDataModel != null) && ((!localIFileDataModel.getFile().isDirectory()) || (!IFileProvider.FilterMode.FilesOnly.equals(FileChooserActivity.this.mFileProvider.getFilterMode()))))
          {
            if (!FileChooserActivity.this.mIsSaveDialog)
            {
              FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
              IFile[] arrayOfIFile = new IFile[1];
              arrayOfIFile[bool] = localIFileDataModel.getFile();
              localFileChooserActivity.doFinish(arrayOfIFile);
            }
            else
            {
              if (!localIFileDataModel.getFile().isFile()) {
                return bool;
              }
              FileChooserActivity.this.mTxtSaveas.setText(localIFileDataModel.getFile().getName());
              FileChooserActivity.this.doCheckSaveasFilenameAndFinish(localIFileDataModel.getFile().getName());
            }
            bool = true;
          }
        }
        return bool;
      }
      
      public boolean onFling(MotionEvent paramAnonymousMotionEvent1, MotionEvent paramAnonymousMotionEvent2, float paramAnonymousFloat1, float paramAnonymousFloat2)
      {
        if ((Math.abs(paramAnonymousMotionEvent1.getY() - paramAnonymousMotionEvent2.getY()) < 19.0F) && (Math.abs(paramAnonymousMotionEvent1.getX() - paramAnonymousMotionEvent2.getX()) > 80.0F) && (Math.abs(paramAnonymousFloat1) > 200.0F))
        {
          Object localObject = getData(paramAnonymousMotionEvent1.getX(), paramAnonymousMotionEvent1.getY());
          if ((localObject instanceof IFileDataModel))
          {
            ((IFileDataModel)localObject).setTobeDeleted(true);
            FileChooserActivity.this.mFileAdapter.notifyDataSetChanged();
            FileChooserActivity.this.doDeleteFile((IFileDataModel)localObject);
          }
        }
        return false;
      }
      
      public void onLongPress(MotionEvent paramAnonymousMotionEvent) {}
      
      public boolean onSingleTapConfirmed(MotionEvent paramAnonymousMotionEvent)
      {
        return false;
      }
    });
  }
  
  private void notifyDataModelNotDeleted(IFileDataModel paramIFileDataModel)
  {
    paramIFileDataModel.setTobeDeleted(false);
    this.mFileAdapter.notifyDataSetChanged();
  }
  
  private void refreshHistories()
  {
    HistoryFilter local29 = new HistoryFilter()
    {
      public boolean accept(IFile paramAnonymousIFile)
      {
        boolean bool;
        if (!paramAnonymousIFile.isDirectory()) {
          bool = true;
        } else {
          bool = false;
        }
        return bool;
      }
    };
    this.mHistory.removeAll(local29);
    this.mFullHistory.removeAll(local29);
  }
  
  private void resortViewFiles()
  {
    if ((!this.mFileProvider.getSortType().equals(DisplayPrefs.getSortType(this))) || (this.mFileProvider.getSortOrder().isAsc() != DisplayPrefs.isSortAscending(this)))
    {
      this.mFileProvider.setSortType(DisplayPrefs.getSortType(this));
      IFileProvider localIFileProvider = this.mFileProvider;
      IFileProvider.SortOrder localSortOrder;
      if (!DisplayPrefs.isSortAscending(this)) {
        localSortOrder = IFileProvider.SortOrder.Descending;
      } else {
        localSortOrder = IFileProvider.SortOrder.Ascending;
      }
      localIFileProvider.setSortOrder(localSortOrder);
      doReloadCurrentLocation();
      if (Build.VERSION.SDK_INT >= 11) {
        ActivityCompat.invalidateOptionsMenu(this);
      }
    }
  }
  
  private void setLocation(IFile paramIFile, TaskListener paramTaskListener)
  {
    setLocation(paramIFile, paramTaskListener, null);
  }
  
  private void setLocation(final IFile paramIFile1, final TaskListener paramTaskListener, final IFile paramIFile2)
  {
    new LoadingDialog(this, R.string.afc_msg_loading, true)
    {
      List<IFile> files;
      boolean hasMoreFiles = false;
      String mLastPath;
      int shouldBeSelectedIdx = -1;
      
      protected Object doInBackground(Void... paramAnonymousVarArgs)
      {
        for (;;)
        {
          int j;
          try
          {
            if ((paramIFile1.isDirectory()) && (paramIFile1.canRead()))
            {
              this.files = new ArrayList();
              FileChooserActivity.this.mFileProvider.listAllFiles(paramIFile1, new IFileFilter()
              {
                public boolean accept(IFile paramAnonymous2IFile)
                {
                  if (FileChooserActivity.this.mFileProvider.accept(paramAnonymous2IFile)) {
                    if (FileChooserActivity.26.this.files.size() >= FileChooserActivity.this.mFileProvider.getMaxFileCount()) {
                      FileChooserActivity.26.this.hasMoreFiles = true;
                    } else {
                      FileChooserActivity.26.this.files.add(paramAnonymous2IFile);
                    }
                  }
                  return false;
                }
              });
              if (this.files == null) {
                break label316;
              }
              Collections.sort(this.files, new FileComparator(FileChooserActivity.this.mFileProvider.getSortType(), FileChooserActivity.this.mFileProvider.getSortOrder()));
              if ((paramIFile2 == null) || (!paramIFile2.exists()) || (!paramIFile2.parentFile().equalsToPath(paramIFile1))) {
                continue;
              }
              int i = 0;
              if (i >= this.files.size()) {
                break label316;
              }
            }
            else
            {
              this.files = null;
              continue;
            }
            IFile localIFile;
            return null;
          }
          catch (Throwable localThrowable)
          {
            setLastException(localThrowable);
            cancel(false);
            break label316;
            if (((IFile)this.files.get(localThrowable)).equalsToPath(paramIFile2))
            {
              this.shouldBeSelectedIdx = localThrowable;
              break label316;
              if ((this.mLastPath != null) && (this.mLastPath.length() >= paramIFile1.getAbsolutePath().length()))
              {
                j = 0;
                if (j < this.files.size())
                {
                  localIFile = (IFile)this.files.get(j);
                  if ((localIFile.isDirectory()) && (this.mLastPath.startsWith(localIFile.getAbsolutePath())))
                  {
                    this.shouldBeSelectedIdx = j;
                  }
                  else
                  {
                    j++;
                    continue;
                  }
                }
              }
            }
          }
          label316:
          j++;
        }
      }
      
      protected void onCancelled()
      {
        super.onCancelled();
        Dlg.toast(FileChooserActivity.this, R.string.afc_msg_cancelled, 0);
      }
      
      protected void onPostExecute(Object paramAnonymousObject)
      {
        super.onPostExecute(paramAnonymousObject);
        Object localObject2;
        Object localObject1;
        if (this.files != null)
        {
          FileChooserActivity.this.createIFileAdapter();
          localObject2 = this.files.iterator();
          while (((Iterator)localObject2).hasNext())
          {
            IFile localIFile = (IFile)((Iterator)localObject2).next();
            FileChooserActivity.this.mFileAdapter.add(new IFileDataModel(localIFile));
          }
          FileChooserActivity.this.mFileAdapter.notifyDataSetChanged();
          localObject2 = FileChooserActivity.this.mFooterView;
          int i;
          if ((!this.hasMoreFiles) && (!FileChooserActivity.this.mFileAdapter.isEmpty())) {
            i = 8;
          } else {
            i = 0;
          }
          ((TextView)localObject2).setVisibility(i);
          if (!this.hasMoreFiles)
          {
            if (FileChooserActivity.this.mFileAdapter.isEmpty()) {
              FileChooserActivity.this.mFooterView.setText(R.string.afc_msg_empty);
            }
          }
          else
          {
            localObject2 = FileChooserActivity.this.mFooterView;
            localObject1 = FileChooserActivity.this;
            int k = R.string.afc_pmsg_max_file_count_allowed;
            Object[] arrayOfObject = new Object[1];
            arrayOfObject[0] = Integer.valueOf(FileChooserActivity.this.mFileProvider.getMaxFileCount());
            ((TextView)localObject2).setText(((FileChooserActivity)localObject1).getString(k, arrayOfObject));
          }
          FileChooserActivity.this.mViewFiles.post(new Runnable()
          {
            public void run()
            {
              if ((FileChooserActivity.26.this.shouldBeSelectedIdx < 0) || (FileChooserActivity.26.this.shouldBeSelectedIdx >= FileChooserActivity.this.mFileAdapter.getCount()))
              {
                if (!FileChooserActivity.this.mFileAdapter.isEmpty()) {
                  FileChooserActivity.this.mViewFiles.setSelection(0);
                }
              }
              else {
                FileChooserActivity.this.mViewFiles.setSelection(FileChooserActivity.26.this.shouldBeSelectedIdx);
              }
            }
          });
          FileChooserActivity.this.createLocationButtons(paramIFile1);
          if (paramTaskListener != null) {
            paramTaskListener.onFinish(true, paramIFile1);
          }
        }
        else
        {
          FileChooserActivity localFileChooserActivity = FileChooserActivity.this;
          localObject2 = FileChooserActivity.this;
          int j = R.string.afc_pmsg_cannot_access_dir;
          localObject1 = new Object[1];
          localObject1[0] = paramIFile1.getName();
          Dlg.toast(localFileChooserActivity, ((FileChooserActivity)localObject2).getString(j, (Object[])localObject1), 0);
          if (paramTaskListener != null) {
            paramTaskListener.onFinish(false, paramIFile1);
          }
        }
      }
    }.execute(new Void[0]);
  }
  
  private void setupFooter()
  {
    Object localObject2 = (ViewGroup)findViewById(R.id.afc_filechooser_activity_viewgroup_footer_container);
    Object localObject1 = (ViewGroup)findViewById(R.id.afc_filechooser_activity_viewgroup_footer);
    if (!this.mIsSaveDialog)
    {
      if (this.mIsMultiSelection)
      {
        ((ViewGroup)localObject2).setVisibility(0);
        ((ViewGroup)localObject1).setVisibility(0);
        localObject2 = ((ViewGroup)localObject1).getLayoutParams();
        ((ViewGroup.LayoutParams)localObject2).width = -2;
        ((ViewGroup)localObject1).setLayoutParams((ViewGroup.LayoutParams)localObject2);
        this.mBtnOk.setMinWidth(getResources().getDimensionPixelSize(R.dimen.afc_single_button_min_width));
        this.mBtnOk.setText(17039370);
        this.mBtnOk.setVisibility(0);
        this.mBtnOk.setOnClickListener(this.mBtnOk_OpenDialog_OnClickListener);
      }
    }
    else
    {
      ((ViewGroup)localObject2).setVisibility(0);
      ((ViewGroup)localObject1).setVisibility(0);
      this.mTxtSaveas.setVisibility(0);
      this.mTxtSaveas.setText(getIntent().getStringExtra(_DefaultFilename));
      this.mTxtSaveas.setOnEditorActionListener(this.mTxtFilenameOnEditorActionListener);
      this.mBtnOk.setVisibility(0);
      this.mBtnOk.setOnClickListener(this.mBtnOk_SaveDialog_OnClickListener);
      this.mBtnOk.setBackgroundResource(R.drawable.afc_selector_button_ok_saveas);
      int i = getResources().getDimensionPixelSize(R.dimen.afc_button_ok_saveas_size);
      localObject1 = (LinearLayout.LayoutParams)this.mBtnOk.getLayoutParams();
      ((LinearLayout.LayoutParams)localObject1).width = i;
      ((LinearLayout.LayoutParams)localObject1).height = i;
      this.mBtnOk.setLayoutParams((ViewGroup.LayoutParams)localObject1);
    }
  }
  
  private void setupHeader()
  {
    int j = 0;
    if (!this.mIsSaveDialog) {
      switch (this.mFileProvider.getFilterMode())
      {
      default: 
        break;
      case DirectoriesOnly: 
        setTitle(R.string.afc_title_choose_files);
        break;
      case FilesAndDirectories: 
        setTitle(R.string.afc_title_choose_directories);
        break;
      case FilesOnly: 
        setTitle(R.string.afc_title_choose_files_and_directories);
        break;
      }
    } else {
      setTitle(R.string.afc_title_save_as);
    }
    this.mViewGoBack.setEnabled(false);
    this.mViewGoBack.setOnClickListener(this.mBtnGoBackOnClickListener);
    this.mViewGoForward.setEnabled(false);
    this.mViewGoForward.setOnClickListener(this.mBtnGoForwardOnClickListener);
    ImageView[] arrayOfImageView = new ImageView[2];
    arrayOfImageView[j] = this.mViewGoBack;
    arrayOfImageView[1] = this.mViewGoForward;
    int i = arrayOfImageView.length;
    while (j < i)
    {
      arrayOfImageView[j].setOnLongClickListener(this.mBtnGoBackForwardOnLongClickListener);
      j++;
    }
  }
  
  private void setupService()
  {
    if (getIntent().getParcelableExtra(_Rootpath) != null) {
      this.mRoot = ((IFile)getIntent().getSerializableExtra(_Rootpath));
    }
    if ((this.mRoot == null) || (!this.mRoot.isDirectory())) {
      this.mRoot = this.mFileProvider.defaultPath();
    }
    Object localObject1 = (IFileProvider.FilterMode)getIntent().getSerializableExtra(_FilterMode);
    if (localObject1 == null) {
      localObject1 = IFileProvider.FilterMode.FilesOnly;
    }
    IFileProvider.SortType localSortType = DisplayPrefs.getSortType(this);
    boolean bool = DisplayPrefs.isSortAscending(this);
    this.mFileProvider.setDisplayHiddenFiles(getIntent().getBooleanExtra(_DisplayHiddenFiles, false));
    Object localObject2 = this.mFileProvider;
    if (this.mIsSaveDialog) {
      localObject1 = IFileProvider.FilterMode.FilesOnly;
    }
    ((IFileProvider)localObject2).setFilterMode((IFileProvider.FilterMode)localObject1);
    this.mFileProvider.setMaxFileCount(getIntent().getIntExtra(_MaxFileCount, 1024));
    localObject1 = this.mFileProvider;
    if (!this.mIsSaveDialog) {
      localObject2 = getIntent().getStringExtra(_RegexFilenameFilter);
    } else {
      localObject2 = null;
    }
    ((IFileProvider)localObject1).setRegexFilenameFilter((String)localObject2);
    localObject1 = this.mFileProvider;
    IFileProvider.SortOrder localSortOrder;
    if (!bool) {
      localSortOrder = IFileProvider.SortOrder.Descending;
    } else {
      localSortOrder = IFileProvider.SortOrder.Ascending;
    }
    ((IFileProvider)localObject1).setSortOrder(localSortOrder);
    this.mFileProvider.setSortType(localSortType);
  }
  
  private void setupViewFiles()
  {
    switch (DisplayPrefs.getViewType(this))
    {
    case Grid: 
      this.mViewFiles = ((AbsListView)getLayoutInflater().inflate(R.layout.afc_listview_files, null));
      break;
    case List: 
      this.mViewFiles = ((AbsListView)getLayoutInflater().inflate(R.layout.afc_gridview_files, null));
    }
    this.mViewFilesContainer.removeAllViews();
    this.mViewFilesContainer.addView(this.mViewFiles, new LinearLayout.LayoutParams(-1, -1, 1.0F));
    this.mViewFiles.setOnItemClickListener(this.mViewFilesOnItemClickListener);
    this.mViewFiles.setOnItemLongClickListener(this.mViewFilesOnItemLongClickListener);
    this.mViewFiles.setOnTouchListener(new View.OnTouchListener()
    {
      public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent)
      {
        return FileChooserActivity.this.mListviewFilesGestureDetector.onTouchEvent(paramAnonymousMotionEvent);
      }
    });
    createIFileAdapter();
    this.mFooterView.setOnLongClickListener(new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        E.show(FileChooserActivity.this);
        return false;
      }
    });
  }
  
  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
  }
  
  public void onCreate(Bundle paramBundle)
  {
    if (getIntent().hasExtra(_Theme))
    {
      int i;
      if (Build.VERSION.SDK_INT < 14)
      {
        if (Build.VERSION.SDK_INT < 11) {
          i = getIntent().getIntExtra(_Theme, 16973829);
        } else {
          i = getIntent().getIntExtra(_Theme, 16973931);
        }
      }
      else {
        i = getIntent().getIntExtra(_Theme, 16974120);
      }
      setTheme(i);
    }
    super.onCreate(paramBundle);
    setContentView(R.layout.afc_file_chooser);
    initGestureDetector();
    this.mFileProviderServiceClass = ((Class)getIntent().getSerializableExtra(_FileProviderClass));
    if (this.mFileProviderServiceClass == null) {
      this.mFileProviderServiceClass = LocalFileProvider.class;
    }
    this.mIsMultiSelection = getIntent().getBooleanExtra(_MultiSelection, false);
    this.mIsSaveDialog = getIntent().getBooleanExtra(_SaveDialog, false);
    if (this.mIsSaveDialog) {
      this.mIsMultiSelection = false;
    }
    this.mDoubleTapToChooseFiles = getIntent().getBooleanExtra(_DoubleTapToChooseFiles, false);
    this.mViewGoBack = ((ImageView)findViewById(R.id.afc_filechooser_activity_button_go_back));
    this.mViewGoForward = ((ImageView)findViewById(R.id.afc_filechooser_activity_button_go_forward));
    this.mViewLocations = ((ViewGroup)findViewById(R.id.afc_filechooser_activity_view_locations));
    this.mViewLocationsContainer = ((HorizontalScrollView)findViewById(R.id.afc_filechooser_activity_view_locations_container));
    this.mTxtFullDirName = ((TextView)findViewById(R.id.afc_filechooser_activity_textview_full_dir_name));
    this.mViewFilesContainer = ((ViewGroup)findViewById(R.id.afc_filechooser_activity_view_files_container));
    this.mFooterView = ((TextView)findViewById(R.id.afc_filechooser_activity_view_files_footer_view));
    this.mTxtSaveas = ((EditText)findViewById(R.id.afc_filechooser_activity_textview_saveas_filename));
    this.mBtnOk = ((Button)findViewById(R.id.afc_filechooser_activity_button_ok));
    if ((paramBundle == null) || (!(paramBundle.get(_History) instanceof HistoryStore))) {
      this.mHistory = new HistoryStore(51);
    } else {
      this.mHistory = ((History)paramBundle.getParcelable(_History));
    }
    this.mHistory.addListener(new HistoryListener()
    {
      public void onChanged(History<IFile> paramAnonymousHistory)
      {
        boolean bool1 = true;
        int i = paramAnonymousHistory.indexOf(FileChooserActivity.this.getLocation());
        ImageView localImageView2 = FileChooserActivity.this.mViewGoBack;
        boolean bool2;
        if (i <= 0) {
          bool2 = false;
        } else {
          bool2 = bool1;
        }
        localImageView2.setEnabled(bool2);
        ImageView localImageView1 = FileChooserActivity.this.mViewGoForward;
        if ((i < 0) || (i >= -1 + paramAnonymousHistory.size())) {
          bool1 = false;
        }
        localImageView1.setEnabled(bool1);
      }
    });
    if ((paramBundle == null) || (!(paramBundle.get(_FullHistory) instanceof HistoryStore))) {
      this.mFullHistory = new HistoryStore(51)
      {
        public void push(IFile paramAnonymousIFile)
        {
          int i = indexOf(paramAnonymousIFile);
          if (i >= 0)
          {
            if (i != -1 + size()) {
              remove(paramAnonymousIFile);
            }
          }
          else {
            super.push(paramAnonymousIFile);
          }
        }
      };
    } else {
      this.mFullHistory = ((History)paramBundle.getParcelable(_FullHistory));
    }
    setResult(0);
    Log.v("tag", "before binding service");
    bindService(paramBundle);
  }
  
  public boolean onCreateOptionsMenu(Menu paramMenu)
  {
    getMenuInflater().inflate(R.menu.afc_file_chooser_activity, paramMenu);
    return true;
  }
  
  protected void onDestroy()
  {
    if (this.mFileProvider != null) {}
    try
    {
      unbindService(this.mServiceConnection);
    }
    catch (Throwable localThrowable)
    {
      try
      {
        for (;;)
        {
          stopService(new Intent(this, this.mFileProviderServiceClass));
          label32:
          super.onDestroy();
          return;
          localThrowable = localThrowable;
          Log.e(_ClassName, "onDestroy() - unbindService() - exception: " + localThrowable);
        }
      }
      catch (SecurityException localSecurityException)
      {
        break label32;
      }
    }
  }
  
  public boolean onOptionsItemSelected(MenuItem paramMenuItem)
  {
    if (paramMenuItem.getGroupId() != R.id.afc_filechooser_activity_menugroup_sorter)
    {
      if (paramMenuItem.getItemId() != R.id.afc_filechooser_activity_menuitem_new_folder)
      {
        if (paramMenuItem.getItemId() != R.id.afc_filechooser_activity_menuitem_switch_viewmode)
        {
          if (paramMenuItem.getItemId() != R.id.afc_filechooser_activity_menuitem_home)
          {
            if (paramMenuItem.getItemId() == R.id.afc_filechooser_activity_menuitem_reload) {
              doReloadCurrentLocation();
            }
          }
          else {
            doGoHome();
          }
        }
        else {
          doSwitchViewType();
        }
      }
      else {
        doCreateNewDir();
      }
    }
    else {
      doResortViewFiles();
    }
    return true;
  }
  
  public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    boolean bool = DisplayPrefs.isSortAscending(this);
    MenuItem localMenuItem = paramMenu.findItem(R.id.afc_filechooser_activity_menuitem_sort);
    int i;
    switch (DisplayPrefs.getSortType(this))
    {
    case SortByDate: 
      if (!bool) {
        i = R.drawable.afc_ic_menu_sort_by_name_desc;
      } else {
        i = R.drawable.afc_ic_menu_sort_by_name_asc;
      }
      localMenuItem.setIcon(i);
      break;
    case SortByName: 
      if (i == 0) {
        i = R.drawable.afc_ic_menu_sort_by_size_desc;
      } else {
        i = R.drawable.afc_ic_menu_sort_by_size_asc;
      }
      localMenuItem.setIcon(i);
      break;
    case SortBySize: 
      if (i == 0) {
        i = R.drawable.afc_ic_menu_sort_by_date_desc;
      } else {
        i = R.drawable.afc_ic_menu_sort_by_date_asc;
      }
      localMenuItem.setIcon(i);
    }
    localMenuItem = paramMenu.findItem(R.id.afc_filechooser_activity_menuitem_switch_viewmode);
    switch (DisplayPrefs.getViewType(this))
    {
    case Grid: 
      localMenuItem.setIcon(R.drawable.afc_ic_menu_gridview);
      localMenuItem.setTitle(R.string.afc_cmd_grid_view);
      break;
    case List: 
      localMenuItem.setIcon(R.drawable.afc_ic_menu_listview);
      localMenuItem.setTitle(R.string.afc_cmd_list_view);
    }
    return true;
  }
  
  protected void onSaveInstanceState(Bundle paramBundle)
  {
    paramBundle.putParcelable(_CurrentLocation, getLocation());
    paramBundle.putParcelable(_History, this.mHistory);
    paramBundle.putParcelable(_FullHistory, this.mFullHistory);
  }
  
  protected void onStart()
  {
    super.onStart();
    if ((!this.mIsMultiSelection) && (!this.mIsSaveDialog) && (this.mDoubleTapToChooseFiles)) {
      Dlg.toast(this, R.string.afc_hint_double_tap_to_select_file, 0);
    }
  }
  
  public static enum ViewType
  {
    Grid,  List;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.FileChooserActivity
 * JD-Core Version:    0.7.0.1
 */