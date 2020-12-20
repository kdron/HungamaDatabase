package com.kdron.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.kdron.hungamadatabase.R;
import com.kdron.models.Items;
import com.kdron.network.ConnectionUtil;

import java.util.ArrayList;

public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.Holder> {
    private Context context;
    private ArrayList<Items> listItems;


    public ItemListAdapter(ArrayList<Items> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        Holder holder = new Holder(v);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, final int position) {


        holder.txtName.setText(listItems.get(position).name);
        Glide.with(context).
                load(ConnectionUtil.IMG_URL + listItems.get(position).poster)
                .placeholder(R.drawable.ic_place_holder)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgPoster);


    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }


    public class Holder extends RecyclerView.ViewHolder {


        TextView txtName;
        ImageView imgPoster;


        public Holder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            imgPoster = itemView.findViewById(R.id.imgPoster);


        }
    }


}

