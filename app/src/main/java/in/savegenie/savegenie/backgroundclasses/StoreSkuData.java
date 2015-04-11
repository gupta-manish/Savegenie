package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class StoreSkuData implements Parcelable
{
	public String ProductSkuId,store_id,store_name,store_image,price,store_sku_id,name,image,size,unit,
	status,count,mrp,value_index,isdeal,costPerUnit,found;
	
	public ArrayList<SimilarStoreSkus> similarStoreSkusList;
	
	public StoreSkuData(String ProductSkuId,String store_id,String store_name,String store_image,
						String price,String store_sku_id,String name,String image,String size,String unit,
						String status,String count,String mrp,String value_index,String isdeal,
						String costPerUnit,String found,ArrayList<SimilarStoreSkus> similarStoreSkusList)
	{
		super();
		this.ProductSkuId = ProductSkuId;
		this.similarStoreSkusList = similarStoreSkusList;
		this.costPerUnit = costPerUnit;
		this.count = count;
		this.found = found;
		this.image = image;
		this.isdeal = isdeal;
		this.mrp = mrp;
		this.price = price;
		this.name = name;
		this.unit = unit;
		this.size = size;
		this.status = status;
		this.store_id = store_id;
		this.store_image = store_image;
		this.store_name = store_name;
		this.store_sku_id = store_sku_id;
	}
	
	private StoreSkuData(Parcel in) {
		// TODO Auto-generated constructor stub
		super();
		this.ProductSkuId = in.readString();
		this.similarStoreSkusList = in.readArrayList(SimilarStoreSkus.class.getClassLoader());
		this.costPerUnit = in.readString();
		this.count = in.readString();
		this.found = in.readString();
		this.image = in.readString();
		this.isdeal = in.readString();
		this.price = in.readString();
		this.mrp = in.readString();
		this.unit = in.readString();
		this.name = in.readString();
		this.size = in.readString();
		this.status = in.readString();
		this.store_id = in.readString();
		this.store_image = in.readString();
		this.store_name = in.readString();
		this.store_sku_id = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int arg1) {
		// TODO Auto-generated method stub
		dest.writeString(ProductSkuId);
		dest.writeList(similarStoreSkusList);
		dest.writeString(costPerUnit);
		dest.writeString(count);
		dest.writeString(found);
		dest.writeString(image);
		dest.writeString(isdeal);
		dest.writeString(mrp);
		dest.writeString(price);
		dest.writeString(unit);
		dest.writeString(name);
		dest.writeString(size);
		dest.writeString(status);
		dest.writeString(store_id);
		dest.writeString(store_image);
		dest.writeString(store_name);
		dest.writeString(store_sku_id);
	}
	
	public static final Parcelable.Creator<StoreSkuData> CREATOR = new Parcelable.Creator<StoreSkuData>() {
        @Override
		public StoreSkuData createFromParcel(Parcel in) {
            return new StoreSkuData(in);
        }

        @Override
		public StoreSkuData[] newArray(int size) {
            return new StoreSkuData[size];
        }
    };
    

}
