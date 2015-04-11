package in.savegenie.savegenie.backgroundclasses;

/**
 * Created by manish on 20/3/15.
 */
public class BundleItem
{
    public String id,bundle_get_id,store_id,store_sku_id,qty,status;
    public StoreSku storeSku;

    public BundleItem(String id,String bundle_get_id,String store_id,String store_sku_id,String qty,String status,StoreSku storeSku)
    {
        this.id = id;
        this.bundle_get_id = bundle_get_id;
        this.store_id = store_id;
        this.store_sku_id = store_sku_id;
        this.qty = qty;
        this.status = status;
        this.storeSku = storeSku;

    }

}
