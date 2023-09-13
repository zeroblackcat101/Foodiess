package ae.ac.adu.joe.loginandregister.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import ae.ac.adu.joe.loginandregister.Adapters.SliderAdapter;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;

import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.opencv.android.OpenCVLoader;


public class Home extends AppCompatActivity {
    static {
        if(OpenCVLoader.initDebug()){
            Log.i("Home: ","Opencv is loaded");
        }
        else {
            Log.i("Home: ","Opencv failed to load");
        }
    }

    Button scanBtn;
    SliderView sliderView;
    int[] images = {R.drawable.one,R.drawable.two,R.drawable.three,R.drawable.four,R.drawable.five,R.drawable.six};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        sliderView = findViewById(R.id.image_slider);
        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();

        scanBtn = findViewById(R.id.scanButton);
        scanBtn.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),CameraActivity.class)));


        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Dashboard Selected
        bottomNavigationView.setSelectedItemId(R.id.home);




        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.search:
                        startActivity(new Intent(getApplicationContext(), Search.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddRecipe.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.community:
                        startActivity(new Intent(getApplicationContext(), Community.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.profile:
                        if (AuthApi.isLoggedIn()){
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        }
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }
}