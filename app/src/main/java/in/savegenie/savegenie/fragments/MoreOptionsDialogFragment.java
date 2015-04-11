package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.MoreOptionsActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.AlternateItem;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetMoreAlternateOptionsAsyncTask;
import in.savegenie.savegenie.responses.MoreOptionsResponse;

/**
 * Created by manish on 8/4/15.
 */
public class MoreOptionsDialogFragment extends DialogFragment
{
    private MoreOptionsActivityAdapter adapter;
    private ListView itemListView;
    private ArrayList<AlternateItem> alternateItemList;
    private GetMoreAlternateOptionsAsyncTask  gmaoat;
    private String storeId,storeListString,oneProductData,productskuid,flag;
    private MoreOptionsResponse moreOptionsResponse;
    private RelativeLayout spinnerRl;
    private Handler mHandler;
    private SharedPreferences prefs;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
        oneProductData = getArguments().getString(IntentString.ONE_PRODUCT_DATA);
        productskuid = getArguments().getString(IntentString.PRODUCT_SKU_ID);
        flag = "0";
        position = getArguments().getInt(IntentString.MORE_OPTION_POSITION);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_dialog_list_with_spinner, null);
        itemListView = (ListView) rootView.findViewById(R.id.quickList);
        spinnerRl = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);

        spinnerRl.setVisibility(View.VISIBLE);
        itemListView.setVisibility(View.INVISIBLE);

        new Thread(getAlternates).start();

        builder.setView(rootView).setTitle("More Options")
                .setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        prefs = activity.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        storeId = prefs.getString(SharedPrefString.GROCERY_STORE_ID,null);
        storeListString = prefs.getString(SharedPrefString.STORE_LIST_STRING,null);
    }

    private Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    Runnable getAlternates = new Runnable()
    {
        @Override
        public void run()
        {
            gmaoat = new GetMoreAlternateOptionsAsyncTask();

            gmaoat.execute(storeId, storeListString, oneProductData, productskuid, flag);

            try
            {
                moreOptionsResponse = gmaoat.get();
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


            adapter = new MoreOptionsActivityAdapter(getActivity(),
                    getItemIds(moreOptionsResponse.moreOptionList.size()),
                    moreOptionsResponse.moreOptionList,position);

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRl.setVisibility(View.INVISIBLE);
                    if (moreOptionsResponse.moreOptionList.size() != 0)
                    {
                        itemListView.setVisibility(View.VISIBLE);
                        itemListView.setAdapter(adapter);
                    }

                }
            });

        }
    };

}
