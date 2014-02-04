package net.simonvt.menudrawer;

import android.view.View;

final class ViewHelper
{
  public static int getBottom(View paramView)
  {
    int i;
    if (!MenuDrawer.USE_TRANSLATIONS) {
      i = paramView.getBottom();
    } else {
      i = (int)(paramView.getBottom() + paramView.getTranslationY());
    }
    return i;
  }
  
  public static int getLeft(View paramView)
  {
    int i;
    if (!MenuDrawer.USE_TRANSLATIONS) {
      i = paramView.getLeft();
    } else {
      i = (int)(paramView.getLeft() + paramView.getTranslationX());
    }
    return i;
  }
  
  public static int getRight(View paramView)
  {
    int i;
    if (!MenuDrawer.USE_TRANSLATIONS) {
      i = paramView.getRight();
    } else {
      i = (int)(paramView.getRight() + paramView.getTranslationX());
    }
    return i;
  }
  
  public static int getTop(View paramView)
  {
    int i;
    if (!MenuDrawer.USE_TRANSLATIONS) {
      i = paramView.getTop();
    } else {
      i = (int)(paramView.getTop() + paramView.getTranslationY());
    }
    return i;
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.ViewHelper
 * JD-Core Version:    0.7.0.1
 */