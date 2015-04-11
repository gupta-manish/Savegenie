package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import in.savegenie.savegenie.responses.SendSelectedStoreResponse;

public class SendSelectedStoreAsyncTask extends BasicAsyncTask<SendSelectedStoreResponse> {

    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        return InternetURL.SEND_SELECTED_STORE;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[name]",params[0]);
        addDataToPostVariable("data[storeId]",params[1]);
    }

    @Override
    SendSelectedStoreResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String result = null,listId = null,listName = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result")) {
                result = readResult(parser,name);
            }
            if(name.equals("listid"))
            {
                listId = readResult(parser,name);
            }
            if(name.equals("listname"))
            {
                listName = readResult(parser,name);
            }
        }

        return new SendSelectedStoreResponse(result,listId,listName);
    }

}
