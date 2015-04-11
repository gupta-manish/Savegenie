package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.adapters.MissingItemActivityAdapter;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.MissingItem;

public class MissingDialogFragment extends DialogFragment
{
    private MissingItemActivityAdapter adapter;
    private ListView itemListView;
    private ArrayList<MissingItem> missingItemList;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.fragment_dialog_list_items, null);
        itemListView = (ListView) rootView.findViewById(R.id.quickList);
        itemListView.setAdapter(adapter);

        builder.setView(rootView).setTitle("Missing Items")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                    }
                });
        return builder.create();
    }

    private Integer[] getItemIds(int n)
    {
        Integer[] arr = new Integer[n];

        for (int i = 1; i <= n; i++)
        {
            arr[i - 1] = i;
        }
        return arr;

    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);

        missingItemList = getArguments().getParcelableArrayList(IntentString.ITEM_ACTIVITY_MISSING_LIST);

        adapter = new MissingItemActivityAdapter(activity,
                getItemIds(missingItemList.size()),
                missingItemList);
    }
}