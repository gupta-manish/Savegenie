package in.savegenie.savegenie.internetcommunication;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import in.savegenie.savegenie.responses.AutoCompleteResponse;

/**
 * Created by manish on 10/4/15.
 */
public class GetAutoCompleteSocietyNamesAsyncTask extends BasicAsyncTask<AutoCompleteResponse>
{
    @Override
    String getUrlLink()
    {
        return InternetURL.AUTO_COMPLETE_SOCIETY_NAME;
    }

    @Override
    AutoCompleteResponse readResponse(XmlPullParser parser) throws XmlPullParserException, IOException
    {
        ArrayList<String> autoComplete = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("name")) {
                autoComplete.add(readResult(parser,name));
            }
        }

        return new AutoCompleteResponse(autoComplete);
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[key_value]",params[0]);
    }
}
