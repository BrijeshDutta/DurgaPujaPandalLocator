package com.durgapoojamumbaipune;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.durgapoojamumbaipune.ViewHolder.PoojaPandalAdapter;
import com.durgapoojamumbaipune.constants.Constants;
import com.durgapoojamumbaipune.model.PoojaPandal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PoojaPandalList extends AppCompatActivity {

    RecyclerView recycler_poojapandallist;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference poojaPandal;

    List<PoojaPandal> poojaPandalList = new ArrayList<>();

    String cityId ="",cityName="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_pandal_list);

        //Get the gategory Id
        if (getIntent() !=null){
            cityName = getIntent().getStringExtra("CityName");

            if (!cityName.isEmpty() && cityName !=null){
                super.setTitle(cityName);
            }
        }
        //Init Firebase
        database = FirebaseDatabase.getInstance();
        poojaPandal = database.getReference(Constants.POOJAPANDAL_DATABASE_REFERENCE);

        recycler_poojapandallist = (RecyclerView) findViewById(R.id.recycler_poojapandallist);
        recycler_poojapandallist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_poojapandallist.setLayoutManager(layoutManager);


        //Get the gategory Id
        if (getIntent() !=null){
            cityId = getIntent().getStringExtra("CityId");

            if (!cityId.isEmpty() && cityId !=null){
                loadListPoojaPandal(cityId);
            }
        }

    }

    private void loadListPoojaPandal(final String cityId) {

        final PoojaPandalAdapter poojaPandalAdapter = new PoojaPandalAdapter(poojaPandalList,PoojaPandalList.this);
        poojaPandal.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                poojaPandalList.clear();
                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot  poojaPandalDetailSnapshot : dataSnapshot.getChildren()){
                        PoojaPandal poojaPandal = poojaPandalDetailSnapshot.getValue(PoojaPandal.class);
                        if (cityId.equalsIgnoreCase(poojaPandal.getCityId())) {
                            //Toast.makeText(PoojaPandalList.this, "Name : " + poojaPandal.getName(), Toast.LENGTH_SHORT).show();
                            poojaPandalList.add(0,poojaPandal);
                            poojaPandalAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        recycler_poojapandallist.setAdapter(poojaPandalAdapter);

    }
}
