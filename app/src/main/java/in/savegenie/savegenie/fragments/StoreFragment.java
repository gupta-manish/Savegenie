package in.savegenie.savegenie.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.StoreFragmentAdapter;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.backgroundclasses.Store;
import in.savegenie.savegenie.internetcommunication.GetStoreListAsyncTask;
import in.savegenie.savegenie.responses.StoreListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class StoreFragment extends Fragment
{
    private String title;
    String[] itemNames;
    private RelativeLayout spinnerRL;
    private RelativeLayout noProduct;
    StoreFragmentAdapter adapter;
    GetStoreListAsyncTask gat;
    StoreListResponse storeListResponse;
    private ListView itemListView;
    private Context context;
    ArrayList<Store> storeList;
    ArrayList<String> listNameList;
    ListView listView;
    // private SetItemListAsyncTask silat;
    private Handler mHandler;
    private Button doneButton;
    private int change;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_store, container, false);

        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);



        context = getActivity();
        listNameList = new ArrayList<String>();
        mHandler = new Handler();
        listView = (ListView)rootView.findViewById(R.id.storeList);

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
                    storeListResponse.storeList.add(new Store("All Stores", SharedPrefString.ALL_STORE_ID,null));
                    adapter = new StoreFragmentAdapter(context,getItemIds(storeListResponse.storeList.size()),storeListResponse.storeList);

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



        return rootView;
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
