package com.duhos.duhosii;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public int[] slide_images={
            R.drawable.ic_molitva_info,
            R.drawable.ic_kalendar_info,
            R.drawable.ic_novosti_info,
            R.drawable.ic_pitanja_info,
            R.drawable.ic_pjesmarica_info
    };
    public String[] slide_headers={
            "Od srca k Srcu",
            "Kalendar",
            "Novosti",
            "Pitanja",
            "Pjesmarica"
    };


    @Override
    public int getCount() {
        return slide_headers.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        String[] slide_descriptions={
                context.getResources().getString(R.string.odSrcakSrcuInfo),
                context.getResources().getString(R.string.kalendarInfo),
                context.getResources().getString(R.string.novostiInfo),
                context.getResources().getString(R.string.pitanjaInfo),
                context.getResources().getString(R.string.pjesmaricaInfo)
        };
        layoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.slide_layout,container,false);

        ImageView slideImageView=view.findViewById(R.id.infoSlika);
        TextView slideHeaderView=view.findViewById(R.id.infoNaslov);
        TextView slideDescriptionView=view.findViewById(R.id.infoOpis);


        slideImageView.setImageResource(slide_images[position]);
        slideHeaderView.setText(slide_headers[position]);
        slideDescriptionView.setText(slide_descriptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
