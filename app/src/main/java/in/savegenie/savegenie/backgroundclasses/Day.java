package in.savegenie.savegenie.backgroundclasses;

import android.os.Parcel;
import android.os.Parcelable;


public class Day implements Parcelable
{
    public String dayName,date,available;

    public Day(String dayName,String date,String available)
    {
        super();
        this.dayName = dayName;
        this.date = date;
        this.available = available;
    }

    private Day(Parcel in) {
        super();
        dayName = in.readString();
        date = in.readString();
        available = in.readString();
    }
    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // TODO Auto-generated method stub
        dest.writeString(dayName);
        dest.writeString(date);
        dest.writeString(available);
    }

    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

}
