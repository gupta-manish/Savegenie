package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.MyOrderListItem;

/**
 * Created by manish on 8/4/15.
 */
public class MyOrdersResponse
{
    public ArrayList<MyOrderListItem> myOrderList;

    public MyOrdersResponse(ArrayList<MyOrderListItem> myOrderList)
    {
        this.myOrderList = myOrderList;
    }
}
