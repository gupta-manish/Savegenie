package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.ItemActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.Brand;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.internetcommunication.GetFilteredSearchResultAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetSearchResultAsyncTask;
import in.savegenie.savegenie.responses.ProductListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class SearchItemFragment extends Fragment
{

    private ItemActivityAdapter itemAdapter;
    private GetSearchResultAsyncTask plat;
    private GetFilteredSearchResultAsyncTask fplat;
    private ProductListResponse listResponse;
    private ListView filterListView,itemListView;
    private Button all;
    private ArrayList<Brand> brandList;
    private ArrayList<Product> productList;
    private String filterId;
    private boolean filtered;
    private int minVal,refresh,lastPosition;
    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private DrawerLayout mDrawerLayout;
    private Handler mHandler;
    public static String title = "Search";
    private String searchString;
    private int runThread;
    private ArrayList<String> brandNameList;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_list_with_filter, container, false);
        filterListView = (ListView)rootView.findViewById(R.id.categoryFilterList);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);
        all = (Button)rootView.findViewById(R.id.clearButton);
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        mDrawerLayout = (DrawerLayout)rootView.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);
        searchString = getArguments().getString(IntentString.ITEM_ACTIVITY_SEARCH_STRING);



        filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mDrawerLayout.closeDrawer(Gravity.END);
                filterId = brandList.get(position).id;
                filtered = true;
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getFilteredSearchProductList).start();

            }
        });

        all.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.closeDrawer(Gravity.END);
                filtered = false;
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getSearchProductList).start();

            }
        });

        if(runThread == 1)
        {
            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.INVISIBLE);
            new Thread(getSearchProductList).start();
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
        brandList = new ArrayList<Brand>();
        productList = new ArrayList<Product>();
        refresh  = 0;
        lastPosition = 0;
        minVal = 0;
        filtered = false;
        mHandler = new Handler();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
        runThread = 1;
    }

    @Override
    public void onPause()
    {
        super.onPause();
        runThread = 0;
    }

    Runnable getSearchProductList = new Runnable()
    {
        @Override
        public void run()
        {
            plat = new GetSearchResultAsyncTask();

            plat.execute(searchString);

            try
            {
                listResponse = plat.get();
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



            if (listResponse != null
                    && listResponse.productList.size() != 0)
            {
                itemAdapter = new ItemActivityAdapter(context,
                        getItemIds(listResponse.productList.size()),
                        listResponse.productList);
                brandList = listResponse.brandList;
                brandNameList = listResponse.getBrandNameList();
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

    Runnable getFilteredSearchProductList = new Runnable()
    {
        @Override
        public void run()
        {
            fplat = new GetFilteredSearchResultAsyncTask();

            fplat.execute(searchString,filterId);

            try
            {
                listResponse = fplat.get();
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



            if (listResponse != null
                    && listResponse.productList.size() != 0)
            {
                itemAdapter = new ItemActivityAdapter(context,
                        getItemIds(listResponse.productList.size()),
                        listResponse.productList);

            }

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    setUpUIWithoutFilter();

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

    private void setUpUIWithoutFilter()
    {
        spinnerRL.setVisibility(View.INVISIBLE);
        if (listResponse != null
                && listResponse.productList.size() != 0)
        {
            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(itemAdapter);
        }
        else
        {
            itemListView.setVisibility(View.INVISIBLE);
        }
    }

    private void setUpUI()
    {
        spinnerRL.setVisibility(View.INVISIBLE);
        if (listResponse != null
                && listResponse.productList.size() != 0)
        {

            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(itemAdapter);
            filterListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,brandNameList));

        }
        else
        {
            noProduct.setVisibility(View.VISIBLE);
        }
    }
}
