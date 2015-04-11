package in.savegenie.savegenie.backgroundclasses;

/**
 * Created by manish on 21/3/15.
 */
public class StoreSku
{
    public String id,code,price,mrp,is_deal,status,store_specific_id,product_skus_id;
    public ProductSku productSku;

    public StoreSku(String id, String code, String price, String mrp, String is_deal, String status,
                    String store_specific_id, String product_skus_id, ProductSku productSku)
    {
        this.id = id;
        this.code = code;
        this.price = price;
        this.mrp = mrp;
        this.is_deal = is_deal;
        this.status = status;
        this.store_specific_id = store_specific_id;
        this.product_skus_id = product_skus_id;
        this.productSku = productSku;
    }
}
