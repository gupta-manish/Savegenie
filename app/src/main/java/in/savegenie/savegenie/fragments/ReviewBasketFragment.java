package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.activities.ItemActivity;
import in.savegenie.savegenie.adapters.ReviewBasketActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.AlternateItem;
import in.savegenie.savegenie.backgroundclasses.FinalBundleStrings;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.backgroundclasses.StoreSkuData;
import in.savegenie.savegenie.internetcommunication.GetReviewBasketProductsAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetStoreDetailsAsyncTask;
import in.savegenie.savegenie.responses.ReviewResponse;
import in.savegenie.savegenie.responses.StoreDetailResponse;

/**
 * Created by manish on 4/4/15.
 */
public class ReviewBasketFragment extends Fragment
{

    private ReviewBasketActivityAdapter adapter;
    private GetStoreDetailsAsyncTask getStoreDetailsAsyncTask;
    private StoreDetailResponse storeDetailResponse;
    private GetReviewBasketProductsAsyncTask grbpat;
    private ReviewResponse reviewResponse;
    private ListView itemListView;
    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private Handler mHandler;
    public static String title = "Review Basket";
    private String storeId,storeName,storeAddress,deliveryCost,listId,storeListString;
    private boolean getDetails;
    private SharedPreferences prefs;
    private int[] altSelected;
    private String[] savings;
    private Button buyButton;
    private ArrayList<String> finalList;
    private Bundle finalBundle;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_review_basket, container, false);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        buyButton = (Button)rootView.findViewById(R.id.buyButton);
        spinnerRL.setAlpha((float) 0.50);
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        storeId = getArguments().getString(IntentString.REVIEW_BASKET_STORE_ID);
        storeName = getArguments().getString(IntentString.REVIEW_BASKET_STORE_NAME);
        getDetails = getArguments().getBoolean(IntentString.REVIEW_BASKET_GET_STORE_DETAILS);
        if(getDetails)
        {
            new Thread(getStoreDetails).start();
        }
        else
        {
            storeAddress = getArguments().getString(IntentString.REVIEW_BASKET_STORE_ADDRESS);
            deliveryCost = getArguments().getString(IntentString.REVIEW_BASKET_DELIVERY_COST);
        }

        buyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finalList = new ArrayList<String>();
                int[] quantity = adapter.getQuantity();
                for (int i = 0; i < reviewResponse.selectedProductList.size(); i++)
                {

                    if (!(reviewResponse.selectedProductList.get(i).storeSkuDataList.get(0).ProductSkuId
                            .equals("")))
                    {
                        StoreSkuData storeSkuData = reviewResponse.selectedProductList.get(i).storeSkuDataList.get(0);
                        finalList.add(storeSkuData.ProductSkuId + "-" + storeSkuData.store_sku_id + "-" + quantity[i]);
                    }
                }

                finalBundle = new Bundle();
                finalBundle.putString(FinalBundleStrings.USER_SORE_ID, storeId);
                finalBundle.putString(FinalBundleStrings.SELECTED_STORES_ID, storeListString);
                finalBundle.putString(FinalBundleStrings.BEST_PRICE, "0");
                finalBundle.putString(FinalBundleStrings.VIRTUAL_STORES_IDS, "0");
                finalBundle.putString(FinalBundleStrings.DELIVERY_COST, deliveryCost);
                finalBundle.putString(FinalBundleStrings.STORE_NAME, storeName);
                finalBundle.putString(FinalBundleStrings.STORE_ADDRESS, storeAddress);
                finalBundle.putStringArrayList(FinalBundleStrings.ORDER_ITEMS, finalList);
                Intent pda = new Intent(
                        IntentString.PICK_DEL_ACTIVITY);
                pda.putExtra(FinalBundleStrings.FINAL_BUNDLE, finalBundle);
                startActivity(pda);
            }
        });

        spinnerRL.setVisibility(View.VISIBLE);
        noProduct.setVisibility(View.INVISIBLE);
        itemListView.setVisibility(View.INVISIBLE);
        new Thread(getReviewBasketList).start();

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
        prefs = activity.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        listId = prefs.getString(SharedPrefString.GROCERY_LIST_ID,null);
        storeListString = prefs.getString(SharedPrefString.STORE_LIST_STRING,null);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        ((ItemActivity)getActivity()).changeButtonLayout();
    }

    Runnable getStoreDetails = new Runnable()
    {
        @Override
        public void run()
        {
            getStoreDetailsAsyncTask = new GetStoreDetailsAsyncTask();
            getStoreDetailsAsyncTask.execute(storeId);
            Log.d("store id", storeId);

            try
            {
                storeDetailResponse = getStoreDetailsAsyncTask.get();
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

            if (storeDetailResponse != null)
            {
                storeAddress = storeDetailResponse.storeAddress;
                deliveryCost = storeDetailResponse.deliveryCost;
            }
        }
    };

    Runnable getReviewBasketList = new Runnable()
    {
        @Override
        public void run()
        {
            grbpat = new GetReviewBasketProductsAsyncTask();
            Log.d("listId",listId);
            Log.d("storeId",storeId);
            Log.d("storeListString",storeListString);
            grbpat.execute(listId, "1", storeId, storeListString);


            try
            {
                reviewResponse = grbpat.get();
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

            savings = new String[reviewResponse.selectedProductList.size()];

            for (int i = 0; i < reviewResponse.selectedProductList.size(); i++)
            {
                savings[i] = "";
                for (int j = 0; j < reviewResponse.swapProductList.size(); j++)
                {
                    Log.d("Product Id",
                            reviewResponse.selectedProductList.get(i).productSkuId
                                    + "="
                                    + reviewResponse.swapProductList.get(j).productId
                    );
                    if ((reviewResponse.selectedProductList.get(i).productSkuId)
                            .equals(reviewResponse.swapProductList.get(j).productId))
                    {

                        savings[i] = reviewResponse.swapProductList.get(j).saving;
                        break;
                    }
                }
            }

            altSelected = new int[reviewResponse.selectedProductList.size()];

            adapter = new ReviewBasketActivityAdapter(context,
                    getItemIds(reviewResponse.selectedProductList.size()),
                    reviewResponse.selectedProductList, savings,
                    altSelected);

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (reviewResponse.selectedProductList.size() != 0)
                    {
                        itemListView.setVisibility(View.VISIBLE);
                        itemListView.setAdapter(adapter);
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
        ((ItemActivity)getActivity()).changeButtonLayout();
    }


    public void replace(AlternateItem alternateItem,int position)
    {
        reviewResponse.selectedProductList.get(position).storeSkuDataList
                .set(0, alternateItem.toStoreSkuData());
        altSelected[position] = 1;
        adapter = new ReviewBasketActivityAdapter(context,
                getItemIds(reviewResponse.selectedProductList.size()),
                reviewResponse.selectedProductList, savings, altSelected);
        adapter.notifyDataSetChanged();

        itemListView.setAdapter(adapter);
        itemListView.setSelection(position);
    }
}
