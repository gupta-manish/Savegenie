package in.savegenie.savegenie.internetcommunication;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import in.savegenie.savegenie.backgroundclasses.AlternateItem;
import in.savegenie.savegenie.responses.MoreOptionsResponse;

public class GetMoreAlternateOptionsAsyncTask extends
        AsyncTask<String, Integer, MoreOptionsResponse>
{

    String responseStr;
    InputStream inputStream;
    InternetURL url = new InternetURL();
    Context context;

    @Override
    protected MoreOptionsResponse doInBackground(String... params)
    {
        // TODO Auto-generated method stub
        inputStream = postDataGetStream(params);
        if (inputStream == null)
        {
            return null;
        }
        MoreOptionsResponse response = null;
        try
        {
            response = parse(inputStream);
        }
        catch (XmlPullParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return response;
    }

    @Override
    protected void onPostExecute(MoreOptionsResponse response)
    {
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    public InputStream postDataGetStream(String... params)
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httppost = new HttpPost(url.GET_MORE_OPTIONS);
        HttpResponse httpResponse;
        try
        {
            // Add your data
            Log.d("Params", params[0] + " " + params[1] + " " + params[2] + " " + params[3]);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("data[storeId]", params[0]));
            nameValuePairs.add(new BasicNameValuePair("data[virtualstore]", "0"));
            nameValuePairs.add(new BasicNameValuePair("data[selectedstoresid]", params[1]));
            nameValuePairs.add(new BasicNameValuePair("data[oneProductData]", params[2]));
            nameValuePairs.add(new BasicNameValuePair("data[evenodd]", "0"));
            nameValuePairs.add(new BasicNameValuePair("data[productskuid]", params[3]));
            nameValuePairs.add(new BasicNameValuePair("data[flag]", params[4]));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            httpResponse = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(httpResponse.getEntity());
            Log.d("deb12", responseStr);
            return new ByteArrayInputStream(responseStr.getBytes());

        }
        catch (ConnectTimeoutException e)
        {
            return null;
        }
        catch (SocketTimeoutException e)
        {
            return null;
            // TODO Auto-generated catch block
        }
        catch (ClientProtocolException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }

    }

    MoreOptionsResponse parse(InputStream inputStream)
            throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readResponse(parser);
        }
        catch (XmlPullParserException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {
            inputStream.close();
        }
        return null;
    }

    MoreOptionsResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<AlternateItem> productList = new ArrayList<AlternateItem>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("similarStoreSkus"))
            {
                productList = readSimilarProductList(parser);
            }
            if (name.equals("products"))
            {
                reachEndTag(parser);
            }
        }

        return new MoreOptionsResponse(productList);
    }

    void reachEndTag(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "products");
        String productSkuId = null, size = null, unit = null, code = null, image = null, mrp = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String str = parser.getName();
            if (str.equals("productSkuId"))
            {
                productSkuId = readResult(parser, str);
            }
            else if (str.equals("size"))
            {
                size = readResult(parser, str);
            }
            else if (str.equals("unit"))
            {
                unit = readResult(parser, str);
            }
            else if (str.equals("code"))
            {
                code = readResult(parser, str);
            }
            else if (str.equals("image"))
            {
                image = readResult(parser, str);
            }
            else if (str.equals("mrp"))
            {
                mrp = readResult(parser, str);
            }
        }
    }

    ArrayList<AlternateItem> readSimilarProductList(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<AlternateItem> alternateItem = new ArrayList<AlternateItem>();
        parser.require(XmlPullParser.START_TAG, null, "similarStoreSkus");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("product"))
            {
                alternateItem.add(readAlternateItem(parser));
            }
        }

        return alternateItem;
    }

    AlternateItem readAlternateItem(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "product");
        String price = null, size = null, productSKUId = null, unit = null, image = null, name = null, storeId = null, count = null, storeSkuId = null, finalCut = null, effectivePrice = null, status = null, isSwappable = null, mrp = null, isdeal = null, value_index = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String str = parser.getName();
            if (str.equals("price"))
            {
                price = readResult(parser, str);
            }
            else if (str.equals("size"))
            {
                size = readResult(parser, str);
            }
            else if (str.equals("productSKUId"))
            {
                productSKUId = readResult(parser, str);
            }
            else if (str.equals("unit"))
            {
                unit = readResult(parser, str);
            }
            else if (str.equals("image"))
            {
                image = readResult(parser, str);
                Log.d("IMAGE", "IMAGE " + image);
            }
            else if (str.equals("name"))
            {
                name = readResult(parser, str);
            }
            else if (str.equals("storeId"))
            {
                storeId = readResult(parser, str);
            }
            else if (str.equals("count"))
            {
                count = readResult(parser, str);
            }
            else if (str.equals("storeSkuId"))
            {
                storeSkuId = readResult(parser, str);
            }
            else if (str.equals("finalCut"))
            {
                finalCut = readResult(parser, str);
            }
            else if (str.equals("effectivePrice"))
            {
                effectivePrice = readResult(parser, str);
            }
            else if (str.equals("status"))
            {
                status = readResult(parser, str);
            }
            else if (str.equals("isSwappable"))
            {
                isSwappable = readResult(parser, str);
            }
            else if (str.equals("mrp"))
            {
                mrp = readResult(parser, str);
            }
            else if (str.equals("isdeal"))
            {
                isdeal = readResult(parser, str);
            }
            else if (str.equals("value_index"))
            {
                status = readResult(parser, str);
            }
        }

        return new AlternateItem(price, size, productSKUId, unit, image, name,
                storeId, count, storeSkuId, finalCut, effectivePrice, status,
                isSwappable, mrp, isdeal, value_index);
    }

    String readResult(XmlPullParser parser, String name)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, name);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, name);
        return result;
    }

    private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException
    {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT)
        {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

}
