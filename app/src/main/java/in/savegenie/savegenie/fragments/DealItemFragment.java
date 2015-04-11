package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.DealActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.internetcommunication.GetDealsAsyncTask;
import in.savegenie.savegenie.responses.DealResponse;

/**
 * Created by manish on 4/4/15.
 */
public class DealItemFragment extends Fragment
{

    private DealActivityAdapter dealActivityAdapter;
    private GetDealsAsyncTask getDealsAsyncTask;
    private DealResponse dealResponse;
    private ListView itemListView;
    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private Handler mHandler;
    public static String title = "Deals";
    public String storeId;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);

        spinnerRL.setAlpha((float) 0.50);
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        storeId = getArguments().getString(IntentString.ITEM_ACTIVITY_DEAL_STORE_ID);
        new Thread(getAllDeals).start();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    Runnable getAllDeals = new Runnable()
    {
        @Override
        public void run()
        {
            getDealsAsyncTask = new GetDealsAsyncTask();
            getDealsAsyncTask.execute(storeId);
            Log.d("store id", storeId);

            try
            {
                dealResponse = getDealsAsyncTask.get();
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

            if (dealResponse != null)
            {
                dealActivityAdapter = new DealActivityAdapter(context,getItemIds(dealResponse.bundleDealList.size()
                        +dealResponse.productDealList.size()),dealResponse.bundleDealList,dealResponse.productDealList);
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (dealResponse != null || (dealResponse.bundleDealList.size()+dealResponse.productDealList.size()!=0))
                    {
                        itemListView.setVisibility(View.VISIBLE);
                        itemListView.setAdapter(dealActivityAdapter);
                    }
                    else
                    {
                        noProduct.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    };

    protected Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().getActionBar().setTitle(title);
    }
}
