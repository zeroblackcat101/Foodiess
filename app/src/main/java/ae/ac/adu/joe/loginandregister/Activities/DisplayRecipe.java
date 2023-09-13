package ae.ac.adu.joe.loginandregister.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import ae.ac.adu.joe.loginandregister.Adapters.RecipeAdapter;
import ae.ac.adu.joe.loginandregister.R;
import models.Recipe;

public class DisplayRecipe extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Recipe> recipeList;
    RecipeAdapter recipeAdapter;
    FirebaseFirestore fStore;
    ProgressDialog progressDialog;




    private String readFromFile(String fileName) {
        File path = getApplicationContext().getFilesDir();
        File readFrom = new File(path,fileName);
        byte[] content = new byte[(int)readFrom.length()];
        try {
            FileInputStream stream = new FileInputStream(readFrom);
            stream.read(content);
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_recipe);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data... ");
        progressDialog.show();

        recyclerView = findViewById(R.id.recipeRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fStore = FirebaseFirestore.getInstance();
        recipeList = new ArrayList<Recipe>();
        recipeAdapter =
        new RecipeAdapter(DisplayRecipe.this,recipeList);

        recyclerView.setAdapter(recipeAdapter);

        EventChangeListener();


    }

    private void EventChangeListener() {


        String jsonFileString = readFromFile("objects.json");



        ObjectMapper mapper = new ObjectMapper();
        try
        {
            CollectionReference recipeRef = fStore.collection("recipes");

            //Convert Map to JSON

            List<Map<String, Object>> list = mapper.readValue(jsonFileString , new TypeReference<List<Map<String, Object>>>(){});
            //Print JSON output

            System.out.println(list);

            List<String> ingredients = list.stream().map(e->e.get("name").toString()).collect(Collectors.toList());

            List<String> keywordList = new ArrayList<String>();

            for(String ingredient:ingredients){
                List<String> splittedIngredient = new ArrayList<String>( Arrays.asList(ingredient.toLowerCase().trim().split(" ")));
                keywordList.addAll(splittedIngredient);
            }


            keywordList =  keywordList.stream().map(e ->e.trim()).collect(Collectors.toList());


            List<String> finalKeywordList = keywordList;
            recipeRef.whereArrayContainsAny("key_words",keywordList).get().addOnCompleteListener(task -> {
                List<Recipe> filteredRecipes = new ArrayList<Recipe>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {

                        filteredRecipes.add(Recipe.fromMap(document.getData()));
                        Log.e("TAG", document.getId() + " => " + document.getData());

                    }

                    filteredRecipes = filteredRecipes.stream().filter(new Predicate<Recipe>() {
                        @Override
                        public boolean test(Recipe recipe) {
                            boolean  result = recipe.getKeyWords().containsAll(finalKeywordList);
                            return result;

                        }
                    }).collect(Collectors.toList());

                    this.recipeList.clear();
                    this.recipeList.addAll(filteredRecipes);
                    recipeAdapter.notifyDataSetChanged();
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();

                } else {
                    Log.e("TAG", "Error getting documents: ", task.getException());
                }

            });

        }
        catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }
}