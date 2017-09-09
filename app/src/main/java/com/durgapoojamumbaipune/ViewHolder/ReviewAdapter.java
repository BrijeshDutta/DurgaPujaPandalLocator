package com.durgapoojamumbaipune.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.durgapoojamumbaipune.Interface.ItemClickListener;
import com.durgapoojamumbaipune.PoojaPandaDetail;
import com.durgapoojamumbaipune.R;
import com.durgapoojamumbaipune.model.PoojaPandal;
import com.durgapoojamumbaipune.model.Review;

import java.util.ArrayList;
import java.util.List;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

/**
 * Created by Rini Banerjee on 09-09-2017.
 */

class ReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    MaterialRatingBar ratings;
    public TextView tvReview, date;

    private ItemClickListener itemClickListener;



    public ReviewViewHolder(View itemView) {
        super(itemView);

        tvReview = (TextView) itemView.findViewById(R.id.tvReview);
        date =(TextView) itemView.findViewById(R.id.date);
        ratings = (MaterialRatingBar) itemView.findViewById(R.id.ratings);

    }

    @Override
    public void onClick(View v) {

    }
}
public class ReviewAdapter extends  RecyclerView.Adapter<ReviewViewHolder>{

    private List<Review> listData = new ArrayList<>();
    private Context context;

    public ReviewAdapter(List<Review> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View itemView = layoutInflater.inflate(R.layout.review_list,parent,false);

        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, final int position) {

        holder.tvReview.setText(listData.get(position).getReviewComments());
        holder.date.setText(listData.get(position).getDate());
        holder.ratings.setRating(Float.valueOf(listData.get(position).getRatings()));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}

