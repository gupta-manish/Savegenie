package in.savegenie.savegenie.backgroundclasses;

public class Product 
{
	public String[] params;
	
	public static final int P_ID = 0,P_SIZE = 1,P_UNIT = 2,P_CODE = 3,P_IMAGE = 4,P_MRP = 5,PRODUCT_ID = 6,PLUS_DEAL = 7,
			DISCOUNT_DEAL = 8,STORE_SKU = 9,STORE_PLUS_DEAL = 10,IS_DEAL = 11,IS_DIST = 12,DISCOUNT = 13,BEST_STORE_DISCOUNT = 14,
			AVG_PACK_PRICE = 15,ACTUAL_PRICE = 16,LOW_PRICE = 17,HIGH_PRICE = 18,DENO_SZ = 19,DENO_UNIT = 20,AVAILABILITY = 21,
			COUNT_AVL_STORE = 22,SGENIE_DISCOUNT = 23,BEST_VALUED = 24,PRODUCT_BRAND_ID = 25;
	
	static String[] paramNames = {"pid","psize","punit","pcode","pimage","pmrp","productid","plusdeal","discountdeal",
							"storesku","storeplusdeal","isdeal","isdist","discount","beststorediscount","avgpackprice",
							"actualprice","lowprice","highprice","denosz","denounit","availability","countavlstore",
							"sgeniediscount","bestvalued","productbrandid"};
	
	public static final int NUMBER_OF_PARAMS = 26;
	private int PRE;
	
	public Product(String params[])
	{
		this.params = params;
		PRE = 0;
	}
	
	public int getParamId(String paramName)
	{
		for(int i=PRE;i<NUMBER_OF_PARAMS+PRE;i++)
		{
			if(paramNames[i%NUMBER_OF_PARAMS].equals(paramName))
			{
				PRE = (PRE+1)%NUMBER_OF_PARAMS;
				return i%NUMBER_OF_PARAMS;
			}
		}
		
		return 0;
	}
	
	public static String getParamName(int paramId)
	{
		return paramNames[paramId];
	}
}
