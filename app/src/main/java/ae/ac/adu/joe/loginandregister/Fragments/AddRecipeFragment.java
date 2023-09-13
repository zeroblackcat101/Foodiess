package ae.ac.adu.joe.loginandregister.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.List;
import java.util.stream.Collectors;

import ae.ac.adu.joe.loginandregister.Adapters.UserRecipeAdapter;
import ae.ac.adu.joe.loginandregister.R;
import models.UserRecipe;

public class AddRecipeFragment extends Fragment {

    RecyclerView addRecyclerView;
   UserRecipeAdapter userRecipeAdapter;
    FirebaseFirestore fStore;
    ListenerRegistration listener;



    public AddRecipeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayoutManager lin = new LinearLayoutManager(getActivity());

        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_add_recipe, container, false);
        addRecyclerView = fragmentView.findViewById(R.id.userRecipeRV);
        addRecyclerView.setLayoutManager(lin);
        addRecyclerView.setHasFixedSize(true);

        fStore = FirebaseFirestore.getInstance();

        return fragmentView ;
    }

    @Override
    public void onStart() {
        super.onStart();
        listener = fStore.collection("user_recipes").addSnapshotListener((value, error) -> {
            if(value == null) {return;}
            List<UserRecipe> recipeList = value
                    .getDocuments()
                    .stream()
                    .map(e -> UserRecipe.fromMap(e.getData()))
                    .collect(Collectors.toList());


            userRecipeAdapter = new UserRecipeAdapter(getActivity(),recipeList);
            addRecyclerView.setAdapter(userRecipeAdapter);
        });



    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onDestroy() {
        listener.remove();
        super.onDestroy();
    }
}
