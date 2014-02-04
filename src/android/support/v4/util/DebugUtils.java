package android.support.v4.util;

public class DebugUtils
{
  public static void buildShortClassTag(Object paramObject, StringBuilder paramStringBuilder)
  {
    if (paramObject != null)
    {
      String str = paramObject.getClass().getSimpleName();
      if ((str == null) || (str.length() <= 0))
      {
        str = paramObject.getClass().getName();
        int i = str.lastIndexOf('.');
        if (i > 0) {
          str = str.substring(i + 1);
        }
      }
      paramStringBuilder.append(str);
      paramStringBuilder.append('{');
      paramStringBuilder.append(Integer.toHexString(System.identityHashCode(paramObject)));
    }
    else
    {
      paramStringBuilder.append("null");
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.util.DebugUtils
 * JD-Core Version:    0.7.0.1
 */