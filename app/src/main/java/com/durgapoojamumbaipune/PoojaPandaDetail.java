package com.durgapoojamumbaipune;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgapoojamumbaipune.constants.Constants;
import com.durgapoojamumbaipune.model.PoojaPandal;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;

public class PoojaPandaDetail extends AppCompatActivity {

    //UI variable
    TextView pandal_name,pandal_description;
    ImageView image_pandal;
    FButton btnGetDirection;

    String PandalId="",PandalName="";

    FirebaseDatabase database;
    DatabaseReference poojaPandal;

    PoojaPandal currentPoojaPandal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pooja_panda_detail);


        //Get the Pandal Name
        if (getIntent() !=null){
            PandalName = getIntent().getStringExtra("PandalName");

            if (!PandalName.isEmpty() && PandalName !=null){
                super.setTitle(PandalName);
            }
        }
        //Init UI
        pandal_name = (TextView) findViewById(R.id.pandal_name);
        pandal_description = (TextView) findViewById(R.id.pandal_description);
        image_pandal = (ImageView) findViewById(R.id.image_pandal);
        btnGetDirection = (FButton) findViewById(R.id.btnGetDirection);


        //Init firebase

        database = FirebaseDatabase.getInstance();
        poojaPandal = database.getReference(Constants.POOJAPANDAL_DATABASE_REFERENCE);
        //Get the Pandal Id
        if (getIntent() !=null){
            PandalId = getIntent().getStringExtra("PandalId");

            if (!PandalId.isEmpty() && PandalId !=null){

                getPandalDetail(PandalId);
            }
        }

        //get Direction
        btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(currentPoojaPandal.getMapLink());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    private void getPandalDetail(String pandalId) {
        poojaPandal.child(pandalId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPoojaPandal = dataSnapshot.getValue(PoojaPandal.class);
                pandal_name.setText(currentPoojaPandal.getName());
                pandal_description.setText(currentPoojaPandal.getDescription());
                Picasso.with(getBaseContext()).load(currentPoojaPandal.getImagePath())
                        .fit()
                        .centerInside().into(image_pandal);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
