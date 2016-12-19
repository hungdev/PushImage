package net.downloadblog.manic.pushimage.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.activity.IData;
import net.downloadblog.manic.pushimage.adapter.RecycleAdapter;
import net.downloadblog.manic.pushimage.object.ImageEntity;

import java.util.ArrayList;

public class MyPostFragment extends Fragment {

    DatabaseReference databaseReference;
    RecyclerView rcvMypost;
    ArrayList<ImageEntity> arrayList;
    RecycleAdapter rcAdapter;
    public LinearLayoutManager lLayout;

    public MyPostFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_my_post, container, false);
        String myUserId = getUid();
//        Query myTopPostsQuery = databaseReference.child("user-posts").child(myUserId);
        databaseReference= FirebaseDatabase.getInstance().getReference().child("user-posts");

        rcvMypost=(RecyclerView)view.findViewById(R.id.rcv_mypost);
        lLayout = new LinearLayoutManager(getContext());
        rcvMypost.setLayoutManager(lLayout);
        arrayList= new ArrayList<ImageEntity>();
        rcvMypost.smoothScrollToPosition(0);
        IData iData = new IData() {
            @Override
            public void onCickData(int checkKey, String dataContent, String dataLocation, String dataPhoto, String dataKey) {

            }
        };
        rcAdapter = new RecycleAdapter(arrayList,getContext(), iData);
        rcvMypost.setAdapter(rcAdapter);


        databaseReference.child(getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                arrayList.add(0, dataSnapshot.getValue(ImageEntity.class));

                Log.e("Array:",  ""+arrayList);
                rcAdapter.notifyItemInserted(0);
                //Log.e("Time: ", "" + Time.calenderTime(dataSnapshot.getValue(ImageEntity.class).getTime()));

                rcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            // TODO: implement the ChildEventListener methods as documented above
            // ...
        });

        return view;
    }
    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
