package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.SelectedProduct;
import in.savegenie.savegenie.backgroundclasses.SwapProduct;


public class ReviewResponse {
	public ArrayList<SelectedProduct> selectedProductList;
	public ArrayList<SwapProduct> swapProductList;
	public ReviewResponse(ArrayList<SelectedProduct> selectedProductList,ArrayList<SwapProduct> swapProductList) {
		this.selectedProductList = selectedProductList;
		this.swapProductList = swapProductList;
	}
}
