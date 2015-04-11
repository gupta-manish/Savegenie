package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class GroceryListItem implements Parcelable
{
	public String productSkuId;
	public String productSkuName;
	public String cartId;
	public String count;
	public String mrp;

	public GroceryListItem(String productSkuId, String productSkuName, String cartId,
			String count, String mrp) {
		super();
		this.productSkuId = productSkuId;
		this.productSkuName = productSkuName;
		this.cartId = cartId;
		this.count = count;
		this.mrp = mrp;
	}
	
	private GroceryListItem(Parcel in) {
		super();
		productSkuId = in.readString();
		productSkuName = in.readString();
		cartId = in.readString();
		count = in.readString();
		mrp = in.readString();
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(productSkuId);
		dest.writeString(productSkuName);
		dest.writeString(cartId);
		dest.writeString(count);
		dest.writeString(mrp);
	}
	
	public static final Parcelable.Creator<GroceryListItem> CREATOR = new Parcelable.Creator<GroceryListItem>() {
        @Override
		public GroceryListItem createFromParcel(Parcel in) {
            return new GroceryListItem(in);
        }

        @Override
		public GroceryListItem[] newArray(int size) {
            return new GroceryListItem[size];
        }
    };

}
