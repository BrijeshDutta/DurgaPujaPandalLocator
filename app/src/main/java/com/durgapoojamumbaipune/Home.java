package com.durgapoojamumbaipune;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

    //UI Compoenents for adding a person to trip
    AutoCompleteTextView actvPersonName,actvPersonMobileNo,actvPersonEmailId,actvPersonDeposit;
    //.........VARIABLE RELATED TO PERSISTENCE.........//
    static boolean calledAlready = false;

    FirebaseDatabase database;
    DatabaseReference category;

    private TextView textViewUserName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<City,CityViewHolder> adapter;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //.....................DATABASE PERSISTENCE............................................//
        if(!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }

        //Init Firebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference(Constants.CITI_DATABASE_REFERENCE);

        //Load menu

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_city);
        recycler_menu.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);
        progressDialog = new ProgressDialog(this);

        progressDialog.setMessage("Loading.......");
        progressDialog.show();
        loadCity();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestToAddPandal();
            }
        });
    }

    private void requestToAddPandal() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.request_to_addpoojapandal, null);  // this line
        builder.setView(v);
        initializeDailogUiComponents(v);

        // Set up the buttons
        builder.setPositiveButton("Ok",null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        getUserEnteredValuesForAddingPerson();
//                        if(validateUserEnteredValues()){
//                            addPersonDataToPersonObject();
//                            createAddPersonCardView();
//                            dialog.dismiss();
//                        }
                    }
                });
            }
        });
        dialog.show();

    }

    private void initializeDailogUiComponents(View v) {
        actvPersonName = (AutoCompleteTextView) v.findViewById(R.id.actvPersonName);
        actvPersonMobileNo = (AutoCompleteTextView) v.findViewById(R.id.actvPersonMobileNo);
        actvPersonEmailId = (AutoCompleteTextView) v.findViewById(R.id.actvPersonEmailId);
        actvPersonDeposit = (AutoCompleteTextView) v.findViewById(R.id.actvPersonDeposit);
    }

    private void loadCity() {

        adapter= new FirebaseRecyclerAdapter<City, CityViewHolder>(City.class,R.layout.city_item,CityViewHolder.class,category) {
            @Override
            protected void populateViewHolder(CityViewHolder viewHolder, City model, int position) {
                viewHolder.txtCityName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.cityImageView);
                progressDialog.dismiss();
                final City clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

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
