package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.ShippingAddress;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 9/4/15.
 */
public class ShippingAddressDialogAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<ShippingAddress> itemList;
    String[] storeId;
    InternetURL urlHandler = new InternetURL();

    public ShippingAddressDialogAdapter(Context context, Integer[] itemId,
                                        ArrayList<ShippingAddress> itemList) {
        super(context, R.layout.dialog_select_shipping_address, R.id.quickList, itemId);
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
            convertView = layInf.inflate(R.layout.list_row_shipping_address,
                    parent, false);
        }

        TextView name = (TextView)convertView.findViewById(R.id.name);
        name.setText("Name: "+itemList.get(position).name);
        TextView address = (TextView)convertView.findViewById(R.id.address);
        address.setText("Address: "+itemList.get(position).address);
        TextView area = (TextView)convertView.findViewById(R.id.area);
        area.setText("Area: "+itemList.get(position).area +","+itemList.get(position).city);
        TextView mobile = (TextView)convertView.findViewById(R.id.mobile);
        mobile.setText("Mobile: "+itemList.get(position).mobileno);

        return convertView;

    }


}