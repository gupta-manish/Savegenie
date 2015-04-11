package in.savegenie.savegenie.responses;



import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.AlternateItem;

public class MoreOptionsResponse 
{
	public ArrayList<AlternateItem> moreOptionList;
	public MoreOptionsResponse(ArrayList<AlternateItem> moreOptionList)
	{
		this.moreOptionList = moreOptionList;
	}

}
