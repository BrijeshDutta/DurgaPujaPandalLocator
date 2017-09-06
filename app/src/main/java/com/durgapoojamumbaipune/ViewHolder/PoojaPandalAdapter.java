package com.durgapoojamumbaipune.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.PoojaPandaDetail;
import com.durgapoojamumbaipune.PoojaPandalList;
import com.durgapoojamumbaipune.R;
import com.durgapoojamumbaipune.model.PoojaPandal;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;


/**
 * Created by Rini Banerjee on 05-09-2017.
 */

class PoojaPandalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView pandal_name, pandal_description;
    public FButton btnGetDirection;
    public CardView cardViewPoojaPandal;

    private ItemClickListener itemClickListener;

    public void setPandal_name(TextView pandal_name) {
        this.pandal_name = pandal_name;
    }

    public PoojaPandalViewHolder(View itemView) {
        super(itemView);

        pandal_name = (TextView) itemView.findViewById(R.id.pandal_name);
        pandal_description =(TextView) itemView.findViewById(R.id.pandal_description);
        btnGetDirection = (FButton) itemView.findViewById(R.id.btnGetDirection);
        cardViewPoojaPandal = (CardView) itemView.findViewById(R.id.cardViewPoojaPandal);
    }

    @Override
    public void onClick(View v) {

    }
}
public class PoojaPandalAdapter extends  RecyclerView.Adapter<PoojaPandalViewHolder>{

    private List<PoojaPandal> listData = new ArrayList<>();
    private Context context;

    public PoojaPandalAdapter(List<PoojaPandal> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public PoojaPandalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.poojapandal_layout,parent,false);

        return new PoojaPandalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PoojaPandalViewHolder holder, final int position) {

        holder.pandal_name.setText(listData.get(position).getName());
        //holder.pandal_description.setText(listData.get(position).getDescription());
        holder.btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(listData.get(position).getMapLink());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(mapIntent);
                }
            }
        });

        holder.cardViewPoojaPandal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentPoojaPandaDetail = new Intent(context.getApplicationContext(), PoojaPandaDetail.class);
                intentPoojaPandaDetail.putExtra("PandalId",listData.get(position).getPandalId());
                intentPoojaPandaDetail.putExtra("PandalName",listData.get(position).getName());
                context.startActivity(intentPoojaPandaDetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
