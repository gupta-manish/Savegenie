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

import in.savegenie.savegenie.backgroundclasses.SelectedProduct;
import in.savegenie.savegenie.backgroundclasses.SimilarStoreSkus;
import in.savegenie.savegenie.backgroundclasses.StoreSkuData;
import in.savegenie.savegenie.backgroundclasses.SwapProduct;
import in.savegenie.savegenie.responses.ReviewResponse;

public class GetReviewBasketProductsAsyncTask extends
        AsyncTask<String, Integer, ReviewResponse>
{

    String responseStr;
    InputStream inputStream;
    InternetURL url = new InternetURL();
    Context context;

    @Override
    protected ReviewResponse doInBackground(String... params)
    {
        // TODO Auto-generated method stub
        inputStream = postDataGetReviewStream(params[0], params[1]);
        if (inputStream == null)
        {
            return null;
        }
        ArrayList<SelectedProduct> productList = new ArrayList<SelectedProduct>();
        ArrayList<SwapProduct> swapProductList = new ArrayList<SwapProduct>();
        try
        {
            productList = parseReview(inputStream);
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

        inputStream = postDataGetSwapStream(params[2], params[3], productList);
        try
        {
            swapProductList = parseSwap(inputStream);
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

        return new ReviewResponse(productList, swapProductList);
    }

    @Override
    protected void onPostExecute(ReviewResponse response)
    {
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    public InputStream postDataGetReviewStream(String listId,
                                               String clickButtonInfo)
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httppost = new HttpPost(url.GET_REVIEW_ITEMS);
        HttpResponse httpResponse;
        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("data[cartId]", listId));
            nameValuePairs.add(new BasicNameValuePair("data[clickbuttoninfo]",
                    clickButtonInfo));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            httpResponse = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(httpResponse.getEntity());
            Log.d("Review", responseStr);
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

    public InputStream postDataGetSwapStream(String userStoreId,
                                             String selectedStoreIds, ArrayList<SelectedProduct> productList)
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httppost = new HttpPost(url.GET_SWAP_RESPONSE);
        HttpResponse httpResponse;
        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            Log.d("data[userstoreid]", "data[userstoreid] " + userStoreId);

            nameValuePairs.add(new BasicNameValuePair("data[userstoreid]",
                    userStoreId));
            Log.d("data[vertualstoreids]", "data[vertualstoreids] " + 0);
            nameValuePairs.add(new BasicNameValuePair("data[vertualstoreids]",
                    "0"));
            Log.d("data[selectedstoreid]", "data[selectedstoreid] "
                    + selectedStoreIds);
            nameValuePairs.add(new BasicNameValuePair("data[selectedstoresid]",
                    selectedStoreIds));

            for (int i = 0; i < productList.size(); i++)
            {
                if ((productList.get(i).storeSkuDataList.get(0).found)
                        .contains("1"))
                {
                    Log.d("data[order][" + userStoreId + "][orderItems][orig]["
                                    + i + "][items]",
                            "data[order]["
                                    + userStoreId
                                    + "][orderItems][orig]["
                                    + i
                                    + "][items] "
                                    + productList.get(i).productSkuId
                                    + "-"
                                    + productList.get(i).storeSkuDataList
                                    .get(0).store_sku_id + "-"
                                    + productList.get(i).count
                    );
                    nameValuePairs.add(new BasicNameValuePair("data[order]["
                            + userStoreId + "][orderItems][orig][" + i
                            + "][items]",
                            productList.get(i).productSkuId
                                    + "-"
                                    + productList.get(i).storeSkuDataList
                                    .get(0).store_sku_id + "-"
                                    + productList.get(i).count
                    ));
                }
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            httpResponse = httpclient.execute(httppost);
            responseStr = EntityUtils.toString(httpResponse.getEntity());
            Log.d("Review", responseStr);
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

    ArrayList<SelectedProduct> parseReview(InputStream inputStream)
            throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readReviewResponse(parser);
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

    ArrayList<SwapProduct> parseSwap(InputStream inputStream)
            throws XmlPullParserException, IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readSwapResponse(parser);
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

    ArrayList<SelectedProduct> readReviewResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<SelectedProduct> productList = new ArrayList<SelectedProduct>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("products"))
            {
                productList.add(readSelectedProduct(parser));
            }
        }

        return productList;
    }

    ArrayList<SwapProduct> readSwapResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<SwapProduct> productList = new ArrayList<SwapProduct>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("product"))
            {
                productList.add(readSwapProduct(parser));
            }
        }

        return productList;
    }

    SelectedProduct readSelectedProduct(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "products");
        String productSkuId = null, size = null, unit = null, count = null, image = null, code = null, mrp = null;
        ArrayList<StoreSkuData> storeSkuDataList = new ArrayList<StoreSkuData>();
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("productSkuId"))
            {
                productSkuId = readResult(parser, name);
            }
            else if (name.equals("size"))
            {
                size = readResult(parser, name);
            }
            else if (name.equals("unit"))
            {
                unit = readResult(parser, name);
            }
            else if (name.equals("count"))
            {
                count = readResult(parser, name);
            }
            else if (name.equals("image"))
            {
                image = readResult(parser, name);
            }
            else if (name.equals("code"))
            {
                code = readResult(parser, name);
            }
            else if (name.equals("mrp"))
            {
                mrp = readResult(parser, name);
            }
            else if (name.equals("storeskudata"))
            {
                storeSkuDataList.add(readStoreSkuData(parser));
            }

        }

        return new SelectedProduct(productSkuId, size, unit, count, image,
                code, mrp, storeSkuDataList);
    }

    SwapProduct readSwapProduct(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "product");
        String productId = null, storeId = null, saving = null;
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("productId"))
            {
                productId = readResult(parser, name);
            }
            else if (name.equals("storeId"))
            {
                storeId = readResult(parser, name);
            }
            else if (name.equals("savings"))
            {
                saving = readResult(parser, name);
            }

        }

        return new SwapProduct(productId, storeId, saving);
    }

    StoreSkuData readStoreSkuData(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "storeskudata");
        String ProductSkuId = null, store_id = null, store_name = null, store_image = null, price = null, store_sku_id = null, name1 = null, image = null, size = null, unit = null, status = null, count = null, mrp = null, value_index = null, isdeal = null, costPerUnit = null, found = null;
        ArrayList<SimilarStoreSkus> similarStoreSkusList = new ArrayList<SimilarStoreSkus>();
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("ProductSkuId"))
            {
                ProductSkuId = readResult(parser, name);
            }
            else if (name.equals("store_id"))
            {
                store_id = readResult(parser, name);
            }
            else if (name.equals("store_name"))
            {
                store_name = readResult(parser, name);
            }
            else if (name.equals("store_image"))
            {
                store_image = readResult(parser, name);
            }
            else if (name.equals("price"))
            {
                price = readResult(parser, name);
            }
            else if (name.equals("store_sku_id"))
            {
                store_sku_id = readResult(parser, name);
            }
            else if (name.equals("name"))
            {
                name1 = readResult(parser, name);
            }
            else if (name.equals("image"))
            {
                image = readResult(parser, name);
            }
            else if (name.equals("size"))
            {
                size = readResult(parser, name);
            }
            else if (name.equals("unit"))
            {
                unit = readResult(parser, name);
            }
            else if (name.equals("status"))
            {
                status = readResult(parser, name);
            }
            else if (name.equals("count"))
            {
                count = readResult(parser, name);
            }
            else if (name.equals("mrp"))
            {
                mrp = readResult(parser, name);
            }
            else if (name.equals("value_index"))
            {
                value_index = readResult(parser, name);
            }
            else if (name.equals("isdeal"))
            {
                isdeal = readResult(parser, name);
            }
            else if (name.equals("costPerUnit"))
            {
                costPerUnit = readResult(parser, name);
            }
            else if (name.equals("found"))
            {
                found = readResult(parser, name);
            }
            else if (name.equals("similarStoreSkus"))
            {
                similarStoreSkusList = readSimilarStoreSkuList(parser);
            }

        }

        return new StoreSkuData(ProductSkuId, store_id, store_name,
                store_image, price, store_sku_id, name1, image, size, unit,
                status, count, mrp, value_index, isdeal, costPerUnit, found,
                similarStoreSkusList);
    }

    ArrayList<SimilarStoreSkus> readSimilarStoreSkuList(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "similarStoreSkus");
        ArrayList<SimilarStoreSkus> similarStoreSkusList = new ArrayList<SimilarStoreSkus>();
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();
            if (name.equals("similer"))
            {
                similarStoreSkusList.add(readSimilar(parser));
            }

        }
        return similarStoreSkusList;
    }

    SimilarStoreSkus readSimilar(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "similer");
        String price = null, size = null, productSKUId = null, unit = null, image = null, name1 = null, storeId = null, count = null, storeSkuId = null, finalCut = null, effectivePrice = null, status = null, isSwappable = null, mrp = null, isdeal = null, value_index = null;
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
                Log.d("name string", "name");
                image = readResult(parser, str);
            }
            else if (str.equals("name"))
            {
                name1 = readResult(parser, str);
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

        return new SimilarStoreSkus(price, size, productSKUId, unit, image,
                name1, storeId, count, storeSkuId, finalCut, effectivePrice,
                status, isSwappable, mrp, isdeal, value_index);
    }

    String readResult(XmlPullParser parser, String name)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, name);
        String result = readText(parser);
        Log.d(name, name + " " + result);
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
