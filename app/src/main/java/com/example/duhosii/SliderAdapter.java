package com.example.duhosii;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import org.w3c.dom.Text;

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
            "Molitva",
            "Kalendar",
            "Novosti",
            "Pitanja",
            "Pjesmarica"
    };
    public String[] slide_descriptions={
            "Ovaj dio aplikacije namijenjen je za tvoj duhovni rast. Moli zajedno s nama i dijeli molitve koje ti se sviđaju pomoću swipe-a. Slobodno nam pošalji svoju molitvenu nakanu klikom na + i molit ćemo zajedno s tobom na sljedećem euharistijskom klanjanju.",
            "Budući da Udruga organizira puno aktivnosti, u ovom dijelu aplikacije jednostavno i brzo prati svaki događaj. Za događaje koje bi htio posjetiti omogućili smo ti postavljanje obavijesti",
            "U novostima prati naše objave na društvenim mrežama sve na jednom mjestu. Informirat ćemo te i o novostima koje dolaze izvan naše udruge",
            "Ako imaš neko pitanje, a nemaš svećenika u blizini da ti odgovori, slobodno pošalji pitanje našim kapelanima klikom na +. Pitanje možeš postaviti i anonimno, a odgovor ćemo objaviti u našoj aplikaciji",
            "Sa mnoštvom pjesama naša pjesmarica ti omogućuje lako pronalaženje tekstova i akorda za svoje potrebe. Pomoću swipe-a jednostavno podijeli tekst putem društvenih mreža",
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
