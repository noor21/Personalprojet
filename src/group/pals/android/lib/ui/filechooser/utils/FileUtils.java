package group.pals.android.lib.ui.filechooser.utils;

import group.pals.android.lib.ui.filechooser.R.drawable;
import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileUtils
{
  private static final Map<String, Integer> _MapFileIcons = new HashMap();
  
  static
  {
    _MapFileIcons.put("(?si).+\\.(mp[2-3]+|wav|aiff|au|m4a|ogg|raw|flac|mid|amr|aac|alac|atrac|awb|m4p|mmf|mpc|ra|rm|tta|vox|wma)", Integer.valueOf(R.drawable.afc_file_audio));
    _MapFileIcons.put("(?si).+\\.(mp[4]+|flv|wmv|webm|m4v|3gp|mkv|mov|mpe?g|rmv?|ogv|avi)", Integer.valueOf(R.drawable.afc_file_video));
    _MapFileIcons.put("(?si).+\\.(gif|jpe?g|png|tiff?|wmf|emf|jfif|exif|raw|bmp|ppm|pgm|pbm|pnm|webp|riff|tga|ilbm|img|pcx|ecw|sid|cd5|fits|pgf|xcf|svg|pns|jps|icon?|jp2|mng|xpm|djvu)", Integer.valueOf(R.drawable.afc_file_image));
    _MapFileIcons.put("(?si).+\\.(zip|7z|lz?|[jrt]ar|gz|gzip|bzip|xz|cab|sfx|z|iso|bz?|rz|s7z|apk|dmg)", Integer.valueOf(R.drawable.afc_file_compressed));
    _MapFileIcons.put("(?si).+\\.(txt|html?|json|csv|java|pas|php.*|c|cpp|bas|python|js|javascript|scala|xml|kml|css|ps|xslt?|tpl|tsv|bash|cmd|pl|pm|ps1|ps1xml|psc1|psd1|psm1|py|pyc|pyo|r|rb|sdl|sh|tcl|vbs|xpl|ada|adb|ads|clj|cls|cob|cbl|cxx|cs|csproj|d|e|el|go|h|hpp|hxx|l|m|url|ini|prop|conf|properties|rc)", Integer.valueOf(R.drawable.afc_file_plain_text));
  }
  
  public static Thread createDeleteFileThread(IFile paramIFile, final IFileProvider paramIFileProvider, final boolean paramBoolean)
  {
    new Thread()
    {
      private void deleteFile(IFile paramAnonymousIFile)
      {
        if (isInterrupted()) {}
        for (;;)
        {
          return;
          if (paramAnonymousIFile.isFile())
          {
            paramAnonymousIFile.delete();
            continue;
          }
          if (!paramAnonymousIFile.isDirectory()) {
            continue;
          }
          if (!paramBoolean)
          {
            paramAnonymousIFile.delete();
            continue;
          }
          try
          {
            Object localObject = paramIFileProvider.listAllFiles(paramAnonymousIFile);
            if (localObject == null)
            {
              paramAnonymousIFile.delete();
              continue;
            }
            localObject = ((List)localObject).iterator();
            for (;;)
            {
              if (!((Iterator)localObject).hasNext())
              {
                paramAnonymousIFile.delete();
                break;
              }
              IFile localIFile = (IFile)((Iterator)localObject).next();
              if (isInterrupted()) {
                break;
              }
              if (localIFile.isFile()) {
                localIFile.delete();
              } else if (localIFile.isDirectory()) {
                if (paramBoolean) {
                  deleteFile(localIFile);
                } else {
                  localIFile.delete();
                }
              }
            }
          }
          catch (Throwable localThrowable) {}
        }
      }
      
      public void run()
      {
        deleteFile(FileUtils.this);
      }
    };
  }
  
  public static int getResIcon(IFile paramIFile)
  {
    int i = 17301533;
    int j;
    if ((paramIFile != null) && (paramIFile.exists())) {
      if (!paramIFile.isFile())
      {
        if (paramIFile.isDirectory()) {
          i = R.drawable.afc_folder;
        }
      }
      else
      {
        String str2 = paramIFile.getName();
        Iterator localIterator = _MapFileIcons.keySet().iterator();
        while (localIterator.hasNext())
        {
          String str1 = (String)localIterator.next();
          if (str2.matches(str1)) {
            return ((Integer)_MapFileIcons.get(str1)).intValue();
          }
        }
        j = R.drawable.afc_file;
      }
    }
    return j;
  }
  
  public static boolean isFilenameValid(String paramString)
  {
    boolean bool;
    if ((paramString == null) || (!paramString.trim().matches("[^\\\\/?%*:|\"<>]+"))) {
      bool = false;
    } else {
      bool = true;
    }
    return bool;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.FileUtils
 * JD-Core Version:    0.7.0.1
 */