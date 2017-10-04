package com.durgapoojamumbaipune.ViewHolder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.R;

/**
 * Created by Rini Banerjee on 04-10-2017.
 */

public class PandalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView pandal_name;
    public ImageButton btnGetDirection,btnShare;
    public CardView cardViewPoojaPandal;

    private ItemClickListener itemClickListener;

    public PandalViewHolder(View itemView) {
        super(itemView);

        pandal_name = (TextView) itemView.findViewById(R.id.pandal_name);
        btnGetDirection = (ImageButton) itemView.findViewById(R.id.btnGetDirection);
        cardViewPoojaPandal = (CardView) itemView.findViewById(R.id.cardViewPoojaPandal);
        btnShare = (ImageButton) itemView.findViewById(R.id.btnShare);
        itemView.setOnClickListener(this);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
