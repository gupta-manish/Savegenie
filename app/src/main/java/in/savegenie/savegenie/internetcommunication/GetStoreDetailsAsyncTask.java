package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.savegenie.savegenie.responses.StoreDetailResponse;

/**
 * Created by manish on 8/4/15.
 */
public class GetStoreDetailsAsyncTask extends BasicAsyncTask<StoreDetailResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.GET_STORE_DETAILS;
    }

    @Override
    StoreDetailResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String address = null,delcost = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("address")) {
                address = readResult(parser,name);
            }
            if(name.equals("delcost"))
            {
                delcost = readResult(parser,name);
            }
        }

        return new StoreDetailResponse(address,delcost);
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[storeId]",params[0]);
    }
}
