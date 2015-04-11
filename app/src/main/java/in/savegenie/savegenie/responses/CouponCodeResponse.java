package in.savegenie.savegenie.responses;

/**
 * Created by Devansh on 16-Jan-15.
 */
public class CouponCodeResponse
{
    public String message,totalprice,couponprice,storecouponprice,netprice,couponcode;

    public CouponCodeResponse(String message,String totalprice,String couponprice,String storecouponprice,String netprice,
                              String couponcode)
    {
        this.message = message;
        this.totalprice = totalprice;
        this.couponprice = couponprice;
        this.storecouponprice = storecouponprice;
        this.netprice = netprice;
        this.couponcode = couponcode;
    }
}
