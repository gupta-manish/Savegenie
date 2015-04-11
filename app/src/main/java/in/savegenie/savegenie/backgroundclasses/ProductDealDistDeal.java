package in.savegenie.savegenie.backgroundclasses;

/**
 * Created by manish on 21/3/15.
 */
public class ProductDealDistDeal
{
    String id,store_skus_id,store_id,count,price_type,max_qty,discount_type,value,start_date,end_date,status;

    public ProductDealDistDeal(String id,String store_skus_id,String store_id,String count,String price_type,String max_qty,
                    String discount_type,String value,String start_date,String end_date,String status)
    {
        this.id = id;
        this.store_skus_id = store_skus_id;
        this.store_id = store_id;
        this.count = count;
        this.price_type = price_type;
        this.max_qty = max_qty;
        this.max_qty = max_qty;
        this.discount_type = discount_type;
        this.value = value;
        this.start_date = start_date;
        this.end_date = end_date;
        this.status = status;
    }
}
