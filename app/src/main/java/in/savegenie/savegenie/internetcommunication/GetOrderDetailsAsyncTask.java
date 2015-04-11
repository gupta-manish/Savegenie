package in.savegenie.savegenie.internetcommunication;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
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

import in.savegenie.savegenie.backgroundclasses.Order;
import in.savegenie.savegenie.backgroundclasses.OrderItem;
import in.savegenie.savegenie.backgroundclasses.OrderStore;
import in.savegenie.savegenie.responses.OrderDetailResponse;

public class GetOrderDetailsAsyncTask extends
        AsyncTask<String, Integer, OrderDetailResponse>
{

	String responseStr;
	InputStream inputStream;
	InternetURL url = new InternetURL();
	Context context;
	private String storeListString;
	private String userStoreId;
	private String userStoreName, userStoreAddress, deliveryCost, bestPrice,
			virtualStoreIds;
	private ArrayList<String> finalList;

	public GetOrderDetailsAsyncTask(Bundle finalBundle) {
		userStoreId = finalBundle.getString("userstoreid");
		storeListString = finalBundle.getString("selectedstoresid");
		bestPrice = finalBundle.getString("best_price");
		virtualStoreIds = finalBundle.getString("vertualstoreids");
		deliveryCost = finalBundle.getString("deleveryCost");
		userStoreName = finalBundle.getString("storeName");
		userStoreAddress = finalBundle.getString("storeAddress");
		finalList = finalBundle.getStringArrayList("orderItems");
	}

	@Override
	protected OrderDetailResponse doInBackground(String... params) {
		// TODO Auto-generated method stub
		inputStream = postDataGetStream();
		if (inputStream == null) {
			return null;
		}
		OrderDetailResponse response = null;
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
	protected void onPostExecute(OrderDetailResponse response) {
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	public InputStream postDataGetStream() {
		// Create a new HttpClient and Post Header
		AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
		HttpClient httpclient = appHttpClient.getHttpClient();
		HttpPost httppost = new HttpPost(url.GET_ORDER_DETAILS);
		HttpResponse httpResponse;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data[userstoreid]",
					userStoreId));
			Log.d("data[userstoreid]", userStoreId);
			nameValuePairs.add(new BasicNameValuePair("data[vertualstoreids]",
					virtualStoreIds));
			Log.d("data[vertualstoreids]",
                    virtualStoreIds);
			nameValuePairs.add(new BasicNameValuePair("data[selectedstoresid]",
					storeListString));
			Log.d("data[selectedstoresid]",
                    storeListString);
			nameValuePairs.add(new BasicNameValuePair("data[order]["
					+ userStoreId + "][best_price]", bestPrice));
			Log.d("data[order]["
                    + userStoreId + "][best_price]", bestPrice);
			nameValuePairs.add(new BasicNameValuePair("data[order]["
					+ userStoreId + "][deleveryCost]", deliveryCost));
			Log.d("data[order]["
                    + userStoreId + "][deleveryCost]", deliveryCost);
			nameValuePairs.add(new BasicNameValuePair("data[order]["
					+ userStoreId + "][storeName]", userStoreName));
			Log.d("data[order]["
                    + userStoreId + "][storeName]", userStoreName);
			nameValuePairs.add(new BasicNameValuePair("data[order]["
					+ userStoreId + "][storeAddress]", userStoreAddress));
			Log.d("data[order]["
                    + userStoreId + "][storeAddress]", userStoreAddress);
			nameValuePairs.add(new BasicNameValuePair("data[order][origCart]",
					"1"));
			nameValuePairs.add(new BasicNameValuePair("data[area_id][areaId]",
					"57"));
			for (int i = 0; i < finalList.size(); i++) {
				nameValuePairs.add(new BasicNameValuePair(
						"data[order][" + userStoreId + "][orderItems][orig]["
								+ i + "][items]", finalList.get(i)));
				Log.d("data[order][" + userStoreId + "][orderItems][orig]["
                        + i + "][items]", finalList.get(i));
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			// Execute HTTP Post Request
			httpResponse = httpclient.execute(httppost);
			responseStr = EntityUtils.toString(httpResponse.getEntity());
			Log.d("deb12", responseStr);
			responseStr = responseStr.replaceAll("&","and");
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

	OrderDetailResponse parse(InputStream inputStream)
			throws XmlPullParserException, IOException
    {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readResponse(parser);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			inputStream.close();
		}
		return null;
	}

	OrderDetailResponse readResponse(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<OrderItem> orderItemList = new ArrayList<OrderItem>();
		Order order = null;
		OrderStore store = null;

		parser.require(XmlPullParser.START_TAG, null, "response");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("store")) {
				store = readOrderStore(parser);
			}
			if (name.equals("order")) {
				order = readOrder(parser);
			}
			if (name.equals("orderitem")) {
				orderItemList.add(readOrderItem(parser));
			}
		}

		return new OrderDetailResponse(orderItemList,order,store);
	}
	
	OrderStore readOrderStore(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {

		String id = null;
		String name = null;
		String address = null;
		String mos = null;
		String deliverycost = null;
		
		parser.require(XmlPullParser.START_TAG, null, "store");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String str = parser.getName();
			if (str.equals("id")) {
				id = readResult(parser, str);
			}
			if (str.equals("name")) {
				name = readResult(parser, str);
			}
			if (str.equals("address")) {
				address = readResult(parser, str);
			}
			if (str.equals("mos")) {
				mos = readResult(parser, str);
			}
			if (str.equals("deliverycost")) {
				deliverycost = readResult(parser, str);
			}
		}

		return new OrderStore(id,name,address,mos,deliverycost);
	}
	
	Order readOrder(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {

		String id = null;
		String priceatmrp = null;
		String storeprice = null;
		String priceafterdeal = null;
		String dealdiscount = null;
		String additionalcashback = null;
		String netprice = null;
		
		parser.require(XmlPullParser.START_TAG, null, "order");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String str = parser.getName();
			if (str.equals("id")) {
				id = readResult(parser, str);
			}
			if (str.equals("priceatmrp")) {
				priceatmrp = readResult(parser, str);
			}
			if (str.equals("storeprice")) {
				storeprice = readResult(parser, str);
			}
			if (str.equals("priceafterdeal")) {
				priceafterdeal = readResult(parser, str);
			}
			if (str.equals("dealdiscount")) {
				dealdiscount = readResult(parser, str);
			}
			if (str.equals("additionalcashback")) {
				additionalcashback = readResult(parser, str);
			}
			if (str.equals("netprice")) {
				netprice = readResult(parser, str);
			}
		}

		return new Order(id,priceatmrp,storeprice,priceafterdeal,dealdiscount,additionalcashback,netprice);
	}
	
	OrderItem readOrderItem(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {

		String productSkuName = null;
		String productName = null;
		String quantity = null;
		String mrp = null;
		String storesku = null;
        String price = null;
		
		parser.require(XmlPullParser.START_TAG, null, "orderitem");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String str = parser.getName();
			if (str.equals("productSkuName")) {
				productSkuName = readResult(parser, str);
			}
			if (str.equals("productName")) {
				productName = readResult(parser, str);
			}
			if (str.equals("quantity")) {
				quantity = readResult(parser, str);
			}
			if (str.equals("mrp")) {
				mrp = readResult(parser, str);
			}
			if (str.equals("storesku")) {
				storesku = readResult(parser, str);
			}
            if (str.equals("price")) {
                price = readResult(parser, str);
            }
		}

		return new OrderItem(productSkuName,productName,quantity,mrp,storesku,price);
	}
	
	

	String readResult(XmlPullParser parser, String str)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, str);
		String result = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, str);
		return result;
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
