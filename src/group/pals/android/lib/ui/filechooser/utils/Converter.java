package group.pals.android.lib.ui.filechooser.utils;

public class Converter
{
  public static String sizeToStr(double paramDouble)
  {
    String str;
    if (paramDouble > 0.0D)
    {
      String[] arrayOfString = new String[5];
      arrayOfString[0] = "B";
      arrayOfString[1] = "KB";
      arrayOfString[2] = "MB";
      arrayOfString[3] = "GB";
      arrayOfString[4] = "TB";
      Short localShort = Short.valueOf((short)1024);
      int i = (int)(Math.log10(paramDouble) / Math.log10(localShort.shortValue()));
      if (i >= arrayOfString.length) {
        i = -1 + arrayOfString.length;
      }
      double d = paramDouble / Math.pow(localShort.shortValue(), i);
      Object localObject2 = new Object[1];
      if (i != 0) {
        localObject1 = "%,.2f";
      } else {
        localObject1 = "%,.0f";
      }
      localObject2[0] = localObject1;
      localObject2 = String.format("%s %%s", (Object[])localObject2);
      Object localObject1 = new Object[2];
      localObject1[0] = Double.valueOf(d);
      localObject1[1] = arrayOfString[i];
      str = String.format((String)localObject2, (Object[])localObject1);
    }
    else
    {
      str = "0 B";
    }
    return str;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.Converter
 * JD-Core Version:    0.7.0.1
 */