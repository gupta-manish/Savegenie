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
import in.savegenie.savegenie.internetcommunication.UpdateCountAsyncTask;

/**
 * Created by manish on 5/4/15.
 */
public class ListDetailAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<GroceryListItem> itemList;
    private int[] quantity;
    private int[] removed;
    DeleteItemFromListAsynTask difla;
    InternetURL urlHandler = new InternetURL();
    UpdateCountAsyncTask ucat;
    String listId = "";
    public ListDetailAdapter(Context context, Integer[] itemId,
                                             ArrayList<GroceryListItem> itemList,String listId) {
        super(context, R.layout.activity_item, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        quantity = new int[itemList.size()];
        for(int i=0;i<itemList.size();i++)
        {
            quantity[i] = Integer.parseInt(itemList.get(i).count);
        }
        this.listId = listId;
        this.itemList = itemList;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_list_detail, parent, false);
        }
        TextView itemName = (TextView) convertView.findViewById(R.id.itemName);
        itemName.setText(itemList.get(position).productSkuName + " (" + itemList.get(position).count +")" );

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
                ucat = new UpdateCountAsyncTask();
                ucat.execute(listId,itemList.get(position).productSkuId,quantity[position]+"");
                addToBasket.setVisibility(View.INVISIBLE);
                quantityLayout.setVisibility(View.VISIBLE);
                quantityView.setText(quantity[position] + "");
            }
        });

        incQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]++;
                quantityView.setText(quantity[position] + "");
                ucat = new UpdateCountAsyncTask();
                ucat.execute(listId,itemList.get(position).productSkuId,quantity[position]+"");

            }
        });

        decQuantity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                quantity[position]--;
                ucat = new UpdateCountAsyncTask();
                ucat.execute(listId,itemList.get(position).productSkuId,quantity[position]+"");
                if (quantity[position] == 0) {
                    quantityLayout.setVisibility(View.INVISIBLE);
                    addToBasket.setVisibility(View.VISIBLE);
                } else {
                    quantityView.setText(quantity[position] + "");
                }

            }
        });





        return convertView;

    }


}

