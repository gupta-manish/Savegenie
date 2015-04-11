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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.internetcommunication.CreateNewListAsyncTask;
import in.savegenie.savegenie.internetcommunication.SendInactiveListNameAsyncTask;
import in.savegenie.savegenie.responses.SendInactiveListNameResponse;

/**
 * Created by manish on 6/4/15.
 */
public class SaveListDialogFragment extends DialogFragment
{
    private RelativeLayout spinnerRL;
    private TextView newListName;
    private CreateNewListAsyncTask cnlat;
    private Handler mHandler;
    String response;
    SharedPreferences preferences;
    SendInactiveListNameAsyncTask sendInactiveListNameAsyncTask;
    SendInactiveListNameResponse sendInactiveListNameResponse;
    SaveListDialogFragmentListener saveListDialogFragmentListener;
    String listNameString;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_create_list, null);
        newListName = (TextView) rootView.findViewById(R.id.newListName);

        mHandler = new Handler();
        builder.setView(rootView).setTitle("Create New List")
                .setPositiveButton(R.string.save, null)
                .setNegativeButton(R.string.continue_without_saving, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        saveListDialogFragmentListener.onSaveListDialogEnd();
                    }
                });
        return builder.create();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        Button b = ((AlertDialog)getDialog()).getButton(AlertDialog.BUTTON_POSITIVE);

        b.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                spinnerRL.setVisibility(View.VISIBLE);
                new Thread(sendListName).start();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        listNameString = getArguments().getString(IntentString.LIST_NAME_STRING);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        saveListDialogFragmentListener = (SaveListDialogFragmentListener)activity;
    }


    Runnable sendListName = new Runnable()
    {
        @Override
        public void run()
        {
            sendInactiveListNameAsyncTask = new SendInactiveListNameAsyncTask();

            sendInactiveListNameAsyncTask.execute(listNameString);

            try
            {
                sendInactiveListNameResponse = sendInactiveListNameAsyncTask.get();
            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if(sendInactiveListNameResponse.result.contains("1"))
                    {
                        saveListDialogFragmentListener.onSaveListDialogEnd();
                        dismiss();
                    }
                    else
                    {
                        spinnerRL.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Request not complete. Please try again", Toast.LENGTH_LONG)
                                .show();
                    }
                }
            });

        }
    };

    public interface SaveListDialogFragmentListener
    {
        public void onSaveListDialogEnd();
    }

}
