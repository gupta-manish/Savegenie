package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.internetcommunication.CreateNewListAsyncTask;

/**
 * Created by manish on 6/4/15.
 */
public class CreateNewListDialogFragment extends DialogFragment
{
    private RelativeLayout spinnerRL;
    private TextView newListName;
    private CreateNewListAsyncTask cnlat;
    private Handler mHandler;
    String response;
    SharedPreferences preferences;
    CreateNewListListener createNewListListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_create_list, null);
        newListName = (TextView) rootView.findViewById(R.id.newListName);

        mHandler = new Handler();
        builder.setView(rootView).setTitle("Create New List")
                .setPositiveButton(R.string.create, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        createNewListListener.OnCreateNewList(newListName.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });
        return builder.create();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        createNewListListener = (CreateNewListListener)activity;
    }

    public interface CreateNewListListener
    {
        public void OnCreateNewList(String listName);
    }

}
