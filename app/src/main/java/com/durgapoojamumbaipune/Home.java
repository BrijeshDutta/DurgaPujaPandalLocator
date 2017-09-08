package com.durgapoojamumbaipune;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.durgapoojamumbaipune.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    //UI Compoenents for adding a person to trip
    AutoCompleteTextView actvPandalName,actvPandalDescription,actvPandalAddress,actvPandalContactNumber;

    //.........VARIABLE RELATED TO PERSISTENCE.........//
    static boolean calledAlready = false;

    FirebaseDatabase database;
    DatabaseReference category;

    DatabaseReference requests;

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
        requests = database.getReference(Constants.REQUEST_POOJAPANDAL_DATABASE_REFERENCE);

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
                        if(validateUserEnteredValues()){
                            processRequestInDatabase();
                            dialog.dismiss();
                        }

                    }
                });
            }
        });
        dialog.show();

    }

    private void processRequestInDatabase() {
        Request request = new Request(actvPandalName.getText().toString(),
                actvPandalDescription.getText().toString(),actvPandalAddress.getText().toString(),actvPandalContactNumber.getText().toString());
        requests.child(String.valueOf(System.currentTimeMillis())).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Home.this,"Request sent to the admin",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private boolean validateUserEnteredValues() {
        boolean isValid = true;
        View focusView = null;
        if(actvPandalName.getText().toString().isEmpty()){
            actvPandalName.setError(getString(R.string.pandalnameisrequired));
            isValid = false;
            focusView = actvPandalName;
        }
        else if(actvPandalAddress.getText().toString().isEmpty()){
            actvPandalAddress.setError(getString(R.string.pandaladdressisrequired));
            isValid = false;
            focusView = actvPandalAddress;
        }
        return isValid;
    }

    private void initializeDailogUiComponents(View v) {
        actvPandalName = (AutoCompleteTextView) v.findViewById(R.id.actvPandalName);
        actvPandalDescription = (AutoCompleteTextView) v.findViewById(R.id.actvPandalDescription);
        actvPandalAddress = (AutoCompleteTextView) v.findViewById(R.id.actvPandalAddress);
        actvPandalContactNumber = (AutoCompleteTextView) v.findViewById(R.id.actvPandalContactNumber);
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
