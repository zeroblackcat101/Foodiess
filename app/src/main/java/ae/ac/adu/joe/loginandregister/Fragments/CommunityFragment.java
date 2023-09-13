package ae.ac.adu.joe.loginandregister.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import ae.ac.adu.joe.loginandregister.Adapters.PostAdapter;
import ae.ac.adu.joe.loginandregister.R;
import models.Post;
import models.Recipe;


public class CommunityFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView postRecyclerView ;
    PostAdapter postAdapter ;
    FirebaseFirestore fStore;
    ListenerRegistration listener;



    public CommunityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
          LinearLayoutManager lin = new LinearLayoutManager(getActivity());

        // Inflate the layout for this fragment
        View fragmentView =  inflater.inflate(R.layout.fragment_community, container, false);
        postRecyclerView  = fragmentView.findViewById(R.id.postRV);
        postRecyclerView.setLayoutManager(lin);
        postRecyclerView.setHasFixedSize(true);

        fStore = FirebaseFirestore.getInstance();



//        fStore.collection("posts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//                if(!task.isSuccessful()) {return;}
//                            List<Post> postList = task.getResult()
//                    .getDocuments()
//                    .stream()
//                    .map(e -> Post.fromMap(e.getData()))
//                    .collect(Collectors.toList());
//
//
//                postAdapter = new PostAdapter(getActivity(),postList);
//                postRecyclerView.setAdapter(postAdapter);
//            }
//        });

        return fragmentView ;
    }

    @Override
    public void onStart() {
        super.onStart();

        listener = fStore.collection("posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value == null) {return;}
                List<DocumentSnapshot> x = value
                        .getDocuments();
                List<Post> postList = value
                        .getDocuments()
                        .stream()
                        .map(e -> Post.fromMap(e.getData()))
                        .collect(Collectors.toList());


                postAdapter = new PostAdapter(getActivity(),postList);
                postRecyclerView.setAdapter(postAdapter);
            }
        });




    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}