package in.savegenie.savegenie.responses;


import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Brand;
import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.backgroundclasses.SubType;


public class ProductListResponse {
	public ArrayList<Product> productList;
	public ArrayList<Brand> brandList;
    public ArrayList<SubType> subTypeList;
	
	public ProductListResponse(ArrayList<Product> productList,ArrayList<Brand> brandList,ArrayList<SubType> subTypeList) {
		this.productList = productList;
		this.brandList = brandList;
        this.subTypeList = subTypeList;
	}

    public ArrayList<String> getBrandNameList()
    {
        ArrayList<String> brandNameList = new ArrayList<String>();
           for(int i=0;i<brandList.size();i++)
           {
               brandNameList.add(brandList.get(i).name);
           }
        return  brandNameList;
    }
}
