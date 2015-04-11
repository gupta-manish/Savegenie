package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.GroceryListItem;


public class GroceryListResponse {
	public ArrayList<GroceryListItem> itemList;
	public String result;
	public String count;
	public GroceryListResponse(ArrayList<GroceryListItem> itemList,String result, String count) {
		this.itemList = itemList;
		this.result = result;
		this.count = count;
	}
}
