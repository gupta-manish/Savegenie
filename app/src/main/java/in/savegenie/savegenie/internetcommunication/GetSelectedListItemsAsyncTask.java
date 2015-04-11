package in.savegenie.savegenie.internetcommunication;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.GroceryListItem;
import in.savegenie.savegenie.responses.GroceryListResponse;

public class GetSelectedListItemsAsyncTask extends
		BasicAsyncTask<GroceryListResponse> {

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_SELECTED_LIST_ITEMS;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[id]",params[0]);
    }

    GroceryListResponse readResponse(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<GroceryListItem> itemList = new ArrayList<GroceryListItem>();
		String result = null;
		String count = null;
		parser.require(XmlPullParser.START_TAG, null, "response");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals("result")) {
				result = readResult(parser,name);
			}
			if (name.equals("countitems")) {
				count = readResult(parser,name);
			}
			if (name.equals("listitem")) {
				Log.d("hahah", "hahah");
				itemList.add(readListItem(parser));
			}
		}

		return new GroceryListResponse(itemList, result, count);
	}

	GroceryListItem readListItem(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "listitem");
		String productSkuId = null;
		String productSkuName = null;
		String cartId = null;
		String count = null;
		String mrp = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("productskuid")) {
				productSkuId = readResult(parser,name);
			}
			if (name.equals("productskuname")) {
				productSkuName = readResult(parser,name);
			}
			if (name.equals("cartid")) {
				cartId = readResult(parser,name);
			}
			if (name.equals("count")) {
				count = readResult(parser,name);
			}
			if (name.equals("mrp")) {
				mrp = readResult(parser,name);
			}
		}

		return new GroceryListItem(productSkuId, productSkuName, cartId, count,
				mrp);
	}

}
