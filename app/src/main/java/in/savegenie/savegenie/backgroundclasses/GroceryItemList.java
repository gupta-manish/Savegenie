package in.savegenie.savegenie.backgroundclasses;

import java.util.ArrayList;

public class GroceryItemList extends GroceryList {

	public ArrayList<GroceryListItem> itemList;
	public GroceryItemList(String name, String id,ArrayList<GroceryListItem> itemList) {
		super(name, id);
		this.itemList = itemList;
	}

}
