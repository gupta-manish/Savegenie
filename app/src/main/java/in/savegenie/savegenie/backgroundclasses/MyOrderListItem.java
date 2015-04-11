package in.savegenie.savegenie.backgroundclasses;

/**
 * Created by manish on 8/4/15.
 */
public class MyOrderListItem
{
    public String orderId,orderDate,orderStatus;

    public void MyOrderListItem(String orderId,String orderDate,String orderStatus)
    {
        this.orderDate = orderDate;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
    }
}
