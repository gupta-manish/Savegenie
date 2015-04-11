package in.savegenie.savegenie.internetcommunication;

import android.util.Log;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Brand;
import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.backgroundclasses.SubType;
import in.savegenie.savegenie.responses.ProductListResponse;

public class GetProductListAsyncTask extends
		BasicAsyncTask<ProductListResponse> {

    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;


    @Override
    String getUrlLink()
    {
        return InternetURL.ANDROID_GET_PRODUCTS;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[ptid]",params[0]);
        addDataToPostVariable("data[filtertype]",params[1]);
        addDataToPostVariable("data[brandid]",params[2]);
        addDataToPostVariable("data[subtypeid]",params[3]);
    }

    ProductListResponse readResponse(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		ArrayList<Product> productList = new ArrayList<Product>();
		ArrayList<Brand> brandList = new ArrayList<Brand>();
        ArrayList<SubType> subTypeList = new ArrayList<SubType>();

		parser.require(XmlPullParser.START_TAG, null, "response");
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();

			if (name.equals("product")) {
				productList.add(readProduct(parser));
			}
			if (name.equals("brand")) {
				brandList.add(readBrand(parser));
			}
            if (name.equals("subtype")) {
                subTypeList.add(readSubType(parser));
            }
		}
		Log.d("Brand", brandList.size() + " Brand");
		return new ProductListResponse(productList,brandList,subTypeList);
	}

	Product readProduct(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "product");
		int paramId;

		Product product = new Product(new String[26]);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			paramId = product.getParamId(name);
			product.params[paramId] = readParamValue(parser, paramId);
			// Log.d(name,product.params[paramId]);
		}

		return product;
	}
	
	
	Brand readBrand(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "brand");
		String brandName = null,brandId = null,count = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals("name")) {
				brandName = readBrandName(parser);
			}
			if (name.equals("id")) {
				brandId = readBrandId(parser);
			}
            if (name.equals("count")) {
                count = readResult(parser,name);
            }
		}
		Log.d("BrandList", "Brand = " + brandName);

		return new Brand(brandName,brandId,count);
	}

    SubType readSubType(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        parser.require(XmlPullParser.START_TAG, null, "subtype");
        String subName = null,subId = null,count = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("name")) {
                subName = readResult(parser,name);
            }
            if (name.equals("id")) {
                subId = readResult(parser,name);
            }
            if (name.equals("count")) {
                count = readResult(parser,name);
            }
        }
        Log.d("BrandList", "Brand = " + subName);

        return new SubType(subName,subId,count);
    }

	String readParamValue(XmlPullParser parser, int paramId)
			throws IOException, XmlPullParserException
    {
		parser.require(XmlPullParser.START_TAG, null,
				Product.getParamName(paramId));
		try {
			String paramValue = readText(parser);
			parser.require(XmlPullParser.END_TAG, null,
					Product.getParamName(paramId));

			return paramValue;
		} catch (XmlPullParserException e) {
			Log.d("XML", "XML " + Product.getParamName(paramId));
			throw e;
		}
		
	}
	
	String readBrandName(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "name");
		String name = readText(parser);
		Log.d("BrandList", "Brand = " + name);
		parser.require(XmlPullParser.END_TAG, null, "name");
		return name;
	}
	
	String readBrandId(XmlPullParser parser)
			throws XmlPullParserException, IOException
    {
		parser.require(XmlPullParser.START_TAG, null, "id");
		String id = readText(parser);
		parser.require(XmlPullParser.END_TAG, null, "id");
		return id;
	}
	 

}
