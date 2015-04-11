package in.savegenie.savegenie.backgroundclasses;

import java.util.ArrayList;

public class Category 
{	
	public String name;
	public int parent;
	public ArrayList<Integer> children;
	public String imgId;
	public String pid;

	Category(String name,String imgId,int parent,ArrayList<Integer> children,String pid)
	{
		this.name = name;
		this.imgId = imgId;
		this.parent = parent;
		this.children = children;
		this.pid = pid;
	}
}
