package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderStore implements Parcelable
{
	
	public String id;
	public String name;
	public String address;
	public String mos;
	public String deliverycost;
	
	public OrderStore(String id,String name,String address,String mos,String deliverycost)
	{
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.mos = mos;
		this.deliverycost = deliverycost;
	}

	private OrderStore(Parcel in) {
		super();
        id = in.readString();
        name = in.readString();
        address = in.readString();
        mos = in.readString();
        deliverycost = in.readString();
    }
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(mos);
		dest.writeString(deliverycost);
		
	}

	public static final Parcelable.Creator<OrderStore> CREATOR = new Parcelable.Creator<OrderStore>() {
        @Override
		public OrderStore createFromParcel(Parcel in) {
            return new OrderStore(in);
        }

        @Override
		public OrderStore[] newArray(int size) {
            return new OrderStore[size];
        }
    };

}
