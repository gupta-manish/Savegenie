package in.savegenie.savegenie.internetcommunication;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.util.EntityUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Store;
import in.savegenie.savegenie.responses.StoreListResponse;

public class GetStoreListAsyncTask extends
        AsyncTask<String, Integer, StoreListResponse>
{

    String responseStr;
    InputStream inputStream;
    InternetURL url = new InternetURL();

    @Override
    protected StoreListResponse doInBackground(String... params)
    {
        // TODO Auto-generated method stub

        inputStream = postDataGetStream();
        StoreListResponse response = null;

        if (inputStream == null)
        {
            return null;
        }

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
    protected void onPostExecute(StoreListResponse response)
    {

    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    public InputStream postDataGetStream()
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httpget = new HttpPost(
                url.USER_SERVED_STORES);

        try
        {

            HttpResponse response = httpclient.execute(httpget);
            responseStr = EntityUtils.toString(response.getEntity());
            Log.d("Store List", responseStr);
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

    StoreListResponse parse(InputStream inputStream) throws XmlPullParserException,
            IOException
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readResponse(parser);
        }
        finally
        {
            inputStream.close();
        }
    }

    StoreListResponse readResponse(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        ArrayList<Store> storeList = new ArrayList<Store>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("store"))
            {
                storeList.add(readStore(parser));
            }
        }

        return new StoreListResponse(storeList);
    }

    Store readStore(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        String storeName = null, storeId = null, imgName = null;
        parser.require(XmlPullParser.START_TAG, null, "store");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("name"))
            {
                storeName = readStoreName(parser);
            }
            else if (name.equals("id"))
            {
                storeId = readStoreId(parser);
            }
            else if (name.equals("imagename"))
            {
                imgName = readImageName(parser);
            }
        }
        return new Store(storeName, storeId, imgName);
    }

    String readStoreName(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "name");
        String storeName = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "name");
        return storeName;
    }

    String readStoreId(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "id");
        String storeId = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "id");
        return storeId;
    }

    String readImageName(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "imagename");
        String storeId = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "imagename");
        return storeId;
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
