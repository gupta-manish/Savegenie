package in.savegenie.savegenie.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.internetcommunication.GetAllAreasAsyncTask;
import in.savegenie.savegenie.internetcommunication.GetUserProfileAsyncTask;
import in.savegenie.savegenie.internetcommunication.ModifyProfileAsyncTask;
import in.savegenie.savegenie.responses.AllAreasResponse;
import in.savegenie.savegenie.responses.ModifyProfileResponse;
import in.savegenie.savegenie.responses.UserProfileResponse;

/**
 * Created by manish on 10/4/15.
 */
public class MyProfileFragment extends Fragment
{
    ModifyProfileAsyncTask mat;
    ModifyProfileResponse response;
    GetUserProfileAsyncTask upat;
    Handler mHandler;
    public Context context;
    UserProfileResponse userProfileResponse;
    GetAllAreasAsyncTask gaat;
    AllAreasResponse areasResponse;
    Spinner mr, area;
    EditText fname, lname, city, pincode, phone, email;
    Button btn;
    String[] mrArray = {"Mr", "Mrs"};
    String gender, areaString, firstName, lastName, cityName, pincodeString, phoneString, emailString;
    String selectedArea;
    ArrayAdapter<String> mrAdapter;
    ArrayAdapter<String> areaAdapter;
    RelativeLayout spinnerRL;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_my_profile, container, false);
        mr = (Spinner) rootView.findViewById(R.id.mr);
        fname = (EditText) rootView.findViewById(R.id.fname);
        lname = (EditText) rootView.findViewById(R.id.lname);
        city = (EditText) rootView.findViewById(R.id.city);
        area = (Spinner) rootView.findViewById(R.id.area);
        pincode = (EditText) rootView.findViewById(R.id.pincode);
        phone = (EditText) rootView.findViewById(R.id.phone);
        email = (EditText) rootView.findViewById(R.id.email);
        btn = (Button) rootView.findViewById(R.id.update);
        spinnerRL = (RelativeLayout)rootView.findViewById(R.id.spinnerRelativeLayout);

        btn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                firstName = fname.getText().toString();
                lastName = lname.getText().toString();
                cityName = city.getText().toString();
                areaString = selectedArea;
                pincodeString = pincode.getText().toString();
                phoneString = phone.getText().toString();
                emailString = email.getText().toString();
                if (firstName.isEmpty() || lastName.isEmpty() || cityName.isEmpty() ||
                        areaString.isEmpty() || pincodeString.isEmpty() || phoneString.isEmpty() || emailString.isEmpty() || gender.isEmpty())
                {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter the details", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    spinnerRL.setVisibility(View.INVISIBLE);
                    new Thread(modifyProfile).start();
                }
            }
        });

        spinnerRL.setVisibility(View.VISIBLE);

        new Thread(setUp).start();


        return rootView;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
    }

    Runnable setUp = new Runnable()
    {
        @Override
        public void run()
        {
            upat = new GetUserProfileAsyncTask();
            upat.execute();
            try
            {
                userProfileResponse = upat.get();

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

            mrAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, mrArray);

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    fname.setText(userProfileResponse.fname);
                    lname.setText(userProfileResponse.lname);
                    city.setText(userProfileResponse.city);
                    pincode.setText(userProfileResponse.pincode);
                    phone.setText(userProfileResponse.phone);
                    email.setText(userProfileResponse.email);
                    mr.setAdapter(mrAdapter);
                    if (userProfileResponse.title.equals("mr."))
                    {
                        gender = "Mr";
                        mr.setSelection(0);
                    }
                    else
                    {
                        gender = "Mrs";
                        mr.setSelection(1);
                    }
                    new Thread(getAreaList).start();
                }
            });
        }
    };

    Runnable getAreaList = new Runnable()
    {
        @Override
        public void run()
        {

            gaat = new GetAllAreasAsyncTask();
            gaat.execute();
            try
            {
                areasResponse = gaat.get();

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
                    areaAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, areasResponse.getAreaNameList());
                    areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    area.setAdapter(areaAdapter);
                    for (int i = 0; i < areasResponse.areaList.size(); i++)
                    {
                        if (areasResponse.areaList.get(i).name.equals(userProfileResponse.area))
                        {
                            area.setSelection(i);
                        }
                    }
                    spinnerRL.setVisibility(View.INVISIBLE);
                }
            });
        }
    };

    Runnable modifyProfile = new Runnable()
    {
        @Override
        public void run()
        {
            mat = new ModifyProfileAsyncTask();
            mat.execute(userProfileResponse.id, gender, firstName, lastName, cityName, areaString, pincodeString,
                    phoneString, emailString, userProfileResponse.userdetailid);

            try
            {
                response = mat.get();


            }
            catch (InterruptedException e)
            {
                // TODO Auto-generated catch block
                Log.d("deb1", "deb2");
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                // TODO Auto-generated catch block
                Log.d("deb1", "deb2");
                e.printStackTrace();
            }

            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    if (response != null)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Your Profile Is Successfully Updated", Toast.LENGTH_LONG)
                                .show();

                    }
                    if (response == null)
                    {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Unable to update profile", Toast.LENGTH_LONG)
                                .show();
                    }
                    spinnerRL.setVisibility(View.VISIBLE);
                }
            });


        }
    };

}
