package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.BrandFilterAdapter;
import in.savegenie.savegenie.adapters.ItemActivityAdapter;
import in.savegenie.savegenie.adapters.SubTypeFilterAdapter;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.internetcommunication.GetProductListAsyncTask;
import in.savegenie.savegenie.responses.ProductListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class CategoryItemFragment extends Fragment
{

    private ItemActivityAdapter itemAdapter;
    private BrandFilterAdapter filterAdapter;
    private ListView subTypeFilterList;
    private SubTypeFilterAdapter subTypeFilterAdapter;
    private GetProductListAsyncTask plat;
    private ProductListResponse listResponse;
    private ListView filterListView, itemListView;
    private boolean filtered;
    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private DrawerLayout mDrawerLayout;
    private Handler mHandler;
    private ListView sortFilterList;
    protected Button applyButton, clearButton;
    private String pid, brand_id, filter_type, subTypeId, pName, title;
    private int runThread;
    private String sortFilterArray[] = {"Popularity", "Deal And Offers", "Discount by %", "Per Unit Price (Low - High)", "Per Unit Price (High - Low)", "Discount by Rs"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_item_category, container, false);
        filterListView = (ListView) rootView.findViewById(R.id.brandFilterList);
        spinnerRL = (RelativeLayout) rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout) rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);
        subTypeFilterList = (ListView) rootView.findViewById(R.id.subTypeFilterList);
        sortFilterList = (ListView) rootView.findViewById(R.id.sortFilterList);
        clearButton = (Button) rootView.findViewById(R.id.clearButton);
        applyButton = (Button) rootView.findViewById(R.id.applyButton);
        itemListView = (ListView) rootView.findViewById(R.id.quickList);
        mDrawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.END);
        sortFilterList.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, sortFilterArray));

        applyButton.setOnClickListener(new View.OnClickListener()
        {

            int[] checkBrand;
            int[] checkSubType;

            @Override
            public void onClick(View arg0)
            {
                filtered = true;
                mDrawerLayout.closeDrawer(Gravity.END);
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.VISIBLE);
                checkBrand = filterAdapter.getCheckedState();

                checkSubType = subTypeFilterAdapter.getCheckedState();

                for (int i = 0; i < checkBrand.length; i++)
                {
                    if (checkBrand[i] == 1)
                    {
                        if (brand_id.equals(""))
                        {
                            brand_id = listResponse.brandList.get(i).id;
                        }
                        else
                        {
                            brand_id = brand_id + "-" + listResponse.brandList.get(i).id;
                        }
                        checkBrand[i] = 0;
                    }
                }
                for (int i = 0; i < checkSubType.length; i++)
                {
                    if (checkSubType[i] == 1)
                    {
                        if (subTypeId.equals(""))
                        {
                            subTypeId = listResponse.subTypeList.get(i).id;
                        }
                        else
                        {
                            subTypeId = brand_id + "-" + listResponse.subTypeList.get(i).id;
                        }
                        checkSubType[i] = 0;
                    }
                }
                Log.d("brand", brand_id);
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getCategoryProductList).start();
                subTypeFilterAdapter.resetCheckStatus();
                filterAdapter.resetCheckStatus();

            }
        });

        clearButton.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                noProduct.setVisibility(View.INVISIBLE);
                mDrawerLayout.closeDrawer(Gravity.END);
                spinnerRL.setVisibility(View.VISIBLE);
                brand_id = "0";
                subTypeId = "0";
                filtered = false;
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getCategoryProductList).start();

            }
        });


        sortFilterList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mDrawerLayout.closeDrawer(Gravity.END);
                spinnerRL.setVisibility(View.VISIBLE);
                filter_type = (position + 1) + "";
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                itemListView.setVisibility(View.INVISIBLE);
                new Thread(getCategoryProductList).start();
            }
        });


        if (runThread == 1)
        {
            spinnerRL.setVisibility(View.VISIBLE);
            noProduct.setVisibility(View.INVISIBLE);
            itemListView.setVisibility(View.INVISIBLE);
            new Thread(getCategoryProductList).start();
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
        brand_id = "0";
        subTypeId = "0";
        filter_type = "1";
        pName = getArguments().getString(IntentString.ITEM_ACTIVITY_CATEGORY_NAME);
        pid = getArguments().getString(IntentString.ITEM_ACTIVITY_CATEGORY_ID);
        title = pName;
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

    protected Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    Runnable getCategoryProductList = new Runnable()
    {
        @Override
        public void run()
        {
            plat = new GetProductListAsyncTask();

            plat.execute(pid, filter_type, brand_id, subTypeId);
            Log.d("pid", pid);
            Log.d("filter_type", filter_type);
            Log.d("brand_id", brand_id);
            Log.d("subTypeId", subTypeId);

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
                if (!filtered)
                {
                    filterAdapter = new BrandFilterAdapter(context,
                            getItemIds(listResponse.brandList.size()),
                            listResponse.brandList);
                    subTypeFilterAdapter = new SubTypeFilterAdapter(context,
                            getItemIds(listResponse.subTypeList.size()),
                            listResponse.subTypeList);
                }

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



    private void setUpUI()
    {
        spinnerRL.setVisibility(View.INVISIBLE);
        if (listResponse != null
                && listResponse.productList.size() != 0)
        {
            itemListView.setVisibility(View.VISIBLE);
            itemListView.setAdapter(itemAdapter);
            if (!filtered)
            {
                filterListView.setAdapter(filterAdapter);
                subTypeFilterList.setAdapter(subTypeFilterAdapter);
            }
        }
        else
        {
            noProduct.setVisibility(View.VISIBLE);
        }
        brand_id = "";
        subTypeId = "";
    }


}
