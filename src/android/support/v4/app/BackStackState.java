package android.support.v4.app;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

final class BackStackState
  implements Parcelable
{
  public static final Parcelable.Creator<BackStackState> CREATOR = new Parcelable.Creator()
  {
    public BackStackState createFromParcel(Parcel paramAnonymousParcel)
    {
      return new BackStackState(paramAnonymousParcel);
    }
    
    public BackStackState[] newArray(int paramAnonymousInt)
    {
      return new BackStackState[paramAnonymousInt];
    }
  };
  final int mBreadCrumbShortTitleRes;
  final CharSequence mBreadCrumbShortTitleText;
  final int mBreadCrumbTitleRes;
  final CharSequence mBreadCrumbTitleText;
  final int mIndex;
  final String mName;
  final int[] mOps;
  final int mTransition;
  final int mTransitionStyle;
  
  public BackStackState(Parcel paramParcel)
  {
    this.mOps = paramParcel.createIntArray();
    this.mTransition = paramParcel.readInt();
    this.mTransitionStyle = paramParcel.readInt();
    this.mName = paramParcel.readString();
    this.mIndex = paramParcel.readInt();
    this.mBreadCrumbTitleRes = paramParcel.readInt();
    this.mBreadCrumbTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
    this.mBreadCrumbShortTitleRes = paramParcel.readInt();
    this.mBreadCrumbShortTitleText = ((CharSequence)TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(paramParcel));
  }
  
  public BackStackState(FragmentManagerImpl paramFragmentManagerImpl, BackStackRecord paramBackStackRecord)
  {
    int i = 0;
    int k;
    BackStackRecord.Op localOp3;
    for (BackStackRecord.Op localOp2 = paramBackStackRecord.mHead;; localOp3 = k.next)
    {
      BackStackRecord.Op localOp1;
      if (localOp2 == null)
      {
        this.mOps = new int[i + 7 * paramBackStackRecord.mNumOp];
        if (paramBackStackRecord.mAddToBackStack)
        {
          localOp1 = paramBackStackRecord.mHead;
          k = 0;
          if (localOp1 == null)
          {
            this.mTransition = paramBackStackRecord.mTransition;
            this.mTransitionStyle = paramBackStackRecord.mTransitionStyle;
            this.mName = paramBackStackRecord.mName;
            this.mIndex = paramBackStackRecord.mIndex;
            this.mBreadCrumbTitleRes = paramBackStackRecord.mBreadCrumbTitleRes;
            this.mBreadCrumbTitleText = paramBackStackRecord.mBreadCrumbTitleText;
            this.mBreadCrumbShortTitleRes = paramBackStackRecord.mBreadCrumbShortTitleRes;
            this.mBreadCrumbShortTitleText = paramBackStackRecord.mBreadCrumbShortTitleText;
            return;
          }
          int[] arrayOfInt2 = this.mOps;
          int m = k + 1;
          arrayOfInt2[k] = localOp1.cmd;
          arrayOfInt2 = this.mOps;
          k = m + 1;
          int i2;
          if (localOp1.fragment == null) {
            i2 = -1;
          } else {
            i2 = localOp1.fragment.mIndex;
          }
          arrayOfInt2[m] = i2;
          arrayOfInt2 = this.mOps;
          m = k + 1;
          arrayOfInt2[k] = localOp1.enterAnim;
          arrayOfInt2 = this.mOps;
          k = m + 1;
          arrayOfInt2[m] = localOp1.exitAnim;
          arrayOfInt2 = this.mOps;
          m = k + 1;
          arrayOfInt2[k] = localOp1.popEnterAnim;
          arrayOfInt2 = this.mOps;
          k = m + 1;
          arrayOfInt2[m] = localOp1.popExitAnim;
          int i1;
          int n;
          int[] arrayOfInt3;
          if (localOp1.removed == null)
          {
            int[] arrayOfInt1 = this.mOps;
            i1 = k + 1;
            arrayOfInt1[k] = 0;
          }
          else
          {
            n = localOp1.removed.size();
            arrayOfInt3 = this.mOps;
            i1 = k + 1;
            arrayOfInt3[k] = n;
            k = 0;
          }
          for (int i3 = i1;; i3 = i1)
          {
            if (k >= n)
            {
              i1 = i3;
              localOp1 = localOp1.next;
              k = i1;
              break;
            }
            arrayOfInt3 = this.mOps;
            i1 = i3 + 1;
            arrayOfInt3[i3] = ((Fragment)localOp1.removed.get(k)).mIndex;
            k++;
          }
        }
        throw new IllegalStateException("Not on back stack");
      }
      if (k.removed != null)
      {
        int j;
        localOp1 += k.removed.size();
      }
    }
  }
  
  public int describeContents()
  {
    return 0;
  }
  
  public BackStackRecord instantiate(FragmentManagerImpl paramFragmentManagerImpl)
  {
    BackStackRecord localBackStackRecord = new BackStackRecord(paramFragmentManagerImpl);
    int j = 0;
    int i = 0;
    if (j >= this.mOps.length)
    {
      localBackStackRecord.mTransition = this.mTransition;
      localBackStackRecord.mTransitionStyle = this.mTransitionStyle;
      localBackStackRecord.mName = this.mName;
      localBackStackRecord.mIndex = this.mIndex;
      localBackStackRecord.mAddToBackStack = true;
      localBackStackRecord.mBreadCrumbTitleRes = this.mBreadCrumbTitleRes;
      localBackStackRecord.mBreadCrumbTitleText = this.mBreadCrumbTitleText;
      localBackStackRecord.mBreadCrumbShortTitleRes = this.mBreadCrumbShortTitleRes;
      localBackStackRecord.mBreadCrumbShortTitleText = this.mBreadCrumbShortTitleText;
      localBackStackRecord.bumpBackStackNesting(1);
      return localBackStackRecord;
    }
    BackStackRecord.Op localOp = new BackStackRecord.Op();
    int[] arrayOfInt3 = this.mOps;
    int m = j + 1;
    localOp.cmd = arrayOfInt3[j];
    if (FragmentManagerImpl.DEBUG) {
      Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " op #" + i + " base fragment #" + this.mOps[m]);
    }
    arrayOfInt3 = this.mOps;
    j = m + 1;
    m = arrayOfInt3[m];
    if (m < 0) {
      localOp.fragment = null;
    } else {
      localOp.fragment = ((Fragment)paramFragmentManagerImpl.mActive.get(m));
    }
    arrayOfInt3 = this.mOps;
    m = j + 1;
    localOp.enterAnim = arrayOfInt3[j];
    int[] arrayOfInt1 = this.mOps;
    int i1 = m + 1;
    localOp.exitAnim = arrayOfInt1[m];
    int[] arrayOfInt2 = this.mOps;
    int k = i1 + 1;
    localOp.popEnterAnim = arrayOfInt2[i1];
    int[] arrayOfInt4 = this.mOps;
    int n = k + 1;
    localOp.popExitAnim = arrayOfInt4[k];
    arrayOfInt4 = this.mOps;
    k = n + 1;
    n = arrayOfInt4[n];
    int i2;
    if (n > 0)
    {
      localOp.removed = new ArrayList(n);
      i2 = 0;
    }
    for (;;)
    {
      if (i2 >= n)
      {
        k = k;
        localBackStackRecord.addOp(localOp);
        i++;
        break;
      }
      if (FragmentManagerImpl.DEBUG) {
        Log.v("FragmentManager", "Instantiate " + localBackStackRecord + " set remove fragment #" + this.mOps[k]);
      }
      ArrayList localArrayList = paramFragmentManagerImpl.mActive;
      int[] arrayOfInt5 = this.mOps;
      Fragment localFragment2 = k + 1;
      Fragment localFragment1 = (Fragment)localArrayList.get(arrayOfInt5[k]);
      localOp.removed.add(localFragment1);
      i2++;
      localFragment1 = localFragment2;
    }
  }
  
  public void writeToParcel(Parcel paramParcel, int paramInt)
  {
    paramParcel.writeIntArray(this.mOps);
    paramParcel.writeInt(this.mTransition);
    paramParcel.writeInt(this.mTransitionStyle);
    paramParcel.writeString(this.mName);
    paramParcel.writeInt(this.mIndex);
    paramParcel.writeInt(this.mBreadCrumbTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbTitleText, paramParcel, 0);
    paramParcel.writeInt(this.mBreadCrumbShortTitleRes);
    TextUtils.writeToParcel(this.mBreadCrumbShortTitleText, paramParcel, 0);
  }
}


/* Location:           C:\Users\Admin\Desktop\nw\New folder\11111\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.app.BackStackState
 * JD-Core Version:    0.7.0.1
 */