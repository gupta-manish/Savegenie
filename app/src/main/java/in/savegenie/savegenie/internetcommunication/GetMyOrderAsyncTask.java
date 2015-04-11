package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.savegenie.savegenie.responses.MyOrdersResponse;

/**
 * Created by manish on 8/4/15.
 */
public class GetMyOrderAsyncTask extends BasicAsyncTask<MyOrdersResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.GET_MY_ORDER_LIST;
    }

    @Override
    MyOrdersResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        return null;
    }

    @Override
    void addPostData(String... params)
    {

    }
}
