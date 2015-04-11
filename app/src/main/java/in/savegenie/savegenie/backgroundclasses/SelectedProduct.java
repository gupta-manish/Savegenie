package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SelectedProduct implements Parcelable
{
	public String productSkuId,size,unit,count,image,code,mrp;
	public ArrayList<StoreSkuData> storeSkuDataList;
	
	public SelectedProduct(String productSkuId,String size,String unit,
			String count,String image,String code,String mrp,ArrayList<StoreSkuData> storeSkuDataList)
	{
		super();
		this.productSkuId = productSkuId;
		this.size = size;
		this.unit = unit;
		this.count = count;
		this.image = image;
		this.code = code;
		this.mrp = mrp;
		this.storeSkuDataList = storeSkuDataList;
	}
	
	private SelectedProduct(Parcel in)
	{
		this.productSkuId = in.readString();
		this.size = in.readString();
		this.unit = in.readString();
		this.count = in.readString();
		this.image = in.readString();
		this.code = in.readString();
		this.mrp = in.readString();
		this.storeSkuDataList = in.readArrayList(StoreSkuData.class.getClassLoader());
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
		dest.writeString(size);
		dest.writeString(unit);
		dest.writeString(count);
		dest.writeString(image);
		dest.writeString(code);
		dest.writeString(mrp);
		dest.writeList(storeSkuDataList);
	}
	
	public static final Parcelable.Creator<SelectedProduct> CREATOR = new Parcelable.Creator<SelectedProduct>() {
        @Override
		public SelectedProduct createFromParcel(Parcel in) {
            return new SelectedProduct(in);
        }

        @Override
		public SelectedProduct[] newArray(int size) {
            return new SelectedProduct[size];
        }
    };

}
