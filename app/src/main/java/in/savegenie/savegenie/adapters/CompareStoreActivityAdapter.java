package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.AlternateItem;
import in.savegenie.savegenie.backgroundclasses.CompareStoreItem;
import in.savegenie.savegenie.backgroundclasses.MissingItem;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.InternetURL;
import in.savegenie.savegenie.internetcommunication.SendSelectedStoreAsyncTask;

public class CompareStoreActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<CompareStoreItem> itemList;
    private int[] quantity;
    private int[] difference;
    String[] storeId;
    InternetURL urlHandler = new InternetURL();
    CompareStoreActivityAdapterListener compareStoreActivityAdapterListener;

    public CompareStoreActivityAdapter(Context context, Integer[] itemId,
                                       ArrayList<CompareStoreItem> itemList) {
        super(context, R.layout.activity_item, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        storeId = new String[itemList.size()];
        for(int i=0;i<itemList.size();i++)
        {
            storeId[i] = itemList.get(i).storeId;
        }
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       compareStoreActivityAdapterListener = (CompareStoreActivityAdapterListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_compare_store,
                    parent, false);
        }
        Button missingItems = (Button) convertView
                .findViewById(R.id.missingItems);
        missingItems.setText("Missing Items: "
                + itemList.get(position).missingItems.size());

        Button alternateItems = (Button) convertView
                .findViewById(R.id.alternateItems);
        alternateItems.setText("Alternate Items: "
                + itemList.get(position).alternateItems.size());

        TextView storePrice = (TextView) convertView
                .findViewById(R.id.storePrice);
        storePrice.setText("Price: Rs" + itemList.get(position).storePrice);

        TextView deliveryCost = (TextView) convertView
                .findViewById(R.id.deliveryCost);
        deliveryCost.setText("Delivery Price: Rs"
                + itemList.get(position).deliveryCost);

        TextView totalCost = (TextView) convertView
                .findViewById(R.id.totalCost);
        totalCost.setText("Total Cost: Rs" + itemList.get(position).totalCost);

        ImageView iv = (ImageView) convertView.findViewById(R.id.storeIcon);
        String url = urlHandler.ALL_STORE_IMAGE
                + itemList.get(position).imgName;

        Picasso.with(context).load(url).into(iv);

        final Button reviewBasket = (Button) convertView
                .findViewById(R.id.reviewBasket);

        reviewBasket.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                SendSelectedStoreAsyncTask gat = new SendSelectedStoreAsyncTask();
                gat.execute(itemList.get(position).storeName,itemList.get(position).storeId);
                SharedPreferences preferences = context.getSharedPreferences(
                        SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean(SharedPrefString.IS_STORE_SELECTED, true);
                editor.putString(SharedPrefString.GROCERY_STORE_ID, itemList.get(position).storeId);
                editor.putString(SharedPrefString.GROCERY_STORE_NAME,  itemList.get(position).storeName);
                editor.apply();
                compareStoreActivityAdapterListener.onReviewBasketClickAfterCompare(itemList.get(position).storeId,
                        itemList.get(position).storeName,itemList.get(position).storeAddress,itemList.get(position).deliveryCost);


            }
        });

        missingItems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                compareStoreActivityAdapterListener.onMissingClick(itemList.get(position).missingItems);
            }
        });

        alternateItems.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                compareStoreActivityAdapterListener.onAlternateClick(itemList.get(position).alternateItems);
            }
        });

        return convertView;

    }

    public int[] getDifference() {
        return difference;
    }

    public interface CompareStoreActivityAdapterListener
    {
        public void onReviewBasketClickAfterCompare(String storeId,String storeName,String storeAddress,String deliveryCost);
        public void onMissingClick(ArrayList<MissingItem> missingList);
        public void onAlternateClick(ArrayList<AlternateItem> alternateList);
    }

}