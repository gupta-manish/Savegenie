package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.GroceryListItem;
import in.savegenie.savegenie.internetcommunication.DeleteItemFromListAsynTask;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 5/4/15.
 */
public class CurrentGroceryListActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<GroceryListItem> itemList;
    private int[] quantity;
    private int[] removed;
    DeleteItemFromListAsynTask difla;
    BasketCountChangeListener basketCountChangeListener;
    InternetURL urlHandler = new InternetURL();
    public CurrentGroceryListActivityAdapter(Context context, Integer[] itemId,
                                            ArrayList<GroceryListItem> itemList) {
        super(context, R.layout.activity_item, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        removed = new int[itemList.size()];
        this.itemList = itemList;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        basketCountChangeListener = (BasketCountChangeListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_current_list_item, parent, false);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(itemList.get(position).productSkuName + " (" + itemList.get(position).count +")" );

        final RelativeLayout listItem = (RelativeLayout)convertView.findViewById(R.id.list_item);
        final Button removeFromBasket = (Button) convertView
                .findViewById(R.id.removeFromBasket);


        if(removed[position] == 1)
        {
            removeFromBasket.setVisibility(View.INVISIBLE);
            listItem.setAlpha(0.5f);
        }
        else
        {
            removeFromBasket.setVisibility(View.VISIBLE);
            listItem.setAlpha(1);
            removeFromBasket.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    difla = new DeleteItemFromListAsynTask();
                    difla.execute(itemList.get(position).productSkuId);
                    basketCountChangeListener.onBasketCountChange();
                    removeFromBasket.setVisibility(View.INVISIBLE);
                    removed[position] = 1;
                    listItem.setAlpha(0.5f);
                }
            });
        }


        return convertView;

    }


    public interface BasketCountChangeListener
    {
        void onBasketCountChange();
    }

}

