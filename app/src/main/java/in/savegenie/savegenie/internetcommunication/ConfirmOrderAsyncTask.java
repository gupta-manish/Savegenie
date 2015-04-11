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

import in.savegenie.savegenie.responses.ConfirmOrderResponse;

/**
 * Created by Devansh on 16-Jan-15.
 */



public class ConfirmOrderAsyncTask extends BaseAsyncTask<ConfirmOrderResponse>
{
    @Override
    String getUrlLink()
    {
       return InternetURL.CONFIRM_ORDER;
    }

    @Override
    ConfirmOrderResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String key = null, hash = null, txnid = null, amount = null, firstname = null,
                email = null, phone = null, productinfo = null, lastname = null,
                address1 = null, address2 = null, city = null, state = null, country = null,
                zipcode = null, udf1 = null, udf2 = null, udf5 = null, mode = null,
                discount = null, delcost = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result"))
            {
                key = parser.getAttributeValue(null,"key");
                hash = parser.getAttributeValue(null,"hash");
                txnid = parser.getAttributeValue(null,"txnid");
                amount = parser.getAttributeValue(null,"amount");
                firstname = parser.getAttributeValue(null,"firstname");
                email = parser.getAttributeValue(null,"email");
                phone = parser.getAttributeValue(null,"phone");
                productinfo = parser.getAttributeValue(null,"productinfo");
                lastname = parser.getAttributeValue(null,"lastname");
                address1 = parser.getAttributeValue(null,"address1");
                address2 = parser.getAttributeValue(null,"address2");
                city = parser.getAttributeValue(null,"city");
                state = parser.getAttributeValue(null,"state");
                country = parser.getAttributeValue(null,"country");
                zipcode = parser.getAttributeValue(null,"zipcode");
                udf1 = parser.getAttributeValue(null,"udf1");
                udf2 = parser.getAttributeValue(null,"udf2");
                udf5 = parser.getAttributeValue(null,"udf5");
                mode = parser.getAttributeValue(null,"mode");
                discount = parser.getAttributeValue(null,"discount");
                delcost = parser.getAttributeValue(null,"delcost");
            }
        }
        return new ConfirmOrderResponse(key, hash, txnid, amount, firstname, email, phone, productinfo, lastname,
                address1, address2, city, state, country, zipcode, udf1, udf2, udf5, mode,
                discount, delcost);
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data[delchkaddr]", params[0]));
        nameValuePairs.add(new BasicNameValuePair("data[delchkaddress]", params[1]));
        nameValuePairs.add(new BasicNameValuePair("data[delchkname]", params[2]));
        nameValuePairs.add(new BasicNameValuePair("data[delchkcity]", params[3]));
        nameValuePairs.add(new BasicNameValuePair("data[delchkarea]", params[4]));
        nameValuePairs.add(new BasicNameValuePair("data[delchkmob]", params[5]));
        nameValuePairs.add(new BasicNameValuePair("data[order]["+params[10]+"][pickupDelivery]", params[6]));
        nameValuePairs.add(new BasicNameValuePair("data[order][selectedStoreSlot]", params[7]));
        nameValuePairs.add(new BasicNameValuePair("data[order][selectDate]", params[8]));
        nameValuePairs.add(new BasicNameValuePair("data[order][pickupDelivery]", params[9]));
        nameValuePairs.add(new BasicNameValuePair("data[order][storeId]", params[10]));
        nameValuePairs.add(new BasicNameValuePair("data[order][discount]", params[11]));
        nameValuePairs.add(new BasicNameValuePair("data[order][sgenieCouponCode]", params[12]));
        nameValuePairs.add(new BasicNameValuePair("data[order][totalRupees]", params[13]));
        nameValuePairs.add(new BasicNameValuePair("data[ordergenerate][OrdId]", params[14]));
        nameValuePairs.add(new BasicNameValuePair("data[referral][chekuncheckid]", params[15]));
        return new UrlEncodedFormEntity(nameValuePairs);
    }
}
