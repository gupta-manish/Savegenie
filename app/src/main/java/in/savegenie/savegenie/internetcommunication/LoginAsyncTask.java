package in.savegenie.savegenie.internetcommunication;


import android.util.Log;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import in.savegenie.savegenie.responses.LoginResponse;

public class LoginAsyncTask extends BasicAsyncTask<LoginResponse>
{

    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        Log.d("Link",InternetURL.ANDROID_LOGIN);
        return InternetURL.ANDROID_LOGIN;
    }

    @Override
    void addPostData(String... params)
    {
        addDataToPostVariable("data[User][email]",params[0]);
        addDataToPostVariable("data[User][password]",params[1]);
    }

    @Override
    LoginResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException
    {
        String result = null,lname = null,fname = null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("result")) {
                result = readResult(parser,name);
            }
            if(name.equals("fname"))
            {
                fname = readResult(parser,name);
            }
            if(name.equals("lname"))
            {
                lname = readResult(parser,name);
            }
        }

        return new LoginResponse(result,fname,lname);
    }

}
