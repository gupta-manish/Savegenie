package in.savegenie.savegenie.responses;

import in.savegenie.savegenie.backgroundclasses.GroceryList;

import java.util.ArrayList;


public class GroceryListsListResponse {
	public ArrayList<GroceryList> groceryListsList;
	public int count;

	public GroceryListsListResponse(ArrayList<GroceryList> groceryListsList,int count) {
		this.groceryListsList = groceryListsList;
		this.count = count;
	}
}
