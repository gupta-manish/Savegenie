package in.savegenie.savegenie.internetcommunication;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class AddRemoveItemToListAsyncTask extends BaseAsyncTask<String>
{

    @Override
    String getUrlLink()
    {
        return InternetURL.ADD_REMOVE_FROM_LIST;
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data[productSkuId]", params[0]));
        nameValuePairs.add(new BasicNameValuePair("data[plusminus]", params[1]));
        nameValuePairs.add(new BasicNameValuePair("data[count]", params[2]));
        try
        {
            nameValuePairs.add(new BasicNameValuePair("data[qtycount]", params[3]));
        }
        catch (Exception e)
        {
        }
        return new UrlEncodedFormEntity(nameValuePairs);
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

