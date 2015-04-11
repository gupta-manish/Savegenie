package in.savegenie.savegenie.internetcommunication;

import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import in.savegenie.savegenie.backgroundclasses.Area;
import in.savegenie.savegenie.responses.AllAreasResponse;

/**
 * Created by manish on 11/4/15.
 */
public class GetAllAreasAsyncTask extends BasicAsyncTask<AllAreasResponse> {
    String responseStr;
    InputStream inputStream;
    String urlLink;
    UrlEncodedFormEntity urlEncodedFormEntity;

    @Override
    String getUrlLink()
    {
        return InternetURL.GET_ALL_AREAS;
    }

    @Override
    void addPostData(String... params)
    {

    }
    @Override
    AllAreasResponse readResponse(XmlPullParser parser)
            throws XmlPullParserException, IOException {
        ArrayList<Area> areaList = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "response");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if(name.equals("area"))
            {
                areaList.add(readArea(parser));
            }

        }

        return new AllAreasResponse(areaList);
    }

    Area readArea(XmlPullParser parser) throws XmlPullParserException,
            IOException
    {
        String  id = null, name = null;
        parser.require(XmlPullParser.START_TAG, null, "area");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String areaName = parser.getName();

            if (areaName.equals("id")) {
                id = readResult(parser, areaName);
            }
            else if (areaName.equals("name")) {
                name = readResult(parser, areaName);
            }
        }
        return new Area(id,name);
    }

}
