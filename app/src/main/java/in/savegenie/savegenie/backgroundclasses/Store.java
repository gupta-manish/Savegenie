package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Store implements Parcelable
{
	public String name;
	public String id;
    public String imgName;
	
	public Store(String name,String id,String imgName)
	{
		super();
		this.name = name;
		this.id = id;
        this.imgName = imgName;
	}

	private Store(Parcel in) {
		super();
        name = in.readString();
        id = in.readString();
        imgName = in.readString();
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
        dest.writeString(imgName);
		
	}

	public static final Parcelable.Creator<Store> CREATOR = new Parcelable.Creator<Store>() {
        @Override
		public Store createFromParcel(Parcel in) {
            return new Store(in);
        }

        @Override
		public Store[] newArray(int size) {
            return new Store[size];
        }
    };

}
