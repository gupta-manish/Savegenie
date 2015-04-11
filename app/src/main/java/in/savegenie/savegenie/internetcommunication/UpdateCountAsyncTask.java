package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Created by manish on 10/4/15.
 */
public class UpdateCountAsyncTask extends BasicAsyncTask<String>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.UPDATE_COUNT;
    }

    @Override
    String readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
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
        addDataToPostVariable("data[listId]",params[0]);
        addDataToPostVariable("data[productId]",params[1]);
        addDataToPostVariable("data[count]",params[1]);
    }
}
