package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import in.savegenie.savegenie.responses.UserProfileResponse;

public class GetUserProfileAsyncTask extends BasicAsyncTask<UserProfileResponse> {
    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_USER_PROFILE;
    }

    @Override
    void addPostData(String... params)
    {

    }

    @Override
    UserProfileResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        String id=null,lname = null,fname = null,title =null,city=null,area=null,pincode=null,phone=null,email=null,userdetailid=null;

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("id"))
            {
                id = readResult(parser,name);
            }
            if(name.equals("title"))
            {
                title = readResult(parser,name);
            }

            if(name.equals("first_name"))
            {
                fname = readResult(parser,name);
            }
            if(name.equals("last_name"))
            {
                lname = readResult(parser,name);
            }
            if(name.equals("city"))
            {
                city = readResult(parser,name);
            }
            if(name.equals("area"))
            {
                area = readResult(parser,name);
            }
            if(name.equals("post_code"))
            {
                pincode = readResult(parser,name);
            }
            if(name.equals("userdetailid"))
            {
                userdetailid = readResult(parser,name);
            }
            if(name.equals("cellphone"))
            {
                phone = readResult(parser,name);
            }
            if(name.equals("email"))
            {
                email = readResult(parser,name);
            }

        }

        return new UserProfileResponse(id,title,fname,lname,city,area,pincode,phone,email,userdetailid);
    }

}