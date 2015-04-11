package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.ListDetailAdapter;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.internetcommunication.GetSelectedListItemsAsyncTask;
import in.savegenie.savegenie.responses.GroceryListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class ListDetailFragment extends Fragment
{

    private ListDetailAdapter listDetailAdapter;
    private GetSelectedListItemsAsyncTask getSelectedListItemsAsyncTask;
    private GroceryListResponse groceryListResponse;
    private ListView itemListView;
    private RelativeLayout spinnerRL, noProduct;
    private Handler mHandler;
    public String title = "List";
    public String listId;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);

        spinnerRL.setAlpha((float) 0.50);
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        listId = getArguments().getString(IntentString.ITEM_ACTIVITY_DETAIL_LIST_ID);
        spinnerRL.setVisibility(View.VISIBLE);
        noProduct.setVisibility(View.INVISIBLE);
        itemListView.setVisibility(View.INVISIBLE);
        new Thread(getListItems).start();

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

    Runnable getListItems = new Runnable()
    {
        @Override
        public void run()
        {
            getSelectedListItemsAsyncTask = new GetSelectedListItemsAsyncTask();
            getSelectedListItemsAsyncTask.execute(listId);

            try
            {
                groceryListResponse = getSelectedListItemsAsyncTask.get();
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

            if (groceryListResponse != null)
            {
                listDetailAdapter = new ListDetailAdapter(context,getItemIds(groceryListResponse.itemList.size()),
                        groceryListResponse.itemList,listId);
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (groceryListResponse != null || (groceryListResponse.itemList.size()!=0))
                    {
                        itemListView.setVisibility(View.VISIBLE);
                        itemListView.setAdapter(listDetailAdapter);
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
