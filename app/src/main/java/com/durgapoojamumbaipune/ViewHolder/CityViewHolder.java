package com.durgapoojamumbaipune.ViewHolder;

/**
 * Created by Rini Banerjee on 06-09-2017.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.R;


/**
 * Created by Rini Banerjee on 05-09-2017.
 */

public class CityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtCityName;
    public ImageView cityImageView;

    private ItemClickListener itemClickListener;

    public CityViewHolder(View itemView) {
        super(itemView);

        txtCityName = (TextView) itemView.findViewById(R.id.city_name);
        cityImageView = (ImageView) itemView.findViewById(R.id.city_image);

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
