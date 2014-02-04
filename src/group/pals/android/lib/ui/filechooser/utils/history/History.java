package group.pals.android.lib.ui.filechooser.utils.history;

import android.os.Parcelable;
import java.util.ArrayList;

public abstract interface History<A>
  extends Parcelable
{
  public abstract void addListener(HistoryListener<A> paramHistoryListener);
  
  public abstract void clear();
  
  public abstract int indexOf(A paramA);
  
  public abstract boolean isEmpty();
  
  public abstract ArrayList<A> items();
  
  public abstract A nextOf(A paramA);
  
  public abstract void notifyHistoryChanged();
  
  public abstract A prevOf(A paramA);
  
  public abstract void push(A paramA);
  
  public abstract void remove(A paramA);
  
  public abstract void removeAll(HistoryFilter<A> paramHistoryFilter);
  
  public abstract void removeListener(HistoryListener<A> paramHistoryListener);
  
  public abstract int size();
  
  public abstract void truncateAfter(A paramA);
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.history.History
 * JD-Core Version:    0.7.0.1
 */