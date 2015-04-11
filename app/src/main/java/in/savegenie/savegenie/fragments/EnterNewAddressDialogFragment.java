package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.internetcommunication.GetAutoCompleteSocietyNamesAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetNewAddressFormAsyncTask;
import in.savegenie.savegenie.internetcommunication.SaveShippingAddressAsyncTask;
import in.savegenie.savegenie.responses.AddressFormResponse;
import in.savegenie.savegenie.responses.AutoCompleteResponse;

/**
 * Created by manish on 9/4/15.
 */
public class EnterNewAddressDialogFragment extends DialogFragment
{
    EditText name, address, city, mobile, area, landmark;
    RelativeLayout spinnerRL;
    GetNewAddressFormAsyncTask gnafat;
    SaveShippingAddressAsyncTask ssaat;
    GetAutoCompleteSocietyNamesAsyncTask gacsnat;
    AutoCompleteResponse autoCompleteResponse;
    AddressFormResponse addressFormResponse;
    String saveResponse;
    Bundle finalBundle;
    Handler mHandler;
    Button saveButton;
    AutoCompleteTextView societyName;
    ArrayAdapter<String> adapter;
    String key;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_enter_new_shipping_address, null);
        area = (EditText)rootView. findViewById(R.id.areaNew);
        name = (EditText) rootView.findViewById(R.id.nameNew);
        address = (EditText) rootView.findViewById(R.id.addressNew);
        landmark = (EditText) rootView.findViewById(R.id.landmark);
        societyName = (AutoCompleteTextView) rootView.findViewById(R.id.societyName);
        societyName.addTextChangedListener(societyNameWatcher);
        area.setEnabled(false);
        city = (EditText) rootView.findViewById(R.id.cityNew);
        city.setEnabled(false);
        mobile = (EditText) rootView.findViewById(R.id.mobileNew);
        mobile.setEnabled(false);
        spinnerRL = (RelativeLayout) rootView.findViewById(R.id.spinnerRelativeLayout);
        spinnerRL.setAlpha(0.5f);

        spinnerRL.setVisibility(View.VISIBLE);

        new Thread(setUp).start();

        mHandler = new Handler();
        builder.setView(rootView).setTitle("Enter New Address")
                .setPositiveButton(R.string.save, null);

        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Button b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spinnerRL.setVisibility(View.VISIBLE);
                new Thread(saveAddress).start();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }


    Runnable setUp = new Runnable()
    {
        @Override
        public void run()
        {
            gnafat = new GetNewAddressFormAsyncTask();
            gnafat.execute();
            try
            {
                addressFormResponse = gnafat.get();
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
                    spinnerRL.setVisibility(View.INVISIBLE);
                    area.setText(addressFormResponse.areaName);
                    city.setText(addressFormResponse.cityName);
                    mobile.setText(addressFormResponse.mobileno);
                }
            });
        }
    };

    Runnable saveAddress = new Runnable()
    {
        @Override
        public void run()
        {
            ssaat = new SaveShippingAddressAsyncTask();
            ssaat.execute(name.getText().toString(),
                    address.getText().toString(),
                    area.getText().toString(),
                    addressFormResponse.areaid,
                    addressFormResponse.cityid,
                    mobile.getText().toString()
                    ,societyName.getText().toString(),landmark.getText().toString());
            try
            {
                saveResponse = ssaat.get();
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
                    if (saveResponse == null || saveResponse.contains("0"))
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Address Could Not Be Saved", Toast.LENGTH_LONG)
                                .show();
                        spinnerRL.setVisibility(View.INVISIBLE);

                    }
                    else if(saveResponse.contains("1"))
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Address Saved", Toast.LENGTH_LONG)
                                .show();
                        dismiss();
                    }
                }
            });
        }
    };

    private final TextWatcher societyNameWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==3)
            {
                key = s.toString();
                spinnerRL.setVisibility(View.VISIBLE);
                new Thread(getAutoComplete).start();
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    Runnable getAutoComplete = new Runnable()
    {
        @Override
        public void run()
        {
            gacsnat = new GetAutoCompleteSocietyNamesAsyncTask();
            gacsnat.execute(key);
            try
            {
                autoCompleteResponse = gacsnat.get();
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

            adapter = new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_dropdown_item_1line, autoCompleteResponse.autocomplete);
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if(autoCompleteResponse!=null)
                    {
                        spinnerRL.setVisibility(View.INVISIBLE);
                        societyName.setAdapter(adapter);
                    }
                }
            });
        }
    };



}
