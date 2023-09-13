package ae.ac.adu.joe.loginandregister.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ae.ac.adu.joe.loginandregister.Adapters.RecipeAdapter;
import ae.ac.adu.joe.loginandregister.R;
import apis.AuthApi;
import models.Recipe;
import models.RecipeLike;

public class Search extends AppCompatActivity {
    SearchView searchView;
    RecyclerView searchRecyclerView;
    private FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    private CollectionReference recipeRef = fStore.collection("recipes");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        searchRecyclerView = findViewById(R.id.searchRecyclerView);
        searchRecyclerView.setHasFixedSize(true);
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        searchView = findViewById(R.id.searchView);
        //removes cursor from search view
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i("testmode onQueryTextSubmit", s);
                filterRecipes(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //ignore

                return false;
            }
        });

        //Initialize and Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Dashboard Selected
        bottomNavigationView.setSelectedItemId(R.id.search);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.search:
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.add:
                        startActivity(new Intent(getApplicationContext(), AddRecipe.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.community:
                        startActivity(new Intent(getApplicationContext(), Community.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.profile:
                        if (AuthApi.isLoggedIn()) {
                            startActivity(new Intent(getApplicationContext(), Profile.class));
                        } else {
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    private void filterRecipes(String searchText) {

        List<String> splittedSearchList =  Arrays.asList(searchText.trim().toLowerCase().replaceAll(" ","_").split(",")).stream().map(e ->e.trim()).collect(Collectors.toList());

        recipeRef.whereArrayContainsAny("key_words", splittedSearchList).get().addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                List<DocumentSnapshot> x =  task.getResult().getDocuments();
                List<Recipe> recipes = task.getResult().getDocuments().stream().map(e -> Recipe.fromMap(e.getData())).collect(Collectors.toList());
                recipes = recipes.stream().filter(new Predicate<Recipe>() {
                    @Override
                    public boolean test(Recipe recipe) {
                       boolean  result = recipe.getKeyWords().containsAll(splittedSearchList);
                        return result;

                    }
                }).collect(Collectors.toList());
                RecipeAdapter recipeAdapter = new RecipeAdapter(Search.this, recipes);
                searchRecyclerView.setAdapter(recipeAdapter);
            } else {
                Toast.makeText(getApplicationContext(), "Search Query Failed",Toast.LENGTH_SHORT);
            }

        });
    }
}