package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.AddRemoveItemToListAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetSelectedListItemsAsyncTask;
import in.savegenie.savegenie.responses.GroceryListResponse;

/**
 * Created by manish on 4/4/15.
 */
public class AccountFragment extends Fragment
{
    private String title;
    String[] itemNames;
    private RelativeLayout spinnerRL;
    private RelativeLayout noProduct;
    private GetSelectedListItemsAsyncTask gsliat;
    private AddRemoveItemToListAsyncTask aritlat;
    private GroceryListResponse listResponse;
    private ListView itemListView;
    private Context context;
    // private SetItemListAsyncTask silat;
    private Handler mHandler;
    private Button doneButton;
    private int change;
    SharedPreferences prefs;
    AccountFragmentListener accountFragmentListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        String activity[] = {"My Orders","My Profile","Feedback","Log Out"};
        View rootView = inflater.inflate(R.layout.fragment_account, container, false);
        prefs = this.getActivity().getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);

        String firstName = prefs.getString(SharedPrefString.FIRST_NAME, null);

        context = getActivity();
        ListView listView = (ListView)rootView.findViewById(R.id.account_list);
        TextView userName = (TextView)rootView.findViewById(R.id.userName);

        userName.setText("Hi " + firstName + "!");

        listView.setAdapter(new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,activity));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch (i)
                {
                    case 2:
                    {
                        accountFragmentListener.onFeedbackClick();
                        break;
                    }
                    case 3:
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.commit();
                        Intent start = new Intent(
                                IntentString.START_ACTIVITY);
                        startActivity(start);
                        getActivity().finish();
                        break;
                    }
                    case 1:
                    {
                        accountFragmentListener.onMyProfileClick();
                        break;
                    }
                }
            }
        });



        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        accountFragmentListener = (AccountFragmentListener)activity;
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

    public interface AccountFragmentListener
    {
        public void onFeedbackClick();
        public void onMyOrdersClick();
        public void onMyProfileClick();
    }
}
