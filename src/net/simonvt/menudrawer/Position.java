package net.simonvt.menudrawer;

import android.util.SparseArray;

public enum Position
{
  private static final SparseArray<Position> STRING_MAPPING;
  final int mValue;
  
  static
  {
    int i = 0;
    LEFT = new Position("LEFT", 0, 0);
    TOP = new Position("TOP", 1, 1);
    RIGHT = new Position("RIGHT", 2, 2);
    BOTTOM = new Position("BOTTOM", 3, 3);
    Position[] arrayOfPosition1 = new Position[4];
    arrayOfPosition1[i] = LEFT;
    arrayOfPosition1[1] = TOP;
    arrayOfPosition1[2] = RIGHT;
    arrayOfPosition1[3] = BOTTOM;
    ENUM$VALUES = arrayOfPosition1;
    STRING_MAPPING = new SparseArray();
    Position[] arrayOfPosition2 = values();
    int j = arrayOfPosition2.length;
    while (i < j)
    {
      Position localPosition = arrayOfPosition2[i];
      STRING_MAPPING.put(localPosition.mValue, localPosition);
      i++;
    }
  }
  
  private Position(int paramInt)
  {
    this.mValue = paramInt;
  }
  
  public static Position fromValue(int paramInt)
  {
    return (Position)STRING_MAPPING.get(paramInt);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     net.simonvt.menudrawer.Position
 * JD-Core Version:    0.7.0.1
 */