package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.util.Log;
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
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 8/4/15.
 */
public class MoreOptionsActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<AlternateItem> itemList;
    InternetURL urlHandler = new InternetURL();;
    int positionInReviewBasket;
    private MoreOptionsActivityAdapterListener moreOptionsActivityAdapterListener;

    public MoreOptionsActivityAdapter(Context context, Integer[] itemId,
                                      ArrayList<AlternateItem> itemList,int positionInReviewBasket) {
        super(context, R.layout.activity_more_options, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        this.positionInReviewBasket = positionInReviewBasket;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        moreOptionsActivityAdapterListener = (MoreOptionsActivityAdapterListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_more_options, parent, false);
        }



        TextView itemPrice = (TextView) convertView.findViewById(R.id.priceString);
        itemPrice.setText("Price: Rs"+itemList.get(position).price);

        TextView quant = (TextView) convertView.findViewById(R.id.itemQuant);
        quant.setText("Quantity: "+itemList.get(position).count);

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText("Name: "+itemList.get(position).name);

        TextView size = (TextView) convertView.findViewById(R.id.itemSize);
        size.setText("Size: "+itemList.get(position).size + " "+ itemList.get(position).unit);


        ImageView iv = (ImageView) convertView.findViewById(R.id.itemIcon);
        String url = urlHandler.ALL_PRODUCT_IMAGE
                + itemList.get(position).image;

        Button selectAlternate = (Button)convertView.findViewById(R.id.selectAlternate);
        selectAlternate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                moreOptionsActivityAdapterListener.onSelectAlternateClick(itemList.get(position),positionInReviewBasket);
            }
        });

        Log.d("IMAGE", "IMAGE " + url);

        Picasso.with(context).load(url).into(iv);
        return convertView;

    }

    public interface MoreOptionsActivityAdapterListener
    {
        public void onSelectAlternateClick(AlternateItem alternateItem,int positionInReviewBasket);
    }

}
