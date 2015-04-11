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
import in.savegenie.savegenie.backgroundclasses.CompareStoreItem;
import in.savegenie.savegenie.backgroundclasses.MissingItem;
import in.savegenie.savegenie.backgroundclasses.SelectedProduct;
import in.savegenie.savegenie.responses.CompareStoreListResponse;

public class GetCompareStoreListAsyncTask extends
        AsyncTask<String, Integer, CompareStoreListResponse>
{

	String responseStr;
	InputStream inputStream;
	InternetURL url = new InternetURL();
	Context context;

	@Override
	protected CompareStoreListResponse doInBackground(String... params) {
		// TODO Auto-generated method stub
		inputStream = postDataGetStream(params[0], params[1]);
		if (inputStream == null) {
			return null;
		}
		CompareStoreListResponse response = null;
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
	protected void onPostExecute(CompareStoreListResponse response) {
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
	}

	public InputStream postDataGetStream(String listId, String clickButtonInfo) {
		// Create a new HttpClient and Post Header
		AppHttpClient appHttpClient = AppHttpClient.getAppHttpClientInstance();
		HttpClient httpclient = appHttpClient.getHttpClient();
		HttpPost httppost = new HttpPost(url.GET_COMPARE_STORE_ITEMS);
		HttpResponse httpResponse;
		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("data[cartId]", listId));
			nameValuePairs.add(new BasicNameValuePair("data[clickbuttoninfo]",
					clickButtonInfo));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			httpResponse = httpclient.execute(httppost);
			responseStr = EntityUtils.toString(httpResponse.getEntity());
			Log.d("deb12", responseStr);
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

	CompareStoreListResponse parse(InputStream inputStream)
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

	CompareStoreListResponse readResponse(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<CompareStoreItem> storeList = new ArrayList<CompareStoreItem>();
		ArrayList<SelectedProduct> productList = new ArrayList<SelectedProduct>();

		parser.require(XmlPullParser.START_TAG, null, "response");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("store")) {
				storeList.add(readStoreListItem(parser));
			}
		}

		return new CompareStoreListResponse(storeList, productList);
	}

	CompareStoreItem readStoreListItem(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "store");
		ArrayList<MissingItem> missingItems = new ArrayList<MissingItem>();
		ArrayList<AlternateItem> alternateItems = new ArrayList<AlternateItem>();
		String price = null;
		String deliveryCost = null;
		String totalCost = null;
		String imgName = null;
		String storeId = null;
		String storeName = null;
		String storeAddress = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("id")) {
				storeId = readStoreId(parser);
			}
			if (name.equals("name")) {
				storeName = readStoreName(parser);
			}
			if (name.equals("address")) {
				storeAddress = readStoreAddress(parser);
			}
			if (name.equals("image")) {
				imgName = readImgName(parser);
			}
			if (name.equals("orderprice")) {
				price = readPrice(parser);
			}
			if (name.equals("delcost")) {
				deliveryCost = readDeliveryCost(parser);
			}
			if (name.equals("netprice")) {
				totalCost = readTotalCost(parser);
			}
			if (name.equals("alternate")) {
				alternateItems = readAlternateItemList(parser);
			}
			if (name.equals("missing")) {
				missingItems = readMissingItemList(parser);
			}
		}

		return new CompareStoreItem(missingItems, alternateItems, price,
				deliveryCost, totalCost, imgName, storeId, storeName,storeAddress);
	}

	String readStoreId(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "id");
		String result = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "id");
		return result;
	}

	String readStoreName(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "name");
		String productskuid = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "name");
		return productskuid;
	}

	String readImgName(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "image");
		String productskuname = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "image");
		return productskuname;
	}

	String readStoreAddress(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "address");
		String productskuname = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "address");
		return productskuname;
	}

	String readPrice(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "orderprice");
		String cartid = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "orderprice");
		return cartid;
	}

	String readDeliveryCost(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "delcost");
		String count = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "delcost");
		return count;
	}

	String readTotalCost(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "netprice");
		String count = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "netprice");
		return count;
	}

	String readAlternateItems(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "alternate");
		String mrp = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "alternate");
		return mrp;
	}

	ArrayList<MissingItem> readMissingItemList(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<MissingItem> missingList = new ArrayList<MissingItem>();
		parser.require(XmlPullParser.START_TAG, null, "missing");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("product")) {
				missingList.add(readMissingItem(parser));
			}
		}

		return missingList;
	}

	ArrayList<AlternateItem> readAlternateItemList(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<AlternateItem> alternateItem = new ArrayList<AlternateItem>();
		parser.require(XmlPullParser.START_TAG, null, "alternate");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("product")) {
				alternateItem.add(readAlternateItem(parser));
			}
		}

		return alternateItem;
	}

	MissingItem readMissingItem(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "product");
		String productSkuId = null, size = null, unit = null, count = null, image = null, code = null, mrp = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("productSkuId")) {
				productSkuId = readResult(parser, name);
			} else if (name.equals("size")) {
				size = readResult(parser, name);
			} else if (name.equals("unit")) {
				unit = readResult(parser, name);
			} else if (name.equals("count")) {
				count = readResult(parser, name);
			} else if (name.equals("image")) {
				image = readResult(parser, name);
			} else if (name.equals("code")) {
				code = readResult(parser, name);
			} else if (name.equals("mrp")) {
				mrp = readResult(parser, name);
			}
		}

		return new MissingItem(productSkuId, size, unit, count, image, code,
				mrp);
	}

	AlternateItem readAlternateItem(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "product");
		String price = null, size = null, productSKUId = null, unit = null, image = null, name = null, storeId = null, count = null, storeSkuId = null, finalCut = null, effectivePrice = null, status = null, isSwappable = null, mrp = null, isdeal = null, value_index = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String str = parser.getName();
			if (str.equals("price")) {
				price = readResult(parser, str);
			} else if (str.equals("size")) {
				size = readResult(parser, str);
			} else if (str.equals("productSKUId")) {
				productSKUId = readResult(parser, str);
			} else if (str.equals("unit")) {
				unit = readResult(parser, str);
			} else if (str.equals("image")) {
				image = readResult(parser, str);
				Log.d("IMAGE", "IMAGE " + image);
			} else if (str.equals("name")) {
				name = readResult(parser, str);
			} else if (str.equals("storeId")) {
				storeId = readResult(parser, str);
			} else if (str.equals("count")) {
				count = readResult(parser, str);
			} else if (str.equals("storeSkuId")) {
				storeSkuId = readResult(parser, str);
			} else if (str.equals("finalCut")) {
				finalCut = readResult(parser, str);
			} else if (str.equals("effectivePrice")) {
				effectivePrice = readResult(parser, str);
			} else if (str.equals("status")) {
				status = readResult(parser, str);
			} else if (str.equals("isSwappable")) {
				isSwappable = readResult(parser, str);
			} else if (str.equals("mrp")) {
				mrp = readResult(parser, str);
			} else if (str.equals("isdeal")) {
				isdeal = readResult(parser, str);
			} else if (str.equals("value_index")) {
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
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

}
