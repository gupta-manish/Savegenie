package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.FinalBundleStrings;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.fragments.SaveListDialogFragment;
import in.savegenie.savegenie.internetcommunication.CheckListAsyncTask;
import in.savegenie.savegenie.internetcommunication.CompleteOrderAsyncTask;
import in.savegenie.savegenie.responses.CompleteOrderResponse;
import in.savegenie.savegenie.responses.ConfirmOrderResponse;

/**
 * Created by manish on 10/4/15.
 */
public class CompleteOrderActivity extends Activity implements SaveListDialogFragment.SaveListDialogFragmentListener
{
    CheckBox acceptCheckBox;
    Button completeOrderButton;
    CompleteOrderAsyncTask coat;
    CheckListAsyncTask checkListAsyncTask;
    Bundle finalBundle;
    ConfirmOrderResponse cor;
    RelativeLayout formLayout,completeOrderLayout;
    CompleteOrderResponse completeOrderResponse;
    LinearLayout table1, table2;
    Handler mHandler;
    TextView orderId, email, loggedtime, ordercompletetime, totaldiscount, storename,
            txnid, priceatmrp, storeprice, priceafterdeal, deliverycharges, discount,
            netamount, dateAndTime, address;


    String mode, phone, deliverydate, starttime, endtime;
    Long loggedTime;
    String loggedInTimeString;
    Long completeTime;
    EditText activeListName;
    String listNameString;
    String checkListResponse;
    Button saveListButton,contnueWithoutSaving;
    Long orderTime;
    TextView errorMessage;
    SaveListDialogFragment saveListDialogFragment;
    RelativeLayout dataLayout,spinnerRL;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_order);
        SharedPreferences prefs = getSharedPreferences(SharedPrefString.SHARED_PREFERENCE_KEY,
                Context.MODE_PRIVATE);

        loggedTime = prefs.getLong(SharedPrefString.LOGGED_IN_TIME_STAMP, 0);
        loggedInTimeString = prefs.getString(SharedPrefString.LOGGED_IN_TIME, "0");

        spinnerRL = (RelativeLayout)findViewById(R.id.spinnerRelativeLayout);
        spinnerRL.setVisibility(View.INVISIBLE);
        acceptCheckBox = (CheckBox) findViewById(R.id.acceptCheckBox);
        completeOrderButton = (Button) findViewById(R.id.completeOrderButton);
        table1 = (LinearLayout) findViewById(R.id.table1);
        table2 = (LinearLayout) findViewById(R.id.table2);

        mHandler = new Handler();
        orderId = (TextView) findViewById(R.id.orderId);
        loggedtime = (TextView) findViewById(R.id.loggedtime);
        ordercompletetime = (TextView) findViewById(R.id.ordercompletetime);
        totaldiscount = (TextView) findViewById(R.id.totaldiscount);
        storename = (TextView) findViewById(R.id.storename);
        txnid = (TextView) findViewById(R.id.txnid);
        priceatmrp = (TextView) findViewById(R.id.priceatmrp);
        storeprice = (TextView) findViewById(R.id.storeprice);
        priceafterdeal = (TextView) findViewById(R.id.priceafterdeal);
        deliverycharges = (TextView) findViewById(R.id.deliverycharges);
        discount = (TextView) findViewById(R.id.discount);
        netamount = (TextView) findViewById(R.id.netamount);
        dateAndTime = (TextView) findViewById(R.id.dateAndTime);
        address = (TextView) findViewById(R.id.address);
        completeOrderLayout = (RelativeLayout)findViewById(R.id.completeOrderLayout);
        completeOrderButton.setEnabled(false);
        saveListDialogFragment = new SaveListDialogFragment();
        dataLayout = (RelativeLayout)findViewById(R.id.dataLayout);
        dataLayout.setVisibility(View.INVISIBLE);

        acceptCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b)
            {
                completeOrderButton.setEnabled(b);
            }
        });


        finalBundle = getIntent().getBundleExtra(FinalBundleStrings.FINAL_BUNDLE);
        cor = finalBundle.getParcelable(FinalBundleStrings.CONFIRM_ORDER_RESPONSE);


        completeOrderButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spinnerRL.setVisibility(View.VISIBLE);
                new Thread(checkList).start();
            }
        });



    }

    Runnable checkList = new Runnable()
    {
        @Override
        public void run()
        {
            checkListResponse = "0";
            checkListAsyncTask = new CheckListAsyncTask();
            checkListAsyncTask.execute();

            try
            {
                checkListResponse = checkListAsyncTask.get();
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
                    if(checkListResponse.contains("1"))
                    {
                        completeTime = System.currentTimeMillis() / 1000;
                        orderTime = (completeTime - loggedTime) / 60;
                        new Thread(completeOrderRunnable).start();
                    }
                    else
                    {
                        Bundle b= new Bundle();
                        b.putString(IntentString.LIST_NAME_STRING,listNameString);
                        saveListDialogFragment.setArguments(b);
                        spinnerRL.setVisibility(View.INVISIBLE);
                        saveListDialogFragment.show(getFragmentManager(), "SaveListDialogFragment");
                    }
                }
            });



        }
    };

    Runnable completeOrderRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            coat = new CompleteOrderAsyncTask();
            coat.execute(cor.key, cor.txnid, cor.amount, cor.firstname, cor.email, cor.phone, cor.productinfo, cor.lastname, cor.address1,
                    cor.address2, "", cor.city, cor.state, cor.country, cor.zipcode, cor.udf1, cor.udf2, cor.udf5, "", cor.mode, cor.discount,
                    cor.delcost, orderTime + "");
            try
            {
                completeOrderResponse = coat.get();
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
                    Long completeTime = System.currentTimeMillis() / 1000;
                    Long orderTime = completeTime - loggedTime;
                    mode = completeOrderResponse.mode;
                    phone = completeOrderResponse.phone;
                    deliverydate = completeOrderResponse.deliverydate;
                    starttime = completeOrderResponse.starttime;
                    endtime = completeOrderResponse.endtime;
                    orderId.setText(completeOrderResponse.orderId);
                    loggedtime.setText(loggedInTimeString);
                    ordercompletetime.setText(orderTime + " Min");
                    totaldiscount.setText(completeOrderResponse.totaldiscount);
                    storename.setText(completeOrderResponse.storename);
                    txnid.setText(completeOrderResponse.txnid);
                    priceatmrp.setText(completeOrderResponse.priceatmrp);
                    storeprice.setText(completeOrderResponse.storeprice);
                    priceafterdeal.setText(completeOrderResponse.priceafterdeal);
                    deliverycharges.setText(completeOrderResponse.deliverycharges);
                    discount.setText(completeOrderResponse.discount);
                    netamount.setText(completeOrderResponse.netamount);
                    dateAndTime.setText(completeOrderResponse.deliverydate + "," + completeOrderResponse.starttime + "-"
                            + completeOrderResponse.endtime);
                    address.setText(completeOrderResponse.address + "," + phone);
                    dataLayout.setVisibility(View.VISIBLE);

                }
            });

        }
    };

    @Override
    public void onSaveListDialogEnd()
    {
        completeTime = System.currentTimeMillis() / 1000;
        orderTime = (completeTime - loggedTime) / 60;
        spinnerRL.setVisibility(View.VISIBLE);
        new Thread(completeOrderRunnable).start();
    }
}
