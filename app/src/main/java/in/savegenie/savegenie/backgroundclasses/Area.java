package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by savegenie on 3/4/15.
 */
public class Area implements Parcelable {
    public String id;
    public String name;

    public Area(String id,String name)
    {
        super();
        this.id = id;
        this.name = name;
    }

    private Area(Parcel in) {
        super();
        id = in.readString();
        name = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);

    }
    public static final Parcelable.Creator<Area> CREATOR = new Parcelable.Creator<Area>() {
        @Override
        public Area createFromParcel(Parcel in) {
            return new Area(in);
        }

        @Override
        public Area[] newArray(int size) {
            return new Area[size];
        }
    };

}
