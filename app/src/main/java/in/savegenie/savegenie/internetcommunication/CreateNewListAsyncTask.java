package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.savegenie.savegenie.backgroundclasses.ListCreateResponse;

public class CreateNewListAsyncTask extends BasicAsyncTask<ListCreateResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.CREATE_NEW_LIST;
    }

    @Override
    ListCreateResponse readResponse(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        String listId = null;
        String result = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("listid")) {
                listId = readResult(parser,name);
            }
            if (name.equals("result")) {
                result = readResult(parser,name);
            }
        }

        return new ListCreateResponse(listId,result);
    }


    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[name]",params[0]);
    }
}
