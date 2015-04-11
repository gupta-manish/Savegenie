package in.savegenie.savegenie.internetcommunication;

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
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public abstract class BasicAsyncTask<ResponseType> extends AsyncTask<String, Integer, ResponseType>
{

    private String responseStr;
    private InputStream inputStream;
    private List<NameValuePair> nameValuePairs;

    @Override
    protected ResponseType doInBackground(String... params)
    {
        // TODO Auto-generated method stub
        inputStream = postDataGetStream(params);
        if (inputStream == null)
        {
            return null;
        }
        ResponseType response = null;
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
    protected void onPostExecute(ResponseType response)
    {
    }

    @Override
    protected void onProgressUpdate(Integer... progress)
    {
    }

    protected InputStream postDataGetStream(String... params)
    {
        // Create a new HttpClient and Post Header
        AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
        HttpClient httpclient = appHttpClient.getHttpClient();
        HttpPost httppost = new HttpPost(getUrlLink());
        HttpResponse httpResponse;
        try
        {
            if(params.length > 0)
            {
                UrlEncodedFormEntity urlEncodedFormEntity = getUrlEncodedFormEntity(params);

                httppost.setEntity(urlEncodedFormEntity);

                // Execute HTTP Post Request
                httpResponse = httpclient.execute(httppost);
            }
            else
            {
                Log.d("hahahah", "hahahaha");
                httpResponse = httpclient.execute(httppost);
            }

            responseStr = EntityUtils.toString(httpResponse.getEntity());
            Log.d("deb12", responseStr);
            responseStr = responseStr.replaceAll("&", "and");
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

    ResponseType parse(InputStream inputStream) throws XmlPullParserException,
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


    protected String readResult(XmlPullParser parser, String name) throws XmlPullParserException,
            IOException
    {
        parser.require(XmlPullParser.START_TAG, null, name);
        String result = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, name);
        return result;
    }

    protected String readText(XmlPullParser parser) throws IOException,
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


    abstract String getUrlLink();

    abstract ResponseType readResponse(XmlPullParser parser) throws XmlPullParserException,
            IOException;

    UrlEncodedFormEntity getUrlEncodedFormEntity(String... params) throws UnsupportedEncodingException
    {
        nameValuePairs = new ArrayList<NameValuePair>();
        addPostData(params);
        return  new UrlEncodedFormEntity(nameValuePairs);
    }


    void addDataToPostVariable(String postVariable,String data)
    {
        nameValuePairs.add(new BasicNameValuePair(postVariable,data));
    }

    abstract void addPostData(String... params);

}
