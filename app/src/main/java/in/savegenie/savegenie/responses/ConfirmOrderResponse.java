package in.savegenie.savegenie.responses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Devansh on 16-Jan-15.
 */
public class ConfirmOrderResponse implements Parcelable
{
    public String key, hash, txnid, amount, firstname, email, phone, productinfo, lastname,
            address1, address2, city, state, country, zipcode, udf1, udf2, udf5, mode,
            discount, delcost;

    public ConfirmOrderResponse(String key, String hash, String txnid, String amount,
                                String firstname,
                                String email, String phone, String productinfo, String lastname,
                                String address1, String address2, String city, String state,
                                String country,
                                String zipcode, String udf1, String udf2, String udf5,
                                String mode,
                                String discount, String delcost)
    {
        this.key = key;
        this.hash = hash;
        this.txnid = txnid;
        this.amount = amount;
        this.firstname = firstname;
        this.email = email;
        this.phone = phone;
        this.productinfo = productinfo;
        this.lastname = lastname;
        this.address1 = address1;
        this.address2 = address2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.udf1 = udf1;
        this.udf2 = udf2;
        this.udf5 = udf5;
        this.mode = mode;
        this.discount = discount;
        this.delcost = delcost;
    }

    private ConfirmOrderResponse(Parcel in) {
        super();
        key = in.readString();
        hash = in.readString();
        txnid = in.readString();
        amount = in.readString();
        firstname = in.readString();
        email = in.readString();
        phone = in.readString();
        productinfo = in.readString();
        lastname = in.readString();
        address1 = in.readString();
        address2 = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        zipcode = in.readString();
        udf1 = in.readString();
        udf2 = in.readString();
        udf5 = in.readString();
        mode = in.readString();
        discount = in.readString();
        delcost = in.readString();
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(key);
        dest.writeString(hash);
        dest.writeString(txnid);
        dest.writeString(amount);
        dest.writeString(firstname);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(productinfo);
        dest.writeString(lastname);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(zipcode);
        dest.writeString(udf1);
        dest.writeString(udf2);
        dest.writeString(udf5);
        dest.writeString(mode);
        dest.writeString(discount);
        dest.writeString(delcost);
    }

    public static final Parcelable.Creator<ConfirmOrderResponse> CREATOR = new Parcelable.Creator<ConfirmOrderResponse>() {
        @Override
        public ConfirmOrderResponse createFromParcel(Parcel in) {
            return new ConfirmOrderResponse(in);
        }

        @Override
        public ConfirmOrderResponse[] newArray(int size) {
            return new ConfirmOrderResponse[size];
        }
    };
}
