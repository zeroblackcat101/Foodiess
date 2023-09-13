package ae.ac.adu.joe.loginandregister.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import ae.ac.adu.joe.loginandregister.R;

import apis.AuthApi;

public class AutoLogInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_log_in);

        AuthApi.autologin(getApplicationContext(), customTaskResult -> {

            if(customTaskResult.isSuccessFull()){


                if(customTaskResult.getData()!=null){
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    Toast.makeText(getApplicationContext(), "Auto-Logged in Successfully as " + customTaskResult.getData().getFullName(), Toast.LENGTH_SHORT).show();
                }else{

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }


            }else{

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                Toast.makeText(getApplicationContext(), customTaskResult.errorMessage(), Toast.LENGTH_SHORT).show();

            }

        });
    }
}