package com.kharybdis.hitchernet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

class InboxOutboxAdapter
  extends BaseAdapter
{
  Context context;
  List<HashMap<String, String>> data;
  boolean inbox;
  
  public InboxOutboxAdapter(List<HashMap<String, String>> paramList, Context paramContext, boolean paramBoolean)
  {
    this.context = paramContext;
    this.data = paramList;
    this.inbox = paramBoolean;
  }
  
  private CharSequence getReadableSize(String paramString)
  {
    double d = Double.parseDouble(paramString);
    String str;
    if (d <= 1073741824.0D)
    {
      if (d <= 1048576.0D)
      {
        if (d <= 1024.0D)
        {
          d = d;
          str = "B";
        }
        else
        {
          d /= 1024.0D;
          str = "KB";
        }
      }
      else
      {
        d /= 1048576.0D;
        str = "MB";
      }
    }
    else
    {
      d /= 1073741824.0D;
      str = "GB";
    }
    return new DecimalFormat("0.00").format(d) + str;
  }
  
  public int getCount()
  {
    return this.data.size();
  }
  
  public String getFilePath(int paramInt)
  {
    return (String)((HashMap)this.data.get(paramInt)).get("filePath");
  }
  
  public Object getItem(int paramInt)
  {
    return this.data.get(paramInt);
  }
  
  public long getItemId(int paramInt)
  {
    return 0L;
  }
  
  public int getPercent(int paramInt)
  {
    return Integer.parseInt((String)((HashMap)this.data.get(paramInt)).get("progressPercent"));
  }
  
  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    int i = Integer.parseInt((String)((HashMap)this.data.get(paramInt)).get("progressPercent"));
    Object localObject1 = (LayoutInflater)this.context.getSystemService("layout_inflater");
    Object localObject3;
    TextView localTextView;
    Object localObject5;
    Object localObject4;
    String str;
    if (i < 100)
    {
      localObject1 = ((LayoutInflater)localObject1).inflate(2130903061, null);
      localObject3 = (ProgressBar)((View)localObject1).findViewById(2131099697);
      localTextView = (TextView)((View)localObject1).findViewById(2131099716);
      ((ProgressBar)localObject3).setProgress(i);
      DecimalFormat localDecimalFormat = new DecimalFormat("0.00");
      localObject5 = (String)((HashMap)this.data.get(paramInt)).get("timeRemaining");
      localObject4 = (String)((HashMap)this.data.get(paramInt)).get("speedMbps");
      if ((localObject5 == null) || (((String)localObject5).equalsIgnoreCase("")))
      {
        str = "";
        localObject5 = "";
      }
      try
      {
        localObject2 = localDecimalFormat.format(i) + "% @ " + localDecimalFormat.format(Double.parseDouble((String)localObject4)) + " Mbps  " + str;
        localObject5 = localObject2;
      }
      catch (Exception localException)
      {
        for (;;)
        {
          Object localObject2;
          localException.printStackTrace();
          ((ProgressBar)localObject3).setVisibility(8);
        }
      }
      localTextView.setText((CharSequence)localObject5);
    }
    for (;;)
    {
      localObject4 = (ImageView)((View)localObject1).findViewById(2131099700);
      localObject2 = (TextView)((View)localObject1).findViewById(2131099714);
      localObject3 = (TextView)((View)localObject1).findViewById(2131099717);
      localTextView = (TextView)((View)localObject1).findViewById(2131099715);
      localObject5 = (TextView)((View)localObject1).findViewById(2131099713);
      ((TextView)localObject2).setText((CharSequence)((HashMap)this.data.get(paramInt)).get("fileName"));
      ((TextView)localObject3).setText((CharSequence)((HashMap)this.data.get(paramInt)).get("createdAt"));
      localTextView.setText(getReadableSize((String)((HashMap)this.data.get(paramInt)).get("size")));
      ((TextView)localObject5).setText((CharSequence)((HashMap)this.data.get(paramInt)).get("deviceName"));
      if (!this.inbox) {
        ((ImageView)localObject4).setImageResource(2130837590);
      }
      return localObject1;
      str = localObject5 + " remaining";
      break;
      localObject1 = ((LayoutInflater)localObject1).inflate(2130903060, null);
    }
  }
  
  public void insertItem(HashMap<String, String> paramHashMap, int paramInt)
  {
    this.data.add(paramInt, paramHashMap);
  }
  
  public Object removeItem(int paramInt)
  {
    return this.data.remove(paramInt);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     com.kharybdis.hitchernet.InboxOutboxAdapter
 * JD-Core Version:    0.7.0.1
 */