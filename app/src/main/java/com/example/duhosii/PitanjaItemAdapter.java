package com.example.duhosii;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class PitanjaItemAdapter extends RecyclerView.Adapter<PitanjaItemAdapter.ViewHolder> {

    private List<Pitanja> itemList;
    private Pitanja sharedItem;
    private int sharedItemPosition;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 8;

    private int pitanjePosition;
    private boolean pitanjeShow = false;
    private boolean doAnimation=true;
    public PitanjaItemAdapter(List<Pitanja> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public PitanjaItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_pitanja_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PitanjaItemAdapter.ViewHolder holder, final int position) {

            context = holder.pitanjeOdgovorLayout.getContext();
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.pitanje.setBackground(null);
            holder.odgovor.setBackground(null);

            holder.pitanje.setText(itemList.get(position).getPitanje());
            holder.odgovor.setText(itemList.get(position).getOdgovor());

            if (doAnimation)
                holder.pitanjeOdgovorLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));

            if (pitanjePosition == position && pitanjeShow) {
                holder.pitanjeLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));
                holder.odgovorLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));
                holder.isExpanded = true;
                holder.odgovorLayout.setVisibility(View.VISIBLE);
                holder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_pitanje_background));
                holder.pitanjeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_pitanje_background));
                holder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.white));
            } else {
                holder.isExpanded = false;
                holder.odgovorLayout.setVisibility(View.GONE);
                holder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_shape_shadow_small_radius));
                holder.pitanjeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                holder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.duhosPlava));
            }

            holder.pitanjeOdgovorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isExpanded == false) {
                        pitanjePosition = position;
                        pitanjeShow = true;
                        doAnimation = false;
                        notifyDataSetChanged();
                    } else if (holder.isExpanded == true) {
                        pitanjeShow = false;
                        doAnimation = false;
                        notifyDataSetChanged();
                    }
                }
            });

            holder.shareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shareItem(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pitanje, odgovor;
        ImageButton shareButton;
        RelativeLayout pitanjeLayout,odgovorLayout,pitanjeOdgovorLayout;
        ShimmerFrameLayout shimmerFrameLayout;
        private boolean isExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pitanje = itemView.findViewById(R.id.idPitanje);
            odgovor = itemView.findViewById(R.id.idOdgovor);
            shareButton = itemView.findViewById(R.id.share);
            pitanjeLayout=itemView.findViewById(R.id.pitanjeLayout);
            odgovorLayout=itemView.findViewById(R.id.odgovorLayout);
            pitanjeOdgovorLayout=itemView.findViewById(R.id.pitanje_odgovor_layout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_pitanja);

        }

    }
    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String odgovor = itemList.get(position).getOdgovor().toString();
        String pitanje = itemList.get(position).getPitanje().toString();
        String tekst = pitanje+"\n\n"+odgovor;
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, tekst);
        share.putExtra(Intent.EXTRA_SUBJECT, "Odgovor kapelana");
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}