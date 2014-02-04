package com.kharybdis.hitchernet.menudrawer;

public class Item
{
  public static final String ITEM_HELP = "HELP";
  public static final String ITEM_HOME = "HOME";
  public static final String ITEM_INBOX = "INBOX";
  public static final String ITEM_OUTBOX = "OUTBOX";
  public static final String ITEM_SETTINGS = "SETTINGS";
  int mIconRes;
  String mTitle;
  public String mUniqueCode;
  
  Item(String paramString1, String paramString2, int paramInt)
  {
    this.mTitle = paramString1;
    this.mIconRes = paramInt;
    this.mUniqueCode = paramString2;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.menudrawer.Item
 * JD-Core Version:    0.7.0.1
 */