package in.savegenie.savegenie.responses;

/**
 * Created by Devansh on 21-Jan-15.
 */
public class CompleteOrderResponse
{
    public String mode, orderId, email, loggedtime, ordercompletetime, totaldiscount, storename,
            txnid, priceatmrp, storeprice, priceafterdeal, deliverycharges, discount,
            netamount, pickupdelivery, address, phone, deliverydate, starttime, endtime;

    public CompleteOrderResponse(String mode, String orderId, String email, String loggedtime,
                                 String ordercompletetime, String totaldiscount, String storename,
                                 String txnid, String priceatmrp, String storeprice, String priceafterdeal,
                                 String deliverycharges, String discount, String netamount, String pickupdelivery,
                                 String address, String phone, String deliverydate, String starttime, String endtime)
    {
        this.mode=mode;
        this.orderId=orderId;
        this.email=email;
        this.loggedtime=loggedtime;
        this.ordercompletetime=ordercompletetime;
        this.totaldiscount=totaldiscount;
        this.storename=storename;
        this.txnid=txnid;
        this.priceatmrp=priceatmrp;
        this.storeprice=storeprice;
        this.priceafterdeal=priceafterdeal;
        this.deliverycharges=deliverycharges;
        this.discount=discount;
        this.netamount=netamount;
        this.pickupdelivery=pickupdelivery;
        this.address=address;
        this.phone=phone;
        this.deliverydate=deliverydate;
        this.starttime=starttime;
        this.endtime=endtime;
    }
}
