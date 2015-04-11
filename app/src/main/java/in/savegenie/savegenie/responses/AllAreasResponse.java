package in.savegenie.savegenie.responses;

import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Area;

/**
 * Created by manish on 11/4/15.
 */
public class AllAreasResponse
{
    public ArrayList<Area> areaList;

    public AllAreasResponse(ArrayList<Area> areaList)
    {
        this.areaList = areaList;
    }
    public ArrayList<String> getAreaNameList(){
        ArrayList<String> ret =  new ArrayList<String>();
        for(int i=0;i<areaList.size();i++){
            ret.add(areaList.get(i).name);
        }
        return ret;
    }
}