package in.savegenie.savegenie.backgroundclasses;

import android.os.AsyncTask;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class CategoryTree {

	private Category[] category;
	private static CategoryTree categoryTree;
	int categoryIndex;
	CreateCategoryTreeAsyncTask cctat;

	private CategoryTree(InputStream ins) {

		categoryIndex = 0;
		cctat = new CreateCategoryTreeAsyncTask();
		cctat.execute(ins);
		try {
			category = cctat.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private CategoryTree() {

	}

	public static void createCategoryTreeFromXmlAndStoreInDatabase(InputStream ins) {
		if (categoryTree == null) {
			categoryTree = new CategoryTree(ins);
		}
	}
	
	public static void createCategoryTreeFromDatabase()
	{
		if (categoryTree == null) {
			categoryTree = new CategoryTree();
		}
	}

	public static CategoryTree getInstance() {
		
		return categoryTree;
	}

	public ArrayList<Integer> getChildren(int parent) {
		return category[parent].children;
	}

	public Category getCategory(int index) {
		return category[index];
	}
	
	public String getProductName(int index)
	{
		return category[index].name;
	}
	
	public String getProductId(int index)
	{
		return category[index].pid;
	}
	
	public int getCategoryTreeSize()
	{
		return category.length;
	}

	private class CreateCategoryTreeAsyncTask extends
            AsyncTask<InputStream, Integer, Category[]>
    {

		@Override
		protected Category[] doInBackground(InputStream... params) {
			// TODO Auto-generated method stub
			return parse(params[0]);

		}

		Category[] parse(InputStream ins) {

			XmlPullParser parser = Xml.newPullParser();

			try {
				parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES,
						false);
				parser.setInput(ins, null);
				parser.nextTag();
				return readResponse(parser);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;

		}

		Category[] readResponse(XmlPullParser parser)
				throws XmlPullParserException, IOException
        {
			Category[] categoryList = new Category[250];
			ArrayList<Integer> children = new ArrayList<Integer>();
			int index = categoryIndex;
			categoryIndex++;
			parser.require(XmlPullParser.START_TAG, null, "root");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();

				if (name.equals("mc")) {
					int childIndex = readMasterCategory(parser, index,
							categoryList);
					children.add(childIndex);
				}
			}
			categoryList[index] = new Category("root", "root", -1, children,"");
			return categoryList;
		}

		int readMasterCategory(XmlPullParser parser, int parent,
				Category[] categoryList) throws XmlPullParserException,
                IOException
        {
			String categoryName = "";
			String imgId = "";
			ArrayList<Integer> children = new ArrayList<Integer>();
			int index = categoryIndex;
			categoryIndex++;
			parser.require(XmlPullParser.START_TAG, null, "mc");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();

				if (name.equals("name")) {
					categoryName = readCategoryName(parser);
				} else if (name.equals("image")) {
					imgId = removePng(readImageId(parser).toLowerCase());
				} else if (name.equals("pc")) {
					int childIndex = readProductCategory(parser, index,
							categoryList);
					children.add(childIndex);
				}
			}

			categoryList[index] = new Category(categoryName, imgId, parent,
					children, "");
			return index;
		}

		int readProductCategory(XmlPullParser parser, int parent,
				Category[] categoryList) throws XmlPullParserException,
                IOException
        {
			String categoryName = "";
			String imgId = "";
			ArrayList<Integer> children = new ArrayList<Integer>();
			int index = categoryIndex;
			categoryIndex++;
			parser.require(XmlPullParser.START_TAG, null, "pc");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();

				if (name.equals("name")) {
					categoryName = readCategoryName(parser);
				} else if (name.equals("image")) {
					imgId = removePng(readImageId(parser).toLowerCase());
				} else if (name.equals("pt")) {
					int childIndex = readProductType(parser, index,
							categoryList);
					children.add(childIndex);
				}
			}
			categoryList[index] = new Category(categoryName, imgId, parent,
					children, "");
			return index;
		}

		int readProductType(XmlPullParser parser, int parent,
				Category[] categoryList) throws XmlPullParserException,
                IOException
        {
			String categoryName = "";
			String imgId = "";
			int index = categoryIndex;
			String pid = "";
			categoryIndex++;
			parser.require(XmlPullParser.START_TAG, null, "pt");
			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}
				String name = parser.getName();

				if (name.equals("name")) {
					categoryName = readCategoryName(parser);
				} else if (name.equals("image")) {
					imgId = removePng(readImageId(parser).toLowerCase());
				} else if (name.equals("pid")) {
					pid = readPid(parser);
				}
			}
			categoryList[index] = new Category(categoryName, imgId, parent,
					null, pid);
			return index;
		}

		String readCategoryName(XmlPullParser parser)
				throws XmlPullParserException, IOException
        {
			parser.require(XmlPullParser.START_TAG, null, "name");
			String categoryName = readText(parser);
			parser.require(XmlPullParser.END_TAG, null, "name");
			return categoryName;
		}

		String readImageId(XmlPullParser parser) throws XmlPullParserException,
                IOException
        {
			parser.require(XmlPullParser.START_TAG, null, "image");
			String imgId = readText(parser);
			parser.require(XmlPullParser.END_TAG, null, "image");
			return imgId;
		}

		String readPid(XmlPullParser parser) throws XmlPullParserException,
                IOException
        {
			parser.require(XmlPullParser.START_TAG, null, "pid");
			String pId = readText(parser);
			parser.require(XmlPullParser.END_TAG, null, "pid");
			return pId;
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

		private String removePng(String img) {
			return img.replace(".png", "");
		}

	}

}
