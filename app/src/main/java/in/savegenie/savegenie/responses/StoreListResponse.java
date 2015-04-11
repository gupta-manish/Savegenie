package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Store;


public class StoreListResponse {
	public ArrayList<Store> storeList;

	public StoreListResponse(ArrayList<Store> storeList) {
		this.storeList = storeList;
	}
}
