package com.example.duhosii;

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

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class MolitvaItemAdapter extends RecyclerView.Adapter<MolitvaItemAdapter.ViewHolder> {

    private List<Molitva> itemList;
    private Molitva sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;
    private String izvor;
    boolean showShimer = true;
    int SHIMMER_ITEM_NUMBER = 5;

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
        if(showShimer){
            holder.shimmerFrameLayout.startShimmer();
        } else{
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.slika.setBackground(null);
            holder.datum.setBackground(null);
            holder.naslov.setBackground(null);

            context=holder.itemLayout.getContext();
            if(izvor.equals("OpćeMolitve"))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_opcemolitve));
            if(izvor.equals("Marija"))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_blazenadjevicamarija));
            if(izvor.equals("Nadahnuća"))
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_nadahnuca));
            if(izvor.equals("Pobožnosti"))
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
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,molitvaOpsirno).addToBackStack("molitvaOpsirnoFragment").commit();
            }
        });
        }
    }

    @Override
    public int getItemCount() {
        return showShimer? SHIMMER_ITEM_NUMBER : itemList.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov, datum;
        ImageView slika;
        RelativeLayout itemLayout,slikaLayout,tekstLayout;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
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
        share.putExtra(Intent.EXTRA_TEXT, tekst);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}