package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.BundleDeal;
import in.savegenie.savegenie.backgroundclasses.ProductDeal;

public class DealResponse
{
    public ArrayList<BundleDeal> bundleDealList;
    public ArrayList<ProductDeal> productDealList;

    public DealResponse(ArrayList<BundleDeal> bundleDealList,ArrayList<ProductDeal> productDealList)
    {
        this.bundleDealList = bundleDealList;
        this.productDealList = productDealList;
    }
}
