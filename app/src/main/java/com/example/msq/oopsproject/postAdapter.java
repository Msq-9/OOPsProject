package com.example.msq.oopsproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder>
{
    public postAdapter(Context mCtx, List<homePagePost> postList) {
        this.mCtx = mCtx;
        this.postList = postList;
    }

    Context mCtx;
    List<homePagePost>postList;

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.post, parent , false);
        postViewHolder PostViewHolder = new postViewHolder(view);


        return PostViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {
    homePagePost p = postList.get(position);
    holder.postBlood.setText(p.getbType());
    holder.postContact.setText(String.valueOf(p.getContact()));
    holder.postLocation.setText(p.getLocation());
    holder.postquantity.setText(String.valueOf(p.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class postViewHolder extends RecyclerView.ViewHolder {
        TextView postBlood, postquantity, postLocation, postContact;

        public postViewHolder(View itemView) {
            super(itemView);

            postBlood = itemView.findViewById(R.id.postBlood);
            postquantity = itemView.findViewById(R.id.postquantity);
            postLocation = itemView.findViewById(R.id.postLocation);
            postContact = itemView.findViewById(R.id.postContact);
        }
    }
}
