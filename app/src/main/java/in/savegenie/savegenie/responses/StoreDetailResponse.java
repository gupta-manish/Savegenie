package in.savegenie.savegenie.responses;

/**
 * Created by manish on 8/4/15.
 */
public class StoreDetailResponse
{
    public String storeAddress,deliveryCost;

    public StoreDetailResponse(String storeAddress,String deliveryCost)
    {
        this.deliveryCost = deliveryCost;
        this.storeAddress = storeAddress;
    }
}
