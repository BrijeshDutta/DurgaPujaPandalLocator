package com.durgapoojamumbaipune;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.durgapoojamumbaipune.constants.Constants;
import com.durgapoojamumbaipune.model.PoojaPandal;
import com.durgapoojamumbaipune.model.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class PoojaPandaDetail extends AppCompatActivity {

    //UI variable
    TextView pandal_name,pandal_description,etRewviewComments,tvError;
    ImageView image_pandal,btnSubmitReview;
    FloatingActionButton btnGetDirection;
    MaterialRatingBar ratings;

    String PandalId="",PandalName="";

    FirebaseDatabase database;
    DatabaseReference poojaPandal;
    DatabaseReference poojaReview;

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
        btnGetDirection = (FloatingActionButton) findViewById(R.id.btnGetDirection);
        btnSubmitReview = (ImageButton) findViewById(R.id.btnSubmitReview);
        etRewviewComments =(EditText) findViewById(R.id.etRewviewComments);
        tvError = (TextView) findViewById(R.id.tvError);

        //Init firebase

        database = FirebaseDatabase.getInstance();
        poojaPandal = database.getReference(Constants.POOJAPANDAL_DATABASE_REFERENCE);
        poojaReview = database.getReference(Constants.REQUEST_POOJAREVIEW_DATABASE_REFERENCE);
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
        btnSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateUserEnteredValues()){
                    addReview();
                }
            }
        });
        etRewviewComments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                tvError.setText(null);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void addReview() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(PoojaPandaDetail.this);
        LayoutInflater inflater = this.getLayoutInflater();
        View v = inflater.inflate(R.layout.ratingbar, null);  // this line
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

                        if (ratings.getRating()<=0){
                            Toast.makeText(PoojaPandaDetail.this,"Give some ratings",Toast.LENGTH_SHORT).show();
                        }else {
                            processAddReviewInDatabase();
                            etRewviewComments.setText(null);
                            dialog.dismiss();
                        }
                    }
                });
            }
        });
        dialog.show();

    }

    private void processAddReviewInDatabase() {
        Review review = new Review(PandalId,etRewviewComments.getText().toString(),
                String.valueOf(ratings.getRating()),
                new SimpleDateFormat("MMM yyyy", Locale.ENGLISH).format(new Date()));

        poojaReview.child(String.valueOf(System.currentTimeMillis())).setValue(review).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(PoojaPandaDetail.this,"Review added",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateUserEnteredValues() {
        boolean isValid = true;
        View focusView = null;
        if(etRewviewComments.getText().toString().isEmpty()){
            tvError.setText("Review comments required");
            isValid = false;
            focusView = etRewviewComments;
        }
        return isValid;
    }

    private void initializeDailogUiComponents(View v) {
        ratings = (MaterialRatingBar) v.findViewById(R.id.ratings);
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
