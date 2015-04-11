package in.savegenie.savegenie.responses;

/**
 * Created by manish on 27/3/15.
 */
public class MergeListResponse
{
    public String result,listid,listname,count;

    public MergeListResponse(String result,String listid,String listname,String count)
    {
        this.result = result;
        this.listid = listid;
        this.listname = listname;
        this.count = count;
    }
}
