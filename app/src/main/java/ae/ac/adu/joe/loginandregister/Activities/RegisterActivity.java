package ae.ac.adu.joe.loginandregister.Activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikhaellopez.circularimageview.CircularImageView;

import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;

public class RegisterActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    TextView alreadyHaveAccount;
    EditText inputEmail, inputPassword, confirmPassword, inputFullName,inputPhone;
    Button btnRegister;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    CircularImageView profileImg;
    Uri imgUri = null;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        alreadyHaveAccount = findViewById(R.id.alreadyHaveAccount);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        inputFullName = findViewById(R.id.inputFullName);
        inputPhone = findViewById(R.id.inputPhone);
        btnRegister = findViewById(R.id.btnRegister);
        profileImg = findViewById(R.id.profileImg);

        progressBar = findViewById(R.id.progressBar2);

        setUpProfileImgClick();



        alreadyHaveAccount.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, MainActivity.class)));


        btnRegister.setOnClickListener(view -> PerformAuth());
    }

    private void setUpProfileImgClick() {
        ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                uri -> {
                    // Handle the returned Uri
                   profileImg.setImageURI(uri);
                    imgUri = uri;

                });


        profileImg.setOnClickListener(view -> {
            //open gallery when img clicked
            mGetContent.launch("image/*");

        });
    }
    //method to verify fields and give error if fields are not filled properly

    private void PerformAuth() {
        String email = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        String conPassword = confirmPassword.getText().toString().trim();
        String fullName = inputFullName.getText().toString().trim();
        String phoneNumber = inputPhone.getText().toString().trim();
        if(fullName.isEmpty()){
            inputFullName.setError("Full Name is required!");
            inputFullName.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            inputEmail.setError("Please provide valid email!");
            inputEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            inputPassword.setError("Please enter password");
            inputPassword.requestFocus();
            return;
        }
        if(password.length() < 6){
            inputPassword.setError("Enter password greater then 6 characters");
            inputPassword.requestFocus();
            return;

        }
        if (!password.matches("(.*[0-9].*)")){
            inputPassword.setError("Password must include at least 1 digit");
            inputPassword.requestFocus();
            return;
        }
        if (!password.matches("(.*[a-z].*)")){
            inputPassword.setError("Password must include at least 1 lowercase letter");
            inputPassword.requestFocus();
            return;
        }
        if (!password.matches("(.*[A-Z].*)")){
            inputPassword.setError("Password must include at least 1 uppercase letter");
            inputPassword.requestFocus();
            return;
        }
        if (!password.matches("(.*[!@#$%^&+=].*)")) {
            inputPassword.setError("Password must include at least 1 special character");
            inputPassword.requestFocus();
            return;
        }

        if (!password.equals(conPassword)){
            confirmPassword.setError("Password Does Not Match");
            confirmPassword.requestFocus();
            return;
        }
        if (phoneNumber.isEmpty()){
            inputPhone.setError("Please enter phone number");
            inputPhone.requestFocus();
            return;
        }
        if (phoneNumber.length() != 10){
            inputPhone.setError("Please enter 10 digit phone number");
            inputPhone.requestFocus();
            return;
        }

        if(imgUri == null){
            Toast.makeText(getApplicationContext(),"Please add profile image",Toast.LENGTH_LONG);
            return;
        }

        progressBar.setVisibility(View.VISIBLE);


        //Create User with specific email and password

        AuthApi.register(
                fullName,
                email,
                phoneNumber,
                password,
                imgUri,
                customTaskResult -> {
                    if(customTaskResult.isSuccessFull()){

                        Toast.makeText(RegisterActivity.this, "User Created", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));


                    }else{

                        Toast.makeText(RegisterActivity.this, customTaskResult.errorMessage(), Toast.LENGTH_SHORT).show();
                     progressBar.setVisibility(View.GONE);


                    }
                });


    }
}