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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;

public class MultimedijaItemAdapter extends RecyclerView.Adapter<MultimedijaItemAdapter.ViewHolder> {

    private List<Medij> itemList;
    private Medij sharedItem;
    private int sharedItemPosition;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 8;
    private AppCompatActivity activity;


    public MultimedijaItemAdapter(List<Medij> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MultimedijaItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_multimedija_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MultimedijaItemAdapter.ViewHolder holder, final int position) {

        context = holder.multimedijaLayout.getContext();
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.multimedijaLayout.setBackground(null);
            holder.itemLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));

            holder.naslov.setText("\""+itemList.get(position).getNaslov()+"\"");
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity = (AppCompatActivity) v.getContext();
                    MultimedijaOpsirno multimedijaOpsirno = new MultimedijaOpsirno(itemList.get(position));
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, multimedijaOpsirno).addToBackStack("multimedijaOpsirnoFragment").commit();
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov;
        RelativeLayout multimedijaLayout,itemLayout;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.idNaslov);
            multimedijaLayout=itemView.findViewById(R.id.multimedijaLayout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_multimedija);
            itemLayout=itemView.findViewById(R.id.itemLayout);

        }

    }
    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String naslov = itemList.get(position).getNaslov().toString();
        String sadrzaj = itemList.get(position).getSadrzaj().toString();
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, naslov+"\n\n"+sadrzaj);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}