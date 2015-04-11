package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by manish on 27/3/15.
 */
public class DeleteItemFromListAsynTask extends BasicAsyncTask<String>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.DELETE_ITEM_FROM_LIST;
    }

    String readResponse(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        String result = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result")) {
                result = readResult(parser,name);
            }
        }

        return result;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[productskuid]",params[0]);
    }
}
