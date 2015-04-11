package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable
{

    public String productSkuName;
    public String productName;
    public String quantity;
    public String mrp;
    public String storesku;
    public String price;
    public String subTotal;

    public OrderItem(String productSkuName, String productName, String quantity, String mrp, String storesku, String price)
    {
        super();
        this.productSkuName = productSkuName;
        this.productName = productName;
        this.quantity = quantity;
        this.mrp = mrp;
        this.storesku = storesku;
        this.price = price;

        double p = Double.parseDouble(price);
        double q = Double.parseDouble(quantity);
        double s = p*q;
        this.subTotal = s + "";

    }

    private OrderItem(Parcel in)
    {
        super();
        productSkuName = in.readString();
        productName = in.readString();
        quantity = in.readString();
        mrp = in.readString();
        storesku = in.readString();
        price = in.readString();
        subTotal = in.readString();
    }

    @Override
    public int describeContents()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        // TODO Auto-generated method stub
        dest.writeString(productSkuName);
        dest.writeString(productName);
        dest.writeString(quantity);
        dest.writeString(mrp);
        dest.writeString(storesku);
        dest.writeString(price);
        dest.writeString(subTotal);
    }

    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>()
    {
        @Override
        public OrderItem createFromParcel(Parcel in)
        {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size)
        {
            return new OrderItem[size];
        }
    };

}
