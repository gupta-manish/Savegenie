package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.activities.ItemActivity;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.backgroundclasses.Store;
import in.savegenie.savegenie.internetcommunication.InternetURL;
import in.savegenie.savegenie.internetcommunication.SendSelectedStoreAsyncTask;

/**
 * Created by manish on 5/4/15.
 */
public class StoreFragmentAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<Store> itemList;
    private int[] quantity;
    private int[] difference;
    int activePosition;
    String activeListId;
    Button activate[];
    Button active[];
    InternetURL urlHandler = new InternetURL();
    Handler mHandler;
    public StoreFragmentAdapter(Context context, Integer[] itemId,
                                ArrayList<Store> itemList) {
        super(context, R.layout.activity_current_grocery_list, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        active = new Button[itemList.size()];
        SharedPreferences preferences = context.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        activeListId = preferences.getString(SharedPrefString.GROCERY_STORE_ID,null);
        activePosition = -1;
        activate = new Button[itemList.size()];
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_store_fragment, parent, false);
        }
        TextView listName = (TextView) convertView.findViewById(R.id.storeName);
        listName.setText(itemList.get(position).name);


        activate[position] = (Button) convertView
                .findViewById(R.id.selectButton);
        active[position] = (Button) convertView
                .findViewById(R.id.selectedButton);

        if((itemList.get(position).id).equals(activeListId))
        {
            activePosition = position;
            active[position].setVisibility(View.VISIBLE);
            activate[position].setVisibility(View.INVISIBLE);
        }
        else
        {
            activate[position].setVisibility(View.VISIBLE);
            active[position].setVisibility(View.INVISIBLE);
        }

        activate[position].setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SendSelectedStoreAsyncTask gat = new SendSelectedStoreAsyncTask();


                Store gcl = itemList.get(position);
                if(gcl.id.equals(SharedPrefString.ALL_STORE_ID))
                {
                    gat.execute(null,null);
                }
                else
                {
                    gat.execute(gcl.name,gcl.id);
                }
                activeListId = gcl.id;
                active[position].setVisibility(View.VISIBLE);
                activate[position].setVisibility(View.INVISIBLE);
                if(activePosition>=0)
                {
                    active[activePosition].setVisibility(View.INVISIBLE);
                    activate[activePosition].setVisibility(View.VISIBLE);
                }
                activePosition = position;
                SharedPreferences preferences = context.getSharedPreferences(
                        SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                if(gcl.id.equals(SharedPrefString.ALL_STORE_ID))
                {
                    editor.putBoolean(SharedPrefString.IS_STORE_SELECTED, false);
                }
                else
                {
                    editor.putBoolean(SharedPrefString.IS_STORE_SELECTED, true);
                }

                editor.putString(SharedPrefString.GROCERY_STORE_ID, gcl.id);
                editor.putString(SharedPrefString.GROCERY_STORE_NAME, gcl.name);
                editor.apply();
                ((ItemActivity)context).openBestSelling();

            }
        });

        return convertView;

    }

    public int[] getDifference()
    {
        return difference;
    }

}
