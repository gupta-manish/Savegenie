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

import in.savegenie.savegenie.backgroundclasses.GroceryList;
import in.savegenie.savegenie.responses.GroceryListsListResponse;

public class GetGroceryListsListAsyncTask extends
        AsyncTask<String, Integer, GroceryListsListResponse>
{

	String responseStr;
	InputStream inputStream;
	InternetURL url = new InternetURL();

	@Override
	protected GroceryListsListResponse doInBackground(String... params) {
		// TODO Auto-generated method stub

		inputStream = postDataGetStream();
		GroceryListsListResponse response = null;

		if (inputStream == null) {
			return null;
		}

		try {

			response = parse(inputStream);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return response;
	}

	@Override
	protected void onPostExecute(GroceryListsListResponse response) {

	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	public InputStream postDataGetStream() {
		// Create a new HttpClient and Post Header
		AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
		HttpClient httpclient = appHttpClient.getHttpClient();
		HttpPost httpget = new HttpPost(url.USER_SERVED_LISTS);

		try {

			HttpResponse response = httpclient.execute(httpget);
			responseStr = EntityUtils.toString(response.getEntity());
			Log.d("Store List", responseStr);
			return new ByteArrayInputStream(responseStr.getBytes());

		} catch (ConnectTimeoutException e) {
			return null;
		} catch (SocketTimeoutException e) {
			return null;
			// TODO Auto-generated catch block
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

	}

	GroceryListsListResponse parse(InputStream inputStream)
			throws XmlPullParserException, IOException
    {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readResponse(parser);
		} finally {
			inputStream.close();
		}
	}

	GroceryListsListResponse readResponse(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<GroceryList> groceryListsList = new ArrayList<GroceryList>();
		int count = 0;
		parser.require(XmlPullParser.START_TAG, null, "response");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals("list")) {
				groceryListsList.add(readGroceryList(parser));
			}
			if (name.equals("count")) {
				count = Integer.parseInt(readCount(parser));
			}
		}

		return new GroceryListsListResponse(groceryListsList,count);
	}

	GroceryList readGroceryList(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		String groceryListName = null, groceryListId = null;
		parser.require(XmlPullParser.START_TAG, null, "list");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals("name")) {
				groceryListName = readGroceryListName(parser);
			} else if (name.equals("id")) {
				groceryListId = readGroceryListId(parser);
			}
		}
		return new GroceryList(groceryListName, groceryListId);
	}

	String readGroceryListName(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "name");
		String storeName = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "name");
		return storeName;
	}

	String readGroceryListId(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "id");
		String storeId = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "id");
		return storeId;
	}

	String readCount(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "count");
		String count = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "count");
		return count;
	}

	private String readText(XmlPullParser parser) throws IOException,
            XmlPullParserException
    {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

}
