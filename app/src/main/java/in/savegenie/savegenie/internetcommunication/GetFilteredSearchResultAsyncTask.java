package in.savegenie.savegenie.internetcommunication;

import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.responses.ProductListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class GetFilteredSearchResultAsyncTask extends BasicAsyncTask<ProductListResponse>
{
    String getUrlLink()
    {
        return InternetURL.GET_FILT_SEARCH_RESULTS;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[search]",params[0]);
        addDataToPostVariable("data[id]",params[1]);
    }

    ProductListResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<Product> productList = new ArrayList<Product>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("product")) {
                productList.add(readProduct(parser));
            }
        }
        return new ProductListResponse(productList,null,null);
    }

    Product readProduct(XmlPullParser parser) throws XmlPullParserException,
            IOException {
        parser.require(XmlPullParser.START_TAG, null, "product");
        int paramId;

        Product product = new Product(new String[26]);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            paramId = product.getParamId(name);
            product.params[paramId] = readParamValue(parser, paramId);
            // Log.d(name,product.params[paramId]);
        }

        return product;
    }

    String readParamValue(XmlPullParser parser, int paramId)
            throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null,
                Product.getParamName(paramId));
        try {
            String paramValue = readText(parser);
            parser.require(XmlPullParser.END_TAG, null,
                    Product.getParamName(paramId));

            return paramValue;
        } catch (XmlPullParserException e) {
            Log.d("XML", "XML " + Product.getParamName(paramId));
            throw e;
        }

    }
}
