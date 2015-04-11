package in.savegenie.savegenie.backgroundclasses;

import java.util.ArrayList;

/**
 * Created by manish on 20/3/15.
 */
public class BundleDeal
{
    public BundleGet bundleGet;
    public ArrayList<BundleItem> bundleItemList;

    public BundleDeal(BundleGet bundleGet,ArrayList<BundleItem> bundleItemList)
    {
        this.bundleGet = bundleGet;
        this.bundleItemList = bundleItemList;
    }
}
