package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.OrderDetailAdapter;
import in.savegenie.savegenie.backgroundclasses.FinalBundleStrings;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.ShippingAddress;
import in.savegenie.savegenie.backgroundclasses.Slot;
import in.savegenie.savegenie.internetcommunication.ConfirmOrderAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetOrderDetailsAsyncTask;
import in.savegenie.savegenie.internetcommunication.SendCouponCodeAsyncTask;
import in.savegenie.savegenie.responses.ConfirmOrderResponse;
import in.savegenie.savegenie.responses.CouponCodeResponse;
import in.savegenie.savegenie.responses.OrderDetailResponse;

/**
 * Created by manish on 9/4/15.
 */
public class OrderDetailActivity extends Activity
{

    String title = "Order Details";

    OrderDetailAdapter adapter;
    RelativeLayout spinnerRL;
    RelativeLayout noProduct,couponCodeLayout;
    GetOrderDetailsAsyncTask godat;
    OrderDetailResponse orderDetailResponse;
    ListView itemListView;
    Context context;
    Handler mHandler;
    Bundle finalBundle;
    String pickDel;
    TextView priceAtMRP, storePrice, priceAfterDeal, dealDiscount,
            deliveryCharges, additionalCashback, netPrice, storeName, couponCodeMessage;
    double netprice;
    Button couponCodeButton;
    SendCouponCodeAsyncTask sccat;
    CouponCodeResponse couponCodeResponse;
    EditText couponEditText;
    Button nextButton,applyButton;
    String couponCode;
    ConfirmOrderAsyncTask coat;
    ConfirmOrderResponse confirmOrderResponse;
    ShippingAddress shippingAddress;
    Slot slot;
    String date;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_order_detail);
        context = this;
        finalBundle = getIntent().getBundleExtra(FinalBundleStrings.FINAL_BUNDLE);
        shippingAddress = finalBundle.getParcelable(FinalBundleStrings.SHIPPING_ADDRESS);
        pickDel = finalBundle.getString(FinalBundleStrings.PICK_DEL);
        slot = finalBundle.getParcelable(((pickDel.equals("pickup"))
                ? FinalBundleStrings.PICKUP_SLOT
                : FinalBundleStrings.DELIVERY_SLOT));
        date = finalBundle.getString(((pickDel.equals("pickup"))
                ? FinalBundleStrings.PICKUP_DATE
                : FinalBundleStrings.DELIVERY_DATE));
        mHandler = new Handler();
        itemListView = (ListView) findViewById(R.id.quickList);
        spinnerRL = (RelativeLayout) findViewById(R.id.spinnerRelativeLayout1);
        couponCodeLayout = (RelativeLayout) findViewById(R.id.couponCodeLayout);
        noProduct = (RelativeLayout) findViewById(R.id.noProduct);
        priceAtMRP = (TextView) findViewById(R.id.priceAtMRP);
        storePrice = (TextView) findViewById(R.id.storePrice);
        priceAfterDeal = (TextView) findViewById(R.id.priceAfterDeal);
        dealDiscount = (TextView) findViewById(R.id.dealDiscount);
        deliveryCharges = (TextView) findViewById(R.id.deliveryCharges);
        additionalCashback = (TextView) findViewById(R.id.additionalCashback);
        couponCodeButton = (Button) findViewById(R.id.couponButton);
        applyButton = (Button) findViewById(R.id.applyButton);
        nextButton = (Button) findViewById(R.id.nextButton);
        netPrice = (TextView) findViewById(R.id.netPrice);
        storeName = (TextView) findViewById(R.id.storeName);
        couponEditText = (EditText) findViewById(R.id.couponEditText);
        couponCodeMessage = (TextView) findViewById(R.id.couponCodeMessage);
        spinnerRL.setVisibility(View.VISIBLE);
        new Thread(setUp).start();

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeAsUpIndicator(R.drawable.ic_action_back);
        getActionBar().setHomeButtonEnabled(true);

        couponCodeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(couponCodeLayout.getVisibility() == View.INVISIBLE)
                {
                    couponCodeLayout.setVisibility(View.VISIBLE);
                    couponCodeButton.setText("Cancel");
                }
                else
                {
                    couponCodeLayout.setVisibility(View.INVISIBLE);
                    couponCodeButton.setText("Coupon Code");
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spinnerRL.setVisibility(View.VISIBLE);
                (new Thread(new ConfirmOrderThread())).start();
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                couponCode = couponEditText.getText().toString();
                if(couponCode.equals(""))
                {
                    Toast.makeText(context.getApplicationContext(),
                            "Empty Code", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    spinnerRL.setVisibility(View.VISIBLE);
                    new Thread(new SendCouponCodeThread()).start();
                }
            }
        });
    }

    Runnable setUp = new Runnable()
    {
        @Override
        public void run()
        {
            godat = new GetOrderDetailsAsyncTask(finalBundle);

            godat.execute();

            try
            {
                orderDetailResponse = godat.get();
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

            adapter = new OrderDetailAdapter(context,
                    getItemIds(orderDetailResponse.orderItemList.size()),
                    orderDetailResponse.orderItemList);

            double priceafterdeal = Double.parseDouble(orderDetailResponse.order.priceafterdeal);
            double mos = Double.parseDouble(orderDetailResponse.store.mos);
            double additionalcashback = Double.parseDouble(orderDetailResponse.order.additionalcashback);
            double deliverycost = Double.parseDouble(orderDetailResponse.store.deliverycost);

            if (pickDel.equals("delivery"))
            {
                if (priceafterdeal > mos)
                {
                    netprice = priceafterdeal - additionalcashback;
                }
                else
                {
                    netprice = priceafterdeal + deliverycost - additionalcashback;
                }
            }
            else
            {
                netprice = priceafterdeal - additionalcashback;
            }

            orderDetailResponse.order.netprice = netprice + "";

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {

                    if (orderDetailResponse.orderItemList.size() != 0)
                    {
                        spinnerRL.setVisibility(View.INVISIBLE);
                        itemListView.setAdapter(adapter);
                        priceAtMRP.setText("Rs " + orderDetailResponse.order.priceatmrp);
                        storePrice.setText("Rs "+orderDetailResponse.order.storeprice);
                        priceAfterDeal.setText("Rs "+orderDetailResponse.order.priceafterdeal);
                        dealDiscount.setText("Rs "+orderDetailResponse.order.dealdiscount);
                        deliveryCharges.setText("Rs "+orderDetailResponse.store.deliverycost);
                        additionalCashback.setText("Rs "+orderDetailResponse.order.additionalcashback);
                        netPrice.setText("Rs "+orderDetailResponse.order.netprice);
                        storeName.setText(orderDetailResponse.store.name);
                    }

                }
            });
        }
    };

    private Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    private class SendCouponCodeThread implements Runnable
    {
        public void run()
        {
            sccat = new SendCouponCodeAsyncTask();
            sccat.execute(couponCode, orderDetailResponse.order.id,
                    orderDetailResponse.order.priceafterdeal, orderDetailResponse.store.id);
            try
            {
                couponCodeResponse = sccat.get();
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
                    couponCodeMessage.setText(couponCodeResponse.message);
                    couponCodeMessage.setVisibility(View.VISIBLE);
                    if (!((couponCodeResponse.couponprice == null)))
                    {
                        orderDetailResponse.order.netprice = couponCodeResponse.netprice;
                        orderDetailResponse.order.additionalcashback = couponCodeResponse.couponprice;
                        additionalCashback.setText(orderDetailResponse.order.additionalcashback);
                        netPrice.setText(orderDetailResponse.order.netprice);

                    }
                }
            });

        }
    }

    private class ConfirmOrderThread implements Runnable
    {
        @Override
        public void run()
        {
            coat = new ConfirmOrderAsyncTask();
            coat.execute((pickDel.equals("pickup") ? "" : shippingAddress.id), pickDel.equals("pickup") ? "" : shippingAddress.address, pickDel.equals("pickup") ? "" : shippingAddress.name,
                    pickDel.equals("pickup") ? "" : shippingAddress.city, pickDel.equals("pickup") ? "" : shippingAddress.area, pickDel.equals("pickup") ? "" : shippingAddress.mobileno,
                    pickDel, slot.id, date, pickDel, orderDetailResponse.store.id,
                    orderDetailResponse.order.dealdiscount, couponCode, orderDetailResponse.order.netprice,
                    orderDetailResponse.order.id, "0");
            try
            {
                confirmOrderResponse = coat.get();
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
                    finalBundle.putParcelable(FinalBundleStrings.CONFIRM_ORDER_RESPONSE, confirmOrderResponse);
                    finalBundle.putParcelable(FinalBundleStrings.ORDER_DATA, orderDetailResponse.order);
                    finalBundle.putParcelable(FinalBundleStrings.ORDER_STORE_DATA, orderDetailResponse.store);
                    finalBundle.putParcelableArrayList(FinalBundleStrings.ORDER_ITEM_LIST, orderDetailResponse.orderItemList);
                    spinnerRL.setVisibility(View.INVISIBLE);
                    Intent coa = new Intent(IntentString.COMPLETE_ORDER_ACTIVITY);
                    coa.putExtra(FinalBundleStrings.FINAL_BUNDLE,finalBundle);
                    startActivity(coa);

                }
            });
        }
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
