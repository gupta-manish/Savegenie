package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.internetcommunication.GetAllProductListAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetFilteredProductListAsyncTask;
import in.savegenie.savegenie.responses.ProductListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class BestSellingFragment extends Fragment
{


    private ItemActivityAdapter itemAdapter;
    private GetAllProductListAsyncTask plat;
    private GetFilteredProductListAsyncTask fplat;
    private ProductListResponse listResponse;
    private ListView filterListView,itemListView;
    private Button all;
    private ArrayList<Brand> brandList;
    private ArrayList<Product> productList;
    private String filterId;
    private boolean filtered;
    private int minVal,refresh,lastPosition;
    private Context context;
    private RelativeLayout spinnerRL, noProduct,rightDrawerHandle;
    private DrawerLayout mDrawerLayout;
    private Handler mHandler;
    public String title = "Best Selling Products";
    private int runThread;
    private ArrayList<String> brandNameList;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_list_with_filter, container, false);
        filterListView = (ListView)rootView.findViewById(R.id.categoryFilterList);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);
        all = (Button)rootView.findViewById(R.id.clearButton);
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        mDrawerLayout = (DrawerLayout)rootView.findViewById(R.id.drawer_layout);

        itemListView.addFooterView(inflater.inflate(R.layout.list_footer_activity_item, null));

        itemListView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                if(firstVisibleItem+visibleItemCount == totalItemCount && refresh==1)
                {
                    lastPosition = firstVisibleItem;
                    if(!filtered)
                    {
                        new Thread(getAllProductList).start();
                    }
                    else
                    {
                        new Thread(getFilteredProductList).start();
                    }
                    refresh = 0;
                }
            }
        });

        filterListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mDrawerLayout.closeDrawer(Gravity.END);
                filterId = brandList.get(position).id;
                filtered = true;
                minVal = 0;
                lastPosition = 0;
                refresh = 0;
                productList.clear();
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getFilteredProductList).start();

            }
        });

        all.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mDrawerLayout.closeDrawer(Gravity.END);
                filtered = false;
                minVal = 0;
                lastPosition = 0;
                refresh = 0;
                productList.clear();
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getAllProductList).start();

            }
        });

        if(runThread == 1)
        {
            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.INVISIBLE);
            new Thread(getAllProductList).start();
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

    Runnable getAllProductList = new Runnable()
    {
        @Override
        public void run()
        {
            plat = new GetAllProductListAsyncTask();

            plat.execute(minVal+"");

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
                minVal = minVal+listResponse.productList.size();
                productList.addAll(listResponse.productList);
                Log.d("Product List Size", listResponse.productList.size() + "");
                itemAdapter = new ItemActivityAdapter(context,
                        getItemIds(productList.size()),
                        productList);
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

    Runnable getFilteredProductList = new Runnable()
    {
        @Override
        public void run()
        {
            fplat = new GetFilteredProductListAsyncTask();

            fplat.execute(filterId,minVal+"");

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
                minVal = minVal+listResponse.productList.size();
                Log.d("Product List Size", listResponse.productList.size()+"");
                productList.addAll(listResponse.productList);
                itemAdapter = new ItemActivityAdapter(context,
                        getItemIds(productList.size()),
                        productList);

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
                && productList.size() != 0)
        {

            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(itemAdapter);
            itemListView.setSelection(lastPosition);
            refresh = 1;
        }
        else
        {
            noProduct.setVisibility(View.VISIBLE);
        }
    }

    private void setUpUI()
    {
        spinnerRL.setVisibility(View.INVISIBLE);
        if (listResponse != null
                && productList.size() != 0)
        {

            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(itemAdapter);
            filterListView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,brandNameList));
            itemListView.setSelection(lastPosition);
            refresh = 1;
        }
        else
        {

            noProduct.setVisibility(View.VISIBLE);
        }
    }


}
