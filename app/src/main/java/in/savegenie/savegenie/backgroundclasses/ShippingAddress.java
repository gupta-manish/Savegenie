package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class ShippingAddress implements Parcelable
{
    public String id;
    public String name;
    public String address;
    public String area;
    public String city;
    public String mobileno;
    public String landmark;
    public String society_name;


    public ShippingAddress(String id, String name,String address, String area, String city, String mobileno,String landmark,
            String society_name)
    {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.area = area;
        this.city = city;
        this.mobileno = mobileno;
        this.landmark = landmark;
        this.society_name = society_name;
    }

    private ShippingAddress(Parcel in) {
        super();
        id = in.readString();
        name = in.readString();
        address = in.readString();
        area = in.readString();
        city = in.readString();
        mobileno = in.readString();
        landmark = in.readString();
        society_name = in.readString();
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
        dest.writeString(area);
        dest.writeString(city);
        dest.writeString(mobileno);
        dest.writeString(landmark);
        dest.writeString(society_name);
    }

    public static final Parcelable.Creator<ShippingAddress> CREATOR = new Parcelable.Creator<ShippingAddress>() {
        @Override
        public ShippingAddress createFromParcel(Parcel in) {
            return new ShippingAddress(in);
        }

        @Override
        public ShippingAddress[] newArray(int size) {
            return new ShippingAddress[size];
        }
    };

}
