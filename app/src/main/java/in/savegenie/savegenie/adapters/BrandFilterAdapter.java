package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.Brand;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 4/4/15.
 */
public class BrandFilterAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<Brand> brandList;
    private int[] check;
    InternetURL urlHandler = new InternetURL();;
    public BrandFilterAdapter(Context context, Integer[] itemId,
                              ArrayList<Brand> brandList) {
        super(context, R.layout.activity_item, R.id.brandFilterList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        check = new int[brandList.size()];
        this.brandList = brandList;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_filter, parent, false);
        }

        final CheckBox checkBox = (CheckBox)convertView.findViewById(R.id.checkBox);
        if(check[position] == 1)
        {
            checkBox.setChecked(true);
        }
        else
        {
            checkBox.setChecked(false);
        }
        checkBox.setText(brandList.get(position).name);

        checkBox.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(check[position]==1)
                {
                    check[position] = 0;
                    checkBox.setChecked(false);
                }
                else
                {
                    check[position] = 1;
                    checkBox.setChecked(true);
                }
            }
        });

        return convertView;

    }

    public int[] getCheckedState()
    {
        return check;
    }

    public void resetCheckStatus()
    {
        for(int i=0;i<check.length;i++)
        {
            check[i] = 0;
        }
    }

    public void resetCheck()
    {
        for(int i=0;i<check.length;i++)
        {
            check[i] = 0;
        }
    }

}
