package in.savegenie.savegenie.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.GroceryList;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.GetSelectedListItemsAsyncTask;
import in.savegenie.savegenie.internetcommunication.InternetURL;

/**
 * Created by manish on 5/4/15.
 */
public class ListFragmentAdapter extends ArrayAdapter<Integer>
{
    private Context context;
    LayoutInflater layInf;
    private ArrayList<GroceryList> itemList;
    private int[] quantity;
    private int[] difference;
    int activePosition;
    String activeListId;
    Button activate[];
    Button active[];
    private int[] checked;
    private int checkNum;
    ListSelectListener listSelectListener;
    CheckNumChangeListener checkNumChangeListener;
    InternetURL urlHandler = new InternetURL();
    Handler mHandler;
    public ListFragmentAdapter(Context context, Integer[] itemId,
                               ArrayList<GroceryList> itemList,CheckNumChangeListener checkNumChangeListener) {
        super(context, R.layout.activity_item, R.id.quickList, itemId);
        // TODO Auto-generated constructor stub
        this.context = context;
        this.itemList = itemList;
        checkNum = 0;
        checked = new int[itemList.size()];
        active = new Button[itemList.size()];
        activate = new Button[itemList.size()];
        this.checkNumChangeListener = checkNumChangeListener;
        layInf = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listSelectListener = (ListSelectListener)context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        if (convertView == null) {
            convertView = layInf.inflate(R.layout.list_row_list_fragment, parent, false);
        }
        TextView listName = (TextView) convertView.findViewById(R.id.listName);
        listName.setText(itemList.get(position).name);

        listName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                checkNumChangeListener.OnListClick(itemList.get(position).id,itemList.get(position).name);
            }
        });

        final CheckBox check = (CheckBox)convertView.findViewById(R.id.check);
        if(checked[position]==1)
        {
            check.setChecked(true);
        }
        else
        {
            check.setChecked(false);
        }

        check.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(checked[position]==1)
                {
                    checked[position] = 0;
                    check.setChecked(false);
                    checkNum--;
                }
                else
                {
                    checked[position] = 1;
                    check.setChecked(true);
                    checkNum++;
                }
                checkNumChangeListener.onCheckNumChange(checkNum);
            }
        });

        SharedPreferences preferences = context.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
        activeListId = preferences.getString(SharedPrefString.GROCERY_LIST_ID,null);
        activate[position] = (Button) convertView
                .findViewById(R.id.activateButton);
        active[position] = (Button) convertView
                .findViewById(R.id.activeButton);

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
                GetSelectedListItemsAsyncTask gat = new GetSelectedListItemsAsyncTask();


                GroceryList gcl = itemList.get(position);
                gat.execute(gcl.id);
                activeListId = gcl.id;
                active[position].setVisibility(View.VISIBLE);
                activate[position].setVisibility(View.INVISIBLE);
                active[activePosition].setVisibility(View.INVISIBLE);
                activate[activePosition].setVisibility(View.VISIBLE);
                activePosition = position;
                SharedPreferences preferences = context.getSharedPreferences(
                        SharedPrefString.SHARED_PREFERENCE_KEY, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(SharedPrefString.GROCERY_LIST_ID, gcl.id);
                editor.putString(SharedPrefString.GROCERY_LIST_NAME, gcl.name);
                editor.putBoolean(SharedPrefString.AUTOMATIC_CREATED_LIST,false);
                editor.apply();
                listSelectListener.OnListSelect();


            }
        });

        return convertView;

    }



    public int[] getCheckStatus()
    {
        return checked;
    }

    public void resetCheckStatus()
    {
        for(int i=0;i<checked.length;i++)
        {
            checked[i] = 0;
        }
    }

    public interface ListSelectListener
    {

        public void OnListSelect();


    }

    public interface CheckNumChangeListener
    {
        public void onCheckNumChange(int checkNum);
        public void OnListClick(String listId,String listName);
    }


}

