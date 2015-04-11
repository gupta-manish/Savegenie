package in.savegenie.savegenie.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

import in.savegenie.savegenie.R;
import in.savegenie.savegenie.backgroundclasses.IntentString;
import in.savegenie.savegenie.backgroundclasses.SharedPrefString;
import in.savegenie.savegenie.internetcommunication.LoginAsyncTask;
import in.savegenie.savegenie.responses.LoginResponse;

/**
 * Created by manish on 4/4/15.
 */
public class LoginActivity extends Activity
{
    private ImageButton loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private ProgressBar pb;
    LoginAsyncTask lat;
    LoginResponse response;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = (EditText) findViewById(R.id.email);
        passwordEditText = (EditText) findViewById(R.id.password);
        loginButton = (ImageButton) findViewById(R.id.loginButton);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.INVISIBLE);


        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),
                            "Please enter the details", Toast.LENGTH_LONG)
                            .show();
                }
                else
                {
                    lat = new LoginAsyncTask();
                    lat.execute(email, password);

                    try
                    {
                        response = lat.get();
                        if (response == null)
                        {
                            Intent nic = new Intent(IntentString.NO_INTERNET_CONNECTION_ACTIVITY);
                            startActivity(nic);
                            finish();
                        }

                    }
                    catch (InterruptedException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    catch (ExecutionException e)
                    {
                        e.printStackTrace();
                    }

                    if (response != null && response.result.contains("1"))
                    {

                        SharedPreferences preferences = getSharedPreferences(SharedPrefString.SHARED_PREFERENCE_KEY, MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(SharedPrefString.EMAIL_ID, email);
                        editor.putString(SharedPrefString.PASSWORD, password);
                        editor.putString(SharedPrefString.FIRST_NAME, response.fname);
                        editor.putString(SharedPrefString.LAST_NAME, response.lname);
                        editor.putBoolean(SharedPrefString.IS_SIGNED_IN, true);
                        editor.apply();
                        Intent sa = new Intent(IntentString.START_ACTIVITY);
                        startActivity(sa);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),
                                "Incorrect Username or password", Toast.LENGTH_LONG)
                                .show();


                    }

                }

            }
        });


    }
}
