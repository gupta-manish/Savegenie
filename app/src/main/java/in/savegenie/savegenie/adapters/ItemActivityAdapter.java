package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.Product;
import in.savegenie.savegenie.internetcommunication.AddRemoveItemToListAsyncTask;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 4/4/15.
 */
public class ItemActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<Product> productList;
    private int[] quantity;
    private int difference[];
    AddRemoveItemToListAsyncTask aritlat;
    ItemActivityAdapterListener itemActivityAdapterListener;
    String response;
    InternetURL urlHandler = new InternetURL();;
    public ItemActivityAdapter(Context context, Integer[] itemId,
                               ArrayList<Product> productList) {
        super(context, R.layout.activity_item, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        quantity = new int[productList.size()];
        difference = new int[productList.size()];
        this.productList = productList;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        itemActivityAdapterListener = (ItemActivityAdapterListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_item, parent, false);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(productList.get(position).params[Product.P_CODE]);

        TextView itemMRP = (TextView) convertView.findViewById(R.id.itemMRP);
        itemMRP.setText("Rs."+productList.get(position).params[Product.P_MRP]);
        itemMRP.setPaintFlags(itemMRP.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        TextView itemAvgPrice = (TextView) convertView
                .findViewById(R.id.itemAvgPrice);
        itemAvgPrice.setText("Avg Price:Rs"
                + productList.get(position).params[Product.AVG_PACK_PRICE]+"/"
                + productList.get(position).params[Product.DENO_SZ]
                + productList.get(position).params[Product.DENO_UNIT]);
        TextView itemPrice = (TextView) convertView
                .findViewById(R.id.itemPrice);
        itemPrice.setText("/Rs."+productList.get(position).params[Product.ACTUAL_PRICE]);

        ImageView iv = (ImageView) convertView.findViewById(R.id.itemIcon);
        String url = urlHandler.ALL_PRODUCT_IMAGE
                + productList.get(position).params[Product.P_IMAGE];

        Picasso.with(context).load(url).into(iv);

        final Button addToBasket = (Button) convertView
                .findViewById(R.id.addToBasket);
        final Button incQuantity = (Button) convertView
                .findViewById(R.id.incQuantity);
        final Button decQuantity = (Button) convertView
                .findViewById(R.id.decQuantity);
        final Button isDeal = (Button) convertView
                .findViewById(R.id.dealButton);

        if(!(productList.get(position).params[Product.IS_DEAL].contains("NULL")))
        {
            isDeal.setVisibility(View.VISIBLE);
            isDeal.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    itemActivityAdapterListener.OnIsDealClick(productList.get(position).params[Product.IS_DEAL]);
                }
            });
        }
        else
        {
            isDeal.setVisibility(View.INVISIBLE);
        }

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
                difference[position]++;

                aritlat = new AddRemoveItemToListAsyncTask();
                aritlat.execute(productList.get(position).params[Product.P_ID],1 + "",1+"");
                addToBasket.setVisibility(View.INVISIBLE);
                quantityLayout.setVisibility(View.VISIBLE);
                quantityView.setText(quantity[position] + "");
                itemActivityAdapterListener.onBasketCountChange();
            }
        });

        incQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]++;
                difference[position]++;
                quantityView.setText(quantity[position] + "");
                aritlat = new AddRemoveItemToListAsyncTask();
                aritlat.execute(productList.get(position).params[Product.P_ID],1 + "",1+"");

            }
        });

        decQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]--;
                difference[position]--;

                aritlat = new AddRemoveItemToListAsyncTask();
                aritlat.execute(productList.get(position).params[Product.P_ID],0+ "",1+"");
                if (quantity[position] == 0) {
                    quantityLayout.setVisibility(View.INVISIBLE);
                    addToBasket.setVisibility(View.VISIBLE);
                    itemActivityAdapterListener.onBasketCountChange();
                } else {
                    quantityView.setText(quantity[position] + "");
                }

            }
        });




        return convertView;

    }

    public int[] getDifference()
    {
        return difference;
    }

    public interface ItemActivityAdapterListener
    {
        void onBasketCountChange();

        void OnIsDealClick(String is_deal);
    }

}
