package com.duhos.duhosii;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class PjesmaricaItemAdapter extends RecyclerView.Adapter<PjesmaricaItemAdapter.ViewHolder> implements Filterable {

    private List<Pjesma> itemList;
    private List<Pjesma> itemListFull;
    private Pjesma sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 6;
    private boolean searchFlag=false;

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

        context = holder.itemLayout.getContext();
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.slika.setBackground(null);
            holder.naslov.setBackground(null);

            //holder.slikaLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));
            holder.itemLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_transition_animation));

            SpannableString ss = new SpannableString(itemList.get(position).getNaslov() + " - " + itemList.get(position).getBend());
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            ss.setSpan(boldSpan, 0, itemList.get(position).getNaslov().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.naslov.setText(ss);

            if (itemList.get(position).getBend().toString().toLowerCase().contains("duhos")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.duhos_logo));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("duhos band")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.duhos_logo));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("duhos bend")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.duhos_logo));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("emanuel")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.emanuel));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("fmk")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.fmk));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("kristofori")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.kristofori));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("yeshua")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_yeshuamusicicon));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("božja pobjeda") ) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.bozja_pobjeda));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("hržica")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.hrzica));
            }
            else if (itemList.get(position).getBend().toString().toLowerCase().contains("dom molitve")) {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.dom_molitve_sb));
            }else {
                holder.slika.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_trzalica));
            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity = (AppCompatActivity) v.getContext();
                    PjesmaOpsirno pjesmaOpsirno = new PjesmaOpsirno(itemList.get(position));
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_containter, pjesmaOpsirno).addToBackStack("subFragment").commit();
                    UIUtil.hideKeyboard(activity);
                    searchFlag=true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemList.size();
    }

    @Override
    public Filter getFilter() {
        searchFlag=false;
        return itemFilter;
    }
    private Filter itemFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Pjesma> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0 || searchFlag==true){
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
            if(filteredList.isEmpty()) {
                Toast.makeText(context, "Nema rezultata pretrage", Toast.LENGTH_SHORT).show();
            }


            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
                itemList.clear();
                itemList.addAll((List) results.values);
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

            naslov = itemView.findViewById(R.id.naslov);
            slika = itemView.findViewById(R.id.slika);
            itemLayout=itemView.findViewById(R.id.itemLayout);
            slikaLayout=itemView.findViewById(R.id.slikaLayout);
            tekstLayout=itemView.findViewById(R.id.tekstLayout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_pjesme);


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