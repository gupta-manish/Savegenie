package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class SendFeedBackAsyncTask extends BasicAsyncTask<String>
{
    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data",params[0]);
    }

    @Override
    String getUrlLink()
    {
        return InternetURL.SEND_FEEDBACK;
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

}

