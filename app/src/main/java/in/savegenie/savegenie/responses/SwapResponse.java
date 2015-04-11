package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.SwapProduct;

public class SwapResponse 
{
	ArrayList<SwapProduct> swapList;
	
	public SwapResponse(ArrayList<SwapProduct> swapList)
	{
		this.swapList = swapList;
	}
}
