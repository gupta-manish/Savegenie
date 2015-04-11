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

import in.savegenie.savegenie.backgroundclasses.Day;
import in.savegenie.savegenie.responses.PickDelDayDateResponse;

public class GetPickDelDayDateAsyncTask extends
        AsyncTask<String, Integer, PickDelDayDateResponse>
{

    String responseStr;
    InputStream inputStream;
    InternetURL url = new InternetURL();
    Context context;

    @Override
    protected PickDelDayDateResponse doInBackground(String... params)
    {
        // TODO Auto-generated method stub
        inputStream = postDataGetStream(params[0], params[1], params[2]);
        if (inputStream == null)
        {
            return null;
        }
        PickDelDayDateResponse response = null;
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
    protected void onPostExecute(PickDelDayDateResponse response)
    {
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    public InputStream postDataGetStream(String storeid, String pickdel, String confirm)
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httppost = new HttpPost(url.GET_DATE_DETAILS);
        HttpResponse httpResponse;
        try
        {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("data[storeid]", storeid));
            nameValuePairs.add(new BasicNameValuePair("data[pickdel]", pickdel));
            nameValuePairs.add(new BasicNameValuePair("data[confirm]", confirm));
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

    PickDelDayDateResponse parse(InputStream inputStream)
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

    PickDelDayDateResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        ArrayList<Day> dayList = new ArrayList<Day>();

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
                dayList.add(readSlot(parser));
            }
        }

        return new PickDelDayDateResponse(dayList);
    }

    Day readSlot(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        String dayName = null, date = null, available = null;
        parser.require(XmlPullParser.START_TAG, null, "slot");
        while (parser.next() != XmlPullParser.END_TAG)
        {
            if (parser.getEventType() != XmlPullParser.START_TAG)
            {
                continue;
            }
            String name = parser.getName();

            if (name.equals("dayname"))
            {
                dayName = readResult(parser, name);
            }
            else if (name.equals("date"))
            {
                date = readResult(parser, name);
            }
            else if (name.equals("available"))
            {
                available = readResult(parser, name);
            }

        }
        return new Day(dayName, date, available);
    }


    String readResult(XmlPullParser parser, String name) throws XmlPullParserException, IOException
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
