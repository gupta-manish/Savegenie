package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Slot;


public class StoreSlotListResponse
{
    public ArrayList<Slot> slotList;

    public StoreSlotListResponse(ArrayList<Slot> slotList) {
        this.slotList = slotList;
    }
}
