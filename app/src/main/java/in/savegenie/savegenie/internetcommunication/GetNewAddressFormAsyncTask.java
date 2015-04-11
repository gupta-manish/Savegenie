package in.savegenie.savegenie.internetcommunication;


import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import in.savegenie.savegenie.responses.AddressFormResponse;

public class GetNewAddressFormAsyncTask extends BaseAsyncTask<AddressFormResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.ENTER_NEW_SHIPPING_ADDRESS;
    }

    @Override
    AddressFormResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String areaName, areaid, cityid, mobileno, cityName;
        areaName = parser.getAttributeValue(null, "areaName");
        areaid = parser.getAttributeValue(null, "areaId");
        cityid = parser.getAttributeValue(null, "cityId");
        cityName = parser.getAttributeValue(null, "cityName");
        mobileno = parser.getAttributeValue(null, "mobileno");
        return new AddressFormResponse(areaName, areaid, cityName, cityid, mobileno);
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        return null;
    }
}
