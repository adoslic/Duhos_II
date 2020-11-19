package com.duhos.duhosii.questions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.duhos.duhosii.R;
import com.duhos.duhosii.models.Question;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.List;


public class QuestionsItemAdapter extends RecyclerView.Adapter<QuestionsItemAdapter.ViewHolder> {

    private List<Question> itemList;
    private Question sharedItem;
    private int sharedItemPosition;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 8;

    private int pitanjePosition;
    private boolean pitanjeShow = false;
    QuestionsItemAdapter.ViewHolder myHolder;
    private boolean doAnimation = true;

    public QuestionsItemAdapter(List<Question> itemList) {
        this.itemList = itemList;
    }

    RecyclerView mRecyclerView;


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;
    }

    @NonNull
    @Override
    public QuestionsItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_pitanja_item, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final QuestionsItemAdapter.ViewHolder holder, final int position) {
        myHolder = holder;
        context = myHolder.pitanjeOdgovorLayout.getContext();

        if (showShimmer) {
            myHolder.shimmerFrameLayout.startShimmer();
        } else {

            if (doAnimation)
                myHolder.pitanjeOdgovorLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));

            myHolder.shimmerFrameLayout.stopShimmer();
            myHolder.shimmerFrameLayout.setShimmer(null);

            myHolder.pitanje.setBackground(null);
            myHolder.odgovor.setBackground(null);

            myHolder.pitanje.setText(itemList.get(position).getPitanje());
            myHolder.odgovor.setText(itemList.get(position).getOdgovor());

            if (pitanjePosition == position && pitanjeShow) {
                myHolder.isExpanded = true;
                myHolder.odgovorLayout.setVisibility(View.VISIBLE);
                myHolder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_pitanje_background));
                myHolder.pitanjeLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.clicked_pitanje_background));
                myHolder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.white));
                Typeface firaSansItalic = Typeface.createFromAsset(context.getAssets(), "fonts/firasans_semibolditalic.ttf");
                myHolder.pitanje.setTypeface(firaSansItalic);
            } else {
                myHolder.isExpanded = false;
                myHolder.odgovorLayout.setVisibility(View.GONE);
                myHolder.pitanjeOdgovorLayout.setBackground(ContextCompat.getDrawable(context, R.drawable.rectangle_shape_shadow_small_radius));
                myHolder.pitanjeLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                myHolder.pitanje.setTextColor(ContextCompat.getColor(context, R.color.duhosPlava));
                Typeface firaSansSemiBold = Typeface.createFromAsset(context.getAssets(), "fonts/firasans_semibold.ttf");
                myHolder.pitanje.setTypeface(firaSansSemiBold);
            }

            myHolder.pitanjeOdgovorLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!myHolder.isExpanded) {
                        pitanjePosition = position;
                        pitanjeShow = true;
                        doAnimation = false;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(position, 0);
                        notifyDataSetChanged();
                    } else {
                        pitanjeShow = false;
                        doAnimation = false;
                        notifyDataSetChanged();
                    }
                }
            });

            myHolder.odgovor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!pitanjeShow) {
                        pitanjePosition = position;
                        pitanjeShow = true;
                        doAnimation = false;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(position, 0);
                        notifyDataSetChanged();
                    } else {
                        pitanjeShow = false;
                        doAnimation = false;
                        LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        layoutManager.scrollToPositionWithOffset(pitanjePosition, 0);
                        notifyDataSetChanged();
                    }
                }
            });

            myHolder.shareButton.setOnClickListener(new View.OnClickListener() {
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

    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String odgovor = itemList.get(position).getOdgovor().toString();
        String pitanje = itemList.get(position).getPitanje().toString();
        String tekst = pitanje + "\n\n" + odgovor;
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, tekst);
        share.putExtra(Intent.EXTRA_SUBJECT, "Odgovor kapelana");
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    public boolean checkHolder() {
        if (pitanjeShow) {
            myHolder.odgovor.performClick();
            return true;
        } else return false;
    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView pitanje, odgovor;
        ImageButton shareButton;
        RelativeLayout pitanjeLayout, odgovorLayout, pitanjeOdgovorLayout;
        ShimmerFrameLayout shimmerFrameLayout;
        private boolean isExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pitanje = itemView.findViewById(R.id.idPitanje);
            odgovor = itemView.findViewById(R.id.idOdgovor);
            shareButton = itemView.findViewById(R.id.share);
            pitanjeLayout = itemView.findViewById(R.id.pitanjeLayout);
            odgovorLayout = itemView.findViewById(R.id.odgovorLayout);
            pitanjeOdgovorLayout = itemView.findViewById(R.id.pitanje_odgovor_layout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_pitanja);

        }

    }
}