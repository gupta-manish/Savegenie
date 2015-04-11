package in.savegenie.savegenie.internetcommunication;


import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import in.savegenie.savegenie.responses.CompleteOrderResponse;



/**
 * Created by Devansh on 21-Jan-15.
 */
public class CompleteOrderAsyncTask extends BasicAsyncTask<CompleteOrderResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.COMPLETE_ORDER;
    }

    @Override
    CompleteOrderResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String mode=null, orderId=null, email=null, loggedtime=null, ordercompletetime=null, totaldiscount=null, storename=null,
                txnid=null, priceatmrp=null, storeprice=null, priceafterdeal=null, deliverycharges=null, discount=null,
                netamount=null, pickupdelivery=null, address=null, phone=null, deliverydate=null, starttime=null, endtime=null;

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
                mode=parser.getAttributeValue(null,"mode");
                orderId=parser.getAttributeValue(null,"orderId");
                email=parser.getAttributeValue(null,"email");
                loggedtime=parser.getAttributeValue(null,"loggedtime");
                ordercompletetime=parser.getAttributeValue(null,"ordercompletetime");
                totaldiscount=parser.getAttributeValue(null,"totaldiscount");
                storename=parser.getAttributeValue(null,"storename");
                txnid=parser.getAttributeValue(null,"txnid");
                priceatmrp=parser.getAttributeValue(null,"priceatmrp");
                storeprice=parser.getAttributeValue(null,"storeprice");
                priceafterdeal=parser.getAttributeValue(null,"priceafterdeal");
                deliverycharges=parser.getAttributeValue(null,"deliverycharges");
                discount=parser.getAttributeValue(null,"discount");
                netamount=parser.getAttributeValue(null,"netamount");
                pickupdelivery=parser.getAttributeValue(null,"pickupdelivery");
                address=parser.getAttributeValue(null,"address");
                phone=parser.getAttributeValue(null,"phone");
                deliverydate=parser.getAttributeValue(null,"deliverydate");
                starttime=parser.getAttributeValue(null,"starttime");
                endtime=parser.getAttributeValue(null,"endtime");
            }
        }
        Log.d("email", email);
        Log.d("email", email);
        Log.d("email", email);
        Log.d("email", email);
        Log.d("email", email);
        Log.d("email", email);
        return  new CompleteOrderResponse(mode, orderId, email, loggedtime, ordercompletetime, totaldiscount, storename,
                txnid, priceatmrp, storeprice, priceafterdeal, deliverycharges, discount,
                netamount, pickupdelivery, address, phone, deliverydate, starttime, endtime);
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[key]",params[0]);
        addDataToPostVariable("data[txnid]",params[1]);
        addDataToPostVariable("data[hash]","");
        addDataToPostVariable("data[amount]",params[2]);
        addDataToPostVariable("data[firstname]",params[3]);
        addDataToPostVariable("data[email]",params[4]);
        addDataToPostVariable("data[phone]",params[5]);
        addDataToPostVariable("data[productinfo]",params[6]);
        addDataToPostVariable("data[lastname]",params[7]);
        addDataToPostVariable("data[address1]",params[8]);
        addDataToPostVariable("data[address2]",params[9]);
        addDataToPostVariable("data[shippingAddress]",params[10]);
        addDataToPostVariable("data[city]",params[11]);
        addDataToPostVariable("data[state]",params[12]);
        addDataToPostVariable("data[country]",params[13]);
        addDataToPostVariable("data[zipcode]",params[14]);
        addDataToPostVariable("data[udf1]",params[15]);
        addDataToPostVariable("data[udf2]",params[16]);
        addDataToPostVariable("data[udf5]",params[17]);
        addDataToPostVariable("data[udf6]",params[18]);
        addDataToPostVariable("data[mode]",params[19]);
        addDataToPostVariable("data[discount]",params[20]);
        addDataToPostVariable("data[del_cost]",params[21]);
        addDataToPostVariable("data[completeTime]",params[22]);

        Log.d("data[key]", params[0]);
        Log.d("data[txnid]", params[1]);
        Log.d("data[amount]", params[2]);
        Log.d("data[firstname]", params[3]);
        Log.d("data[email]", params[4]);
        Log.d("data[phone]", params[5]);
        Log.d("data[productinfo]", params[6]);
        Log.d("data[lastname]", params[7]);
        Log.d("data[address1]", params[8]);
        Log.d("data[address2]", params[9]);
        Log.d("data[shippingAddress]", params[10]);
        Log.d("data[city]", params[11]);
        Log.d("data[state]", params[12]);
        Log.d("data[country]", params[13]);
        Log.d("data[zipcode]", params[14]);
        Log.d("data[udf1]", params[15]);
        Log.d("data[udf2]", params[16]);
        Log.d("data[udf5]", params[17]);
        Log.d("data[udf6]", params[18]);
        Log.d("data[mode]", params[19]);
        Log.d("data[discount]", params[20]);
        Log.d("data[del_cost]", params[21]);
    }
}
