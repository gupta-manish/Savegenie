package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Day;

/**
 * Created by Devansh on 06-Jan-15.
 */
public class PickDelDayDateResponse
{
    public ArrayList<Day> dayList;

    public PickDelDayDateResponse(ArrayList<Day> dayList)
    {
        this.dayList = dayList;
    }
}
