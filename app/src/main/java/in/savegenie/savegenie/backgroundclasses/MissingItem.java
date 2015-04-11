package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class MissingItem implements Parcelable
{
	public String productSkuId,size,unit,count,image,code,mrp;

	public MissingItem(String productSkuId,String size,String unit,
			String count,String image,String code,String mrp) {
		super();
		this.productSkuId = productSkuId;
		this.size = size;
		this.unit = unit;
		this.count = count;
		this.image = image;
		this.code = code;
		this.mrp = mrp;
	}

	private MissingItem(Parcel in) {
		// TODO Auto-generated constructor stub
		super();
		this.productSkuId = in.readString();
		this.size = in.readString();
		this.unit = in.readString();
		this.count = in.readString();
		this.image = in.readString();
		this.code = in.readString();
		this.mrp = in.readString();
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
	}
	
	public static final Parcelable.Creator<MissingItem> CREATOR = new Parcelable.Creator<MissingItem>() {
        @Override
		public MissingItem createFromParcel(Parcel in) {
            return new MissingItem(in);
        }

        @Override
		public MissingItem[] newArray(int size) {
            return new MissingItem[size];
        }
    };
}
