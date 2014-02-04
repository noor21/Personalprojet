package group.pals.android.lib.ui.filechooser.utils;

import android.content.Context;
import group.pals.android.lib.ui.filechooser.R.string;
import group.pals.android.lib.ui.filechooser.prefs.DisplayPrefs.FileTimeDisplay;
import java.util.Calendar;

public class DateUtils
{
  public static final int _FormatMonthAndDay = 65560;
  public static final int _FormatShortTime = 65;
  public static final int _FormatYear = 4;
  
  public static String formatDate(Context paramContext, long paramLong, DisplayPrefs.FileTimeDisplay paramFileTimeDisplay)
  {
    Calendar localCalendar = Calendar.getInstance();
    localCalendar.setTimeInMillis(paramLong);
    return formatDate(paramContext, localCalendar, paramFileTimeDisplay);
  }
  
  public static String formatDate(Context paramContext, Calendar paramCalendar, DisplayPrefs.FileTimeDisplay paramFileTimeDisplay)
  {
    Object localObject = Calendar.getInstance();
    ((Calendar)localObject).add(6, -1);
    if (!android.text.format.DateUtils.isToday(paramCalendar.getTimeInMillis()))
    {
      if ((paramCalendar.get(1) != ((Calendar)localObject).get(1)) || (paramCalendar.get(6) != ((Calendar)localObject).get(6)))
      {
        if (paramCalendar.get(1) != ((Calendar)localObject).get(1))
        {
          if (!paramFileTimeDisplay.isShowTimeForOldDays()) {
            localObject = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65564);
          } else {
            localObject = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65629);
          }
        }
        else if (!paramFileTimeDisplay.isShowTimeForOldDaysThisYear()) {
          localObject = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65560);
        } else {
          localObject = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65625);
        }
      }
      else
      {
        localObject = new Object[2];
        localObject[0] = paramContext.getString(R.string.afc_yesterday);
        localObject[1] = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65);
        localObject = String.format("%s, %s", (Object[])localObject);
      }
    }
    else {
      localObject = android.text.format.DateUtils.formatDateTime(paramContext, paramCalendar.getTimeInMillis(), 65);
    }
    return localObject;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.DateUtils
 * JD-Core Version:    0.7.0.1
 */