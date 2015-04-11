package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.CompareStoreActivityAdapter;
import in.savegenie.savegenie.adapters.CurrentGroceryListActivityAdapter;
import in.savegenie.savegenie.adapters.DealActivityAdapter;
import in.savegenie.savegenie.adapters.ItemActivityAdapter;
import in.savegenie.savegenie.adapters.ListFragmentAdapter;
import in.savegenie.savegenie.adapters.MoreOptionsActivityAdapter;
import in.savegenie.savegenie.adapters.ReviewBasketActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.AlternateItem;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.ListCreateResponse;
import in.savegenie.savegenie.backgroundclasses.MissingItem;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.fragments.AccountFragment;
import in.savegenie.savegenie.fragments.AlternateDialogFragment;
import in.savegenie.savegenie.fragments.BasketFragment;
import in.savegenie.savegenie.fragments.BestSellingFragment;
import in.savegenie.savegenie.fragments.CategoryItemFragment;
import in.savegenie.savegenie.fragments.CompareStoreFragment;
import in.savegenie.savegenie.fragments.CreateNewListDialogFragment;
import in.savegenie.savegenie.fragments.DealFragment;
import in.savegenie.savegenie.fragments.DealItemFragment;
import in.savegenie.savegenie.fragments.FavoriteItemFragment;
import in.savegenie.savegenie.fragments.FeedbackFragment;
import in.savegenie.savegenie.fragments.LeftDrawerFragment;
import in.savegenie.savegenie.fragments.ListsFragment;
import in.savegenie.savegenie.fragments.MissingDialogFragment;
import in.savegenie.savegenie.fragments.MoreOptionsDialogFragment;
import in.savegenie.savegenie.fragments.MyProfileFragment;
import in.savegenie.savegenie.fragments.ProductDealFragment;
import in.savegenie.savegenie.fragments.ReviewBasketFragment;
import in.savegenie.savegenie.fragments.SearchItemFragment;
import in.savegenie.savegenie.fragments.StoreFragment;
import in.savegenie.savegenie.fragments.SwapAndSaveDialogFragment;
import in.savegenie.savegenie.internetcommunication.CreateNewListAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetBasketCountAsyncTask;

