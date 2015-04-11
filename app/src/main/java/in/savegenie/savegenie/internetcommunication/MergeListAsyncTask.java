package in.savegenie.savegenie.internetcommunication;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.savegenie.savegenie.responses.MergeListResponse;

public class MergeListAsyncTask extends
        BasicAsyncTask<MergeListResponse> {

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_MERGED_LIST;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data",params[0]);
        Log.d("data",params[0]);
    }

    MergeListResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String listid = null;
        String result = null;
        String listname = null;
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
            if (name.equals("cartname")) {
                listname = readResult(parser,name);
            }
            if(name.equals("cartId"))
            {
                listid = readResult(parser,name);
            }
            if(name.equals("count"))
            {
                count = readResult(parser,name);;
            }
        }

        return new MergeListResponse(result, listid, listname,count);
    }
}
