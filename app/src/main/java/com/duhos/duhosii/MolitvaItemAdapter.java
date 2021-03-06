package com.duhos.duhosii;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MolitvaItemAdapter extends RecyclerView.Adapter<MolitvaItemAdapter.ViewHolder> {

    private List<Molitva> itemList;
    private Molitva sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;
    private String izvor;

    public MolitvaItemAdapter(List<Molitva> itemList, String izvor) {
        this.itemList = itemList;
        this.izvor=izvor;
    }

    @NonNull
    @Override
    public MolitvaItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_molitva_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MolitvaItemAdapter.ViewHolder holder, final int position) {

            context=holder.itemLayout.getContext();
            if(izvor.equals(context.getResources().getString(R.string.molitvaIpozonostiString)))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_opcemolitve));
            if(izvor.equals(context.getResources().getString(R.string.marijaString)))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_blazenadjevicamarija));
            if(izvor.equals(context.getResources().getString(R.string.nadahnucaString)))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nadahnuca));
            if(izvor.equals(context.getResources().getString(R.string.svjedocanstvaString)))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_poboznosti));

            holder.slikaLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
            holder.tekstLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.scale_transition_animation));

            holder.naslov.setText(itemList.get(position).getNaslov());

            holder.datum.setText(itemList.get(position).getDatum());

        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                MolitvaOpsirno molitvaOpsirno=new MolitvaOpsirno(itemList.get(position),izvor);
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,molitvaOpsirno).addToBackStack("").commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov, datum;
        ImageView slika;
        RelativeLayout itemLayout,slikaLayout,tekstLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.naslov);
            datum = itemView.findViewById(R.id.datum);
            slika = itemView.findViewById(R.id.slika);
            itemLayout=itemView.findViewById(R.id.itemLayout);
            slikaLayout=itemView.findViewById(R.id.slikaLayout);
            tekstLayout=itemView.findViewById(R.id.tekstLayout);

        }

    }
    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String tekst = itemList.get(position).tekst.toString();
        String naslov = itemList.get(position).naslov.toString();
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov+"\n\n"+tekst);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}