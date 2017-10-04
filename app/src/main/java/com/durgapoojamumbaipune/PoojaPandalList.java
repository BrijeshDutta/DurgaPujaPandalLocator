package com.durgapoojamumbaipune;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.ViewHolder.PandalViewHolder;
import com.durgapoojamumbaipune.ViewHolder.PoojaPandalAdapter;
import com.durgapoojamumbaipune.constants.Constants;
import com.durgapoojamumbaipune.model.PoojaPandal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PoojaPandalList extends AppCompatActivity {

    RecyclerView recycler_poojapandallist;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference poojaPandal;

    List<PoojaPandal> poojaPandalList = new ArrayList<>();

    String cityId ="",cityName="";

    FirebaseRecyclerAdapter<PoojaPandal,PandalViewHolder> adapter;
    //Search funtionality

    FirebaseRecyclerAdapter<PoojaPandal,PandalViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

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


        //Search funcinality

        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter pandal name");
        //materialSearchBar.setSpeechMode(false);

        loadSuggest();      //Function to load suggestions..from firebase
        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //when user tyoe their tex we will change suggest list
                List<String> suggest = new ArrayList<String>();
                for (String search:suggestList){
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                        suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                //When search bar is closed restore original adapter

                if (!enabled){
                    recycler_poojapandallist.setAdapter(adapter);
                }
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                //When search finished
                //show results od search adapter

                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });
    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<PoojaPandal, PandalViewHolder>(PoojaPandal.class,
                R.layout.poojapandal_layout,
                PandalViewHolder.class,
                poojaPandal.orderByChild("Name").equalTo(text.toString())   //SELECT * FROM Food where menuId =
        ) {
            @Override
            protected void populateViewHolder(PandalViewHolder viewHolder, final PoojaPandal model, int position) {

                viewHolder.pandal_name.setText(model.getName());

                final PoojaPandal local = model;
                viewHolder.cardViewPoojaPandal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentPoojaPandaDetail = new Intent(getApplicationContext(), PoojaPandaDetail.class);
                        intentPoojaPandaDetail.putExtra("PandalId",model.getPandalId());
                        intentPoojaPandaDetail.putExtra("PandalName",model.getName());
                        startActivity(intentPoojaPandaDetail);
                    }
                });
                viewHolder.btnGetDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri gmmIntentUri = Uri.parse(model.getMapLink());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
                viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,model.getName()+"\n"
                                +model.getMapLink()+"\n"
                                +getString(R.string.sentfromapp)+getString(R.string.app_name));

                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
            }
        };
        //set adapter
        recycler_poojapandallist.setAdapter(searchAdapter);

    }

    private void loadSuggest() {
        poojaPandal.orderByChild("CityId").equalTo(cityId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot postSnapshot:dataSnapshot.getChildren()){

                            PoojaPandal item = postSnapshot.getValue(PoojaPandal.class);
                            suggestList.add(item.getName());  //Add name of food to suggest list
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadListPoojaPandal(final String cityId) {

        adapter = new FirebaseRecyclerAdapter<PoojaPandal, PandalViewHolder>(PoojaPandal.class,
                R.layout.poojapandal_layout,
                PandalViewHolder.class,
                poojaPandal.orderByChild("CityId").equalTo(cityId)   //SELECT * FROM Food where menuId =
        ) {
            @Override
            protected void populateViewHolder(PandalViewHolder viewHolder, final PoojaPandal model, int position) {

                viewHolder.pandal_name.setText(model.getName());

                final PoojaPandal local = model;
                viewHolder.cardViewPoojaPandal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intentPoojaPandaDetail = new Intent(getApplicationContext(), PoojaPandaDetail.class);
                        intentPoojaPandaDetail.putExtra("PandalId",model.getPandalId());
                        intentPoojaPandaDetail.putExtra("PandalName",model.getName());
                        startActivity(intentPoojaPandaDetail);
                    }
                });
                viewHolder.btnGetDirection.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri gmmIntentUri = Uri.parse(model.getMapLink());
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        if (mapIntent.resolveActivity(getPackageManager()) != null) {
                            startActivity(mapIntent);
                        }
                    }
                });
                viewHolder.btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT,model.getName()+"\n"
                                +model.getMapLink()+"\n"
                                +getString(R.string.sentfromapp)+getString(R.string.app_name));

                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
            }
        };

        //set adapter
        recycler_poojapandallist.setAdapter(adapter);


    }
}
