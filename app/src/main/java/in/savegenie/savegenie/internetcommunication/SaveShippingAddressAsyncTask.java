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

public class SaveShippingAddressAsyncTask extends BaseAsyncTask<String>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.SAVE_NEW_SHIPPING_ADDRESS;
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
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][name]",params[0]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][address]",params[1]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][areaName]",params[2]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][areaId]",params[3]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][city]",params[4]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][mobileno]",params[5]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][socity_name]",params[6]));
        nameValuePairs.add(new BasicNameValuePair("data[ShippingAddress][landmark]",params[7]));
        return  new UrlEncodedFormEntity(nameValuePairs);
    }
}
