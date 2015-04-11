package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable
{
	
	public String id;
	public String priceatmrp;
	public String storeprice;
	public String priceafterdeal;
	public String dealdiscount;
	public String additionalcashback;
	public String netprice;
	
	public Order(String id,String priceatmrp,String storeprice,String priceafterdeal,String dealdiscount,String additionalcashback,String netprice)
	{
		super();
		this.id = id;
		this.priceatmrp = priceatmrp;
		this.storeprice = storeprice;
		this.priceafterdeal = priceafterdeal;
		this.dealdiscount = dealdiscount;
		this.additionalcashback = additionalcashback;
		this.netprice = netprice;
	}

	private Order(Parcel in) {
		super();
        id = in.readString();
        priceatmrp = in.readString();
        storeprice = in.readString();
        priceafterdeal = in.readString();
        dealdiscount = in.readString();
        additionalcashback = in.readString();
        netprice = in.readString();
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
		dest.writeString(priceatmrp);
		dest.writeString(storeprice);
		dest.writeString(priceafterdeal);
		dest.writeString(dealdiscount);
		dest.writeString(additionalcashback);
		dest.writeString(netprice);
		
	}

	public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
		public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
		public Order[] newArray(int size) {
            return new Order[size];
        }
    };

}
