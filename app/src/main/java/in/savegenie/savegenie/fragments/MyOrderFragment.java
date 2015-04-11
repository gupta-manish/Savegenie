package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.activities.ItemActivity;
import in.savegenie.savegenie.adapters.CompareStoreActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetMyOrderAsyncTask;
import in.savegenie.savegenie.responses.CompareStoreListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class MyOrderFragment extends Fragment
{

    private CompareStoreActivityAdapter adapter;
    private GetMyOrderAsyncTask gcsliat;
    private CompareStoreListResponse listResponse;
    private ListView itemListView;
    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private Handler mHandler;
    public static String title = "My Orders";
    private SharedPreferences prefs;
    private String listId;
    private int runThread;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);

        spinnerRL.setAlpha((float) 0.50);

        itemListView = (ListView)rootView.findViewById(R.id.quickList);

        if(runThread == 1)
        {
            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.INVISIBLE);
            new Thread(getCompareStoreList).start();
        }
        else
        {
            spinnerRL.setVisibility(View.INVISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(adapter);
        }

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        mHandler = new Handler();
        listId = prefs.getString(SharedPrefString.GROCERY_LIST_ID, null);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        prefs = activity.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        runThread = 1;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        ((ItemActivity)getActivity()).changeButtonLayout();
        runThread = 0;
    }

    Runnable getCompareStoreList = new Runnable()
    {
        @Override
        public void run()
        {


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
        ((ItemActivity)getActivity()).changeButtonLayout();
    }

}
