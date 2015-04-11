package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

public class Slot implements Parcelable
{
    public String time;
    public String id;

    public Slot(String id,String time)
    {
        super();
        this.time = time;
        this.id = id;
    }

    private Slot(Parcel in) {
        super();
        time = in.readString();
        id = in.readString();
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(time);
        dest.writeString(id);

    }

    public static final Parcelable.Creator<Slot> CREATOR = new Parcelable.Creator<Slot>() {
        @Override
        public Slot createFromParcel(Parcel in) {
            return new Slot(in);
        }

        @Override
        public Slot[] newArray(int size) {
            return new Slot[size];
        }
    };

}
