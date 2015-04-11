package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.MissingItem;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 7/4/15.
 */
public class MissingItemActivityAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<MissingItem> itemList;
    InternetURL urlHandler = new InternetURL();;
    public MissingItemActivityAdapter(Context context, Integer[] itemId,
                                      ArrayList<MissingItem> itemList) {
        super(context, R.layout.activity_missing_items, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_missing_items, parent, false);
        }



        TextView mrp = (TextView) convertView.findViewById(R.id.itemMRP);
        mrp.setText("MRP: Rs"+itemList.get(position).mrp);

        TextView quant = (TextView) convertView.findViewById(R.id.itemQuant);
        quant.setText("Quantity: "+itemList.get(position).count);

        TextView name = (TextView) convertView.findViewById(R.id.itemName);
        name.setText("Name: "+itemList.get(position).code);


        return convertView;

    }

}
