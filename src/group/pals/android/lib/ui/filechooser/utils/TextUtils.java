package group.pals.android.lib.ui.filechooser.utils;

public class TextUtils
{
  public static String quote(String paramString)
  {
    Object[] arrayOfObject = new Object[1];
    if (paramString == null) {
      paramString = "";
    }
    arrayOfObject[0] = paramString;
    return String.format("\"%s\"", arrayOfObject);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.TextUtils
 * JD-Core Version:    0.7.0.1
 */