package in.savegenie.savegenie.internetcommunication;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.ShippingAddress;
import in.savegenie.savegenie.responses.ShippingAddressResponse;

public class GetShippingAddressListAsyncTask extends BaseAsyncTask<ShippingAddressResponse>
{

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_SHIPPING_ADDRESS_LIST;
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        return null;
    }

    @Override
    ShippingAddressResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<ShippingAddress> addressList = new ArrayList<ShippingAddress>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String str = parser.getName();

            if (str.equals("shippingaddress"))
            {
                String id=null,name=null,address=null,area=null,city=null,mobileno=null;
                id = parser.getAttributeValue(null,"id");
                name = parser.getAttributeValue(null,"name");
                address = parser.getAttributeValue(null,"address");
                area = parser.getAttributeValue(null,"area");
                city = parser.getAttributeValue(null,"city");
                mobileno = parser.getAttributeValue(null,"mobileno");
                addressList.add(new ShippingAddress(id,name,address,area,city,mobileno));
                parser.nextTag();
            }
        }

        return new ShippingAddressResponse(addressList);
    }




}
