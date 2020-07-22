package com.example.duhosii;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.text.TextPaint;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class KalendarItemAdapter extends RecyclerView.Adapter<KalendarItemAdapter.ViewHolder> {

    private List<Dogadjaj> itemList;
    private Dogadjaj sharedItem;
    private int sharedItemPosition;
    private AppCompatActivity activity;
    private Context context;
    boolean showShimmer = true;
    private int SHIMMER_ITEM_NUMBER = 6;
    private int pitanjePosition;
    private boolean pitanjeShow = false;
    private boolean doAnimation=true;

    public KalendarItemAdapter(List<Dogadjaj> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public KalendarItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_kalendar_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final KalendarItemAdapter.ViewHolder holder, final int position) {

        context = holder.itemLayout.getContext();
        if (showShimmer) {
            holder.shimmerFrameLayout.startShimmer();
        } else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);

            holder.naslov.setBackground(null);
            holder.opis.setBackground(null);
            holder.lokacija.setBackground(null);
            holder.datum.setBackground(null);
            holder.dan.setBackground(null);



            String[] parts = itemList.get(position).datum.split("/");
            String dan = parts[0];
            String mjesec = parts[1];

            String dateString=itemList.get(position).datum;
            Date date=null;
            try {
                date =new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

            holder.naslov.setText(itemList.get(position).naslov);
            holder.opis.setText(itemList.get(position).opis);
            holder.lokacija.setText(itemList.get(position).lokacija);
            holder.datum.setText(dan+"/"+mjesec);
            switch (dayOfWeek){
                case (2):
                    holder.dan.setText("Pon");
                    break;
                case (3):
                    holder.dan.setText("Uto");
                    break;
                case (4):
                    holder.dan.setText("Sri");
                    break;
                case (5):
                    holder.dan.setText("Čet");
                    break;
                case (6):
                    holder.dan.setText("Pet");
                    break;
                case (7):
                    holder.dan.setText("Sub");
                    break;
                case (1):
                    holder.dan.setText("Ned");
                    break;
            }

            TextPaint paint = holder.opis.getPaint();
            float width = paint.measureText(holder.opis.getText().toString());

            if (doAnimation)
                holder.itemLayout.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_transition_animation));

            if (pitanjePosition == position && pitanjeShow) {
                holder.isExpanded = true;
                holder.opis.setMaxLines(100000);

                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*5,
                        new int[]{Color.parseColor("#143F61"),Color.parseColor("#143F61")},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.VISIBLE);
                holder.lokacija.setVisibility(View.VISIBLE);
                holder.obavijest.setVisibility(View.VISIBLE);
            } else {
                holder.isExpanded = false;
                holder.opis.setMaxLines(4);
                Shader textShader=new LinearGradient(0, 0,0, holder.opis.getPaint().getTextSize()*5,
                        new int[]{Color.parseColor("#143F61"),Color.TRANSPARENT},
                        new float[]{0, 1}, Shader.TileMode.CLAMP);
                holder.opis.getPaint().setShader(textShader);
                holder.lokacijaText.setVisibility(View.GONE);
                holder.lokacija.setVisibility(View.GONE);
                holder.obavijest.setVisibility(View.GONE);
            }


            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.isExpanded == false) {
                        pitanjePosition = position;
                        pitanjeShow = true;
                        doAnimation = false;
                        notifyDataSetChanged();
                    } else if (holder.isExpanded == true) {
                        pitanjeShow = false;
                        doAnimation = true;
                        notifyDataSetChanged();
                    }


                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return showShimmer ? SHIMMER_ITEM_NUMBER : itemList.size();
    }

    public Dogadjaj getDatum(int position){
        return itemList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView naslov,opis,lokacija,datum,dan,lokacijaText;
        RelativeLayout itemLayout,datumLayout,contentLayout;
        ImageButton obavijest;
        ShimmerFrameLayout shimmerFrameLayout;
        private boolean isExpanded = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            naslov = itemView.findViewById(R.id.naslov);
            opis = itemView.findViewById(R.id.opis);
            lokacija = itemView.findViewById(R.id.lokacija);
            datum = itemView.findViewById(R.id.datum);
            dan = itemView.findViewById(R.id.dan);
            lokacijaText = itemView.findViewById(R.id.lokacija_tekst);
            obavijest=itemView.findViewById(R.id.obavijest);

            itemLayout=itemView.findViewById(R.id.itemLayout);
            datumLayout=itemView.findViewById(R.id.datumLayout);
            contentLayout=itemView.findViewById(R.id.contentLayout);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_layout_kalendar);
        }

    }
    public void shareItem(int position) {
        sharedItem = itemList.get(position);
        sharedItemPosition = position;
        itemList.remove(position);
        notifyItemRemoved(position);
        undoDelete();

        String opis = itemList.get(position).opis.toString();
        String naslov = itemList.get(position).naslov.toString();
        Intent share = new Intent();
        share.setAction(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_TEXT, opis);
        share.putExtra(Intent.EXTRA_SUBJECT, naslov);
        share.setType("text/plain");
        context.startActivity(share.createChooser(share, "Share using"));

    }

    private void undoDelete() {
        itemList.add(sharedItemPosition, sharedItem);
        notifyItemInserted(sharedItemPosition);
    }
}