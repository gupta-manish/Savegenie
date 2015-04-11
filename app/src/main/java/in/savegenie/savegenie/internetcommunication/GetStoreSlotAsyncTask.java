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

import in.savegenie.savegenie.backgroundclasses.Slot;
import in.savegenie.savegenie.responses.StoreSlotListResponse;

/**
 * Created by Devansh on 07-Jan-15.
 */
public class GetStoreSlotAsyncTask extends BaseAsyncTask<StoreSlotListResponse>
{

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_STORE_SLOT_DETAILS;
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data[storeid]", params[0]));
        nameValuePairs.add(new BasicNameValuePair("data[pickdel]", params[1]));
        nameValuePairs.add(new BasicNameValuePair("data[date]", params[2]));
        return new UrlEncodedFormEntity(nameValuePairs);
    }

    @Override
    StoreSlotListResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<Slot> slotList = new ArrayList<Slot>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("slot"))
            {
                slotList.add(readSlot(parser));
            }
        }

        return new StoreSlotListResponse(slotList);
    }

    Slot readSlot(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String id = null,time = null;
        parser.require(XmlPullParser.START_TAG, null, "slot");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("id"))
            {
                id = readResult(parser,name);
            }
            else if(name.equals("time"))
            {
                time = readResult(parser,name);
            }
        }
        return new Slot(id,time);
    }
}
