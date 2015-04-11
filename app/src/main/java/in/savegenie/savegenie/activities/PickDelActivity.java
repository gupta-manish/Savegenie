package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.FinalBundleStrings;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.backgroundclasses.ShippingAddress;
import in.savegenie.savegenie.backgroundclasses.Slot;
import in.savegenie.savegenie.fragments.EnterNewAddressDialogFragment;
import in.savegenie.savegenie.fragments.SelectAddressDialogFragment;
import in.savegenie.savegenie.internetcommunication.GetPickDelDayDateAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetShippingAddressListAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetStoreSlotAsyncTask;
import in.savegenie.savegenie.responses.PickDelDayDateResponse;
import in.savegenie.savegenie.responses.ShippingAddressResponse;
import in.savegenie.savegenie.responses.StoreSlotListResponse;

/**
 * Created by manish on 9/4/15.
 */
public class PickDelActivity extends Activity implements SelectAddressDialogFragment.SelectAddressDialogFragmentListener
{
    Context context;
    Handler mHandler;
    RelativeLayout spinnerRL, noProduct, pickupLayout, deliveryLayout, pickupInfoLayout, deliveryInfoLayout;
    ViewGroup pickupDayGroup, pickupDateGroup, deliveryDateGroup, deliveryDayGroup;
    Button pickup, delivery, pickDelNext;
    GetPickDelDayDateAsyncTask gpdddatPickup, gpdddatDelivery;
    GetShippingAddressListAsyncTask gsalat;
    GetStoreSlotAsyncTask gssat;
    StoreSlotListResponse pickupSlotListResponse, deliverySlotListResponse;
    ShippingAddressResponse shippingAddressResponse;
    Resources res;
    Slot deliverySlot, pickupSlot;
    Bundle finalBundle;
    String userStoreId, userStoreAddress, userStoreName, pickupDate, deliveryDate;
    PickDelDayDateResponse pickupDayResponse, deliveryDayResponse;
    String pickDel;
    ArrayList<String> pickupSlotList, deliverySlotList, shippingAddressList;
    ArrayAdapter<String> pickupArrayAdapter,deliveryArrayAdapter;
    Spinner pickupStoreSlotDropdown, deliveryStoreSlotDropdown;
    TextView pickupStoreName, pickupStoreAddress;
    Boolean deliverySlotFirstSelect, pickSlotFirstSelect;
    ShippingAddress shippingAddress;
    Button homeAddress;
    TextView selectedAddress;
    SharedPreferences prefs;
    public static String title = "Pickup or Delivery";
    int refresh;
    private DialogFragment selectAddressDialogFragment,enterNewAddressDialogFragment;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pick_del);
        res = getResources();
        prefs = getSharedPreferences(SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        context = this;
        deliverySlotFirstSelect = true;
        pickSlotFirstSelect = true;
        finalBundle = getIntent().getBundleExtra(FinalBundleStrings.FINAL_BUNDLE);
        userStoreId = finalBundle.getString(FinalBundleStrings.USER_SORE_ID);
        userStoreName = finalBundle.getString(FinalBundleStrings.STORE_NAME);
        userStoreAddress = finalBundle.getString(FinalBundleStrings.STORE_ADDRESS);
        pickupSlot = null;
        deliverySlot = null;
        pickupDate = null;
        deliveryDate = null;
        mHandler = new Handler();
        shippingAddress = null;
        pickupSlotList = new ArrayList<String>();
        deliverySlotList = new ArrayList<String>();
        spinnerRL = (RelativeLayout) findViewById(R.id.spinnerRelativeLayout);
        noProduct = (RelativeLayout) findViewById(R.id.noProduct);
        pickupLayout = (RelativeLayout) findViewById(R.id.pickupLayout);
        deliveryLayout = (RelativeLayout) findViewById(R.id.deliveryLayout);
        pickupInfoLayout = (RelativeLayout) findViewById(R.id.pickupInfoLayout);
        deliveryInfoLayout = (RelativeLayout) findViewById(R.id.deliveryInfoLayout);
        spinnerRL.setAlpha((float) 0.5);
        pickupDayGroup = (ViewGroup) findViewById(R.id.pickupDays);
        pickupDateGroup = (ViewGroup) findViewById(R.id.pickupDates);
        deliveryDayGroup = (ViewGroup) findViewById(R.id.deliveryDays);
        deliveryDateGroup = (ViewGroup) findViewById(R.id.deliveryDates);
        pickup = (Button) findViewById(R.id.pickup);
        delivery = (Button) findViewById(R.id.delivery);
        pickupStoreSlotDropdown = (Spinner) findViewById(R.id.pickupStoreSlotDropdown);
        deliveryStoreSlotDropdown = (Spinner) findViewById(R.id.deliveryStoreSlotDropdown);
        pickupStoreName = (TextView) findViewById(R.id.pickupStoreName);
        pickupStoreAddress = (TextView) findViewById(R.id.pickupStoreAddress);
        pickupStoreAddress.setText(userStoreAddress);
        pickupStoreName.setText(userStoreName);
        homeAddress = (Button) findViewById(R.id.homeAddress);
        selectedAddress = (TextView) findViewById(R.id.selectedAddress);
        pickDelNext = (Button) findViewById(R.id.pickDelNext);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getActionBar().setHomeButtonEnabled(true);

        pickupStoreSlotDropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {


                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                               long arg3)
                    {
                        if (pickSlotFirstSelect)
                        {
                            pickSlotFirstSelect = false;
                            return;
                        }
                        pickupSlot = pickupSlotListResponse.slotList.get(pos - 1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0)
                    {
                        // TODO Auto-generated method stub

                    }

                }
        );


        deliveryStoreSlotDropdown.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener()
                {


                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
                                               long arg3)
                    {
                        if (deliverySlotFirstSelect)
                        {
                            deliverySlotFirstSelect = false;
                            return;
                        }
                        deliverySlot = deliverySlotListResponse.slotList.get(pos - 1);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0)
                    {
                        // TODO Auto-generated method stub

                    }

                }
        );

        homeAddress.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                selectAddressDialogFragment = new SelectAddressDialogFragment();
                selectAddressDialogFragment.show(getFragmentManager(),"SelectAddressDialogFragment");
            }
        });

        pickDelNext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finalBundle.putString(FinalBundleStrings.PICK_DEL, pickDel);
                if (pickDel.equals("delivery"))
                {
                    if(deliveryDate == null || deliverySlot == null || shippingAddress == null )
                    {
                        Toast.makeText(getApplicationContext(),
                                "Please select everything. Something is missing", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    finalBundle.putString(FinalBundleStrings.DELIVERY_DATE, deliveryDate);
                    finalBundle.putParcelable(FinalBundleStrings.DELIVERY_SLOT, deliverySlot);
                    finalBundle.putParcelable(FinalBundleStrings.SHIPPING_ADDRESS, shippingAddress);
                    startOrderDetailActivity();
                }
                else if (pickDel.equals("pickup"))
                {
                    if(pickupDate == null || pickupSlot == null)
                    {
                        Toast.makeText(getApplicationContext(),
                                "Please select everything. Something is missing", Toast.LENGTH_LONG)
                                .show();
                        return;
                    }
                    finalBundle.putString(FinalBundleStrings.PICKUP_DATE, pickupDate);
                    finalBundle.putParcelable(FinalBundleStrings.PICKUP_SLOT, pickupSlot);
                    startOrderDetailActivity();
                }

            }
        });

        spinnerRL.setVisibility(View.VISIBLE);
        setupThread.start();
    }



    Runnable getPickupTimeSlotList = new Runnable()
    {
        @Override
        public void run()
        {
            gssat = new GetStoreSlotAsyncTask();
            gssat.execute(userStoreId, "pickup", pickupDate);
            try
            {
                pickupSlotListResponse = gssat.get();
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
            pickupSlotList.clear();
            if (pickupSlotListResponse.slotList.size() > 0)
            {
                pickupSlotList.add("Choose your slot");
                for (int i = 0; i < pickupSlotListResponse.slotList.size(); i++)
                {
                    pickupSlotList.add(pickupSlotListResponse.slotList.get(i).time);
                }
                pickupArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, pickupSlotList);
                pickupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (pickupSlotListResponse.slotList.size() > 0)
                    {
                        pickSlotFirstSelect = true;
                        pickupStoreSlotDropdown.setVisibility(View.VISIBLE);
                        pickupStoreSlotDropdown.setAdapter(pickupArrayAdapter);
                    }
                    else
                    {
                        pickupStoreSlotDropdown.setVisibility(View.GONE);
                    }
                }
            });


        }
    };

    Runnable getDeliveryTimeSlotList = new Runnable()
    {
        @Override
        public void run()
        {
            gssat = new GetStoreSlotAsyncTask();
            gssat.execute(userStoreId, "delivery", deliveryDate);
            try
            {
                deliverySlotListResponse = gssat.get();
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
            deliverySlotList.clear();
            if (deliverySlotListResponse.slotList.size() > 0)
            {
                deliverySlotList.add("Choose your slot");
                for (int i = 0; i < deliverySlotListResponse.slotList.size(); i++)
                {
                    deliverySlotList.add(deliverySlotListResponse.slotList.get(i).time);
                }
                deliveryArrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, deliverySlotList);
                deliveryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (deliverySlotListResponse.slotList.size() > 0)
                    {
                        deliverySlotFirstSelect = true;
                        deliveryStoreSlotDropdown.setVisibility(View.VISIBLE);
                        deliveryStoreSlotDropdown.setAdapter(deliveryArrayAdapter);
                    }
                    else
                    {
                        deliveryStoreSlotDropdown.setVisibility(View.GONE);
                    }
                }
            });


        }
    };

    Thread setupThread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            gpdddatPickup = new GetPickDelDayDateAsyncTask();
            gpdddatPickup.execute(userStoreId, "pickup", "confirm");
            try
            {
                pickupDayResponse = gpdddatPickup.get();
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

            gpdddatDelivery = new GetPickDelDayDateAsyncTask();
            gpdddatDelivery.execute(userStoreId, "delivery", "confirm");
            try
            {
                deliveryDayResponse = gpdddatDelivery.get();
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
                    setUpButtons();
                    delivery.performClick();
                }
            });

        }
    });


    private Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    @Override
    public void onAddressClick(ShippingAddress address)
    {

    }

    @Override
    public void onEnterNewAddressClick()
    {
        enterNewAddressDialogFragment = new EnterNewAddressDialogFragment();
        enterNewAddressDialogFragment.show(getFragmentManager(),"EnterNewAddressDialogFragment");
    }

    public void setAddress(ShippingAddress address)
    {
        shippingAddress = address;
        selectedAddress.setText(shippingAddress.address);
        selectedAddress.setVisibility(View.VISIBLE);

    }

    @Override
    public void onPause()
    {
        super.onPause();
        refresh = 0;
    }

    private void setUpButtons()
    {
        pickup.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickDel = "pickup";
                delivery.setBackgroundResource(R.drawable.custom_pick_del_unfocused);
                pickup.setBackgroundResource(R.drawable.custom_pick_del_focused);
                pickupLayout.setVisibility(View.VISIBLE);
                deliveryLayout.setVisibility(View.INVISIBLE);

                for (int i = 0; i < pickupDateGroup.getChildCount(); i++)
                {
                    final Button dateButton = (Button) pickupDateGroup.getChildAt(i);
                    dateButton.setText(pickupDayResponse.dayList.get(i).date);
                    if ((pickupDayResponse.dayList.get(i).available).contains("0"))
                    {
                        dateButton.setPaintFlags(dateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        dateButton.setEnabled(false);
                    }
                    else
                    {
                        dateButton.setEnabled(true);
                        dateButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                spinnerRL.setVisibility(View.VISIBLE);
                                Calendar cal = Calendar.getInstance();
                                ;
                                pickupDate = cal.get(Calendar.YEAR) + "-"
                                        + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
                                        + ((Button) view).getText();
                                pickupStoreSlotDropdown.setVisibility(View.INVISIBLE);
                                new Thread(getPickupTimeSlotList).start();
                            }
                        });
                    }
                }

            }
        });
        delivery.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                pickDel = "delivery";
                pickup.setBackgroundResource(R.drawable.custom_pick_del_unfocused);
                delivery.setBackgroundResource(R.drawable.custom_pick_del_focused);
                deliveryLayout.setVisibility(View.VISIBLE);
                pickupLayout.setVisibility(View.INVISIBLE);
                for (int i = 0; i < deliveryDateGroup.getChildCount(); i++)
                {
                    Button dateButton = (Button) deliveryDateGroup.getChildAt(i);
                    dateButton.setText(deliveryDayResponse.dayList.get(i).date);
                    if ((deliveryDayResponse.dayList.get(i).available).contains("0"))
                    {
                        dateButton.setPaintFlags(dateButton.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        dateButton.setEnabled(false);
                    }
                    else
                    {
                        dateButton.setEnabled(true);
                        dateButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {

                                spinnerRL.setVisibility(View.VISIBLE);
                                Calendar cal = Calendar.getInstance();
                                deliveryDate = cal.get(Calendar.YEAR) + "-"
                                        + String.format("%02d", cal.get(Calendar.MONTH) + 1) + "-"
                                        + ((Button) view).getText();
                                Log.d("deliveryDate", deliveryDate);
                                deliveryStoreSlotDropdown.setVisibility(View.INVISIBLE);
                                new Thread(getDeliveryTimeSlotList).start();
                            }
                        });
                    }
                }
            }
        });
    }

    public void startOrderDetailActivity()
    {
        Intent oda = new Intent(IntentString.ORDER_DETAIL_ACTIVITY);
        oda.putExtra(FinalBundleStrings.FINAL_BUNDLE,finalBundle);
        startActivity(oda);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
