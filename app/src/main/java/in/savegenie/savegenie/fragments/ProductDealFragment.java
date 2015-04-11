package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.DealActivityAdapter;
import in.savegenie.savegenie.internetcommunication.GetProductItemDealAsyncTask;
import in.savegenie.savegenie.responses.DealResponse;

/**
 * Created by manish on 4/4/15.
 */
public class ProductDealFragment extends Fragment
{

    GetProductItemDealAsyncTask getDealsAsyncTask;
    String is_deal;
    DealResponse dealResponse;
    DealActivityAdapter dealActivityAdapter;
    ListView itemListView;
    private Handler mHandler;
    RelativeLayout noProduct,spinnerRL;
    private int runThread;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_product_deal, container, false);

        itemListView = (ListView)rootView.findViewById(R.id.deals_list);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        if(runThread == 1)
        {
            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.INVISIBLE);
            new Thread(getAllDeals).start();
        }
        else
        {
            setUpUI();
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        is_deal = this.getArguments().getString("IS_DEAL");
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        runThread = 1;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        runThread = 0;
    }

    Runnable getAllDeals = new Runnable()
    {
        @Override
        public void run()
        {
            getDealsAsyncTask = new GetProductItemDealAsyncTask();
            getDealsAsyncTask.execute(is_deal);

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
                dealActivityAdapter = new DealActivityAdapter(getActivity(),getItemIds(dealResponse.bundleDealList.size()
                        +dealResponse.productDealList.size()),dealResponse.bundleDealList,dealResponse.productDealList);
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                       setUpUI();

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

    private void setUpUI()
    {
        spinnerRL.setVisibility(View.INVISIBLE);
        if (dealResponse != null && dealResponse.bundleDealList.size()
                +dealResponse.productDealList.size()>0)
        {
            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(dealActivityAdapter);
        }
        else
        {
            noProduct.setVisibility(View.VISIBLE);
        }
    }
}
