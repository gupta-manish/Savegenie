package in.savegenie.savegenie.internetcommunication;

import android.content.Context;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import in.savegenie.savegenie.responses.ModifyProfileResponse;

/**
 * Created by manish on 11/4/15.
 */
public class ModifyProfileAsyncTask extends BasicAsyncTask<ModifyProfileResponse> {
    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;
    private Context context;
    @Override
    String getUrlLink()
    {
        return InternetURL.ANDROID_MODIFY_PROFILE;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[User][id]",params[0]);
        addDataToPostVariable("data[User][title]",params[1]);
        addDataToPostVariable("data[User][first_name]",params[2]);
        addDataToPostVariable("data[User][last_name]",params[3]);
        addDataToPostVariable("data[User][city]",params[4]);
        addDataToPostVariable("data[User][area]",params[5]);
        addDataToPostVariable("data[User][post_code]",params[6]);
        addDataToPostVariable("data[UserDetail][phone]",params[7]);
        addDataToPostVariable("data[User][email]",params[8]);
        addDataToPostVariable("data[UserDetail][id]",params[9]);
    }
    @Override
    ModifyProfileResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String result = null, msg = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result")) {
                result = readResult(parser,name);
            }
            if(name.equals("msg"))
            {
                msg = readResult(parser,name);
            }

        }

        return new ModifyProfileResponse(result,msg);
    }

}
