package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CompareStoreItem implements Parcelable
{
	public ArrayList<MissingItem> missingItems;
	public ArrayList<AlternateItem> alternateItems;
	public String storePrice;
	public String deliveryCost;
	public String totalCost;
	public String imgName;
	public String storeId;
	public String storeName;
	public String storeAddress;

	public CompareStoreItem(ArrayList<MissingItem> missingItems,ArrayList<AlternateItem> alternateItems,
			String storePrice, String deliveryCost, String totalCost,
			String imgName, String storeId, String storeName, String storeAddress)
    {
		this.alternateItems = alternateItems;
		this.deliveryCost = deliveryCost;
		this.storePrice = storePrice;
		this.missingItems = missingItems;
		this.totalCost = totalCost;
		this.imgName = imgName;
		this.storeId = storeId;
		this.storeName = storeName;
		this.storeAddress = storeAddress;
	}

    private CompareStoreItem(Parcel in)
    {
        super();
        this.alternateItems = in.readArrayList(AlternateItem.class.getClassLoader());
        this.deliveryCost = in.readString();;
        this.storePrice = in.readString();;
        this.missingItems = in.readArrayList(MissingItem.class.getClassLoader());
        this.totalCost = in.readString();;
        this.imgName = in.readString();;
        this.storeId = in.readString();;
        this.storeName = in.readString();;
        this.storeAddress = in.readString();;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i)
    {
        dest.writeList(alternateItems);
        dest.writeString(deliveryCost);
        dest.writeString(storePrice);
        dest.writeList(missingItems);
        dest.writeString(totalCost);
        dest.writeString(imgName);
        dest.writeString(storeId);
        dest.writeString(storeName);
        dest.writeString(storeAddress);
    }

    public static final Parcelable.Creator<CompareStoreItem> CREATOR = new Parcelable.Creator<CompareStoreItem>() {
        @Override
        public CompareStoreItem createFromParcel(Parcel in) {
            return new CompareStoreItem(in);
        }

        @Override
        public CompareStoreItem[] newArray(int size) {
            return new CompareStoreItem[size];
        }
    };
}
