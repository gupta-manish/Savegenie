package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import in.savegenie.savegenie.responses.SendInactiveListNameResponse;

public class SendInactiveListNameAsyncTask extends BasicAsyncTask<SendInactiveListNameResponse>
{

    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        return InternetURL.SEND_INACTIVE_LIST_NAME;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[SgenieCart][name]",params[0]);
    }

    @Override
    SendInactiveListNameResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String result = null,error = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result")) {
                result = readResult(parser,name);
            }
            if(name.equals("error"))
            {
                error = readResult(parser,name);
            }
        }

        return new SendInactiveListNameResponse(result,error);
    }

}
