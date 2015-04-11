package in.savegenie.savegenie.responses;

import java.util.ArrayList;

/**
 * Created by manish on 10/4/15.
 */
public class AutoCompleteResponse
{
    public ArrayList<String> autocomplete;

    public AutoCompleteResponse(ArrayList<String> autocomplete)
    {
        this.autocomplete = autocomplete;
    }
}
