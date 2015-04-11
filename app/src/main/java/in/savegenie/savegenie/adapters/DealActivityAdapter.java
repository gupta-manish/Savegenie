package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.BundleDeal;
import in.savegenie.savegenie.backgroundclasses.ProductDeal;
import in.savegenie.savegenie.internetcommunication.AddRemoveItemToListAsyncTask;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 5/4/15.
 */
public class DealActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    ArrayList<BundleDeal> bundleDealList;
    ArrayList<ProductDeal> productDealList;
    private int[] quantity;
    AddRemoveItemToListAsyncTask aritlat;
    String response;
    BasketCountChangeListener basketCountChangeListener;
    InternetURL urlHandler = new InternetURL();
    int layoutNum;


    public DealActivityAdapter(Context context, Integer[] itemId,
                               ArrayList<BundleDeal> bundleDealList, ArrayList<ProductDeal> productDealList)
    {
        super(context, R.layout.activity_item_deal, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.productDealList = productDealList;
        this.bundleDealList = bundleDealList;
        quantity = new int[productDealList.size()+bundleDealList.size()];
        if (bundleDealList.size() != 0)
        {
            layoutNum = 0;
        }
        else
        {
            layoutNum = 1;
        }
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        basketCountChangeListener = (BasketCountChangeListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            if (getItemViewType(position)==0)
            {
                convertView = layInf.inflate(R.layout.list_row_bundle_deal, parent, false);
                LinearLayout listLayout = (LinearLayout) convertView.findViewById(R.id.listLayout);
                for (int i = 0; i < bundleDealList.get(position).bundleItemList.size(); i++)
                {
                    LinearLayout linearLayout = new LinearLayout(context.getApplicationContext());
                    linearLayout.setGravity(Gravity.CENTER);
                    LayoutInflater layInf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View listElement = layInf.inflate(R.layout.list_horizontal_element_bundle_deal,linearLayout);
                    TextView productName = (TextView)  listElement.findViewById(R.id.productName);
                    productName.setText(bundleDealList.get(position).bundleItemList.get(i).storeSku.code);
                    TextView price = (TextView) listElement.findViewById(R.id.price);
                    TextView quantity = (TextView) listElement.findViewById(R.id.fixedquantity);

                    price.setText("Rs." + bundleDealList.get(position).bundleItemList.get(i).storeSku.mrp);
                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    quantity.setText("Qty: " + bundleDealList.get(position).bundleItemList.get(i).qty);

                    ImageView iv = (ImageView) listElement.findViewById(R.id.image);
                    String url = InternetURL.ALL_PRODUCT_IMAGE
                            + bundleDealList.get(position).bundleItemList.get(i).storeSku.productSku.image;
                    Picasso.with(context).load(url).into(iv);

                    listLayout.addView(linearLayout);

                }

                final Button addToBasket = (Button) convertView
                        .findViewById(R.id.addToBasket);
                final Button incQuantity = (Button) convertView
                        .findViewById(R.id.incQuantity);
                final Button decQuantity = (Button) convertView
                        .findViewById(R.id.decQuantity);
                TextView priceString = (TextView)convertView.findViewById(R.id.priceString);

                priceString.setText("Rs:"+bundleDealList.get(position).bundleGet.value);

                final RelativeLayout quantityLayout = (RelativeLayout) convertView
                        .findViewById(R.id.quantityLayout);
                final TextView quantityView = (TextView) convertView
                        .findViewById(R.id.quantity);

                if (quantity[position] != 0) {
                    addToBasket.setVisibility(View.INVISIBLE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    quantityView.setText(quantity[position] + "");
                } else {
                    quantityLayout.setVisibility(View.INVISIBLE);
                    addToBasket.setVisibility(View.VISIBLE);
                }

                addToBasket.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]++;
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(getProductIdString(position),1 + "",getQtyString(position),quantity[position]+"");
                        addToBasket.setVisibility(View.INVISIBLE);
                        quantityLayout.setVisibility(View.VISIBLE);
                        quantityView.setText(quantity[position] + "");
                        basketCountChangeListener.onBasketCountChange();
                    }
                });

                incQuantity.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]++;
                        quantityView.setText(quantity[position] + "");
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(getProductIdString(position),1 + "",getQtyString(position),quantity[position]+"");

                    }
                });

                decQuantity.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]--;
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(getProductIdString(position),0+ "",getQtyString(position),quantity[position]+"");
                        if (quantity[position] == 0) {
                            quantityLayout.setVisibility(View.INVISIBLE);
                            addToBasket.setVisibility(View.VISIBLE);
                            basketCountChangeListener.onBasketCountChange();
                        } else {
                            quantityView.setText(quantity[position] + "");
                        }


                    }
                });


            }
            else
            {

                convertView = layInf.inflate(R.layout.list_row_product_deal, parent, false);
                final int pos = position - bundleDealList.size();
                TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
                itemName.setText(productDealList.get(pos).storeSku.code);

                TextView itemMRP = (TextView) convertView.findViewById(R.id.itemMRP);
                itemMRP.setText("Rs." + productDealList.get(pos).storeSku.mrp);
                itemMRP.setPaintFlags(itemMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                TextView itemPrice = (TextView) convertView
                        .findViewById(R.id.itemPrice);
                itemPrice.setText("/Rs." + productDealList.get(pos).storeSku.price);


                ImageView iv = (ImageView) convertView.findViewById(R.id.itemIcon);
                String url = urlHandler.ALL_PRODUCT_IMAGE
                        + productDealList.get(pos).storeSku.productSku.image;

                Picasso.with(context).load(url).into(iv);
                final Button addToBasket = (Button) convertView
                        .findViewById(R.id.addToBasket);
                final Button incQuantity = (Button) convertView
                        .findViewById(R.id.incQuantity);
                final Button decQuantity = (Button) convertView
                        .findViewById(R.id.decQuantity);

                final RelativeLayout quantityLayout = (RelativeLayout) convertView
                        .findViewById(R.id.quantityLayout);
                final TextView quantityView = (TextView) convertView
                        .findViewById(R.id.quantity);

                if (quantity[position] != 0) {
                    addToBasket.setVisibility(View.INVISIBLE);
                    quantityLayout.setVisibility(View.VISIBLE);
                    quantityView.setText(quantity[position] + "");
                } else {
                    quantityLayout.setVisibility(View.INVISIBLE);
                    addToBasket.setVisibility(View.VISIBLE);
                }

                addToBasket.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]++;
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(productDealList.get(pos).storeSku.id,1 + "",1+"");
                        addToBasket.setVisibility(View.INVISIBLE);
                        quantityLayout.setVisibility(View.VISIBLE);
                        quantityView.setText(quantity[position] + "");
                        basketCountChangeListener.onBasketCountChange();
                    }
                });

                incQuantity.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]++;
                        quantityView.setText(quantity[position] + "");
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(productDealList.get(pos).storeSku.id,1 + "",1+"");

                    }
                });

                decQuantity.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        quantity[position]--;
                        aritlat = new AddRemoveItemToListAsyncTask();
                        aritlat.execute(productDealList.get(pos).storeSku.id,0+ "",1+"");
                        if (quantity[position] == 0) {
                            quantityLayout.setVisibility(View.INVISIBLE);
                            addToBasket.setVisibility(View.VISIBLE);
                            basketCountChangeListener.onBasketCountChange();
                        } else {
                            quantityView.setText(quantity[position] + "");
                        }

                    }
                });
            }
        }





        return convertView;


    }

    String getProductIdString(int position)
    {
        String ret = bundleDealList.get(position).bundleItemList.get(0).storeSku.product_skus_id;
        for(int i=1;i<bundleDealList.get(position).bundleItemList.size();i++)
        {
            ret = ret + "-" + bundleDealList.get(position).bundleItemList.get(1).storeSku.product_skus_id;
        }
        return ret;
    }

    String getQtyString(int position)
    {
        String ret = bundleDealList.get(position).bundleItemList.get(0).qty;
        for(int i=1;i<bundleDealList.get(position).bundleItemList.size();i++)
        {
            ret = ret + "-" + bundleDealList.get(position).bundleItemList.get(1).qty;
        }
        return ret;
    }

    @Override
    public int getViewTypeCount()
    {
        return 2;
    }

    @Override
    public int getItemViewType(int position)
    {
        if (position < bundleDealList.size())
        {
            return 0;
        }
        else
        {
            return 1;
        }
    }

    public interface BasketCountChangeListener
    {
        void onBasketCountChange();
    }

}

