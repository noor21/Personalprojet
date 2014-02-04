package group.pals.android.lib.ui.filechooser.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import group.pals.android.lib.ui.filechooser.FileChooserActivity.ViewType;
import group.pals.android.lib.ui.filechooser.R.bool;
import group.pals.android.lib.ui.filechooser.R.integer;
import group.pals.android.lib.ui.filechooser.R.string;
import group.pals.android.lib.ui.filechooser.services.IFileProvider.SortType;

public class DisplayPrefs
  extends Prefs
{
  public static final int _DefHistoryCapacity = 51;
  public static final int _DelayTimeWaitingThreads = 10;
  
  public static String getLastLocation(Context paramContext)
  {
    return p(paramContext).getString(paramContext.getString(R.string.afc_pkey_display_last_location), null);
  }
  
  public static IFileProvider.SortType getSortType(Context paramContext)
  {
    for (localSortType : ) {
      if (localSortType.ordinal() == p(paramContext).getInt(paramContext.getString(R.string.afc_pkey_display_sort_type), paramContext.getResources().getInteger(R.integer.afc_pkey_display_sort_type_def))) {
        return localSortType;
      }
    }
    IFileProvider.SortType localSortType = IFileProvider.SortType.SortByName;
    return localSortType;
  }
  
  public static FileChooserActivity.ViewType getViewType(Context paramContext)
  {
    FileChooserActivity.ViewType localViewType;
    if (FileChooserActivity.ViewType.List.ordinal() != p(paramContext).getInt(paramContext.getString(R.string.afc_pkey_display_view_type), paramContext.getResources().getInteger(R.integer.afc_pkey_display_view_type_def))) {
      localViewType = FileChooserActivity.ViewType.Grid;
    } else {
      localViewType = FileChooserActivity.ViewType.List;
    }
    return localViewType;
  }
  
  public static boolean isRememberLastLocation(Context paramContext)
  {
    return p(paramContext).getBoolean(paramContext.getString(R.string.afc_pkey_display_remember_last_location), paramContext.getResources().getBoolean(R.bool.afc_pkey_display_remember_last_location_def));
  }
  
  public static boolean isShowTimeForOldDays(Context paramContext)
  {
    return p(paramContext).getBoolean(paramContext.getString(R.string.afc_pkey_display_show_time_for_old_days), paramContext.getResources().getBoolean(R.bool.afc_pkey_display_show_time_for_old_days_def));
  }
  
  public static boolean isShowTimeForOldDaysThisYear(Context paramContext)
  {
    return p(paramContext).getBoolean(paramContext.getString(R.string.afc_pkey_display_show_time_for_old_days_this_year), paramContext.getResources().getBoolean(R.bool.afc_pkey_display_show_time_for_old_days_this_year_def));
  }
  
  public static boolean isSortAscending(Context paramContext)
  {
    return p(paramContext).getBoolean(paramContext.getString(R.string.afc_pkey_display_sort_ascending), paramContext.getResources().getBoolean(R.bool.afc_pkey_display_sort_ascending_def));
  }
  
  public static void setLastLocation(Context paramContext, String paramString)
  {
    p(paramContext).edit().putString(paramContext.getString(R.string.afc_pkey_display_last_location), paramString).commit();
  }
  
  public static void setRememberLastLocation(Context paramContext, Boolean paramBoolean)
  {
    if (paramBoolean == null) {
      paramBoolean = Boolean.valueOf(paramContext.getResources().getBoolean(R.bool.afc_pkey_display_remember_last_location_def));
    }
    p(paramContext).edit().putBoolean(paramContext.getString(R.string.afc_pkey_display_remember_last_location), paramBoolean.booleanValue()).commit();
  }
  
  public static void setShowTimeForOldDays(Context paramContext, Boolean paramBoolean)
  {
    if (paramBoolean == null) {
      paramBoolean = Boolean.valueOf(paramContext.getResources().getBoolean(R.bool.afc_pkey_display_show_time_for_old_days_def));
    }
    p(paramContext).edit().putBoolean(paramContext.getString(R.string.afc_pkey_display_show_time_for_old_days), paramBoolean.booleanValue()).commit();
  }
  
  public static void setShowTimeForOldDaysThisYear(Context paramContext, Boolean paramBoolean)
  {
    if (paramBoolean == null) {
      paramBoolean = Boolean.valueOf(paramContext.getResources().getBoolean(R.bool.afc_pkey_display_show_time_for_old_days_this_year_def));
    }
    p(paramContext).edit().putBoolean(paramContext.getString(R.string.afc_pkey_display_show_time_for_old_days_this_year), paramBoolean.booleanValue()).commit();
  }
  
  public static void setSortAscending(Context paramContext, Boolean paramBoolean)
  {
    if (paramBoolean == null) {
      paramBoolean = Boolean.valueOf(paramContext.getResources().getBoolean(R.bool.afc_pkey_display_sort_ascending_def));
    }
    p(paramContext).edit().putBoolean(paramContext.getString(R.string.afc_pkey_display_sort_ascending), paramBoolean.booleanValue()).commit();
  }
  
  public static void setSortType(Context paramContext, IFileProvider.SortType paramSortType)
  {
    String str = paramContext.getString(R.string.afc_pkey_display_sort_type);
    if (paramSortType != null) {
      p(paramContext).edit().putInt(str, paramSortType.ordinal()).commit();
    } else {
      p(paramContext).edit().putInt(str, paramContext.getResources().getInteger(R.integer.afc_pkey_display_sort_type_def)).commit();
    }
  }
  
  public static void setViewType(Context paramContext, FileChooserActivity.ViewType paramViewType)
  {
    String str = paramContext.getString(R.string.afc_pkey_display_view_type);
    if (paramViewType != null) {
      p(paramContext).edit().putInt(str, paramViewType.ordinal()).commit();
    } else {
      p(paramContext).edit().putInt(str, paramContext.getResources().getInteger(R.integer.afc_pkey_display_view_type_def)).commit();
    }
  }
  
  public static class FileTimeDisplay
  {
    private boolean mShowTimeForOldDays;
    private boolean mShowTimeForOldDaysThisYear;
    
    public FileTimeDisplay(boolean paramBoolean1, boolean paramBoolean2)
    {
      this.mShowTimeForOldDaysThisYear = paramBoolean1;
      this.mShowTimeForOldDays = paramBoolean2;
    }
    
    public boolean isShowTimeForOldDays()
    {
      return this.mShowTimeForOldDays;
    }
    
    public boolean isShowTimeForOldDaysThisYear()
    {
      return this.mShowTimeForOldDaysThisYear;
    }
    
    public FileTimeDisplay setShowTimeForOldDays(boolean paramBoolean)
    {
      this.mShowTimeForOldDays = paramBoolean;
      return this;
    }
    
    public FileTimeDisplay setShowTimeForOldDaysThisYear(boolean paramBoolean)
    {
      this.mShowTimeForOldDaysThisYear = paramBoolean;
      return this;
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.prefs.DisplayPrefs
 * JD-Core Version:    0.7.0.1
 */