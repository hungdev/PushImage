package net.downloadblog.manic.pushimage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import net.downloadblog.manic.pushimage.R;
import net.downloadblog.manic.pushimage.activity.IData;
import net.downloadblog.manic.pushimage.activity.PostActivity;
import net.downloadblog.manic.pushimage.activity.ShowPhotoClick;
import net.downloadblog.manic.pushimage.adapter.RecycleAdapter;
import net.downloadblog.manic.pushimage.object.ImageEntity;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
//used lib: https://github.com/wasabeef/recyclerview-animators
public class MainListFragment extends Fragment implements AdapterView.OnItemClickListener {
    private LinearLayoutManager lLayout;
    private ListView mDrawerList;
    public ArrayAdapter<String> mAdapter;

    private RecyclerView mRecycler;
    private LinearLayoutManager mManager;

    private MenuItem itemRefresh, itemAdd, itemSetting;

    private DatabaseReference mDatabase;
    private ArrayList<ImageEntity> arrayList;
    RecycleAdapter rcAdapter;

    IData iData = new IData() {
        @Override
        public void onCickData(int checkKey, String dataContent, String dataLocation, String dataPhoto, String dataKey) {

            if(checkKey==1) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("dataContent", dataContent);
                bundle.putString("dataLocation", dataLocation);
                bundle.putString("dataPhoto", dataPhoto);
                bundle.putString("dataKey", dataKey);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(getActivity(), ShowPhotoClick.class);
                Bundle bundle = new Bundle();

                bundle.putString("dataContent", dataContent);
                bundle.putString("dataLocation", dataLocation);
                bundle.putString("dataPhoto", dataPhoto);
                bundle.putString("dataKey", dataKey);
                intent.putExtra("data", bundle);
                startActivity(intent);
            }
        }
    };
    public MainListFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MainListFragment newInstance(String param1, String param2) {
        MainListFragment fragment = new MainListFragment();
        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        mRecycler.setHasFixedSize(true);
        View view = inflater.inflate(R.layout.main_list_layout, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.rcv_recycle);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        //List<ImageEntity> rowListItem = getAllItemList();
        arrayList = new ArrayList<ImageEntity>();
        lLayout = new LinearLayoutManager(getContext());

        mRecycler.setLayoutManager(lLayout);

        mRecycler.smoothScrollToPosition(0);
        //MyFirebase.getData(mRecycler,getContext());


        mDatabase.child("posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayList = new ArrayList<ImageEntity>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    ImageEntity imageEntity = snapshot.getValue(ImageEntity.class);
                    arrayList.add(0, imageEntity);
                    Log.e("Show", dataSnapshot.toString());
                    rcAdapter = new RecycleAdapter(arrayList, getContext(),iData);
                    //mRecycler.setAdapter(rcAdapter);
                   // mRecycler.setAdapter(new AlphaInAnimationAdapter(rcAdapter));

                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(rcAdapter);
                    alphaAdapter.setDuration(1000);
                    alphaAdapter.setInterpolator(new OvershootInterpolator());
                    //scaleAdapter.setFirstOnly(false);
                    //mRecycler.setAdapter(alphaAdapter);
                    mRecycler.setAdapter(new ScaleInAnimationAdapter(alphaAdapter));
                    rcAdapter.notifyItemInserted(0);

                    rcAdapter.notifyDataSetChanged();

                }
                // Log.e("data",list.toString());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    public void refreshRecycleview() {
        mRecycler.setAdapter(new RecycleAdapter(arrayList, getContext(),iData));
        mRecycler.invalidate();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        itemRefresh = menu.findItem(R.id.action_refresh);
        itemAdd = menu.findItem(R.id.action_new);
        itemSetting = menu.findItem(R.id.action_settings);

        itemRefresh.setVisible(false);
        itemAdd.setVisible(false);
        itemSetting.setVisible(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getContext(), "id: " + mDatabase.getKey().toString(), Toast.LENGTH_LONG).show();
        Log.e("ItemClick", "Click");
    }


}
