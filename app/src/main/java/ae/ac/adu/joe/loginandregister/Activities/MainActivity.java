package ae.ac.adu.joe.loginandregister.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import apis.RecipeApi;
import models.Recipe;
import models.User;
import utils.CustomTask;
import utils.CustomTaskResult;

public class MainActivity extends AppCompatActivity {


    TextView createNewAccount,guestLogin, forgotPassword;
    EditText inputEmail, inputPassword;
    Button btnLogin;
    FirebaseAuth fAuth;
    ProgressBar progressBar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        fAuth = FirebaseAuth.getInstance();
        btnLogin = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);
        forgotPassword = findViewById(R.id.forgotPassword);

        createNewAccount = findViewById(R.id.createNewAccount);
        guestLogin = findViewById(R.id.guestLogin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        btnLogin.setOnClickListener(view -> {
            String email = inputEmail.getText().toString().trim();
            String password = inputPassword.getText().toString().trim();

            if (email.isEmpty()) {
                inputEmail.setError("Email is Required.");
                inputEmail.requestFocus();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                inputEmail.setError("Please provide valid email!");
                inputEmail.requestFocus();
                return;
            }

            if (password.isEmpty() || password.length() < 6) {
                inputPassword.setError("Enter password greater then 6 characters");
                inputPassword.requestFocus();
                return;

            }
            progressBar.setVisibility(View.VISIBLE);

            AuthApi.login(getApplicationContext(),email, password, customTaskResult -> {

                if (customTaskResult.isSuccessFull()) {

                    Toast.makeText(MainActivity.this, "Logged in Successfully as " + customTaskResult.getData().getFullName(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                } else {

                    Toast.makeText(MainActivity.this, customTaskResult.errorMessage(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);


                }

            });
        });


        createNewAccount.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, RegisterActivity.class)));

        guestLogin.setOnClickListener(view -> startActivity(new Intent(MainActivity.this,Home.class)));

        forgotPassword.setOnClickListener(view -> {
            EditText resetMail = new EditText(view.getContext());
            AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(view.getContext());
            //setting title and message for reset dialog to tell user what to do
            passwordResetDialog.setTitle("Reset Password?");
            passwordResetDialog.setMessage("Enter Your Email to Receive Reset Link");
            passwordResetDialog.setView(resetMail);
            //if no, go back to log in screen
            //if yes, take email and send the reset email
            passwordResetDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
                //extract the email and send resent link
                String mail = resetMail.getText().toString();
                //if the resent link is sent successfully
                fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(aVoid -> Toast.makeText(MainActivity.this,"Reset Link Sent To Your Email.",Toast.LENGTH_SHORT).show()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this,"Error ! Reset Link Did Not Send " + e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            });
            passwordResetDialog.setNegativeButton("No", (dialogInterface, i) -> {
                // close the dialog
            });
            passwordResetDialog.create().show();
        });




    }
    @Override
    protected void onStart() {
        super.onStart();




    }

}
