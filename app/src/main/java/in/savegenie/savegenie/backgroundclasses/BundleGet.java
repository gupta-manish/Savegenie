package in.savegenie.savegenie.backgroundclasses;

/**
 * Created by manish on 20/3/15.
 */
public class BundleGet
{
    public String id,store_id,bundle_id,price_type,discount_type,value,start_date,end_date,status;

    public BundleGet(String id,String store_id,String bundle_id,String price_type,String discount_type,String value,
                     String start_date,String end_date,String status)
    {
        this.id = id;
        this.store_id = store_id;
        this.bundle_id = bundle_id;
        this.price_type = price_type;
        this.discount_type = discount_type;
        this.value = value;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;

    }
}
