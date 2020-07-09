package com.example.duhosii;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class PitanjaItemAdapter extends RecyclerView.Adapter<PitanjaItemAdapter.ViewHolder> {

    private List<Pitanja> itemList;
    private Pitanja sharedItem;
    private int sharedItemPosition;
    private Context context;

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

            context=holder.pitanjeOdgovorLayout.getContext();

            holder.pitanjeOdgovorLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));

            holder.pitanje.setText(itemList.get(position).getPitanje());
            holder.odgovor.setText(itemList.get(position).getOdgovor());

        holder.pitanjeOdgovorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.pitanje.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
                holder.odgovor.setAnimation(AnimationUtils.loadAnimation(context,R.anim.scale_transition_animation));
                if(holder.odgovorLayout.getVisibility()==View.GONE){
                    holder.odgovorLayout.setVisibility(View.VISIBLE);
                    holder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_pitanje_background));
                    holder.pitanjeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.duhosPlava));
                    holder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
                else {
                    holder.odgovorLayout.setVisibility(View.GONE);
                    holder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_shape_shadow));
                    holder.pitanjeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    holder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.duhosPlava));
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

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pitanje, odgovor;
        ImageButton shareButton;
        RelativeLayout pitanjeLayout,odgovorLayout,pitanjeOdgovorLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pitanje = itemView.findViewById(R.id.idPitanje);
            odgovor = itemView.findViewById(R.id.idOdgovor);
            shareButton = itemView.findViewById(R.id.share);
            pitanjeLayout=itemView.findViewById(R.id.pitanjeLayout);
            odgovorLayout=itemView.findViewById(R.id.odgovorLayout);
            pitanjeOdgovorLayout=itemView.findViewById(R.id.pitanje_odgovor_layout);

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