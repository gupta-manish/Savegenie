package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.CategoryTree;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetStoreListAsyncTask;
import in.savegenie.savegenie.internetcommunication.LoginAsyncTask;
import in.savegenie.savegenie.internetcommunication.SelectAllStoresAsyncTask;
import in.savegenie.savegenie.responses.LoginResponse;
import in.savegenie.savegenie.responses.SendSelectedStoreResponse;
import in.savegenie.savegenie.responses.StoreListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class Start extends Activity
{

    GetStoreListAsyncTask gat;
    LoginResponse loginResponse;
    LoginAsyncTask lat;
    RelativeLayout spinnerRL;
    SharedPreferences prefs;
    Boolean isSignedIn;
    Handler mHandler;
    String nextActivity;
    Context context;
    SelectAllStoresAsyncTask sssat;
    GetStoreListAsyncTask gslat;
    StoreListResponse storeListResponse;
    SendSelectedStoreResponse response;
    String storeListString;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i("pkg", "pkgName = " + getPackageName());
        prefs = getSharedPreferences(SharedPrefString.SHARED_PREFERENCE_KEY,
                Context.MODE_PRIVATE);
        isSignedIn = prefs.getBoolean(SharedPrefString.IS_SIGNED_IN, false);
        spinnerRL = (RelativeLayout) findViewById(R.id.spinnerRelativeLayout);
        storeListString = "";
        mHandler = new Handler();
        context = this;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                InputStream ins;
                try
                {
                    ins = getResources().getAssets().open("test.xml");
                    CategoryTree
                            .createCategoryTreeFromXmlAndStoreInDatabase(ins);

                }
                catch (IOException e1)
                {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                catch (NullPointerException e)
                {
                    e.printStackTrace();
                }
				/*}*/
                if (!isSignedIn)
                {
                    nextActivity = IntentString.LOGIN_ACTIVITY;
                }
                else
                {

                    String email = prefs.getString(SharedPrefString.EMAIL_ID, null);
                    String password = prefs.getString(SharedPrefString.PASSWORD, null);
                    lat = new LoginAsyncTask();
                    try
                    {

                        lat.execute(email, password);

                    }
                    catch (IllegalStateException e)
                    {
                        e.printStackTrace();
                    }
                    try
                    {
                        loginResponse = lat.get();


                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }

                    if (loginResponse == null)
                    {
                        nextActivity = IntentString.NO_INTERNET_CONNECTION_ACTIVITY;
                    }
                    else if (loginResponse.result.contains("0"))
                    {
                        nextActivity = IntentString.LOGIN_ACTIVITY;
                    }
                    else
                    {

                        nextActivity = "";
                        SharedPreferences.Editor editor = prefs.edit();
                        Long tsLong = System.currentTimeMillis() / 1000;
                        Calendar calendar = Calendar.getInstance();
                        editor.putLong(SharedPrefString.LOGGED_IN_TIME_STAMP, tsLong);
                        editor.putString(SharedPrefString.FIRST_NAME, loginResponse.fname);
                        editor.putString(SharedPrefString.LAST_NAME, loginResponse.lname);
                        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                        String formattedTime = df.format(calendar.getTime());
                        editor.putString(SharedPrefString.LOGGED_IN_TIME, formattedTime);
                        editor.apply();
                    }

                }
                mHandler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(nextActivity.equals(""))
                        {
                            new Thread(getStoreList).start();
                        }
                        else
                        {
                            Intent ia = new Intent(nextActivity);
                            startActivity(ia);
                            finish();
                        }
                    }
                });

            }

        }).start();
    }

    Runnable getStoreList = new Runnable()
    {
        @Override
        public void run()
        {
            gslat = new GetStoreListAsyncTask();

            gslat.execute();

            try
            {
                storeListResponse = gslat.get();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (storeListResponse.storeList.size() > 0)
            {
                for (int i = 0; i < storeListResponse.storeList.size(); i++)
                {
                    storeListString = storeListString + storeListResponse.storeList.get(i).id;
                    if (i != storeListResponse.storeList.size() - 1)
                    {
                        storeListString = storeListString + "-";
                    }
                }
            }

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    new Thread(selectAllStore).start();
                }
            });
        }
    } ;

    Runnable selectAllStore = new Runnable()
    {
        @Override
        public void run()
        {
            sssat = new SelectAllStoresAsyncTask();
            sssat.execute();

            SharedPreferences preferences = getSharedPreferences(SharedPrefString.SHARED_PREFERENCE_KEY, MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(SharedPrefString.IS_STORE_SELECTED, false);
            editor.putString(SharedPrefString.GROCERY_STORE_ID, SharedPrefString.ALL_STORE_ID);
            editor.putString(SharedPrefString.GROCERY_STORE_NAME, SharedPrefString.ALL_STORE_NAME);
            editor.putString(SharedPrefString.STORE_LIST_STRING, storeListString);
            editor.apply();

            try
            {
                response = sssat.get();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            editor.putString(SharedPrefString.GROCERY_LIST_ID, response.listId);
            editor.putString(SharedPrefString.GROCERY_LIST_NAME, response.listName);
            editor.putBoolean(SharedPrefString.AUTOMATIC_CREATED_LIST,true);
            editor.apply();
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    // if (response.contains("1"))
                    {
                        Intent ia = new Intent(IntentString.ITEM_ACTIVITY);
                        startActivity(ia);
                        finish();
                    }
                }
            });
        }
    };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}
