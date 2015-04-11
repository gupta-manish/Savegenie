package in.savegenie.savegenie.adapters;

import android.content.Context;
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
import in.savegenie.savegenie.backgroundclasses.SelectedProduct;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 8/4/15.
 */
public class ReviewBasketActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<SelectedProduct> itemList;
    String[] saving;
    InternetURL urlHandler = new InternetURL();
    private int[] quantity;
    private int[] difference;
    private int[] altSelected;
    ReviewBasketActivityAdapterListener reviewBasketActivityAdapterListener;

    public ReviewBasketActivityAdapter(Context context, Integer[] itemId,
                                       ArrayList<SelectedProduct> itemList, String[] saving,int[] altSelected) {
        super(context, R.layout.activity_review_basket, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        this.saving = saving;
        quantity = new int[itemList.size()];
        this.altSelected = altSelected;
        difference = new int[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            quantity[i] = Integer.parseInt(itemList.get(i).count);
        }
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        reviewBasketActivityAdapterListener = (ReviewBasketActivityAdapterListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_review_basket,
                    parent, false);
        }

        RelativeLayout availableLayout = (RelativeLayout)convertView.findViewById(R.id.availableLayout);
        RelativeLayout missingLayout = (RelativeLayout)convertView.findViewById(R.id.missingLayout);
        RelativeLayout alternateLayout = (RelativeLayout)convertView.findViewById(R.id.alternateLayout);
        TextView itemPrice = (TextView) convertView
                .findViewById(R.id.priceString);

        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);

        TextView itemSize = (TextView) convertView.findViewById(R.id.itemSize);

        TextView itemQuant = (TextView) convertView
                .findViewById(R.id.itemQuant);

        ImageView iv = (ImageView) convertView.findViewById(R.id.itemIcon);
        String url;

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

        if (itemList.get(position).storeSkuDataList.get(0).ProductSkuId
                .equals("")) {

            itemPrice.setText("Price: Rs" + itemList.get(position).mrp);

            itemName.setText("Name: " + itemList.get(position).code);

            itemSize.setText("Size: " + itemList.get(position).size + " "
                    + itemList.get(position).unit);

            itemQuant.setText("Quantity: " + itemList.get(position).count);

            url = urlHandler.ALL_PRODUCT_IMAGE + itemList.get(position).image;

            convertView.setAlpha((float) 0.3);
            alternateLayout.setVisibility(View.INVISIBLE);
            missingLayout.setVisibility(View.VISIBLE);
            availableLayout.setVisibility(View.INVISIBLE);

        } else {
            itemPrice.setText("Price: Rs"
                    + itemList.get(position).storeSkuDataList.get(0).price);
            itemName.setText("Name: "
                    + itemList.get(position).storeSkuDataList.get(0).name);
            itemSize.setText("Size: "
                    + itemList.get(position).storeSkuDataList.get(0).size + " "
                    + itemList.get(position).storeSkuDataList.get(0).unit);
            itemQuant.setText("Quantity: "
                    + itemList.get(position).storeSkuDataList.get(0).count);
            url = urlHandler.ALL_PRODUCT_IMAGE
                    + itemList.get(position).storeSkuDataList.get(0).image;
            convertView.setAlpha((float) 1);
            alternateLayout.setVisibility(View.INVISIBLE);
            missingLayout.setVisibility(View.INVISIBLE);
            availableLayout.setVisibility(View.VISIBLE);

        }

        Button moreOptions = (Button) convertView
                .findViewById(R.id.moreOptions);
        if (((itemList.get(position).storeSkuDataList.get(0).found.contains("1") && itemList
                .get(position).storeSkuDataList.get(0).similarStoreSkusList
                .size() == 0) || altSelected[position] == 1)
                || itemList.get(position).storeSkuDataList.get(0).ProductSkuId
                .equals("")) {
            moreOptions.setVisibility(View.INVISIBLE);
        } else {
            moreOptions.setVisibility(View.VISIBLE);
            alternateLayout.setVisibility(View.VISIBLE);
            missingLayout.setVisibility(View.INVISIBLE);
            availableLayout.setVisibility(View.INVISIBLE);
        }

        Button swapItem = (Button) convertView.findViewById(R.id.swapItem);
        if ((saving[position].equals("")) || altSelected[position] == 1) {
            swapItem.setVisibility(View.INVISIBLE);
        } else {
            swapItem.setText("Swap and Save: " + saving[position]);
            swapItem.setVisibility(View.VISIBLE);
        }

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
                addToBasket.setVisibility(View.INVISIBLE);
                quantityLayout.setVisibility(View.VISIBLE);
                quantityView.setText(quantity[position] + "");
            }
        });

        incQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]++;
                difference[position]++;
                quantityView.setText(quantity[position] + "");
            }
        });

        decQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]--;
                difference[position]--;
                if (quantity[position] == 0) {
                    quantityLayout.setVisibility(View.INVISIBLE);
                    addToBasket.setVisibility(View.VISIBLE);
                } else {
                    quantityView.setText(quantity[position] + "");
                }

            }
        });

        moreOptions.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                reviewBasketActivityAdapterListener.onMoreOptionsClick(itemList.get(position).storeSkuDataList.get(0).ProductSkuId
                        + "-"
                        + itemList.get(position).storeSkuDataList
                        .get(0).store_sku_id
                        + "-"
                        + itemList.get(position).storeSkuDataList
                        .get(0).count,
                        itemList.get(position).storeSkuDataList.get(0).ProductSkuId,position);

            }
        });

        swapItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                reviewBasketActivityAdapterListener.onSwapAndSaveClick(itemList.get(position).storeSkuDataList.get(0).ProductSkuId
                                + "-"
                                + itemList.get(position).storeSkuDataList
                                .get(0).store_sku_id
                                + "-"
                                + itemList.get(position).storeSkuDataList
                                .get(0).count,
                        itemList.get(position).storeSkuDataList.get(0).ProductSkuId,position);
            }
        });

        Picasso.with(context).load(url).into(iv);

        return convertView;

    }

    public int[] getQuantity()
    {
        return quantity;
    }

    public void setAltSelected(int position)
    {
        altSelected[position] = 1;
    }

    public interface ReviewBasketActivityAdapterListener
    {
        public void onMoreOptionsClick(String oneProductData,String productskuid,int position);
        public void onSwapAndSaveClick(String oneProductData,String productskuid,int position);
    }

}
