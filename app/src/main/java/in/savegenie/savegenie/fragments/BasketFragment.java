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
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.CurrentGroceryListActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetSelectedListItemsAsyncTask;
import in.savegenie.savegenie.responses.GroceryListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class BasketFragment extends Fragment
{

    private String title;
    String[] itemNames;
    private CurrentGroceryListActivityAdapter adapter;
    private RelativeLayout spinnerRL;
    private RelativeLayout noProduct;
    private GetSelectedListItemsAsyncTask gsliat;
    private GroceryListResponse listResponse;
    private ListView itemListView;
    private Context context;
    // private SetItemListAsyncTask silat;
    private Handler mHandler;
    private Button doneButton;
    private int change;
    private Button compareStores,buyFrom;
    private TextView basket_name;
    private BasketFragmentListener basketFragmentListener;

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        basketFragmentListener = (BasketFragmentListener)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_basket, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setAlpha((float) 0.50);


        title = prefs.getString(SharedPrefString.GROCERY_LIST_NAME, "Grocery List");
        itemListView = (ListView)rootView.findViewById(R.id.quickList);
        basket_name = (TextView)rootView.findViewById(R.id.basket_name);
        basket_name.setText(title);
        mHandler = new Handler();
        final String listId = prefs.getString(SharedPrefString.GROCERY_LIST_ID, null);
        spinnerRL = (RelativeLayout) rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout) rootView.findViewById(R.id.noProduct);
        context = getActivity();


        buyFrom = (Button)rootView.findViewById(R.id.buyFrom);
        compareStores = (Button)rootView.findViewById(R.id.compareStores);

        spinnerRL.setVisibility(View.VISIBLE);
        noProduct.setVisibility(View.INVISIBLE);
        itemListView.setVisibility(View.INVISIBLE);
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                gsliat = new GetSelectedListItemsAsyncTask();

                gsliat.execute(listId);

                try {
                    listResponse = gsliat.get();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                adapter = new CurrentGroceryListActivityAdapter(context,
                        getItemIds(listResponse.itemList.size()),
                        listResponse.itemList);

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        spinnerRL.setVisibility(View.INVISIBLE);
                        if (listResponse.itemList.size() != 0) {
                            itemListView.setVisibility(View.VISIBLE);
                            itemListView.setAdapter(adapter);
                        } else {
                            noProduct.setVisibility(View.VISIBLE);
                        }

                    }
                });

            }

        }).start();

        boolean isStoreSelected = prefs.getBoolean(SharedPrefString.IS_STORE_SELECTED,
                false);
        final String userStoreName = prefs.getString(SharedPrefString.GROCERY_STORE_NAME,
                null);
        final String userStoreId = prefs.getString(SharedPrefString.GROCERY_STORE_ID,
                null);

        if (isStoreSelected)
        {
            buyFrom.setText("Buy From "+userStoreName);
            buyFrom.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    basketFragmentListener.onReviewBasketClick(userStoreId,userStoreName);
                }
            });
        }
        else
        {
            buyFrom.setVisibility(View.INVISIBLE);
        }

        compareStores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                basketFragmentListener.onCompareStoreClick();
            }
        });

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
    public void onPause()
    {
        super.onPause();
    }

    public interface BasketFragmentListener
    {
        public void onReviewBasketClick(String storeId,String storeName);

        public void onCompareStoreClick();
    }
}
