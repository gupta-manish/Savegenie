package in.savegenie.savegenie.responses;

/**
 * Created by Devansh on 10-Jan-15.
 */
public class AddressFormResponse
{
    public String areaName,areaid,cityName,cityid,mobileno;

    public AddressFormResponse(String areaName,String areaid,String cityName,String cityid,String mobileno)
    {
        this.areaid = areaid;
        this.areaName = areaName;
        this.cityid = cityid;
        this.cityName = cityName;
        this.mobileno = mobileno;
    }
}