public class ItemActivity extends Activity implements LeftDrawerFragment.LeftDrawerListener,
        ItemActivityAdapter.ItemActivityAdapterListener,CurrentGroceryListActivityAdapter.BasketCountChangeListener,
        BasketFragment.BasketFragmentListener,DealFragment.DealFromStoreClickListener, DealActivityAdapter.BasketCountChangeListener,
        ListFragmentAdapter.ListSelectListener,ListsFragment.ListsFragmentListener,CreateNewListDialogFragment.CreateNewListListener,
        AccountFragment.AccountFragmentListener,CompareStoreActivityAdapter.CompareStoreActivityAdapterListener,
        ReviewBasketActivityAdapter.ReviewBasketActivityAdapterListener,MoreOptionsActivityAdapter.MoreOptionsActivityAdapterListener
{

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private String title;
    private Menu menu;
    private GetBasketCountAsyncTask getBasketCountAsyncTask;
    private String basketCount;
    private TextView numOfItems;
    private Handler mHandler;
    private ProgressBar pb;
    private RelativeLayout myAccount, myLists, myStores, myBasket, deals, searchLayout, buttonLayout;
    private FrameLayout dialogFragment;
    private FragmentTransaction mainContentFragmentTransaction;
    private Fragment bestSellingFragment,favoriteItemFragment,categoryItemFragment,searchItemFragment,dealItemFragment,
                    feedbackFragment,compareStoreFragment,myProfileFragment;
    int mainContentFragment,dialogFragmentId;
    private boolean isSearchVisible,isBasketOpen, isAccountOpen, isListsOpen, isStoresOpen, isDealsOpen, isProductDealsOpen;
    private Button searchButton;
    private String categoryId,categoryName,searchString;
    private Bundle categoryItemFragmentBundle,searchBundle;
    private EditText ssearchEditText;
    private DialogFragment createNewListDialogFragment,missingDialogFragment,alternateDialogFragment;
    private String newListName;
    private CreateNewListAsyncTask cnlat;
    private ReviewBasketFragment reviewBasketFragment;
    private DialogFragment activeDialogFragment;
    private SwapAndSaveDialogFragment swapAndSaveDialogFragment;
    private MoreOptionsDialogFragment moreOptionsDialogFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        initializeVariables();
        setLeftDrawerLayout();
        updateBasketCount();
        setButtonLayoutListener();

        addMainContentFragment(bestSellingFragment);


    }

    private void initializeVariables()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mHandler = new Handler();
        pb = (ProgressBar) findViewById(R.id.countSpinner);
        myAccount = (RelativeLayout) findViewById(R.id.myAccount);
        myLists = (RelativeLayout) findViewById(R.id.myLists);
        myStores = (RelativeLayout) findViewById(R.id.myStores);
        myBasket = (RelativeLayout) findViewById(R.id.myBasket);
        deals = (RelativeLayout) findViewById(R.id.deals);
        numOfItems = (TextView)findViewById(R.id.numOfItems);
        dialogFragmentId = R.id.dialogFragment;
        mainContentFragment = R.id.mainContentFragment;
        basketCount = "0";
        isSearchVisible = false;
        categoryId = "";
        categoryName = "";
        searchLayout = (RelativeLayout)findViewById(R.id.searchLayout);
        dialogFragment = (FrameLayout)findViewById(R.id.dialogFragment);
        searchButton = (Button)findViewById(R.id.searchButton);
        bestSellingFragment = new BestSellingFragment();
        favoriteItemFragment = new FavoriteItemFragment();
        createNewListDialogFragment = new CreateNewListDialogFragment();
        ssearchEditText = (EditText) findViewById(R.id.searchEditText);
        feedbackFragment = new FeedbackFragment();

    }

    private void setLeftDrawerLayout()
    {
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,R.drawable.ic_drawer, R.string.open_drawer,
                R.string.close_drawer)
        {

            @Override
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                getActionBar().setTitle(title);
            }

            @Override
            public void onDrawerOpened(View drawerView)
            {
                super.onDrawerOpened(drawerView);
                getActionBar().setTitle(R.string.app_name);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public void onBackPressed()
    {
        // TODO Auto-generated method stub
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
        {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        else if(isSearchVisible)
        {
            searchLayout.setVisibility(View.INVISIBLE);
            isSearchVisible = false;
        }
        else if (isBasketOpen || isAccountOpen || isListsOpen || isStoresOpen || isDealsOpen|| isProductDealsOpen)
        {
            dialogFragment.setVisibility(View.INVISIBLE);
            isBasketOpen = false;
            isAccountOpen = false;
            isListsOpen = false;
            isStoresOpen = false;
            isDealsOpen = false;
            isProductDealsOpen = false;
        }
        else
        {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        this.menu = menu;
        inflater.inflate(R.menu.actions, menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (mDrawerToggle.onOptionsItemSelected(item))
        {
            if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
            {
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
            }

            return true;
        }
        if (mDrawerLayout.isDrawerOpen(Gravity.START))
        {
            mDrawerLayout.closeDrawer(Gravity.START);
        }
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT))
        {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        }
        switch (item.getItemId())
        {
            case R.id.search:
            {
                if(!isSearchVisible)
                {
                    searchLayout.setVisibility(View.VISIBLE);
                    isSearchVisible = true;
                }
                else
                {
                    searchLayout.setVisibility(View.INVISIBLE);
                    isSearchVisible = false;
                }
            }
        }

        return false;
    }



    @Override
    public void onCategoryClick(String productName, String productId)
    {
        clearTheFragmentBackStack();
        mDrawerLayout.closeDrawer(Gravity.START);
        if(!categoryId.equals(productId))
        {
            categoryItemFragmentBundle = new Bundle();
            categoryItemFragment = new CategoryItemFragment();
            categoryId = productId;
            categoryName = productName;
            categoryItemFragmentBundle.putString(IntentString.ITEM_ACTIVITY_CATEGORY_ID,categoryId);
            categoryItemFragmentBundle.putString(IntentString.ITEM_ACTIVITY_CATEGORY_NAME, categoryName);
            categoryItemFragment.setArguments(categoryItemFragmentBundle);
            addMainContentFragment(categoryItemFragment);
        }
    }

    @Override
    public void onBestSellingClick()
    {
        clearTheFragmentBackStack();
        mDrawerLayout.closeDrawer(Gravity.START);
        if(!bestSellingFragment.isVisible())
        {
            addMainContentFragment(bestSellingFragment);
        }
    }

    @Override
    public void onFavoriteClick()
    {
        clearTheFragmentBackStack();
        mDrawerLayout.closeDrawer(Gravity.START);
        if(!favoriteItemFragment.isVisible())
        {
            addMainContentFragment(favoriteItemFragment);
        }
    }

    @Override
    public void onDealLeftClick()
    {
        mDrawerLayout.closeDrawer(Gravity.START);
        deals.performClick();
    }

    Runnable changeBasketCountRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            getBasketCountAsyncTask = new GetBasketCountAsyncTask();
            getBasketCountAsyncTask.execute();
            try
            {
                basketCount = getBasketCountAsyncTask.get();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    numOfItems.setText(basketCount+"");
                    numOfItems.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.INVISIBLE);
                }
            });

        }
    };

    private void updateBasketCount()
    {
        numOfItems.setVisibility(View.INVISIBLE);
        pb.setVisibility(View.VISIBLE);
        new Thread(changeBasketCountRunnable).start();
    }

    private void addMainContentFragment(Fragment fragment)
    {
        mainContentFragmentTransaction = getFragmentManager().beginTransaction();
        mainContentFragmentTransaction.replace(mainContentFragment,fragment);
        mainContentFragmentTransaction.commit();
    }

    private void addMainContentFragmentOnBackStack(Fragment fragment)
    {
        mainContentFragmentTransaction = getFragmentManager().beginTransaction();
        mainContentFragmentTransaction.replace(mainContentFragment,fragment);
        mainContentFragmentTransaction.addToBackStack(null);
        mainContentFragmentTransaction.commit();
    }

    private void removeFragment(Fragment fragment)
    {
        mainContentFragmentTransaction = getFragmentManager().beginTransaction();
        mainContentFragmentTransaction.remove(fragment);
        mainContentFragmentTransaction.commit();
    }


    @Override
    public void onBasketCountChange()
    {
        updateBasketCount();
    }

    @Override
    public void OnIsDealClick(String is_deal)
    {
        dialogFragment.setVisibility(View.VISIBLE);
        isAccountOpen = false;
        isBasketOpen = false;
        isListsOpen = false;
        isStoresOpen = false;
        isDealsOpen = false;
        isProductDealsOpen = true;
        Bundle b = new Bundle();
        b.putString("IS_DEAL",is_deal);
        ProductDealFragment list = new ProductDealFragment();
        list.setArguments(b);
        getFragmentManager().beginTransaction().replace(dialogFragmentId, list).commit();
    }

    public void setButtonLayoutListener()
    {
        myBasket.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isBasketOpen == false)
                {
                    dialogFragment.setVisibility(View.VISIBLE);
                    isBasketOpen = true;
                    isAccountOpen = false;
                    isListsOpen = false;
                    isDealsOpen = false;
                    isProductDealsOpen = false;
                    isStoresOpen = false;
                    BasketFragment basket = new BasketFragment();
                    basket.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().replace(dialogFragmentId, basket).commit();
                }
                else
                {
                    dialogFragment.setVisibility(View.INVISIBLE);
                    isBasketOpen = false;
                }
            }
        });

        myAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isAccountOpen == false)
                {
                    dialogFragment.setVisibility(View.VISIBLE);
                    isAccountOpen = true;
                    isBasketOpen = false;
                    isListsOpen = false;
                    isProductDealsOpen = false;
                    isStoresOpen = false;
                    isDealsOpen = false;
                    AccountFragment basket = new AccountFragment();
                    basket.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().replace(dialogFragmentId, basket).commit();
                }
                else
                {
                    dialogFragment.setVisibility(View.INVISIBLE);
                    isAccountOpen = false;
                }


            }
        });

        myLists.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isListsOpen == false)
                {
                    dialogFragment.setVisibility(View.VISIBLE);
                    isAccountOpen = false;
                    isBasketOpen = false;
                    isListsOpen = true;
                    isProductDealsOpen = false;
                    isDealsOpen = false;
                    isStoresOpen = false;
                    ListsFragment list = new ListsFragment();
                    list.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().replace(dialogFragmentId, list).commit();
                }
                else
                {
                    dialogFragment.setVisibility(View.INVISIBLE);
                    isListsOpen = false;
                }


            }
        });

        myStores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isStoresOpen == false)
                {
                    dialogFragment.setVisibility(View.VISIBLE);
                    isAccountOpen = false;
                    isProductDealsOpen = false;
                    isBasketOpen = false;
                    isListsOpen = false;
                    isDealsOpen = false;
                    isStoresOpen = true;
                    StoreFragment list = new StoreFragment();
                    list.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().replace(dialogFragmentId, list).commit();
                }
                else
                {
                    dialogFragment.setVisibility(View.INVISIBLE);
                    isStoresOpen = false;
                }


            }
        });

        deals.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (isDealsOpen == false)
                {
                    dialogFragment.setVisibility(View.VISIBLE);
                    isAccountOpen = false;
                    isBasketOpen = false;
                    isListsOpen = false;
                    isStoresOpen = false;
                    isProductDealsOpen = false;
                    isDealsOpen = true;
                    DealFragment list = new DealFragment();
                    list.setArguments(getIntent().getExtras());
                    getFragmentManager().beginTransaction().replace(dialogFragmentId, list).commit();
                }
                else
                {
                    dialogFragment.setVisibility(View.INVISIBLE);
                    isDealsOpen = false;
                }


            }
        });

        searchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                searchItemFragment = new SearchItemFragment();
                searchBundle = new Bundle();
                searchString = ssearchEditText.getText().toString();
                if(searchString.equals(""))
                {
                    Toast.makeText(getApplicationContext(),
                            "Search Empty", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                searchBundle.putString(IntentString.ITEM_ACTIVITY_SEARCH_STRING,searchString);
                searchItemFragment.setArguments(searchBundle);
                addMainContentFragment(searchItemFragment);
            }
        });

    }

    @Override
    public void onReviewBasketClickAfterCompare(String storeId,String storeName,String storeAddress,String deliveryCost)
    {
        reviewBasketFragment = new ReviewBasketFragment();
        Bundle b = new Bundle();
        b.putString(IntentString.REVIEW_BASKET_STORE_ID,storeId);
        b.putString(IntentString.REVIEW_BASKET_STORE_NAME,storeName);
        b.putString(IntentString.REVIEW_BASKET_STORE_ADDRESS,storeAddress);
        b.putString(IntentString.REVIEW_BASKET_DELIVERY_COST,deliveryCost);
        b.putBoolean(IntentString.REVIEW_BASKET_GET_STORE_DETAILS, false);
        reviewBasketFragment.setArguments(b);
        addMainContentFragmentOnBackStack(reviewBasketFragment);
    }

    @Override
    public void setTitle(CharSequence title)
    {
        this.title = title.toString();
        super.setTitle(title);
    }

    @Override
    public void onReviewBasketClick(String storeId, String storeName)
    {
        myBasket.performClick();
        reviewBasketFragment = new ReviewBasketFragment();
        Bundle b = new Bundle();
        b.putString(IntentString.REVIEW_BASKET_STORE_ID,storeId);
        b.putString(IntentString.REVIEW_BASKET_STORE_NAME,storeName);
        b.putBoolean(IntentString.REVIEW_BASKET_GET_STORE_DETAILS,true);
        reviewBasketFragment.setArguments(b);
        addMainContentFragmentOnBackStack(reviewBasketFragment);
    }

    @Override
    public void onMissingClick(ArrayList<MissingItem> missingList)
    {
        missingDialogFragment = new MissingDialogFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(IntentString.ITEM_ACTIVITY_MISSING_LIST,missingList);
        missingDialogFragment.setArguments(b);
        missingDialogFragment.show(getFragmentManager(),"MissingDialogFragment");
        activeDialogFragment = missingDialogFragment;
    }

    @Override
    public void onAlternateClick(ArrayList<AlternateItem> alternateList)
    {
        alternateDialogFragment = new AlternateDialogFragment();
        Bundle b = new Bundle();
        b.putParcelableArrayList(IntentString.ITEM_ACTIVITY_ALTERNATE_LIST,alternateList);
        alternateDialogFragment.setArguments(b);
        alternateDialogFragment.show(getFragmentManager(),"AlternateDialogFragment");
        activeDialogFragment = alternateDialogFragment;
    }

    @Override
    public void onCompareStoreClick()
    {

        myBasket.performClick();
        compareStoreFragment = new CompareStoreFragment();
        addMainContentFragmentOnBackStack(compareStoreFragment);
    }

    @Override
    public void onDealFromStoreClick(String store_id)
    {
        clearTheFragmentBackStack();
        deals.performClick();
        dealItemFragment = new DealItemFragment();
        Bundle dealItemFragmentBundle = new Bundle();
        dealItemFragmentBundle.putString(IntentString.ITEM_ACTIVITY_DEAL_STORE_ID,store_id);
        dealItemFragment.setArguments(dealItemFragmentBundle);
        addMainContentFragment(dealItemFragment);
    }

    @Override
    public void OnListSelect()
    {
        updateBasketCount();
    }


    @Override
    public void onCreateNewListClick()
    {
        createNewListDialogFragment.show(getFragmentManager(),"CreateNewListDialogFragment");
        activeDialogFragment = createNewListDialogFragment;
    }

    @Override
    public void onMergListClick(String count)
    {
        basketCount = count;
        numOfItems.setText(count);
    }

    Runnable createNewList = new Runnable()
    {
        ListCreateResponse listCreateResponse;
        @Override
        public void run()
        {
            cnlat = new CreateNewListAsyncTask();

            cnlat.execute(newListName);

            try
            {
                listCreateResponse = cnlat.get();
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



            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if(listCreateResponse == null)
                    {
                        Toast.makeText(getApplicationContext(),
                                "List not created due to no internet connection", Toast.LENGTH_LONG)
                                .show();
                    }
                    else if (listCreateResponse.result.equals("1"))
                    {
                        Toast.makeText(getApplicationContext(),
                                "New List Created", Toast.LENGTH_LONG)
                                .show();
                        SharedPreferences preferences = getSharedPreferences(
                                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SharedPrefString.GROCERY_LIST_ID, listCreateResponse.id);
                        editor.putString(SharedPrefString.GROCERY_LIST_NAME, newListName);
                        editor.apply();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "List not created", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });
        }
    };

    @Override
    public void OnCreateNewList(String listName)
    {
        myLists.performClick();
        newListName = listName;
        new Thread(createNewList).start();
        basketCount = "0";
        numOfItems.setText("0");
    }

    @Override
    public void onFeedbackClick()
    {
        myAccount.performClick();
        addMainContentFragmentOnBackStack(feedbackFragment);
    }

    @Override
    public void onMyOrdersClick()
    {

    }

    @Override
    public void onMyProfileClick()
    {
        myProfileFragment = new MyProfileFragment();
        addMainContentFragmentOnBackStack(myProfileFragment);
    }

    public void changeButtonLayout()
    {
        if(myLists.getVisibility() == View.VISIBLE)
        {
            myLists.setVisibility(View.GONE);
            myBasket.setVisibility(View.GONE);
            myStores.setVisibility(View.GONE);
        }
        else
        {
            myLists.setVisibility(View.VISIBLE);
            myBasket.setVisibility(View.VISIBLE);
            myStores.setVisibility(View.VISIBLE);
        }
    }

    public void clearTheFragmentBackStack()
    {
        if(getFragmentManager().getBackStackEntryCount() >= 1)
        getFragmentManager().popBackStack(null, getFragmentManager().POP_BACK_STACK_INCLUSIVE);
    }


    @Override
    public void onSwapAndSaveClick(String oneProductData, String productskuid,int position)
    {
        swapAndSaveDialogFragment = new SwapAndSaveDialogFragment();
        Bundle b = new Bundle();
        b.putString(IntentString.ONE_PRODUCT_DATA,oneProductData);
        b.putString(IntentString.PRODUCT_SKU_ID,productskuid);
        b.putInt(IntentString.MORE_OPTION_POSITION,position);
        swapAndSaveDialogFragment.setArguments(b);
        swapAndSaveDialogFragment.show(getFragmentManager(),"SwapAndSaveDialogFragment");
        activeDialogFragment = swapAndSaveDialogFragment;
    }

    @Override
    public void onMoreOptionsClick(String oneProductData, String productskuid,int position)
    {
        moreOptionsDialogFragment = new MoreOptionsDialogFragment();
        Bundle b = new Bundle();
        b.putString(IntentString.ONE_PRODUCT_DATA,oneProductData);
        b.putString(IntentString.PRODUCT_SKU_ID,productskuid);
        b.putInt(IntentString.MORE_OPTION_POSITION,position);
        moreOptionsDialogFragment.setArguments(b);
        moreOptionsDialogFragment.show(getFragmentManager(),"MoreOptionsDialogFragment");
        activeDialogFragment = moreOptionsDialogFragment;
    }

    @Override
    public void onSelectAlternateClick(AlternateItem alternateItem, int positionInReviewBasket)
    {
        activeDialogFragment.dismiss();
        reviewBasketFragment.replace(alternateItem,positionInReviewBasket);
    }

    public void openBestSelling()
    {
        clearTheFragmentBackStack();
        addMainContentFragment(bestSellingFragment);
    }
}
