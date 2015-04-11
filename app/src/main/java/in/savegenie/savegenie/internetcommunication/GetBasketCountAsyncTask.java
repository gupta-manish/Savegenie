package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by manish on 24/3/15.
 */
public class GetBasketCountAsyncTask extends BasicAsyncTask<String>
{
    String responseStr;
    InputStream inputStream;
    String urlLink;

    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_BASKET_COUNT;
    }

    @Override
    void addPostData(String... params)
    {
    }

    @Override
    String readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String result = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("count")) {
                result = readResult(parser,name);
            }
        }

        return result;
    }


}
