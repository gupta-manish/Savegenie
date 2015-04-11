package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class AlternateItem implements Parcelable
{
	public String price, size, productSKUId, unit, image, name, storeId, count,
			storeSkuId, finalCut, effectivePrice, status, isSwappable, mrp,
			isdeal, value_index;

	public AlternateItem(String price, String size, String productSKUId,
			String unit, String image, String name, String storeId,
			String count, String storeSkuId, String finalCut,
			String effectivePrice, String status, String isSwappable,
			String mrp, String isdeal, String value_index) {
		super();
		this.price = price;
		this.size = size;
		this.productSKUId = productSKUId;
		this.unit = unit;
		this.image = image;
		this.name = name;
		this.storeId = storeId;
		this.count = count;
		this.storeSkuId = storeSkuId;
		this.finalCut = finalCut;
		this.effectivePrice = effectivePrice;
		this.status = status;
		this.isSwappable = isSwappable;
		this.mrp = mrp;
		this.isdeal = isdeal;
		this.value_index = value_index;
	}

	private AlternateItem(Parcel in) {
		// TODO Auto-generated constructor stub
		super();
		this.price = in.readString();
		this.size = in.readString();
		this.productSKUId = in.readString();
		this.unit = in.readString();
		this.image = in.readString();
		this.name = in.readString();
		this.storeId = in.readString();
		this.count = in.readString();
		this.storeSkuId = in.readString();
		this.finalCut = in.readString();
		this.effectivePrice = in.readString();
		this.status = in.readString();
		this.isSwappable = in.readString();
		this.mrp = in.readString();
		this.isdeal = in.readString();
		this.value_index = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(price);
		dest.writeString(size);
		dest.writeString(productSKUId);
		dest.writeString(unit);
		dest.writeString(image);
		dest.writeString(name);
		dest.writeString(storeId);
		dest.writeString(count);
		dest.writeString(storeSkuId);
		dest.writeString(finalCut);
		dest.writeString(effectivePrice);
		dest.writeString(status);
		dest.writeString(isSwappable);
		dest.writeString(mrp);
		dest.writeString(isdeal);
		dest.writeString(value_index);
	}

	public static final Parcelable.Creator<AlternateItem> CREATOR = new Parcelable.Creator<AlternateItem>() {
		@Override
		public AlternateItem createFromParcel(Parcel in) {
			return new AlternateItem(in);
		}

		@Override
		public AlternateItem[] newArray(int size) {
			return new AlternateItem[size];
		}
	};
	
	public StoreSkuData toStoreSkuData(){
		StoreSkuData storeSkuData = new StoreSkuData(productSKUId,storeId, "", "", effectivePrice, 
				storeSkuId, name, image, size, unit, status, count, mrp, value_index, 
														isdeal, "", "", new ArrayList<SimilarStoreSkus>());
		
		return storeSkuData;
		
	}
}
