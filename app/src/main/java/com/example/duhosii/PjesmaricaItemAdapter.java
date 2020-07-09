package com.example.duhosii;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class PjesmaricaItemAdapter extends RecyclerView.Adapter<PjesmaricaItemAdapter.ViewHolder> implements Filterable {

    private List<Pjesma> itemList;
    private List<Pjesma> itemListFull;
    private Pjesma sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;


    public PjesmaricaItemAdapter(List<Pjesma> itemList) {
        this.itemList = itemList;
        itemListFull=new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public PjesmaricaItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_pjesmarica_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PjesmaricaItemAdapter.ViewHolder holder, final int position) {
        context=holder.itemLayout.getContext();

        holder.slikaLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_transition_animation));
        holder.tekstLayout.setAnimation(AnimationUtils.loadAnimation(context,R.anim.scale_transition_animation));

        SpannableString ss = new SpannableString(itemList.get(position).getNaslov() + " - " + itemList.get(position).getBend());
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        ss.setSpan(boldSpan, 0,itemList.get(position).getNaslov().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.naslov.setText(ss);

        if(itemList.get(position).getBend().contains("duhos") || itemList.get(position).getBend().contains("DUHOS") || itemList.get(position).getBend().contains("Duhos")){
            holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_duhos_logo));
        }
        else if(itemList.get(position).getBend().contains("božja pobjeda") || itemList.get(position).getBend().contains("BOŽJA POBJEDA") || itemList.get(position).getBend().contains("Božja pobjeda")){
            holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bozja_pobjeda_logo));
        }
        else {
            holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_trzalica));
        }


        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity = (AppCompatActivity) v.getContext();
                PjesmaOpsirno pjesmaOpsirno=new PjesmaOpsirno(itemList.get(position));
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter,pjesmaOpsirno).addToBackStack("pjesmaOpsirnoFragment").commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public Filter getFilter() {
        return itemFilter;
    }
    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pjesma> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredList.addAll(itemListFull);
            }
            else {
                String filterPattern=constraint.toString().toLowerCase().trim();
                for(Pjesma item:itemListFull){
                    if((item.getNaslov().toLowerCase()+" - "+item.getBend().toLowerCase()).contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            itemList.clear();
            itemList.addAll((List)results.values);
            notifyDataSetChanged();
        }
    };

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov;
        ImageView slika;
        RelativeLayout itemLayout,slikaLayout,tekstLayout;
        ShimmerFrameLayout shimmerFrameLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout);
            naslov = itemView.findViewById(R.id.naslov);
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

        String tekst = itemList.get(position).tekstPjesme.toString();
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