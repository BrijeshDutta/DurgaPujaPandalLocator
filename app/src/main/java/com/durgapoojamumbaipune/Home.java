package com.durgapoojamumbaipune;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.ViewHolder.CityViewHolder;
import com.durgapoojamumbaipune.constants.Constants;
import com.durgapoojamumbaipune.model.City;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {


    //NEW

    FirebaseDatabase database;
    DatabaseReference category;

    private TextView textViewUserName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<City,CityViewHolder> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Init Firebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference(Constants.CITI_DATABASE_REFERENCE);

        //Load menu

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_city);
        recycler_menu.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadCity();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadCity() {

        adapter= new FirebaseRecyclerAdapter<City, CityViewHolder>(City.class,R.layout.city_item,CityViewHolder.class,category) {
            @Override
            protected void populateViewHolder(CityViewHolder viewHolder, City model, int position) {
                viewHolder.txtCityName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.cityImageView);

                final City clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Toast.makeText(Home.this,"" +clickItem.getName(),Toast.LENGTH_SHORT).show();

                        //Get category and send it to new Activity

                        Intent foodList = new Intent(Home.this, PoojaPandalList.class);
                        foodList.putExtra("CityId",adapter.getRef(position).getKey());
                        foodList.putExtra("CityName",clickItem.getName());
                        startActivity(foodList);

                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);


    }

}
