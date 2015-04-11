package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.IntentString;

/**
 * Created by manish on 5/4/15.
 */
public class NoInternetConnectionActivity extends Activity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_no_internet_connection);

        Button refresh = (Button) findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                Intent start = new Intent(
                        IntentString.START_ACTIVITY);
                startActivity(start);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long

        return super.onOptionsItemSelected(item);
    }


}
