package com.kdron.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kdron.hungamadatabase.R;
import com.kdron.models.MovieDetail;
import com.kdron.network.ConnectionUtil;

import java.util.ArrayList;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.Holder> {
    private Context context;
    private ArrayList<MovieDetail> listMovies;


    public MovieListAdapter(ArrayList<MovieDetail> listMovies, Context context) {
        this.listMovies = listMovies;
        this.context = context;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        Holder holder = new Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {


        holder.txtMovieName.setText(listMovies.get(position).title);
        holder.txtReleaseDate.setText(listMovies.get(position).release_date);
        holder.txtInfo.setText("Popularity "+listMovies.get(position).popularity);

        Log.d("1111111", "onBindViewHolder: "+ConnectionUtil.IMG_URL+listMovies.get(position).poster_path);
        Glide.with(context).
                load(ConnectionUtil.IMG_URL+listMovies.get(position).poster_path)
                .placeholder(R.drawable.ic_place_holder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgPoster);

    }

    @Override
    public int getItemCount() {
        return listMovies.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView imgPoster;
        TextView txtMovieName;
        TextView txtReleaseDate;
        TextView txtInfo;

        public Holder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imgPoster = itemView.findViewById(R.id.imgPoster);
            txtMovieName = itemView.findViewById(R.id.txtMovieName);
            txtReleaseDate = itemView.findViewById(R.id.txtReleaseDate);
            txtInfo = itemView.findViewById(R.id.txtInfo);


        }
    }
}

