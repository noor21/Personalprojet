package group.pals.android.lib.ui.filechooser.utils.history;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HistoryStore<A extends Parcelable>
  implements History<A>
{
  public static final Parcelable.Creator<HistoryStore> CREATOR = new Parcelable.Creator()
  {
    public HistoryStore createFromParcel(Parcel paramAnonymousParcel)
    {
      return new HistoryStore(paramAnonymousParcel, null);
    }
    
    public HistoryStore[] newArray(int paramAnonymousInt)
    {
      return new HistoryStore[paramAnonymousInt];
    }
  };
  private final ArrayList<A> mHistoryList = new ArrayList();
  private final List<HistoryListener<A>> mListeners = new ArrayList();
  private final int mMaxSize;
  
  public HistoryStore(int paramInt)
  {
    if (paramInt <= 0) {
      paramInt = 100;
    }
    this.mMaxSize = paramInt;
  }
  
  private HistoryStore(Parcel paramParcel)
  {
    this.mMaxSize = paramParcel.readInt();
    int j = paramParcel.readInt();
    for (int i = 0; i < j; i++) {
      this.mHistoryList.add(paramParcel.readParcelable(null));
    }
  }
  
  public void addListener(HistoryListener<A> paramHistoryListener)
  {
    this.mListeners.add(paramHistoryListener);
  }
  
  public void clear()
  {
    this.mHistoryList.clear();
    notifyHistoryChanged();
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public int indexOf(A paramA)
  {
    return this.mHistoryList.indexOf(paramA);
  }
  
  public boolean isEmpty()
  {
    return this.mHistoryList.isEmpty();
  }
  
  public ArrayList<A> items()
  {
    return (ArrayList)this.mHistoryList.clone();
  }
  
  public A nextOf(A paramA)
  {
    int i = this.mHistoryList.indexOf(paramA);
    Parcelable localParcelable;
    if ((i < 0) || (i >= -1 + this.mHistoryList.size())) {
      localParcelable = null;
    } else {
      localParcelable = (Parcelable)this.mHistoryList.get(localParcelable + 1);
    }
    return localParcelable;
  }
  
  public void notifyHistoryChanged()
  {
    Iterator localIterator = this.mListeners.iterator();
    while (localIterator.hasNext()) {
      ((HistoryListener)localIterator.next()).onChanged(this);
    }
  }
  
  public A prevOf(A paramA)
  {
    int i = this.mHistoryList.indexOf(paramA);
    Parcelable localParcelable;
    if (i <= 0) {
      localParcelable = null;
    } else {
      localParcelable = (Parcelable)this.mHistoryList.get(localParcelable - 1);
    }
    return localParcelable;
  }
  
  public void push(A paramA)
  {
    if ((paramA != null) && ((this.mHistoryList.isEmpty()) || (this.mHistoryList.indexOf(paramA) != -1 + this.mHistoryList.size())))
    {
      this.mHistoryList.add(paramA);
      notifyHistoryChanged();
    }
  }
  
  public void remove(A paramA)
  {
    if (this.mHistoryList.remove(paramA)) {
      notifyHistoryChanged();
    }
  }
  
  public void removeAll(HistoryFilter<A> paramHistoryFilter)
  {
    int j = 0;
    for (int i = -1 + this.mHistoryList.size(); i >= 0; i--) {
      if (paramHistoryFilter.accept((Parcelable)this.mHistoryList.get(i)))
      {
        this.mHistoryList.remove(i);
        if (j == 0) {
          j = 1;
        }
      }
    }
    if (j != 0) {
      notifyHistoryChanged();
    }
  }
  
  public void removeListener(HistoryListener<A> paramHistoryListener)
  {
    this.mListeners.remove(paramHistoryListener);
  }
  
  public int size()
  {
    return this.mHistoryList.size();
  }
  
  public void truncateAfter(A paramA)
  {
    if (paramA != null)
    {
      int i = this.mHistoryList.indexOf(paramA);
      if ((i >= 0) && (i < -1 + this.mHistoryList.size()))
      {
        this.mHistoryList.subList(i + 1, this.mHistoryList.size()).clear();
        notifyHistoryChanged();
      }
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeInt(this.mMaxSize);
    paramParcel.writeInt(size());
    for (int i = 0; i < size(); i++) {
      paramParcel.writeParcelable((Parcelable)this.mHistoryList.get(i), paramInt);
    }
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     group.pals.android.lib.ui.filechooser.utils.history.HistoryStore
 * JD-Core Version:    0.7.0.1
 */