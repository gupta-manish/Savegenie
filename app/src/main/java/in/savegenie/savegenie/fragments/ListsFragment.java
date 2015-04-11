package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.ListDetailAdapter;
import in.savegenie.savegenie.adapters.ListFragmentAdapter;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetGroceryListsListAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetSelectedListItemsAsyncTask;
import in.savegenie.savegenie.internetcommunication.MergeListAsyncTask;
import in.savegenie.savegenie.responses.GroceryListResponse;
import in.savegenie.savegenie.responses.GroceryListsListResponse;
import in.savegenie.savegenie.responses.MergeListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class ListsFragment extends Fragment implements ListFragmentAdapter.CheckNumChangeListener
{
    String[] itemNames;
    private RelativeLayout spinnerRL;
    private RelativeLayout noProduct;
    ListFragmentAdapter listFragmentAdapter;
    GetGroceryListsListAsyncTask gat;
    GroceryListsListResponse groceryListsListResponse;
    private GroceryListResponse listResponse;
    private GetSelectedListItemsAsyncTask gsliat;
    private ListDetailAdapter listDetailAdapter;
    private ListView itemListView;
    MergeListAsyncTask mergeListAsyncTask;
    MergeListResponse mergeListResponse;
    String idString;
    private TextView title;
    private Context context;
    ArrayList<String> listNameList;
    ListView listView;
    // private SetItemListAsyncTask silat;
   Button mergeLists;
    private Handler mHandler;
    private Button doneButton;
    private int change;
    ListsFragment listsFragment;
    private int[] checked;
    private Button createNewList,goBack;
    private ListsFragmentListener listsFragmentListener;
    private String listId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        View rootView = inflater.inflate(R.layout.fragment_lists, container, false);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        listsFragmentListener = (ListsFragmentListener)getActivity();
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        title = (TextView)rootView.findViewById(R.id.title);
        spinnerRL.setAlpha((float) 0.50);
        listsFragment = this;
        idString = "";
        context = getActivity();
        listNameList = new ArrayList<String>();
        mHandler = new Handler();
        listView = (ListView)rootView.findViewById(R.id.listList);
        mergeLists = (Button)rootView.findViewById(R.id.mergeList);
        mergeLists.setClickable(false);
        mergeLists.setAlpha(0.5f);
        createNewList = (Button)rootView.findViewById(R.id.createNewList);
        goBack = (Button)rootView.findViewById(R.id.backButton);

        createNewList.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                listsFragmentListener.onCreateNewListClick();
            }
        });

        mergeLists.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checked = listFragmentAdapter.getCheckStatus();

                for (int i=0;i<checked.length;i++)
                {
                    if(checked[i]==1)
                    {
                        if(idString.equals(""))
                        {
                            idString=groceryListsListResponse.groceryListsList.get(i).id;
                        }
                        else
                        {
                            idString = idString + "-" + groceryListsListResponse.groceryListsList.get(i).id;
                        }
                        checked[i] = 0;
                    }
                }
                spinnerRL.setVisibility(View.VISIBLE);
                noProduct.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.INVISIBLE);
                new Thread(mergeListThread).start();
                listFragmentAdapter.resetCheckStatus();
            }
        });

        goBack.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                noProduct.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(listFragmentAdapter);
                mergeLists.setVisibility(View.VISIBLE);
                createNewList.setVisibility(View.VISIBLE);
                goBack.setVisibility(View.INVISIBLE);
            }
        });



        spinnerRL.setVisibility(View.VISIBLE);
        noProduct.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        new Thread(getLists).start();



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
    public void onPause() {
        super.onPause();
    }

    Runnable getLists =new Runnable()
    {
        @Override
        public void run()
        {

            gat = new GetGroceryListsListAsyncTask();

            gat.execute();

            try
            {
                groceryListsListResponse = gat.get();
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


            if (groceryListsListResponse.count > 0)
            {
                listFragmentAdapter = new ListFragmentAdapter(context,getItemIds(groceryListsListResponse.count),
                        groceryListsListResponse.groceryListsList,listsFragment);

            }

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (groceryListsListResponse.count > 0)
                    {
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(listFragmentAdapter);
                    }
                    else
                    {
                        noProduct.setVisibility(View.VISIBLE);
                    }
                }
            });

        }

    };

    @Override
    public void onCheckNumChange(int checkNum)
    {
        if(checkNum>=2)
        {
            mergeLists.setClickable(true);
            mergeLists.setAlpha(1);
        }
        else
        {
            mergeLists.setClickable(false);
            mergeLists.setAlpha(0.5f);
        }
    }

    Runnable mergeListThread = new Runnable()
    {
        @Override
        public void run()
        {
            mergeListAsyncTask = new MergeListAsyncTask();
            mergeListAsyncTask.execute(idString);
            Log.d("Merge String", idString);
            try
            {
                mergeListResponse = mergeListAsyncTask.get();
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
                    if(mergeListResponse!=null)
                    {
                        SharedPreferences preferences = getActivity().getSharedPreferences(
                                SharedPrefString.SHARED_PREFERENCE_KEY, Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SharedPrefString.GROCERY_LIST_ID, mergeListResponse.listid);
                        editor.putString(SharedPrefString.GROCERY_LIST_NAME, mergeListResponse.listname);
                        editor.putBoolean(SharedPrefString.AUTOMATIC_CREATED_LIST, true);
                        editor.apply();
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Lists Merged", Toast.LENGTH_LONG)
                                .show();
                        listsFragmentListener.onMergListClick(mergeListResponse.count);
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "The lists coud not be merged. Sorry for inconvenience", Toast.LENGTH_LONG)
                                .show();
                    }
                    idString = "";
                    new Thread(getLists).start();
                }
            });
        }
    };

    public interface ListsFragmentListener
    {
        public void onCreateNewListClick();
        public void onMergListClick(String count);
    }

    @Override
    public void OnListClick(String listId,String listName)
    {
        this.listId = listId;
        spinnerRL.setVisibility(View.VISIBLE);
        noProduct.setVisibility(View.INVISIBLE);
        listView.setVisibility(View.INVISIBLE);
        mergeLists.setVisibility(View.INVISIBLE);
        createNewList.setVisibility(View.INVISIBLE);
        goBack.setVisibility(View.VISIBLE);
        new Thread(getListItems).start();
        title.setText(listName);
    }



    Runnable getListItems = new Runnable()
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

            listDetailAdapter = new ListDetailAdapter(context,
                    getItemIds(listResponse.itemList.size()),
                    listResponse.itemList,listId);

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (listResponse.itemList.size() != 0) {
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(listDetailAdapter);
                        mergeLists.setVisibility(View.INVISIBLE);
                    } else {
                        noProduct.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    };

}
