package com.haarman.listviewanimations;

import android.widget.BaseAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class ArrayAdapter<T>
  extends BaseAdapter
{
  private ArrayList<T> mItems = new ArrayList();
  
  public ArrayAdapter()
  {
    this(null);
  }
  
  public ArrayAdapter(List<T> paramList)
  {
    if (paramList != null) {
      this.mItems.addAll(paramList);
    }
  }
  
  public void add(int paramInt, T paramT)
  {
    this.mItems.add(paramInt, paramT);
    notifyDataSetChanged();
  }
  
  public void add(T paramT)
  {
    this.mItems.add(paramT);
    notifyDataSetChanged();
  }
  
  public void addAll(int paramInt, Collection<? extends T> paramCollection)
  {
    this.mItems.addAll(paramInt, paramCollection);
    notifyDataSetChanged();
  }
  
  public void addAll(int paramInt, T... paramVarArgs)
  {
    for (int i = paramInt; i < paramInt + paramVarArgs.length; i++) {
      this.mItems.add(i, paramVarArgs[i]);
    }
    notifyDataSetChanged();
  }
  
  public void addAll(Collection<? extends T> paramCollection)
  {
    this.mItems.addAll(paramCollection);
    notifyDataSetChanged();
  }
  
  public void addAll(T... paramVarArgs)
  {
    Collections.addAll(this.mItems, paramVarArgs);
    notifyDataSetChanged();
  }
  
  public void clear()
  {
    this.mItems.clear();
    notifyDataSetChanged();
  }
  
  public int getCount()
  {
    return this.mItems.size();
  }
  
  public T getItem(int paramInt)
  {
    return this.mItems.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return paramInt;
  }
  
  public int indexOf(T paramT)
  {
    return this.mItems.indexOf(paramT);
  }
  
  public void remove(int paramInt)
  {
    this.mItems.remove(paramInt);
    notifyDataSetChanged();
  }
  
  public void remove(T paramT)
  {
    this.mItems.remove(paramT);
    notifyDataSetChanged();
  }
  
  public void removeAll(Collection<T> paramCollection)
  {
    this.mItems.removeAll(paramCollection);
    notifyDataSetChanged();
  }
  
  public void removePositions(Collection<Integer> paramCollection)
  {
    ArrayList localArrayList = new ArrayList(paramCollection);
    Collections.sort(localArrayList);
    Collections.reverse(localArrayList);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      int i = ((Integer)localIterator.next()).intValue();
      this.mItems.remove(i);
    }
    notifyDataSetChanged();
  }
  
  public void retainAll(Collection<T> paramCollection)
  {
    this.mItems.retainAll(paramCollection);
    notifyDataSetChanged();
  }
  
  public void set(int paramInt, T paramT)
  {
    this.mItems.set(paramInt, paramT);
    notifyDataSetChanged();
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.haarman.listviewanimations.ArrayAdapter
 * JD-Core Version:    0.7.0.1
 */