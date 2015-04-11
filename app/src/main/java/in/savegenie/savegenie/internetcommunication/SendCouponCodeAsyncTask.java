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

import in.savegenie.savegenie.responses.CouponCodeResponse;

/**
 * Created by Devansh on 16-Jan-15.
 */
public class SendCouponCodeAsyncTask extends BaseAsyncTask<CouponCodeResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.APPLY_COUPON_CODE;
    }

    @Override
    CouponCodeResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String message = null, totalprice = null, couponprice = null, storecouponprice = null, netprice = null, couponcode = null;

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
                message = parser.getAttributeValue(null,"message");
                try
                {
                    totalprice = parser.getAttributeValue(null,"totalprice");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    couponprice = parser.getAttributeValue(null,"couponprice");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    storecouponprice = parser.getAttributeValue(null,"storecouponprice");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                try
                {
                    netprice = parser.getAttributeValue(null,"netprice");
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                couponcode = parser.getAttributeValue(null,"couponcode");
            }
        }

        return new CouponCodeResponse(message,totalprice,couponprice,storecouponprice,netprice,couponcode);
    }

    @Override
    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("data[order][sgenieCouponCode]", params[0]));
        nameValuePairs.add(new BasicNameValuePair("data[ordergenerate][OrdId]", params[1]));
        nameValuePairs.add(new BasicNameValuePair("data[order][totalRupees]", params[2]));
        nameValuePairs.add(new BasicNameValuePair("data[order][storeId]", params[3]));
        return new UrlEncodedFormEntity(nameValuePairs);
    }
}
