package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class GroceryList implements Parcelable
{
	public String name;
	public String id;
	
	public GroceryList(String name,String id)
	{
		super();
		this.name = name;
		this.id = id;
	}

	private GroceryList(Parcel in) {
		super();
        name = in.readString();
        id = in.readString();
    }
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(name);
		dest.writeString(id);
		
	}

	public static final Parcelable.Creator<GroceryList> CREATOR = new Parcelable.Creator<GroceryList>() {
        @Override
		public GroceryList createFromParcel(Parcel in) {
            return new GroceryList(in);
        }

        @Override
		public GroceryList[] newArray(int size) {
            return new GroceryList[size];
        }
    };

}
