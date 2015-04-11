package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.BundleDeal;
import in.savegenie.savegenie.backgroundclasses.BundleGet;
import in.savegenie.savegenie.backgroundclasses.BundleItem;
import in.savegenie.savegenie.backgroundclasses.ProductDeal;
import in.savegenie.savegenie.backgroundclasses.ProductDealDistDeal;
import in.savegenie.savegenie.backgroundclasses.ProductSku;
import in.savegenie.savegenie.backgroundclasses.StoreSku;
import in.savegenie.savegenie.responses.DealResponse;

/**
 * Created by manish on 20/3/15.
 */
public class GetDealsAsyncTask extends BasicAsyncTask<DealResponse>
{


    @Override
    String getUrlLink()
    {
        return InternetURL.GET_STORE_DEALS;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[storeId]",params[0]);
    }

    @Override
    DealResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<BundleDeal> bundleDealList = new ArrayList<BundleDeal>();
        ArrayList<ProductDeal> productDealList = new ArrayList<ProductDeal>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("bundledeals")) {
                bundleDealList.add(readBundleDeal(parser));
            }
            if(name.equals("productdeals"))
            {
                productDealList.add(readProductDeal(parser));
            }
        }

        return new DealResponse(bundleDealList,productDealList);
    }

    BundleDeal readBundleDeal(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        BundleGet bundleGet = null;
        ArrayList<BundleItem> bundleItemList = new ArrayList<BundleItem>();

        parser.require(XmlPullParser.START_TAG, null, "bundledeals");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("BundleGet")) {
                bundleGet = readBundleGet(parser);
            }
            if(name.equals("BundleBuy"))
            {
                bundleItemList.add(readBundleItem(parser));
            }
        }

        return new BundleDeal(bundleGet,bundleItemList);
    }

    BundleGet readBundleGet(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id = null,store_id = null,bundle_id = null,price_type = null,
                discount_type = null,value = null,start_date = null,end_date = null,status = null;

        parser.require(XmlPullParser.START_TAG, null, "BundleGet");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser, name);
            }
            else if(name.equals("store_id"))
            {
                store_id = readResult(parser,name);
            }
            else if(name.equals("bundle_id"))
            {
                bundle_id = readResult(parser,name);
            }
            else if(name.equals("price_type"))
            {
                price_type = readResult(parser,name);
            }
            else if(name.equals("discount_type"))
            {
                discount_type = readResult(parser,name);
            }
            else if(name.equals("value"))
            {
                value = readResult(parser,name);
            }
            else if(name.equals("start_date"))
            {
                start_date = readResult(parser,name);
            }
            else if(name.equals("end_date"))
            {
                end_date = readResult(parser,name);
            }
            else if(name.equals("status"))
            {
                status = readResult(parser,name);
            }
        }

        return new BundleGet(id,store_id,bundle_id,price_type,discount_type,value,start_date,end_date,status);
    }

    BundleItem readBundleItem(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id = null,bundle_get_id = null,store_id = null,store_sku_id = null,qty = null,status = null;
        StoreSku storeSku = null;

        parser.require(XmlPullParser.START_TAG, null, "BundleBuy");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser, name);
            }
            else if(name.equals("bundle_get_id"))
            {
                bundle_get_id = readResult(parser,name);
            }
            else if(name.equals("store_id"))
            {
                store_id = readResult(parser,name);
            }
            else if(name.equals("store_sku_id"))
            {
                store_sku_id = readResult(parser,name);
            }
            else if(name.equals("qty"))
            {
                qty = readResult(parser,name);
            }
            else if(name.equals("status"))
            {
                status = readResult(parser,name);
            }
            else if(name.equals("StoreSku"))
            {
                storeSku = readStoreSku(parser);
            }
        }

        return new BundleItem(id,bundle_get_id,store_id,store_sku_id,qty,status,storeSku);
    }

    StoreSku readStoreSku(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id = null,code = null,price = null,mrp = null,status = null,
                store_specific_id = null,product_skus_id = null,is_deal = null;
       ProductSku productSku = null;

        parser.require(XmlPullParser.START_TAG, null, "StoreSku");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser, name);
            }
            else if(name.equals("code"))
            {
                code = readResult(parser,name);
            }
            else if(name.equals("price"))
            {
                price = readResult(parser,name);
            }
            else if(name.equals("mrp"))
            {
                mrp = readResult(parser,name);
            }
            else if(name.equals("is_deal"))
            {
                is_deal = readResult(parser,name);
            }
            else if(name.equals("store_specific_id"))
            {
                store_specific_id = readResult(parser,name);
            }
            else if(name.equals("status"))
            {
                status = readResult(parser,name);
            }
            else if(name.equals("product_skus_id"))
            {
                product_skus_id = readResult(parser,name);
            }
            else if(name.equals("ProductSku"))
            {
                productSku = readProductSku(parser);
            }
        }
        return new StoreSku(id,code,price,mrp,is_deal,status,store_specific_id,product_skus_id,productSku);
    }

    ProductSku readProductSku(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id = null,image = null;

        parser.require(XmlPullParser.START_TAG, null, "ProductSku");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser, name);
            }
            else if(name.equals("image"))
            {
                image = readResult(parser,name);
            }

        }
        return new ProductSku(id,image);
    }

    ProductDeal readProductDeal(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ProductDealDistDeal distDeal = null;
        StoreSku storeSku = null;

        parser.require(XmlPullParser.START_TAG, null, "productdeals");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("DistDeal"))
            {
                distDeal = readDistDeal(parser);
            }
            else if(name.equals("StoreSku"))
            {
                storeSku = readStoreSku(parser);
            }

        }
        return new ProductDeal(distDeal,storeSku);
    }

    ProductDealDistDeal readDistDeal(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id=null,store_skus_id=null,store_id=null,count=null,price_type=null,
                max_qty=null,discount_type=null,value=null,start_date=null,end_date=null,
                status=null;
        parser.require(XmlPullParser.START_TAG, null, "DistDeal");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser,name);
            }
            else if(name.equals("store_skus_id"))
            {
                store_skus_id = readResult(parser, name);
            }
            else if(name.equals("count"))
            {
                count = readResult(parser,name);
            }
            else if(name.equals("price_type"))
            {
                price_type = readResult(parser,name);
            }
            else if(name.equals("max_qty"))
            {
                max_qty = readResult(parser,name);
            }
            else if(name.equals("discount_type"))
            {
                discount_type = readResult(parser,name);
            }
            else if(name.equals("value"))
            {
                value = readResult(parser,name);
            }
            else if(name.equals("start_date"))
            {
                start_date = readResult(parser,name);
            }
            else if(name.equals("end_date"))
            {
                end_date = readResult(parser,name);
            }
            else if(name.equals("status"))
            {
                status = readResult(parser,name);
            }
            else if(name.equals("store_id"))
            {
                store_id = readResult(parser,name);
            }

        }
        return new ProductDealDistDeal(id,store_skus_id,store_id,count,price_type,max_qty,
                discount_type,value,start_date,end_date,status);
    }


}
