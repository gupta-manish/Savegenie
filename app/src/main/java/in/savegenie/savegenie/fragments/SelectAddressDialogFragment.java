package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.ShippingAddressDialogAdapter;
import in.savegenie.savegenie.backgroundclasses.ShippingAddress;
import in.savegenie.savegenie.internetcommunication.GetShippingAddressListAsyncTask;
import in.savegenie.savegenie.responses.ShippingAddressResponse;

/**
 * Created by manish on 8/4/15.
 */
public class SelectAddressDialogFragment extends DialogFragment
{
    private ShippingAddressDialogAdapter adapter;
    private ListView itemListView;
    private GetShippingAddressListAsyncTask  gsalat;
    private ShippingAddressResponse listResponse;
    private RelativeLayout spinnerRL,noProduct;
    private Handler mHandler;
    private SelectAddressDialogFragmentListener selectAddressDialogFragmentListener;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_dialog_list_with_spinner, null);
        itemListView = (ListView) rootView.findViewById(R.id.quickList);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout)rootView.findViewById(R.id.noProduct);
        spinnerRL.setVisibility(View.VISIBLE);
        itemListView.setVisibility(View.INVISIBLE);

        new Thread(getList).start();

        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                selectAddressDialogFragmentListener.onAddressClick(listResponse.addressList.get(i));
                dismiss();
            }
        });

        builder.setView(rootView).setTitle("Select Address")
                .setPositiveButton(R.string.enterNewAddress, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        selectAddressDialogFragmentListener.onEnterNewAddressClick();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        selectAddressDialogFragmentListener = (SelectAddressDialogFragmentListener)activity;
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

    Runnable getList = new Runnable()
    {
        @Override
        public void run()
        {

            gsalat = new GetShippingAddressListAsyncTask();

            gsalat.execute();

            try
            {
                listResponse = gsalat.get();
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

            adapter = new ShippingAddressDialogAdapter(getActivity(),
                    getItemIds(listResponse.addressList.size()),
                    listResponse.addressList);

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (listResponse.addressList.size() != 0)
                    {
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

    public interface SelectAddressDialogFragmentListener
    {
        public void onAddressClick(ShippingAddress address);
        public void onEnterNewAddressClick();
    }



}
