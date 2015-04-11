package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.OrderItem;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 9/4/15.
 */
public class OrderDetailAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<OrderItem> itemList;
    InternetURL urlHandler = new InternetURL();;
    public OrderDetailAdapter(Context context, Integer[] itemId,
                              ArrayList<OrderItem> itemList) {
        super(context, R.layout.activity_order, R.id.quickList, itemId);
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
            convertView = layInf.inflate(R.layout.list_row_order_detail, parent, false);
        }

        TextView productName = (TextView)convertView.findViewById(R.id.productName);
        productName.setText(itemList.get(position).productSkuName);

        TextView quantity = (TextView)convertView.findViewById(R.id.quantity);
        quantity.setText(itemList.get(position).quantity);

        TextView mrp = (TextView)convertView.findViewById(R.id.mrp);
        mrp.setText("Rs " + itemList.get(position).mrp);

        TextView unitPrice = (TextView)convertView.findViewById(R.id.unitPrice);
        unitPrice.setText("Rs "+itemList.get(position).price);

        TextView subTotal = (TextView)convertView.findViewById(R.id.subTotal);
        subTotal.setText("Rs "+itemList.get(position).subTotal);

        return convertView;

    }

}
