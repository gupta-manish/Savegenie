package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.CompareStoreItem;
import in.savegenie.savegenie.backgroundclasses.SelectedProduct;

public class CompareStoreListResponse {
	public ArrayList<CompareStoreItem> storeList;
	public ArrayList<SelectedProduct> productList;
	public CompareStoreListResponse(ArrayList<CompareStoreItem> itemList,ArrayList<SelectedProduct> productList) {
		this.storeList = itemList;
		this.productList = productList;
	}
}
