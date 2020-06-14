package com.example.duhosii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<Model> itemList;

    public ItemAdapter(List<Model> itemList) {
        this.itemList=itemList;
    }

    @NonNull
    @Override
    public ItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ViewHolder holder, int position) {
        holder.naslov.setText(itemList.get(position).getNaslov());
        holder.datum.setText(itemList.get(position).getDatum());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov,datum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov=itemView.findViewById(R.id.naslov);
            datum=itemView.findViewById(R.id.datum);
        }
    }
}
