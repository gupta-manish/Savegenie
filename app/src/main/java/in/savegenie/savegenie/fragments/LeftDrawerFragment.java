package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.LeftDrawerAdapter;
import in.savegenie.savegenie.backgroundclasses.CategoryTree;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;

/**
 * Created by manish on 4/4/15.
 */
public class LeftDrawerFragment extends Fragment
{
    private ListView leftDrawerList1, leftDrawerList2, leftDrawerList3;
    private ArrayList<Integer> categoryIndex1, categoryIndex2, categoryIndex3;
    private LeftDrawerAdapter adapter, adapter2, adapter3;
    private Menu menu;
    private TextView left_drawer_username;
    private CategoryTree categoryTree;
    private SharedPreferences preferences;
    private String userName;
    private LeftDrawerListener leftDrawerListener;
    RelativeLayout bestSellingProducts,favorites,deals_left;
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        preferences = activity.getSharedPreferences(
                SharedPrefString.SHARED_PREFERENCE_KEY, Activity.MODE_PRIVATE);
        leftDrawerListener = (LeftDrawerListener)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        categoryTree = CategoryTree.getInstance();
        userName= preferences.getString(SharedPrefString.FIRST_NAME, null) + " " + preferences.getString(SharedPrefString.LAST_NAME, null);
        categoryIndex1 = categoryTree.getChildren(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_left_drawer, container, false);
        left_drawer_username = (TextView) rootView.findViewById(R.id.left_drawer_username);
        left_drawer_username.setText(userName);
        leftDrawerList1 = (ListView) rootView.findViewById(R.id.left_drawer_list);
        leftDrawerList2 = (ListView) rootView.findViewById(R.id.left_drawer_list_1);
        leftDrawerList3 = (ListView) rootView.findViewById(R.id.left_drawer_list_2);
        bestSellingProducts = (RelativeLayout)rootView.findViewById(R.id.bestSellingProducts);
        favorites = (RelativeLayout)rootView.findViewById(R.id.favorites);
        deals_left = (RelativeLayout)rootView.findViewById(R.id.deals_left);
        adapter = new LeftDrawerAdapter(getActivity(), R.layout.activity_item,
                R.id.left_drawer_list, categoryIndex1);
        leftDrawerList1.setAdapter(adapter);

        leftDrawerList1.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {

            @Override
            public void onItemClick(AdapterView<?> arg0, View view,
                                    int position, long id)
            {
                categoryIndex2 = categoryTree.getChildren(categoryIndex1
                        .get(position));
                adapter2 = new LeftDrawerAdapter(getActivity(), R.layout.activity_item,
                        R.id.left_drawer_list_1, categoryIndex2);
                leftDrawerList2.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();

                leftDrawerList2.setOnItemClickListener(new AdapterView.OnItemClickListener()
                {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View view,
                                            int position, long id)
                    {
                        categoryIndex3 = categoryTree.getChildren(categoryIndex2
                                .get(position));
                        adapter3 = new LeftDrawerAdapter(getActivity(), R.layout.activity_item,
                                R.id.left_drawer_list_2, categoryIndex3);
                        leftDrawerList3.setAdapter(adapter3);
                        adapter2.notifyDataSetChanged();
                        leftDrawerList3.setOnItemClickListener(new AdapterView.OnItemClickListener()
                        {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View view,
                                                    int position, long id)
                            {
                                leftDrawerListener.onCategoryClick(categoryTree.getProductName(categoryIndex3
                                        .get(position)),categoryTree.getProductId(categoryIndex3
                                        .get(position)));
                            }
                        });
                    }

                });
            }
        });


        bestSellingProducts.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                leftDrawerListener.onBestSellingClick();
            }
        });

        favorites.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                leftDrawerListener.onFavoriteClick();
            }
        });

        deals_left.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                leftDrawerListener.onDealLeftClick();
            }
        });

        return rootView;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    public interface LeftDrawerListener
    {
        public void onCategoryClick(String productName,String productId);
        public void onBestSellingClick();
        public void onFavoriteClick();
        public void onDealLeftClick();

    }

}
