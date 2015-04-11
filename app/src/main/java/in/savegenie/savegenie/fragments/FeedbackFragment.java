package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.internetcommunication.SendFeedBackAsyncTask;

/**
 * Created by manish on 4/4/15.
 */
public class FeedbackFragment extends Fragment
{


    private Context context;
    private RelativeLayout spinnerRL, noProduct;
    private Handler mHandler;
    public String title = "Feedback";
    private EditText feedback;
    private Button submitButton;
    private SendFeedBackAsyncTask sendFeedBackAsyncTask;
    private String response;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
    {
        getActivity().setTitle(title);
        View rootView = inflater.inflate(R.layout.fragment_feedback, container, false);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);
        spinnerRL.setAlpha((float) 0.50);
        feedback = (EditText)rootView.findViewById(R.id.feedback);
        submitButton = (Button)rootView.findViewById(R.id.submitButton);
        spinnerRL.setVisibility(View.INVISIBLE);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(feedback.getText().toString().equals(""))
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                           "Feedback Empty", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    spinnerRL.setVisibility(View.VISIBLE);
                    new Thread(sendFeedBack).start();
                }
            }
        });


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onPause()
    {
        super.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getActivity().getActionBar().setTitle(title);
    }

    Runnable sendFeedBack = new Runnable()
    {
        @Override
        public void run()
        {
            sendFeedBackAsyncTask = new SendFeedBackAsyncTask();

            sendFeedBackAsyncTask.execute(feedback.getText().toString());

            try
            {
                response = sendFeedBackAsyncTask.get();
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
                    spinnerRL.setVisibility(View.INVISIBLE);
                    if (response != null)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Feedback Sent", Toast.LENGTH_LONG)
                                .show();
                        spinnerRL.setVisibility(View.INVISIBLE);
                    }
                    else
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Feedback Not Sent", Toast.LENGTH_LONG)
                                .show();
                        spinnerRL.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }
    };



}
