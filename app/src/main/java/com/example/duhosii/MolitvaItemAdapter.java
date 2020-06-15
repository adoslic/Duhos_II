package com.example.duhosii;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MolitvaItemAdapter extends RecyclerView.Adapter<MolitvaItemAdapter.ViewHolder> {

    private List<Molitva> itemList;

    public MolitvaItemAdapter(List<Molitva> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MolitvaItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MolitvaItemAdapter.ViewHolder holder, final int position) {
        holder.naslov.setText(itemList.get(position).getNaslov());
        holder.datum.setText(itemList.get(position).getDatum());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                MolitvaOpsirno molitvaOpsirno=new MolitvaOpsirno(itemList.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,molitvaOpsirno).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov, datum;
        ImageView slika;
        RelativeLayout itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.naslov);
            datum = itemView.findViewById(R.id.datum);
            slika = itemView.findViewById(R.id.slika);
            itemLayout=itemView.findViewById(R.id.itemLayout);

        }

    }
}