package in.savegenie.savegenie.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetStoreListAsyncTask;
import in.savegenie.savegenie.responses.StoreListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class DealFragment extends Fragment
{
    private String title;
    String[] itemNames;
    private RelativeLayout spinnerRL;
    private RelativeLayout noProduct;
    ArrayAdapter adapter;
    GetStoreListAsyncTask gat;
    StoreListResponse storeListResponse;
    private Context context;
    ArrayList<String> storeList;
    private Handler mHandler;
    DealFromStoreClickListener onDealFromStoreClickListener;
    ListView listView;
    String store_id,store_name;
    private int change;
    boolean isStoreSelected;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {


        View rootView = inflater.inflate(R.layout.fragment_deals, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        listView = (ListView)rootView.findViewById(R.id.deals_list);
        store_id = prefs.getString(SharedPrefString.GROCERY_STORE_ID,null);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);

        isStoreSelected = prefs
                .getBoolean(SharedPrefString.IS_STORE_SELECTED, false);
        mHandler = new Handler();
        context = getActivity();
        storeList = new ArrayList<String>();
        onDealFromStoreClickListener = (DealFromStoreClickListener)context;
        if(!isStoreSelected)
        {

            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.INVISIBLE);
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {

                    gat = new GetStoreListAsyncTask();

                    gat.execute();

                    try
                    {
                        storeListResponse = gat.get();
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
                        for(int i=0;i<storeListResponse.storeList.size();i++)
                        {
                            storeList.add("Deals from " + storeListResponse.storeList.get(i).name);
                        }

                        adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,storeList);
                    }

                    mHandler.post(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            spinnerRL.setVisibility(View.INVISIBLE);


                            if (storeListResponse.storeList.size() > 0)
                            {
                                listView.setVisibility(View.VISIBLE);
                                listView.setAdapter(adapter);
                            }
                            else
                            {
                                noProduct.setVisibility(View.VISIBLE);
                            }
                        }
                    });

                }

            }).start();
        }
        else
        {

            spinnerRL.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            store_id = prefs.getString(SharedPrefString.GROCERY_STORE_ID,null);
            store_name = prefs.getString(SharedPrefString.GROCERY_STORE_NAME,null);
            storeList.add("Deals from " + store_name);
            adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,storeList);
            listView.setAdapter(adapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(!isStoreSelected)
                {
                    onDealFromStoreClickListener.onDealFromStoreClick(storeListResponse.storeList.get(position).id);
                }
                else
                {
                    onDealFromStoreClickListener.onDealFromStoreClick(store_id);
                }
            }
        });

        return rootView;
    }

    public interface DealFromStoreClickListener
    {
        public void onDealFromStoreClick(String store_id);
    }

    private Integer[] getItemIds(int n) {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++) {
            arr[i - 1] = i;
        }
        return arr;

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
